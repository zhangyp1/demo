package com.newland.paas.ee.vo;

import java.util.List;

/**
 * 进度明细结果
 * 
 * @author ai
 *
 */
public class ProgressDetailResult {
	/**
	 * 操作对象
	 */
	private OperateResult operateResult;
	/**
	 * 阶段结果列表
	 */
	private List<StageResult> stageResultList;
	/**
	 * 构建结果
	 */
	private BuildResult buildResult;

	public OperateResult getOperateResult() {

		return operateResult;
	}

	public void setOperateResult(OperateResult operateResult) {

		this.operateResult = operateResult;
	}

	public List<StageResult> getStageResultList() {

		return stageResultList;
	}

	public void setStageResultList(List<StageResult> stageResultList) {

		this.stageResultList = stageResultList;
	}

	public BuildResult getBuildResult() {

		return buildResult;
	}

	public void setBuildResult(BuildResult buildResult) {

		this.buildResult = buildResult;
	}

	@Override
	public String toString() {
		return "ProgressDetailResult [operateResult=" + operateResult
				+ ", stageResultList=" + stageResultList + ", buildResult="
				+ buildResult + "]";
	}

}
