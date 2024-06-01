package com.calyee.chat.common.common.interceptor;

import com.calyee.chat.common.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.interceptor
 * @className: TokenInterceptor
 * @author: Caluee
 * @description: Token拦截器
 * @date: 2024/03/10 010 20:04
 * @version: 1.0
 */

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    public static final String UID = "uid";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_BEARER = "Bearer ";
    /**
     * 公共接口标识符
     */
    public static final String PUBLIC = "public";
    @Autowired
    private LoginService loginService; // 校验token

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
//        log.info("Req:{},Token:{},认证：{}", request.getRequestURI(), token, request.getHeader(HEADER_AUTHORIZATION));
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {// 用户有登录态
            request.setAttribute(UID, validUid); // 如果有登录 则放入附件
        } else { // 用户未登录
            boolean isPublicURI = isPublicURI(request);
//            if (!isPublicURI) { // 不是public接口
//                // 401
//                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
//                return false;
//            }
        }
        return true;
    }

    private boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/"); // 拆分请求路径 如果不是public公共接口则返回401
        return split.length > 3 && PUBLIC.equals(split[3]);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(AUTHORIZATION_BEARER))
                .map(h -> h.substring(AUTHORIZATION_BEARER.length()))
                .orElse(null);
    }
}
