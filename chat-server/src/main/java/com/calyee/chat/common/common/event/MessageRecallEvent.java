package com.calyee.chat.common.common.event;

import com.calyee.chat.common.chat.domain.dto.ChatMsgRecallDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageRecallEvent extends ApplicationEvent {

    private final ChatMsgRecallDTO recallDTO;

    public MessageRecallEvent(Object source, ChatMsgRecallDTO recallDTO) {
        super(source);
        this.recallDTO = recallDTO;
    }

}
