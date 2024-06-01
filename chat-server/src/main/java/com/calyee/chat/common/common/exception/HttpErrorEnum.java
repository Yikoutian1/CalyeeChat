package com.calyee.chat.common.common.exception;

import cn.hutool.http.ContentType;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import com.calyee.chat.common.common.utils.JsonUtils;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.exception
 * @className: HttpErrorEnum
 * @author: Caluee
 * @description: 错误码枚举
 * @date: 2024/03/10 010 20:29
 * @version: 1.0
 */
@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401, "登录失效，请重新登录");
    /**
     * http码
     */
    private Integer httpCode;
    /**
     * 描述
     */
    private String desc;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Headers","*");
        response.setHeader("Access-Control-Allow-Method", "*");
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(httpCode, desc)));
    }
}
