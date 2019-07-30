package com.newland.paas.ee.vo;

/**
 * 依赖详细信息
 * @author ai
 *
 */
public class DependInstanceInfo {

	public static final int DEPEND_TYPE_APPLICATION = 1;
	public static final int DEPEND_TYPE_SERVICE = 2;

	public String  getDependName( ) {
		return dependName;
	}
	
	public void  setDependName( String dependName ) {
		this.dependName = dependName;
	}
	
	public int getDependType() {
		return dependType; 
	}
	
	public void setDependType(int dependType) {
		this.dependType = dependType;
	}
	
	public AppInstanceDetailInfo getAppInstanceDetailInfo() {
		return appInstanceDetailInfo ; 
	}
	
	public void setAppInstanceDetailInfo(AppInstanceDetailInfo appInstanceDetailInfo) {
		this.appInstanceDetailInfo = appInstanceDetailInfo;
	}

	public ServiceInstanceDetailInfo getServiceInstanceDetailInfo() {
		return serviceInstanceDetailInfo;
	}

	public void setServiceInstanceDetailInfo(ServiceInstanceDetailInfo serviceInstanceDetailInfo) {
		this.serviceInstanceDetailInfo = serviceInstanceDetailInfo;
	}

	public AssetDetailInfo getAssetDetailInfo() {
		return assetDetailInfo;
	}

	public void setAssetDetailInfo(AssetDetailInfo assetDetailInfo) {
		this.assetDetailInfo = assetDetailInfo;
	}

	private AppInstanceDetailInfo appInstanceDetailInfo;
	
	private ServiceInstanceDetailInfo serviceInstanceDetailInfo;
	
	private AssetDetailInfo assetDetailInfo;
	
	private String dependName;
	
	/**
	 * 依赖类型 1-应用 2-服务
	 */
	private int dependType;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DependInstanceInfo [appInstanceDetailInfo=");
		builder.append(appInstanceDetailInfo);
		builder.append(", serviceInstanceDetailInfo=");
		builder.append(serviceInstanceDetailInfo);
		builder.append(", assetDetailInfo=");
		builder.append(assetDetailInfo);
		builder.append(", dependName=");
		builder.append(dependName);
		builder.append(", dependType=");
		builder.append(dependType);
		builder.append("]");
		return builder.toString();
	}
	
	
}
