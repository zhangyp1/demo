package com.newland.paas.ee.vo;

/**
 * 租户实例详细信息
 * @author ai
 *
 */
public class TenantInstanceDetailInfo {
    private long tenantId;               //租户ID
    private String tenantName;          //租户名称
    private String imageProject;     //租户镜像仓库地址
    private String imageUsername;
    private String imagePassword;
    private String astAddress;     //租户资产仓库地址
    private String astUsername;
    private String astPassword;

    private String imageProjectDir;     //租户镜像仓库地址,只有目录，没有ip和port，提供给prometheus和fluentd使用
    //private String tenantHarborPath;    //租户Harbor仓库地址

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getImageProject() {
        return imageProject;
    }

    public void setImageProject(String imageProject) {
        this.imageProject = imageProject;
    }

    public String getAstAddress() {
        return astAddress;
    }

    public void setAstAddress(String astAddress) {
        this.astAddress = astAddress;
    }

    public String getImageUsername() {
        return imageUsername;
    }

    public void setImageUsername(String imageUsername) {
        this.imageUsername = imageUsername;
    }

    public String getImagePassword() {
        return imagePassword;
    }

    public void setImagePassword(String imagePassword) {
        this.imagePassword = imagePassword;
    }

    public String getAstUsername() {
        return astUsername;
    }

    public void setAstUsername(String astUsername) {
        this.astUsername = astUsername;
    }

    public String getAstPassword() {
        return astPassword;
    }

    public void setAstPassword(String astPassword) {
        this.astPassword = astPassword;
    }

    public String getImageProjectDir() {
        return imageProjectDir;
    }

    public void setImageProjectDir(String imageProjectDir) {
        this.imageProjectDir = imageProjectDir;
    }

    /*public String getTenantHarborPath() {
        return tenantHarborPath;
    }

    public void setTenantHarborPath(String tenantHarborPath) {
        this.tenantHarborPath = tenantHarborPath;
    }*/
}
