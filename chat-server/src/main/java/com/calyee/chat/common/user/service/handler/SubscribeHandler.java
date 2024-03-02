package com.calyee.chat.common.user.service.handler;

import com.calyee.chat.common.user.service.WxMsgService;
import com.calyee.chat.common.user.service.adapter.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Autowired
    private WxMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());
        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = wxMsgService.scan(wxMessage);
//            responseResult = this.handleSpecial(weixinService, wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
        // 如果可以取到事件码 则说明是通过扫码进来的关注的
        if (responseResult != null) {
            return responseResult;
        }
        // 如果不能取到事件码 则为正常用户
        return TextBuilder.build("感谢关注", wxMessage);
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */

}
