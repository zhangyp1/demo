package com.newland.paas.sbcommon.doc.dto;

import com.newland.paas.paasservice.controllerapi.annotation.Doc;

/**
 * @author laiCheng
 * @date 2018/8/3 17:03
 */
public class ParamDefine {
    @Doc("代码")
    private String code;
    @Doc("类型")
    private String type;
    @Doc("名称")
    private String name;
    @Doc("父节点名称")
    private String parentName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
