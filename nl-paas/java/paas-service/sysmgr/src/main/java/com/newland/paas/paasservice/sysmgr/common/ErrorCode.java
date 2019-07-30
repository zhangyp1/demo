package com.newland.paas.paasservice.sysmgr.common;

/**
 * 错误异常码
 */
public class ErrorCode {
    /**
     * 后台服务管理一级异常
     */
    public static final String ERR_BUS_ONE = "2";

    /**
     * 后台服务管理二级异常
     */
    public static final String ERR_BUS_TWO = ERR_BUS_ONE + "16";

    /**
     * 业务级公共二级异常
     */
    public static final String ERR_BUS_COM_TWO = ERR_BUS_ONE + "10";

    /**
     * 输入参数异常
     */
    public static final String ERR_CODE_PARAM = ERR_BUS_COM_TWO + "00001";
    public static final String ERR_MSG_PARAM = "输入参数为null异常";

    /**
     * 未找到流程taskId
     */
    public static final String ERR_CODE_SVR_TASKID_NOT_FOUND_ERROR = ERR_BUS_TWO + "00053";
    public static final String ERR_MSG_SVR_TASKID_NOT_FOUND_ERROR = "未找到流程taskId";

    /**
     *调用流程异常
     */
    public static final String ERR_CODE_SVR_ACTIVITI_ERROR = ERR_BUS_TWO + "00054";
    public static final String ERR_MSG_SVR_ACTIVITI_ERROR = "调用流程异常";

    private ErrorCode(){}
}
