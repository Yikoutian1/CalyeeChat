package com.calyee.chat.common.common.domain.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdReqVO {
    //    @ApiModelProperty("id")
    @NotNull
    private long id;
}
