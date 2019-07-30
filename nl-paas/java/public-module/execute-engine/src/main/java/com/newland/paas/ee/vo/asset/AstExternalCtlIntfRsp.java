package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 资产外部控制接口
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstExternalCtlIntfRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8267467752491632025L;
    // 接口标识
    private Long interfaceId;
    // 接口分类
    private String intfCategory;
    // 方法名称
    private String methodName;
    // 显示名称
    private String showName;
    // 集群类型
    private String clusterType;
    // 方法类型
    private String methodType;
    // 可见性
    private String visibility;
    // 方法描述
    private String methodDesc;
    // 方法名称对应实际的文件名称
    private String methodFileName;
    // 归属目标
    private String belongTo;
    // 是否允许重试：0-否，1-是
    private Long allowRetry;
    // UNDO方法对应文件名
    private String undoFilename;
    // 执行类型
    private String executeType;
    // 执行目标
    private String executeTarget;
    // 方法参数
    private List<AstIntfParamRsp> parameters = Collections.emptyList();

    public Long getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Long interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getIntfCategory() {
        return intfCategory;
    }

    public void setIntfCategory(String intfCategory) {
        this.intfCategory = intfCategory;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String getMethodFileName() {
        return methodFileName;
    }

    public void setMethodFileName(String methodFileName) {
        this.methodFileName = methodFileName;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public Long getAllowRetry() {
        return allowRetry;
    }

    public void setAllowRetry(Long allowRetry) {
        this.allowRetry = allowRetry;
    }

    public String getUndoFilename() {
        return undoFilename;
    }

    public void setUndoFilename(String undoFilename) {
        this.undoFilename = undoFilename;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getExecuteTarget() {
        return executeTarget;
    }

    public void setExecuteTarget(String executeTarget) {
        this.executeTarget = executeTarget;
    }

    public List<AstIntfParamRsp> getParameters() {
        return parameters;
    }

    public void setParameters(List<AstIntfParamRsp> parameters) {
        this.parameters = parameters;
    }
}
