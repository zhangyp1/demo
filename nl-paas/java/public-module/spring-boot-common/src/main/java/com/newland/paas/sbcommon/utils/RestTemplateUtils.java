package com.newland.paas.sbcommon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Map;

/**
 * RestTemplate工具类
 *
 * @author wrp
 * @since 2017/10/10
 */
@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class RestTemplateUtils {

    private static final Log LOG = LogFactory.getLogger(RestTemplateUtils.class);

    private static final String HTTP = "http://";
    private static final String BACKSLASH = "/";

    private static final PaasError SERVICE_ID_UN_KNOW = new PaasError("12200001", "serviceId为空!");
    private static final PaasError INTERFACE_URL_UN_KNOW = new PaasError("12200002", "接口路径为空!");
    private static final PaasError REST_CLIENT_EXCEPTION = new PaasError("12200000", "服务请求失败!");
    private static final PaasError REQUEST_ERROR = new PaasError("12201001", "请求错误!");
    private static final PaasError SERVICE_ERROR = new PaasError("12201002", "服务器错误!");

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 发送japplication/json方式的post请求
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T postForEntity(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                               Object params, ParameterizedTypeReference responseType) {
        return postForTokenEntity(microservicesItemProperties, interfaceUrl, null, params, responseType);
    }

    /**
     * 发送japplication/json方式的post请求，header带token
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param token                       token
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T postForTokenEntity(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                                    String token, Object params, ParameterizedTypeReference responseType) {
        String uri = buildInterfaceUri(microservicesItemProperties, interfaceUrl);
        LOG.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("postForTokenEntity,uri:{0},params:{1}", uri, JSON.toJSONString(params)));
//        if ("execengn".equals(microservicesItemProperties.getServiceId())) {
//            return EeControllerMock.chooseInterface(interfaceUrl, params);
//        }
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.POST, buildHttpEntity(token, params), responseType);
        } catch (RestClientException e) {
            parseException(uri, e);
        }
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is4xxClientError()) {
            throw new SystemException(REQUEST_ERROR);
        } else if (httpStatus.is5xxServerError()) {
            throw new SystemException(SERVICE_ERROR);
        }
        return responseEntity.getBody();
    }

    /**
     * 发送japplication/json方式的post请求，header带token、paasToken
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param token                       token
     * @param paasToken                   paasToken
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T postForTokenEntityActiviti(
            MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
            String token, String paasToken, Object params, ParameterizedTypeReference responseType) {
        String uri = buildInterfaceUri(microservicesItemProperties, interfaceUrl);
        LOG.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("postForTokenEntity,uri:{0},params:{1}", uri, JSON.toJSONString(params)));
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(
                    uri, HttpMethod.POST, buildHttpEntityActiviti(token, paasToken, params), responseType);
        } catch (RestClientException e) {
            parseException(uri, e);
        }
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is4xxClientError()) {
            throw new SystemException(REQUEST_ERROR);
        } else if (httpStatus.is5xxServerError()) {
            throw new SystemException(SERVICE_ERROR);
        }
        return responseEntity.getBody();
    }

    /**
     * 发送get请求
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T getForEntity(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                              Map<String, Object> params, ParameterizedTypeReference responseType) {
        return getForTokenEntity(microservicesItemProperties, interfaceUrl, null, params, responseType);
    }

    /**
     * 发送get请求，header带token
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param token                       token
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T getForTokenEntity(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                                   String token, Map<String, Object> params, ParameterizedTypeReference responseType) {
        String uri = buildInterfaceUri(microservicesItemProperties, interfaceUrl);
        LOG.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("getForTokenEntity,uri:{0},params:{1}", uri, JSON.toJSONString(params)));
//        if ("execengn".equals(microservicesItemProperties.getServiceId())) {
//            return EeControllerMock.chooseInterface(interfaceUrl, params);
//        }
        String urlParam = buildGetParams(params);
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri + urlParam, HttpMethod.GET, buildHttpEntity(token, null),
                    responseType, params);
        } catch (RestClientException e) {
            parseException(uri, e);
        }
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is4xxClientError()) {
            throw new SystemException(REQUEST_ERROR);
        } else if (httpStatus.is5xxServerError()) {
            throw new SystemException(SERVICE_ERROR);
        }
        return responseEntity.getBody();
    }

    /**
     * 发送delete请求
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param params                      参数
     */
    public void delete(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                       Map<String, Object> params) {
        deleteForToken(microservicesItemProperties, interfaceUrl, null, params);
    }

    /**
     * 发送delete请求，header带token
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param token                       token
     * @param params                      参数
     */
    public void deleteForToken(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                               String token, Map<String, Object> params) {
        String uri = buildInterfaceUri(microservicesItemProperties, interfaceUrl);
        LOG.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("getForEntity,uri:{0},params:{1}", uri, JSON.toJSONString(params)));
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity =
                    restTemplate.exchange(uri, HttpMethod.DELETE, buildHttpEntity(token, params), String.class);
        } catch (RestClientException e) {
            parseException(uri, e);
        }
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is4xxClientError()) {
            throw new SystemException(REQUEST_ERROR);
        } else if (httpStatus.is5xxServerError()) {
            throw new SystemException(SERVICE_ERROR);
        }
    }

    /**
     * 发送japplication/json方式的DELETE请求
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T deleteForEntity(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                                 Object params, ParameterizedTypeReference responseType) {
        return deleteForTokenEntity(microservicesItemProperties, interfaceUrl, null, params, responseType);
    }

    /**
     * 发送japplication/json方式的DELETE请求，header带token
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param token                       token
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T deleteForTokenEntity(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl,
                                      String token, Object params, ParameterizedTypeReference responseType) {
        String uri = buildInterfaceUri(microservicesItemProperties, interfaceUrl);
        LOG.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("deleteForTokenEntity,uri:{0},params:{1}", uri, JSON.toJSONString(params)));
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity =
                    restTemplate.exchange(uri, HttpMethod.DELETE, buildHttpEntity(token, params), responseType);
        } catch (RestClientException e) {
            parseException(uri, e);
        }
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is4xxClientError()) {
            throw new SystemException(REQUEST_ERROR);
        } else if (httpStatus.is5xxServerError()) {
            throw new SystemException(SERVICE_ERROR);
        }
        return responseEntity.getBody();
    }

    /**
     * 发送japplication/json方式的put请求
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T put(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl, Object params,
                     ParameterizedTypeReference responseType) {
        return putForToken(microservicesItemProperties, interfaceUrl, null, params, responseType);
    }

    /**
     * 发送japplication/json方式的put请求，header带token
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @param token                       token
     * @param params                      参数
     * @param responseType                接口返回的数据类型
     * @param <T>                         接口返回的数据对象
     * @return 接口返回的数据
     */
    public <T> T putForToken(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl, String token,
                             Object params, ParameterizedTypeReference responseType) {
        String uri = buildInterfaceUri(microservicesItemProperties, interfaceUrl);
        LOG.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("postForTokenEntity,uri:{0},params:{1}", uri, JSON.toJSONString(params)));
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, buildHttpEntity(token, params), responseType);
        } catch (RestClientException e) {
            parseException(uri, e);
        }
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is4xxClientError()) {
            throw new SystemException(REQUEST_ERROR);
        } else if (httpStatus.is5xxServerError()) {
            throw new SystemException(SERVICE_ERROR);
        }
        return responseEntity.getBody();
    }

    /**
     * 构造请求完整接口路径
     *
     * @param microservicesItemProperties 微服务参数
     * @param interfaceUrl                接口地址
     * @return 接口路径
     */
    private String buildInterfaceUri(MicroservicesItemProperties microservicesItemProperties, String interfaceUrl) {
        LOG.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("buildInterfaceUri,microservicesItemProperties:{0},interfaceUrl:{1}",
                        JSON.toJSONString(microservicesItemProperties), interfaceUrl));
        if (null == microservicesItemProperties || StringUtils.isEmpty(microservicesItemProperties.getServiceId())) {
            LOG.info(LogProperty.LOGTYPE_DETAIL,
                    MessageFormat.format("buildInterfaceUri,SERVICE_ID_UN_KNOW:",
                            JSON.toJSONString(SERVICE_ID_UN_KNOW)));
            throw new SystemException(SERVICE_ID_UN_KNOW);
        }
        if (StringUtils.isEmpty(interfaceUrl)) {
            LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("buildInterfaceUri,INTERFACE_URL_UN_KNOW:",
                    JSON.toJSONString(SERVICE_ID_UN_KNOW)));
            throw new SystemException(INTERFACE_URL_UN_KNOW);
        }
        // 将contentPath前面加/
        if (StringUtils.isEmpty(microservicesItemProperties.getContentPath())) {
            microservicesItemProperties.setContentPath("");
        } else {
            if (!microservicesItemProperties.getContentPath().substring(0, 1).equals(BACKSLASH)) {
                microservicesItemProperties.setContentPath(BACKSLASH + microservicesItemProperties.getContentPath());
            }
        }
        // 接口路径前面加/
        if (!interfaceUrl.substring(0, 1).equals(BACKSLASH)) {
            interfaceUrl = BACKSLASH + interfaceUrl;
        }
        return HTTP + microservicesItemProperties.getServiceId() + microservicesItemProperties.getContentPath()
                + interfaceUrl;
    }

    /**
     * 构造get请求的参数
     *
     * @param params 参数
     * @return url参数
     */
    private String buildGetParams(Map<String, Object> params) {
        String urlParam = "";
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            urlParam = sb.substring(0, sb.length() - 1);
        }
        return urlParam;
    }

    /**
     * 构造请求参数
     *
     * @param params json数据
     * @param token  token
     * @return 请求头
     */
    private static HttpEntity<String> buildHttpEntity(String token, Object params) {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotEmpty(token)) {
            headers.add("Authorization", token);
        }
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(JSON.toJSONString(params), headers);
    }

    /**
     * 构造请求参数
     *
     * @param params    json数据
     * @param token     token
     * @param paasToken paasToken
     * @return 请求头
     */
    private static HttpEntity<String> buildHttpEntityActiviti(String token, String paasToken, Object params) {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotEmpty(token)) {
            headers.add("Authorization", token);
            headers.add("PAAS-Authorization", paasToken);
        }
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(JSON.toJSONString(params), headers);
    }

    /**
     * 异常处理
     *
     * @param uri uri
     * @param e   异常
     */
    private static void parseException(String uri, RestClientException e) {
        LOG.error(LogProperty.LOGTYPE_DETAIL, "调用接口异常:" + uri, e);
        // 调用接口4xx,5xx异常
        if (e instanceof HttpStatusCodeException) {
            String responseBodyString = ((HttpStatusCodeException) e).getResponseBodyAsString();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "调用接口异常,返回值:" + responseBodyString);
            BasicResponseContentVo<Object> errorVo = JSON.parseObject(responseBodyString,
                    new TypeReference<BasicResponseContentVo<Object>>() {});
            PaasError paasError = new PaasError("10300005", e.getMessage());
            if (errorVo != null && errorVo.getError() != null && errorVo.getError().getDescription() != null) {
                paasError.setDescription(errorVo.getError().getDescription());
            }
            paasError.setSolution("调用接口异常:" + uri);
            throw new SystemException(paasError);
        }
        throw new SystemException(REST_CLIENT_EXCEPTION);
    }

}
