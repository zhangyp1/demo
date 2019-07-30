package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryCount;

import java.io.Serializable;
import java.util.List;

/**
 * 系统分组统计
 *
 * @author zhongqingjiang
 */
public class SysCategoryStatisticVo implements Serializable {

    private List<SysCategoryCount> sysCategoryCount;

    public List<SysCategoryCount> getSysCategoryCount() {
        return sysCategoryCount;
    }

    public void setSysCategoryCount(List<SysCategoryCount> sysCategoryCount) {
        this.sysCategoryCount = sysCategoryCount;
    }
}
