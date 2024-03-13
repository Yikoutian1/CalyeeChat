package com.calyee.chat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calyee.chat.common.user.domain.entity.ItemConfig;
import com.calyee.chat.common.user.mapper.ItemConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/yikoutian1">calyeechat</a>
 * @since 2024-03-06
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig> {

    public List<ItemConfig> getValidByType(Integer itemType) {
        return lambdaQuery()
                .eq(ItemConfig::getType, itemType)
                .list();
    }
}
