package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年7月30日 下午6:25:32
 */
public class SysGroupRespBo extends SysGroup {

    private String creatorName;

    private Boolean bGroupAdmin;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Boolean getbGroupAdmin() {
        return bGroupAdmin;
    }

    public void setbGroupAdmin(Boolean bGroupAdmin) {
        this.bGroupAdmin = bGroupAdmin;
    }
}
