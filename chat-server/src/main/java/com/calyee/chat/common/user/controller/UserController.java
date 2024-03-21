package com.calyee.chat.common.user.controller;


import com.calyee.chat.common.common.domain.dto.RequestUserInfo;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import com.calyee.chat.common.common.utils.AssertUtil;
import com.calyee.chat.common.common.utils.RequestHolder;
import com.calyee.chat.common.user.domain.enums.RoleEnum;
import com.calyee.chat.common.user.domain.vo.req.BlackReq;
import com.calyee.chat.common.user.domain.vo.req.ModifyNameReq;
import com.calyee.chat.common.user.domain.vo.req.WearingBadgeReq;
import com.calyee.chat.common.user.domain.vo.resp.BadgesResp;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import com.calyee.chat.common.user.service.IRoleService;
import com.calyee.chat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    private IRoleService roleService;

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

    @GetMapping("/badges")
    @ApiOperation(value = "可选徽章列表预览")
    public ApiResult<List<BadgesResp>> getBadges() {
        return ApiResult.success(userService.getBadges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badge")
    @ApiOperation(value = "佩戴徽章")
    public ApiResult<Void> wearingBadge(@Validated @RequestBody WearingBadgeReq wearingBadgeReq) {
        userService.wearingBadge(RequestHolder.get().getUid(), wearingBadgeReq.getItemId());
        return ApiResult.success();
    }

    @PutMapping("/black")
    @ApiOperation(value = "拉黑")
    public ApiResult<Void> black(@Validated @RequestBody BlackReq req) {
        Long uid = RequestHolder.get().getUid();
        boolean hasPower = roleService.hasPower(uid, RoleEnum.ADMIN);
        AssertUtil.isTrue(hasPower, "管理员权限不足");
        userService.black(req);
        return ApiResult.success();
    }
}

