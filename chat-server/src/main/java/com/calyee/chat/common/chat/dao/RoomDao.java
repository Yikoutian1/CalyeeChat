package com.calyee.chat.common.chat.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.chat.domain.entity.Room;
import com.calyee.chat.common.chat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> {
    public void refreshActiveTime(Long roomId, Long msgId, Date msgTime) {
        lambdaUpdate()
                .eq(Room::getId, roomId)
                .set(Room::getLastMsgId, msgId)
                .set(Room::getActiveTime, msgTime)
                .update();
    }
}
