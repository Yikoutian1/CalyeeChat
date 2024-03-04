package com.calyee.chat.common.user.service;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service
 * @className: LoginService
 * @author: Caluee
 * @description: 登录模块
 * @date: 2024/03/02 002 20:33
 * @version: 1.0
 */

public interface LoginService {
    /**
     * 登录成功，获取token
     *
     * @param uid
     * @return 返回token
     */
    String login(Long uid);

    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 如果token有效，返回uid
     *
     * @param token
     * @return
     */
    Long getValidUid(String token);
}
