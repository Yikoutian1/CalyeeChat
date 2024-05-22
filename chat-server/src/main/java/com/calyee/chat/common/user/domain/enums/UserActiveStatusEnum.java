package com.calyee.chat.common.user.domain.enums;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Description:
 * Author:戏中言
 *
 * @date: 2024/4/4 15:58
 */


@Getter
@AllArgsConstructor
public enum UserActiveStatusEnum {
    ONLINE(1,"在线"),
    OFFLINE(2,"下线");
    private final Integer status;
    private final String  desc;
}
