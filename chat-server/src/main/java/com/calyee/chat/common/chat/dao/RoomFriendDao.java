package com.calyee.chat.common.chat.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.chat.domain.entity.RoomFriend;
import com.calyee.chat.common.chat.mapper.RoomFriendMapper;
import com.calyee.chat.common.common.domain.enums.NormalOrNoEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 单聊房间表 服务实现类
 * </p>
 *
 * @author OYBW
 * @since 2024-05-19
 */
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend>  {

    public RoomFriend getByKey(String key) {
        return lambdaQuery().eq(RoomFriend::getRoomKey, key).one();
    }

    public void restoreRoom(Long id) {
        lambdaUpdate()
                .eq(RoomFriend::getId, id)
                .set(RoomFriend::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .update();
    }

    public void disableRoom(String key) {
        lambdaUpdate()
                .eq(RoomFriend::getRoomKey, key)
                .set(RoomFriend::getStatus, NormalOrNoEnum.NOT_NORMAL.getStatus())
                .update();
    }

    public List<RoomFriend> listByRoomIds(List<Long> roomIds) {
        return lambdaQuery()
                .in(RoomFriend::getRoomId, roomIds)
                .list();
    }

    public RoomFriend getByRoomId(Long roomID) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomId, roomID)
                .one();
    }
}
