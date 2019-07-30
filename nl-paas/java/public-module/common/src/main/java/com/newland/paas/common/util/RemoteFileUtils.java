package com.newland.paas.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zhoufeilong
 * @Description:
 * @Date: 2017-10-19 00:21
 * @Modified By: zhoufeilong
 */
public class RemoteFileUtils {
    /*
        进度回调接口
     */
    public interface HttpClientDownLoadProgress {
        public void onProgress(int progress);
    }

    private static class RemoteFileUtilsHolder {
        private static final RemoteFileUtils INSTANCE = new RemoteFileUtils();
    }

    private RemoteFileUtils (){}

    public static final RemoteFileUtils getInstance() {
        return RemoteFileUtilsHolder.INSTANCE;
    }

    /**
     * 下载文件
     *
     * @param url 文件链接
     * @param filePath 文件下载完整路径
     * @throws Exception
     */
    public void downloadFile(final String url, final String filePath) throws Exception {
        downloadFile(url, filePath, null, null);
    }

    /**
     * 下载文件
     *
     * @param url 文件链接
     * @param filePath 文件下载完整路径
     * @param progress 进度回调
     * @throws Exception
     */
    public void downloadFile(final String url, final String filePath,
                         final HttpClientDownLoadProgress progress) throws Exception {
        downloadFile(url, filePath, progress, null);
    }

    /**
     * 下载文件
     *
     * @param url 文件链接
     * @param filePath 文件下载完整路径
     * @param progress 进度回调
     * @param headMap 请求消息头部
     * @throws Exception
     */
    private void downloadFile(String url, String filePath,
                                  HttpClientDownLoadProgress progress, Map<String, String> headMap) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            if(headMap != null){
                setGetHead(httpGet, headMap);
            }

            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                HttpEntity httpEntity = response.getEntity();
                long contentLength = httpEntity.getContentLength();
                InputStream is = httpEntity.getContent();
                // 根据InputStream下载文件
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int r = 0;
                long totalRead = 0;
                while ((r = is.read(buffer)) > 0) {
                    output.write(buffer, 0, r);
                    totalRead += r;
                    if (progress != null) {// 回调进度
                        progress.onProgress((int) (totalRead * 100 / contentLength));
                    }
                }
                FileOutputStream fos = new FileOutputStream(filePath);
                output.writeTo(fos);
                output.flush();
                output.close();
                fos.close();
                EntityUtils.consume(httpEntity);
            } finally {
                response.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     *
     * @param serverUrl 服务器地址
     * @param localFilePath 本地文件路径
     * @return
     * @throws Exception
     */
    public String uploadFile(String serverUrl, String localFilePath) throws Exception {
        return this.uploadFile(serverUrl, localFilePath, "file", null);
    }

    /**
     *
     * @param serverUrl 服务器地址
     * @param localFilePath 本地文件路径
     * @param serverFieldName 服务端文件字段名
     * @return
     * @throws Exception
     */
    public String uploadFile(String serverUrl, String localFilePath, String serverFieldName) throws Exception {
        return this.uploadFile(serverUrl, localFilePath, serverFieldName, null);
    }

    /**
     * 上传文件
     *
     * @param serverUrl
     *            服务器地址
     * @param localFilePath
     *            本地文件路径
     * @param serverFieldName
     *            服务端文件字段名
     * @param params
     *            普通表单数据
     * @return
     * @throws Exception
     */
    public String uploadFile(String serverUrl, String localFilePath,
                             String serverFieldName, Map<String, String> params) throws Exception {
        String respStr = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(serverUrl);
            FileBody binFileBody = new FileBody(new File(localFilePath));

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
                    .create();
            multipartEntityBuilder.setCharset(Charset.forName("utf-8"));
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // 添加上传的文件参数
            multipartEntityBuilder.addPart(serverFieldName, binFileBody);
            // 设置上传的其他参数
            if(params != null){
                setUploadParams(multipartEntityBuilder, params);
            }

            HttpEntity reqEntity = multipartEntityBuilder.build();
            httppost.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity resEntity = response.getEntity();
                respStr = getRespString(resEntity);
                EntityUtils.consume(resEntity);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                throw e;
            }
        }
        return respStr;
    }

    /**
     * 设置http的头部
     *
     * @param httpGet
     * @param headMap
     */
    private void setGetHead(HttpGet httpGet, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            Set<String> keySet = headMap.keySet();
            for (String key : keySet) {
                httpGet.addHeader(key, headMap.get(key));
            }
        }
    }

    /**
     * 设置上传文件时所附带的其他参数
     *
     * @param multipartEntityBuilder
     * @param params
     */
    private void setUploadParams(MultipartEntityBuilder multipartEntityBuilder,
                                 Map<String, String> params) {
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            for (String key : keys) {
                multipartEntityBuilder
                        .addPart(key, new StringBody(params.get(key),
                                contentType));
            }
        }
    }

    /**
     * 将返回结果转化为String
     *
     * @param entity
     * @return
     * @throws Exception
     */
    private String getRespString(HttpEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        StringBuffer strBuf = new StringBuffer();
        byte[] buffer = new byte[4096];
        int r = 0;
        while ((r = is.read(buffer)) > 0) {
            strBuf.append(new String(buffer, 0, r, "UTF-8"));
        }
        return strBuf.toString();
    }


    public static void main(String[] args) {
        // 测试文件下载
        try {
            RemoteFileUtils.getInstance().downloadFile(
                    "http://localhost:8090/tmp/inception5h.zip", "d:\\inception5h.zip",
                    new HttpClientDownLoadProgress() {

                        @Override
                        public void onProgress(int progress) {
                            System.out.println("download progress = " + progress);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 测试文件上传
        /*try {
            Map<String,String> uploadParams = new LinkedHashMap<String, String>();
            uploadParams.put("userName", "yun");
            uploadParams.put("password", "yun123");
            RemoteFileUtils.getInstance().uploadFile(
                    "http://localhost:8090/upload/FileUploadServlet", "d:\\班车.png");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}
