package com.newland.paas.sbcommon.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobSubmitOut;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobSubmitVo;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;

/**
 * 执行引擎客户端
 * 
 * @author SongDi
 * @date 2018/11/05
 */
@Service
public class ExecuteEngineClient {

    private static final Log LOG = LogFactory.getLogger(ExecuteEngineClient.class);
    /**
     * 微服务之间调用工具类
     */
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    /**
     * 微服务配置（config/common.yml）
     */
    @Autowired
    private MicroservicesProperties microservicesProperties;

    /**
     * 提交一个任务
     * 
     * @param jobSubmitVo
     * @param token
     * @return
     */
    public JobSubmitOut submit(JobSubmitVo jobSubmitVo, String token) {

        if (StringUtils.isEmpty(jobSubmitVo.getServiceId())) {
            String appName = SpringContextUtil.getContext().getEnvironment().getProperty("spring.application.name");
            LOG.info(LogProperty.LOGTYPE_DETAIL, "appname", appName);
            jobSubmitVo.setServiceId(appName);
        }
        BasicRequestContentVo<JobSubmitVo> request = new BasicRequestContentVo<JobSubmitVo>();
        request.setParams(jobSubmitVo);
        BasicResponseContentVo<JobSubmitOut> result =
            restTemplateUtils.postForTokenEntity(microservicesProperties.getExecuteEngine(), "/v1/job/submit", token,
                request, new ParameterizedTypeReference<BasicResponseContentVo<JobSubmitOut>>() {});
        return result.getContent();
    }

}
