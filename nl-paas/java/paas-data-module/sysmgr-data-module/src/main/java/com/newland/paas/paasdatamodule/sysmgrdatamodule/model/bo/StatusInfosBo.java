package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-08-07 19:29
 **/
@JsonIgnoreProperties(value = {"handler"})
public class StatusInfosBo {
    /**
     * progress : 创建租户
     * description : 创建租户
     * step : 0
     * detailInfos : [{"title":"初始化租户数据","startTime":"2010-02-02 15:15:15","endTime":"2010-02-02 15:15:20","statusCode":"1","status":"已完成"},{"title":"创建租户仓库","startTime":"2010-02-02 15:15:15","endTime":"===================","statusCode":"2","status":"进行中"},{"title":"生成租户信息","startTime":"===================","endTime":"===================","statusCode":"0","status":"未开始"}]
     */

    private String progress;
    private String description;
    private int step;
    private List<DetailInfosBo> detailInfos;

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public List<DetailInfosBo> getDetailInfos() {
        return detailInfos;
    }

    public void setDetailInfos(List<DetailInfosBo> detailInfos) {
        this.detailInfos = detailInfos;
    }
}
