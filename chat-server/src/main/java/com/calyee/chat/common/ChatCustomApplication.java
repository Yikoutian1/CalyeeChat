package com.calyee.chat.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName ChatCustomApplication
 * @Description websocket启动类
 * @Author Calyee
 * @DATE 2024/01/23 023 13:58
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.calyee.chat"})
@MapperScan({"com.calyee.chat.common.**.mapper"})
public class ChatCustomApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatCustomApplication.class, args);
    }
}
