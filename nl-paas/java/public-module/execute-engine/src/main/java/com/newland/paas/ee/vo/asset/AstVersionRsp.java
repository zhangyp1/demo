package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName:AstVersionRsp
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 * 
 * @author kangweixiang
 * @version
 * @Date 2018年6月25日 上午9:10:19
 * 
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public class AstVersionRsp implements Serializable {
    private static final long serialVersionUID = 1L;

    private String assetId;

    private String assetName;

    private String assetVersion;

    public String getAssetId() {

        return assetId;
    }

    public void setAssetId(String assetId) {

        this.assetId = assetId;
    }

    public String getAssetName() {

        return assetName;
    }

    public void setAssetName(String assetName) {

        this.assetName = assetName;
    }

    public String getAssetVersion() {

        return assetVersion;
    }

    public void setAssetVersion(String assetVersion) {

        this.assetVersion = assetVersion;
    }

    @Override
    public String toString() {
        return "AstVersionRsp [assetId=" + assetId + ", assetName=" + assetName
                + ", assetVersion=" + assetVersion + "]";
    }

}
