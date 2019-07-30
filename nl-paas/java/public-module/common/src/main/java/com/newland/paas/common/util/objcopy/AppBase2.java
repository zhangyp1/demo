package com.newland.paas.common.util.objcopy;

/**
 * 应用基本信息
 *
 * @author Administrator
 *
 */
public class AppBase2 {
	// 应用分类 字典：APP_CATEGORY 1 有状态应用,2 后台应用,3 微服务应用,4 调度应用
	private String appCategory;
	// 应用ID
	private Long appInfoId;
	// 名称
	private String appName;
	// 字典：APP_STATUS 新增 new,部署中 deploy,停止 stop,启动中 start,运行 run,停止中 stopping,卸载中
	// unloading,已卸载 unloaded
	private String appStatus;
	// 应用唯一标识,对外部子系统使用的id
	private String appCode;
	// 资产id
	private Long astId;
	// 资产名称
	private String astName1;
	// 版本
	private String astVer1;
	// 字典：APP_IMPORTANT 1 普通,2 重要
	private String important;
	// 图标路径
	private String logoUrl;
	// 系统组id
	private Long sysCategoryId;

	public String getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}

	public Long getAppInfoId() {
		return appInfoId;
	}

	public void setAppInfoId(Long appInfoId) {
		this.appInfoId = appInfoId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public Long getAstId() {
		return astId;
	}

	public void setAstId(Long astId) {
		this.astId = astId;
	}

	public String getImportant() {
		return important;
	}

	public void setImportant(String important) {
		this.important = important;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Long getSysCategoryId() {
		return sysCategoryId;
	}

	public void setSysCategoryId(Long sysCategoryId) {
		this.sysCategoryId = sysCategoryId;
	}

	public String getAstName1() {
		return astName1;
	}

	public void setAstName1(String astName1) {
		this.astName1 = astName1;
	}

	public String getAstVer1() {
		return astVer1;
	}

	public void setAstVer1(String astVer1) {
		this.astVer1 = astVer1;
	}
}
