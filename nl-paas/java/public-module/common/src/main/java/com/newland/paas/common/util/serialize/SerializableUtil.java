package com.newland.paas.common.util.serialize;

import java.io.*;

/**
 * Copyright (c) 2016, NEWLAND , LTD All Rights Reserved.
 *
 * @author zhuyl
 * @ClassName: SerializableUtil
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 * @Date 2016年03月14日        15:58
 * @History: // 历史修改记录
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class SerializableUtil {

    public static byte[] serialize(Object obj) {
        if(obj==null){
            throw new IllegalArgumentException("serialize obj is null");
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream out = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(10240);
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T deserialize(byte[] byteMsg){
        if(byteMsg==null || byteMsg.length==0){
            throw new IllegalArgumentException("deserialize byte is null or size is 0");
        }
        ByteArrayInputStream byteArrayInputStream=null;
        ObjectInputStream in=null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(byteMsg);
            in=new ObjectInputStream(byteArrayInputStream);
            T obj=(T)in.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(byteArrayInputStream!=null){
                    byteArrayInputStream.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
