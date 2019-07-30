package com.newland.paas.microservicemanager.configcenter;

import com.newland.paas.DruidConfig;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.microservicemanager.configcenter.utils.MicrosevicesUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 配置中心
 *
 * @author WRP
 * @since 2018/11/28
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableConfigServer
@EnableEurekaClient
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.newland.paas.paasdatamodule.configcenterdatamodule"})
@Import({DruidConfig.class})
public class ConfigCenterApplication {

    /**
     * 启动
     *
     * @param args
     */
    public static void main(String[] args) {
        MicrosevicesUtils.setSystemProperty(args);
        Log log = LogFactory.getLogger(ConfigCenterApplication.class);
        log.info(LogProperty.LOGCONFIG_DEALID, "config starting...");
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(ConfigCenterApplication.class);
        springApplicationBuilder.web(true);
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addLast(
                new PropertiesPropertySource("micro-service", MicrosevicesUtils.getPropertiesByEnv()));
        springApplicationBuilder.environment(environment);
        springApplicationBuilder.run(args);
        log.info(LogProperty.LOGCONFIG_DEALID, "config started...");
    }
}
