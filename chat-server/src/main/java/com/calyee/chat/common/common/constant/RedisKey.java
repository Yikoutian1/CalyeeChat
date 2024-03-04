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
