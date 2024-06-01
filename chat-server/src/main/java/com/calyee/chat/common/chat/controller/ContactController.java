package com.calyee.chat.common.chat.controller;


import com.calyee.chat.common.chat.domain.vo.request.ContactFriendReq;
import com.calyee.chat.common.chat.domain.vo.response.ChatRoomResp;
import com.calyee.chat.common.chat.service.ChatService;
import com.calyee.chat.common.chat.service.RoomAppService;
import com.calyee.chat.common.common.domain.vo.req.CursorPageBaseReq;
import com.calyee.chat.common.common.domain.vo.req.IdReqVO;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import com.calyee.chat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.calyee.chat.common.common.utils.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 会话相关接口
 * </p>
 */
@CrossOrigin
@RestController
@RequestMapping("/capi/chat")
//@Api(tags = "聊天室相关接口")
@Slf4j
public class ContactController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private RoomAppService roomService;

    @GetMapping("/public/contact/page")
//    @ApiOperation("会话列表")
    public ApiResult<CursorPageBaseResp<ChatRoomResp>> getRoomPage(@Valid CursorPageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getContactPage(request, uid));
    }

    @GetMapping("/public/contact/detail")
//    @ApiOperation("会话详情")
    public ApiResult<ChatRoomResp> getContactDetail(@Valid IdReqVO request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getContactDetail(uid, request.getId()));
    }

    @GetMapping("/public/contact/detail/friend")
//    @ApiOperation("会话详情(联系人列表发消息用)")
    public ApiResult<ChatRoomResp> getContactDetailByFriend(@Valid ContactFriendReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(roomService.getContactDetailByFriend(uid, request.getUid()));
    }
}