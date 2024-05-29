package com.calyee.chat.common.chat.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReadInfoResp {
//    @ApiModelProperty("消息id")
    private Long msgId;

//    @ApiModelProperty("已读数")
    private Integer readCount;

//    @ApiModelProperty("未读数")
    private Integer unReadCount;

}
