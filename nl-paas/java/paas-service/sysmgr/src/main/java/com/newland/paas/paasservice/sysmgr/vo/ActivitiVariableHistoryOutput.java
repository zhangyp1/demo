package com.newland.paas.paasservice.sysmgr.vo;

import java.util.List;

/**
 *
 */
public class ActivitiVariableHistoryOutput {

	private List<ActivitiVariableHistory> data;

	public List<ActivitiVariableHistory> getData() {
		return data;
	}

	public void setData(List<ActivitiVariableHistory> data) {
		this.data = data;
	}
	@Override
    public String toString() {
        return "ActivitiVariableHistoryOutput{"
				+ "data="
				+ data
				+ '}';
    }
}
