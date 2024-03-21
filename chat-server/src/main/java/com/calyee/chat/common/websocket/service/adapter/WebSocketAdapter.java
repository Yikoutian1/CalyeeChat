package com.calyee.chat.common.websocket.service.adapter;

import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.websocket.domain.vo.enums.WSRespTypeEnum;
import com.calyee.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.calyee.chat.common.websocket.domain.vo.resp.WSBlack;
import com.calyee.chat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.calyee.chat.common.websocket.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.websocket.service.adapter
 * @className: WebSocketAdapter
 * @author: Caluee
 * @description: websocket适配器，转换
 * @date: 2024/02/29 029 21:57
 * @version: 1.0
 */

public class WebSocketAdapter {
    public static WSBaseResp<?> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));// 返回url
        return resp;
    }

    public static WSBaseResp<?> buildResp(User user, String token, boolean power) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess loginSuccess = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .power(power ? 1 : 0)
                .build();
        resp.setData(loginSuccess);
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizeResp() {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType()); // 构造登录状态2
        return resp;
    }

    public static WSBaseResp<?> buildAuthorizeResp() {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType()); // token生效
        return resp;
    }

    public static WSBaseResp<?> buildBlack(User user) {
        WSBaseResp<WSBlack> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.BLACK.getType());
        WSBlack black = WSBlack.builder()
                .uid(user.getId())
                .build();
        resp.setData(black);
        return resp;
    }
}
