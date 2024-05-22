package com.calyee.chat.common.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.calyee.chat.common.chat.domain.entity.Contact;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会话列表 Mapper 接口
 * </p>
 *
 * @author OYBW
 * @since 2024-05-19
 */
public interface ContactMapper extends BaseMapper<Contact> {

    void refreshOrCreateActiveTime(Long roomId, List<Long> memberUidList, Long msgId, Date activeTime);
}
