package com.newland.paas.sbcommon.utils;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by slevenc on 2018/12/11.
 */
public class MicroserviceRpcServiceRequestUtil implements InvocationHandler {
    private RestTemplateUtils restTemplateUtils = null;
    private MicroservicesItemProperties microservicesItemProperties = null;

    private static final Log log = LogFactory.getLogger(MicroserviceRpcServiceRequestUtil.class);

    public static <T> T getInstance(Class<T> cls, String serviceId) {
        MicroserviceRpcServiceRequestUtil h = new MicroserviceRpcServiceRequestUtil(serviceId);
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, h);
    }


    private MicroserviceRpcServiceRequestUtil(String serviceId) {
        restTemplateUtils = SpringContextUtil.getContext().getBean(RestTemplateUtils.class);
        MicroservicesProperties mp = SpringContextUtil.getContext().getBean(MicroservicesProperties.class);
        String methodName = "get" + serviceId;
        List<Method> ms = Arrays.asList(mp.getClass().getMethods());
        Method method = ms.stream().filter(e -> e.getName().toUpperCase().equals(methodName.toUpperCase()) && e.getParameters().length == 0)
                .findFirst().orElseThrow(() -> new SystemException(new PaasError("", "无效的serviceId:" + serviceId)));
        try {
            microservicesItemProperties = (MicroservicesItemProperties) method.invoke(mp);
        } catch (Throwable e) {
            throw new SystemException(new PaasError("", "无效的serviceId:" + serviceId));
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        ParameterizedTypeReference<Object> ptr = new ParameterizedTypeReference<Object>() {

            @Override
            public Type getType() {
                if (method.getReturnType().equals(BasicResponseContentVo.class)) {
                    return method.getGenericReturnType();
                }
                return new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{method.getGenericReturnType()};
                    }

                    @Override
                    public Type getRawType() {
                        return BasicResponseContentVo.class;
                    }

                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };
            }
        };
        Object response = null;
        GetMapping get = method.getAnnotation(GetMapping.class);
        PostMapping post = method.getAnnotation(PostMapping.class);
        PutMapping put = method.getAnnotation(PutMapping.class);
        DeleteMapping del = method.getAnnotation(DeleteMapping.class);
        String uri = null;
        uri = del != null ? del.value()[0] : uri;
        uri = put != null ? put.value()[0] : uri;
        uri = post != null ? post.value()[0] : uri;
        uri = get != null ? get.value()[0] : uri;
        if (uri == null) {
            throw new SystemException(new PaasError("", "不支持的方法"));
        }

        Annotation[][] pas = method.getParameterAnnotations();
        Object body = null;
        for (int ip = 0; ip < pas.length; ip++) {
            Annotation[] pa = pas[ip];
            PathVariable paa = (PathVariable) Arrays.asList(pa).stream().filter(e -> e instanceof PathVariable).findFirst().orElse(null);
            if (paa != null) {
                uri = uri.replace("{" + paa.value() + "}", String.valueOf(args[ip]));
            }
            RequestBody bodya = (RequestBody) Arrays.asList(pa).stream().filter(e -> e instanceof RequestBody).findFirst().orElse(null);
            if (bodya != null) {
                body = args[ip];
            }
        }
        Object respObj = null;
        if (get != null) {
            respObj = restTemplateUtils.getForEntity(microservicesItemProperties, uri, Collections.EMPTY_MAP, ptr);
        }
        if (post != null) {
            respObj = restTemplateUtils.postForEntity(microservicesItemProperties, uri, body, ptr);
        }
        if (put != null) {
            respObj = restTemplateUtils.putForToken(microservicesItemProperties, uri, null, body, ptr);
        }
        if (del != null) {
            respObj = restTemplateUtils.deleteForEntity(microservicesItemProperties, uri, body, ptr);
        }
        log.debug(LogProperty.LOGTYPE_DETAIL,"rpc call "+method.toString()+"\n resp::"+ JSONObject.toJSONString(respObj));
        if (method.getReturnType().equals(BasicResponseContentVo.class)) {
            return respObj;
        }
        if (respObj == null) {
            return null;
        }
        return ((BasicResponseContentVo) respObj).getContent();
    }
}
