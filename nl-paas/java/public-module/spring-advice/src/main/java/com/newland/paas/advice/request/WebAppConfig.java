package com.newland.paas.advice.request;

import com.newland.paas.sbcommon.config.FileUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 * 
 * @author wrp
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private FileUploadConfig fileUploadConfig;
    @Autowired
    private ApiRequestInterceptors apiRequestInterceptors;
    /**
     * 配置拦截器
     * 
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiRequestInterceptors).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    /**
     * 配置静态访问资源
     * 
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (null != fileUploadConfig) {
            if (null != fileUploadConfig.getUploadPath() && !"".equals(fileUploadConfig.getUploadPath())) {
                registry.addResourceHandler("/files/**")
                    .addResourceLocations("file:///" + fileUploadConfig.getUploadPath());
            }
        }
        super.addResourceHandlers(registry);
    }
}
