package com.calyee.chat.common.user.service.impl;

import com.calyee.chat.common.user.domain.enums.RoleEnum;
import com.calyee.chat.common.user.service.IRoleService;
import com.calyee.chat.common.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        Set<Long> roleSetByUid = userCache.getRoleSetByUid(uid);
        return isAdmin(roleSetByUid) || roleSetByUid.contains(roleEnum.getId());
    }

    private boolean isAdmin(Set<Long> roleSet) {
        return roleSet.contains(RoleEnum.ADMIN.getId());
    }
}
