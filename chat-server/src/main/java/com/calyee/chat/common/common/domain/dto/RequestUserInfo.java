package com.calyee.chat.common.common.domain.dto;

import lombok.Data;

/**
 * @description: 请求上下文用户请求信息对象
 * @date: 2024/03/10 010 20:56
 * @version: 1.0
 */
@Data
public class RequestUserInfo {
    private Long uid;
    private String ip;
}
