package com.calyee.chat.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.exception
 * @className: CommonErrorEnum
 * @author: Caluee
 * @description: 全局异常参数定义
 * @date: 2024/03/12 012 20:30
 * @version: 1.0
 */

@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum{
    BUSINESS_ERROR(0, "{0}"),
    PARAM_INVALID(-2, "参数校验失败"),
    SYSTEM_ERROR(-1, "系统出小差了，请稍后再试"), // 业务异常 >1
    ;
    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
