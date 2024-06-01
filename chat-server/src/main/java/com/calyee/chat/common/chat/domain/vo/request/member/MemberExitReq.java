package com.calyee.chat.common.chat.domain.vo.request.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Description 退出群聊
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberExitReq {
    @NotNull
//    @ApiModelProperty("会话id")
    private Long roomId;
}
