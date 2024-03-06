package com.calyee.chat.common.user.controller;


import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/yikoutian1">calyeechat</a>
 * @since 2024-02-28
 */
@RestController
@RequestMapping("/capi/user")  // capi：c端api接口
@ApiModel(value="用户模块接口", description="用户模块接口")
public class UserController {
    @GetMapping("/userInfo")
    @ApiOperation(value = "获取个人信息")
    public UserInfoResp getUserInfo(@RequestParam(value = "uid") Long uid) {
        return null;
    }
}

