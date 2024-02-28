package com.calyee.chat.common;

import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.User;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private WxMpService wxMpService;

    @Test
    public void testWxCode() throws WxErrorException {
        // 获取二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 10000);
        String url = wxMpQrCodeTicket.getUrl();
        System.out.println(url);
    }
}
