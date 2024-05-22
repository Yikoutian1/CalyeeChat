package com.calyee.chat.common.chat.service.cache;


import com.calyee.chat.common.chat.dao.GroupMemberDao;
import com.calyee.chat.common.chat.dao.MessageDao;
import com.calyee.chat.common.chat.dao.RoomGroupDao;
import com.calyee.chat.common.chat.domain.entity.RoomGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Description: 群成员相关缓存
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 */
@Component
public class GroupMemberCache {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private RoomGroupDao roomGroupDao;
    @Autowired
    private GroupMemberDao groupMemberDao;

    @Cacheable(cacheNames = "member", key = "'groupMember'+#roomId")
    public List<Long> getMemberUidList(Long roomId) {
        RoomGroup roomGroup = roomGroupDao.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            return null;
        }
        return groupMemberDao.getMemberUidList(roomGroup.getId());
    }

    @CacheEvict(cacheNames = "member", key = "'groupMember'+#roomId")
    public List<Long> evictMemberUidList(Long roomId) {
        return null;
    }

}
