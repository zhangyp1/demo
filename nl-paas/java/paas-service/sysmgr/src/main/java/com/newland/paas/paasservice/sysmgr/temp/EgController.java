package com.newland.paas.paasservice.sysmgr.temp;

import com.alibaba.fastjson.JSON;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.CodeIdMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.CodeId;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.CodeIdVO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GenerateTokenVO;
import com.newland.paas.sbcommon.codeid.CodeIdUtil;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 例子、示例代码
 *
 * @author WRP
 * @since 2019/1/30
 */
@RestController
@RequestMapping(value = "/sysmgr/v1/eg")
public class EgController {

    private static final Log LOG = LogFactory.getLogger(EgController.class);

    @Autowired
    private CodeIdMapper codeIdMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     *
     */
    @GetMapping(value = "delCodeId", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public void delCodeId() {
        List<CodeId> codeIds = codeIdMapper.selectBySelective(null);
        codeIds.forEach(v -> {
            LOG.info(LogProperty.LOGTYPE_CALL, "PAAS:CODE_ID:" + v.getTableName().toLowerCase());
            redisTemplate.delete("PAAS:CODE_ID:" + v.getTableName().toLowerCase());
        });
    }

    /**
     * 示例接口：根据table_name、code获取信息
     *
     * @param tableName CodeId信息
     * @param code      CodeId信息
     */
    @GetMapping(value = "getId/{tableName}/{code}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String getId(@PathVariable("tableName") String tableName, @PathVariable("code") String code) {
        CodeIdVO codeIdVO = CodeIdUtil.getCodeIdByCode(CodeIdUtil.SYS_TENANT, "test1214");
        LOG.info(LogProperty.LOGTYPE_CALL, "getId,codeIdVO:" + JSON.toJSONString(codeIdVO));
        return CodeIdUtil.getByCode(tableName, code);
    }

    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;

    /**
     * 示例接口：生成token
     */
    @GetMapping(value = "generateToken", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String generateToken() {
        String account = "zhadmin";
        String tenantCode = "test1214";
        GenerateTokenVO generateTokenVO = new GenerateTokenVO();
        generateTokenVO.setAccount(account);
        generateTokenVO.setTenantCode(tenantCode);
        BasicRequestContentVo<GenerateTokenVO> reqVo = new BasicRequestContentVo<>();
        reqVo.setParams(generateTokenVO);
        // 生成token
        BasicResponseContentVo<String> response = restTemplateUtils.postForEntity(microservicesProperties.getAuth(),
                "/v1/token/generateToken", reqVo, new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
                });

        // 用token去访问需要用户信息的接口
        restTemplateUtils.getForTokenEntity(microservicesProperties.getAuth(),
                "getId/SYS_TENANT/test1214",
                response.getContent(), new HashMap<>(),
                new ParameterizedTypeReference<BasicResponseContentVo<String>>() {
                });

        return response.getContent();
    }

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 示例接口：获取项目所有接口
     *
     * @return
     */
    @GetMapping(value = "getAllUrl", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String getAllUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("url_id").append("--")
                .append("service_id").append("--")
                .append("content_path").append("--")
                .append("method").append("--")
                .append("uri").append("--")
                .append("class_method").append("--").append('\n');

        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        int i = 1;
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            sb.append(i).append("--")
                    .append(microservicesProperties.getAppName()).append("--")
                    .append(microservicesProperties.getSysmgrRoot().getContentPath()).append("--")
                    .append(info.getMethodsCondition()).append("--")
                    .append(info.getPatternsCondition()).append("--")
                    .append(method.getMethod().getDeclaringClass() + "#"
                            + method.getMethod().getName()).append("--").append('\n');
            i++;
        }
        return sb.toString();
    }

    /**
     * 示例接口：根据serviceId获取对应配置项
     *
     * @param serviceId
     */
    @GetMapping(value = "getMip/{serviceId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String getMip(@PathVariable("serviceId") String serviceId) {
        return JSON.toJSONString(microservicesProperties.getMicroservicesItemProperties(serviceId));
    }

}
