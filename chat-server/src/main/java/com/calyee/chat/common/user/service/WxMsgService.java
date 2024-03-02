package com.calyee.chat.common.user.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service
 * @className: WxMsgService
 * @author: Caluee
 * @description: 微信消息处理接口
 * @date: 2024/02/29 029 22:26
 * @version: 1.0
 */

public interface WxMsgService {
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage);

    void authorize(WxOAuth2UserInfo userInfo);
}
