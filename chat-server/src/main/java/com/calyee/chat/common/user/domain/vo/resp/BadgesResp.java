package com.calyee.chat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.domain.vo.resp
 * @className: UserInfoResp
 * @author: Calyee
 * @description: 徽章返回实体
 * @date: 2024/03/06 006 21:21
 * @version: 1.0
 */
@Data
public class BadgesResp {
    @ApiModelProperty(value = "徽章id")
    private Long id;
    @ApiModelProperty(value = "徽章图标")
    private String img;
    @ApiModelProperty(value = "徽章描述")
    private String describe;
    @ApiModelProperty(value = "是否拥有 0否 1是")
    private Integer obtain;
    @ApiModelProperty(value = "是否佩戴 0否 1是")
    private Integer wearing;
}
