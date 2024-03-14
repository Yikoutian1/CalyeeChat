package com.calyee.chat.common.common.event;

import com.calyee.chat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.event
 * @className: UserRegisterEvent
 * @author: Calyee
 * @description: 用户注册事件
 * @date: 2024/03/14 014 19:05
 * @version: 1.0
 */

@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private User user;

    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
