package com.calyee.chat.common.chat.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.calyee.chat.common.chat.domain.entity.Contact;
import com.calyee.chat.common.chat.domain.entity.GroupMember;
import com.calyee.chat.common.chat.domain.entity.Room;
import com.calyee.chat.common.chat.domain.entity.RoomGroup;
import com.calyee.chat.common.chat.domain.vo.request.ChatMessageReq;
import com.calyee.chat.common.chat.domain.vo.response.ChatMessageReadResp;
import com.calyee.chat.common.chat.domain.vo.response.ChatRoomResp;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.enums.GroupRoleEnum;
import com.calyee.chat.common.user.domain.enums.MessageTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 消息适配器
 */
public class RoomAdapter {


    public static List<ChatRoomResp> buildResp(List<Room> list) {
        return list.stream()
                .map(a -> {
                    ChatRoomResp resp = new ChatRoomResp();
                    BeanUtil.copyProperties(a, resp);
                    resp.setActiveTime(a.getActiveTime());
                    return resp;
                }).collect(Collectors.toList());
    }

    public static List<ChatMessageReadResp> buildReadResp(List<Contact> list) {
        return list.stream().map(contact -> {
            ChatMessageReadResp resp = new ChatMessageReadResp();
            resp.setUid(contact.getUid());
            return resp;
        }).collect(Collectors.toList());
    }

    public static List<GroupMember> buildGroupMemberBatch(List<Long> uidList, Long groupId) {
        return uidList.stream()
                .distinct()
                .map(uid -> {
                    GroupMember member = new GroupMember();
                    member.setRole(GroupRoleEnum.MEMBER.getType());
                    member.setUid(uid);
                    member.setGroupId(groupId);
                    return member;
                }).collect(Collectors.toList());
    }

    public static ChatMessageReq buildGroupAddMessage(RoomGroup groupRoom, User inviter, Map<Long, User> member) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setRoomId(groupRoom.getRoomId());
        chatMessageReq.setMsgType(MessageTypeEnum.SYSTEM.getType());
        StringBuilder sb = new StringBuilder();
        sb.append("\"")
                .append(inviter.getName())
                .append("\"")
                .append("邀请")
                .append(member.values().stream().map(u -> "\"" + u.getName() + "\"").collect(Collectors.joining(",")))
                .append("加入群聊");
        chatMessageReq.setBody(sb.toString());
        return chatMessageReq;
    }
}
