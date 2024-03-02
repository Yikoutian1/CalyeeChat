package com.calyee.chat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.service.LoginService;
import com.calyee.chat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.calyee.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.calyee.chat.common.websocket.service.WebSocketService;
import com.calyee.chat.common.websocket.service.adapter.WebSocketAdapter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service
 * @className: WebSocketServiceImpl
 * @author: Caluee
 * @description: WebSocketService 实现类  专门管理websocket的逻辑
 * @date: 2024/02/29 029 21:05
 * @version: 1.0
 */

@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    @Lazy
    private WxMpService wxMpService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;
    /**
     * 管理所有在线用户的连接（登录状态/游客）
     * （此时使用WSChannelExtraDTO去代替魔法的uid是为了以后可以拓展 拓展可以存其他的）
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WEBSOCKET_MAP = new ConcurrentHashMap<>();
    public static final int MAXIMUM_SIZE = 10000;
    public static final Duration DURATION_TIME = Duration.ofHours(1);
    /**
     * 临时保存登录code和channel的映射关系
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION_TIME)
            .build();

    @Override
    public void connect(Channel channel) {
        // 保存逻辑就是 把channel放入ConcurrentHashMap中
        ONLINE_WEBSOCKET_MAP.put(channel, new WSChannelExtraDTO()); // 暂时先保存空对象，后期登录认证在设置value
    }

    @SneakyThrows
    @Override
    public void handeLoginReq(Channel channel) {
        // 生成随机二维码 （靠code和channel映射保存到一起）
        Integer code = generateLoginCode(channel);
        // 找微信申请申请带参数的二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION_TIME.getSeconds());
        // 把二维码发送给前端生成二维码
        sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    /**
     * channel断开连接
     *
     * @param channel
     */
    @Override
    public void offline(Channel channel) {
        ONLINE_WEBSOCKET_MAP.remove(channel);
        // TODO 用户下线广播
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        // 确认连接在机器上
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        User user = userDao.getById(uid); // 获取频道用户信息
        // 移除code
        WAIT_LOGIN_MAP.invalidate(code);
        // 调用登录模块 获取token
        String token = loginService.login(uid);
        // 用户登录
        sendMsg(channel,WebSocketAdapter.buildResp(user,token));
    }

    @Override
    public void waitAuthorize(Integer code) {
        // 通过code获取channel
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        sendMsg(channel,WebSocketAdapter.buildWaitAuthorizeResp());
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp))); // 以websocket的形式返回回去
    }

    /**
     * 生成登录码
     *
     * @param channel
     * @return
     */
    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE); // 生成随机码
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        // 如果channel和code映射在缓存里面没有则生成成功，即putIfAbsent返回为空则退出，设置成功（碰撞后仅一个值则为期望结果）
        return code;
    }
}
