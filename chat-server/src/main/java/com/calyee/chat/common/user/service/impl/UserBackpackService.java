package com.calyee.chat.common.user.service.impl;

import com.calyee.chat.common.common.annotation.RedissonLock;
import com.calyee.chat.common.common.domain.enums.IdempotentEnum;
import com.calyee.chat.common.common.domain.enums.YesOrNoEnum;
import com.calyee.chat.common.common.service.LockService;
import com.calyee.chat.common.user.dao.UserBackpackDao;
import com.calyee.chat.common.user.domain.entity.UserBackpack;
import com.calyee.chat.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserBackpackService implements IUserBackpackService {


    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    @Lazy
    private UserBackpackService userBackpackService;
    @Autowired
    private LockService lockService;

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEum, String busineesId) {
        String idempotent = getIdempotentEum(itemId, idempotentEum, busineesId);
//        ((UserBackpackService)AopContext.currentProxy()).doAcquireItem(uid, itemId, idempotent);
        userBackpackService.doAcquireItem(uid, itemId, idempotent);
    }

    @RedissonLock(key = "#idempotent", waitTime = 5000)//等五秒
    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
        UserBackpack userBackpack = userBackpackDao.getByIdempotent(idempotent);
        if (Objects.nonNull(userBackpack)) {
            return;
        }
        //发物品
        UserBackpack inset = UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent).build();
        userBackpackDao.save(inset);
    }

    private String getIdempotentEum(Long itemId, IdempotentEnum idempotentEum, String busineesId) {
        return String.format("%d_%d_%S", itemId, idempotentEum.getType(), busineesId);
    }
}
