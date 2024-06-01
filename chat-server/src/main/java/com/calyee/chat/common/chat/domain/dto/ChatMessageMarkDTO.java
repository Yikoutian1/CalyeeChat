package com.calyee.chat.common.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 消息标记请求

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageMarkDTO {

//    @ApiModelProperty("操作者")
    private Long uid;

//    @ApiModelProperty("消息id")
    private Long msgId;

    /**

     */
//    @ApiModelProperty("标记类型 1点赞 2举报")
    private Integer markType;

    /**

     */
//    @ApiModelProperty("动作类型 1确认 2取消")
    private Integer actType;
}
