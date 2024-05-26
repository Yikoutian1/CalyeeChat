package com.calyee.chat.common.user.domain.vo.req.ws;

import lombok.Data;

/**
 * Description: websocket前端请求体
 */
@Data
public class WSBaseReq {
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}