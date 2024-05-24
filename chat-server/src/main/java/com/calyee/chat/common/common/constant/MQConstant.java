package com.calyee.chat.common.common.constant;

/**
 */
public interface MQConstant {

    /**
     * 消息发送mq
     */
    String SEND_MSG_TOPIC = "chat_send_msg_calyee";
    String SEND_MSG_GROUP = "chat_send_msg_group_calyee";

    /**
     * push用户
     */
    String PUSH_TOPIC = "websocket_push_calyee";
    String PUSH_GROUP = "websocket_push_group_calyee";

    /**
     * (授权完成后)登录信息mq
     */
    String LOGIN_MSG_TOPIC = "user_login_send_msg_calyee";
    String LOGIN_MSG_GROUP = "user_login_send_msg_group_calyee";

    /**
     * 扫码成功 信息发送mq
     */
    String SCAN_MSG_TOPIC = "user_scan_send_msg_calyee";
    String SCAN_MSG_GROUP = "user_scan_send_msg_group_calyee";
}
