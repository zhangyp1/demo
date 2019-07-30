package com.newland.paas.paastool.etcdtool.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.newland.paas.paastool.eureka.vo.Application;
import com.newland.paas.paastool.eureka.vo.Applications;
import com.newland.paas.paastool.eureka.vo.InstanceInfo;
import com.newland.paas.paastool.vo.Provider;
import com.newland.paas.paastool.vo.backend.Backend;
import com.newland.paas.paastool.vo.backend.CircuitBreaker;
import com.newland.paas.paastool.vo.backend.LoadBalancer;
import com.newland.paas.paastool.vo.backend.Server;
import com.newland.paas.paastool.vo.frontend.Frontend;
import com.newland.paas.paastool.vo.frontend.Route;

public class EurekaProvider {
    private static final String PREFIX_BACKEND = "backend-";
    private static final String PREFIX_FRONTEND = "frontend-";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new LoadBalancer();
    private static final CircuitBreaker DEFAULT_CIRCUIT_BREAKER = new CircuitBreaker();
    private static final List<String> DEFAULT_ENTRY_POINTS = new ArrayList<>();
    static {
        DEFAULT_LOAD_BALANCER.setMethod("drr");
        DEFAULT_CIRCUIT_BREAKER.setExpression("NetworkErrorRatio() > 0.5");
        DEFAULT_ENTRY_POINTS.add(HTTP);
    }

    public static Provider create(Applications apps) {
        Provider provider = new Provider();
        Map<String, Backend> backends = new HashMap<>();
        Map<String, Frontend> frontends = new HashMap<>();
        List<Application> appList = apps.getApplication();
        if (!CollectionUtils.isEmpty(appList)) {
            appList.forEach(app -> {
                // 构建后台
                Backend backend = new Backend();
                backend.setName(PREFIX_BACKEND + app.getName());
                backend.setServers(getServers(app.getInstance()));
                backend.setLoadBalancer(DEFAULT_LOAD_BALANCER);
                backend.setCircuitBreaker(DEFAULT_CIRCUIT_BREAKER);
                backends.put(backend.getName(), backend);
                // 构建前台
                Frontend frontend = new Frontend();
                frontend.setName(PREFIX_FRONTEND + app.getName());
                frontend.setBackend(PREFIX_BACKEND + app.getName());
                frontend.setEntryPoints(DEFAULT_ENTRY_POINTS);
                frontend.setRoutes(getRoutes(app));
                frontends.put(frontend.getName(), frontend);
            });
        }
        provider.setBackends(backends);
        provider.setFrontends(frontends);
        return provider;
    }

    private static Map<String, Route> getRoutes(Application app) {

        Map<String, Route> routes = new HashMap<>();
        Route route = new Route();
        route.setName("route-host-" + app.getName());
        route.setRule("Host:" + app.getName().toLowerCase());
        routes.put(route.getName(), route);

        return routes;
    }

    private static Map<String, Server> getServers(List<InstanceInfo> instances) {

        Map<String, Server> servers = new HashMap<>();
        instances.forEach(instance -> {
            Server server = new Server();
            server.setName("server-" + getInstanceID(instance));
            server.setWeight(1);
            server.setUrl(getProtocol(instance) + "://" + instance.getIpAddr() + ":" + getPort(instance));
            servers.put(server.getName(), server);
        });
        return servers;
    }

    private static String getInstanceID(InstanceInfo instance) {
        return normalize(instance.getIpAddr()) + '-' + getPort(instance);
    }

    private static String getPort(InstanceInfo instance) {
        if (instance.getSecurePort().isEnabled()) {
            return String.valueOf(instance.getSecurePort().getPort());
        }
        return String.valueOf(instance.getPort().getPort());
    }

    private static String getProtocol(InstanceInfo instance) {
        if (instance.getSecurePort().isEnabled()) {
            return HTTPS;
        }
        return HTTP;
    }

    private static String normalize(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        return string.replace(".", "-");
    }

    /**
     * 同步eureka数据到etcd
     * 
     * @param etcdProvider
     * @param eurekaProvider
     * @return
     */
    public static Provider sync(Provider etcdProvider, Provider eurekaProvider) {
        if(etcdProvider.getFrontends()==null){
            etcdProvider.setFrontends(new HashMap<>());
        }
        if(etcdProvider.getBackends()==null){
            etcdProvider.setBackends(new HashMap<>());
        }

        Map<String, Frontend> etcdFrontends = etcdProvider.getFrontends();
        Map<String, Frontend> eurekaFrontends = eurekaProvider.getFrontends();

        syncFrontendMap(etcdFrontends, eurekaFrontends);

        Map<String, Backend> etcdBackends = etcdProvider.getBackends();
        Map<String, Backend> eurekaBackends = eurekaProvider.getBackends();

        syncBackendMap(etcdBackends, eurekaBackends);
        return etcdProvider;
    }

    private static void syncBackend(Backend etcdBackend, Backend eurekaBackend) {
        etcdBackend.getServers().clear();
        etcdBackend.getServers().putAll(eurekaBackend.getServers());
    }

    private static void syncBackendMap(Map<String, Backend> etcdBackends, Map<String, Backend> eurekaBackends) {
        if (CollectionUtils.isEmpty(eurekaBackends)) {
            return;
        }
        //服务器列表更新
        eurekaBackends.forEach((k, v) -> {
            // etcd没有此配置，则添加
            if (!etcdBackends.containsKey(k)) {
                etcdBackends.put(k, v);
            } else {
                Backend etcdBackend = etcdBackends.get(k);
                syncBackend(etcdBackend, v);
            }
        });

        //下线或暂退的服务器，从etcd库中移除
        etcdBackends.forEach((k, v) -> {
            // eureka没有此配置，则移除服务器列表
            if (!eurekaBackends.containsKey(k)) {
                 v.getServers().clear();

            }
        });
    }

    /**
     * 同步eureka的前端到etcd
     * 
     * @param etcdFrontends
     * @param eurekaFrontends
     * @return
     */
    private static void syncFrontendMap(Map<String, Frontend> etcdFrontends, Map<String, Frontend> eurekaFrontends) {
        if (CollectionUtils.isEmpty(eurekaFrontends)) {
            return;
        }
        eurekaFrontends.forEach((k, v) -> {
            // etcd没有此配置，则添加
            if (!etcdFrontends.containsKey(k)) {
                etcdFrontends.put(k, v);
            }
        });

    }

}
