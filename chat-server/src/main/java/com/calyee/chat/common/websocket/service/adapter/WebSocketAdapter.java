package com.calyee.chat.common.websocket.service.adapter;

import com.calyee.chat.common.websocket.domain.vo.enums.WSRespTypeEnum;
import com.calyee.chat.common.websocket.domain.vo.resp.WSBaseResp;
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
}
