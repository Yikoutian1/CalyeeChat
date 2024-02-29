package com.calyee.chat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.calyee.chat.common.websocket.domain.vo.enums.WSReqTypeEnum;
import com.calyee.chat.common.websocket.domain.vo.req.WSBaseReq;
import com.calyee.chat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private WebSocketService webSocketService;

    /**
     * 当web客户端连接后，触发该方法
     * 记录并管理连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // 将连接放入一个map中管理，目前该连接是未登录态
        // 通过hutool工具包获取bean实例
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        // WebSocket保存
        webSocketService.connect(ctx.channel());
    }

    /**
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
            }
        }
    }

    /**
     * 用户下线统一处理
     * @param channel
     */
    private void userOffline(Channel channel){
        webSocketService.offline(channel);
        channel.close();
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
                // 通过连接管道写入消息，通过TextWebSocketFrame对象写入消息
                webSocketService.handeLoginReq(ctx.channel()); // 处理当前channel的信息
        }
    }
}

