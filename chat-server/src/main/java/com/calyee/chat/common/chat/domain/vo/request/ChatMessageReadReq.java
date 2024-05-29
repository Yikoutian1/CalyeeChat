package com.calyee.chat.common.chat.domain.vo.request;

import com.calyee.chat.common.common.domain.vo.req.CursorPageBaseReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-07-17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReadReq extends CursorPageBaseReq {
//    @ApiModelProperty("消息id")
    @NotNull
    private Long msgId;

//    @ApiModelProperty("查询类型 1已读 2未读")
    @NotNull
    private Long searchType;
}
