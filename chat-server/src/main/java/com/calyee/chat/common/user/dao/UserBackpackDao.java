package com.calyee.chat.common.user.dao;

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

    public Integer getCountByValidItemId(Long uid, Long itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
    }

    public UserBackpack getFirstValidItem(Long uid, Long itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .orderByAsc(UserBackpack::getId) // 获取最老的一张
                .last("limit 1")
                .one();
    }

    public boolean userItem(UserBackpack item) {
        // 此锁为数据库级别的锁 每次修改都需要判断是否已经修改过才能修改
        return lambdaUpdate()
                .eq(UserBackpack::getId, item.getId()) // 必须是待使用的改名卡id
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO)
                .set(UserBackpack::getStatus, YesOrNoEnum.YES)// 设置为使用
                .update();

    }

    public List<UserBackpack> getByItemId(Long uid, List<Long> itemIds) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus()) // 有效的徽章
                .in(UserBackpack::getItemId, itemIds)
                .list();
    }
}
