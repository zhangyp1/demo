package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.List;

/**
 * 依赖资产
 * 
 * @author cjw
 *
 */
public class DependAssetRsp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8838526572865899547L;

	private List<Long> assetIds;// 资产标识数组

	public List<Long> getAssetIds() {
		return assetIds;
	}

	public void setAssetIds(List<Long> assetIds) {
		this.assetIds = assetIds;
	}

}
