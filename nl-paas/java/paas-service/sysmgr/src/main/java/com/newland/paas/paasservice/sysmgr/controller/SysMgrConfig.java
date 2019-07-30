/*
 *
 */
package com.newland.paas.paasservice.sysmgr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年8月7日 下午5:00:03
 */
@Component
@ConfigurationProperties(prefix = "sysMgrConfig")
public class SysMgrConfig {
    private List<String> docApps = new ArrayList<>();

    public List<String> getDocApps() {
        return docApps;
    }

    public void setDocApps(List<String> docApps) {
        this.docApps = docApps;
    }


}
