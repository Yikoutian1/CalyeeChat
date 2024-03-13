package com.calyee.chat.common.user.service.impl;


import com.calyee.chat.common.common.domain.enums.YesOrNoEnum;
import com.calyee.chat.common.common.service.LockService;
import com.calyee.chat.common.user.dao.UserBackpackDao;
import com.calyee.chat.common.user.domain.entity.UserBackpack;
import com.calyee.chat.common.user.domain.enums.IdempotentEnum;
import com.calyee.chat.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserBackServiceImpl implements IUserBackpackService {
    @Autowired
    private LockService lockService;
    @Autowired
    private UserBackpackDao userBackpackDao;

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId); // 根据幂等号 构造分布式锁
        lockService.executeWithLock("acquireItem" + idempotent, () -> {
            UserBackpack backpack = userBackpackDao.getIdempotent(idempotent);// 判断幂等是否存在
            if (Objects.nonNull(backpack)) {
                return; // 不需要给任何异常
            }
            // 发放物品
            UserBackpack build = UserBackpack.builder()
                    .uid(uid)
                    .itemId(itemId)
                    .status(YesOrNoEnum.NO.getStatus())
                    .idempotent(idempotent)
                    .build();
            userBackpackDao.save(build);
        });
    }

    /**
     * 构造幂等号
     */
    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        // 幂等号=itemId+source+businessId
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
