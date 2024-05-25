package com.calyee.chat.common.user.controller;


import com.calyee.chat.common.common.domain.dto.RequestUserInfo;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import com.calyee.chat.common.common.utils.AssertUtil;
import com.calyee.chat.common.common.utils.RequestHolder;
import com.calyee.chat.common.user.domain.dto.ItemInfoDTO;
import com.calyee.chat.common.user.domain.dto.SummeryInfoDTO;
import com.calyee.chat.common.user.domain.enums.RoleEnum;
import com.calyee.chat.common.user.domain.vo.req.user.*;
import com.calyee.chat.common.user.domain.vo.resp.BadgesResp;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import com.calyee.chat.common.user.service.IRoleService;
import com.calyee.chat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/yikoutian1">calyeechat</a>
 * @since 2024-02-28
 */
@CrossOrigin
@RestController
@RequestMapping("/capi/user")  // capi：c端api接口
//@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private IRoleService iRoleService;
    @GetMapping("/userInfo")
//    @ApiOperation(value = "获取用户个人信息(通过token)")
    public ApiResult<UserInfoResp> getUserInfo(){
        RequestUserInfo requestInfo = RequestHolder.get();
        return ApiResult.success(userService.getUserinfo(requestInfo.getUid()));
    }


    @PutMapping("/name")
//    @ApiOperation(value = "修改用户名字")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        userService.modifyName(RequestHolder.get().getUid(), req);
        return ApiResult.success();
    }

    @GetMapping("/badges")
//    @ApiOperation(value = "可选徽章预览")
    public ApiResult<List<BadgesResp>> badges(){
        //查询所有徽章 通过缓存
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badge")
//    @ApiOperation(value = "佩戴徽章")
    public ApiResult<Void> badges(@Valid @RequestBody WearingBadgeReq req){
        //查询所有徽章 通过缓存
        userService.wearingBadge(RequestHolder.get().getUid(), req.getItemId());
        return ApiResult.success();
    }

    @PutMapping("/black")
//    @ApiOperation("拉黑用户")
    public ApiResult<Void> black(@Valid @RequestBody BlackReq req) {
        Long uid = RequestHolder.get().getUid();
        boolean hasPower = iRoleService.hasPower(uid, RoleEnum.ADMIN);
        AssertUtil.isTrue(hasPower, "没有权限");
        userService.black(req);
        return ApiResult.success();
    }


    @PostMapping("/public/summary/userInfo/batch")
//    @ApiOperation("用户聚合信息-返回的代表需要刷新的")
    public ApiResult<List<SummeryInfoDTO>> getSummeryUserInfo(@Valid @RequestBody SummeryInfoReq req) {
        return ApiResult.success(userService.getSummeryUserInfo(req));
    }

    @PostMapping("/public/badges/batch")
//    @ApiOperation("徽章聚合信息-返回的代表需要刷新的")
    public ApiResult<List<ItemInfoDTO>> getItemInfo(@Valid @RequestBody ItemInfoReq req) {
        return ApiResult.success(userService.getItemInfo(req));
    }

}

