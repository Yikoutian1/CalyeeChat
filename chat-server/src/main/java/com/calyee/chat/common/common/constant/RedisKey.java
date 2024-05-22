package com.calyee.chat.common.common.constant;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.constant
 * @className: RedisKey
 * @author: Caluee
 * @description: Redis Key存放
 * @date: 2024/03/04 004 19:39
 * @version: 1.0
 */
public class RedisKey {

    private static final String BASE_KEY = "calyeechat:chat";
    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    /**
     * 在线用户列表
     */
    public static final String ONLINE_UID_ZET = "online";

    /**
     * 离线用户列表
     */
    public static final String OFFLINE_UID_ZET = "offline";

    /**
     * 热门房间列表
     */
    public static final String HOT_ROOM_ZET = "hotRoom";

    /**
     * 用户信息
     */
    public static final String USER_INFO_STRING = "userInfo:uid_%d";

    /**
     * 房间详情
     */
    public static final String ROOM_INFO_STRING = "roomInfo:roomId_%d";

    /**
     * 群组详情
     */
    public static final String GROUP_INFO_STRING = "groupInfo:roomId_%d";

    /**
     * 用户的信息更新时间
     */
    public static final String USER_MODIFY_STRING = "userModify:uid_%d";

    /**
     * 用户的信息汇总
     */
    public static final String USER_SUMMARY_STRING = "userSummary:uid_%d";

    /**
     * 用户GPT聊天次数
     */
    public static final String USER_CHAT_NUM = "useChatGPTNum:uid_%d";

    public static final String USER_CHAT_CONTEXT = "useChatGPTContext:uid_%d_roomId_%d";

    /**
     * 保存Open id
     */
    public static final String OPEN_ID_STRING = "openid:%s";


    /**
     * 用户上次使用GLM使用时间
     */
    public static final String USER_GLM2_TIME_LAST = "userGLM2UseTime:uid_%d";
    /**
     * 获取key
     *
     * @param key 模板
     * @param o   填充值
     * @return
     */
    public static String getKey(String key, Object... o) {
        return BASE_KEY + String.format(key, o);
    }
}
