package com.calyee.chat.common.chat.service;


import com.calyee.chat.common.chat.domain.vo.request.admin.AdminAddReq;
import com.calyee.chat.common.chat.domain.vo.request.admin.AdminRevokeReq;
import com.calyee.chat.common.chat.domain.vo.request.member.MemberExitReq;
import com.calyee.chat.common.user.domain.entity.User;

/**
 * <p>
 * 群成员表 服务类
 * </p>
 *
 * @author OYBW
 * @since 2024-05-19
 */
public interface IGroupMemberService {
    /**
     * 增加管理员
     *
     * @param uid     用户ID
     * @param request 请求信息
     */
    void addAdmin(Long uid, AdminAddReq request);

    /**
     * 撤销管理员
     *
     * @param uid     用户ID
     * @param request 请求信息
     */
    void revokeAdmin(Long uid, AdminRevokeReq request);

    /**
     * 退出群聊
     *
     * @param uid     用户ID
     * @param request 请求信息
     */
    void exitGroup(Long uid, MemberExitReq request);

    /**
     * 是否存在主群
     * @param id
     * @return true 在，false 不在
     */
    boolean isInGroup(Long id);

    /**
     * 加入主群
     * @param user
     */
    void addMainGroup(User user);
}
