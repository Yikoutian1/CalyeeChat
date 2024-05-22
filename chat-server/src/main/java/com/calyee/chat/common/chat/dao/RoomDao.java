package com.calyee.chat.common.chat.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.chat.domain.entity.Room;
import com.calyee.chat.common.chat.mapper.RoomMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author OYBW
 * @since 2024-05-19
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> {

}
