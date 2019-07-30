package com.newland.paas.log;

import com.newland.paas.log.impl.LogLog4j;
import com.newland.paas.log.impl.MdcLog4j;
import com.newland.paas.log.startlog.StartLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

/**
 * LOG工厂类 支持log4j2.xml存放在classes/config目录下或者直接放在classes下
 * 
 * @author Administrator
 */
@SuppressWarnings("rawtypes")
public class LogFactory {

    private static final String STATIC_LOGGERCONFIG_PATH = "config/log4j2-";
    private static final String STATIC_LOGGER_PATH = "log4j2-";
    private static final String FILE_TYPE = ".xml";
    private static final String DEFAULT_ENV = "default";
    private static final String DEFAULT_CONTAINER_ENV = "container";

    private static boolean isInit = false;

    private static void initConfig() {
        if (isInit)
            return;
        initLoad();
    }

    private synchronized static void initLoad() {
        if (isInit)
            return;
        URL url = null;
        try {
            // 指定配置文件（dev、test、prod）
            String env = System.getProperty("spring.profiles.active");
            String staticLoggerConfigPath = null;
            String staticLoggerPath = null;
            if (!StringUtils.isEmpty(env)) {
                System.out.println("指定配置文件:" + env);
                staticLoggerConfigPath = STATIC_LOGGERCONFIG_PATH + env + FILE_TYPE;
                staticLoggerPath = STATIC_LOGGER_PATH + env + FILE_TYPE;
                url = getUrl(staticLoggerConfigPath, staticLoggerPath);
            }
            // 不存在指定的配置文件
            if (url == null) {
                // 判断当前是否在kubernates中启动。是：读取-container文件，否：读取-default文件
                Map<String, String> envMap = System.getenv();
                String containerEnv = envMap.get("SPRING_PROFILES_ACTIVE");
                if (!StringUtils.isEmpty(containerEnv)) {
                    System.out.println("kubernates中启动:" + DEFAULT_CONTAINER_ENV);
                    staticLoggerConfigPath = STATIC_LOGGERCONFIG_PATH + DEFAULT_CONTAINER_ENV + FILE_TYPE;
                    staticLoggerPath = STATIC_LOGGER_PATH + DEFAULT_CONTAINER_ENV + FILE_TYPE;
                } else {
                    System.out.println("物理主机启动:" + DEFAULT_ENV);
                    staticLoggerConfigPath = STATIC_LOGGERCONFIG_PATH + DEFAULT_ENV + FILE_TYPE;
                    staticLoggerPath = STATIC_LOGGER_PATH + DEFAULT_ENV + FILE_TYPE;
                }
                url = getUrl(staticLoggerConfigPath, staticLoggerPath);
            }

            if (url != null) {
                loadConfigFile(url.toURI());
                isInit = true;
            } else {
                System.out.println("配置文件没有读取到");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取日志配置文件
     * 
     * @param url
     * @return
     * @throws IOException
     * @throws MalformedURLException
     */
    private static URL getUrl(String staticLoggerConfigPath, String staticLoggerPath)
        throws IOException, MalformedURLException {
        URL url = null;
        Enumeration paths;
        System.out.println("log4j2 path：加载配置文件：" + staticLoggerConfigPath);
        System.out.println("log4j2 path：加载配置文件：" + staticLoggerPath);
        paths = ClassLoader.getSystemResources(staticLoggerPath);
        if (paths.hasMoreElements()) {
            url = (URL)paths.nextElement();
        } else {
            paths = ClassLoader.getSystemResources(staticLoggerConfigPath);
            if (paths.hasMoreElements()) {
                url = (URL)paths.nextElement();
            }
        }
        System.out.println("log4j2 path：ClassLoader.getSystemResources=" + url);
        // 没有读到配置路径文件，读classes下资源文件
        if (url == null) {
            url = LogFactory.class.getClassLoader().getResource(staticLoggerPath);
            if (url == null) {
                url = LogFactory.class.getClassLoader().getResource("/" + staticLoggerPath);
            }
        }
        System.out.println("log4j2 path：LogFactory.class.getClassLoader().getResource=" + url);

        // 读取根目录下配置文件，读取到则直接替换
        String appPath = System.getProperty("user.dir");
        File f = new File(appPath + "/" + staticLoggerConfigPath);
        if (f.exists()) {
            url = new URL("file://" + appPath + "/" + staticLoggerConfigPath);
        } else {
            f = new File(appPath + "/" + staticLoggerPath);
            if (f.exists()) {
                url = new URL("file://" + appPath + "/" + staticLoggerPath);
            }
        }
        System.out.println("log4j2 path：url=" + url);
        return url;
    }

    /**
     * 加载配置文件
     *
     * @param uri
     */
    public static void loadConfigFile(URI uri) {
        System.out.println("03 log4j2 loadConfigFile uri=" + uri);
        // 加载配置文件
        // LoggerContext context = (LoggerContext) LogManager.getContext(LogLog4j.class.getClassLoader().getParent(),
        // false, uri);
        // context.reconfigure();
        LoggerContext context = (LoggerContext)LogManager.getContext(false);
        context.setConfigLocation(uri);
        context.reconfigure();
    }

    /**
     * 获取日志操作实例
     *
     * @return
     */
    public static Log getRootLogger() {
        initConfig();
        return LogLog4j.getRootLogger();
    }

    /**
     * 获取日志操作实例
     *
     * @param filterPage 日志打印位置过滤名 , 过滤codeline打印信息
     * @return
     */
    public static Log getRootLogger(String filterPage) {
        initConfig();
        return LogLog4j.getRootLogger(filterPage);
    }

    /**
     * 获取日志操作实例
     *
     * @param clazz 通过类定义
     * @return
     */
    public static Log getLogger(Class clazz) {
        initConfig();
        return LogLog4j.getLogger(clazz);
    }

    /**
     * 获取日志操作实例
     *
     * @param clazz 通过类定义
     * @param filterPage 日志打印位置过滤名 , 过滤codeline打印信息
     * @return
     */
    public static Log getLogger(Class clazz, String filterPage) {
        initConfig();
        return LogLog4j.getLogger(clazz, filterPage);
    }

    /**
     * 获取日志操作实例
     *
     * @param name
     * @return
     */
    public static Log getLogger(String name) {
        initConfig();
        return LogLog4j.getLogger(name);
    }

    /**
     * @param name
     * @param filterPage
     * @return
     */
    public static Log getLogger(String name, String filterPage) {
        initConfig();
        return LogLog4j.getLogger(name, filterPage);
    }

    /**
     * @return
     */
    public static Mdc getMdc() {
        return new MdcLog4j();
    }

    /**
     * 打开启动文件日志写入
     */
    public static void openStartLog() {
        StartLog.setOpen(true);
    }

    /**
     * 关闭启动文件日志写入
     */
    public static void closeStartLog() {
        StartLog.setOpen(false);
    }

    /**
     * 获取启动日志文件名
     */
    public static String getStartLogFile() {
        return StartLog.getLogFile();
    }
}
