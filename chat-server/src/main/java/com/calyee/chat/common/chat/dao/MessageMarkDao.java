package com.calyee.chat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.chat.domain.entity.MessageMark;
import com.calyee.chat.common.chat.mapper.MessageMarkMapper;
import com.calyee.chat.common.common.domain.enums.NormalOrNoEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息标记表 服务实现类
 * </p>
 *
 * @author OYBW
 * @since 2024-05-21
 */
@Service
public class MessageMarkDao extends ServiceImpl<MessageMarkMapper, MessageMark> {
    public MessageMark get(Long uid, Long msgId, Integer markType) {
        return lambdaQuery().eq(MessageMark::getUid, uid)
                .eq(MessageMark::getMsgId, msgId)
                .eq(MessageMark::getType, markType)
                .one();
    }

    public Integer getMarkCount(Long msgId, Integer markType) {
        return lambdaQuery().eq(MessageMark::getMsgId, msgId)
                .eq(MessageMark::getType, markType)
                .eq(MessageMark::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .count();
    }

    public List<MessageMark> getValidMarkByMsgIdBatch(List<Long> msgIds) {
        return lambdaQuery()
                .in(MessageMark::getMsgId, msgIds)
                .eq(MessageMark::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .list();
    }
}
