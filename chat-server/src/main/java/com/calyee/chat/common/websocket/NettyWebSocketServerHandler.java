package com.calyee.chat.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.calyee.chat.common.websocket.domain.vo.enums.WSReqTypeEnum;
import com.calyee.chat.common.websocket.domain.vo.req.WSBaseReq;
import com.calyee.chat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
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
        webSocketService.handleLoginReq(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关闭用户的连接
                userOffLine(ctx);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            this.webSocketService.connect(ctx.channel());
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if (StrUtil.isNotBlank(token)) {
                this.webSocketService.authorize(ctx.channel(), token);
            }
        }
        super.userEventTriggered(ctx, evt);
    }
    private void userOffLine(ChannelHandlerContext ctx) {
        this.webSocketService.remove(ctx.channel());
        ctx.channel().close();
    }


    /**
     * 2： 客户端下线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffline(ctx.channel());
    }

    /**
     * 用户下线统一处理
     *
     * @param channel
     */
    private void userOffline(Channel channel) {
        webSocketService.remove(channel);
        channel.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        // 反序列化 hutool工具包
        WSBaseReq baseRequest = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(baseRequest.getType())) {
            case AUTHORIZE:
                webSocketService.authorize(ctx.channel(), baseRequest.getData());
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                // 通过连接管道写入消息，通过TextWebSocketFrame对象写入消息
                webSocketService.handleLoginReq(ctx.channel()); // 处理当前channel的信息
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        super.exceptionCaught(ctx, cause);
    }
}

