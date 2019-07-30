package com.newland.paas.sbcommon.k8sApi;

import com.alibaba.fastjson.JSON;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by CZX on 2018/8/6.
 */
public abstract class WorkerBaseApi {

    private static final Log logger = LogFactory.getLogger(WorkerBaseApi.class);

    protected static final String SERVER_REQUEST_ERROR_CODE = "12200001";
    protected static final String SERVER_REQUEST_ERROR_MSG = "服务请求失败!";
    protected static final String REQUEST_ERROR_CODE = "12200002";
    protected static final String REQUEST_ERROR_MSG = "请求错误!";
    protected static final String SERVER_ERROR_CODE = "12200003";
    protected static final String SERVER_ERROR_MSG = "服务器错误!";
    protected static final String WORKER_NO_EXIST_CODE = "12200404";
    protected static final String WORKER_NO_EXIST_MSG = "资源不存在!";

    protected static final String METADATA = "metadata";
    protected static final String LABELS = "labels";
    protected static final String STATUS = "status";
    protected static final String ITEMS = "items";
    protected static final String SPEC = "spec";
    protected static final String CONTAINERS = "containers";
    protected static final String RESOURCES = "resources";
    protected static final String REQUESTS = "requests";
    protected static final String CPU = "cpu";
    protected static final String MEMORY = "memory";
    protected static final String CAPACITY = "capacity";
    protected static final Integer MEGABYTE = 1024;
    protected static final Integer THOUSAND = 1000;
    protected static final String PERCENT = "%";

    public static final String CPUCORES = "cpuCores";//CPU总核数
    public static final String CPUUSEDPERCENT = "cpuUsedPercent";//CPU使用率
    public static final String MEMSIZE = "memSize";//内存总大小
    public static final String MEMUSEDPERCENT = "memUsedPercent";//内存使用率

//    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    /**
     * 调用apiServer获取nodes列表所有信息
     * @param url
     * @param methodType
     * @param httpEntity
     * @param params
     * @param <T>
     * @return
     * @author czx
     */
    private <T> T getForEntity(String url, HttpMethod methodType, HttpEntity<?> httpEntity, Class<T> clzss, Map<String, Object> params) {
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getForEntity,url:{0},params:{1}", url, JSON.toJSONString(params)));
        ResponseEntity<T> responseEntity = null;
        try {
            //initiate a get request
            responseEntity = restTemplate.exchange(url, methodType, httpEntity, clzss, params);
            logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getForEntity,responseEntity:{0},params:{1}", responseEntity.toString()));
        } catch (NLUnCheckedException e) {
            logger.error(LogProperty.LOGTYPE_DETAIL, "NLUnCheckedException", e);
            throw new NLUnCheckedException(SERVER_REQUEST_ERROR_CODE, SERVER_REQUEST_ERROR_MSG);
        }
        //get the response status code
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is4xxClientError()) {
            throw new NLUnCheckedException(REQUEST_ERROR_CODE, REQUEST_ERROR_MSG);
        } else if (httpStatus.is5xxServerError()) {
            throw new NLUnCheckedException(SERVER_ERROR_CODE, SERVER_ERROR_MSG);
        }
        return responseEntity.getBody();
    }


    /**
     * 获取节点相关的labels
     *
     * @param url        http://10.1.8.8:8080/api/v1/nodes/ or get node detail: http://10.1.8.8:8080/api/v1/nodes/10.1.8.8，the name is 10.1.8.8
     * @param methodType
     * @param httpEntity
     * @param params
     * @return
     */
    public Map<String, Object> getNodesListOrNodeDetail(String url, HttpMethod methodType, HttpEntity<?> httpEntity, Map<String, Object> params) {
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getNodesListOrNodeDetail,url:{0},params:{1}", url, JSON.toJSONString(params)));
        //call the parent method·
        Map<String, Object> result = this.getForEntity(url, methodType, httpEntity, Map.class, params);
        logger.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("getNodesListOrNodeDetail,result:{0}", result.toString()));
        if (CollectionUtils.isEmpty(result)) {
            throw new NLUnCheckedException(WORKER_NO_EXIST_CODE, WORKER_NO_EXIST_MSG);
        }
        return result;
    }



}