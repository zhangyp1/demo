package com.newland.paas.ee;

import com.newland.paas.ee.health.HealthCheck;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthCheckTest {
    //@Test
    public void testHealth() {
        try {
            HealthCheck healthCheck = new HealthCheck("http://192.168.59.3:8080", "root", "system");
            System.out.println(healthCheck.health2machine("192.168.59.2"));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
    //@Test
    public void testModiHost() {
        try {
            HealthCheck healthCheck = new HealthCheck("http://10.1.8.13:9999", "admin", "taasadmin");
            Map<String, String> mapIps = new IdentityHashMap<>();
            mapIps.put(new String("10.1.8.16"), "ansible00");
            mapIps.put(new String("10.1.8.16"), "ansible01");
            System.out.println(healthCheck.modihost2machine("10.1.8.34", mapIps));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        Map<String, String> mapIps = new IdentityHashMap<>();
//        String h1 = getHost("http://192.168.59.3/abc");
//        String h2 = getHost("http://192.168.59.3/abc");
//        mapIps.put(h1, "ansible00");
//        mapIps.put(h2, "ansible01");
//        mapIps.forEach((ip, host)->{
//            System.out.println(ip + ":" + host);
//        });

        HealthCheckTest test = new HealthCheckTest();
        test.testModiHost();

//        System.out.println(getHost("10.1.8.3:a"));
    }

//    private static String getHost(String url) {
//        Pattern p = Pattern.compile("://(.*?)[:|/]");
//        Matcher m = p.matcher(url);
//        while(m.find()){
//           return m.group(1);
//        }
//
//        return new String("");
//    }

    private static String getHost(String url) {
        Pattern p = Pattern.compile("(.*):(.*)");
        Matcher m = p.matcher(url);
        while(m.find()){
            return m.group(1);
        }

        return new String(url);
    }
}
