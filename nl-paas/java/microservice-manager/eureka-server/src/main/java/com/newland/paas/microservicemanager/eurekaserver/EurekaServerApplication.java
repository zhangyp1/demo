package com.newland.paas.microservicemanager.eurekaserver;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.microservicemanager.eurekaserver.utils.MicrosevicesUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author WRP
 * @since 2019/2/22
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableEurekaServer
public class EurekaServerApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MicrosevicesUtils.setSystemProperty(args);
        Log log = LogFactory.getLogger(EurekaServerApplication.class);
        log.info(LogProperty.LOGCONFIG_DEALID, "eureka starting...");
        SpringApplication.run(EurekaServerApplication.class, args);
        log.info(LogProperty.LOGCONFIG_DEALID, "eureka started...");
    }

}
