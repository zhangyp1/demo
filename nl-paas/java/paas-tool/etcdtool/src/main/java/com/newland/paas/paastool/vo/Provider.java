package com.newland.paas.paastool.vo;


import com.newland.paas.paastool.vo.backend.Backend;
import com.newland.paas.paastool.vo.frontend.Frontend;

import java.util.Map;

public class Provider {
    private Map<String, Backend> backends;
    private Map<String, Frontend> frontends;

    public Map<String, Backend> getBackends() {
        return backends;
    }

    public void setBackends(Map<String, Backend> backends) {
        this.backends = backends;
    }

    public Map<String, Frontend> getFrontends() {
        return frontends;
    }

    public void setFrontends(Map<String, Frontend> frontends) {
        this.frontends = frontends;
    }

}
