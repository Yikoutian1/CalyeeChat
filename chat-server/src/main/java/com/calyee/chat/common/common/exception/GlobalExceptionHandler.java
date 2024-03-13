package com.calyee.chat.common.common.exception;

import cn.hutool.core.text.StrBuilder;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.exception
 * @className: GlobalExceptionHandler
 * @author: Caluee
 * @description: 全局异常处理
 * @date: 2024/03/12 012 20:11
 * @version: 1.0
 */
@Slf4j
@RestControllerAdvice //对所有的controller出来的异常都会通知
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StrBuilder errorMsg = new StrBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMsg.append(fieldError.getField())
                    .append(fieldError.getDefaultMessage())
                    .append(",");
        }
        String errRes = errorMsg.toString().substring(0, errorMsg.length() - 1); // 去除最后的逗号
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getCode(), errRes);
    }

    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<?> businessException(BusinessException e) {
        log.info("business exception! The reason is:{}", e.getMessage());
        return ApiResult.fail(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 最后的防线 顶级异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public ApiResult<?> throwable(Throwable e) {
        log.error("system exception! The reason is:{}", e.getMessage(), e);
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }
}
