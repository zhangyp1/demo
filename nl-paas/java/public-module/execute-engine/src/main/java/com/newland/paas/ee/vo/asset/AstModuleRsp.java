package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 资产模块
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstModuleRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6572460319891964059L;

    // 模块标识
    private Long moduleId;
    // 模块名称
    private String moduleName;
    // 模块显示名称
    private String moduleShowName;
    // 模块版本
    private String moduleVersion;
    // 模块类型
    private String moduleType;
    // 模块描述
    private String moduleDesc;
    // 模块路径
    private String modulePath;
    // 模块保存地址
    private String moduleSavePath;
    // 文件路径
    private String fileUrl;
    // 镜像id
    private Long imageId;
    // exec：shell脚本方式 httpGet：http方式
    private String hookStartType;
    // IP地址（httpGet方式生效）
    private String hookStartHost;
    // 端口（httpGet方式生效）
    private Long hookStartPort;
    // 地址（httpGet方式生效）
    private String hookStartUrl;
    // 模块启动后执行的内容，由HOOK_START的几个生成
    private String postStart;
    // 执行命令(command方式生效)
    private String hookStartCommand;
    // exec：shell脚本方式 httpGet：http方式
    private String hookStopType;
    // IP地址（httpGet方式生效）
    private String hookStopHost;
    // 端口（httpGet方式生效）
    private Long hookStopPort;
    // 地址（httpGet方式生效）
    private String hookStopUrl;
    // 模块停止前执行的内容，由HOOK_STOP的几个生成
    private String preStop;
    // 执行命令(command方式生效)
    private String hookStopCommand;
    // 单元包含的模块
    private List<AstModuleMountRsp> moduleMounts = Collections.emptyList();

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleShowName() {
        return moduleShowName;
    }

    public void setModuleShowName(String moduleShowName) {
        this.moduleShowName = moduleShowName;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public String getModuleSavePath() {
        return moduleSavePath;
    }

    public void setModuleSavePath(String moduleSavePath) {
        this.moduleSavePath = moduleSavePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getHookStartType() {
        return hookStartType;
    }

    public void setHookStartType(String hookStartType) {
        this.hookStartType = hookStartType;
    }

    public String getHookStartHost() {
        return hookStartHost;
    }

    public void setHookStartHost(String hookStartHost) {
        this.hookStartHost = hookStartHost;
    }

    public Long getHookStartPort() {
        return hookStartPort;
    }

    public void setHookStartPort(Long hookStartPort) {
        this.hookStartPort = hookStartPort;
    }

    public String getHookStartUrl() {
        return hookStartUrl;
    }

    public void setHookStartUrl(String hookStartUrl) {
        this.hookStartUrl = hookStartUrl;
    }

    public String getPostStart() {
        return postStart;
    }

    public void setPostStart(String postStart) {
        this.postStart = postStart;
    }

    public String getHookStartCommand() {
        return hookStartCommand;
    }

    public void setHookStartCommand(String hookStartCommand) {
        this.hookStartCommand = hookStartCommand;
    }

    public String getHookStopType() {
        return hookStopType;
    }

    public void setHookStopType(String hookStopType) {
        this.hookStopType = hookStopType;
    }

    public String getHookStopHost() {
        return hookStopHost;
    }

    public void setHookStopHost(String hookStopHost) {
        this.hookStopHost = hookStopHost;
    }

    public Long getHookStopPort() {
        return hookStopPort;
    }

    public void setHookStopPort(Long hookStopPort) {
        this.hookStopPort = hookStopPort;
    }

    public String getHookStopUrl() {
        return hookStopUrl;
    }

    public void setHookStopUrl(String hookStopUrl) {
        this.hookStopUrl = hookStopUrl;
    }

    public String getPreStop() {
        return preStop;
    }

    public void setPreStop(String preStop) {
        this.preStop = preStop;
    }

    public String getHookStopCommand() {
        return hookStopCommand;
    }

    public void setHookStopCommand(String hookStopCommand) {
        this.hookStopCommand = hookStopCommand;
    }

    public List<AstModuleMountRsp> getModuleMounts() {
        return moduleMounts;
    }

    public void setModuleMounts(List<AstModuleMountRsp> moduleMounts) {
        this.moduleMounts = moduleMounts;
    }

}
