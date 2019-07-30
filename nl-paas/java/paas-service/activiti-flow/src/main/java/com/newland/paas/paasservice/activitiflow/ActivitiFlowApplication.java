package com.newland.paas.paasservice.activitiflow;

import com.newland.paas.DruidConfig;
import com.newland.paas.sbcommon.utils.MicrosevicesUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 工单服务
 *
 * @author WRP
 * @since 2019/1/30
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.newland.paas.paasdatamodule.activitiflowdatamodule"})
@ComponentScan(basePackages = {"com.newland.paas.paasservice.activitiflow",
        "com.newland.paas.advice", "com.newland.paas.sbcommon"})
@Import({DruidConfig.class})
public class ActivitiFlowApplication {

    /**
     * main
     *
     * @param args
     */
    public static void main(String[] args) {
        MicrosevicesUtils.setSystemProperty(args);
        SpringApplication.run(ActivitiFlowApplication.class, args);
    }

}

