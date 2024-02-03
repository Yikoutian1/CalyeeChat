package com.calyee.chat.common.websocket;

import cn.hutool.json.JSONUtil;
import com.calyee.chat.common.websocket.domain.vo.enums.WSReqTypeEnum;
import com.calyee.chat.common.websocket.domain.vo.req.WSBaseReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     *
     * @param ctx
     * @param evt 事件类型
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 如果事件是一个WebSocket握手的事件
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("[握手完成]");
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("[读空闲]");
                //todo 用户下线
                ctx.channel().close();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        // 反序列号 hutool工具包
        WSBaseReq baseRequest = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(baseRequest.getType())) {
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                log.info("[请求二维码]");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("123")); // 通过连接管道写入消息，通过TextWebSocketFrame对象写入消息
        }
    }
}

