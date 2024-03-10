package com.calyee.chat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.calyee.chat.common.common.domain.dto.RequestUserInfo;
import com.calyee.chat.common.common.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.interceptor
 * @className: CollectorInterceptor
 * @author: Caluee
 * @description: 收集拦截器 优雅的收集token拦截后的信息
 * @date: 2024/03/10 010 20:46
 * @version: 1.0
 */
@Component
public class CollectorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.UID))
                .map(Object::toString)
                .map(Long::parseLong) // 因为原生对象为Object对象 所以必须先toString
                .orElse(null);
        RequestUserInfo requestUserInfo = new RequestUserInfo();
        requestUserInfo.setIp(ServletUtil.getClientIP(request));
        requestUserInfo.setUid(uid);
        RequestHolder.set(requestUserInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove(); // 需要设置就需要移除
    }
}
