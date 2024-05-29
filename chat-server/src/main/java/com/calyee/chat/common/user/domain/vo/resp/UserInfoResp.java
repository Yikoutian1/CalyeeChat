package com.calyee.chat.common.user.domain.vo.resp;

import lombok.Data;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.domain.vo.resp
 * @className: UserInfoResp
 * @author: Caluee
 * @description: 用户返回请求体
 * @date: 2024/03/06 006 21:21
 * @version: 1.0
 */
@Data
public class UserInfoResp {
//    @ApiModelProperty(value = "uid")
    private Long id;
//    @ApiModelProperty(value = "用户名称")
    private String name;
//    @ApiModelProperty(value = "用户头像")
    private String avatar;
//    @ApiModelProperty(value = "用户性别")
    private Integer sex;
    /**
     * 改名卡剩余次数
     */
//    @ApiModelProperty(value = "改名卡剩余次数")
    private Integer modifyNameChance;
}
