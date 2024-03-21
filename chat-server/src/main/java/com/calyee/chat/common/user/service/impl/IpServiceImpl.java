package com.calyee.chat.common.user.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.calyee.chat.common.common.domain.vo.resp.ApiResult;
import com.calyee.chat.common.user.dao.UserDao;
import com.calyee.chat.common.user.domain.entity.IpDetail;
import com.calyee.chat.common.user.domain.entity.IpInfo;
import com.calyee.chat.common.user.domain.entity.User;
import com.calyee.chat.common.user.service.IpService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.user.service.impl
 * @className: IpServiceImpl
 * @author: Calyee
 * @description: IP解析 异步版
 * @date: 2024/03/20 020 22:51
 * @version: 1.0
 */
@Service
@Slf4j
public class IpServiceImpl implements IpService, DisposableBean {

    private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));

    @Autowired
    private UserDao userDao;

    @Override
    public void refreshIpDetailAsync(Long id) {
        executor.execute(() -> {
            User user = userDao.getById(id);
            IpInfo ipInfo = user.getIpInfo();
            if (Objects.isNull(ipInfo)) {
                return; // 没有Ip信息
            }
            String refreshIp = ipInfo.needRefreshIp();
            if (StringUtils.isBlank(refreshIp)) {
                return;
            }
            IpDetail ipDetail = tryGetIpDetailOrNullTreeTimes(refreshIp);
            if (Objects.nonNull(ipDetail)) {// 不是空则刷新ip并且保存
                ipInfo.refreshIpDetail(ipDetail);
                User update = new User();
                update.setId(id);
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
            }
        });
    }

    private static IpDetail tryGetIpDetailOrNullTreeTimes(String ip) {
        // 只获取三次ip
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if (Objects.nonNull(ipDetail)) {
                return ipDetail;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("tryGetIpDetailOrNullTreeTimes InterruptedException", e);
            }
        }
        return null;
    }

    public static IpDetail getIpDetailOrNull(String ip) {
        String body = HttpUtil.get("https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc");
        try {
            ApiResult<IpDetail> result = JSONUtil.toBean(body, new TypeReference<ApiResult<IpDetail>>() {
            }, false);
            if (result.isSuccess()) {
                return result.getData();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void main(String[] args) {
        Date begin = new Date();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                // https://ip.taobao.com/outGetIpInfo?ip=172.18.83.51&accessKey=alibaba-inc
                IpDetail ipDetail = tryGetIpDetailOrNullTreeTimes("172.18.83.51");
                if (Objects.nonNull(ipDetail)) {
                    Date end = new Date();
                    System.out.println(String.format("目前耗时%dms", (end.getTime() - begin.getTime())));
                }
            });
        }
    }

    @Override
    public void destroy() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {//最多等30秒，处理不完就拉倒
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", executor);
            }
        }
    }
}
