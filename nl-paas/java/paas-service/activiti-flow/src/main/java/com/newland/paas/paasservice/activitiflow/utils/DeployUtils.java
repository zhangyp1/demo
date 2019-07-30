package com.newland.paas.paasservice.activitiflow.utils;

import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author WRP
 * @since 2018/7/25
 */
public class DeployUtils {

    private static final String BASEURI = "http://127.0.0.1:8097/paas/activitiflow";

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //流程发布
        publishFlow();
    }

    /**
     * createClient
     *
     * @param uri
     * @return
     */
    private static WebClient createClient(String uri) {
        WebClient client = WebClient.create(BASEURI + uri);
        String auth = "Basic " + Base64Utility.encode("admin:admin".getBytes());
        client.header("Authorization", auth);
        return client;
    }

    /**
     * publishFlow
     *
     * @throws IOException
     */
    public static void publishFlow() throws IOException {
        String filepath = "D:\\work\\java\\IDEASpaces\\Paas\\java\\"
                + "paas-service\\activiti-flow\\src\\main\\resources\\processes\\";
        String fileName = "SvrReleaseOrder.bpmn";
        WebClient client = createClient("/repository/deployments");
        client.type("multipart/form-data");
        // 创建用户流程
        InputStream bpmnFileIS = new FileInputStream(filepath + fileName);
        ContentDisposition cd = new ContentDisposition("form-data;name=bpmn.xml;filename=" + fileName);
        Attachment att = new Attachment(fileName, bpmnFileIS, cd);
        client.post(new MultipartBody(att));
    }


}
