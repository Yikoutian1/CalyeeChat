package com.calyee.chat.common.chat.service;


import com.calyee.chat.common.chat.domain.dto.MsgReadInfoDTO;
import com.calyee.chat.common.chat.domain.entity.Message;
import com.calyee.chat.common.chat.domain.vo.request.*;
import com.calyee.chat.common.chat.domain.vo.request.member.MemberReq;
import com.calyee.chat.common.chat.domain.vo.response.ChatMemberListResp;
import com.calyee.chat.common.chat.domain.vo.response.ChatMemberStatisticResp;
import com.calyee.chat.common.chat.domain.vo.response.ChatMessageReadResp;
import com.calyee.chat.common.chat.domain.vo.response.ChatMessageResp;
import com.calyee.chat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.calyee.chat.common.websocket.domain.vo.resp.ChatMemberResp;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Description: 消息处理类

 */
public interface ChatService {

    /**
     * 发送消息
     *
     * @param request
     */
    Long sendMsg(ChatMessageReq request, Long uid);

    /**
     * 根据消息获取消息前端展示的物料
     *
     * @param message
     * @param receiveUid 接受消息的uid，可null
     * @return
     */
    ChatMessageResp getMsgResp(Message message, Long receiveUid);

    /**
     * 根据消息获取消息前端展示的物料
     *
     * @param msgId
     * @param receiveUid 接受消息的uid，可null
     * @return
     */
    ChatMessageResp getMsgResp(Long msgId, Long receiveUid);

    /**
     * 获取群成员列表
     *
     * @param memberUidList
     * @param request
     * @return
     */
    CursorPageBaseResp<ChatMemberResp> getMemberPage(List<Long> memberUidList, MemberReq request);

    /**
     * 获取消息列表
     *
     * @param request
     * @return
     */
    CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq request, @Nullable Long receiveUid);
//
    ChatMemberStatisticResp getMemberStatistic();
//
    void setMsgMark(Long uid, ChatMessageMarkReq request);

    void recallMsg(Long uid, ChatMessageBaseReq request);

    List<ChatMemberListResp> getMemberList(ChatMessageMemberReq chatMessageMemberReq);

    Collection<MsgReadInfoDTO> getMsgReadInfo(Long uid, ChatMessageReadInfoReq request);

    CursorPageBaseResp<ChatMessageReadResp> getReadPage(Long uid, ChatMessageReadReq request);

    void msgRead(Long uid, ChatMessageMemberReq request);
}
