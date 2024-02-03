package com.calyee.chat.common.websocket.domain.vo.req;

import lombok.Data;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.websocket.domain.vo.req
 * @className: WSBaseReq
 * @author: Caluee
 * @description: 基础请求封装
 * @date: 2024/02/03 003 21:44
 * @version: 1.0
 */

@Data
public class WSBaseReq {
    /**
     * 请求类型
     * @see com.calyee.chat.common.websocket.domain.vo.enums.WSReqTypeEnum
     */
    private Integer type;
    /**
     * 数据
     */
    private String data;
}
