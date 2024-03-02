package com.calyee.chat.common.user.service.impl;

import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional // 保证事件和用户注册的一致性
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        // TODO 用户注册的事件  -> 谁订阅就给谁发送通知
        return insert.getId();
    }
}
