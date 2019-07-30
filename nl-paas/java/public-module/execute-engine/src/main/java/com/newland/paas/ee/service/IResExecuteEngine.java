package com.newland.paas.ee.service;

import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.ProgressDetailResult;
import com.newland.paas.ee.vo.ResourceDetailInfo;

public interface IResExecuteEngine {
	
	/**
	 * 远程密钥分发
	 * @param installConfig				安装配置
	 * @param resourceDetailInfo		资源详细信息
	 * @param depotPath                 仓库地址
	 * @return OperateResult			操作返回结果，如：进度查询地址、Job名称、构建号等
	 */
	public OperateResult remoteCipherPublish(InstallerConfig installConfig, ResourceDetailInfo resourceDetailInfo, String depotPath);
	/**
	 * 获取执行过程描述
	 * @param operateResult				操作结果
	 * @return	ProgressDetailResult	进度明细信息
	 */
	public ProgressDetailResult getResExecuteDescribe(OperateResult operateResult);
	/**
	 * 获取执行结果信息	调用前提: job执行完成
	 * @param resourceDetailInfo		资源详细信息
	 * @param operateResult				操作结果
	 * @return	ResourceDetailInfo		返回信息
	 */
	public ResourceDetailInfo getResExecuteResult(ResourceDetailInfo resourceDetailInfo, OperateResult operateResult);
}