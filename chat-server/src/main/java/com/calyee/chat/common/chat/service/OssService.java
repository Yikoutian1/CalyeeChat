package com.calyee.chat.common.chat.service;


import com.calyee.chat.common.chat.domain.vo.request.oss.UploadUrlReq;
import com.calyee.oss.domain.OssResp;

/**
 * <p>
 * oss 服务类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 */
public interface OssService {

    /**
     * 获取临时的上传链接
     */
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}
