package com.newland.paas.configtool;

import com.newland.paas.configtool.common.MicrosevicesUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 配置管理
 *
 * @author WRP
 * @since 2019/1/14
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.newland.paas.configtool.dao"})
@EnableTransactionManagement
@Controller
public class ConfigToolApplication {

    /**
     * 启动
     *
     * @param args
     */
    public static void main(String[] args) {
        MicrosevicesUtils.setSystemProperty(args);
        SpringApplication.run(ConfigToolApplication.class, args);
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

}
