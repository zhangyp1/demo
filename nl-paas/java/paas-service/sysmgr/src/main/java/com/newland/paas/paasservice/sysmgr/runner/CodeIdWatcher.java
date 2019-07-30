package com.newland.paas.paasservice.sysmgr.runner;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.sysmgr.service.CodeIdService;
import com.newland.paas.sbcommon.utils.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * CODE_ID同步
 *
 * @author WRP
 * @since 2019/1/10
 */
@Service
public class CodeIdWatcher implements ApplicationListener<ApplicationReadyEvent> {

    private static final Log LOG = LogFactory.getLogger(CodeIdWatcher.class);

    @Autowired
    private CodeIdService codeIdService;

    @Value("${groupobj.sync.timeout}")
    private Integer timeout;
    @Value("${groupobj.sync.sleepTime}")
    private Integer sleepTime;

    /**
     * stop flag
     */
    private boolean stoped = false;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format(
                "同步CODE对应ID数据 开启线程...,timeout:{0},sleepTime:{1}", timeout, sleepTime));
        new Thread(() -> {
            Object waitLock = new Object();
            long sleepDur = TimeUnit.SECONDS.toMillis(sleepTime);
            RedisLockUtil redisLockUtil = new RedisLockUtil();
            redisLockUtil.setKey("sysmgrSyncCodeIdKey");
            redisLockUtil.setTimeout((int) TimeUnit.MINUTES.toMillis(timeout));
            while (!stoped) {
                LOG.info(LogProperty.LOGTYPE_DETAIL, "同步CODE对应ID数据 开始循环...");
                try {
                    redisLockUtil.executeWhenGetLock(() -> {
                        LOG.info(LogProperty.LOGTYPE_CALL, "调用同步CODE对应ID数据方法");
                        codeIdService.syncCodeId();
                    });
                } catch (Exception ex) {
                    LOG.error(LogProperty.LOGTYPE_DETAIL, "0000", ex);
                }
                synchronized (waitLock) {
                    try {
                        waitLock.wait(sleepDur);
                    } catch (InterruptedException e) {
                        LOG.error(LogProperty.LOGTYPE_DETAIL, "0000", e, "线程休眠失败,退出操作执行状态监视循环");
                        stoped = true;
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();

    }

}
