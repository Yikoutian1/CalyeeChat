package com.calyee.chat.common.chat.service.impl;


import com.calyee.chat.common.chat.domain.enums.OssSceneEnum;
import com.calyee.chat.common.chat.domain.vo.request.oss.UploadUrlReq;
import com.calyee.chat.common.chat.service.OssService;
import com.calyee.chat.common.common.utils.AssertUtil;
import com.calyee.oss.MinIOTemplate;
import com.calyee.oss.domain.OssReq;
import com.calyee.oss.domain.OssResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private MinIOTemplate minIOTemplate;

    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        OssSceneEnum sceneEnum = OssSceneEnum.of(req.getScene());
        AssertUtil.isNotEmpty(sceneEnum, "场景有误");
        OssReq ossReq = OssReq.builder()
                .fileName(req.getFileName())
                .filePath(sceneEnum.getPath())
                .uid(uid)
                .build();
        return minIOTemplate.getPreSignedObjectUrl(ossReq);
    }
}
