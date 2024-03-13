package com.calyee.chat.common.user.service.impl;

import com.calyee.chat.common.common.utils.AssertUtil;
import com.calyee.chat.common.user.dao.ItemConfigDao;
import com.calyee.chat.common.user.dao.UserBackpackDao;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.ItemConfig;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.entity.UserBackpack;
import com.calyee.chat.common.user.domain.enums.ItemEnum;
import com.calyee.chat.common.user.domain.enums.ItemTypeEnum;
import com.calyee.chat.common.user.domain.vo.resp.BadgesResp;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import com.calyee.chat.common.user.service.UserService;
import com.calyee.chat.common.user.service.adapter.UserAdapter;
import com.calyee.chat.common.user.service.cache.ItemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    private ItemConfigDao itemConfigDao;
    @Autowired
    private ItemCache itemCache;


    @Override
    @Transactional // 保证事件和用户注册的一致性
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        // TODO 用户注册的事件  -> 谁订阅就给谁发送通知
        return insert.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer modifyNameCardCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId()); // 获取用户改名卡个数
        return UserAdapter.buildUserInfo(user, modifyNameCardCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        // 断言工具 业务校验工具类
        AssertUtil.isEmpty(oldUser, "名字已经被抢占了，换个名字吧");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "改名卡不够了，等后续活动领取");
        // 使用改名卡
        boolean success = userBackpackDao.userItem(modifyNameItem);
        if (success) {
            // 使用成功则可以改名字了
            boolean successUpdateName = userDao.modifyName(uid, name);
        }
    }

    @Override
    public List<BadgesResp> getBadges(Long uid) {
        // 获取所有的徽章列表（不经常变化则存本地缓存） 详情追踪进去
        List<ItemConfig> badges = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        // 查询用户拥有的徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemId(uid, badges.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        // 查询用户佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(badges, backpacks, user);
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

}
