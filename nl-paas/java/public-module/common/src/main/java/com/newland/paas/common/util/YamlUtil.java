package com.newland.paas.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

public class YamlUtil {

    private static final Log LOG = LogFactory.getLogger(YamlUtil.class);

    /**
     * 获取yaml文件信息
     *
     * @param dumpFileName
     * @return
     * @throws Exception
     * @throws FileNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> readYamlFile(String dumpFileName) throws FileNotFoundException {
        Map<String, String> map = null;
        // 获取yaml文件中的配置数据，然后转换为obj，
        // Object load = yaml.load(new FileInputStream(dumpFile));
        // 也可以将值转换为Map
        FileInputStream fis = null;
        try {
            Yaml yaml = new Yaml();
            File dumpFile = new File(dumpFileName);
            fis = new FileInputStream(dumpFile);
            Map<String, Object> tmpMap = (Map<String, Object>)yaml.load(fis);
            map = new HashMap<>();
            for (Map.Entry<String, Object> en : tmpMap.entrySet()) {
                map.put(en.getKey(), String.valueOf(en.getValue()));
            }
            LOG.info(LogProperty.LOGTYPE_DETAIL, "readYamlFile>>>>>解析完成");
        } catch (FileNotFoundException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ce) {
                    fis = null;
                }
            }
        }
        return map;
    }

    /**
     * 获取yaml文件信息
     *
     * @param dumpFileName 输入文件
     * @return 返回map的对象，其实value为Object
     * @throws FileNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readYamlFileDiy(String dumpFileName) throws FileNotFoundException {
        Map<String, Object> map = null;
        // 获取yaml文件中的配置数据，然后转换为obj，
        // Object load = yaml.load(new FileInputStream(dumpFile));
        // 也可以将值转换为Map
        FileInputStream fis = null;
        try {
            Yaml yaml = new Yaml();
            File dumpFile = new File(dumpFileName);
            fis = new FileInputStream(dumpFile);
            map = (Map<String, Object>)yaml.load(fis);
            LOG.info(LogProperty.LOGTYPE_DETAIL, "readYamlFile>>>>>" + map);
            LOG.info(LogProperty.LOGTYPE_DETAIL, "readYamlFile>>>>>解析完成");
        } catch (FileNotFoundException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ce) {
                    fis = null;
                }
            }
        }
        return map;
    }

    /**
     * 导出yaml文件到指定目录
     *
     * @param data map数据
     * @param filePath 文件路径
     * @throws IOException
     */
    public static void writerYamlFile(Map<String, Object> data, String filePath) throws IOException {
        // 生成yaml格式
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        // 生成yaml文件
        Yaml yaml = new Yaml(dumperOptions);
        FileWriter fileWriter = new FileWriter(filePath);
        yaml.dump(data, fileWriter);
        fileWriter.write("\n");
        fileWriter.close();
    }

    /**
     * 将对象序列化为yaml字符串
     * 
     * @param obj
     * @return
     */
    public static String toYaml(Object obj) {
        Yaml yaml = new Yaml();
        String strYaml = yaml.dumpAsMap(obj);
        return strYaml;
    }

    /**
     * 将对象序列化为yaml字符串
     * 
     * @param obj
     * @return
     */
    public static String toYaml2(Object obj) {
        Yaml yaml = new Yaml();
        String strYaml = yaml.dump(obj);
        return strYaml;
    }

    /**
     * 将yaml字符串序列化为对象
     * 
     * @param strYaml
     * @param type
     * @return
     */
    public static <T> T toObj(String strYaml, Class<T> type) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(strYaml, type);
    }

    public static void main(String[] args) {
        System.out.println("-----------------------------------------------------------------------------");
        // yaml导出
        // String filePath = "D:/newland/upload/test.yaml";
        // Map<String, Object> data = new HashMap<String, Object>();
        // data.put("a", "a");
        // data.put("b", "b");
        // List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // Map<String, Object> data1 = new HashMap<String, Object>();
        // data1.put("c", "c1");
        // list.add(data1);
        // data1 = new HashMap<String, Object>();
        // data1.put("c", "c2");
        // list.add(data1);
        // data.put("list", list);
        // try {
        // YamlUtil.writerYamlFile(data, filePath);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // // yaml解析
        // String yamlFilePath = "D:/Worker/paas最新文件/paas7-new/AssetMeta.yaml";
        // Map<String, String> mapYaml = null;
        // try {
        // mapYaml = YamlUtil.readYamlFileDiy(yamlFilePath);
        // } catch (FileNotFoundException e) {
        // // Auto-generated catch block
        // e.printStackTrace();
        // }
        // System.out.println(mapYaml);
        // HttpGet httpGet = new HttpGet();
        // httpGet.setPath("http://123456");
        // httpGet.setPort(8080);
        // httpGet.setHost("nl-1");
        // HttpHeader httpHeader = new HttpHeader();
        // httpHeader.setName("name1");
        // httpHeader.setValue("value1");
        // HttpHeader httpHeader2 = new HttpHeader();
        // httpHeader.setName("name2");
        // httpHeader.setValue("value2");
        // List<HttpHeader> httpHeaders = new ArrayList<HttpHeader>();
        // httpHeaders.add(httpHeader);
        // httpHeaders.add(httpHeader2);
        // httpGet.setHttpHeaders(httpHeaders);
        // LivenessProbe livenessProbe = new LivenessProbe();
        // livenessProbe.setHttpGet(httpGet);
        // livenessProbe.setInitialDelaySeconds(11);
        // livenessProbe.setSuccessThreshold(22);
        // SecurityContext securityContext = new SecurityContext();
        // securityContext.setPrivileged(true);
        // livenessProbe.setSecurityContext(securityContext);
        // Exec exec = new Exec();
        // exec.setCommand(new String[] {"11111","22222"});
        // livenessProbe.setExec(exec);
        //
        // System.out.println(livenessProbe);
        // System.out.println("===");
        // Yaml yaml = new Yaml();
        //// String strYaml = yaml.dump(livenessProbe);
        //// System.out.println(yaml.dump(livenessProbe));
        //// System.out.println(yaml.dumpAsMap(livenessProbe));
        // System.out.println(YamlUtil.toYaml(livenessProbe));
        // System.out.println("===");
        // String strYaml = "exec: null\r\n" +
        // "failureThreshold: null\r\n" +
        // "httpGet:\r\n" +
        // " host: nl-1\r\n" +
        // " httpHeaders:\r\n" +
        // " - name: name2\r\n" +
        // " value: value2\r\n" +
        // " - name: null\r\n" +
        // " value: null\r\n" +
        // " path: http://123456\r\n" +
        // " port: 8080\r\n" +
        // " scheme: HTTP\r\n" +
        // "initialDelaySeconds: 11\r\n" +
        // "periodSeconds: 0\r\n" +
        // "securityContext:\r\n" +
        // " privileged: true\r\n" +
        // "successThreshold: 22\r\n" +
        // "tcpSocket: null\r\n" +
        // "timeoutSeconds: 0";
        //// LivenessProbe livenessProbe2 = yaml.loadAs(strYaml, LivenessProbe.class);
        // LivenessProbe livenessProbe2 = YamlUtil.toObj(strYaml, LivenessProbe.class);
        // System.out.println(livenessProbe2);
    }
}
