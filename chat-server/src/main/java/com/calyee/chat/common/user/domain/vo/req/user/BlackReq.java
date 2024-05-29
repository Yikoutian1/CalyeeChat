package com.calyee.chat.common.user.domain.vo.req.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BlackReq {
//    @ApiModelProperty("拉黑用户id")
    @NotNull
    private Long uid;
}
