package com.calyee.chat.common.common.utils;

import com.calyee.chat.common.common.domain.dto.RequestUserInfo;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.utils
 * @className: RequestHolder
 * @author: Caluee
 * @description: 请求上下文
 * @date: 2024/03/10 010 20:53
 * @version: 1.0
 */

public class RequestHolder {
    private static final ThreadLocal<RequestUserInfo> thread = new ThreadLocal<>();

    public static void set(RequestUserInfo requestUserInfo) {
        thread.set(requestUserInfo);
    }

    public static RequestUserInfo get() {
        return thread.get();
    }

    /**
     * 避免内存泄露
     */
    public static void remove(){
        thread.remove();
    }
}
