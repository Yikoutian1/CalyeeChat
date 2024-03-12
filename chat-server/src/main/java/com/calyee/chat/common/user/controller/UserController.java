package com.calyee.chat.common.user.controller;


import com.calyee.chat.common.common.domain.dto.RequestUserInfo;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import com.calyee.chat.common.common.utils.RequestHolder;
import com.calyee.chat.common.user.domain.vo.req.ModifyNameReq;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import com.calyee.chat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "用户模块接口")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation(value = "获取个人信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        RequestUserInfo userInfo = RequestHolder.get(); // 在ThreadLocal中获取
        return ApiResult.success(userService.getUserInfo(userInfo.getUid()));
    }

    @PutMapping("/name")
    @ApiOperation(value = "修改用户名")
    public ApiResult<Void> modifyName(@Validated @RequestBody ModifyNameReq modifyNameReq) {
        userService.modifyName(RequestHolder.get().getUid(), modifyNameReq.getName());
        return ApiResult.success();
    }
}

