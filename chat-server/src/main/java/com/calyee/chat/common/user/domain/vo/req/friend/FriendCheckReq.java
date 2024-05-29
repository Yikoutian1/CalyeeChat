package com.calyee.chat.common.user.domain.vo.req.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * Description: 好友校验
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckReq {

    @NotEmpty
    @Size(max = 50)
//    @ApiModelProperty("校验好友的uid")
    private List<Long> uidList;

}
