package com.calyee.chat.common.websocket.service;

import com.calyee.chat.common.websocket.domain.vo.resp.WSBaseResp;
import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WebSocketService {
    void connect(Channel channel);


    /**
     * 拿到二维码
     *
     * @param channel
     * @throws WxErrorException
     */
    void handleLoginReq(Channel channel) throws WxErrorException;

    /**
     * 移除channel，下线
     *
     * @param channel
     */
    void remove(Channel channel);

    void scanLoginSuccess(Integer code, Long id);

    /**
     * 等待授权
     *
     * @param code
     */
    void waitAuthorize(Integer code);


    /**
     * 认证回调
     * @param channel
     * @param token
     */
    void authorize(Channel channel, String token);

    void sendMsgToAll(WSBaseResp<?> msg);


    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     * @param skipUid    需要跳过的人
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid);

    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp);

    void sendToUid(WSBaseResp<?> wsBaseResp, Long uid);
}
