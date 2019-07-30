package com.newland.paas.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @program: paas-all
 * @description: harbor工具类
 * @author: Frown
 * @create: 2018-08-03 16:11
 **/
public class HarborUtil {

    private static final Log LOG = LogFactory.getLogger(HarborUtil.class);

    private static final String ERROR_CODE_HARBOR = "9999";

    private static final String CREATE_PROJECT_PATH = "/projects";
    private static final String SEARCH_PROJECT_PATH = "/search";
    private static final String DELETE_PROJECT_PATH = "/projects";
    private static final String SEARCH_USER_PATH = "/users";
    private static final String CREATE_USER_PATH = "/users";
    private static final String DELETE_USER_PATH = "/users";
    private static final String SEARCH_IMAGE_PATH = "/repositories";
    private static final String DELETE_IMAGE_PATH = "/repositories";
    private static final String GET_IMAGE_PATH = "/repositories";

    // 用户名
    private String username;
    // 密码
    private String password;

    /** harbor 服务器地址IP地址 */
    private String host;
    /** harbor 端口 */
    private int port;

    private String ipPort;

    public HarborUtil() {}

    public HarborUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.ipPort = "http://" + this.host + ":" + this.port;
    }

    /**
     * 创建工程
     * 
     * @param projName 项目名称
     * @param isPublic 是否发布
     * @throws Exception 异常
     */
    public void createProject(String projName, Boolean isPublic) throws Exception {
        Integer projId = this.getProjIdByName(projName);
        if (projId != null) {
            return;
        }
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(this.ipPort + "/api" + CREATE_PROJECT_PATH);
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.addHeader("Authorization", "Basic " + auth);
        JSONObject projParam = new JSONObject();
        projParam.put("project_name", projName);
        projParam.put("public", isPublic ? 1 : 0);
        httpPost.setEntity(new StringEntity(projParam.toJSONString(), Charset.forName("UTF-8")));
        LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("HARBOR请求创建项目，入参{0}", projParam.toJSONString()));
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_CREATED != status) {
                throw new Exception(response.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目创建失败:" + e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目创建失败:" + e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目创建失败:" + e.getMessage());
        } finally {
            httpClient.close();
        }
    }

    /**
     * 删除工程
     * 
     * @param projName 工程名称
     * @throws Exception 异常
     */
    public void deleteProject(String projName) throws Exception {
        Integer projId = this.getProjIdByName(projName);
        if (projId == null) {
            return;
        }
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(this.ipPort + "/api" + DELETE_PROJECT_PATH + "/" + projId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.addHeader("Authorization", "Basic " + auth);
        try {
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_OK != status) {
                throw new Exception(response.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目删除失败:" + e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目删除失败:" + e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目删除失败:" + e.getMessage());
        } finally {
            httpClient.close();
        }
    }

    /**
     * 根据工程名获取工程id
     * 
     * @param projName 工程名称
     * @return 返回
     * @throws Exception 异常
     */
    public Integer getProjIdByName(String projName) throws Exception {
        if (StringUtils.isEmpty(projName)) {
            return null;
        }
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(this.ipPort + "/api" + SEARCH_PROJECT_PATH + "?q=" + projName);
        httpGet.setHeader("Accept", "application/json");
        httpGet.addHeader("Authorization", "Basic " + auth);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_OK == status) {
                HttpEntity entity = response.getEntity();
                if (null == entity) {
                    return null;
                }
                // Document doc = Jsoup.parse(entity.getContent(), "UTF-8", ""); 可直接用jsoup接收为网页
                // entity.getContent内容流, 该api返回的是json字符串
                BufferedReader isr = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = isr.readLine()) != null) {
                    sb.append(line);
                }
                // 接口返回的是json数据
                LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的内容>>>>>" + sb.toString());
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                if (jsonObject != null && jsonObject.get("project") != null) {
                    JSONArray projs = (JSONArray) jsonObject.get("project");
                    if (projs != null && projs.size() > 0) {
                        return (Integer) ((JSONObject) projs.get(0)).get("project_id");
                    }
                }
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目查询失败:" + e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目查询失败:" + e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR项目查询失败:" + e.getMessage());
        } finally {
            httpClient.close();
        }
        return null;
    }

    /**
     * 创建用户
     * 
     * @param username 用户名
     * @param password 密码
     * @throws Exception 异常
     */
    public void createUser(String username, String password) throws Exception {
        Integer userId = this.getUserIdByName(username);
        if (userId != null || Objects.equals("admin", username)) {
            return;
        }
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(this.ipPort + "/api" + CREATE_USER_PATH);
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.addHeader("Authorization", "Basic " + auth);
        JSONObject userParam = new JSONObject();
        userParam.put("username", username);
        userParam.put("email", username + "@harbor.com");
        userParam.put("password", password);
        userParam.put("realname", username);
        httpPost.setEntity(new StringEntity(userParam.toJSONString(), Charset.forName("UTF-8")));
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_CREATED != status) {
                throw new Exception(response.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户创建失败:" + e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户创建失败:" + e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户创建失败:" + e.getMessage());
        } finally {
            httpClient.close();
        }
    }

    /**
     * 删除用户
     * 
     * @param username 用户
     * @throws Exception 异常
     */
    public void deleteUser(String username) throws Exception {
        Integer userId = this.getUserIdByName(username);
        if (userId == null) {
            return;
        }
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(this.ipPort + "/api" + DELETE_USER_PATH + "/" + userId);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.addHeader("Authorization", "Basic " + auth);
        try {
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_OK != status) {
                throw new Exception(response.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户删除失败:" + e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户删除失败:" + e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户删除失败:" + e.getMessage());
        } finally {
            httpClient.close();
        }

    }

    /**
     * 根据用户名获取用户id
     * 
     * @param username 用户名
     * @return 返回
     * @throws Exception 异常
     */
    private Integer getUserIdByName(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(this.ipPort + "/api" + SEARCH_USER_PATH + "?username=" + username);
        httpGet.setHeader("Accept", "application/json");
        httpGet.addHeader("Authorization", "Basic " + auth);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_OK == status) {
                HttpEntity entity = response.getEntity();
                if (null == entity) {
                    return null;
                }
                // Document doc = Jsoup.parse(entity.getContent(), "UTF-8", ""); 可直接用jsoup接收为网页
                // entity.getContent内容流, 该api返回的是json字符串
                BufferedReader isr = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = isr.readLine()) != null) {
                    sb.append(line);
                }
                // 接口返回的是json数据
                LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的内容>>>>>" + sb.toString());
                JSONArray jSONArray = JSONObject.parseArray(sb.toString());
                if (jSONArray != null && jSONArray.size() > 0) {
                    JSONObject jsonObject = (JSONObject) jSONArray.get(0);
                    return (Integer) jsonObject.get("user_id");
                }
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户查询失败:" + e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户查询失败:" + e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
            throw new Exception("HARBOR用户查询失败:" + e.getMessage());
        } finally {
            httpClient.close();
        }
        return null;
    }

    /**
     * 检查镜像
     * 
     * @param repoName 镜像名
     * @param tag 镜像版本
     * @return 返回
     * @throws Exception 异常
     */
    public Boolean checkImage(String repoName, String tag) throws Exception {
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(this.ipPort + "/api" + SEARCH_IMAGE_PATH + "/" + repoName + "/tags/" + tag);
        httpGet.setHeader("Accept", "application/json");
        httpGet.addHeader("Authorization", "Basic " + auth);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_OK == status) {
                HttpEntity entity = response.getEntity();
                if (null == entity) {
                    return null;
                }
                // Document doc = Jsoup.parse(entity.getContent(), "UTF-8", ""); 可直接用jsoup接收为网页
                // entity.getContent内容流, 该api返回的是json字符串
                BufferedReader isr = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = isr.readLine()) != null) {
                    sb.append(line);
                }
                // 接口返回的是json数据
                LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的内容>>>>>" + sb.toString());
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                if (jsonObject != null) {
                    return true;
                }
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } finally {
            httpClient.close();
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR检查镜像不存在");
        return false;
    }

    /**
     * 根据tag删除镜像
     * 
     * @param repoName 镜像名
     * @param tag 镜像版本
     * @return 返回
     * @throws Exception 异常
     */
    public Boolean deleteImage(String repoName, String tag) throws Exception {
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete =
            new HttpDelete(this.ipPort + "/api" + DELETE_IMAGE_PATH + "/" + repoName + "/tags/" + tag);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.addHeader("Authorization", "Basic " + auth);
        try {
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_OK == status) {
                return true;
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } finally {
            httpClient.close();
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR删除镜像失败");
        return false;
    }

    /**
     * 获取镜像的SHA256
     * 
     * @param repoName 镜像名
     * @param tag 镜像版本
     * @return 返回
     * @throws Exception 异常
     */
    public String getProjImageSha256ByTag(String repoName, String tag) throws Exception {
        String auth = new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes("GBK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(this.ipPort + "/api" + GET_IMAGE_PATH + "/" + repoName + "/tags/" + tag);
        httpGet.setHeader("Accept", "application/json");
        httpGet.addHeader("Authorization", "Basic " + auth);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的状态>>>>>" + status);
            if (HttpStatus.SC_OK == status) {
                HttpEntity entity = response.getEntity();
                if (null == entity) {
                    return null;
                }
                // Document doc = Jsoup.parse(entity.getContent(), "UTF-8", ""); 可直接用jsoup接收为网页
                // entity.getContent内容流, 该api返回的是json字符串
                BufferedReader isr = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = isr.readLine()) != null) {
                    sb.append(line);
                }
                // 接口返回的是json数据
                LOG.info(LogProperty.LOGTYPE_DETAIL, "HARBOR请求响应的内容>>>>>" + sb.toString());
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                if (jsonObject != null && jsonObject.get("digest") != null) {
                    String digest = jsonObject.getString("digest");
                    return digest;
                }
            }
        } catch (ClientProtocolException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, ERROR_CODE_HARBOR, e.getCause(), e.getMessage());
        } finally {
            httpClient.close();
        }
        return null;
    }

//    public static void main(String[] args) {
//        HarborUtil harborUtil = new HarborUtil("nl2", "Harbor12345", "10.1.8.15", 5000);
//        try {
//            harborUtil.login();
//            harborUtil.listProject("");
//            harborUtil.createProject("test3", false);
//            harborUtil.deleteProject("test3");
//            Integer userId = harborUtil.getUserIdByName("");
//            System.out.println(userId);
//            harborUtil.createUser("test4", "aA12345678");
//            harborUtil.deleteUser("test2");
//            Integer userId = harborUtil.getUserIdByName("test5");
//            System.out.println(userId);
//            harborUtil.deleteProject("dd2");
//            harborUtil.deleteUser("dd");
//            Boolean aBoolean = harborUtil.checkImage("myproj01/grafana", "5.1.3");
//            if (aBoolean) {
//                Boolean aBoolean1 = harborUtil.deleteImage("myproj01/grafana", "5.1.3");
//                System.out.println(aBoolean1);
//            }
//            String a = harborUtil.getProjImageSha256ByTag("nl2/mysql", "5.6.35");
//            String b = harborUtil.getProjImageSha256ByTag("nl2/mysql", "5.6.36");
//
//            System.out.println(a.equals(b));
//
//            if (StringUtils.isNotEmpty(a)) {
//                String[] imagesDigest = a.split(":");
//                System.out.println(imagesDigest[1]);
//            }
//            if (StringUtils.isNotEmpty(a)) {
//                String[] imagesDigest = a.split(":");
//                System.out.println(imagesDigest[1]);
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
