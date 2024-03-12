package com.calyee.chat.common.user.service.adapter;

import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import lombok.Data;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service.adapter
 * @className: UserAdapter
 * @author: Caluee
 * @description: 用户适配器
 * @date: 2024/03/02 002 19:35
 * @version: 1.0
 */
@Data
public class UserAdapter {

    public static User buildUserSave(String openId) {
        return User.builder()
                .openId(openId)
                .build();

    }

    public static User buildAuthorizeUserInfo(Long uid, WxOAuth2UserInfo user) {
        String avatar = user.getHeadImgUrl();
        Integer sex = user.getSex();
        String name = user.getNickname();
        String openid = user.getOpenid();
        return User.builder()
                .id(uid)
                .openId(openid)
                .avatar(avatar)
                .name(name)
                .sex(sex)
                .build();

    }

    public static UserInfoResp buildUserInfo(User user, Integer modifyNameCardCount) {
        UserInfoResp vo = new UserInfoResp();
        vo.setId(user.getId());
        vo.setAvatar(user.getAvatar());
        vo.setSex(user.getSex());
        vo.setName(user.getName());
        vo.setModifyNameChance(modifyNameCardCount);
        return vo;
    }
}
