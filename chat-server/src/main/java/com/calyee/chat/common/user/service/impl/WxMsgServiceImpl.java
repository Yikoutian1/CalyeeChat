package com.calyee.chat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.service.UserService;
import com.calyee.chat.common.user.service.WxMsgService;
import com.calyee.chat.common.user.service.adapter.TextBuilder;
import com.calyee.chat.common.user.service.adapter.UserAdapter;
import com.calyee.chat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service.impl
 * @className: WxMsgServiceImpl
 * @author: Caluee
 * @description: 微信消息处理实现类
 * @date: 2024/02/29 029 22:27
 * @version: 1.0
 */
@Slf4j
@Service
public class WxMsgServiceImpl implements WxMsgService {
    /**
     * 等待授权的map
     * openid和登录code关系map
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();
    @Autowired
    private WebSocketService webSocketService;
    @Value("${wx.mp.callback}")
    private String callback;

    // 用户同意授权，获取code  （链接引导，%s占位）
    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    @Lazy // 解决循环依赖
    private WxMpService wxMpService;

    /**
     * 用户扫码成功推送
     *
     * @param wxMpXmlMessage
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String openId = wxMpXmlMessage.getFromUser();// FormUser -> OpenID
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)) {
            return null;
        }
        User user = userDao.getByOpenId(openId);
        boolean registered = Objects.nonNull(user);
        boolean authorized = registered && StrUtil.isNotBlank(user.getAvatar()); // 需要先判定 如果都没有注册 就不会存在后面的条件

        // 用户已经注册并且授权
        if (registered && authorized) {
            // 走登录成功的逻辑 通过code找到channel给channel推送消息
            webSocketService.scanLoginSuccess(code, user.getId());
            return null;
        }
        // 用户未注册,就先注册
        if (!registered) {
            User insert = UserAdapter.buildUserSave(openId);
            userService.register(insert);
        }
        // 扫码事件处理 推送消息让用户授权
        WAIT_AUTHORIZE_MAP.put(openId, code);
        webSocketService.waitAuthorize(code);
        String authorizeUrl = String.format(URL,
                wxMpService.getWxMpConfigStorage().getAppId(),
                URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        return TextBuilder.build("请点击登录： <a href=\"" + authorizeUrl + "\">登录</a>", wxMpXmlMessage);
    }

    /**
     * 将授权信息保存至数据库
     *
     * @param userInfo
     */
    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpenId(openid);
        // 更新用户信息
        if (StrUtil.isBlank(user.getAvatar())) {
            fillUserInfo(user.getId(), userInfo);
        }
        // 通过code找到用户channel,进行登录
        Integer code = WAIT_AUTHORIZE_MAP.remove(openid);
        webSocketService.scanLoginSuccess(code, user.getId());
    }

    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUserInfo(uid, userInfo);
        // TODO 后期可以在此处修改用户名不重复的逻辑 try-catch,主键冲突异常 DuplicateKeyException
        userDao.updateById(user);
    }


    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        // 分析事件码 qrscene_2
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("qrscene_", "");
            return Integer.parseInt(code);
        } catch (Exception e) {
            // 失败情况
            log.error("getEventKey error eventKey:{}", wxMpXmlMessage.getEventKey());
            return null;
        }
    }
}
