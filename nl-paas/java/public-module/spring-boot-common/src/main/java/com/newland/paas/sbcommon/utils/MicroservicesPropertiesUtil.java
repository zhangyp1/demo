package com.newland.paas.sbcommon.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;

/**
 * 工具类，生成MicroserviceProperties对象
 * 
 * @author zkq
 * @date 2019年1月22日
 */
public class MicroservicesPropertiesUtil {
    private static final Log log = LogFactory.getLogger(MicroservicesPropertiesUtil.class);

    public static MicroservicesItemProperties get(String serviceId) {
        MicroservicesItemProperties resultObj = null;
        MicroservicesProperties mp = SpringContextUtil.getContext().getBean(MicroservicesProperties.class);
        Method[] methods = mp.getClass().getMethods();
        List<Method> methodList = Arrays.asList(methods);
        log.info(LogProperty.LOGTYPE_DETAIL, "传递的ServiceId为：" + serviceId);
        String methodName = "get" + serviceId;
        Method method =
            methodList.stream()
                .filter(m -> m.getParameterCount() == 0 && methodName.toUpperCase().equals(m.getName().toUpperCase()))
                .findFirst().get();
        try {
            resultObj = (MicroservicesItemProperties)method.invoke(mp);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, "1-24-00001", e, "获取不到反射方法");
        }

        return resultObj;
    }
}
