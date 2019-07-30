//package com.newland.paas.common.util;
//
//import com.alibaba.fastjson.JSON;
//import com.newland.paas.common.exception.ErrorCode;
//import com.newland.paas.common.exception.NLUnCheckedException;
//import com.newland.paas.log.Log;
//import com.newland.paas.log.LogFactory;
//import com.newland.paas.log.LogProperty;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.client.RestTemplate;
//
//import java.text.MessageFormat;
//
///**
// * @author wrp
// * @Description RestTemplateU工具类
// * @Date 2017/10/10
// */
//public class RestTemplateUtils {
//
//    private static final Log log = LogFactory.getLogger(RestTemplateUtils.class);
//
//    private RestTemplate restTemplate = new RestTemplate();
//
//    @Autowired
//    private LoadBalancerClient loadBalancerClient;
//
//    private static final String HTTP = "http://";
//    private static final String COLON = ":";
//    private static final String BACKSLASH = "/";
//    private static final String INFO_URL = "info";
//
//
//    /**
//     * post方式请求数据，参数为json。
//     *
//     * @param serviceId    访问服务的serviceId
//     * @param contentPath  服务上下文
//     * @param interfaceUrl 请求接口的url
//     * @param param        参数，对象类型
//     * @param clazz        返回的数据类型
//     * @param <T>
//     * @return
//     */
//    public <T> T postForObject(String serviceId, String contentPath, String interfaceUrl, Object param, Class<T> clazz) {
//        if (StringUtils.isEmpty(serviceId)) {
//            throw new NLUnCheckedException(ErrorCode.SERVICE_ID_UN_KNOW, ErrorCode.SERVICE_ID_UN_KNOW_MSG);
//        }
//        // 选择当前轮询的节点
//        ServiceInstance serviceInstance = this.loadBalancerClient.choose(serviceId);
//        if (serviceInstance == null) {
//            throw new NLUnCheckedException(ErrorCode.SERVICE_ID_NOT_FIND, ErrorCode.SERVICE_ID_NOT_FIND_MSG);
//        }
//        // 将contentPath前面加上/
//        if (StringUtils.isEmpty(contentPath)) {
//            contentPath = "";
//        } else {
//            if (!contentPath.substring(0, 1).equals(BACKSLASH)) {
//                contentPath = BACKSLASH + contentPath;
//            }
//        }
//        // 将interfaceUrl前面加上/
//        if (StringUtils.isEmpty(interfaceUrl)) {
//            interfaceUrl = "";
//        } else {
//            if (!interfaceUrl.substring(0, 1).equals(BACKSLASH)) {
//                interfaceUrl = BACKSLASH + interfaceUrl;
//            }
//        }
//        // 服务上下文
//        String contentUrl = HTTP + serviceInstance.getHost() + COLON + serviceInstance.getPort() + contentPath;
//        // 获取请求服务的info
//        String infoUrl = contentUrl + BACKSLASH + INFO_URL;
//        String infoData = restTemplate.getForObject(infoUrl, String.class);
//        log.info(LogProperty.LOGTYPE_CALL, MessageFormat.format("当前请求{0}服务的info信息为：{1}", serviceId, infoData));
//        // 构造实际请求的url
//        String url = contentUrl + interfaceUrl;
//        /*String url = "http://10.1.3.80:8091/paas/v1/" + interfaceUrl;*/
//        log.info(LogProperty.LOGTYPE_CALL, "访问接口:", url);
//        return restTemplate.postForObject(url, RestTemplateUtils.buildHttpEntity(JSON.toJSONString(param)), clazz);
//    }
//
//
//    /**
//     * 构造请求参数
//     *
//     * @param params json数据
//     * @return
//     */
//    public static HttpEntity<String> buildHttpEntity(String params) {
//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//        return new HttpEntity<String>(params, headers);
//    }
//
//
//}
