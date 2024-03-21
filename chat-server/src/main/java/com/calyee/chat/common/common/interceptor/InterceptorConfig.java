package com.calyee.chat.common.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.interceptor
 * @className: InterceptorConfig
 * @author: Caluee
 * @description: Spring拦截器配置生效
 * @date: 2024/03/10 010 20:35
 * @version: 1.0
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    public static final String CAPI = "/capi/**";
    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Autowired
    private CollectorInterceptor collectorInterceptor;
    @Autowired
    private BlackInterceptor blackInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(CAPI);
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns(CAPI);
        registry.addInterceptor(blackInterceptor)
                .addPathPatterns(CAPI);
    }
}
