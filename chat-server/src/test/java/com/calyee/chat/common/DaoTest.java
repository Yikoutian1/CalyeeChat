package com.calyee.chat.common;

import com.calyee.chat.common.common.utils.JwtUtils;
import com.calyee.chat.common.common.utils.RedisUtils;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
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
@SpringBootTest
@RunWith(SpringRunner.class) // 注入依赖环境
public class DaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void test1() {
        User user = userDao.getById(1);
    }


    @Autowired
    private JwtUtils jwtUtils;

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
        String name = RedisUtils.get("name");
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

    @Autowired
    private LoginService loginService;

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
