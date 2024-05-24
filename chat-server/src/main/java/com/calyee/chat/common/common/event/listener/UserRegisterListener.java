package com.calyee.chat.common.common.event.listener;

import com.calyee.chat.common.common.domain.enums.IdempotentEnum;
import com.calyee.chat.common.common.event.UserRegisterEvent;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.enums.ItemEnum;
import com.calyee.chat.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.event.listener
 * @className: UserRegisterListener
 * @author: Calyee
 * @description: 用户注册监听
 * @date: 2024/03/14 014 19:08
 * @version: 1.0
 */
@Component
public class UserRegisterListener {
    @Autowired
    private IUserBackpackService userBackpackService;
    @Autowired
    private UserDao userDao;
    /**
     * 发送改名卡
     *
     * @param userRegisterEvent 注册事件
     */
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT) // 事务提交后
    public void sendCard(UserRegisterEvent userRegisterEvent) {
        User user = userRegisterEvent.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID, user.getId().toString());

        //TODO 注册成功需要写入contact表

        // TODO 谁拉的就是谁的裙主  需要在group_m写入相对应的身份信息
    }
    /**
     * 发送 前10名/100名 注册的徽章
     */
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendBadge(UserRegisterEvent userRegisterEvent) {
        User user = userRegisterEvent.getUser();
        int registeredUserCount = userDao.count();
        if(registeredUserCount < 10){
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID, user.getId().toString());
        }else if(registeredUserCount <100){
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID, user.getId().toString());
        }
    }
}
