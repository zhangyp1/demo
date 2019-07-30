package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

public class SysUrlRole implements Serializable {
    private static final long serialVersionUID = 666061322777035728L;

    private Long urlRoleId;

    private Long roleId;

    private String url;

    private Date createDate;

    private Date changeDate;

    private Long creatorId;

    public Long getUrlRoleId() {
        return urlRoleId;
    }

    public void setUrlRoleId(Long urlRoleId) {
        this.urlRoleId = urlRoleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}