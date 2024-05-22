package com.calyee.chat.common.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.calyee.chat.common.common.annotation.RedissonLock;
import com.calyee.chat.common.common.event.UserBlackEvent;
import com.calyee.chat.common.common.event.UserRegisterEvent;
import com.calyee.chat.common.common.utils.AssertUtil;
import com.calyee.chat.common.user.dao.BlackDao;
import com.calyee.chat.common.user.dao.ItemConfigDao;
import com.calyee.chat.common.user.dao.UserBackpackDao;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.dto.ItemInfoDTO;
import com.calyee.chat.common.user.domain.dto.SummeryInfoDTO;
import com.calyee.chat.common.user.domain.entity.*;
import com.calyee.chat.common.user.domain.enums.BlackTypeEnum;
import com.calyee.chat.common.user.domain.enums.ItemEnum;
import com.calyee.chat.common.user.domain.enums.ItemTypeEnum;
import com.calyee.chat.common.user.domain.vo.req.user.BlackReq;
import com.calyee.chat.common.user.domain.vo.req.user.ItemInfoReq;
import com.calyee.chat.common.user.domain.vo.req.user.ModifyNameReq;
import com.calyee.chat.common.user.domain.vo.req.user.SummeryInfoReq;
import com.calyee.chat.common.user.domain.vo.resp.BadgesResp;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import com.calyee.chat.common.user.service.UserService;
import com.calyee.chat.common.user.service.adapter.UserAdapter;
import com.calyee.chat.common.user.service.cache.ItemCache;
import com.calyee.chat.common.user.service.cache.UserCache;
import com.calyee.chat.common.user.service.cache.UserSummaryCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service.impl
 * @className: UserServiceImpl
 * @author: Caluee
 * @description: 用户服务实现类
 * @date: 2024/03/02 002 19:38
 * @version: 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCache usercache;
    @Autowired
    private BlackDao blackDao;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    private ItemConfigDao itemConfigDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserSummaryCache userSummaryCache;

    @Transactional
    @Override
    public Long register(User insert) {
        userDao.save(insert);
        // 用户注册的事件
        // 用户注册的事件  -> 谁订阅就给谁发送通知 （1.Mq  2.ApplicationEventPublisher）

        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, insert)); // this事件订阅者，发送端
        return insert.getId();
    }

    @Override
    public UserInfoResp getUserinfo(Long uid) {
        User user = userDao.getById(uid);
        Integer modifyNameCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user, modifyNameCount);
    }


    /**
     * 修改名字
     *
     * @param uid
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, ModifyNameReq req) {
        User olduser = userDao.getByName(req.getName());
        AssertUtil.isEmpty(olduser, "名字已经被抢占了，请换一个");
//        if(Objects.nonNull(olduser)){
//            throw new BusinessException("名字重复了");
//        }

        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "改名卡不够了，等后续活动领取");
        // 使用改名卡
        boolean success = userBackpackDao.userItem(modifyNameItem);
        if (success) {
            //改名
            boolean ismodifynamesuccess = userDao.modifyName(uid, req.getName());
        }
    }


    @Override
    public List<BadgesResp> badges(Long uid) {
        //查询所有徽章
        List<ItemConfig> itemconfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        //查询用户拥有的徽章
        List<Long> collect = itemconfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, collect);
        //查询用户佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemconfigs, backpacks, user);
    }


    @Override
    public void wearingBadge(Long uid, Long itemId) {
        // 确保有徽章
        UserBackpack validItem = userBackpackDao.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(validItem, "您还没有获得该徽章，快去获得吧"); // 没有徽章
        // 确保这个物品是徽章
        ItemConfig itemConfig = itemConfigDao.getById(validItem.getItemId());
        AssertUtil.equal(ItemTypeEnum.BADGE.getType(), itemConfig.getType(), "只有徽章才能佩戴哦");
        // 判断完上面的
        userDao.wearingBadge(uid, itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black user = new Black();
        user.setType(BlackTypeEnum.UID.getType());
        user.setTarget(uid.toString());
        blackDao.save(user);
        User byId = userDao.getById(uid);
        blackIP(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getCreateIp).orElse(null));
        blackIP(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getUpdateIp).orElse(null));
        applicationEventPublisher.publishEvent(new UserBlackEvent(this, byId));

    }


    @Override
    public List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req) {
        //需要前端同步的uid
        List<Long> uidList = getNeedSyncUidList(req.getReqList());
        //加载用户信息
        Map<Long, SummeryInfoDTO> batch = userSummaryCache.getBatch(uidList);
        return req.getReqList()
                .stream()
                .map(a -> batch.containsKey(a.getUid()) ? batch.get(a.getUid()) : SummeryInfoDTO.skip(a.getUid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemInfoDTO> getItemInfo(ItemInfoReq req) {//简单做，更新时间可判断被修改
        return req.getReqList().stream().map(a -> {
            ItemConfig itemConfig = itemCache.getById(a.getItemId());
            if (Objects.nonNull(a.getLastModifyTime()) && a.getLastModifyTime() >= itemConfig.getUpdateTime().getTime()) {
                return ItemInfoDTO.skip(a.getItemId());
            }
            ItemInfoDTO dto = new ItemInfoDTO();
            dto.setItemId(itemConfig.getId());
            dto.setImg(itemConfig.getImg());
            dto.setDescribe(itemConfig.getDescribe());
            return dto;
        }).collect(Collectors.toList());
    }


    private List<Long> getNeedSyncUidList(List<SummeryInfoReq.infoReq> reqList) {
        List<Long> needSyncUidList = new ArrayList<>();
        List<Long> userModifyTime = usercache.getUserModifyTime(reqList.stream().map(SummeryInfoReq.infoReq::getUid).collect(Collectors.toList()));
        for (int i = 0; i < reqList.size(); i++) {
            SummeryInfoReq.infoReq infoReq = reqList.get(i);
            Long modifyTime = userModifyTime.get(i);
            if (Objects.isNull(infoReq.getLastModifyTime()) || (Objects.nonNull(modifyTime) && modifyTime > infoReq.getLastModifyTime())) {
                needSyncUidList.add(infoReq.getUid());
            }
        }
        return needSyncUidList;
    }


    private void blackIP(String Ip) {
        if (StringUtils.isBlank(Ip)) {
            return;
        }//如果Ip是空的直接返回
        try {
            Black insert = new Black();
            insert.setType(BlackTypeEnum.IP.getType());
            insert.setTarget(Ip);
            blackDao.save(insert);
        } catch (Exception e) {
            log.error("duplicate black ip:{}", Ip);
        }
    }

}
