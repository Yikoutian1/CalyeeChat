package com.calyee.chat.common.chat.domain.vo.request.member;


import com.calyee.chat.common.common.domain.vo.req.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2023-07-17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberReq extends CursorPageBaseReq {
//    @ApiModelProperty("房间号")
    private Long roomId = 1L;
}
