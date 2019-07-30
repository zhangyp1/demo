package com.newland.paas.sbcommon.activiti.vo;

import java.util.List;

/**
 * 历史流程变量
 *
 * @author WRP
 * @since 2019/7/24
 */
public class ProcessInstancesVariableHistory {

    private List<VariableHistory> data;

    public List<VariableHistory> getData() {
        return data;
    }

    public void setData(List<VariableHistory> data) {
        this.data = data;
    }
}
