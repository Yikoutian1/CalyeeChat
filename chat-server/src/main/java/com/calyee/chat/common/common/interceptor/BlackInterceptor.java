package com.calyee.chat.common.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.calyee.chat.common.common.domain.dto.RequestUserInfo;
import com.calyee.chat.common.common.exception.HttpErrorEnum;
import com.calyee.chat.common.common.utils.RequestHolder;
import com.calyee.chat.common.user.domain.enums.BlackTypeEnum;
import com.calyee.chat.common.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @description: 黑名单拦截器
 * @version: 1.0
 */

@Component
public class BlackInterceptor implements HandlerInterceptor {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestUserInfo requestUserInfo = RequestHolder.get();
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        boolean inBlackListJudgeUID = inBlackList(requestUserInfo.getUid(), blackMap.get(BlackTypeEnum.UID.getType()));
        if (inBlackListJudgeUID) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        boolean inBlackListJudgeIP = inBlackList(requestUserInfo.getUid(), blackMap.get(BlackTypeEnum.IP.getType()));
        if (inBlackListJudgeIP) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        return true;
    }

    private boolean inBlackList(Object target, Set<String> set) {
        if (Objects.isNull(target) || CollectionUtil.isEmpty(set)) {
            return false;
        }
        return set.contains(target.toString());
    }


}
