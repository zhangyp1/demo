package com.newland.paas.sbcommon.serviceapi;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysInterfaceUrlVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * API
 *
 * @author WRP
 * @since 2019/1/18
 */
@RestController
@RequestMapping(value = "/service/v1/api")
public class ApiController {

    public static final Log LOG = LogFactory.getLogger(ApiController.class);

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 获取项目所有接口
     *
     * @return 接口列表
     */
    @GetMapping(value = "getAllUrl", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<SysInterfaceUrlVO> getAllUrl() {
        return buildAllUrl();
    }

    /**
     * 获取项目所有接口
     *
     * @return 接口列表
     */
    public List<SysInterfaceUrlVO> buildAllUrl() {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        List<SysInterfaceUrlVO> sysInterfaceUrls = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            String rm = methods.isEmpty() ? null : methods.toString().substring(1, methods.toString().length() - 1);
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            String uri = patterns.isEmpty() ? null : patterns.toString().substring(1, patterns.toString().length() - 1);
            SysInterfaceUrlVO sysInterfaceUrl = new SysInterfaceUrlVO();
            sysInterfaceUrl.setMethod(rm);
            sysInterfaceUrl.setUri(uri);
            sysInterfaceUrl.setClassMethod(method.getMethod().getDeclaringClass().getName()
                    + "#" + method.getMethod().getName());
            sysInterfaceUrls.add(sysInterfaceUrl);
        }
        return sysInterfaceUrls;
    }

}
