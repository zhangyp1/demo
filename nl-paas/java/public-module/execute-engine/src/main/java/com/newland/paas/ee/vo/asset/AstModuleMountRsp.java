package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产模块日志
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstModuleMountRsp implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 719346864066179679L;

    // 资产模块ID
    private Long moduleId;
    // 资产ID
    private Long assetId;
    // 挂载类型：log（目前只有这种类型）
    private String type;
    // 挂载路径
    private String path;
    // 挂载策略：type为log：app/trace/pf/idc/other 日志策略（应用日志/过程日志/性能日志/指标日志/其他日志）
    private String strategy;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

}
