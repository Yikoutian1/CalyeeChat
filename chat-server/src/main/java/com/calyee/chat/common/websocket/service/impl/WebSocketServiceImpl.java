package com.calyee.chat.common.websocket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.calyee.chat.common.chat.service.adapter.websocketAdapter;
import com.calyee.chat.common.common.config.ThreadPoolConfig;
import com.calyee.chat.common.common.event.UserOnlineEvent;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.IpInfo;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.enums.RoleEnum;
import com.calyee.chat.common.user.service.IRoleService;
import com.calyee.chat.common.user.service.LoginService;
import com.calyee.chat.common.websocket.NettyUtil;
import com.calyee.chat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.calyee.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.calyee.chat.common.websocket.service.WebSocketService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private LoginService loginService;
    @Autowired
    UserDao userDao;
    @Autowired
    //引入微信service
    @Lazy//解决循环依赖
    private WxMpService wxMpService;

    //这里保存了通道和用户id 通过channel我们可以找到用户
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    //还要保存一个用户id和通道的联系，通过用户找到通道 一个用户可能有多个设备所以做list
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    private IRoleService iRoleService;

    @Autowired
    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 临时存保存code和channel的映射关系
     */
    public static final int MAXIMUM_SIZE = 1000;
    public static final Duration DURATION = Duration.ofHours(1);
    //ctrl + alt + c 抽取静态变量
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)//最大容量更多就会淘汰
            .expireAfterWrite(DURATION)//过期时间一个小时
            .build();//随机码对应channel 过段时间销毁掉
    // caffeine 操作

    /**
     * 管理所有用户的连接 包括登录态和游客
     *
     * @param channel
     */
    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());//放入通道和用户的联系
        //扫码登录链接成功以后会把channel放入然后同时new一个DTO 这个时候不知道他的uid是什么
    }


    @Override
    public void remove(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        Optional<Long> uidOptional = Optional.ofNullable(wsChannelExtraDTO)
                .map(WSChannelExtraDTO::getUid);//判断下线这个人uid非空
        boolean offlineAll = offline(channel, uidOptional);//判断channel在列表中是否存在然后循环过滤删除
        if (uidOptional.isPresent() && offlineAll) {//已登录用户断连,并且全下线成功
            User user = new User();
            user.setId(uidOptional.get());//设置userid
            user.setLastOptTime(new Date());//设置最后上下线时间
            applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));//这个里面先保存了这个下线时间去了数据库还PUSH到了redis中去
        }
    }

    /**
     * 用户下线
     * return 是否全下线成功
     */
    private boolean offline(Channel channel, Optional<Long> uidOptional) {
        ONLINE_WS_MAP.remove(channel);
        if (uidOptional.isPresent()) {
            CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uidOptional.get());
            if (CollectionUtil.isNotEmpty(channels)) {
                channels.removeIf(ch -> Objects.equals(ch, channel));
            }
            return CollectionUtil.isEmpty(ONLINE_UID_MAP.get(uidOptional.get()));
        }
        return true;
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel))
            return;
        //防御使编程
        sendMsg(channel, websocketAdapter.waitAuthorizeBuildResp());
    }

    /**
     * 用户重新登录的时候需要调用这个函数判断token
     *
     * @param channel
     * @param token
     */
    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {
            User user = userDao.getById(validUid);
            logSuccess(channel, user, token);//认证以后走这个登录成功的逻辑
        } else {
            sendMsg(channel, websocketAdapter.buildValidTokenResp());
        }
    }

    @Override
    /**
     * 这个特定拉黑的时候会使用这个函数 让所有在线用户知道谁被拉黑了一般管理员才有这个能力
     */
    public void sendMsgToAll(WSBaseResp<?> msg) {
        //所有在线用户发送
        ONLINE_WS_MAP.forEach((channel, cxt) -> {
            threadPoolTaskExecutor.execute(() -> {
                sendMsg(channel, msg);
            });
        });
    }


    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
        //生成随机码
        Integer code = generateLoginCode(channel);
        //找到微信中申请带参数的二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        //把码发给前端
        sendMsg(channel, websocketAdapter.buildResp(wxMpQrCodeTicket));
    }


    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);//只要在int范围内就行
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        //确认链接在机器上
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel))
            return;
        //不存在直接返回了
        User user = userDao.getById(uid);
        //移除code
        WAIT_LOGIN_MAP.invalidate(code);//登录成功以后，就不需要在临时列表存这个code和channel的关系了
        //调用登录模块获取token
        String token = loginService.login(uid);
        //用户登录
        logSuccess(channel, user, token);
    }

    private void logSuccess(Channel channel, User user, String token) {
        //保存channel消息对应uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);//通过cahnnel获取这个用户id
        wsChannelExtraDTO.setUid(user.getId());
//        ONLINE_WS_MAP.put(channel,wsChannelExtraDTO);//在这里如过登录成功了会放入uid 但是这样写不行 ，因为有可能他这个channel不存在会报错 会导致系统崩溃
        WSChannelExtraDTO WSChannelExtradto = ONLINE_WS_MAP.getOrDefault(channel, new WSChannelExtraDTO());//使用concurrentHashmap的方法 如果有这个就传入 如果没有就新建一个 但是这个时候这里面还是没有值
        WSChannelExtraDTO old = ONLINE_WS_MAP.putIfAbsent(channel, wsChannelExtraDTO);//判断channel是否存在并且值不为空 如果是的话就不放 否则酒吧这个获取到的放进去 然后通过这个channel放值
        //这里类似于一个防御编程
        ONLINE_WS_MAP.put(channel, Objects.isNull(old) ? WSChannelExtradto : old);


        //放了ONLINE_WS_MAP这个以后我们还需要存uid和channel的关系 以便于传递消息
        ONLINE_UID_MAP.putIfAbsent(user.getId(), new CopyOnWriteArrayList<Channel>());
        ONLINE_UID_MAP.get(user.getId()).add(channel);//在这里

        NettyUtil.setAttr(channel, NettyUtil.UID, user.getId());
        //推送成功消息
        sendMsg(channel, websocketAdapter.buildResp(user, token, iRoleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER)));
        // 用户上线成功事件
        user.setLastOptTime(new Date());
        IpInfo ipInfo = new IpInfo();
        ipInfo.setCreateIp(NettyUtil.getAttr(channel, NettyUtil.IP));
        ipInfo.setUpdateIp(NettyUtil.getAttr(channel, NettyUtil.IP));
        user.refreshIp(NettyUtil.getAttr(channel, NettyUtil.IP));
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
    }
//    /**
//     * 用户上线
//     */
//    private void online(Channel channel, Long uid) {
//        getOrInitChannelExt(channel).setUid(uid);
//        ONLINE_UID_MAP.putIfAbsent(uid, new CopyOnWriteArrayList<>());
//        ONLINE_UID_MAP.get(uid).add(channel);
//        NettyUtil.setAttr(channel, NettyUtil.UID, uid);
//    }
//    /**
//     * 如果在线列表不存在，就先把该channel放进在线列表
//     *
//     * @param channel
//     * @return
//     */
//    private WSChannelExtraDTO getOrInitChannelExt(Channel channel) {
//        WSChannelExtraDTO wsChannelExtraDTO =
//                ONLINE_WS_MAP.getOrDefault(channel, new WSChannelExtraDTO());
//        WSChannelExtraDTO old = ONLINE_WS_MAP.putIfAbsent(channel, wsChannelExtraDTO);
//        return ObjectUtil.isNull(old) ? wsChannelExtraDTO : old;
//    }

    //entrySet的值不是快照数据,但是它支持遍历，所以无所谓了，不用快照也行。
    @Override
    /**
     *     给所有在线用户发消息
     */
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid) {
        ONLINE_WS_MAP.forEach((channel, ext) -> {
            if (Objects.nonNull(skipUid) && Objects.equals(ext.getUid(), skipUid)) {
                return;
            }
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        });
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp) {
        sendToAllOnline(wsBaseResp, null);
    }

    @Override
    /**
     *     给指定用户发消息
     */
    public void sendToUid(WSBaseResp<?> wsBaseResp, Long uid) {
        CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uid);//找到单个用户的所有channel
        if (CollectionUtil.isEmpty(channels)) {
            log.info("用户：{}不在线", uid);
            return;
        }
        channels.forEach(channel -> {
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        });
    }

    /**
     * 给本地channel发送消息
     *
     * @param channel
     * @param wsBaseResp
     */
    private void sendMsg(Channel channel, WSBaseResp<?> wsBaseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }

}
