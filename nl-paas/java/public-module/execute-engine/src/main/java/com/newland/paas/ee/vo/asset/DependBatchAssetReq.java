package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.List;

/**
 * 依赖资产
 * 
 * @author cjw
 *
 */
public class DependBatchAssetReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7265616996822196030L;

	private Long tenantId;// 租户标识
	private List<DependBatchInfoAssetReq> depends;// 依赖资产列表

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public List<DependBatchInfoAssetReq> getDepends() {
		return depends;
	}

	public void setDepends(List<DependBatchInfoAssetReq> depends) {
		this.depends = depends;
	}

}
