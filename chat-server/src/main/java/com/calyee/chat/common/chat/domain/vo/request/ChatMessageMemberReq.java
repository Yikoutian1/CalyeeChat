package com.calyee.chat.common.chat.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Description: 消息列表请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageMemberReq {
    @NotNull
//    @ApiModelProperty("会话id")
    private Long roomId;
}
