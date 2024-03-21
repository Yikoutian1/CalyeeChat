package com.calyee.chat.common.user.service.cache;

import com.calyee.chat.common.user.dao.BlackDao;
import com.calyee.chat.common.user.dao.UserRoleDao;
import com.calyee.chat.common.user.domain.entity.Black;
import com.calyee.chat.common.user.domain.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 用户角色缓存
 */
@Component
public class UserCache {
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private BlackDao blackDao;

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


    @Cacheable(cacheNames = "user", key = "'userBlackList'")
    public Map<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> collect = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer, Set<String>> res = new HashMap<>();
        collect.forEach((type, list) -> {
            res.put(type, list.stream().map(Black::getTarget).collect(Collectors.toSet()));
        });
        return res;
    }

    @CacheEvict(cacheNames = "user", key = "'userBlackList'")
    public Map<Integer, Set<String>> clearGetBlackMap() {
        return null;
    }
}
