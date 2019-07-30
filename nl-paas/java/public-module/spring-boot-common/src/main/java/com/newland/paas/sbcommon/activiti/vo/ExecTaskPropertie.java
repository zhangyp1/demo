package com.newland.paas.sbcommon.activiti.vo;

/**
 * 执行处理-参数
 *
 * @author WRP
 * @since 2019/7/1
 */
public class ExecTaskPropertie {

    /**
     * 审批意见id
     */
    public static final String APPROVE = "approve";
    /**
     * 审批意见value-同意
     */
    public static final String APPROVE_TRUE = "true";
    /**
     * 审批意见id（创建租户流程、加入租户流程）
     */
    public static final String IS_AGREE = "isAgree";
    /**
     * 审批意见value（创建租户流程、加入租户流程）-同意
     */
    public static final String IS_AGREE_TRUE = "1";
    /**
     * 审批意见备注id
     */
    public static final String REMARK = "remark";

    private String id;
    private String value;

    public ExecTaskPropertie() {
    }

    public ExecTaskPropertie(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
