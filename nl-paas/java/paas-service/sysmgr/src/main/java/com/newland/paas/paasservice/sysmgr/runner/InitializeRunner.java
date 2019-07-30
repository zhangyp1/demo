package com.newland.paas.paasservice.sysmgr.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 程序启动时初始化
 *
 * @author WRP
 * @author zhongqingjiang
 */
@Component
public class InitializeRunner implements CommandLineRunner {

    @Autowired
    private Upgrader upgrader;
    @Autowired
    private RoleMenuCorrector roleMenuCorrector;
    @Autowired
    private RoleApiCacheRefresher roleApiCacheRefresher;

    @Override
    public void run(String... args) throws Exception {
        upgrader.run();
        roleMenuCorrector.correct();
        roleApiCacheRefresher.refreshAll();
    }

}
