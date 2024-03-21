package com.calyee.chat.common.user.service.cache;

import com.calyee.chat.common.user.dao.UserRoleDao;
import com.calyee.chat.common.user.domain.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 用户角色缓存
 */
@Component
public class UserCache {
    @Autowired
    private UserRoleDao userRoleDao;

    @Cacheable(cacheNames = "user", key = "'roles: '+ #uid")
    public Set<Long> getRoleSetByUid(Long uid) {
        List<UserRole> roles = userRoleDao.listByUid(uid);
        return roles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
    }

    // 清空缓存
    @CacheEvict(cacheNames = "user", key = "'roles: '+ #uid")
    public void evictByUid(Integer uid) {
    }
}
