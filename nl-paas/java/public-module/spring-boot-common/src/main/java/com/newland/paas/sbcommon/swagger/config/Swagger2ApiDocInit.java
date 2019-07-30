package com.newland.paas.sbcommon.swagger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.newland.paas.common.util.Json;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.sbcommon.constants.RedisKeyConstants;

import io.swagger.models.Swagger;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

/**
 * 
 * @author SongDi
 * @date 2018/08/08
 */
@Configuration
public class Swagger2ApiDocInit implements CommandLineRunner {
    private static final Log logger = LogFactory.getLogger(Swagger2ApiDocInit.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private DocumentationCache documentationCache;
    @Autowired
    private ServiceModelToSwagger2Mapper mapper;

    @Override

    public void run(String... args) throws Exception {

        String groupName = Docket.DEFAULT_GROUP_NAME;
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation != null) {
            Swagger swagger = mapper.mapDocumentation(documentation);

            redisTemplate.opsForValue().set(RedisKeyConstants.REDIS_PAAS_API_DOCS, Json.toJson(swagger));
        }

    }
}
