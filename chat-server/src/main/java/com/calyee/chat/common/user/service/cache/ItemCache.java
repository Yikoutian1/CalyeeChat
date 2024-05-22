package com.calyee.chat.common.user.service.cache;


import com.calyee.chat.common.user.dao.ItemConfigDao;
import com.calyee.chat.common.user.domain.entity.ItemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date: 2024/4/2 19:45
 */
@Component
public class ItemCache {
    // 如果缓存中存在指定键值的数据，则直接返回缓存数据
    // 否则执行方法体，并将结果存入缓存
    @Autowired
    private ItemConfigDao itemConfigDao;

    @Cacheable(cacheNames = "item",key ="'itemsByType:' + #itemType")
    public List<ItemConfig> getByType(Integer itemType){
        return itemConfigDao.getValidByType(itemType);
    }

    @CacheEvict(cacheNames = "item",key ="'itemsByType:' + #itemType")//清空缓存
    public void evictByType(Integer itemType){
    }
    @Cacheable(cacheNames = "item", key = "'item:'+#itemId")
    public ItemConfig getById(Long itemId) {
        return itemConfigDao.getById(itemId);
    }

}
