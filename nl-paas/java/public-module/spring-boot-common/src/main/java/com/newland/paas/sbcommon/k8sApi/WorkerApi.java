package com.newland.paas.sbcommon.k8sApi;

import com.alibaba.fastjson.JSON;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CZX on 2018/8/6.
 */

@Component
public class WorkerApi extends WorkerBaseApi{
    private static final Log logger = LogFactory.getLogger(WorkerApi.class);

    /**
     * 获取节点相关的labels
     * @param url http://10.1.8.8:8080/api/v1/nodes/10.1.8.8，the name is 10.1.8.8
     * @param methodType
     * @param httpEntity
     * @param params
     * @return
     */
    public Map<String,Object> getWorkerlabelByName(String url, HttpMethod methodType, HttpEntity<?> httpEntity,Map<String, Object> params){
        if(null == params)  params = new HashMap<>();
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getNodelabelByName,url:{0},params:{1}", url, JSON.toJSONString(params)));
        //call the parent method·
        Map<String,Object> result = this.getNodesListOrNodeDetail(url, methodType,httpEntity,params);
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getNodelabelByName,result:{0}", result.toString()));
        result = (Map<String, Object>) ((Map<String, Object>) result.get(METADATA)).get(LABELS);
        return result;
    }


    /**
     * 获取节点相关的versions
     * @param url http://10.1.8.8:8080/version
     * @param methodType
     * @param httpEntity
     * @param params
     * @return
     */
    public Map<String,Object> getK8sVersion(String url, HttpMethod methodType, HttpEntity<?> httpEntity,Map<String, Object> params){
        if(null == params)  params = new HashMap<>();
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getK8sVersion,url:{0},params:{1}", url, JSON.toJSONString(params)));
        //call the parent method·
        Map<String,Object> result = this.getNodesListOrNodeDetail(url, methodType,httpEntity,params);
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getK8sVersion,result:{0}", result.toString()));
        return result;
    }

    /**
     * 获取集群PODS数量
     * @param apiServerUrl
     * @param methodType
     * @param httpEntity
     * @param params
     * @return Map<String,Map<String,Object>>
     */
    public Integer getClusterPodCount(String apiServerUrl, HttpMethod methodType, HttpEntity<?> httpEntity,Map<String, Object> params){
        if(null == params){
            throw new IllegalArgumentException("map为空,请确认参数!");
        }
        String hostIp  = (String) params.get("hostIP");
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getResourceUsageByResourceQuota,url:{0},params:{1}", apiServerUrl, JSON.toJSONString(params)));
        // if exist the '//' at the end
        int pos = apiServerUrl.length() - "/".length();
        if(apiServerUrl.indexOf("/",pos) > 0) apiServerUrl  = apiServerUrl.substring(0,apiServerUrl.length()-1);
        String podsUrl =apiServerUrl + "/api/v1/pods";
        //call the parent method  url:http://10.1.8.8:8080/api/v1/pods
        Map<String,Object> allPodsResultMap = this.getNodesListOrNodeDetail(podsUrl, methodType,httpEntity,params);
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getResourceUsageByResourceQuota,result:{0}", allPodsResultMap.toString()));
        List<Map<String,Object>> allPodsResult = (ArrayList<Map<String,Object>>) allPodsResultMap.get(ITEMS);
        List<Integer>  nodePodsCount = new ArrayList<>();
         allPodsResult.stream().forEach(map ->{
            Map<String,Object> specMap = (Map<String, Object>) map.get(SPEC);
            if(CollectionUtils.isEmpty(specMap)) return;
            List<Object> containersList = (List<Object>) specMap.get(CONTAINERS);
            nodePodsCount.add(containersList.size());
        });
       return nodePodsCount.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * 通过resourceQuota来获取域下面的资源使用情况，集群必须创建resourceQuota，否则无法查询
     * (ps:暂时没用到，后期做租户资源查询时候可以使用)
     * @param url eg:http://10.1.8.8:8080/api/v1/resourcequotas
     * @param methodType
     * @param httpEntity
     * @param params
     * @return
     */
    public Map<String,Object> getResourceUsageByResourceQuota(String url, HttpMethod methodType, HttpEntity<?> httpEntity,Map<String, Object> params){
        if(null == params)  params = new HashMap<>();
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getClusterResourceUsage,url:{0},params:{1}", url, JSON.toJSONString(params)));
        //call the parent method·
        Map<String,Object> result = this.getNodesListOrNodeDetail(url, methodType,httpEntity,params);
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getClusterResourceUsage,result:{0}", result.toString()));
        ArrayList<Map<String,Object>> itemsList = (ArrayList<Map<String,Object>>) result.get(ITEMS);
        Optional<Map<String,Object>> dataOptional = itemsList.stream().filter(map -> map.containsKey(STATUS)).findFirst();
        if (dataOptional.isPresent()) {
            return (Map<String, Object>) dataOptional.get().get(STATUS);
        }
        throw new NLUnCheckedException(WORKER_NO_EXIST_CODE, WORKER_NO_EXIST_MSG);
    }


    /**
     * 获取集群资源使用情况
     * @param apiServerUrl
     * @param methodType
     * @param httpEntity
     * @param params
     * @return
     */
    public Map<String,String> getClusterResourceUsage(String apiServerUrl, HttpMethod methodType, HttpEntity<?> httpEntity,Map<String, Object> params){
        if(null == params)  params = new HashMap<>();
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getResourceUsageByResourceQuota,url:{0},params:{1}", apiServerUrl, JSON.toJSONString(params)));
        // if exist the '//' at the end
        if(apiServerUrl.substring(apiServerUrl.length()-1).equals("/") ) apiServerUrl  = apiServerUrl.substring(0,apiServerUrl.length()-1);
        String podsUrl =apiServerUrl + "/api/v1/pods";
        //call the parent method  url:http://10.1.8.8:8080/api/v1/pods
        Map<String,Object> allPodsResult = this.getNodesListOrNodeDetail(podsUrl, methodType,httpEntity,params);
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getResourceUsageByResourceQuota,result:{0}", allPodsResult.toString()));
        //call the parent method·url:http://10.1.8.8:8080/api/v1/nodes
        String nodesUrl = apiServerUrl + "/api/v1/nodes";
        //get pods used resource
        Map<String,Double>  podsResourceUsageData = this.getPodsResourceUsage((ArrayList<Map<String,Object>>) allPodsResult.get(ITEMS));
        List<Map<String,Object>> allNodesResult = (List<Map<String, Object>>) this.getNodesListOrNodeDetail(nodesUrl, methodType,httpEntity,params).get(ITEMS);
        //get node used resource
        Map<String,Double>  nodesResourceUsageData = this.getNodesResource(allNodesResult);
        Map<String,String> data  = new HashMap<>();
        data.put(CPUCORES, String.valueOf((nodesResourceUsageData.get(CPU)/THOUSAND)));
        data.put(CPUUSEDPERCENT,(podsResourceUsageData.get(CPU) / nodesResourceUsageData.get(CPU))*100 + PERCENT);
        data.put(MEMSIZE, String.valueOf(nodesResourceUsageData.get(MEMORY)/MEGABYTE));
        data.put(MEMUSEDPERCENT,(podsResourceUsageData.get(MEMORY) / nodesResourceUsageData.get(MEMORY))*100 + PERCENT);
        return data;
    }

    /**
     * 通过获取到的podsList信息解析出使用的总资源
     * @param allPodsResult
     * @return
     */
    private Map<String,Double> getPodsResourceUsage(ArrayList<Map<String,Object>> allPodsResult){
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getPodsResourceUsage,allPodsResult:{0}",JSON.toJSONString(allPodsResult)));
        List<Double> podsCpuList = new ArrayList<>();
        List<Double> podsMemoryList = new ArrayList<>();
        //to get the json data,key: spec -> containers -> resources -> requests
        allPodsResult.stream().forEach(map ->{
            Map<String,Object> specMap = (Map<String, Object>) map.get(SPEC);
            //check the collention is not null
            if(CollectionUtils.isEmpty(specMap)) return;
            List<Map<String,Object>> containersList = (List<Map<String, Object>>) specMap.get(CONTAINERS);
            if(CollectionUtils.isEmpty(containersList)) return;
            containersList.forEach(containersMap ->{
                Map<String,Object> resourceMap = (Map<String, Object>) containersMap.get(RESOURCES);
                if(CollectionUtils.isEmpty(resourceMap)) return;
                Map<String,String> requestMap = (Map<String, String>) resourceMap.get(REQUESTS);
                if(!CollectionUtils.isEmpty(requestMap)){
                    requestMap.forEach((k,v)->{
                        //TODO 最好后期规范化，就用一种来写yaml文件,就不需要这么多判断条件   ***现在统一以兆为单位计算
                        //eg: 100m=0.1Gi or 1Gi or 1(1 is 1Gi) or 0.1 (0.1 is 100m)
                        if(CPU.equals(k)) {
                            //指明单位是m
                            if (v.lastIndexOf("m") > -1) {
                                //eg: 100m-->100m==0.1Gi==0.1*0.24Mi.,m m.l.
                                Double cpuUsage = Double.parseDouble(v.substring(0, v.length() - 1));
                                podsCpuList.add((cpuUsage/THOUSAND)*MEGABYTE);
                            } else if(v.lastIndexOf("Gi") > -1){
                                podsCpuList.add(Double.parseDouble(v.substring(0, v.length() - 2))* MEGABYTE);
                            }else{//只写了数字,eg：1 is 1Gi or 0.1 is 100m
                                if(isNumeric(v)){
                                    podsCpuList.add((double) (Integer.parseInt(v) * MEGABYTE));
                                }else{
                                    Double cpuUsage = Double.parseDouble(v.substring(0, v.length() - 1));
                                    podsCpuList.add((cpuUsage/THOUSAND)*MEGABYTE);
                                }
                            }
                        }else{//eg: 751619276800m  /1000==byte   /1024==kb    /1024=M    /1024=GI   or  100mi or 1(1 is 1G)
                            if(v.lastIndexOf("Mi")>-1){
                                podsMemoryList.add(Double.valueOf(v.substring(0,v.length()-2)));
                            }else if(v.lastIndexOf("m") > -1){
                                Double memoryUsage = ((Double.parseDouble(v.substring(0, v.length() - 1)))/THOUSAND)/(MEGABYTE*MEGABYTE);
                                podsMemoryList.add(memoryUsage);
                            }else if(v.lastIndexOf("Gi") > -1){
                                Integer intMemory = Integer.parseInt(v.substring(0, v.length() - 2)) * MEGABYTE;
                                podsMemoryList.add(Double.parseDouble(intMemory.toString()));
                            }else{//G的单位，但是只写了数字,eg：1
                                Integer intMemory = Integer.parseInt(v) * MEGABYTE;
                                podsMemoryList.add(Double.parseDouble(intMemory.toString()));
                            }
                        }
                    });
                }
            });
        });
        Map<String,Double>  podsResourceUsageMap = new HashMap<>();
        //sum cpu
        podsResourceUsageMap.put(CPU,podsCpuList.stream().mapToDouble(Double::floatValue).sum());
        //sum memory
        podsResourceUsageMap.put(MEMORY,podsMemoryList.stream().mapToDouble(Double::floatValue).sum());
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getPodsResourceUsage,podsResourceUsageMap:{0}",JSON.toJSONString(podsResourceUsageMap)));
        return podsResourceUsageMap;
    }

    /**
     * 通过获取的nodeList信息解析出获取出所有资源
     * @param allNodesResult
     * @return
     */
    private Map<String,Double> getNodesResource(List<Map<String,Object>> allNodesResult){
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getNodesResource,allNodesResult:{0}",JSON.toJSONString(allNodesResult)));
        List<Double> nodesCpuList = new ArrayList<>();
        List<Double> nodesMemoryList = new ArrayList<>();
        //to get the json data, key: status -> capacity ->
        allNodesResult.forEach(node ->{
            Map<String,Object> statusMap = (Map<String, Object>) node.get(STATUS);
            //check the collention is not null
            if(CollectionUtils.isEmpty(statusMap)) return;
            Map<String,String> capacityMap = (Map<String, String>) statusMap.get(CAPACITY);
            if(CollectionUtils.isEmpty(capacityMap)) return;
            capacityMap.forEach((k,v) ->{
                if(CPU.equals(k)){
                    //eg:32
                    nodesCpuList.add((double) (Integer.parseInt(v)*THOUSAND));
                }else{
                    //eg:131712072Ki
                    nodesMemoryList.add(Double.parseDouble(v.substring(0,v.length()-2))/MEGABYTE);
                }
            });
        });
        Map<String,Double>  nodeResourceMap = new HashMap<>();
        //sum cpu
        nodeResourceMap.put(CPU,nodesCpuList.stream().mapToDouble(Double::floatValue).sum());
        //sum memory
        nodeResourceMap.put(MEMORY,nodesMemoryList.stream().mapToDouble(Double::floatValue).sum());
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getNodesResource,nodeResourceMap:{0}",JSON.toJSONString(nodeResourceMap)));
        return nodeResourceMap;
    }

    /**
     * 正则判断是否为整数
     * @param str
     * @return
     */
    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^\\d+$|-\\d+$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
