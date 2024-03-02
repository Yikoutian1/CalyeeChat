package com.calyee.chat.common.user.service.impl;

import com.calyee.chat.common.user.service.LoginService;
import org.springframework.stereotype.Service;

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

    @Override
    public String login(Long uid) {
        // TODO token保存 -> redis
        return null;
    }
}
