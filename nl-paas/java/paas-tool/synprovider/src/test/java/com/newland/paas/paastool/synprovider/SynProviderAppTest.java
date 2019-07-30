package com.newland.paas.paastool.synprovider;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.newland.paas.paastool.etcdtool.EtcdConnection;
import com.newland.paas.paastool.etcdtool.impl.EtcdConImpl;
import com.newland.paas.paastool.etcdtool.utils.ProviderOper;

import mockit.Mocked;

/**
 * Created with IntelliJ IDEA. Description: User: luolifeng Date: 2019-01-17 Time: 10:01
 */
public class SynProviderAppTest {

    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    @Mocked
    private EtcdConnection connection;
    @Mocked
    private EtcdConImpl etcdConImpl;
    @Mocked
    private ProviderOper providerOper;

    @BeforeClass
    public void beforeClass() {
        ReflectionTestUtils.setField(SynProviderApp.class, "restTemplate", restTemplate);

    }

    @Test(enabled = false)
    public void testMain() {
        String[] args = {"-traefik.endpoints", "http://192.168.11.20:18080", "-eureka.endpoints",
            "http://10.1.8.13:32001/eureka", "-etcd.endpoints", "https://192.168.11.63:32379", "-frwId", "1000",
            "-etcd.crt", "F:\\etcd.pem", "-etcd.key", "F:\\etcd-key.pem", "-etcd.ca", "F:\\ca.pem",};
        SynProviderApp.main(args);
    }

}