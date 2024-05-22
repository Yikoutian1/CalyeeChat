package com.calyee.chat.common.common.event.listener;


import com.calyee.chat.common.chat.dao.ContactDao;
import com.calyee.chat.common.chat.dao.MessageDao;
import com.calyee.chat.common.chat.dao.RoomDao;
import com.calyee.chat.common.chat.dao.RoomFriendDao;
import com.calyee.chat.common.chat.domain.entity.Room;
import com.calyee.chat.common.chat.domain.enums.HotFlagEnum;
import com.calyee.chat.common.chat.service.ChatService;
import com.calyee.chat.common.chat.service.cache.GroupMemberCache;
import com.calyee.chat.common.chat.service.cache.HotRoomCache;
import com.calyee.chat.common.chat.service.cache.RoomCache;
import com.calyee.chat.common.common.constant.MQConstant;
import com.calyee.chat.common.common.domain.dto.MsgSendMessageDTO;
import com.calyee.chat.common.common.event.MessageSendEvent;
import com.calyee.chat.common.user.service.cache.UserCache;
import com.calyee.chat.common.websocket.service.WebSocketService;
import com.calyee.chat.transaction.service.MQProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

/**
 * 消息发送监听器
 */
@Slf4j
@Component
public class MessageSendListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageDao messageDao;
    //    @Autowired
//    private IChatAIService openAIService;
//    @Autowired
//    WeChatMsgOperationService weChatMsgOperationService;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private UserCache userCache;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private HotRoomCache hotRoomCache;
    @Autowired
    private MQProducer mqProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        mqProducer.sendSecureMsg(MQConstant.SEND_MSG_TOPIC, new MsgSendMessageDTO(msgId), msgId);
    }
//这里开启了chatgpt 只有热点群聊才会有这个
//    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
//    public void handlerMsg(@NotNull MessageSendEvent event) {
//        Message message = messageDao.getById(event.getMsgId());
//        Room room = roomCache.get(message.getRoomId());
//        if (isHotRoom(room)) {
//            openAIService.chat(message);
//        }
//    }

    public boolean isHotRoom(Room room) {
        return Objects.equals(HotFlagEnum.YES.getType(), room.getHotFlag());
    }

//    /**
//     * 给用户微信推送艾特好友的消息通知
//     * （这个没开启，微信不让推）
//     */
//    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
//    public void publishChatToWechat(@NotNull MessageSendEvent event) {
//        Message message = messageDao.getById(event.getMsgId());
//        if (Objects.nonNull(message.getExtra().getAtUidList())) {
//            weChatMsgOperationService.publishChatMsgToWeChatUser(message.getFromUid(), message.getExtra().getAtUidList(),
//                    message.getContent());
//        }
//    }
}
