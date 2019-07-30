package com.newland.paas.common.constant;

public class Constant {

    /**
     * 清单属性及清单数据分隔符
     */
    public static final String LIST_SEPARATE = "##";
    /**
     * 清单属性分隔符
     */
    public static final String LIST_SEPARATE_PROPERTY = "#";
    /**
     * 多条清单分隔符
     */
    public static final String LIST_ROW_SEPERATE = "\n";
    /**
     * 稽核日志序号分隔符
     */
    public static final String LIST_SPIT_VERSION = "|";

    public static final String LIST_SPIT_PROPERTY = ",";
    /**
     * 文件头分隔符
     */
    public static final String FILE_ATTRIBUTE_SUFFIX = ",";
    /**
     * 文件名分隔符
     */
    public static final String FILE_NAMING_SUFFIX = ".";
    /**
     * 文件路径分隔符
     */
    public static final String FILE_OBLIQUE_LINE = "/";
    /**
     * 文件名分隔符-下划线
     */
    public static final String FILE_NAMING_UNDERLINE = "_";
    /**
     * ZK中的target父节点路径
     */
    public static final String ZOOKEEPER_TARGET_ROOT_PATH = "/config/type-route/type-target";
    /**
     * ZK中的cluster的父节点路径
     */
    public static final String ZOOKEEPER_CLUSTER_ROOT_PATH = "/config/type-cluster";

    /**
     * 线程运行状态
     * 正常
     */
    public static final int THREADRUNSTATE_NORMAL = 0;
    /**
     * 线程运行状态
     * 停止中
     */
    public static final int THREADRUNSTATE_STOPING = 1;
    /**
     * 线程运行状态
     * 停止
     */
    public static final int THREADRUNSTATE_STOP = 2;

    //框架定义主键前缀
    public static final String KEY_FRAME_PREFIX = "FRAME_";

    /**
     * 组别ID KEY 用于线程上下文KEY
     */
    public static final String THREADMODEL_GROUP_ID = KEY_FRAME_PREFIX + "THREADMODEL_GROUP_ID";

    /**
     * RPC服务端节点
     */
    public static final String RPC_SERVICE_ROOT = "services/";
    /**
     * 服务的参数项开头
     */
    public static final String RPC_SERVICE_FEATURES = "features";
    /**
     * 服务名称的key值
     */
    public static final String RPC_SERVICE_NAME = "serviceName";

    /**
     * 服务状态
     * 可用
     */
    public static final String SERVICE_STATE_ENABLED = "1";
    /**
     * 服务状态
     * 不可用
     */
    public static final String SERVICE_STATE_DISABLED = "0";
    /**
     * 服务状态
     * 切换
     */
    public static final String SERVICE_STATE_SWITCH = "2";

    /**
     * 内容分隔符
     */
    public static final String SEPARATE_1 = "#1#";
    public static final String SEPARATE_2 = "#2#";
    public static final String SEPARATE_3 = "#3#";

    /**
     * 属性类型
     */
    public static final String classType_String = String.class.getName();

    public static final String classType_Integer = Integer.class.getName();

    public static final String classType_int = "int";

    public static final String classType_Long = Long.class.getName();

    public static final String classType_long = "long";

    public static final String classType_Double = Double.class.getName();

    public static final String classType_double = "double";

    /**
     * 文件提取标识
     */
    public static final String FILE_OK = "ok";

    /**
     * 调用方法类型
     * 1、ftp
     * 2、hdfs
     */
    public static final String IMPLEMENT_TYPE_FTP = "ftp";
    public static final String IMPLEMENT_TYPE_HDFS = "hdfs";
}