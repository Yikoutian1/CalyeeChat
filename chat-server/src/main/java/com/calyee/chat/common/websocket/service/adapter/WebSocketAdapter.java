package com.calyee.chat.common.websocket.service.adapter;

import com.calyee.chat.common.common.domain.enums.YesOrNoEnum;
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

        WSBaseResp<WSLoginUrl>resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return resp;
    }

    public static WSBaseResp<?> buildResp(User user, String token) {

        WSBaseResp<WSLoginSuccess>resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess build = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .build();
        resp.setData(build);
        return resp;

    }


    public static WSBaseResp<?> buildResp(User user, String token,Boolean power) {

        WSBaseResp<WSLoginSuccess>resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess build = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .power(power? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus())
                .uid(user.getId())
                .build();
        resp.setData(build);
        return resp;

    }


    public static WSBaseResp<?> buildResp(User user) {

        WSBaseResp<WSBlack>resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.BLACK.getType());
        WSBlack build = WSBlack.builder()
                .uid(user.getId())
                .build();
        resp.setData(build);
        return resp;

    }

    public static WSBaseResp<?> waitAuthorizeBuildResp() {

        WSBaseResp<WSLoginUrl>resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }


    public static WSBaseResp<?> buildInvalidTokenResp() {
        WSBaseResp<WSLoginUrl>resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return resp;
    }
}
