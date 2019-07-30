package com.newland.paas.common.util.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2016, NEWLAND , LTD All Rights Reserved.
 *
 * @author zhuyl
 * @ClassName: FastjsonUtil
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 * @Date 2016年03月17日        14:31
 * @History: // 历史修改记录
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class FastjsonUtil extends JSON{

    public static <T> String serializeString(T object) {
        return JSON.toJSONString(object);
    }

    public static <T> T deserializeString(String string, Class<T> clz) {
        return JSON.parseObject(string, clz);
    }

    public static <T> byte[] serialize(T obj){
        byte[] msg = JSON.toJSONBytes(obj);
        return msg;
    }

    public static <T> T deserialize(byte[] bb,Class<?> clazz){
        T obj = (T)JSON.parseObject(bb, clazz);
        return obj;
    }

    public static <T> T deserialize(byte[] bb,Class<?> clazz,Class<?> targetClazz){
        T obj = deserialize(bb, clazz);
        if(obj instanceof String){
            return obj;
        }
        //Map<String, Map<Integer, ICacheModel>> datas
        //Map<String, Map<String, int[]>> pkDatas
        //Map<String, List<Integer>> indexDatas
        if(obj instanceof Map){
            Map<String,JSONObject> map = (Map<String,JSONObject>)obj;
            for (Map.Entry<String, JSONObject> entry : map.entrySet()) {
//                cacheName = entry.getKey();
                JSONObject jsonObject = entry.getValue();
                if(jsonObject==null){
                    continue;
                }
                if(jsonObject instanceof Map){
                    Map<String,Object> map2 = (Map)jsonObject;
                    for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
                        JSONObject value = entry.getValue();
                        System.out.println(value);
                        for (Map.Entry<String, Object> entry3 : value.entrySet()) {
                            if(entry3 instanceof Map){
                                String key3 = entry3.getKey();//bean 属性名称
                                Object value3 = entry3.getValue();//bean 属性值
                                try {
                                    Object ins = targetClazz.newInstance();
                                    Field[] fields = ins.getClass().getDeclaredFields();
                                    for (Field field:fields){
                                        if(field.getName().equalsIgnoreCase(key3)){
                                            field.setAccessible(true);
                                            field.set(ins,value3);
                                        }
                                    }
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                }else if(jsonObject instanceof List){
                    System.out.println("bb");
                }
            }

        }

        return obj;
    }

}
