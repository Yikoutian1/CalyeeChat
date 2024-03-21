package com.calyee.chat.common.common.event.listener;

import com.calyee.chat.common.common.event.UserOnlineEvent;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.enums.UserActiveStatusEnum;
import com.calyee.chat.common.user.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.event.listener
 * @className: UserOnlineListener
 * @author: Calyee
 * @description: 用户在线监听
 * @date: 2024/03/20 020 22:28
 * @version: 1.0
 */

@Component
public class UserOnlineListener {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IpService ipService;

    @Async
    @TransactionalEventListener(classes = UserOnlineEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    // 如果监听前没有事务，则需要额外配置fallbackExecution：Whether the event should be handled if no transaction is running.
    public void saveDB(UserOnlineEvent userOnlineEvent) {
        User user = userOnlineEvent.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        // 用户ip详情更新 串行解析 不能再多添加一个监听
        ipService.refreshIpDetailAsync(user.getId());
    }
}
