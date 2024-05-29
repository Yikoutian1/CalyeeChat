package com.calyee.chat.common.user.domain.vo.resp.friend;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Description: 好友校验
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendUnreadResp {

//    @ApiModelProperty("申请列表的未读数")
    private Integer unReadCount;

}
