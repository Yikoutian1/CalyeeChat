package com.calyee.chat.common.common.utils;

import java.util.Random;

/**
 * @projectName: calyeechat
 * @className: RandomStringUtils
 * @author: Calyee
 * @description: 随机字符串
 * @version: 1.0
 */

public class RandomStringUtils {
    //length用户要求产生字符串的长度
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
