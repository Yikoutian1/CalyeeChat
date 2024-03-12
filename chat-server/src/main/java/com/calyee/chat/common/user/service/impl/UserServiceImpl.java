package com.calyee.chat.common.user.service.impl;

import com.calyee.chat.common.common.utils.AssertUtil;
import com.calyee.chat.common.user.dao.UserBackpackDao;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.entity.UserBackpack;
import com.calyee.chat.common.user.domain.enums.ItemEnum;
import com.calyee.chat.common.user.domain.vo.resp.UserInfoResp;
import com.calyee.chat.common.user.service.UserService;
import com.calyee.chat.common.user.service.adapter.UserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service.impl
 * @className: UserServiceImpl
 * @author: Caluee
 * @description: 用户服务实现类
 * @date: 2024/03/02 002 19:38
 * @version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserBackpackDao userBackpackDao;

    @Override
    @Transactional // 保证事件和用户注册的一致性
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        // TODO 用户注册的事件  -> 谁订阅就给谁发送通知
        return insert.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer modifyNameCardCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId()); // 获取用户改名卡个数
        return UserAdapter.buildUserInfo(user, modifyNameCardCount);
    }

    @Override
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        // 断言工具 业务校验工具类
        AssertUtil.isEmpty(oldUser,"名字已经被抢占了，换个名字吧");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem,"改名卡不够了，等后续活动领取");
        // 使用改名卡

    }
}
