package com.calyee.chat.common.user.service;

import com.calyee.chat.common.user.domain.dto.ItemInfoDTO;
import com.calyee.chat.common.user.domain.dto.SummeryInfoDTO;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.vo.req.user.BlackReq;
import com.calyee.chat.common.user.domain.vo.req.user.ItemInfoReq;
import com.calyee.chat.common.user.domain.vo.req.user.ModifyNameReq;
import com.calyee.chat.common.user.domain.vo.req.user.SummeryInfoReq;
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

    UserInfoResp getUserinfo(Long uid);

    void modifyName(Long uid, ModifyNameReq modifyName);


    List<BadgesResp> badges(Long uid);

    void wearingBadge(Long uid, Long itemId);

    void black(BlackReq req);

    /**
     * 获取用户汇总信息
     *
     * @param req
     * @return
     */
    List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req);

    List<ItemInfoDTO> getItemInfo(ItemInfoReq req);

}