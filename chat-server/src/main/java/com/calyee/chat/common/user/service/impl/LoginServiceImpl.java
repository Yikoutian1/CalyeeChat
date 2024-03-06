package com.calyee.chat.common.user.service.impl;

import com.calyee.chat.common.common.constant.RedisKey;
import com.calyee.chat.common.common.utils.JwtUtils;
import com.calyee.chat.common.common.utils.RedisUtils;
import com.calyee.chat.common.user.service.LoginService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service.impl
 * @className: LoginServiceImpl
 * @author: Caluee
 * @description: 登录模块实现类
 * @date: 2024/03/02 002 20:33
 * @version: 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {

    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEW_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        String redisKey = getUserTokenKey(uid);
        RedisUtils.set(redisKey, token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }


    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        // 异步刷新
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);// 我们当前使用Day作为界限
        if (expireDays == -2) { // 不存在的key，直接拿结论-2来比较即可
            return;
        }
        if (expireDays < TOKEN_RENEW_DAYS) {// 小于一天则直接续期
            RedisUtils.expire(getUserTokenKey(uid), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }

    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return null;
        }
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if (StringUtils.isBlank(oldToken)) {
            return null; // 没有获取到token
        }
        return Objects.equals(oldToken, token) ? uid : null; // 如果旧的token和新的token一致才能返回
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }
}
