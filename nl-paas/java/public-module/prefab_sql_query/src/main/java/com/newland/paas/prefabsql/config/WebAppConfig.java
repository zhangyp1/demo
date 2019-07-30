package com.newland.paas.prefabsql.config;

import com.newland.paas.dataaccessmodule.model.PrefabSql;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.prefabsql.interceptor.ApiRequestInterceptors;
import com.newland.paas.prefabsql.service.PrefabSqlService;
import com.newland.paas.prefabsql.util.SqlNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * web配置
 *
 * @author wrp
 * @since 2018/6/11
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    private static final Log LOG = LogFactory.getLogger(WebAppConfig.class);

    @Autowired
    private PrefabSqlService prefabSqlService;

    @Value("${server.port}")
    private Integer serverPort;
    @Value("${server.context-path}")
    private String serverContextPath;

    /**
     * 配置拦截器,多个拦截器组成一个拦截器链
     * 1:addPathPatterns 用于添加拦截规则
     * 2:excludePathPatterns 用户排除拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(apiRequestInterceptors()).addPathPatterns("/**");
//        super.addInterceptors(registry);
    }

    @Bean
    ApiRequestInterceptors apiRequestInterceptors() {
        return new ApiRequestInterceptors();
    }

    /**
     * 项目启动初始化
     */
    @PostConstruct
    public void init() {
        // 初始化预置查询数据
        List<PrefabSql> prefabSqlList = prefabSqlService.findAll();
        SqlNameUtils.loadPrefabSql(prefabSqlList);
    }

    /**
     * 定时器，刷新缓存
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void scheduled() {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "=============scheduled begin=====================");
        List<PrefabSql> prefabSqlList = prefabSqlService.findAll();
        SqlNameUtils.loadPrefabSql(prefabSqlList);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "=============scheduled end=====================");
        LOG.info(LogProperty.LOGTYPE_DETAIL, "=============config Refresh begin=====================");

        String serverContextPathTmp = "";
        if (!"/".equals(serverContextPath)) {
            serverContextPathTmp = serverContextPath;
        }
        String uri = "http://localhost:" + serverPort + serverContextPathTmp + "/refresh";
        LOG.info(LogProperty.LOGTYPE_DETAIL, "=============config Refresh uri:" + uri);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(null, headers);
        String result = restTemplate.postForObject(uri, formEntity, String.class);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "=============config Refresh result:" + result);

        LOG.info(LogProperty.LOGTYPE_DETAIL, "=============config Refresh end=====================");
    }

}
