package com.newland.paas.prefabsql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.newland.paas.dataaccessmodule.dao"})
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.newland.paas.prefabsql", "com.newland.paas.aop" })
public class PrefabSqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrefabSqlApplication.class, args);
    }
    
    
}
