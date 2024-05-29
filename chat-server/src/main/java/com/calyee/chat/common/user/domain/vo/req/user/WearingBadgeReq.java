package com.calyee.chat.common.user.domain.vo.req.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**

 * @author: Calyee
 * @description: 佩戴徽章
 */
@Data
public class WearingBadgeReq {
//    @ApiModelProperty("徽章id")
    @NotBlank
    private Long itemId;
}
