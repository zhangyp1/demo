package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.List;

/**
 * 资产请求对象
 * 
 * @author cjw
 *
 */
public class AssetIdsRsp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7983732948099639692L;

	private List<Long> assetIds;// 资产标识数组

	public List<Long> getAssetIds() {
		return assetIds;
	}

	public void setAssetIds(List<Long> assetIds) {
		this.assetIds = assetIds;
	}

}
