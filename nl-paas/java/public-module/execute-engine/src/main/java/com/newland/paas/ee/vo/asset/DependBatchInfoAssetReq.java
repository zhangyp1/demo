package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 依赖资产
 * 
 * @author cjw
 *
 */
public class DependBatchInfoAssetReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8697662755611497147L;

	private String assetName;// 资产名称
	private String assetVersionRange;// 资产版本范围

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAssetVersionRange() {
		return assetVersionRange;
	}

	public void setAssetVersionRange(String assetVersionRange) {
		this.assetVersionRange = assetVersionRange;
	}

}
