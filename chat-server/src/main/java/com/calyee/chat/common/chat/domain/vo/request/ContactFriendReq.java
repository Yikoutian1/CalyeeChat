package com.calyee.chat.common.chat.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Description: 移除群成员
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactFriendReq {

    @NotNull
//    @ApiModelProperty("好友uid")
    private Long uid;
}
