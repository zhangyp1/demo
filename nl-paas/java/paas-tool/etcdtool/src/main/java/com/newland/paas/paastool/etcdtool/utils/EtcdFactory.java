package com.newland.paas.paastool.etcdtool.utils;

import com.newland.paas.paastool.etcdtool.EtcdConnection;
import com.newland.paas.paastool.etcdtool.ToolDict;
import com.newland.paas.paastool.etcdtool.impl.EtcdConImpl;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: luolifeng
 * Date: 2018-12-24
 * Time: 15:55
 * @author Administrator
 */
public class EtcdFactory {
//    public static final Map<String, EtcdConnection> connectionPools = new HashMap<>();

    /**
     *
     * @param endpoints 访问端点
     * @return 返回
     */
     static EtcdConnection getEtcdConnection(String endpoints){
        String eps=stringAnalyze(endpoints);
        return new EtcdConImpl(eps);
//        synchronized (connectionPools) {
//            if (null == connectionPools.get(eps)) {
//                connectionPools.put(eps, new EtcdConImpl(eps));
//            }
//        }
//        return connectionPools.get(eps);
    }
    private EtcdFactory(){

    }
    public static EtcdConnection getEtcdConnectionWithSSL(String endpoints,String keyFile,String crtFile,String caFile){
        String eps=stringAnalyze(endpoints);
        return  new EtcdConImpl(eps, keyFile, crtFile,caFile);
//        synchronized (connectionPools) {
//            if (null == connectionPools.get(eps)) {
//                connectionPools.put(eps, new EtcdConImpl(eps, privateKey, keyCertChain));
//            }
//        }
//        return connectionPools.get(eps);
    }
    public static EtcdConnection getEtcdConnectionWithSSL(String endpoints,String privateKey,String keyCertChain){
        String eps=stringAnalyze(endpoints);
        return  new EtcdConImpl(eps, privateKey, keyCertChain);
//        synchronized (connectionPools) {
//            if (null == connectionPools.get(eps)) {
//                connectionPools.put(eps, new EtcdConImpl(eps, privateKey, keyCertChain));
//            }
//        }
//        return connectionPools.get(eps);
    }
    /**
     * 去空格
     * @param s 字符串
     * @return 返回
     */
    private static String stringAnalyze(String s){
        if(s==null || s.isEmpty()){
            return null;
        }
        StringBuilder sb=new StringBuilder();
        String[] spt=s.split(ToolDict.SPLIT);
        for(int i=0;i<spt.length;i++){
            sb.append(spt[i].trim());
            if(spt.length-1>i){
                sb.append(ToolDict.SPLIT);
            }
        }
        return sb.toString();
    }

}

