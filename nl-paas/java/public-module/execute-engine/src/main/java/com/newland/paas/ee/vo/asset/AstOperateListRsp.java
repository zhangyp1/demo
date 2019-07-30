package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

public class AstOperateListRsp implements Serializable {

	/**
	 * 资产操作列表
	 */
	private static final long serialVersionUID = -7329944098170222276L;

	//资产ID
	private Long assetId;
	//方法类型
	private String methodType;
	//方法名称
	private String methodName;
	
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	
}
