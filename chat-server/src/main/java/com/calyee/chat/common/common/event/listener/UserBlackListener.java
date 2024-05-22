package com.calyee.chat.common.common.event.listener;

import com.calyee.chat.common.chat.service.adapter.websocketAdapter;
import com.calyee.chat.common.common.event.UserBlackEvent;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.service.cache.UserCache;
import com.calyee.chat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
public class UserBlackListener {

    //    @Autowired
//    private MessageDao messageDao;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserCache userCache;

    @Autowired
    private UserDao userDao;

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    public void sendMSGToAll(UserBlackEvent event) {
        User user = event.getUser();
        webSocketService.sendMsgToAll(websocketAdapter.buildResp(user));
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void changeUserStatus(UserBlackEvent event) {
        User user = event.getUser();
        userDao.invalidUid(user.getId());
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void eviCache(UserBlackEvent event) {
        userCache.evictBlackMap();
    }
//    @Async
//    @EventListener(classes = UserBlackEvent.class)
//    public void refreshRedis(UserBlackEvent event) {
//        UserCache.evictBlackMap();
//        UserCache.remove(event.getUser().getId());
//    }

//    @Async
//    @EventListener(classes = UserBlackEvent.class)
//    public void deleteMsg(UserBlackEvent event) {
//        messageDao.invalidByUid(event.getUser().getId());
//    }
//
//    @Async
//    @EventListener(classes = UserBlackEvent.class)
//    public void sendPush(UserBlackEvent event) {
//        Long uid = event.getUser().getId();
//        WSBaseResp<WSBlack> resp = new WSBaseResp<>();
//        WSBlack black = new WSBlack(uid);
//        resp.setData(black);
//        resp.setType(WSRespTypeEnum.BLACK.getType());
//        webSocketService.sendToAllOnline(resp, uid);
//    }

}
