package com.newland.paas.log.startlog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 启动日志 写独立文件
 * @author Administrator
 *
 */
public class StartLog {

    private static boolean isOpen = false;

    private static String logFile = System.getProperty("user.dir") + "/log-sys-start.log";

    private static StartLogRunnable startLogRunnable;

    static{
        StartLogRunnable.buildFile(logFile, "", false);
        startLogRunnable = new StartLogRunnable(logFile);
        Thread t = new Thread(startLogRunnable, "log_thread_startlog");
        t.start();
    }

    /**
     * 打印日志
     * 输出格式：time{yyyy-MM-dd,HH:mm:ss.SSS} | nanoTime | class.method | content
     * @param str
     */
    public static void print(String str, String filterPage) {
        if (isOpen) {
            StackTraceElement[] stacks = new Throwable().getStackTrace();
            Map<String, String> filterMap = new HashMap<String, String>();
            if (filterPage != null)
                filterMap.put(filterPage, "");
            filterMap.put("com.newland.paas.log.impl.LogLog4j", "");
            filterMap.put("com.newland.paas.log.startlog.StartLog", "");
            for (int i = 0; i < stacks.length; i++) {
                String className = stacks[i].getClassName();
                if (filterMap.get(className) != null) {
                    continue;
                }
                StringBuffer sb = new StringBuffer();
                sb.setLength(0);
                //时间
                sb.append(getTime() + " | ");
                sb.append(System.nanoTime() + " | ");
                //线程名
                sb.append(Thread.currentThread().getName() + " | ");
                //代码
                sb.append(stacks[i].getClassName())
                        .append(".")
                        .append(stacks[i].getMethodName())
                        .append("(")
                        .append(stacks[i].getFileName())
                        .append(":")
                        .append(stacks[i].getLineNumber())
                        .append(")");
                sb.append(" | " + str);
                startLogRunnable.putStr(sb.toString());
                break;
            }
        }
    }

    private static String getTime() {
        Date date = new Date();
        String value = (new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss.SSS")).format(date);
        return value;
    }

    public static synchronized void setOpen(boolean isOpen) {
        StartLog.isOpen = isOpen;
        if (isOpen) {
            //开启启动日志
            if (startLogRunnable == null) {
                startLogRunnable = new StartLogRunnable(logFile);
                Thread t = new Thread(startLogRunnable, "log_thread_startlog");
                t.start();
            }
        } else {
            //关闭启动日志
            if (startLogRunnable != null) {
                startLogRunnable.writeStop();
                startLogRunnable = null;
            }
        }
    }

    public static String getLogFile() {
        return logFile;
    }

    public static boolean isOpen() {
        return isOpen;
    }
}
