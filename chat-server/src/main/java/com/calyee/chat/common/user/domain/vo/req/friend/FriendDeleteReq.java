package com.calyee.chat.common.user.domain.vo.req.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * Description: 申请好友信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDeleteReq {

    @NotNull
//    @ApiModelProperty("好友uid")
    private Long targetUid;

}
