package com.newland.paas.common.exception;

/**
 * 错误码定义类
 * 
 * @author czc 编码规则：第三方异常如果有errcode则直接使用，否则按一级分类(1位)+二级分类(2位)+三级分类(自定义)
 */
public class ErrorCode {

	/**
	 * 系统级异常
	 */
	public static final String SYSERROR = "1";

	public static final String SYSERROR_UNKNOW = SYSERROR + "00" + "00000";

	/**
	 * 操作系统异常
	 */
	public static final String SYSERROR_OS = SYSERROR + "01";

	public static final String SYSERROR_OS_DISKFULL = SYSERROR_OS + "00001"; // 系统磁盘满
	public static final String SYSERROR_OS_NOAUTHORITY = SYSERROR_OS + "00002"; // 缺乏系统操作权限
	public static final String SYSERROR_OS_IO = SYSERROR_OS + "00003"; // 系统IO操作异常
	public static final String SYSERROR_OS_SYSSCRIPT = SYSERROR_OS + "00004"; // 执行系统脚本异常

	/**
	 * JVM异常
	 */
	public static final String SYSERROR_JVM = SYSERROR + "02";
	public static final String SYSERROR_JVM_ClASSNOTFIND = SYSERROR_JVM + "00001"; //找不到加载类
	public static final String SYSERROR_JVM_OBJINIT = SYSERROR_JVM + "00002"; //对象实例化失败
	public static final String SYSERROR_JVM_CLASSCAST = SYSERROR_JVM + "00003"; //类型强转失败

	/**
	 * 网络异常
	 */
	public static final String SYSERROR_NETWORK = SYSERROR + "03";
	public static final String SYSERROR_NETWORK_INIT_CONN = SYSERROR_NETWORK + "00001"; // 网络初始化连接错误
	public static final String SYSERROR_NETWORK_TIMEOUT = SYSERROR_NETWORK + "00002"; // 网络通讯超时
	public static final String SYSERROR_NETWORK_LOST_CONN = SYSERROR_NETWORK + "00003"; // 连接丢失
	public static final String SYSERROR_NETWORK_ADDR_NOTFOUND = SYSERROR_NETWORK + "00004"; // 获取系统网络地址失败
	public static final String SYSERROR_NETWORK_HTTP = SYSERROR_NETWORK + "00005"; // HTTP调用异常

	/**
	 * OSGI\Blueprint\KARAF异常(checked)
 	 */
	public static final String SYSERROR_OSGI = SYSERROR + "04";

	/**
	 * 配置异常
	 */
	public static final String SYSERROR_CONFIG = SYSERROR + "10";
	public static final String SYSERROR_CONFIG_BLUEPRINT = SYSERROR_CONFIG + "00001"; // blueprint配置异常
	public static final String SYSERROR_CONFIG_PROPERTIES = SYSERROR_CONFIG + "00002"; // properties配置异常
	public static final String SYSERROR_CONFIG_ZK = SYSERROR_CONFIG + "00003"; // zk配置异常
	public static final String SYSERROR_CONFIG_XML = SYSERROR_CONFIG + "00004"; // xml配置异常

	/**
	 * 指标监控异常
	 */
	public static final String SYSERROR_INDICATORMONITOR = SYSERROR + "11";

	/**
	 * 路由负载异常
	 */
	public static final String SYSERROR_LB = SYSERROR + "12";
	// 无法找到可用的路由节点
	public static final String SYSERROR_LB_CSNNOTFOUND = SYSERROR_LB + "00001";
	// 路由消息分组异常
	public static final String SYSERROR_LB_GROUP_MSG   = SYSERROR_LB + "00002";
	// 消息路由异常
	public static final String SYSERROR_LB_ROUTE       = SYSERROR_LB + "00003";
	// 消息路由表达式执行异常
	public static final String SYSERROR_LB_EXPR        = SYSERROR_LB + "00004";

	/**
	 * MQ异常
	 */
	public static final String SYSERROR_MQ = SYSERROR + "13";

	/**
	 * ZK异常
	 */
	public static final String SYSERROR_ZK = SYSERROR + "14";

	/**
	 * 线程异常
	 */
	public static final String SYSERROR_THREAD = SYSERROR + "15";

	/**
	 * 权限异常
	 */
	public static final String SYSERROR_PERMISSION = SYSERROR + "16";

	/**
	 * 数据库操作异常
	 */
	public static final String SYSERROR_DATABASE = SYSERROR + "17";
	public static final String SYSERROR_DATABASE_COMMON = SYSERROR_DATABASE + "00000";

	/**
	 * JMX 异常
	 */
	public static final String SYSERROR_JMX = SYSERROR + "18";
	public static final String SYSERROR_JMX_RELEASE = SYSERROR_JMX + "00001";	// jmx发布异常

	/**
	 * 文件操作异常
	 */
	public static final String SYSERROR_FILE = SYSERROR + "19";
	public static final String SYSERROR_FILE_PATHNOTFIND = SYSERROR_FILE + "00001";	// 文件目录不存在
	public static final String SYSERROR_FILE_FILENOTFIND = SYSERROR_FILE + "00002";	// 文件不存在
	public static final String SYSERROR_FILE_UNZIP = SYSERROR_FILE + "00003";	// 解压文件异常
	public static final String SYSERROR_FILE_NEWFILE = SYSERROR_FILE + "00004";	// 创建文件异常
	public static final String SYSERROR_FILE_NEWDIR = SYSERROR_FILE + "00005";	// 创建目录异常
	public static final String SYSERROR_FILE_MOVEFILE = SYSERROR_FILE + "00006";	// 移动文件异常
	public static final String SYSERROR_FILE_ZIP = SYSERROR_FILE + "00007";	// 压缩文件异常

	/**
	 * 执行系统脚本失败编码
	 */
	public static final String SYSERROR_SCRIPT = SYSERROR + "20";

	/**
	 * 执行系统脚本失败编码
	 */
	public static final String SYSERROR_SB = SYSERROR + "22";

	// 未传serviceId异常
	public static final String SERVICE_ID_UN_KNOW = SYSERROR + SYSERROR_SB + "00001";
	public static final String SERVICE_ID_UN_KNOW_MSG = "serviceId为空!";
	// 轮询不到节点异常
	public static final String SERVICE_ID_NOT_FIND = SYSERROR + SYSERROR_SB + "00002";
	public static final String SERVICE_ID_NOT_FIND_MSG = "轮询不到节点!";

	/**
	 * 业务级异常
	 */
	public static final String BUSERROR = "2";

	/**
	 * 日期处理异常
	 */
	public static final String BUSERROR_DATE = BUSERROR + "01";
	public static final String BUSERROR_DATE_FORMAT = BUSERROR_DATE + "00001"; // 日期格式化异常
	public static final String BUSERROR_DATE_PARSER = BUSERROR_DATE + "00002"; // 日期转换异常

	/**
	 * JSON处理异常
	 */
	public static final String BUSERROR_JSON = BUSERROR + "02";
	public static final String BUSERROR_JSON_TRANSFORM = BUSERROR_JSON + "00001"; //JSON转换对象异常

	/**
	 * FTP操作异常
	 */
	public static final String BUSERROR_FTP = BUSERROR + "03";
	public static final String BUSERROR_FTP_UP = BUSERROR_FTP + "00001";// 上传异常
	public static final String BUSERROR_FTP_DOWN = BUSERROR_FTP + "00002";// 下载异常
	public static final String BUSERROR_FTP_DEL = BUSERROR_FTP + "00003";// 删除异常
	public static final String BUSERROR_FTP_FILE_NOTEXIST = BUSERROR_FTP + "00003";// 文件不存在

	//业务操作异常从209开始 定义在各自BUNDLE中

    //模型服务异常
    public static final String BUSERROR_MODELSERVICE = BUSERROR + "11";
    public static final String BUSERROR_MODELSERVICE_START = BUSERROR_MODELSERVICE + "00001"; // 启动服务失败
    public static final String BUSERROR_MODELSERVICE_STOP = BUSERROR_MODELSERVICE + "00002"; // 停止服务失败
    public static final String BUSERROR_MODELSERVICE_RESTART = BUSERROR_MODELSERVICE + "00003"; // 重启服务失败
    public static final String BUSERROR_MODELSERVICE_UNINSTALL = BUSERROR_MODELSERVICE + "00004"; // 卸载服务失败
    public static final String BUSERROR_MODELSERVICE_ADD = BUSERROR_MODELSERVICE + "00005"; // 新增服务失败
    public static final String BUSERROR_MODELSERVICE_NOT_EXIST = BUSERROR_MODELSERVICE + "00009"; // 服务不存在

}
