package com.calyee.chat.common.user.domain.vo.req.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.domain.vo.req
 * @className: ModifyNameReq
 * @author: Calyee
 * @description: 修改用户名请求vo
 * @date: 2024/03/12 012 19:53
 * @version: 1.0
 */
@Data
public class ModifyNameReq {
    //    @ApiModelProperty("用户名")
    @NotBlank
    @Length(max = 6, message = "用户名不可以取太长了，不然我会记不住哦")
    private String name;
}
