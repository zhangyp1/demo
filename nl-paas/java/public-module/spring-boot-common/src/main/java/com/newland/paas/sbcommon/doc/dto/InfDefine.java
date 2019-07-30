package com.newland.paas.sbcommon.doc.dto;

import java.lang.reflect.Method;

import com.newland.paas.paasservice.controllerapi.annotation.Doc;

/**
 * @author laiCheng
 * @date 2018/8/3 15:23
 */
public class InfDefine {
    @Doc("映射路径")
    private String mappingString;
    @Doc("名称")
    private String name;
    @Doc("映射方法")
    private Method method;

    public String getMappingString() {
        return mappingString;
    }

    public void setMappingString(String mappingString) {
        this.mappingString = mappingString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
