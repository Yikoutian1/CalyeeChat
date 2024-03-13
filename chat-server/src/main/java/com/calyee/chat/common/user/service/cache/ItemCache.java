package com.calyee.chat.common.user.service.cache;

import com.calyee.chat.common.user.dao.ItemConfigDao;
import com.calyee.chat.common.user.domain.entity.ItemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service.cache
 * @className: ItemCache
 * @author: Caluee
 * @description: item缓存
 * @date: 2024/03/13 013 19:37
 * @version: 1.0
 */
@Component
public class ItemCache {
    @Autowired
    private ItemConfigDao itemConfigDao;

    // @CachePut   // 主动刷新缓存
    // 获取缓存 如果没有则执行下面
    @Cacheable(cacheNames = "item", key = "'itemsByType: '+ #itemType")
    public List<ItemConfig> getByType(Integer itemType) {
        return itemConfigDao.getValidByType(itemType);
    }
    // 清空缓存
    @CacheEvict(cacheNames = "item", key = "'itemsByType: '+ #itemType")
    public void evictByType(Integer itemType) {
    }
}
