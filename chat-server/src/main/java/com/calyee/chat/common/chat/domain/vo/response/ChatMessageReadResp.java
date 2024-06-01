package com.calyee.chat.common.chat.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReadResp {
//    @ApiModelProperty("已读或者未读的用户uid")
    private Long uid;
}
