package com.newland.paas.paasservice.sysmgr;

import com.newland.paas.DruidConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.newland.paas.sbcommon.utils.MicrosevicesUtils;

/**
 * @author wrp
 * @since 2017/10/12
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.newland.paas.paasdatamodule.sysmgrdatamodule"})
@ComponentScan
        (basePackages = {"com.newland.paas.paasservice.sysmgr", "com.newland.paas.advice", "com.newland.paas.sbcommon"})
@Import({DruidConfig.class})
public class SysmgrApplication {

    /**
     * 系统管理启动入口
     * @param args
     */
    public static void main(String[] args) {
        MicrosevicesUtils.setSystemProperty(args);
        SpringApplication.run(SysmgrApplication.class, args);
    }

}
