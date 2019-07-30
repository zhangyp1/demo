package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysCategoryVO;

import java.io.Serializable;
import java.util.List;

/**
 * 系统分组列表
 *
 * @author zhongqingjiang
 */
public class SysCategoryListByTenantVo implements Serializable {

    private Long tenantId;

    private List<SysCategoryVO> sysCategoryList;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public List<SysCategoryVO> getSysCategoryList() {
        return sysCategoryList;
    }

    public void setSysCategoryList(List<SysCategoryVO> sysCategoryList) {
        this.sysCategoryList = sysCategoryList;
    }
}
