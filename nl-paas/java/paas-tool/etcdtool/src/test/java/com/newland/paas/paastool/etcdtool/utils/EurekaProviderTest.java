package com.newland.paas.paastool.etcdtool.utils;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.newland.paas.paastool.eureka.vo.Application;
import com.newland.paas.paastool.eureka.vo.Applications;
import com.newland.paas.paastool.eureka.vo.EurekaApp;
import com.newland.paas.paastool.eureka.vo.InstanceInfo;
import com.newland.paas.paastool.vo.Provider;

public class EurekaProviderTest {

    @Test
    public void testCreate() {
        Provider provider = getProvider();
        assertNotNull(provider.getBackends());
    }

    private Provider getProvider() {
        EurekaApp eurekaApp = new EurekaApp();
        Applications Applications = new Applications();
        List<Application> apps = new ArrayList<>();
        Application Application = new Application();
        apps.add(Application);
        List<InstanceInfo> instanceInfos = new ArrayList<>();
        InstanceInfo InstanceInfo = new InstanceInfo();
        InstanceInfo.PortWrapper PortWrapper = new InstanceInfo.PortWrapper(true, 8080);
        InstanceInfo.PortWrapper PortWrapper2 = new InstanceInfo.PortWrapper(false, 443);
        instanceInfos.add(InstanceInfo);
        eurekaApp.setApplications(Applications);
        Applications.setApplication(apps);
        Application.setName("");
        Application.setInstance(instanceInfos);

        InstanceInfo.setApp("APPMGR");
        InstanceInfo.setIpAddr("10.1.8.8");
        InstanceInfo.setPort(PortWrapper);
        InstanceInfo.setSecurePort(PortWrapper2);

        Provider provider = EurekaProvider.create(Applications);
        return provider;
    }

    @Test
    public void testSync() {
        Provider provider = getProvider();

        EurekaProvider.sync(provider, getProvider());
        assertNotNull(provider.getBackends());
    }

}
