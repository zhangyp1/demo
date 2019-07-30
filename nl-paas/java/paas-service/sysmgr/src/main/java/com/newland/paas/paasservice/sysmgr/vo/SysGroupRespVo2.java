package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;

/**
 * 工组Vo
 *
 * @author zhongqingjiang
 */
public class SysGroupRespVo2 extends SysGroupRespBo {

    public Boolean bCategoryOwner;

    public Boolean getbCategoryOwner() {
        return bCategoryOwner;
    }

    public void setbCategoryOwner(Boolean bCategoryOwner) {
        this.bCategoryOwner = bCategoryOwner;
    }
}
