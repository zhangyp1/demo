package com.newland.paas.sbcommon.constants;

/**
 * 执行引擎相关的常量定义
 *
 * @author SongDi
 * @date 2018/11/05
 */
public interface ExecuteEngineConstants {
    /**
     * 默认的公共回调接口
     */
    String COMMON_CALL_BACK_URL = "/v1/execute-engine/call-back";

    /**
     * 指令版本
     */
    interface CmdVersion {

        String ONE = "1";
    }

    /**
     * 指令分类
     */
    interface CmdCatg {
        // 资源
        String RES = "res";
        // k8s集群
        String K8SCLU = "k8sClu";
        // K8s集群苏研
        String K8SCLUCMSS = "k8sCluCmss";
        // 物理机集群
        String PHYSCLU = "physClu";
        // K8s集群分区
        String K8SZONE = "k8sZone";
        // ingress
        String INGRESS = "ingress";
        // 应用实例
        String APP = "app";
        // 单元
        String UNIT = "unit";
        // 框架
        String FRW = "frw";
        // 执行脚本
        String SHELL = "shell";
        // 数据库脚本
        String DATA_BASE = "dataBase";
        // 配置文件
        String CONFIG = "config";
        // 配置文件
        String EXECUTOR = "executor";
        // 作业
        String JOB = "job";
        // ingress通道
        String INGRESS_CHANNEL = "ingressChannel";

        // DNS配置
        String KUBE_DNS_CONFIG_MAP = "kubeDnsConfigMap";

        // 持久卷(普通卷)
        String VOL = "vol";
        // 持久卷(卷集)
        String VOL_SET = "volset";
    }

    /**
     * 指令CODE
     */
    interface CmdCode {
        // 资源免密指令CODE
        String CMD_CODE_AVOID_PASSWORD = "avoidPassword";
        // 资源初始换指令
        String CMD_CODE_RES_INIT = "resInit";
        // k8s集群部署
        String CMD_CODE_PF_DEPLOY = "pf_deploy";
        // k8s集群卸载
        String CMD_CODE_PF_UNINSTALL = "pf_uninstall";
        // k8s集群扩容
        String CMD_CODE_PF_SCALE_OUT = "pf_scale_out";
        // k8s集群缩容
        String CMD_CODE_PF_SCALE_IN = "pf_scale_in";
        // k8s集群修改节点标签
        String CMD_CODE_PF_UPDT_LBL = "pf_updt_lbl";

        String CMD_CODE_PF_MODIFY_QUOTA = "pf_modify_quota";
        //
        String CMD_CODE_PF_REINSTALL = "pf_reinstall";

        // 应用单元启动
        String CMD_CODE_UNIT_PF_START = "pf_start";
        // 应用单元停止
        String CMD_CODE_UNIT_PF_STOP = "pf_stop";
        // 应用单元重启
        String CMD_CODE_UNIT_PF_RESTART = "pf_restart";

        // 执行shell脚本
        String CMD_CODE_EXEC_SHELL = "exec_shell";
        // 执行sql脚本
        String CMD_CODE_EXEC_SQL = "exec_sql";
        // 执行配置文件
        String CMD_CODE_CONFIG_FILE = "config_file";
        // DNS
        String CMD_CODE_PF_UPDATE = "pf_update";
        // 作业回滚
        String ROLLBACK = "rollback";

        // 卷集挂载操作
        String CMD_CODE_PF_MOUNT= "pf_mount";
        // 卷集卸载操作
        String CMD_CODE_PF_UNMOUNT = "pf_unmount";
        // 卷集删除操作
        String CMD_CODE_PF_CLEAN = "pf_clean";
    }

    /**
     * 外部参数KEY
     */
    interface ExternalParms {
        // 扩容
        String NEW_NODE = "new_node";
        // 缩容
        String DEL_NODE = "del_node";

    }

}
