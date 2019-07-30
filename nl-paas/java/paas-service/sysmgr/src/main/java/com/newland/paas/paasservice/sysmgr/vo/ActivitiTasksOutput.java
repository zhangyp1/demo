package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenshen
 * @Description com.newland.paas.paasservice.svrmgr.vo.ActivitiTasksOutput
 * @Date 2018/8/6
 */
public class ActivitiTasksOutput implements Serializable {
    private List<ActivitiTasksObj> data;

    public List<ActivitiTasksObj> getData() {
        return data;
    }

    public void setData(List<ActivitiTasksObj> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ActivitiTasksOutput{"
                + "data=" + data
                + '}';
    }
}
