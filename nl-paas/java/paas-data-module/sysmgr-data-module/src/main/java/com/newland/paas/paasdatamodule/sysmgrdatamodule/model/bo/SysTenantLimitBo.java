package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantLimit;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

public class SysTenantLimitBo extends SysTenantLimit {
    // 资源配额单位
    private String resLimitUnit;
    // 资源项是否选中 0--未被选中 1--被选中
    private Integer itemChecked;

    public String getResLimitUnit() {
        return resLimitUnit;
    }

    public void setResLimitUnit(String resLimitUnit) {
        this.resLimitUnit = resLimitUnit;
    }

    public Integer getItemChecked() {
        return itemChecked;
    }

    public void setItemChecked(Integer itemChecked) {
        this.itemChecked = itemChecked;
    }
}