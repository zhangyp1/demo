package com.newland.paas.log.startlog;


import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 启动日志输出线程
 * @author Administrator
 *
 */
public class StartLogRunnable implements Runnable {
    private static final Log log = LogFactory.getLogger(StartLogRunnable.class);

    private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);

    private boolean isStop = false;

    private String fileName;

    public StartLogRunnable(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        StringBuffer sb = new StringBuffer();
        while (true) {
            try {
                sb.setLength(0);
                for (int i = 0; i < 100; i++) {
                    String str = queue.poll();
                    if (str == null) {
                        break;
                    }
                    sb.append(str + "\r\n");
                }
                if (sb.length() > 0) {
                    buildFile(fileName, sb.toString(), true);
                }
                if (queue.size() == 0) {
                    if (isStop)
                        break;
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                log.error(LogProperty.LOGTYPE_SYS, "", e, "out start log error");
            }
        }
    }

    public void putStr(String str) {
        queue.offer(str);
    }

    public void writeStop() {
        isStop = true;
    }

    public static void buildFile(String fileName, String s, boolean append) {
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(fileName, append);
            bw = new BufferedWriter(fw);
            bw.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
