package com.calyee.chat.common.websocket.service;

import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WebSocketService {

    void connect(Channel channel);

    void handeLoginReq(Channel channel) throws WxErrorException;

    void offline(Channel channel);
}
