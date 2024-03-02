package com.calyee.chat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/yikoutian1">calyeechat</a>
 * @since 2024-02-28
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public User getByOpenId(String openId) {
        return lambdaQuery()
                .eq(openId != null, User::getOpenId, openId)
                .one();
    }
}
