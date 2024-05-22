package com.calyee.chat.common;

import com.calyee.chat.common.common.domain.enums.IdempotentEnum;
import com.calyee.chat.common.common.utils.JwtUtils;
import com.calyee.chat.common.common.utils.RedisUtils;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.domain.enums.ItemEnum;
import com.calyee.chat.common.user.service.IUserBackpackService;
import com.calyee.chat.common.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common
 * @className: DaoTest
 * @author: Caluee
 * @description: 测试Dao功能是否可以正常使用
 * @date: 2024/02/28 028 20:06
 * @version: 1.0
 */
@Slf4j
@SpringBootTest(classes = ChatCustomApplication.class)
@RunWith(SpringRunner.class) // 注入依赖环境
@ActiveProfiles(value = "prod")
public class DaoTest {

    public static final long UID = 11005L;
    @Autowired
    private UserDao userDao;

    @Test
    public void test1() {
        User user = userDao.getById(1);
    }


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private LoginService loginService;

    /**
     * 登录获取token
     */
    @Test
    public void UserLogin() {
        String token = loginService.login(11005L);
        // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjExMDA1LCJjcmVhdGVUaW1lIjoxNzEwMDc2NjkzfQ.lsYJ-63HkThrvu-Yaj_tSBypnH5DgalUtxTUGRDACr4
        System.out.println(token);
    }

    @Test
    public void testJWT() {
        System.out.println(jwtUtils.createToken(1111L));
        System.out.println(jwtUtils.createToken(1111L));
    }


    /**
     * redis引入
     */
    @Test
    public void baseRedis() {
        RedisUtils.set("name", "卷心菜");
        String name = RedisUtils.getStr("name");
        System.out.println(name); //卷心菜
        RedisUtils.del("name");
    }

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test2() {
        RLock lock = redissonClient.getLock("123");
        lock.lock();
        System.out.println();
        lock.unlock();
    }
    @Test
    public void test3() {
        int a = 10;
        Integer aa = 10;
        System.out.println(aa.equals(a));
        int b = 129;
        Integer bb = 129;
        System.out.println(bb.equals(b));
    }

    @Autowired
    private IUserBackpackService userBackpackService;

    /**
     * 幂等的发放
     */
    @Test
    public void acquireItem() {
        userBackpackService.acquireItem(UID, ItemEnum.PLANET.getId(), IdempotentEnum.UID, UID + "");
    }


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void threadTest() {
        threadPoolTaskExecutor.execute(() -> {
            if (1 == 1) {
                log.error("log error");
                throw new RuntimeException("123");
            }
        });
    }


    @Test
    public void redis() {
        long true_value = 11005L;
        long false_value = 11111L;
        String s = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjExMDA1LCJjcmVhdGVUaW1lIjoxNzA5NTUzMDMyfQ.vn6I3s1dluv8VfieE46iyLn2Sv5hYu4_JSbRZc-Y8J4";
        Assert.assertEquals(true_value, (long) loginService.getValidUid(s));
    }


    @Autowired
    private WxMpService wxMpService;

    @Test
    public void testWxCode() throws WxErrorException {
        // 获取二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 10000);
        String url = wxMpQrCodeTicket.getUrl();
        System.out.println(url);
    }
}
