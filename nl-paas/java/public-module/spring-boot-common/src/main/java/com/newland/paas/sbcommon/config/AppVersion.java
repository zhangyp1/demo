package com.newland.paas.sbcommon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import com.newland.paas.common.util.DateUtils;

/**
 * 应用版本信息
 * 
 * @author SongDi
 * @date 2018/11/13
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppVersion {

    @Autowired(required = false)
    private BuildProperties buildProperties;
    private String version;
    private String timestamp;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        if (buildProperties != null) {

            return DateUtils.formatDate(buildProperties.getTime(), DateUtils.DATE_FULL_FORMAT);
        }
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}