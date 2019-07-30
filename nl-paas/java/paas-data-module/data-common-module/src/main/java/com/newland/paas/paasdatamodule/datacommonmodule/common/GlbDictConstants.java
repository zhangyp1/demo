package com.newland.paas.paasdatamodule.datacommonmodule.common;

/**
 * 字典表数据
 *
 * @author wrp
 * @since 2018/6/29
 */
public enum GlbDictConstants {

    /** 工单类型 */
    WORK_TYPE("WORK_TYPE", "工单类型", "-1"),
    /** 资源申请工单 */
    WORK_TYPE_RESOURCE_APPLY("resourceApply", "资源申请工单", "WORK_TYPE"),
    /** 释放工单 */
    WORK_TYPE_RESOURCE_RELEASE("resourceRelease", "释放工单", "WORK_TYPE"),
    /** 回收工单 */
    WORK_TYPE_RESOURCE_RECYCLE("resourceRecycle", "回收工单", "WORK_TYPE"),

    /** 资源状态 */
    RESES_STATUS("RESES_STATUS", "资源状态", "-1"),
    /** 待回收 */
    RESES_STATUS_BE_RECYCLED("beRecycled", "待回收", "RESES_STATUS"),
    /** 删除 */
    RESES_STATUS_DEL("del", "删除", "RESES_STATUS"),
    /** 故障 */
    RESES_STATUS_FAULT("fault", "故障", "RESES_STATUS"),
    /** 空闲 */
    RESES_STATUS_FREE("free", "空闲", "RESES_STATUS"),
    /** 未启用 */
    RESES_STATUS_NO_TENABLED("notEnabled", "未启用", "RESES_STATUS"),
    /** 已下线 */
    RESES_STATUS_OFF_LINE("offLine", "已下线", "RESES_STATUS"),
    /** 预占 */
    RESES_STATUS_PREEMPTED("preempted", "预占", "RESES_STATUS"),
    /** 使用中 */
    RESES_STATUS_USE("use", "使用中", "RESES_STATUS"),
    /** 已分配 */
    RESES_STATUS_ALLOCATED("allocated", "已分配", "RESES_STATUS"),

    /** 应用状态 */
    APP_STATUS("APP_STATUS", "应用状态", "-1"),
    /** 新增 */
    APP_STATUS_NEW("new", "新增", "APP_STATUS"),
    /** 部署中 */
    APP_STATUS_DEPLOY("deploy", "部署中", "APP_STATUS"),
    /** 停止 */
    APP_STATUS_STOP("stop", "停止", "APP_STATUS"),
    /** 启动中 */
    APP_STATUS_START("start", "启动中", "APP_STATUS"),
    /** 运行 */
    APP_STATUS_RUN("run", "运行", "APP_STATUS"),
    /** 停止中 */
    APP_STATUS_STOPPING("stopping", "停止中", "APP_STATUS"),
    /** 卸载中 */
    APP_STATUS_UNLOADING("unloading", "卸载中", "APP_STATUS"),
    /** 已卸载 */
    APP_STATUS_UNLOADED("unloaded", "已卸载", "APP_STATUS"),
    /** 部署失败 */
    APP_STATUS_DEPLOYFAILURE("deployFailure", "部署失败", "APP_STATUS"),
    /** 启动失败 */
    APP_STATUS_STARTFAILURE("startFailure", "启动失败", "APP_STATUS"),
    /** 停止失败 */
    APP_STATUS_STOPFAILURE("stopFailure", "停止失败", "APP_STATUS"),
    /** 卸载失败 */
    APP_STATUS_UNLOADFAILURE("unloadFailure", "卸载失败", "APP_STATUS"),
    /** 升级中 */
    APP_STATUS_UPGRADE("upgrade", "升级中", "APP_STATUS"),
    /** 升级失败 */
    APP_STATUS_UPGRADEFAILURE("upgradeFailure", "升级失败", "APP_STATUS"),
    /** 重启中 */
    APP_STATUS_RESTART("restart", "重启中", "APP_STATUS"),
    /** 重启失败 */
    APP_STATUS_RESTARTFAILURE("restartFailure", "重启失败", "APP_STATUS"),
    /** 异常 */
    APP_STATUS_EXCEPTION("exception", "异常", "APP_STATUS"),
    /** 失效 */
    APP_STATUS_INVALID("invalid", "失效", "APP_STATUS"),
    /** 已下线 */
    APP_STATUS_OFFLINED("offlined", "已下线", "APP_STATUS"),
    /** 下线中 */
    APP_STATUS_OFFLINING("offlining", "下线中", "APP_STATUS"),
    /** 迁移中 */
    APP_STATUS_MERGRATING("mergrating", "迁移中", "APP_STATUS"),
    /** 回退中 */
    APP_STATUS_ROLLBACKING("rollbacking", "回退中", "APP_STATUS"),

    /** 操作执行状态 */
    OPERATION_STATUS("OPERATION_STATUS", "操作执行状态", "-1"),
    /** 运行中 */
    OPERATION_RUNNING("running", "运行中", "OPERATION_STATUS"),
    /** 成功 */
    OPERATION_SUCCESS("success", "成功", "OPERATION_STATUS"),
    /** 失败 */
    OPERATION_FAILURE("failure", "失败", "OPERATION_STATUS"),

    /** 关系目标类型 */
    RELATION_TARGET_TYPE("RELATION_TARGET_TYPE", "关系目标类型", "-1"),
    /** 应用 */
    RELATION_TARGET_APP("app", "应用", "RELATION_TARGET_TYPE"),
    /** 服务 */
    RELATION_TARGET_SERVICE("service", "服务", "RELATION_TARGET_TYPE"),

    /** 弹性策略类型 */
    ELASTIC_POLICY_TYPE("ELASTIC_POLICY_TYPE", "弹性策略类型", "-1"),
    /** 计划模式 */
    ELASTIC_POLICY_TYPE_TIMING("timing", "计划模式", "ELASTIC_POLICY_TYPE"),
    /** 动态模式 */
    ELASTIC_POLICY_TYPE_DYNAMIC("dynamic", "动态模式", "ELASTIC_POLICY_TYPE"),
    /** 实时模式 */
    ELASTIC_POLICY_TYPE_REALTIME("realTime", "实时模式", "ELASTIC_POLICY_TYPE"),

    /** 服务类型 */
    SERVICE_TYPE("SERVICE_TYPE", "服务类型", "-1"),
    /** 平台 */
    SERVICE_TYPE_PUBLIC("public", "平台", "SERVICE_TYPE"),
    /** 软件 */
    SERVICE_TYPE_PRIVATE("private", "软件", "SERVICE_TYPE"),
    /** 第三方 */
    SERVICE_TYPE_THIRD("third", "第三方", "SERVICE_TYPE"),

    /** 服务状态 */
    SVR_STATUS("SVR_STATUS", "服务状态", "-1"),
    /** 已保存 */
    SVR_STATUS_SAVED("saved", "已保存", "SVR_STATUS"),
    /** 发布中 */
    SVR_STATUS_RELEASE("release", "发布中", "SVR_STATUS"),
    /** 可用 */
    SVR_STATUS_AVAILABLE("available", "可用", "SVR_STATUS"),
    /** 注销中 */
    SVR_STATUS_CANCELLATION("cancellation", "注销中", "SVR_STATUS"),
    /** 已注销 */
    SVR_STATUS_CANCELLED("cancelled", "已注销", "SVR_STATUS"),
    /** 未通过 */
    SVR_STATUS_APPROVALFAILED("approvalFailed", "未通过", "SVR_STATUS"),

    /** 订阅状态 */
    SUBSCRIBE_STATUS("SUBSCRIBE_STATUS", "订阅状态", "-1"),
    /** 订阅中 */
    SUBSCRIBE_STATUS_SUBSCRIBE("subscribe", "订阅中", "SUBSCRIBE_STATUS"),
    /** 已订阅 */
    SUBSCRIBE_STATUS_SUBSCRIBED("subscribed", "已订阅", "SUBSCRIBE_STATUS"),
    /** 订阅失败 */
    SUBSCRIBE_STATUS_SUBSCRIBE_FAILURE("subscribeFailure", "订阅失败", "SUBSCRIBE_STATUS"),
    /** 已撤回 */
    SUBSCRIBE_STATUS_WITHDRAWER("withdrawer", "已撤回", "SUBSCRIBE_STATUS"),
    /** 退订中 */
    SUBSCRIBE_STATUS_THE_UNSUBSCRIBE("theUnsubscribe", "退订中", "SUBSCRIBE_STATUS"),
    /** 已退订 */
    SUBSCRIBE_STATUS_HAS_UNSUBSCRIBE("hasUnsubscribe", "已退订", "SUBSCRIBE_STATUS"),

    /** 服务实例创建时机 */
    SVR_INS_CRE_OPPORTUNITY("SVR_INS_CRE_OPPORTUNITY", "服务实例创建时机", "-1"),
    /** 延迟加载 */
    SVR_INS_CRE_OPPORTUNITY_LAZY_LOADING("lazyLoading", "延迟加载", "SVR_INS_CRE_OPPORTUNITY"),
    /** 预加载 */
    SVR_INS_CRE_OPPORTUNITY_PRELOAD("preload", "预加载", "SVR_INS_CRE_OPPORTUNITY"),

    /** 弹性循环方式 */
    ELAS_CYCLE_WAY("ELAS_CYCLE_WAY", "弹性循环方式", "-1"),
    /** 一次 */
    ELAS_CYCLE_WAY_ONETIME("ELAS_CYCLE_WAY", "一次", "ELAS_CYCLE_WAY"),
    /** 每天 */
    ELAS_CYCLE_WAY_EVERYDAY("ELAS_CYCLE_WAY", "每天", "ELAS_CYCLE_WAY"),
    /** 每周 */
    ELAS_CYCLE_WAY_WEEK("ELAS_CYCLE_WAY", "每周", "ELAS_CYCLE_WAY"),
    /** 每月 */
    ELAS_CYCLE_WAY_MONTH("month", "每月", "ELAS_CYCLE_WAY"),

    /** 比较方式 */
    COMPARE_WAY("COMPARE_WAY", "比较方式", "-1"),
    /** 比较方式 >= */
    COMPARE_WAY_GREATER_EQUAL(">=", ">=", "COMPARE_WAY"),
    /** 比较方式 <= */
    COMPARE_WAY_LESS_EQUAL("<=", "<=", "COMPARE_WAY"),

    /** 成员状态 */
    MEMBER_STATUS("MEMBER_STATUS", "成员状态", "-1"),
    /** 启动中 */
    MEMBER_STATUS_IN_START("in_start", "启动中", "MEMBER_STATUS"),
    /** 运行 */
    MEMBER_STATUS_RUN("run", "运行", "MEMBER_STATUS"),
    /** 停止 */
    MEMBER_STATUS_STOP("stop", "停止", "MEMBER_STATUS"),
    /** 失效 */
    MEMBER_STATUS_FAILURE("failure", "失效", "MEMBER_STATUS"),

    /** 应用重要程度 */
    APP_IMPORTANT("APP_IMPORTANT", "应用重要程度", "-1"),
    /** 普通 */
    APP_IMPORTANT_1("1", "普通", "APP_IMPORTANT"),
    /** 重要 */
    APP_IMPORTANT_2("2", "重要", "APP_IMPORTANT"),

    /** 依赖目标类型 */
    RELY_TARGET_TYPE("RELY_TARGET_TYPE", "依赖目标类型", "-1"),
    /** 应用 */
    RELY_TARGET_TYPE_1("1", "应用", "RELY_TARGET_TYPE"),
    /** 服务 */
    RELY_TARGET_TYPE_2("2", "服务", "RELY_TARGET_TYPE"),

    /** 任务状态 */
    TASK_STATUS("TASK_STATUS", "任务状态", "-1"),
    /** 待执行 */
    TASK_STATUS_1("1", "待执行", "TASK_STATUS"),
    /** 执行中 */
    TASK_STATUS_2("2", "执行中", "TASK_STATUS"),
    /** 执行完成 */
    TASK_STATUS_9("9", "执行完成", "TASK_STATUS"),

    /** 弹性执行类型 */
    ELAS_PERFORM_TYPE("ELAS_PERFORM_TYPE", "弹性执行类型", "-1"),
    /** 添加 */
    ELAS_PERFORM_TYPE_1("1", "添加", "ELAS_PERFORM_TYPE"),
    /** 减少 */
    ELAS_PERFORM_TYPE_2("2", "减少", "ELAS_PERFORM_TYPE"),

    /** 弹性策略状态 */
    ELAS_STRATEGY_STATUS("ELAS_STRATEGY_STATUS", "弹性策略状态", "-1"),
    /** 启用 */
    ELAS_STRATEGY_STATUS_1("1", "启用", "ELAS_STRATEGY_STATUS"),
    /** 禁用 */
    ELAS_STRATEGY_STATUS_2("2", "禁用", "ELAS_STRATEGY_STATUS"),

    /** 任务来源 */
    TASK_SOURCE("TASK_SOURCE", "任务来源", "-1"),
    /** 策略 */
    TASK_SOURCE_1("1", "策略", "TASK_SOURCE"),
    /** 手工 */
    TASK_SOURCE_2("2", "手工", "TASK_SOURCE"),

    /** 资产类型 */
    ASSET_TYPE("ASSET_TYPE", "资产类型", "-1"),
    /** 常驻应用软件 */
    ASSET_TYPE_APPLICATION("application", "常驻应用软件", "ASSET_TYPE"),
    /** 子应用软件 */
    ASSET_TYPE_APPLET("applet", "子应用软件", "ASSET_TYPE"),
    /** 服务软件 */
    ASSET_TYPE_SERVICE("service", "服务软件", "ASSET_TYPE"),
    /** 子服务软件 */
    ASSET_TYPE_SERVICELET("servicelet", "子服务软件", "ASSET_TYPE"),
    /** 非托管软件 */
    ASSET_TYPE_UNMANAGEDAPP("unmanagedApp", "非托管软件", "ASSET_TYPE"),
    /** 作业应用软件 */
    ASSET_TYPE_JOB("job", "作业应用软件", "ASSET_TYPE"),
    /** 原生类型 */
    ASSET_TYPE_K8SORIGINAL("k8sOriginal", "原生类型", "ASSET_TYPE"),

    /** 资产发布类型 */
    ASSET_PUBLISH_TYPE("ASSET_PUBLISH_TYPE", "资产发布类型", "-1"),
    /** 服务型 */
    ASSET_PUBLISH_TYPE_PUBLISH("publish", "服务型", "ASSET_PUBLISH_TYPE"),
    /** 软件型 */
    ASSET_PUBLISH_TYPE_ORIGINSOFT("originSoft", "软件型", "ASSET_PUBLISH_TYPE"),

    /** 资产集群类型 */
    ASSET_CLUSTER_TYPE("ASSET_CLUSTER_TYPE", "资产集群类型", "-1"),
    /** K8S集群 */
    ASSET_CLUSTER_TYPE_K8S("k8s", "K8S集群", "ASSET_CLUSTER_TYPE"),
    /** 金属机集群 */
    ASSET_CLUSTER_TYPE_METALMACHINE("metalMachine", "金属机集群", "ASSET_CLUSTER_TYPE"),

    /** 资产模块类型 */
    ASSET_MODULE_TYPE("ASSET_MODULE_TYPE", "资产模块类型", "-1"),
    /** 镜像地址 */
    ASSET_MODULE_TYPE_IMAGEAADDRESS("imageaAddress", "镜像地址", "ASSET_MODULE_TYPE"),
    /** 镜像代码包 */
    ASSET_MODULE_TYPE_IMAGECODEPKG("imageCodePkg", "镜像代码包", "ASSET_MODULE_TYPE"),
    /** 压缩代码包 */
    ASSET_MODULE_TYPE_COMPRESSCODEPKG("compressCodePkg", "压缩代码包", "ASSET_MODULE_TYPE"),
    /** 开发包 */
    ASSET_MODULE_TYPE_DEVELOPPKG("developPkg", "开发包", "ASSET_MODULE_TYPE"),
    /** 安装脚本 */
    ASSET_MODULE_TYPE_SCRIPT("script", "安装脚本", "ASSET_MODULE_TYPE"),
    /** 监控包 */
    ASSET_MODULE_TYPE_MONITORPKG("monitorPkg", "监控包", "ASSET_MODULE_TYPE"),
    /** 控制包 */
    ASSET_MODULE_TYPE_CONTROLLERPKG("controllerPkg", "控制包", "ASSET_MODULE_TYPE"),
    /** SDK包 */
    ASSET_MODULE_TYPE_SDKPKG("sdkPkg", "SDK包", "ASSET_MODULE_TYPE"),

    /** 资产配置生效方式 */
    ASSET_CFG_EFFECT_TYPE("ASSET_CFG_EFFECT_TYPE", "资产配置生效方式", "-1"),
    /** 实时生效 */
    ASSET_CFG_EFFECT_TYPE_IMMEDIATE("immediate", "实时生效", "ASSET_CFG_EFFECT_TYPE"),
    /** 重启生效 */
    ASSET_CFG_EFFECT_TYPE_RESTART("restart", "重启生效", "ASSET_CFG_EFFECT_TYPE"),
    /** ConfigMap */
    ASSET_CFG_EFFECT_TYPE_CONFIGMAP("cm", "ConfigMap", "ASSET_CFG_EFFECT_TYPE"),
    /** 通过环境变量生效 */
    ASSET_CFG_EFFECT_TYPE_ENV("env", "通过环境变量生效", "ASSET_CFG_EFFECT_TYPE"),
    /** 通过域名变量配置 */
    ASSET_CFG_EFFECT_TYPE_HOSTS("hosts", "通过域名变量配置", "ASSET_CFG_EFFECT_TYPE"),

    /** 资产指标分类 */
    ASSET_METRIC_TYPE("ASSET_METRIC_TYPE", "资产指标分类", "-1"),
    /** 性能类 */
    ASSET_METRIC_TYPE_PERFORMANCE("performance", "性能类", "ASSET_METRIC_TYPE"),
    /** 功能类 */
    ASSET_METRIC_TYPE_BUSINESS("business", "功能类", "ASSET_METRIC_TYPE"),
    /** 其他类 */
    ASSET_METRIC_TYPE_OTHER("other", "其他类", "ASSET_METRIC_TYPE"),

    /** 资产指标级别 */
    ASSET_METRIC_LEVEL("ASSET_METRIC_LEVEL", "资产指标级别", "-1"),
    /** 应用 */
    ASSET_METRIC_LEVEL_APPLICATION("application", "应用", "ASSET_METRIC_LEVEL"),
    /** 单元 */
    ASSET_METRIC_LEVEL_UNIT("unit", "单元", "ASSET_METRIC_LEVEL"),
    /** 模块 */
    ASSET_METRIC_LEVEL_MODULE("module", "模块", "ASSET_METRIC_LEVEL"),

    /** 资产依赖类型 */
    ASSET_DEPEND_TYPE("ASSET_DEPEND_TYPE", "资产依赖类型", "-1"),
    /** 单一依赖 */
    ASSET_DEPEND_TYPE_SPECIAL("special", "单一依赖", "ASSET_DEPEND_TYPE"),
    /** 多选一 */
    ASSET_DEPEND_TYPE_RADIO("radio", "多选一", "ASSET_DEPEND_TYPE"),

    /** 资产可选必选开关 */
    ASSET_OPTIANAL_FLAG("ASSET_OPTIANAL_FLAG", "资产可选必选开关", "-1"),
    /** 必选 */
    ASSET_OPTIANAL_FLAG_TRUE("true", "必选", "ASSET_OPTIANAL_FLAG"),
    /** 可选 */
    ASSET_OPTIANAL_FLAG_FALSE("false", "可选", "ASSET_OPTIANAL_FLAG"),

    /** 资产接口类别 */
    ASSET_INTF_CATEGORY("ASSET_INTF_CATEGORY", "资产接口类别", "-1"),
    /** 生命周期接口 */
    ASSET_INTF_CATEGORY_DEFAULT("defaultLifeCycleInterface", "生命周期接口", "ASSET_INTF_CATEGORY"),
    /** 依赖初始化接口 */
    ASSET_INTF_CATEGORY_DEPEND("dependInitInterface", "依赖初始化接口", "ASSET_INTF_CATEGORY"),
    /** 自定义接口 */
    ASSET_INTF_CATEGORY_SELF("selfDefineInterface", "自定义接口", "ASSET_INTF_CATEGORY"),
    /** 详情接口 */
    ASSET_INTF_CATEGORY_DETAIL("detailInfoInterface", "详情接口", "ASSET_INTF_CATEGORY"),
    /** 其他内置接口 */
    ASSET_INTF_CATEGORY_OTHER("otherBuiltInInterface", "其他内置接口", "ASSET_INTF_CATEGORY"),

    /** 资产接口方式 */
    ASSET_INTF_MODE("ASSET_INTF_MODE", "资产接口方式", "ASSET_INTF_MODE"),
    /** http */
    ASSET_INTF_MODE_HTTP("http", "http", "ASSET_INTF_MODE"),
    /** sdk */
    ASSET_INTF_MODE_SDK("sdk", "sdk", "ASSET_INTF_MODE"),
    /** sdk-jdbc */
    ASSET_INTF_MODE_JDBC("jdbc", "jdbc", "ASSET_INTF_MODE"),

    /** 负载均衡器 */
    ASSET_SVR_LBTYPE("ASSET_SVR_LBTYPE", "负载均衡器", "ASSET_SVR_LBTYPE"),
    /** ingress4负载均衡器 */
    ASSET_SVR_LBTYPE_INGRESS("ingress", "ingress4负载均衡器", "ASSET_SVR_LBTYPE"),
    /** ingress7负载均衡器 */
    ASSET_SVR_LBTYPE_INGRESS_SEVEN("ingress7", "ingress7负载均衡器", "ASSET_SVR_LBTYPE"),
    /** nodePort负载均衡器 */
    ASSET_SVR_LBTYPE_NODE_POD("nodePort", "nodePort负载均衡器", "ASSET_SVR_LBTYPE"),

    /** 资产方法类型 */
    ASSET_METHOD_TYPE("ASSET_METHOD_TYPE", "资产方法类型", "-1"),
    /** ansible */
    ASSET_METHOD_TYPE_ANSIBLE("ansible", "ansible", "ASSET_METHOD_TYPE"),
    /** restful */
    ASSET_METHOD_TYPE_RESTFUL("restful", "restful", "ASSET_METHOD_TYPE"),
    /** custom */
    ASSET_METHOD_TYPE_CUSTOM("custom", "custom", "ASSET_METHOD_TYPE"),

    /** 资产方法可见性 */
    ASSET_VISIBILITY("ASSET_VISIBILITY", "资产方法可见性", "-1"),
    /** 公有方法 */
    ASSET_VISIBILITY_PUBLIC("public", "公有方法", "ASSET_VISIBILITY"),
    /** 私有方法 */
    ASSET_VISIBILITY_PRIVATE("private", "私有方法", "ASSET_VISIBILITY"),

    /** 资产单元类型 */
    ASSET_UNIT_TYPE("ASSET_UNIT_TYPE", "资产单元类型", "-1"),
    /** pod_无状态型 */
    ASSET_UNIT_TYPE_POD_STATELESS("pod_stateless", "pod_无状态型", "ASSET_UNIT_TYPE"),
    /** pod_有状态型 */
    ASSET_UNIT_TYPE_POD_STATEFUL("pod_stateful", "pod_有状态型", "ASSET_UNIT_TYPE"),
    /** pod_daemon型 */
    ASSET_UNIT_TYPE_POD_DAEMON("pod_daemon", "pod_daemon型", "ASSET_UNIT_TYPE"),

    /** 原生型 */
    ASSET_UNIT_TYPE_METALMACHINE("metalMachine", "原生型", "ASSET_UNIT_TYPE"),
    /** war型 */
    ASSET_UNIT_TYPE_WAR("war", "war型", "ASSET_UNIT_TYPE"),
    /** topology型 */
    ASSET_UNIT_TYPE_TOPOLOGY("topology", "topology型", "ASSET_UNIT_TYPE"),

    /** 资产使用区域 */
    ASSET_REGION("AST_REGION", "资产使用区域", "-1"),
    /** 租户可见 */
    ASSET_REGION_PUBLIC("pubilcRegion", "租户可见", "AST_REGION"),
    /** 系统分组可见 */
    ASSET_REGION_PRIVATE("privateRegion", "系统分组可见", "AST_REGION"),

    /** 取值类型 */
    VALUE_TYPE("VALUE_TYPE", "取值类型", "-1"),
    /** 数值 */
    VALUE_TYPE_NUMBER("number", "数值", "VALUE_TYPE"),
    /** 字符 */
    VALUE_TYPE_STRING("string", "字符", "VALUE_TYPE"),
    /** 枚举 */
    VALUE_TYPE_ENUM("enum", "枚举", "VALUE_TYPE"),
    /** 布尔 */
    VALUE_TYPE_BOOLEAN("boolean", "布尔", "VALUE_TYPE"),
    /** 密码 */
    VALUE_TYPE_PASSWORD("password", "密码", "VALUE_TYPE"),
    /** 时间 */
    VALUE_TYPE_TIME("time", "时间", "VALUE_TYPE"),
    /** 文本域 */
    VALUE_TYPE_TEXTFIELD("textField", "文本域", "VALUE_TYPE"),

    /** 资产接口可见性 */
    AST_DEPEND_SCOPE("AST_DEPEND_SCOPE", "资产接口可见性", "-1"),
    /** 公有 */
    AST_DEPEND_SCOPE_PUBLLIC("public", "公有", "AST_DEPEND_SCOPE"),
    /** 私有 */
    AST_DEPEND_SCOPE_PRIVATE("private", "私有", "AST_DEPEND_SCOPE"),

    /** 资产依赖限制 */
    AST_DEPEND_RESTRICT("AST_DEPEND_RESTRICT", "资产依赖限制", "-1"),
    /** 强依赖 */
    AST_DEPEND_RESTRICT_STRONG("strong", "强依赖", "AST_DEPEND_RESTRICT"),
    /** 弱依赖 */
    AST_DEPEND_RESTRICT_WEAK("weak", "弱依赖", "AST_DEPEND_RESTRICT"),

    /** 承载目标类型 */
    DEPLOY_TARGET_TYPE("DEPLOY_TARGET_TYPE", "承载目标类型", "-1"),
    /** k8s集群 */
    DEPLOY_TARGET_TYPE_K8S("k8s", "k8s集群", "DEPLOY_TARGET_TYPE"),
    /** 物理机集群 */
    DEPLOY_TARGET_TYPE_METALMACHINE("metalMachine", "物理机集群", "DEPLOY_TARGET_TYPE"),
    /** 资产 */
    DEPLOY_TARGET_TYPE_ASSET("asset", "资产", "DEPLOY_TARGET_TYPE"),

    /** 重启策略 */
    RESTART_POLICY("RESTART_POLICY", "重启策略", "-1"),
    /** 容器失效时自动重启 */
    RESTART_POLICY_ALWAYS("always", "容器失效时自动重启", "RESTART_POLICY"),
    /** 当容器终止运行且退出码不为0时重启 */
    RESTART_POLICY_ONFAILURE("onfailure", "当容器终止运行且退出码不为0时重启", "RESTART_POLICY"),
    /** 不重启 */
    RESTART_POLICY_NEVER("Never", "不重启", "RESTART_POLICY"),

    /** 控制接口执行类型 */
    CNTL_INTFC_EXECUTE_TYPE("CNTL_INTFC_EXECUTE_TYPE", "控制接口执行类型", "-1"),
    /** 由执行引擎生成 */
    CNTL_INTFC_EXECUTE_TYPE_DEFAULT("default", "由执行引擎生成", "CNTL_INTFC_EXECUTE_TYPE"),
    /** 资产包提供 */
    CNTL_INTFC_EXECUTE_TYPE_CUSTOM("custom", "资产包提供", "CNTL_INTFC_EXECUTE_TYPE"),
    /** 不支持 */
    CNTL_INTFC_EXECUTE_TYPE_UNSUPPORTED("unsupported", "不支持", "CNTL_INTFC_EXECUTE_TYPE"),

    /** 控制接口执行目标 */
    CNTL_INTFC_EXECUTE_TARGET("CNTL_INTFC_EXECUTE_TARGET", "控制接口执行目标", "-1"),
    /** 容器 */
    CNTL_INTFC_EXECUTE_TARGET_CONTAINER("container", "容器", "CNTL_INTFC_EXECUTE_TARGET"),
    /** 节点 */
    CNTL_INTFC_EXECUTE_TARGET_NODE("node", "节点", "CNTL_INTFC_EXECUTE_TARGET"),
    /** 集群 */
    CNTL_INTFC_EXECUTE_TARGET_CLUSTER("cluster", "集群", "CNTL_INTFC_EXECUTE_TARGET"),
    /** 执行引擎主机 */
    CNTL_INTFC_EXECUTE_TARGET_ENGINEHOST("engineHost", "执行引擎主机", "CNTL_INTFC_EXECUTE_TARGET"),

    /** 服务接口接口适用范围 */
    SVR_INTFC_SCOPE("SVR_INTFC_SCOPE", "服务接口接口适用范围", "-1"),
    /** 资产内部 */
    SVR_INTFC_SCOPE_INNER("inner", "资产内部", "SVR_INTFC_SCOPE"),
    /** 框架内 */
    SVR_INTFC_SCOPE_FRAME("frame", "框架内", "SVR_INTFC_SCOPE"),
    /** 集群内 */
    SVR_INTFC_SCOPE_CLUSTER("cluster", "集群内", "SVR_INTFC_SCOPE"),
    /** 跨集群 */
    SVR_INTFC_SCOPE_CROSSCLUSTER("crossCluster", "跨集群", "SVR_INTFC_SCOPE"),

    /** 资产状态 */
    ASSET_STATUS("ASSET_STATUS", "资产状态", "-1"),
    /** 新增 */
    ASSET_STATUS_10("10", "新增", "ASSET_STATUS"),
    /** 审批中 */
    ASSET_STATUS_11("11", "审批中", "ASSET_STATUS"),
    /** 已入库 */
    ASSET_STATUS_12("12", "已入库", "ASSET_STATUS"),
    /** 失效 */
    ASSET_STATUS_13("13", "失效", "ASSET_STATUS"),
    /** 删除 */
    ASSET_STATUS_14("14", "删除", "ASSET_STATUS"),

    /** 资产销售状态 */
    ASSET_SALE_STATUS("ASSET_SALE_STATUS", "资产销售状态", "-1"),
    /** 未销售 */
    ASSET_SALE_STATUS_11("11", "未销售", "ASSET_SALE_STATUS"),
    /** 已上传未审核 */
    ASSET_SALE_STATUS_12("12", "已上传未审核", "ASSET_SALE_STATUS"),
    /** 已销售 */
    ASSET_SALE_STATUS_13("13", "已销售", "ASSET_SALE_STATUS"),

    /** 资产审批状态 */
    ASSET_APPROVE_STATUS("ASSET_APPROVE_STATUS", "资产审批状态", "-1"),
    /** 申请中 */
    ASSET_APPROVE_STATUS_11("11", "申请中", "ASSET_APPROVE_STATUS"),
    /** 审批通过 */
    ASSET_APPROVE_STATUS_12("12", "审批通过", "ASSET_APPROVE_STATUS"),
    /** 审批未通过 */
    ASSET_APPROVE_STATUS_13("13", "审批未通过", "ASSET_APPROVE_STATUS"),

    /** 服务发布状态 */
    SVR_RELEASE_STATUS("SVR_RELEASE_STATUS", "服务发布状态", "-1"),
    /** 待分配 */
    SVR_RELEASE_STATUS_1("1", "待分配", "SVR_RELEASE_STATUS"),
    /** 待编排 */
    SVR_RELEASE_STATUS_2("2", "待编排", "SVR_RELEASE_STATUS"),
    /** 完成 */
    SVR_RELEASE_STATUS_0("0", "完成", "SVR_RELEASE_STATUS"),
    /** 待发布 */
    SVR_RELEASE_STATUS_3("3", "待发布", "SVR_RELEASE_STATUS"),
    /** 待审批 */
    SVR_RELEASE_STATUS_4("4", "待审批", "SVR_RELEASE_STATUS"),

    /** 日志类别 */
    LOG_CATEGORY("LOG_CATEGORY", "日志类别", "-1"),
    /** 应用日志 */
    LOG_CATEGORY_APP("app", "应用日志", "LOG_CATEGORY"),
    /** 过程日志 */
    LOG_CATEGORY_PROCESS("trace", "过程日志", "LOG_CATEGORY"),
    /** 性能日志 */
    LOG_CATEGORY_PERFORMANCE("pf", "性能日志", "LOG_CATEGORY"),
    /** 指标日志 */
    LOG_CATEGORY_INDICATORS("idc", "指标日志", "LOG_CATEGORY"),
    /** 其他日志 */
    LOG_CATEGORY_OTHER("other", "其他日志", "LOG_CATEGORY"),

    /** 存储卷策略 */
    STORAGE_CATEGORY("STORAGE_CATEGORY", "存储卷策略", "-1"),
    /** 卷 */
    STORAGE_CATEGORY_VOLUME("volume", "卷", "STORAGE_CATEGORY"),
    /** 卷集 */
    STORAGE_CATEGORY_VOLUMESET("volumeSet", "卷集", "STORAGE_CATEGORY"),

    /** 框架类型 */
    FRW_TYPE("FRW_TYPE", "框架类型", "-1"),
    /** 新大陆微服务框架 */
    FRW_TYPE_NLCLOUD("nl-cloud", "新大陆微服务框架", "FRW_TYPE"),

    /** 框架类型 */
    FRW_NLCLOUD_SERVICE_MODE("FRW_NLCLOUD_SERVICE_MODE", "框架类型", "-1"),
    /** 单节点模式 */
    FRW_NLCLOUD_SERVICE_MODE_SINGLE("single", "单节点模式", "FRW_NLCLOUD_SERVICE_MODE_SINGLE"),
    /** 高可用模式 */
    FRW_NLCLOUD_SERVICE_MODE_HA("ha", "高可用模式", "FRW_NLCLOUD_SERVICE_MODE_HA"),

    /** 集群状态 */
    CLUSTER_STATUS("CLUSTER_STATUS", "集群状态", "-1"),
    /** 新增 */
    CLUSTER_STATUS_SAVE("save", "新增", "CLUSTER_STATUS"),
    /** 部署中 */
    CLUSTER_STATUS_DEPLOYING("deploying", "部署中", "CLUSTER_STATUS"),
    /** 申请中 */
    CLUSTER_STATUS_APPLYING("applying", "申请中", "CLUSTER_STATUS"),
    /** 启动 */
    CLUSTER_STATUS_STARTED("started", "启动", "CLUSTER_STATUS"),
    /** 启动中 */
    CLUSTER_STATUS_STARTING("starting", "启动中", "CLUSTER_STATUS"),
    /** 停止 */
    CLUSTER_STATUS_STOPED("stopped", "停止", "CLUSTER_STATUS"),
    /** 停止中 */
    CLUSTER_STATUS_STOPING("stoping", "停止中", "CLUSTER_STATUS"),
    /** 卸载中 */
    CLUSTER_STATUS_UNINSTALLING("uninstalling", "卸载中", "CLUSTER_STATUS"),
    /** 已卸载 */
    CLUSTER_STATUS_UNINSTALLED("uninstalled", "已卸载", "CLUSTER_STATUS"),
    /** 失败 */
    CLUSTER_STATUS_FAIL("fail", "失败", "CLUSTER_STATUS"),
    /** 卸载申请中 */
    CLUSTER_STATUS_UNINSTALLAPPLLYING("uninstallApplying", "卸载申请中", "CLUSTER_STATUS"),

    /** 集群来源 */
    CLU_SOURCE_TYPE("CLU_SOURCE_TYPE", "集群来源", "-1"),
    /** 自建集群 */
    CLU_SOURCE_TYPE_CUSTOM("CUSTOM", "自建集群", "CLU_SOURCE_TYPE"),
    /** 申请集群 */
    CLU_SOURCE_TYPE_APPLY_CLU("APPLY_CLU", "申请集群", "CLU_SOURCE_TYPE"),

    /** 资源使用范围 */
    RESES_USESCOPE("RESES_USESCOPE", "资源使用范围", "-1"),
    /** 运管 */
    RESES_USESCOPE_OPERATIONS_MANAGEMENT("operations_management", "运管", "RESES_USESCOPE"),
    /** 保留 */
    RESES_USESCOPE_RETAIN("retain", "保留", "RESES_USESCOPE"),
    /** 共享 */
    RESES_USESCOPE_SHARE("share", "共享", "RESES_USESCOPE"),
    /** 租户 */
    RESES_USESCOPE_TENANT("tenant", "租户", "RESES_USESCOPE"),

    /** 资源使用范围 */
    ELAS_TASK_SOURCE("ELAS_TASK_SOURCE", "资源使用范围", "-1"),
    /** 策略 */
    ELAS_TASK_SOURCE_STRATEGY("1", "策略", "TASK_SOURCE"),
    /** 手工 */
    ELAS_TASK_SOURCE_MANUAL("2", "手工", "TASK_SOURCE"),

    /** 弹性任务状态 */
    ELAS_TASK_STATUS("ELAS_TASK_SOURCE", "弹性任务状态", "-1"),
    /** 待执行 */
    ELAS_TASK_STATUS_TOPERFORM("1", "待执行", "TASK_STATUS"),
    /** 执行中 */
    ELAS_TASK_STATUS_PERFORMING("2", "执行中", "TASK_STATUS"),
    /** 失败 */
    ELAS_TASK_STATUS_FAILED("4", "失败", "TASK_STATUS"),
    /** 执行完成 */
    ELAS_TASK_STATUS_FINISH("9", "执行完成", "TASK_STATUS"),

    /** 租户配额计算方式 */
    TENANT_QUOTA_CALC_TYPE("TENANT_QUOTA_CALC_TYPE", "租户配额计算方式", "-1"),
    /** 按百分比 */
    TENANT_QUOTA_CALC_TYPE_PERCENTAGE("1", "按百分比", "TENANT_QUOTA_CALC_TYPE"),
    /** 具体配额值 */
    TENANT_QUOTA_CALC_TYPE_QUOTAVALUES("2", "具体配额值", "TENANT_QUOTA_CALC_TYPE"),

    /** 菜单操作类型 */
    MENU_OPER_TYPE("MENU_OPER_TYPE", "菜单操作类型", "-1"),
    /** 菜单 */
    MENU_OPER_TYPE_MENU("1", "菜单", "MENU_OPER_TYPE"),

    /** 操作 */
    MENU_OPER_TYPE_OPERATE("2", "操作", "MENU_OPER_TYPE"),

    /** 对象类型 */
    OBJECT_TYPE("OBJECT_TYPE", "对象类型", "-1"),
    /** 资产 */
    OBJECT_TYPE_ASSETS("assets", "资产", "OBJECT_TYPE"),

    /** 应用 */
    OBJECT_TYPE_APPLICATION("application", "应用", "OBJECT_TYPE"),
    /** 服务 */
    OBJECT_TYPE_SERVICE("service", "服务", "OBJECT_TYPE"),

    /** 集群 */
    OBJECT_TYPE_CLUSTER("cluster", "集群", "OBJECT_TYPE"),

    /** 集群分区 */
    OBJECT_TYPE_CLUSTER_PARTITION("cluster_partition", "集群分区", "OBJECT_TYPE"),

    /** 服务实例 */
    OBJECT_TYPE_SERVICE_INST("service_inst", "服务实例", "OBJECT_TYPE"),

    /** ingress负载均衡器 */
    OBJECT_TYPE_INGRESS("ingress", "ingress负载均衡器", "OBJECT_TYPE"),
    /** 持久卷 */
    OBJECT_TYPE_VOL("vol", "持久卷", "OBJECT_TYPE"),

    /** 角色类型 */
    ROLE_TYPE("ROLE_TYPE", "角色类型", "-1"),
    /** 全局角色 */
    ROLE_TYPE_GLOBAL("1", "全局角色", "ROLE_TYPE"),
    /** 工组角色 */
    ROLE_TYPE_CREW("2", "工组角色", "ROLE_TYPE"),

    /** 操作执行步骤状态 */
    OPERATION_STAGE_STATUS("OPERATION_STAGE_STATUS", "操作执行步骤状态 ", "-1"),
    /** 进行中 */
    OPERATION_STAGE_STATUS_PROGREESSING("1", "进行中", "OPERATION_STAGE_STATUS"),
    /** 成功 */
    OPERATION_STAGE_STATUS_SUCCESS("2", "成功", "OPERATION_STAGE_STATUS"),
    /** 失败 */
    OPERATION_STAGE_STATUS_FALIED("3", "失败", "OPERATION_STAGE_STATUS"),

    /** 连接方式 */
    CONNECTION_TYPE("CONNECTION_TYPE", "连接方式", "-1"),
    /** TCP */
    CONNECTION_TYPE_TCP("TCP", "TCP", "CONNECTION_TYPE"),
    /** UDP */
    CONNECTION_TYPE_UDP("UDP", "UDP", "CONNECTION_TYPE"),

    /** 承载目标类型 */
    BEAR_TARGET_TYPE("BEAR_TARGET_TYPE", "承载目标类型", "-1"),
    /** 集群 */
    BEAR_TARGET_TYPE_CLUSTER("1", "集群", "BEAR_TARGET_TYPE"),
    /** 实例 */
    BEAR_TARGET_TYPE_INSTANCE("2", "实例", "BEAR_TARGET_TYPE"),

    /** 平台模块 */
    PF_MODULE("PF_MODULE", "平台模块", "-1"),
    /** 系统 */
    PF_MODULE_SYSMGR("sysmgr", "系统", "PF_MODULE"),
    /** 公共 */
    PF_MODULE_COMMON("common", "公共", "PF_MODULE"),
    /** 集群 */
    PF_MODULE_CLUMGR("clumgr", "集群", "PF_MODULE"),
    /** 资源 */
    PF_MODULE_RESMGR("resmgr", "资源", "PF_MODULE"),
    /** 应用 */
    PF_MODULE_APPMGR("appmgr", "应用", "PF_MODULE"),
    /** 服务 */
    PF_MODULE_SVRMGR("svrmgr", "服务", "PF_MODULE"),
    /** 资产 */
    PF_MODULE_ASTMGR("astmgr", "资产", "PF_MODULE"),

    /** 审计类别 */
    AUDIT_CATEGORY("AUDIT_CATEGORY", "审计类别", "-1"),
    /** 对象操作 */
    AUDIT_CATEGORY_OBJECT_OPERATE("object_operate", "对象操作", "AUDIT_CATEGORY"),

    /** 日志解析器 */
    LOG_RESOLVER("LOG_RESOLVER", "日志解析器", "-1"),
    /** 无 */
    LOG_RESOLVER_NONE("none", "无", "LOG_RESOLVER"),
    /** kv解析 */
    LOG_RESOLVER_KV("kv", "kv解析", "LOG_RESOLVER"),
    /** split解析 */
    LOG_RESOLVER_SPLIT("split", "split解析", "LOG_RESOLVER"),
    /** 正则解析 */
    LOG_RESOLVER_REGEX("regex", "正则解析", "LOG_RESOLVER"),

    /** 日志解析器 */
    ALARM_LEVEL("ALARM_LEVEL", "日志解析器", "-1"),
    /** 一般 */
    ALARM_LEVEL_GENERAL("1", "一般", "ALARM_LEVEL"),
    /** 重要 */
    ALARM_LEVEL_IMPORTANT("2", "重要", "ALARM_LEVEL"),
    /** 紧急 */
    ALARM_LEVEL_URGENT("3", "紧急", "ALARM_LEVEL"),

    /** 对象操作定义 */
    OBJ_OPERATE_DEFINE("OBJ_OPERATE_DEFINE", "对象操作定义", "-1"),
    /** 资产 */
    OBJ_OPERATE_DEFINE_ASSETS_OP("assets_op", "资产", "OBJ_OPERATE_DEFINE"),
    /** 生效 */
    ASSETS_OP_PF_VALID("pf_valid", "生效", "assets_op"),
    /** 失效 */
    ASSETS_OP_PF_INVALID("pf_invalid", "失效", "assets_op"),
    /** 删除 */
    ASSETS_OP_PF_DELETE("pf_delete", "删除", "assets_op"),
    /** 导出申请 */
    ASSETS_OP_PF_EXPORT_APPLY("pf_exportApply", "导出申请", "assets_op"),
    /** 详情 */
    ASSETS_OP_PF_DETAIL("pf_detail", "详情", "assets_op"),
    /** 导出 */
    ASSETS_OP_PF_EXPORT_PAG("pf_exportPag", "导出", "assets_op"),
    /** 入库申请 */
    ASSETS_OP_PF_APPLY("pf_apply", "入库申请", "assets_op"),

    /** 原生资产境像 */
    OBJ_OPERATE_DEFINE_ASSETS_CONTAINER_OP("assets_container_op", "原生资产境像", "OBJ_OPERATE_DEFINE"),
    /** 删除 */
    ASSETS_CONTAINER_OP_PF_DELETE("pf_delete", "删除", "assets_container_op"),

    /** 资产编排 */
    OBJ_OPERATE_DEFINE_ASSETS_M_OP("assets_m_op", "资产编排", "OBJ_OPERATE_DEFINE"),
    /** 详情 */
    ASSETS_M_OP_PF_DETAIL("pf_detail", "详情", "assets_m_op"),
    /** 导出 */
    ASSETS_M_OP_PF_EXPORT("pf_export", "导出", "assets_m_op"),
    /** 编辑 */
    ASSETS_M_OP_PF_MODIFY("pf_modify", "编辑", "assets_m_op"),
    /** 删除 */
    ASSETS_M_OP_PF_DELETE("pf_delete", "删除", "assets_m_op"),
    /** 引用 */
    ASSETS_M_OP_PF_QUOTE("pf_quote", "引用", "assets_m_op"),

    /** 应用 */
    OBJ_OPERATE_DEFINE_APPLICATION_OP("application_op", "应用", "OBJ_OPERATE_DEFINE"),
    /** 修改 */
    APPLICATION_OP_PF_MODIFY("pf_modify", "修改", "application_op"),
    /** 删除 */
    APPLICATION_OP_PF_DELETE("pf_delete", "删除", "application_op"),
    /** 部署 */
    APPLICATION_OP_PF_DEPLOY("pf_deploy", "部署", "application_op"),
    /** 启动 */
    APPLICATION_OP_PF_START("pf_start", "启动", "application_op"),
    /** 停止 */
    APPLICATION_OP_PF_STOP("pf_stop", "停止", "application_op"),
    /** 卸载 */
    APPLICATION_OP_PF_UNINSTALL("pf_uninstall", "卸载", "application_op"),
    /** 赋权 */
    APPLICATION_OP_PF_EMPOWER("pf_empower", "赋权", "application_op"),
    /** 伸缩 */
    APPLICATION_OP_PF_SCALE("pf_scale", "伸缩", "application_op"),
    /** 策略 */
    APPLICATION_OP_PF_POLICY("pf_policy", "策略", "application_op"),
    /** 升级 */
    APPLICATION_OP_PF_UPGRADE("pf_upgrade", "升级", "application_op"),
    /** 回退 */
    APPLICATION_OP_PF_ROLLBACK("pf_rollback", "回退", "application_op"),
    /** 迁移 */
    APPLICATION_OP_PF_MIGRATE("pf_migrate", "迁移", "application_op"),

    /** 上线 */
    APPLICATION_OP_PF_ONLINE("pf_online", "上线", "application_op"),
    /** 下线 */
    APPLICATION_OP_PF_OFFLINE("pf_offline", "下线", "application_op"),
    /** 执行 */
    APPLICATION_OP_PF_EXECUTE("pf_execute", "执行", "application_op"),
    /** 滚动升级 */
    APPLICATION_OP_PF_ROLLING_UP("pf_rolling_up", "滚动升级", "application_op"),

    /** 服务 */
    OBJ_OPERATE_DEFINE_SERVICE_OP("service_op", "服务", "OBJ_OPERATE_DEFINE"),
    /** 删除 */
    SERVICE_OP_PF_DELETE("pf_delete", "删除", "service_op"),
    /** 启动 */
    SERVICE_OP_PF_START("pf_start", "启动", "service_op"),
    /** 停止 */
    SERVICE_OP_PF_STOP("pf_stop", "停止", "service_op"),
    /** 赋权 */
    SERVICE_OP_PF_EMPOWER("pf_empower", "赋权", "service_op"),
    /** 伸缩 */
    SERVICE_OP_PF_SCALE("pf_scale", "伸缩", "service_op"),
    /** 策略 */
    SERVICE_OP_PF_POLICY("pf_policy", "策略", "service_op"),
    /** 详情 */
    SERVICE_OP_PF_DETAIL("pf_detail", "详情", "service_op"),

    /** 集群 */
    OBJ_OPERATE_DEFINE_CLUSTER_OP("cluster_op", "集群", "OBJ_OPERATE_DEFINE"),
    /** 集群修改 */
    CLUSTER_OP_PF_MODIFY("pf_modify", "集群修改", "cluster_op"),
    /** 集群删除 */
    CLUSTER_OP_PF_DELETE("pf_delete", "集群删除", "cluster_op"),
    /** 部署 */
    CLUSTER_OP_PF_DEPLOY("pf_deploy", "部署", "cluster_op"),
    /** 卸载 */
    CLUSTER_OP_PF_UNINSTALL("pf_uninstall", "卸载", "cluster_op"),
    /** 缩容 */
    CLUSTER_OP_PF_SCALE_IN("pf_scale_in", "缩容", "cluster_op"),
    /** 扩容 */
    CLUSTER_OP_PF_SCALE_OUT("pf_scale_out", "扩容", "cluster_op"),
    /** 部署申请 */
    CLUSTER_OP_PF_DEPLOY_APPLY("pf_deploy_apply", "部署申请", "cluster_op"),
    /** 部署审批 */
    CLUSTER_OP_PF_DEPLOY_APPROVE("pf_deploy_approve", "部署审批", "cluster_op"),
    /** 卸载申请 */
    CLUSTER_OP_PF_UNINSTALL_APPLY("pf_uninstall_apply", "卸载申请", "cluster_op"),
    /** 卸载审批 */
    CLUSTER_OP_PF_UNINSTALL_APPROVE("pf_uninstall_approve", "卸载审批", "cluster_op"),
    /** 缩容申请 */
    CLUSTER_OP_PF_SCALEIN_APPLY("pf_scaleIn_apply", "缩容申请", "cluster_op"),
    /** 缩容审批 */
    CLUSTER_OP_PF_SCALEIN_APPROVE("pf_scaleIn_approve", "缩容审批", "cluster_op"),
    /** 扩容申请 */
    CLUSTER_OP_PF_SCALEOUT_APPLY("pf_scaleOut_apply", "扩容申请", "cluster_op"),
    /** 扩容审批 */
    CLUSTER_OP_PF_SCALEOUT_APPROVE("pf_scaleOut_approve", "扩容审批", "cluster_op"),
    /** 创建分区配额 */
    CLUSTER_OP_PF_CLUSTER_PARTITION_CREATE("pf_cluster_partition_create", "创建分区配额", "cluster_op"),
    /** 修改标签 */
    CLUSTER_OP_PF_UPDT_LBL("pf_updt_lbl", "修改标签", "cluster_op"),
    /** 新增分区 */
    CLUSTER_OP_PF_ADD_ZONE("pf_add_zone", "新增分区", "cluster_op"),

    /** 集群分区 */
    OBJ_OPERATE_DEFINE_CLUSTER_PARTITION_OP("cluster_partition_op", "集群分区", "OBJ_OPERATE_DEFINE"),
    /** 创建分区配额 */
    CLUSTER_PARTITION_OP_PF_ADD_ZONE("pf_add_zone", "分区创建 ", "cluster_partition_op"),
    /** 分区配额修改 */
    CLUSTER_PARTITION_OP_PF_MODIFY("pf_modify", "分区修改", "cluster_partition_op"),
    /** 分区删除 */
    CLUSTER_PARTITION_OP_PF_DELETE("pf_delete", "分区删除", "cluster_partition_op"),
    /** 系统分组分配 */
    CLUSTER_PARTITION_OP_PF_SYS_CATEGORY_ASSIGN("pf_sys_category_assign", "系统分组分配", "cluster_partition_op"),
    /** 工组配额 */
    CLUSTER_PARTITION_OP_PF_WG_QUOTA("pf_wg_quota", "工组配额", "cluster_partition_op"),
    /** 系统分组配额 */
    CLUSTER_PARTITION_OP_PF_CATEGORY_QUOTA("pf_category_quota", "系统分组配额", "cluster_partition_op"),

    /** 服务实例 */
    OBJ_OPERATE_DEFINE_SERVICE_INST_OP("service_inst_op", "服务实例", "OBJ_OPERATE_DEFINE"),

    /** 修改 */
    SERVICE_INST_OP_PF_MODIFY("pf_modify", "修改", "service_inst_op"),
    /** 删除 */
    SERVICE_INST_OP_PF_DELETE("pf_delete", "删除", "service_inst_op"),
    /** 部署 */
    SERVICE_INST_OP_PF_DEPLOY("pf_deploy", "部署", "service_inst_op"),
    /** 启动 */
    SERVICE_INST_OP_PF_START("pf_start", "启动", "service_inst_op"),
    /** 停止 */
    SERVICE_INST_OP_PF_STOP("pf_stop", "停止", "service_inst_op"),
    /** 卸载 */
    SERVICE_INST_OP_PF_UNINSTALL("pf_uninstall", "卸载", "service_inst_op"),
    /** 赋权 */
    SERVICE_INST_OP_PF_EMPOWER("pf_empower", "赋权", "service_inst_op"),
    /** 伸缩 */
    SERVICE_INST_OP_PF_SCALE("pf_scale", "伸缩", "service_inst_op"),
    /** 策略 */
    SERVICE_INST_OP_PF_POLICY("pf_policy", "策略", "service_inst_op"),

    /** ingress */
    OBJ_OPERATE_DEFINE_INGRESS_OP("ingress_op", "ingress", "OBJ_OPERATE_DEFINE"),

    /** 修改 */
    INGRESS_OP_PF_MODIFY("pf_modify", "修改", "ingress_op"),
    /** 删除 */
    INGRESS_OP_PF_DELETE("pf_delete", "删除", "ingress_op"),
    /** 详情 */
    INGRESS_OP_PF_DETAIL("pf_detail", "详情", "ingress_op"),

    /** 单元 */
    OBJ_OPERATE_DEFINE_UNIT_OP("unit_op", "单元", "OBJ_OPERATE_DEFINE"),
    /** 修改 */
    UNIT_OP_PF_MODIFY("pf_modify", "修改", "unit_op"),
    /** 删除 */
    UNIT_OP_PF_DELETE("pf_delete", "删除", "unit_op"),
    /** 删除 */
    UNIT_OP_PF_STOP("pf_stop", "删除", "unit_op"),

    /** 集群节点类型 */
    CLUSTER_NODE_TYPE("CLUSTER_NODE_TYPE", "集群节点类型", "-1"),
    /** etcd节点 */
    CLUSTER_NODE_TYPE_ETCD("etcd", "etcd节点", "CLUSTER_NODE_TYPE"),
    /** master节点 */
    CLUSTER_NODE_TYPE_MASTER("master", "master节点", "CLUSTER_NODE_TYPE"),
    /** work节点 */
    CLUSTER_NODE_TYPE_WORK("work", "work节点", "CLUSTER_NODE_TYPE"),

    /** 集群负载均衡器类型 */
    CLUSTER_LB_TYPE("CLUSTER_LB_TYPE", "集群负载均衡器类型", "-1"),
    /** ingress负载均衡器 */
    CLUSTER_LB_TYPE_INGRESS("ingress", "ingress负载均衡器", "CLUSTER_LB_TYPE"),

    /** 集群负载均衡器算法 */
    CLUSTER_LB_ALGORITHM("CLUSTER_LB_ALGORITHM", "集群负载均衡器算法", "-1"),
    /** 最少连接数 */
    CLUSTER_LB_ALGORITHM_LEASTCONNECT("leastConnect", "最少连接数", "CLUSTER_LB_ALGORITHM"),

    /** 集群操作类型 */
    CLUSTER_OP_TYPE("CLUSTER_OP_TYPE", "集群操作类型", "-1"),
    /** 部署 */
    CLUSTER_OP_TYPE_DEPLOY("deploy", "部署", "CLUSTER_OP_TYPE"),
    /** 启动 */
    CLUSTER_OP_TYPE_START("start", "启动", "CLUSTER_OP_TYPE"),
    /** 停止 */
    CLUSTER_OP_TYPE_STOP("stop", "停止", "CLUSTER_OP_TYPE"),
    /** 卸载 */
    CLUSTER_OP_TYPE_UNINSTALL("uninstall", "卸载", "CLUSTER_OP_TYPE"),
    /** 缩容 */
    CLUSTER_OP_TYPE_SCALEIN("scaleIn", "缩容", "CLUSTER_OP_TYPE"),
    /** 扩容 */
    CLUSTER_OP_TYPE_SCALEOUT("scaleOut", "扩容", "CLUSTER_OP_TYPE"),
    /** 部署申请 */
    CLUSTER_OP_TYPE_DEPLOYAPPLY("deployApply", "部署申请", "CLUSTER_OP_TYPE"),
    /** 卸载申请 */
    CLUSTER_OP_TYPE_UNINSTALLAPPLY("uninstallApply", "卸载申请", "CLUSTER_OP_TYPE"),
    /** 缩容申请 */
    CLUSTER_OP_TYPE_SCALEINAPPLY("scaleInApply", "缩容申请", "CLUSTER_OP_TYPE"),
    /** 扩容申请 */
    CLUSTER_OP_TYPE_SCALEOUTAPPLY("scaleOutApply", "扩容申请", "CLUSTER_OP_TYPE"),

    /** 应用分类 */
    APP_CATEGORY("APP_CATEGORY", "应用分类", "-1"),
    /** 有状态应用 */
    APP_CATEGORY_STATUS("1", "有状态应用", "APP_CATEGORY"),
    /** 后台应用 */
    APP_CATEGORY_BACKAPP("2", "后台应用", "APP_CATEGORY"),
    /** 微服务应用 */
    APP_CATEGORY_MICROSERVICE("3", "微服务应用", "APP_CATEGORY"),
    /** 调度应用 */
    APP_CATEGORY_DISPATCH("4", "调度应用", "APP_CATEGORY"),

    /** 资源配额 */
    RESOURCE_LIMIT_CATEGORY("RESOURCE_LIMIT_CATEGORY", "资源配额", "-1"),
    /** CPU */
    RESOURCE_LIMIT_CATEGORY_CPU("cpu", "CPU", "RESOURCE_LIMIT_CATEGORY"),
    /** 内存 */
    RESOURCE_LIMIT_CATEGORY_MEMORY("memory", "内存", "RESOURCE_LIMIT_CATEGORY"),
    /** 存储 */
    RESOURCE_LIMIT_CATEGORY_STORAGE("storage", "存储", "RESOURCE_LIMIT_CATEGORY"),
    /** 台数 */
    RESOURCE_LIMIT_CATEGORY_MACHINE("machine", "台数", "RESOURCE_LIMIT_CATEGORY"),

    /** 服务分类图标 */
    SVR_CATEGORY_ICON("SVR_CATEGORY_ICON", "服务分类图标", "-1"),
    /** CPU */
    SVR_CATEGORY_ICON_ICON_BUSINESS_SERVICES("icon-business-services", "CPU", "SVR_CATEGORY_ICON"),
    /** 数据库 */
    SVR_CATEGORY_ICON_ICON_DATA_BASE("icon-data-base", "数据库", "SVR_CATEGORY_ICON"),
    /** 大数据 */
    SVR_CATEGORY_ICON_ICON_BIG_DATA("icon-big-data", "大数据", "SVR_CATEGORY_ICON"),
    /** 云计算 */
    SVR_CATEGORY_ICON_ICON_CLOUD_COMPUTING("icon-cloud-computing", "云计算", "SVR_CATEGORY_ICON"),

    /** 执行方式 */
    EXECUTE_WAY("EXECUTE_WAY", "执行方式", "-1"),
    /** 单机-集群中其中一台执行 */
    EXECUTE_WAY_ALONE("alone", "单机-集群中其中一台执行", "EXECUTE_WAY"),
    /** 多机运行中-集群中正在运行的主机执行 */
    EXECUTE_WAY_MULTIRUN("multiRun", "多机运行中-集群中正在运行的主机执行", "EXECUTE_WAY"),
    /** 多机全部-集群中所有主机 */
    EXECUTE_WAY_MULTIALL("multiAll", "多机全部-集群中所有主机", "EXECUTE_WAY"),

    /** 脚本类型 */
    SCRIPT_TYPE("SCRIPT_TYPE", "脚本类型", "-1"),
    /** ansible */
    SCRIPT_TYPE_ANSIBLE("ansible", "ansible", "SCRIPT_TYPE"),
    /** shell */
    SCRIPT_TYPE_SHELL("shell", "shell", "SCRIPT_TYPE"),
    /** beanshell */
    SCRIPT_TYPE_BEANSHELL("beanshell", "beanshell", "SCRIPT_TYPE"),
    /** javaClass */
    SCRIPT_TYPE_JAVACLASS("javaClass", "javaClass", "SCRIPT_TYPE"),

    /** 命令类别 */
    CMD_CATG("CMD_CATG", "命令类别", "-1"),
    /** 资源 */
    CMD_CATG_RES("res", "资源", "CMD_CATG"),
    /** k8s集群 */
    CMD_CATG_K8SCLU("k8sClu", "k8s集群", "CMD_CATG"),
    /** K8s集群苏研 */
    CMD_CATG_K8SCLUCMSS("k8sCluCmss", "K8s集群苏研", "CMD_CATG"),
    /** 物理机集群 */
    CMD_CATG_PHYSCLU("physClu", "物理机集群", "CMD_CATG"),
    /** K8s集群分区 */
    CMD_CATG_K8SZONE("k8sZone", "K8s集群分区", "CMD_CATG"),
    /** ingress */
    CMD_CATG_INGRESS("ingress", "ingress", "CMD_CATG"),
    /** 应用 */
    CMD_CATG_APP("app", "应用", "CMD_CATG"),
    /** 服务 */
    CMD_CATG_SVR("svr", "服务", "CMD_CATG"),
    /** 小应用 */
    CMD_CATG_SMALLAPP("smallApp", "小应用", "CMD_CATG"),
    /** 小服务 */
    CMD_CATG_SMALLSVR("smallSvr", "小服务", "CMD_CATG"),
    /** 单元 */
    CMD_CATG_UNIT("unit", "单元", "CMD_CATG"),
    /** 分布式框架 */
    CMD_CATG_DISTFRAME("distFrame", "分布式框架", "CMD_CATG"),
    /** 作业 */
    CMD_CATG_JOB("job", "作业", "CMD_CATG"),

    /** 执行引擎作业状态 */
    EXEC_JOB_STATUS("EXEC_JOB_STATUS", "执行引擎作业状态", "-1"),
    /** 开始 */
    EXEC_JOB_STATUS_START("start", "开始", "EXEC_JOB_STATUS"),
    /** 准备 */
    EXEC_JOB_STATUS_READY("ready", "准备", "EXEC_JOB_STATUS"),
    /** 运行中 */
    EXEC_JOB_STATUS_RUN("run", "运行中", "EXEC_JOB_STATUS"),
    /** 成功 */
    EXEC_JOB_STATUS_SUCC("succ", "成功", "EXEC_JOB_STATUS"),
    /** 回滚中 */
    EXEC_JOB_STATUS_ROLLBACK("rollback", "回滚中", "EXEC_JOB_STATUS"),
    /** 失败 */
    EXEC_JOB_STATUS_FAILURE("failure", "失败", "EXEC_JOB_STATUS"),
    /** 未知 */
    EXEC_JOB_STATUS_UNKNOWN("unknown", "未知", "EXEC_JOB_STATUS"),

    /** 执行引擎作业明细状态 */
    EXEC_JOB_RUN_STATUS("EXEC_JOB_RUN_STATUS", "执行引擎作业明细状态", "-1"),
    /** 等待 */
    EXEC_JOB_RUN_STATUS_WAIT("wait", "等待", "EXEC_JOB_RUN_STATUS"),
    /** 运行中 */
    EXEC_JOB_RUN_STATUS_RUN("run", "运行中", "EXEC_JOB_RUN_STATUS"),
    /** 成功 */
    EXEC_JOB_RUN_STATUS_SUCC("succ", "成功", "EXEC_JOB_RUN_STATUS"),
    /** 失败 */
    EXEC_JOB_RUN_STATUS_FAILURE("failure", "失败", "EXEC_JOB_RUN_STATUS"),
    /** 未知 */
    EXEC_JOB_RUN_STATUS_UNKNOWN("unknown", "未知", "EXEC_JOB_RUN_STATUS"),

    /** 执行引擎作业明细回滚状态 */
    EXEC_JOB_ROLLBACK_STATUS("EXEC_JOB_ROLLBACK_STATUS", "执行引擎作业明细回滚状态", "-1"),
    /** 无 */
    EXEC_JOB_ROLLBACK_STATUS_WITHOUT("without", "无", "EXEC_JOB_ROLLBACK_STATUS"),
    /** 回滚中 */
    EXEC_JOB_ROLLBACK_STATUS_ROLLBACK("rollback", "回滚中", "EXEC_JOB_ROLLBACK_STATUS"),
    /** 成功 */
    EXEC_JOB_ROLLBACK_STATUS_SUCC("succ", "成功", "EXEC_JOB_ROLLBACK_STATUS"),
    /** 失败 */
    EXEC_JOB_ROLLBACK_STATUS_FAILURE("failure", "失败", "EXEC_JOB_ROLLBACK_STATUS"),
    /** 未知 */
    EXEC_JOB_ROLLBACK_STATUS_UNKNOWN("unknown", "未知", "EXEC_JOB_ROLLBACK_STATUS"),

    /** 节点标签类型 */
    NODE_LABEL_TYPE("NODE_LABEL_TYPE", "节点标签类型", "-1"),
    /** 系统标签 */
    NODE_LABEL_TYPE_SYSTEM("system", "系统标签", "NODE_LABEL_TYPE"),
    /** 自定义标签 */
    NODE_LABEL_TYPE_CUSTOM("custom", "自定义标签", "NODE_LABEL_TYPE"),

    /** 应用版本信息状态 */
    APP_VERSION_STATUS("APP_VERSION_STATUS", "应用版本信息状态", "-1"),
    /** 升级中 */
    APP_VERSION_STATUS_UPGRADE("upgrade", "升级中", "APP_VERSION_STATUS"),
    /** 归档 */
    APP_VERSION_STATUS_ARCHIVING("archiving", "归档", "APP_VERSION_STATUS"),

    /** 框架状态 */
    FRW_STATUS("FRW_STATUS", "框架状态", "-1"),
    /** 待创建 */
    FRW_STATUS_TOBECREATED("tobeCreated", "待创建", "FRW_STATUS"),
    /** 创建中 */
    FRW_STATUS_CREATING("creating", "创建中", "FRW_STATUS"),
    /** 运行 */
    FRW_STATUS_RUNNING("running", "运行", "FRW_STATUS"),
    /** 删除 */
    FRW_STATUS_DELETE("delete", "删除", "FRW_STATUS"),
    /** 删除中 */
    FRW_STATUS_DELETING("deleting", "删除中", "FRW_STATUS"),
    /** 异常 */
    FRW_STATUS_FAILURE("failure", "异常", "FRW_STATUS"),
    /** 失效 */
    FRW_STATUS_INVALID("invalid", "失效", "FRW_STATUS"),

    /** 框架组件状态 */
    FRW_COMPONENT_STATUS("FRW_COMPONENT_STATUS", "框架组件状态", "-1"),
    /** 启动中 */
    FRW_COMPONENT_STATUS_STARTING("starting", "启动中", "FRW_COMPONENT_STATUS"),
    /** 停止中 */
    FRW_COMPONENT_STATUS_STOPING("stoping", "停止中", "FRW_COMPONENT_STATUS"),
    /** 停止 */
    FRW_COMPONENT_STATUS_STOP("stop", "停止", "FRW_COMPONENT_STATUS"),
    /** 异常 */
    FRW_COMPONENT_STATUS_FAILURE("failure", "异常", "FRW_COMPONENT_STATUS"),
    /** 重启中 */
    FRW_COMPONENT_STATUS_RESTARTING("restarting", "重启中", "FRW_COMPONENT_STATUS"),
    /** 删除中 */
    FRW_COMPONENT_STATUS_DELETING("deleting", "删除中", "FRW_COMPONENT_STATUS"),
    /** 删除 */
    FRW_COMPONENT_STATUS_DELETE("delete", "删除", "FRW_COMPONENT_STATUS"),
    /** 运行 */
    FRW_COMPONENT_STATUS_RUNNING("running", "运行", "FRW_COMPONENT_STATUS"),

    /** LB注册状态 */
    LB_REG_STATUS("LB_REG_STATUS", "LB注册状态", "-1"),
    /** 预注册 */
    LB_REG_STATUS_PREREG("preReg", "预注册", "LB_REG_STATUS"),
    /** 已注册 */
    LB_REG_STATUS_REG("reg", "已注册", "LB_REG_STATUS"),

    /** 资产生命周期接口方法名称 */
    ASSET_LIFEINTF_METHOD_NAME("ASSET_LIFEINTF_METHOD_NAME", "资产生命周期接口方法名称", "-1"),
    /** 部署 */
    ASSET_LIFEINTF_METHOD_NAME_PF_DEPLOY("pf_deploy", "部署", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 启动 */
    ASSET_LIFEINTF_METHOD_NAME_PF_START("pf_start", "启动", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 停止 */
    ASSET_LIFEINTF_METHOD_NAME_PF_STOP("pf_stop", "停止", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 卸载 */
    ASSET_LIFEINTF_METHOD_NAME_PF_UNINSTALL("pf_uninstall", "卸载", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 升级卸载 */
    ASSET_LIFEINTF_METHOD_NAME_PF_UP_UNINSTALL("pf_up_uninstall", "升级卸载", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 升级部署 */
    ASSET_LIFEINTF_METHOD_NAME_PF_UP_DEPLOY("pf_up_deploy", "升级部署", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 重启 */
    ASSET_LIFEINTF_METHOD_NAME_PF_RESTART("pf_restart", "重启", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 扩容 */
    ASSET_LIFEINTF_METHOD_NAME_PF_SCALE_OUT("pf_scale_out", "扩容", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 缩容 */
    ASSET_LIFEINTF_METHOD_NAME_PF_SCALE_IN("pf_scale_in", "缩容", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 升级 */
    ASSET_LIFEINTF_METHOD_NAME_PF_UPGRADE("pf_upgrade", "升级", "ASSET_LIFEINTF_METHOD_NAME"),
    /** 滚动升级 */
    ASSET_LIFEINTF_METHOD_NAME_PF_ROLLING_UP("pf_rolling_up", "滚动升级", "ASSET_LIFEINTF_METHOD_NAME"),

    /** 资产其他内置接口方法名称 */
    ASSET_OTHERINTF_METHOD_NAME("ASSET_OTHERINTF_METHOD_NAME", "资产其他内置接口方法名称", "-1"),
    /** 迁移卸载 */
    ASSET_OTHERINTF_METHOD_NAME_PF_MV_UNINSTALL("pf_mv_uninstall", "迁移卸载", "ASSET_OTHERINTF_METHOD_NAME"),
    /** 迁移部署 */
    ASSET_OTHERINTF_METHOD_NAME_PF_MV_DEPLOY("pf_mv_deploy", "迁移部署", "ASSET_OTHERINTF_METHOD_NAME"),

    /** 执行方式 */
    SCP_RUN_MODE("SCP_RUN_MODE", "执行方式", "-1"),
    /** httpGet */
    SCP_RUN_MODE_HTTP_GET("http_get", "httpGet", "SCP_RUN_MODE"),
    /** command */
    SCP_RUN_MODE_COMMAND("command", "command", "SCP_RUN_MODE"),

    /** 挂载类型 */
    POD_MOUNT_TYPE("POD_MOUNT_TYPE", "挂载类型", "-1"),
    /** 日志路径 */
    POD_MOUNT_TYPE_LOG("log", "日志路径", "POD_MOUNT_TYPE"),
    /** 挂载Pod所在宿主机的目录 */
    POD_MOUNT_TYPE_HOSTPATH("hostPath", "挂载Pod所在宿主机的目录", "POD_MOUNT_TYPE"),
    /** 挂载集群与定义的secre对象到容器内部 */
    POD_MOUNT_TYPE_SECRET("secret", "挂载集群与定义的secre对象到容器内部", "POD_MOUNT_TYPE"),
    /** 挂载预定义的configMap对象到容器内部 */
    POD_MOUNT_TYPE_CONFIGMAP("configMap", "挂载预定义的configMap对象到容器内部", "POD_MOUNT_TYPE"),
    /** 存储 */
    POD_MOUNT_TYPE_STORAGE("storage", "存储", "POD_MOUNT_TYPE"),

    /** 日志采集级别 */
    LOG_CLCT_LEVEL("LOG_CLCT_LEVEL", "日志采集级别", "-1"),
    /** ERROR */
    LOG_CLCT_LEVEL_ERROR("60", "ERROR", "LOG_CLCT_LEVEL"),
    /** WARN */
    LOG_CLCT_LEVEL_WARN("40", "WARN", "LOG_CLCT_LEVEL"),
    /** INFO */
    LOG_CLCT_LEVEL_INFO("30", "INFO", "LOG_CLCT_LEVEL"),
    /** DEBUG */
    LOG_CLCT_LEVEL_DEBUG("20", "DEBUG", "LOG_CLCT_LEVEL"),
    /** TRACE */
    LOG_CLCT_LEVEL_TRACE("10", "TRACE", "LOG_CLCT_LEVEL"),

    /** 负载均衡器状态 */
    LB_STATUS("LB_STATUS", "负载均衡器状态", "-1"),
    /** 新增 */
    LB_STATUS_ADD("add", "新增", "LB_STATUS"),
    /** 部署中 */
    LB_STATUS_DEPLOY("deploy", "部署中", "LB_STATUS"), LB_STATUS_FAIL("fail", "部署失败 ", "LB_STATUS"),
    /** 部署成功 */
    LB_STATUS_SCCESS("success", "部署成功", "LB_STATUS"),

    /** 脚本类型 */
    SCP_TYPES("SCP_TYPES", "脚本类型", "-1"),
    /** SQL脚本类型 */
    SCP_TYPE_SQL("sql", "SQL脚本类型", "SCP_TYPES"),
    /** shell脚本类型 */
    SCP_TYPE_SHELL("shell", "shell脚本类型", "SCP_TYPES"),
    /** 配置文件脚本类型 */
    SCP_TYPE_CONFIG_FILE("configFile", "配置文件脚本类型", "SCP_TYPES"),

    /** 判断类型 */
    SYS_DETERMINE_TYPE("SYS_DETERMINE_TYPE", "判断类型", "-1"),
    /** 是 */
    SYS_DETERMINE_TYPE_FALSE("false", "是", "SYS_DETERMINE_TYPE"),
    /** 否 */
    SYS_DETERMINE_TYPE_TRUE("true", "否", "SYS_DETERMINE_TYPE"),

    /** 资产编排状态 */
    AST_MSTATUS("AST_MSTATUS", "资产编排状态", "-1"),
    /** 编排中 */
    AST_MSTATUS_REDACT("redact", "编排中", "AST_MSTATUS"),
    /** 已提交 */
    AST_MSTATUS_SUBMIT("submit", "已提交", "AST_MSTATUS"),

    /** 审批状态 */
    APPROVE_STATUS("APPROVE_STATUS", "审批状态", "-1"),
    /** 新增 */
    APPROVE_STATUS_NEW("new", "新增", "APPROVE_STATUS"),
    /** 审批中 */
    APPROVE_STATUS_APPROVE("approve", "审批中", "APPROVE_STATUS"),
    /** 审批通过 */
    APPROVE_STATUS_APPROVEY("approveY", "审批通过", "APPROVE_STATUS"),
    /** 审批未通过 */
    APPROVE_STATUS_APPROVEN("approveN", "审批未通过", "APPROVE_STATUS"),

    /** 别名对象类型 */
    ALIAS_OBJ_TYPE("ALIAS_OBJ_TYPE", "别名对象类型", "-1"),
    /** 订阅服务对象 */
    ALIAS_SVR_SUBS_INST("ALIAS_SVR_SUBS_INST", "订阅服务对象", "ALIAS_OBJ_TYPE"),
    /** 日志集群 */
    ALIAS_LOG_CLUSTER("ALIAS_LOG_CLUSTER", "日志集群", "ALIAS_OBJ_TYPE"),
    /** 系统组 */
    ALIAS_CATEGORY("ALIAS_CATEGORY", "系统组", "ALIAS_OBJ_TYPE"),
    /** 工组 */
    ALIAS_GROUP("ALIAS_GROUP", "工组", "ALIAS_OBJ_TYPE"),
    /** 集群 */
    ALIAS_CLU("ALIAS_CLU", "集群", "ALIAS_OBJ_TYPE"),

    /** 资源-IP */
    ALIAS_CLU_RES_IP("ALIAS_RES_IP", "资源-IP", "ALIAS_CLU"),
    /** 集群分区 */
    ALIAS_CLU_ZONE("ALIAS_CLU_ZONE", "集群分区", "ALIAS_CLU"),

    /** LB负载均衡 */
    ALIAS_CLU_ZONE_LB("ALIAS_LB", "LB负载均衡", "ALIAS_CLU_ZONE"),
    /** 框架 */
    ALIAS_CLU_ZONE_FRW("ALIAS_FRW", "框架", "ALIAS_CLU_ZONE"),

    /** harbor状态 */
    HARBOR_STATUS("HARBOR_STATUS", "harbor状态", "-1"),
    /** 新建 */
    HARBOR_STATUS_NEW("new", "新建", "HARBOR_STATUS"),
    /** 异常 */
    HARBOR_STATUS_EXCEPTION("exception", "异常", "HARBOR_STATUS"),
    /** 健康 */
    HARBOR_STATUS_HEALTHY("healthy", "健康", "HARBOR_STATUS"),

    HARBOR_JOB_STATUS("HARBOR_JOB_STATUS", "harbor job状态", "-1"),
    /** 等待 */
    HARBOR_JOB_STATUS_WAIT("wait", "等待", "HARBOR_JOB_STATUS"),
    /** 运行中 */
    HARBOR_JOB_STATUS_RUN("run", "运行中", "HARBOR_JOB_STATUS"),
    /** 成功 */
    HARBOR_JOB_STATUS_SUCCESS("success", "成功", "HARBOR_JOB_STATUS"),
    /** 失败 */
    HARBOR_JOB_STATUS_FAILURE("failure", "失败", "HARBOR_JOB_STATUS"),

    HARBOR_JOB_TYPE("HARBOR_JOB_TYPE", "harbor job类型", "-1"),
    /** 添加租户 */
    HARBOR_JOB_TYPE_ADDTENANT("addTenant", "添加租户", "HARBOR_JOB_TYPE"),
    /** 删除租户 */
    HARBOR_JOB_TYPE_DELTENANT("delTenant", "删除租户", "HARBOR_JOB_TYPE"),
    /** 添加机房 */
    HARBOR_JOB_TYPE_ADDROOM("addRoom", "添加机房", "HARBOR_JOB_TYPE"),
    /** 删除机房 */
    HARBOR_JOB_TYPE_DELROOM("delRoom", "删除机房", "HARBOR_JOB_TYPE"),
    /** 仓库纠错 */
    HARBOR_JOB_TYPE_RECOVERY("recovery", "仓库纠错", "HARBOR_JOB_TYPE"),

    HARBOR_JOB_CALL_BACK_STATUS("HARBOR_JOB_CALL_BACK_STATUS", "harbor 回调状态", "-1"),
    /** 未回调 */
    HARBOR_JOB_CALL_BACK_STATUS_NO("0", "未回调", "HARBOR_JOB_CALL_BACK_STATUS"),
    /** 回调成功 */
    HARBOR_JOB_CALL_BACK_STATUS_SUCCESS("1", "回调成功", "HARBOR_JOB_CALL_BACK_STATUS"), HARBOR_JOB_CALL_BACK_STATUS_FAILURE("-1", "回调失败", "HARBOR_JOB_CALL_BACK_STATUS"),

    /** 标签类型 */
    LABEL_TYPE("LABEL_TYPE", "标签类型", "-1"),
    /** 普通 */
    LABEL_TYPE_NORMAL("normal", "普通", "LABEL_TYPE"),
    /** k8s */
    LABEL_TYPE_K8S("k8s", "k8s", "LABEL_TYPE"),

    /** 卷状态 */
    VOL_STATUS("VOL_STATUS", "卷状态", "-1"),
    /** 新增 */
    VOL_STATUS_ADD("add", "新增", "VOL_STATUS"),
    /** 部署中 */
    VOL_STATUS_DEPLOYING("deploying", "部署中", "VOL_STATUS"),
    /** 未挂载 */
    VOL_STATUS_UNMOUNT("unmount", "未挂载", "VOL_STATUS"),
    /** 已挂载 */
    VOL_STATUS_MOUNT("mount", "已挂载", "VOL_STATUS"),
    /** 卸载中 */
    VOL_STATUS_UNINSTALLING("uninstalling", "卸载中", "VOL_STATUS"),
    /** 失败 */
    VOL_STATUS_FAIL("fail", "失败", "VOL_STATUS"),
    /** 申请中 */
    VOL_STATUS_APPLYING("applying", "申请中", "VOL_STATUS"),

    /** 持久卷 */
    OBJ_OPERATE_DEFINE_VOL_OP("vol_op", "持久卷", "OBJ_OPERATE_DEFINE"),
    /** 删除 */
    VOL_OP_PF_DELETE("pf_delete", "删除", "vol_op"),
    /** 绑定 */
    VOL_OP_PF_BIND("pf_bind", "绑定", "vol_op"),
    /** 解绑 */
    VOL_OP_PF_UNBIND("pf_unbind", "解绑", "vol_op"),
    /** 详情 */
    VOL_OP_PF_DETAIL("pf_detail", "详情", "vol_op"),

    /** 持久卷类型 */
    VOL_TYPE("VOL_TYPE", "持久卷类型", "-1"),
    /** 普通卷 */
    VOL_TYPE_NORMAL("normal", "普通卷", "VOL_TYPE"),
    /** 卷集 */
    VOL_TYPE_VOLUMESET("volumeSet", "卷集", "VOL_TYPE"),

    /** 持久卷使用范围 */
    VOL_SCOPE("VOL_SCOPE", "持久卷使用范围", "-1"),
    /** 单元使用 */
    VOL_SCOPE_UNIT("UNIT", "单元使用", "VOL_SCOPE"),
    /** 集群使用 */
    VOL_SCOPE_CLUSTER("CLUSTER", "集群使用", "VOL_SCOPE"),
    /** 集群使用 */
    VOL_SCOPE_TENANT("TENANT", "租户内使用", "VOL_SCOPE"),

    ;

    public final String dictCode;
    public final String dictName;
    public final String dictPcode;

    GlbDictConstants(String dictCode, String dictName, String dictPcode) {
        this.dictCode = dictCode;
        this.dictName = dictName;
        this.dictPcode = dictPcode;
    }
}
