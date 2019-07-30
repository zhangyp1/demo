package com.newland.paas.sbcommon.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * @author laiCheng
 * @date 2018/8/9 10:32
 */
public class FormRestfulRequestUtil implements InvocationHandler {

    private static final Log LOG = LogFactory.getLogger(FormRestfulRequestUtil.class);

    protected FormRestfulRequestUtil() {

    }

    public static <T> T getInstance(Class<T> cls, String urlPrefix) {
        FormRestfulRequestUtil ih = new FormRestfulRequestUtil();
        ih.urlPrefix = urlPrefix;
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {cls}, ih);
    }

    protected String urlPrefix;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String url = parseUrl(method, args);
        String requestMethod = parseRequestMethod(method);
        String requestBody = parseRequestBody(method, args);
        if (requestMethod.toLowerCase().equals("get")) {
            url += "?" + requestBody;
        }
        String response = request(requestMethod, url, requestBody);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "response:\n" + response);
        return JSONObject.parseObject(response, method.getGenericReturnType());
    }

    private String parseRequestBody(Method method, Object[] args) {
        Annotation[][] annoaa = method.getParameterAnnotations();
        List<String> nps = new ArrayList<>();
        for (int i = 0; i < annoaa.length; i++) {
            List<Annotation> annos = Arrays.asList(annoaa[i]);
            int finalI = i;
            annos.stream().filter(e -> e instanceof RequestParam).findFirst().ifPresent((one) -> {
                RequestParam a = (RequestParam) one;
                try {
                    nps.add(URLEncoder.encode(a.value(), "utf-8") + "="
                        + URLEncoder.encode(String.valueOf(args[finalI]), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
        }
        return org.apache.commons.lang.StringUtils.join(nps, "&");
    }

    private String parseRequestMethod(Method method) {
        GetMapping get = method.getAnnotation(GetMapping.class);
        PostMapping post = method.getAnnotation(PostMapping.class);
        PutMapping put = method.getAnnotation(PutMapping.class);
        DeleteMapping del = method.getAnnotation(DeleteMapping.class);

        if (get != null) {
            return "GET";
        }
        if (post != null) {
            return "POST";
        }
        if (put != null) {
            return "PUT";
        }
        if (del != null) {
            return "DELETE";
        }
        return null;
    }

    private String parseUrl(Method method, Object[] args) {
        String url = "";
        GetMapping get = method.getAnnotation(GetMapping.class);
        PostMapping post = method.getAnnotation(PostMapping.class);
        PutMapping put = method.getAnnotation(PutMapping.class);
        DeleteMapping del = method.getAnnotation(DeleteMapping.class);

        if (get != null) {
            url = get.value()[0];
        }
        if (post != null) {
            url = post.value()[0];
        }
        if (put != null) {
            url = put.value()[0];
        }
        if (del != null) {
            url = del.value()[0];
        }

        Annotation[][] annoaa = method.getParameterAnnotations();
        for (int i = 0; i < annoaa.length; i++) {
            List<Annotation> annos = Arrays.asList(annoaa[i]);
            PathVariable anno = (PathVariable) annos.stream().filter(e -> e instanceof PathVariable).findFirst()
                .orElse(null);
            if (anno == null) {
                continue;
            }
            url = url.replace("{" + anno.value() + "}", String.valueOf(args[i]));
        }
        return url;
    }

    private String cookie = "";

    /**
     * 设置请求的属性
     * @param hc
     */
    protected void putRequestProperties(HttpURLConnection hc) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(this.cookie)) {
            hc.setRequestProperty("Cookie", this.cookie);
        }
    }

    private String request(String requestMethod, String url, String requestBody) throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "request send " + requestMethod + " " + url);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "request body:\n " + requestBody);
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        HttpURLConnection hc = null;
        try {
            URL u = new URL(this.urlPrefix + "/" + url);
            hc = (HttpURLConnection) u.openConnection();
            hc.setRequestMethod(requestMethod);
            hc.setDoInput(true);
            hc.setConnectTimeout(30000);
            hc.setReadTimeout(30000);
            this.putRequestProperties(hc);
            if (StringUtils.isNotEmpty(requestBody) && !requestMethod.toLowerCase().equals("get")) {
                hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                hc.setDoOutput(true);
                osw = new OutputStreamWriter(hc.getOutputStream(), "utf-8");
                osw.write(requestBody);
                osw.flush();
            }
            int responseCode = hc.getResponseCode();
            if (responseCode != 200) {
                throw new ApplicationException(new PaasError("10300005", url + "调用异常,状态码:" + responseCode));
            }
            isr = new InputStreamReader(hc.getInputStream(), "utf-8");
            char[] buffer = new char[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = isr.read(buffer)) > 0) {
                sb.append(buffer, 0, len);
            }
            String respCookie = hc.getHeaderField("Set-Cookie");
            if (respCookie.contains("JSESSIONID=")) {
                this.cookie = respCookie.replaceAll("(.*?);.*", "$1");
            }
            return sb.toString();
        } catch (ApplicationException ae) {
            throw ae;
        } catch (Exception ex) {
            throw new ApplicationException(new PaasError("10300005", this.urlPrefix + "/" + url + "调用异常"), ex);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (hc != null) {
                hc.disconnect();
            }
        }
    }
}
