package com.newland.paas.paasservice.sysmgr.vo;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Author:PanYang
 * Date:Created in 下午3:50 2018/8/2
 * Modified By:
 */
public class AuditInfoVo implements Serializable {
    private static final long serialVersionUID = 90717641001019044L;
    /**
     * 管理实体名称，用于采集业务处理。  非空 以审计平台定义的应用编码为准，如：4AAthLog、4ABOSSLog、 默认值？4APAASLog？4ABOMCLog、4ABASSLog
     */
    public String IDENTITY_NAME;
    /**
     * 资源类别，对于应用来说值为“1” 非空 1应用 默认值1
     */
    public String RESOURCE_KIND;
    /**
     * 资源编码，每种应用系统对应一种资源编码；  非空 以审计平台定义的应用编码为准，如：UAP、BOSS、BOMC、BASS 无法提供
     */
    public String RESOURCE_CODE;
    /**
     *
     */
    public String IDR_CREATION_TIME;
    /**
     * 主帐号ID 非空 4A管理平台定义的MAIN_ACCT_ID 可提供
     */
    public String MAIN_ACCOUNT_NAME;
    /**
     * 应用从帐号，用户登录应用系统使用的帐号； 可为空 应用从账号名称(4A系统为空) 可提供
     */
    public String SUB_ACCOUNT_NAME;
    /**
     * 操作时间，格式为YYYY-MM-DD HH:mm:ss 非空 操作实际发生时间 可提供
     */
    public String OPERATE_TIME;
    /**
     * 操作类型编码 非空 审计平台定义的操作类型ID,如1-HABOSS-10677 无法提供
     */
    public String OP_TYPE_ID;
    /**
     * 操作类型名称 非空 所进行的具体操作名称,如：异地补换卡回滚 可提供
     */
    public String OP_TYPE_NAME;
    /**
     * 操作级别ID 非空 该操作的级别 可提供5-严重、4-警告、3-敏感、2-重要、1-一般
     */
    public String OP_LEVEL_ID;
    /**
     *
     */
    public String OPERATE_CONTENT;
    /**
     * 操作结果 非空 0-成功 1-失败 可提供
     */
    public String OPERATE_RESULT;
    /**
     * 模块ID 非空 应用操作的菜单编号，对应于权限系统ID 可提供
     */
    public String MODULE_ID;
    /**
     * 模块名称 非空 应用操作的菜单名称 可提供
     */
    public String MODULE_NAME;
    /**
     * 该操作对应的工单号 可选 非汉字 无法提供
     */
    public String TASK_CODE;
    /**
     * 金库审批流水号 可选 金库业务标识，唯一代表一次金库模式触发，如果业务访问触发了金库模式，此字段必填，字段值：OPERATEID + "#" + SVCSN + "#" + SERVICEID  无法提供
     */
    public String BANKAPPROVE;
    /**
     * 金库验证过程标识 可选 金库验证过程标识，即从模态窗口返回值解析的前半部分 ***#&&&&& 的 ****： 无法提供 -3 ——金库应急，允许业务继续 -2 ——场景或元业务未开启，允许业务继续 -1 ——直接关闭窗口，未申请审批 1 ——审批通过0 ——审批不通过 2 ——超时，允许业务继续访问 3 ——超时，不允许业务继续访问 4 ——出现错误或异常（包括数据异常）5 ——未配置策略，允许业务继续访问 6 ——未配置策略，不允许继续访问
     */
    public String BANKFLAG;
    /**
     * 客户端IP地址（用户客户端上多网卡IP ） 非空 标准IP1, 标准IP2,标准IP3… 可提供
     */
    public String CLIENT_NETWORK_ADDRESS;
    /**
     * 客户端名称 可选 客户端名称 无法提供
     */
    public String CLIENT_NAME;
    /**
     * 客户端通信IP地址 非空 标准ip格式数据 可提供（和CLIENT_NETWORK_ADDRESS区别是什么？）
     */
    public String CLIENT_ADDRESS;
    /**
     * 客户端端口 非空 可提供
     */
    public String CLIENT_PORT;
    /**
     * 客户端MAC地址（多个） 非空  标准MAC1,标准MAC2… 有疑问
     */
    public String CLIENT_MAC;
    /**
     * 客户端CPU序列号 非空 有疑问
     */
    public String CLIENT_CPU_SERIAL;
    /**
     * 服务端IP地址 非空 标准ip格式数据 可提供
     */
    public String SERVER_ADDRESS;
    /**
     * 应用服务器端端口 非空 可提供
     */
    public String SERVER_PORT;
    /**
     * 应用服务器端MAC地址 非空 有疑问
     */
    public String SERVER_MAC;
    /**
     * 本地查询的外省份ID 可选 无法提供
     */
    public String TO_PROVINCES_ID;
    /**
     * 本地查询的外省份名称 可选 无法提供
     */
    public String TO_PROVINCES_NAME;
    /**
     * 本地被查询的外省份ID 可选 无法提供
     */
    public String FROM_PROVINCES_ID;
    /**
     * 本地被查询的外省份名称 可选 无法提供
     */
    public String FROM_PROVINCES_NAME;


    public void setRESOURCE_KIND(String RESOURCE_KIND) {
        this.RESOURCE_KIND = RESOURCE_KIND;
    }

    public void setOPERATE_TIME(String OPERATE_TIME) {
        this.OPERATE_TIME = OPERATE_TIME;
    }

    public void setIDENTITY_NAME(String IDENTITY_NAME) {
        this.IDENTITY_NAME = IDENTITY_NAME;
    }

    public void setRESOURCE_CODE(String RESOURCE_CODE) {
        this.RESOURCE_CODE = RESOURCE_CODE;
    }

    public void setIDR_CREATION_TIME(String IDR_CREATION_TIME) {
        this.IDR_CREATION_TIME = IDR_CREATION_TIME;
    }

    public void setMAIN_ACCOUNT_NAME(String MAIN_ACCOUNT_NAME) {
        this.MAIN_ACCOUNT_NAME = MAIN_ACCOUNT_NAME;
    }

    public void setSUB_ACCOUNT_NAME(String SUB_ACCOUNT_NAME) {
        this.SUB_ACCOUNT_NAME = SUB_ACCOUNT_NAME;
    }

    public void setOP_TYPE_ID(String OP_TYPE_ID) {
        this.OP_TYPE_ID = OP_TYPE_ID;
    }

    public void setOP_TYPE_NAME(String OP_TYPE_NAME) {
        this.OP_TYPE_NAME = OP_TYPE_NAME;
    }

    public void setOP_LEVEL_ID(String OP_LEVEL_ID) {
        this.OP_LEVEL_ID = OP_LEVEL_ID;
    }

    public void setOPERATE_CONTENT(String OPERATE_CONTENT) {
        this.OPERATE_CONTENT = OPERATE_CONTENT;
    }

    public void setOPERATE_RESULT(String OPERATE_RESULT) {
        this.OPERATE_RESULT = OPERATE_RESULT;
    }

    public void setMODULE_ID(String MODULE_ID) {
        this.MODULE_ID = MODULE_ID;
    }

    public void setMODULE_NAME(String MODULE_NAME) {
        this.MODULE_NAME = MODULE_NAME;
    }

    public void setTASK_CODE(String TASK_CODE) {
        this.TASK_CODE = TASK_CODE;
    }

    public void setBANKAPPROVE(String BANKAPPROVE) {
        this.BANKAPPROVE = BANKAPPROVE;
    }

    public void setBANKFLAG(String BANKFLAG) {
        this.BANKFLAG = BANKFLAG;
    }

    public void setCLIENT_NETWORK_ADDRESS(String CLIENT_NETWORK_ADDRESS) {
        this.CLIENT_NETWORK_ADDRESS = CLIENT_NETWORK_ADDRESS;
    }

    public void setCLIENT_NAME(String CLIENT_NAME) {
        this.CLIENT_NAME = CLIENT_NAME;
    }

    public void setCLIENT_ADDRESS(String CLIENT_ADDRESS) {
        this.CLIENT_ADDRESS = CLIENT_ADDRESS;
    }

    public void setCLIENT_PORT(String CLIENT_PORT) {
        this.CLIENT_PORT = CLIENT_PORT;
    }

    public void setCLIENT_MAC(String CLIENT_MAC) {
        this.CLIENT_MAC = CLIENT_MAC;
    }

    public void setCLIENT_CPU_SERIAL(String CLIENT_CPU_SERIAL) {
        this.CLIENT_CPU_SERIAL = CLIENT_CPU_SERIAL;
    }

    public void setSERVER_ADDRESS(String SERVER_ADDRESS) {
        this.SERVER_ADDRESS = SERVER_ADDRESS;
    }

    public void setSERVER_PORT(String SERVER_PORT) {
        this.SERVER_PORT = SERVER_PORT;
    }

    public void setSERVER_MAC(String SERVER_MAC) {
        this.SERVER_MAC = SERVER_MAC;
    }

    public void setTO_PROVINCES_ID(String TO_PROVINCES_ID) {
        this.TO_PROVINCES_ID = TO_PROVINCES_ID;
    }

    public void setTO_PROVINCES_NAME(String TO_PROVINCES_NAME) {
        this.TO_PROVINCES_NAME = TO_PROVINCES_NAME;
    }

    public void setFROM_PROVINCES_ID(String FROM_PROVINCES_ID) {
        this.FROM_PROVINCES_ID = FROM_PROVINCES_ID;
    }

    public void setFROM_PROVINCES_NAME(String FROM_PROVINCES_NAME) {
        this.FROM_PROVINCES_NAME = FROM_PROVINCES_NAME;
    }
}
   