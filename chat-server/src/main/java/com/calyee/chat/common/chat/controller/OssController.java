package com.calyee.chat.common.chat.controller;


import com.calyee.chat.common.chat.domain.vo.request.oss.UploadUrlReq;
import com.calyee.chat.common.chat.service.OssService;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import com.calyee.chat.common.common.utils.RequestHolder;
import com.calyee.oss.domain.OssResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Description: oss控制层
 */
@RestController
@RequestMapping("/capi/oss")
@CrossOrigin
//@Api(tags = "oss相关接口")
public class OssController {
    @Autowired
    private OssService ossService;

    @GetMapping("/upload/url")
//    @ApiOperation("获取临时上传链接")
    public ApiResult<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
        return ApiResult.success(ossService.getUploadUrl(RequestHolder.get().getUid(), req));
    }
}
