package com.newland.paas.common.constant;

/**
 * Copyright (c) 2014, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName: IConfigCenter
 * @Description: 配置中心相关常量
 * @Funtion List:
 * 
 * @author birdtsai
 * @version
 * @Date 2014年6月19日 下午3:09:13
 * 
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public interface IConfigCenter {
    /*
     * 配置信息分隔符
     */
    public static final String SPIT_CHAR = ",";

    public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";

    /*
     * 配置文件相对路径名(应用根目录)
     */
    public static final String CONFIG_FILE_PATH = "config";
    /*
     * 默认临时(状态)输出文件相对路径名
     */
    public static final String TEMP_FILE_PATH   = "temp";
    /*
     * metrics指标输出相对路径名
     */
    public static final String METRICS_FILE_PATH= "metrics";
    /*
     * 配置文件名
     */
    public static final String CONFIG_FILE_NAME = "rtc.properties";

    public static final String HOST_NAME = "hostname";

    public static final String JMX_DOMAIN_NAME = "rmc";

    public static final String JMX_OBJECT_NAME = "rmcMBean";

    // 配置文件路径
    public static final String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";

    public static final String ZK_HOST = "zkHost";

    public static final String ZK_TIMEOUT = "zkTimeOut";

    public static final String JDBC_DRIVER = "driverClass";

    public static final String JDBC_URL = "jdbcUrl";

    public static final String JDBC_USERNAME = "user";

    public static final String JDBC_PASSWORD = "password";

    // 需要判断run_state实体是否启动
    public static final String[] NEED_RUN_STATE = {"type-bcn", "type-chday",
            "type-csn", "type-dmc", "type-ocn", "type-ocgf", "type-rcn",
            "type-tsn"};

    // run_state定时任务在5秒后执行
    public static final long TIMER_DELAY = 1000;

    // run_state定时任务每隔3秒执行一次
    public static final long TIMER_PERIOD = 1000;

    // command超时时间 10分钟
    public static final long TIME_OUT = 10 * 60 * 1000;

    /*
     * 是否调试状态
     */
    public final static String DEBUG_VALUE = "true";

    public final static String UNDEBUG_VALUE = "false";

    public enum DebugState {
        DEBUG(DEBUG_VALUE), UNDEBUG(UNDEBUG_VALUE);

        private final String strValue;

        DebugState(String value) {
            this.strValue = value;
        }

        public String getStrValue() {
            return strValue;
        }

        public static DebugState fromStr(String value) {
            switch (value)
            {
                case DEBUG_VALUE:
                    return DebugState.DEBUG;
                case UNDEBUG_VALUE:
                    return DebugState.UNDEBUG;
                default:
                    throw new RuntimeException("无效的debug状态值");
            }
        }
    }
    
    /*
     * ZK根结点是否锁定状态
     */
    public final static String ZK_LOCK_VALUE = "LOCK";

    public final static String ZK_FREE_VALUE = "FREE";

    public enum ZkState {
        ZK_LOCK(ZK_LOCK_VALUE), ZK_FREE(ZK_FREE_VALUE);

        private final String strValue;

        ZkState(String value) {
            this.strValue = value;
        }

        public String getStrValue() {
            return strValue;
        }

        public static ZkState fromStr(String value) {
            switch (value)
            {
                case ZK_LOCK_VALUE:
                    return ZkState.ZK_LOCK;
                case ZK_FREE_VALUE:
                    return ZkState.ZK_FREE;
                default:
                    throw new RuntimeException("无效的ZK状态值");
            }
        }
    }

    /*
     * ZK节点同步状态，标识是否可以更新
     */
    public final static String ZK_SYNC_LOCK_VALUE = "LOCK";

    public final static String ZK_SYNC_FREE_VALUE = "FREE";

    public enum ZkNodeSyncState {
        ZK_LOCK(ZK_SYNC_LOCK_VALUE), ZK_FREE(ZK_SYNC_FREE_VALUE);

        private final String strValue;

        ZkNodeSyncState(String value) {
            this.strValue = value;
        }

        public String getStrValue() {
            return strValue;
        }

        public static ZkNodeSyncState fromStr(String value) {
            switch (value)
            {
                case ZK_SYNC_LOCK_VALUE:
                    return ZkNodeSyncState.ZK_LOCK;
                case ZK_SYNC_FREE_VALUE:
                    return ZkNodeSyncState.ZK_FREE;
                default:
                    throw new RuntimeException("无效的ZK同步状态值");
            }
        }
    }

    /*
     * ZK实体节点的子节点
     */
    public final static String ZK_SYNC_FLAG_VALUE = "sync_flag";

    public final static String ZK_COMMAND_VALUE = "command";

    public final static String ZK_RESULT_VALUE = "result";

    public final static String ZK_RUNSTATE_VALUE = "run_state";

    public final static String ZK_CONFIG_VALUE = "config";

    public final static String ZK_TEMP_NODE_VALUE = "tmp_node";

    public enum ZkEntityChild {
        SYNC_FLAG(ZK_SYNC_FLAG_VALUE), COMMAND(ZK_COMMAND_VALUE), RESULT(
                ZK_RESULT_VALUE), RUNSTATE(ZK_RUNSTATE_VALUE), CONFIG(
                ZK_CONFIG_VALUE), TEMP_NODE(ZK_TEMP_NODE_VALUE);

        private final String strValue;

        ZkEntityChild(String value) {
            this.strValue = value;
        }

        public String getStrValue() {
            return strValue;
        }

        public static ZkEntityChild fromStr(String value) {
            switch (value)
            {
                case ZK_SYNC_FLAG_VALUE:
                    return ZkEntityChild.SYNC_FLAG;
                case ZK_COMMAND_VALUE:
                    return ZkEntityChild.COMMAND;
                case ZK_RESULT_VALUE:
                    return ZkEntityChild.RESULT;
                case ZK_RUNSTATE_VALUE:
                    return ZkEntityChild.RUNSTATE;
                case ZK_CONFIG_VALUE:
                    return ZkEntityChild.CONFIG;
                case ZK_TEMP_NODE_VALUE:
                    return ZkEntityChild.TEMP_NODE;
                default:
                    throw new RuntimeException("无效的ZK实体子节点名称");
            }
        }
    }

    /*
     * ZK节点属性中的关键属性名
     */
    public final static String CLUSTER_PROP_NAME = "cluster";

    /*
     * 业务配置
     */
    public static final String BIZ_PROP_NAME = "bizCfg";

    /*
     * 平台配置
     */
    public static final String PT_PROP_NAME = "ptCfg";

    public final static String TYPE_PROP_NAME = "type";

    public final static String VALUE_PROP_NAME = "value";

    public final static String NODE_TYPE_PROP_NAME = "node_type";

    public final static String STATE_PROP_NAME = "state";

    public final static String COMMANDID_PROP_NAME = "commandId";

    public final static String PARAM_PROP_NAME = "param";

    public final static String NODE_RUN_STATE_NAME = "node_run_state";
    
    public final static String DEBUG_STATE_NAME = "debug";
   
    public final static String ID_PROP_NAME   = "id";
    
    public final static String IP_PROP_NAME   = "ip";
    
    public final static String PORT_PROP_NAME = "port";
    /*
     * 节点的网卡配置信息
     */
    public final static String NICDEV_PROP_NAME = "nicdev";
    
    public enum ZkPropKey {
        CLUSTER(CLUSTER_PROP_NAME), PT(PT_PROP_NAME), BIZ(BIZ_PROP_NAME), TYPE(
                TYPE_PROP_NAME), VALUE(VALUE_PROP_NAME), NODE_TYPE(
                NODE_TYPE_PROP_NAME), STATE(STATE_PROP_NAME), COMMANDID(
                COMMANDID_PROP_NAME), PARAM(PARAM_PROP_NAME), ID(ID_PROP_NAME),
                IP(IP_PROP_NAME),PORT(PORT_PROP_NAME),NICDEV(NICDEV_PROP_NAME),NODE_RUN_STATE(
                NODE_RUN_STATE_NAME), DEBUG_STATE(DEBUG_STATE_NAME);

        private final String strValue;

        ZkPropKey(String value) {
            this.strValue = value;
        }

        public String getStrValue() {
            return strValue;
        }

        public static ZkPropKey fromStr(String value) {
            switch (value)
            {
                case CLUSTER_PROP_NAME:
                    return ZkPropKey.CLUSTER;
                case TYPE_PROP_NAME:
                    return ZkPropKey.TYPE;
                case VALUE_PROP_NAME:
                    return ZkPropKey.VALUE;
                case NODE_TYPE_PROP_NAME:
                    return ZkPropKey.NODE_TYPE;
                case STATE_PROP_NAME:
                    return ZkPropKey.STATE;
                case COMMANDID_PROP_NAME:
                    return ZkPropKey.COMMANDID;
                case PARAM_PROP_NAME:
                    return ZkPropKey.PARAM;
                case NODE_RUN_STATE_NAME:
                    return ZkPropKey.NODE_RUN_STATE;
                case BIZ_PROP_NAME:
                    return ZkPropKey.BIZ;
                case PT_PROP_NAME:
                    return ZkPropKey.PT;
                case ID_PROP_NAME:
                    return ZkPropKey.ID;
                case IP_PROP_NAME:
                    return ZkPropKey.IP;
                case PORT_PROP_NAME:
                    return ZkPropKey.PORT;
                case NICDEV_PROP_NAME:
                    return ZkPropKey.NICDEV;
                default:
                    throw new RuntimeException("无效的ZK属性名");
            }
        }
    }

    // 1：实体操作增加；
    public final static int OPER_TYPE_ADD = 1;

    // 2：实体操作删除；
    public final static int OPER_TYPE_DELETE = 2;

    // 3：实体操作修改
    public final static int OPER_TYPE_UPDATE = 3;

    public enum ZkEntityOperType {
        ADD(OPER_TYPE_ADD), DELETE(OPER_TYPE_DELETE), UPDATE(OPER_TYPE_UPDATE);

        private final int value;

        ZkEntityOperType(int value) {
            this.value = value;
        }

        public int getIntValue() {
            return value;
        }

        public static ZkEntityOperType fromStr(int value) {
            switch (value)
            {
                case OPER_TYPE_ADD:
                    return ZkEntityOperType.ADD;
                case OPER_TYPE_DELETE:
                    return ZkEntityOperType.DELETE;
                case OPER_TYPE_UPDATE:
                    return ZkEntityOperType.UPDATE;
                default:
                    throw new RuntimeException("无效的实体操作");
            }
        }
    }

    // 操作状态（1：开启；2：结束）
    public static final int OPER_STAT_OPEN = 1;

    public static final int OPER_STAT_CLOSE = 2;

    public enum ZkEntityOperStatType {
        OPEN(OPER_STAT_OPEN), CLOSE(OPER_STAT_CLOSE);

        private final int value;

        ZkEntityOperStatType(int value) {
            this.value = value;
        }

        public int getIntValue() {
            return value;
        }

        public static ZkEntityOperStatType fromStr(int value) {
            switch (value)
            {
                case OPER_STAT_OPEN:
                    return ZkEntityOperStatType.OPEN;
                case OPER_STAT_CLOSE:
                    return ZkEntityOperStatType.CLOSE;
                default:
                    throw new RuntimeException("无效的实体操作状态");
            }
        }
    }

    // 同步状态（1：同步完成；0：同步未完成；）
    public static final int SYNC_STAT_COMPLETE = 1;

    public static final int SYNC_STAT_UNCOMPLETE = 0;

    public enum ZkEntitySyncStatType {
        UNCOMPLETE(SYNC_STAT_UNCOMPLETE), COMPLETE(SYNC_STAT_COMPLETE);

        private final int value;

        ZkEntitySyncStatType(int value) {
            this.value = value;
        }

        public int getIntValue() {
            return value;
        }

        public static ZkEntitySyncStatType fromStr(int value) {
            switch (value)
            {
                case SYNC_STAT_UNCOMPLETE:
                    return ZkEntitySyncStatType.UNCOMPLETE;
                case SYNC_STAT_COMPLETE:
                    return ZkEntitySyncStatType.COMPLETE;
                default:
                    throw new RuntimeException("无效的实体同步状态");
            }
        }
    }

    // 节点类型entity
    public static final String NODE_ENTITY = "1";

    // 节点类型entity_type
    public static final String NODE_ENTITY_TYPE = "2";

    public enum ZkEntityType {
        ENTITY(NODE_ENTITY), ENTITY_TYPE(NODE_ENTITY_TYPE);

        private final String value;

        ZkEntityType(String value) {
            this.value = value;
        }

        public String getStrValue() {
            return value;
        }

        public static ZkEntityType fromStr(String value) {
            switch (value)
            {
                case NODE_ENTITY:
                    return ZkEntityType.ENTITY;
                case NODE_ENTITY_TYPE:
                    return ZkEntityType.ENTITY_TYPE;
                default:
                    throw new RuntimeException("无效的节点类型");
            }
        }
    }

    // state属性状态ok
    public static final String STATE_OK = "ok";

    // state属性状态running
    public static final String STATE_RUNNING = "running";

    // state属性状态dead
    public static final String STATE_DEAD = "dead";

    // type属性实时更新
    public static final String STATE_TIMING = "1";

    // type属性重启更新
    public static final String STATE_RESTART = "2";

    // bundle存活
    public static final String STATE_ONLINE = "online";

    // bundle离线
    public static final String STATE_OFFLINE = "offline";

    public enum ZkNodePropState {
        OK(STATE_OK), RUNNING(STATE_RUNNING), DEAD(STATE_DEAD), TIMING(
                STATE_TIMING), RESTART(STATE_RESTART), ONLINE(STATE_ONLINE), OFFLINE(
                STATE_OFFLINE);

        private final String value;

        ZkNodePropState(String value) {
            this.value = value;
        }

        public String getStrValue() {
            return value;
        }

        public static ZkNodePropState fromStr(String value) {
            switch (value)
            {
                case STATE_OK:
                    return ZkNodePropState.OK;
                case STATE_RUNNING:
                    return ZkNodePropState.RUNNING;
                case STATE_DEAD:
                    return ZkNodePropState.DEAD;
                case STATE_TIMING:
                    return ZkNodePropState.TIMING;
                case STATE_RESTART:
                    return ZkNodePropState.RESTART;
                case STATE_ONLINE:
                    return ZkNodePropState.ONLINE;
                case STATE_OFFLINE:
                    return ZkNodePropState.OFFLINE;
                default:
                    throw new RuntimeException("无效的属性状态");
            }
        }
    }

    public static final String FILE_OUT = "out";

    public static final String FILE_ERR = "err";

    public static final String FILE_LOCK = "lock";

    public static final String FILE_EXT = "ext";

    public static final String FILE_REP = "rep";

    public static final String FILE_INVALID = "invalid";

    public static final String FILE_ORG = "org";

    public static final String FILE_COMPELETE = "compelete";

    public enum FileDir {
        OUT(FILE_OUT), ERR(FILE_ERR), LOCK(FILE_LOCK), EXT(FILE_EXT), REP(
                FILE_REP), INVALID(FILE_INVALID), ORG(FILE_ORG), COMPELETE(
                FILE_COMPELETE);

        private String menu;

        FileDir(String menu) {
            this.menu = menu;
        }

        public String getStrValue() {
            return menu;
        }

        public static FileDir formStr(String menu) {
            switch (menu)
            {
                case FILE_OUT:
                    return FileDir.OUT;
                case FILE_ERR:
                    return FileDir.ERR;
                case FILE_EXT:
                    return FileDir.EXT;
                case FILE_INVALID:
                    return FileDir.INVALID;
                case FILE_ORG:
                    return FileDir.ORG;
                case FILE_REP:
                    return FileDir.REP;
                case FILE_LOCK:
                    return FileDir.LOCK;
                case FILE_COMPELETE:
                    return FileDir.COMPELETE;
                default:
                    throw new RuntimeException("无效的文件目录");
            }
        }
    }
}
