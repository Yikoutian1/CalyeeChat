package com.calyee.chat.common.chat.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.chat.domain.entity.RoomGroup;
import com.calyee.chat.common.chat.mapper.RoomGroupMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群聊房间表 服务实现类
 * </p>
 *
 * @author OYBW
 * @since 2024-05-19
 */
@Service
public class RoomGroupDao extends ServiceImpl<RoomGroupMapper, RoomGroup> {

    public List<RoomGroup> listByRoomIds(List<Long> roomIds) {
        return lambdaQuery()
                .in(RoomGroup::getRoomId, roomIds)
                .list();
    }

    public RoomGroup getByRoomId(Long roomId) {
        return lambdaQuery()
                .eq(RoomGroup::getRoomId, roomId)
                .one();
    }
}
