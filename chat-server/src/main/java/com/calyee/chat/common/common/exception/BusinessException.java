package com.calyee.chat.common.common.exception;

import lombok.Data;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.exception
 * @className: BusinessException
 * @author: Caluee
 * @description: 自定义义务异常
 * @date: 2024/03/12 012 20:55
 * @version: 1.0
 */

@Data
public class BusinessException extends RuntimeException {
    protected Integer errorCode;
    protected String errorMsg;

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = CommonErrorEnum.BUSINESS_ERROR.getErrorCode();
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public BusinessException(CommonErrorEnum errorEnum) {
        super(errorEnum.getErrorMsg());
        this.errorMsg = errorEnum.getErrorMsg();
        this.errorCode = errorEnum.getErrorCode();
    }
}
