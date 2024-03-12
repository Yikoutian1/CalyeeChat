package com.calyee.chat.common.common.exception;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.exception
 * @className: ErrorEnum
 * @author: Caluee
 * @description: 定义错误规范
 * @date: 2024/03/12 012 20:41
 * @version: 1.0
 */

public interface ErrorEnum {
    Integer getErrorCode();

    String getErrorMsg();
}
