package com.newland.paas.common.util;

import com.newland.paas.common.constant.Constant;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;

/**
 * @author chenshen
 * @Description com.newland.paas.common.util.PropertiesUtil
 * @Date 2017/10/24
 */
public class PropertiesUtil {

    private static Log logger = LogFactory.getLogger(PropertiesUtil.class);

    /**
     * 数据以key-value形式封装成Map
     */
    public static String MODE_KEY_VALUE = "mode_key_value";

    /**
     * 数据以value-key形式封装成Map
     */
    public static String MODE_VALUE_KEY = "mode_value_key";



    /**
     * 将数据放入map
     * @param defaultConfigFile
     * @param configMap
     */
    public static void load(File defaultConfigFile, Map<String, String> configMap, String mode) throws IOException {
        InputStream in = null;
        try {
            if(configMap==null){
                configMap = new HashMap<>();
            }else{
                configMap.clear();
            }
            in = new FileInputStream(defaultConfigFile);
            Properties props = new Properties();
            props.load(new InputStreamReader(in, "utf-8"));//解决中文乱码
            Enumeration<?> enu = props.propertyNames();
            while (enu.hasMoreElements()) {
                String key = (String)enu.nextElement();
                String value = (String) props.get(key);
                if("mode_value_key".equals(mode)){
                    if(StringUtils.isNotEmpty(value)&&value.trim().length()>0){
                        configMap.put(value, key);
                    }
                }else{
                    configMap.put(key, value);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally{
            if (in != null){
                try{
                    in.close();
                } catch (Exception ex){
                }
                in = null;
            }
        }
    }

    /**
     * 将数据放入map
     * @param defaultConfigFileName
     * @param configMap
     */
    public static void load(String defaultConfigFileName, Map<String, String> configMap, String mode) throws IOException {
//        logger.info(LogProperty.LOGTYPE_CALL,"load Properties["+mode+"]:" + defaultConfigFileName);
        InputStream in = null;
        try {
            if(configMap==null){
                configMap = new HashMap<>();
            }else{
                configMap.clear();
            }
            if(StringUtils.isEmpty(defaultConfigFileName)){
                logger.info(LogProperty.LOGTYPE_CALL,"Properties not exist:" + defaultConfigFileName);
                return;
            }
            in = new FileInputStream(defaultConfigFileName);
            Properties props = new Properties();
            props.load(new InputStreamReader(in, "utf-8"));//解决中文乱码
            Enumeration<?> enu = props.propertyNames();
            while (enu.hasMoreElements()) {
                String key = (String)enu.nextElement();
                String value = (String) props.get(key);
                if("mode_value_key".equals(mode)){
                    if(StringUtils.isNotEmpty(value)&&value.trim().length()>0){
                        configMap.put(value, key);
                    }
                }else{
                    configMap.put(key, value);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally{
            if (in != null){
                try{
                    in.close();
                } catch (Exception ex){
                }
                in = null;
            }
        }
    }

    /**
     * 写文件
     * @param defaultConfigFileName
     * @param map
     * @throws IOException
     */
    public static void write(String defaultConfigFileName, Map<String,String> map) throws IOException {
        logger.info(LogProperty.LOGTYPE_CALL,"write Properties:" + defaultConfigFileName);
        OutputStream output = null;
        try {
            if(StringUtils.isEmpty(defaultConfigFileName)){
                logger.info(LogProperty.LOGTYPE_CALL,"Properties not exist:" + defaultConfigFileName);
                return;
            }
            output = new FileOutputStream(defaultConfigFileName);
            Properties props = new Properties();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                props.setProperty(entry.getKey(), entry.getValue());
            }
            props.store(output, "clock file");// 保存键值对到文件中
        } catch (IOException ex) {
            throw ex;
        } finally{
            if (output != null){
                try{
                    output.close();
                } catch (Exception ex){
                }
                output = null;
            }
        }
    }

    public static void main(String[] args) throws Exception{

        FileLock lock = null;
        FileChannel channel = null;
        String lockFilePath = "D:/tmp/tensorflow/dataSet/dataSet/newone/lock.properties";
        File lockFile = new File(lockFilePath);
        RandomAccessFile raf = new RandomAccessFile(lockFile,"rw");
        channel = raf.getChannel();
        lock = channel.tryLock(0L, Long.MAX_VALUE, true);//获取文件锁
        System.out.println("lock:"+lock);
//        if (lock.isValid()) {
//            byte[] buf = new byte[1024];
//            String fag = "";
//            while((raf.read(buf))!=-1){
//                fag = new String(buf);
//                buf = new byte[1024];
//            }
//            System.out.println(fag+"!!!");
//            if ("upTag=ok".equals(fag)){
//                System.out.println("#######");
//            }
//        }
        Map<String,String> lockMap = new HashMap<>();//锁文件内容信息
        load(lockFile,lockMap,MODE_KEY_VALUE);
        System.out.println(lockMap);
    }
}
