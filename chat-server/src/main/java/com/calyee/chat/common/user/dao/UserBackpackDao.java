package com.calyee.chat.common.user.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.common.domain.enums.YesOrNoEnum;
import com.calyee.chat.common.user.domain.entity.UserBackpack;
import com.calyee.chat.common.user.mapper.UserBackpackMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/yikoutian1">calyeechat</a>
 * @since 2024-03-06
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> {

    public boolean userItem(UserBackpack modifyNameItem) {
        // 此锁为数据库级别的锁 每次修改都需要判断是否已经修改过才能修改
        //数据库锁又叫逻辑锁
        return lambdaUpdate()
                .eq(UserBackpack::getId, modifyNameItem.getId()) // 必须是待使用的改名卡id
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .set(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus())// 设置为使用
                .update();
    }
    public UserBackpack getByIdempotent(String idempotent) {
        return lambdaQuery()
                .eq(UserBackpack::getIdempotent, idempotent)
                .one();
    }
    public Integer getCountByValidItemId(Long uid, Long itemId) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
    }

    public UserBackpack getFirstValidItem(Long uid, Long itemId) {
        LambdaQueryWrapper<UserBackpack> wrapper = new QueryWrapper<UserBackpack>().lambda()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .last("limit 1");
        return getOne(wrapper);
    }

    public boolean invalidItem(Long id) {
        UserBackpack update = new UserBackpack();
        update.setId(id);
        update.setStatus(YesOrNoEnum.YES.getStatus());
        return updateById(update);
    }

    public List<UserBackpack> getByItemIds(Long uid, List<Long> itemIds) {
        return lambdaQuery().eq(UserBackpack::getUid, uid)
                .in(UserBackpack::getItemId, itemIds)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .list();
    }

    public List<UserBackpack> getByItemIds(List<Long> uids, List<Long> itemIds) {
        return lambdaQuery().in(UserBackpack::getUid, uids)
                .in(UserBackpack::getItemId, itemIds)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .list();
    }

    public UserBackpack getByIdp(String idempotent) {
        return lambdaQuery().eq(UserBackpack::getIdempotent, idempotent).one();
    }
    

}
