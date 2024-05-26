package com.calyee.chat.common.common.event.listener;


import com.calyee.chat.common.common.event.UserApplyEvent;
import com.calyee.chat.common.user.dao.UserApplyDao;
import com.calyee.chat.common.user.domain.entity.UserApply;
import com.calyee.chat.common.user.service.adapter.WSAdapter;
import com.calyee.chat.common.user.service.impl.PushService;
import com.calyee.chat.common.websocket.domain.vo.resp.WSFriendApply;
import com.calyee.chat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 好友申请监听器
 */
@Slf4j
@Component
public class UserApplyListener {
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = UserApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(UserApplyEvent event) {
        UserApply userApply = event.getUserApply();
        Integer unReadCount = userApplyDao.getUnReadCount(userApply.getTargetId());
        pushService.sendPushMsg(WSAdapter.buildApplySend(new WSFriendApply(userApply.getUid(), unReadCount)), userApply.getTargetId());
    }

}
