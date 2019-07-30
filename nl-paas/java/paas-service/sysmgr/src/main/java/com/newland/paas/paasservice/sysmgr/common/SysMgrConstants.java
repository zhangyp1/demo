/*
 *
 */
package com.newland.paas.paasservice.sysmgr.common;

import com.newland.paas.sbcommon.utils.SpringContextUtil;

/**
 * 描述
 * 
 * @author linkun
 * @created 2018年6月28日 下午4:47:07
 */
public class SysMgrConstants {
    public static final String PROFILE = SpringContextUtil.getActiveProfile() + ":";
    /**
     * 是否删除 已删除
     */
    public static final short IS_DELETE_TRUE = 1;
    /**
     * 是否删除 未删除
     */
    public static final short IS_DELETE_FALSE = 0;
    /**
     * 是否管理员 是
     */
    public static final short IS_ADMIN_YES = 1;
    /**
     * 是否管理员 否
     */
    public static final short IS_ADMIN_NO = 0;

    /**
     * 审核状态 审核通过
     */
    public static final String EXAMTYPE_PASSED = "1";
    /**
     * 审核状态 审核不通过
     */
    public static final String EXAMTYPE_NOPASS = "-1";
    /**
     * 审核状态 待审
     */
    public static final String EXAMTYPE_PREEXAM = "0";

    public static final String REDIS_CAPTCHA = PROFILE + "PAAS:CAPTCHA:";

    /**
     * 任务状态
     */
    public static final String TASK_START = "start";
    public static final String TASK_RUN = "run";
    public static final String TASK_SUCCESS = "success";
    public static final String TASK_FAILURE = "failure";

    /**
     * 重新输入
     */
    public static final String RESUME_LOAD = "请重新输入";


    /**
     * 请稍后重试
     */
    public static final String TRY_AGAIN_LATER = "请稍后重试";

    /**
     * 工单类型
     */
    public static final String ACTIVITI_TENANT_CREATE_ORDERID = "sysmgrTenantCreatetenant";
    public static final String ACTIVITI_TENANT_CREATE_ORDERKEY = "创建租户工单";
    public static final String ACTIVITI_TENANT_JOIN_ORDERID = "sysmgrTenantJointenant";
    public static final String ACTIVITI_TENANT_JOIN_ORDERKEY = "加入租户工单";
    public static final String ACTIVITI_TENANT_PROVIDER_ORDERID = "ProviderApply";
    public static final String ACTIVITI_TENANT_PROVIDER_ORDERKEY = "提供方申请工单";

    /**
     * 运营租户ID
     */
    public static final Long TENANT_ID_YUNYIN = 0L;
    /**
     * 运维租户ID
     */
    public static final Long TENANT_ID_YUNWEI = 1L;
    /**
     * 平台管理租户ID
     */
    public static final Long TENANT_ID_PINGTAI = 2L;
    /**
     * 系统管理员租户ID
     */
    public static final Long TENANT_ID_ADMIN = -1L;
    /**
     * 普通租户的最小ID
     */
    public static final Long TENANT_ID_NORMAL_MIN = 101L;

    /**
     * 系统管理员角色类型
     */
    public static final short ROLE_TYPE_SYS_ADMIN = 0;
    /**
     * 平台角色类型
     */
    public static final short ROLE_TYPE_PLATFORM = 1;
    /**
     * 工组角色类型
     */
    public static final short ROLE_TYPE_GROUP = 2;
    /**
     * 工组角色模板类型
     */
    public static final short ROLE_TYPE_GROUP_TEMPLATE = 3;

    /**
     * 运营管理员角色ID
     */
    public static final Long ROLE_ID_YY_ADMIN = 1L;
    /**
     * 运维管理员角色ID
     */
    public static final Long ROLE_ID_YW_ADMIN = 2L;
    /**
     * 运维普通成员角色ID
     */
    public static final Long ROLE_ID_YW_NORMAL = 4L;
    /**
     * 运维工组管理员角色ID
     */
    public static final Long ROLE_ID_YW_GROUP_ADMIN = 3L;
    /**
     * 运维工组操作员角色ID
     */
    public static final Long ROLE_ID_YW_GROUP_OPERATOR = 12L;
    /**
     * 运维工组观察员角色ID
     */
    public static final Long ROLE_ID_YW_GROUP_OBSERVER = 10L;
    /**
     * 租户管理员角色ID
     */
    public static final Long ROLE_ID_ZH_ADMIN = 5L;
    /**
     * 租户普通成员角色ID
     */
    public static final Long ROLE_ID_ZH_NORMAL = 7L;
    /**
     * 租户工组管理员角色ID
     */
    public static final Long ROLE_ID_ZH_GROUP_ADMIN = 6L;
    /**
     * 租户工组操作员角色ID
     */
    public static final Long ROLE_ID_ZH_GROUP_OPERATOR = 13L;
    /**
     * 租户工组观察员角色ID
     */
    public static final Long ROLE_ID_ZH_GROUP_OBSERVER = 11L;
    /**
     * 系统管理员角色ID
     */
    public static final Long ROLE_ID_SYS_ADMIN = 9L;

    /**
     * 工组管理员角色名称
     */
    public static final String ROLE_NAME_GROUP_ADMIN = "工组管理员";
    /**
     * 工组操作员角色名称
     */
    public static final String ROLE_NAME_GROUP_OPERATOR = "工组操作员";
    /**
     * 工组观察员角色名称
     */
    public static final String ROLE_NAME_GROUP_OBSERVER = "工组观察员";

    /**
     * 系统管理员用户ID
     */
    public static final Long USER_ID_ADMIN = 11L;

    public static final Long[] ROLE_ID_LIST_OF_ADMIN = {ROLE_ID_YY_ADMIN, ROLE_ID_YW_ADMIN, ROLE_ID_ZH_ADMIN};

    /**
     * 根工组的父工组ID
     */
    public static final Long ROOT_GROUP_PARENT_ID = -1L;

    /**
     * 根系统分组的父系统分组ID
     */
    public static final Long ROOT_SYS_CATEGORY_PARENT_ID = -1L;

    /**
     * 公共仓库
     */
    public static final String DEPOT_PUB = "public";
    /**
     * 私有仓库
     */
    public static final String DEPOT_PRI = "private";

    /**
     * 资产仓库
     */
    public static final String DEPOT_AST = "ast";
    /**
     * 镜像仓库
     */
    public static final String DEPOT_IMG = "img";

    /**
     * 机房状态：不可用
     */
    public static final short RES_COMPUTER_ROOM_STATUS_INVALID = 0;
    /**
     * 机房状态：正常
     */
    public static final short RES_COMPUTER_ROOM_STATUS_VALID = 1;

    /**
     * 我的服务菜单
     */
    public static final String MY_SERVICE_MENU_URL = "/business/serviceMgr/PaasMgrServiceSummary";

    /**
     * 对象类型：资产
     */
    public static final String OBJECT_TYPE_ASSETS = "assets";
    /**
     * 对象类型：原生资产镜像
     */
    public static final String OBJECT_TYPE_ASSETS_CONTAINER = "assets_container";
    /**
     * 对象类型：资产编排
     */
    public static final String OBJECT_TYPE_ASSETS_M = "assets_m";
    /**
     * 对象类型：应用
     */
    public static final String OBJECT_TYPE_APPLICATION = "application";
    /**
     * 对象类型：服务
     */
    public static final String OBJECT_TYPE_SERVICE = "service";
    /**
     * 对象类型：服务实例
     */
    public static final String OBJECT_TYPE_SERVICE_INST = "service_inst";
    /**
     * 对象类型：集群
     */
    public static final String OBJECT_TYPE_CLUSTER = "cluster";
    /**
     * 对象类型：集群分区
     */
    public static final String OBJECT_TYPE_CLUSTER_PARTITION = "cluster_partition";
    /**
     * 对象类型：ingress负载均衡器
     */
    public static final String OBJECT_TYPE_INGRESS = "ingress";
    /**
     * 对象类型：持久卷
     */
    public static final String OBJECT_TYPE_VOL = "vol";

    private SysMgrConstants(){}

}
