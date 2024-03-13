package com.calyee.chat.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.calyee.chat.common.common.domain.enums.YesOrNoEnum;
import com.calyee.chat.common.user.domain.entity.ItemConfig;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.entity.UserBackpack;
import com.calyee.chat.common.user.domain.vo.resp.BadgesResp;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import lombok.Data;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static List<BadgesResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigs.stream().map(i -> {
                    BadgesResp resp = new BadgesResp();
                    BeanUtil.copyProperties(i, resp);
                    resp.setObtain(obtainItemSet.contains(i.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    resp.setWearing(Objects.equals(i.getId(), user.getItemId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    return resp;
                }).sorted(Comparator.comparing(BadgesResp::getWearing, Comparator.reverseOrder())// 如果佩戴了则倒叙的展示  展示在最前面
                        .thenComparing(BadgesResp::getObtain, Comparator.reverseOrder()))        // 是否获得排序 如果获得也需要排到前面
                .collect(Collectors.toList());
    }
}
