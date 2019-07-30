package com.newland.paas.paastool.vo.backend;

import java.util.Map;

public class Backend {
    private String name;
    /**
     * 断路器
     */
    private CircuitBreaker circuitBreaker;
    /**
     * 连接数配置
     */
    private MaxConn maxConn;
    /**
     * 服务器列表
     */
    private Map<String, Server> servers;
    /**
     * 负载均衡器
     */
    private LoadBalancer loadBalancer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public void setCircuitBreaker(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public MaxConn getMaxConn() {
        return maxConn;
    }

    public void setMaxConn(MaxConn maxConn) {
        this.maxConn = maxConn;
    }

    public Map<String, Server> getServers() {
        return servers;
    }

    public void setServers(Map<String, Server> servers) {
        this.servers = servers;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

}
