package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @program: paas-all
 * @description: ${description}
 * @author: Frown
 * @create: 2018-07-25 10:25
 **/
@JsonIgnoreProperties(value = {"handler"})
public class CreateTenantDetail {

    /**
     * activeStep : 0
     * statusInfos : [{"progress":"创建租户","description":"创建租户","step":0,"detailInfos":[{"title":"初始化租户数据","startTime":"2010-02-02 15:15:15","endTime":"2010-02-02 15:15:20","statusCode":"1","status":"已完成"},{"title":"创建租户仓库","startTime":"2010-02-02 15:15:15","endTime":"===================","statusCode":"2","status":"进行中"},{"title":"生成租户信息","startTime":"===================","endTime":"===================","statusCode":"0","status":"未开始"}]},{"progress":"完成","description":"","step":1,"detailInfos":[]}]
     */

    private int activeStep;
    private List<StatusInfosBo> statusInfos;

    public int getActiveStep() {
        return activeStep;
    }

    public void setActiveStep(int activeStep) {
        this.activeStep = activeStep;
    }

    public List<StatusInfosBo> getStatusInfos() {
        return statusInfos;
    }

    public void setStatusInfos(List<StatusInfosBo> statusInfos) {
        this.statusInfos = statusInfos;
    }
}
