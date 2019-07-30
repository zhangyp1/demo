package com.newland.paas.paastool.vo.frontend;

import java.util.List;
import java.util.Map;

public class Frontend {
    private String name;
    /**
     * 访问端点
     */
    private List<String> entryPoints;
    /**
     * 后台
     */
    private String backend;
    /**
     * 路由规则
     */
    private Map<String, Route> routes;
    /**
     * 优先级
     */
    private Integer priority = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<String> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}
