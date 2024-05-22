package com.calyee.chat.common.common.event.listener;


import com.calyee.chat.common.chat.domain.dto.ChatMsgRecallDTO;
import com.calyee.chat.common.chat.service.ChatService;
import com.calyee.chat.common.chat.service.cache.MsgCache;
import com.calyee.chat.common.common.event.MessageRecallEvent;
import com.calyee.chat.common.user.service.adapter.WSAdapter;
import com.calyee.chat.common.user.service.impl.PushService;
import com.calyee.chat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 消息撤回监听器
 */
@Slf4j
@Component
public class MessageRecallListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MsgCache msgCache;
    @Autowired
    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void evictMsg(MessageRecallEvent event) {
        ChatMsgRecallDTO recallDTO = event.getRecallDTO();
        msgCache.evictMsg(recallDTO.getMsgId());
    }

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void sendToAll(MessageRecallEvent event) {
        pushService.sendPushMsg(WSAdapter.buildMsgRecall(event.getRecallDTO()));
    }

}
