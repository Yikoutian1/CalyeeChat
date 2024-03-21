package com.calyee.chat.common.user.service;

import com.calyee.chat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/yikoutian1">calyeechat</a>
 * @since 2024-03-21
 */
public interface IRoleService {
    /**
     * 是否拥有某个权限 临时写法
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);
}
