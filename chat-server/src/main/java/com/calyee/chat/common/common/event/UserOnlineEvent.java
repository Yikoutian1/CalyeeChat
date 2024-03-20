package com.calyee.chat.common.common.event;

import com.calyee.chat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.event
 * @className: UserOnlineEvent
 * @author: Calyee
 * @description: 用户上线的事件
 * @date: 2024/03/20 020 22:25
 * @version: 1.0
 */

@Getter
public class UserOnlineEvent extends ApplicationEvent {

    private User user;
    public UserOnlineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
