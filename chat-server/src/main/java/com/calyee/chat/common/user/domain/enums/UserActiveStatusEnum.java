package com.calyee.chat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.domain.enums
 * @className: UserActiveEnum
 * @author: Calyee
 * @description: 用户是否在线 枚举
 * @date: 2024/03/20 020 22:46
 * @version: 1.0
 */

@Getter
@AllArgsConstructor
public enum UserActiveStatusEnum {
    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    ;

    private final Integer status;
    private final String desc;
}
