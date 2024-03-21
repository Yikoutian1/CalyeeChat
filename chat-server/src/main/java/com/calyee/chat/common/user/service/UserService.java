package com.calyee.chat.common.user.service;

import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.vo.req.BlackReq;
import com.calyee.chat.common.user.domain.vo.resp.BadgesResp;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/yikoutian1">calyeechat</a>
 * @since 2024-02-28
 */
public interface UserService {

    Long register(User insert);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);

    List<BadgesResp> getBadges(Long uid);

    void wearingBadge(Long uid,Long itemId);

    void black(BlackReq req);
}
