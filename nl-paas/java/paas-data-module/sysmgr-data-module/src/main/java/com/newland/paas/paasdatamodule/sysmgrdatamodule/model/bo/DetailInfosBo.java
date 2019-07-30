package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-08-07 19:28
 **/
@JsonIgnoreProperties(value = {"handler"})
public class DetailInfosBo {
    /**
     * title : 初始化租户数据
     * startTime : 2010-02-02 15:15:15
     * endTime : 2010-02-02 15:15:20
     * statusCode : 1
     * status : 已完成
     */

    private String title;
    private String startTime;
    private String endTime;
    private String statusCode;
    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
