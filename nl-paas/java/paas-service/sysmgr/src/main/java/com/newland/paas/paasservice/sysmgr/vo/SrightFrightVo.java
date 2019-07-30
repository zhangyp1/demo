package com.newland.paas.paasservice.sysmgr.vo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupForObjRespBo;

import java.io.Serializable;
import java.util.List;

/**
 * @author WRP
 * @since 2018/8/7
 */
public class SrightFrightVo implements Serializable {

    private static final long serialVersionUID = -681786554232735312L;

    private List<GlbDict> operates;

    private List<SysGroupForObjRespBo> groups;

    public List<GlbDict> getOperates() {
        return operates;
    }

    public void setOperates(List<GlbDict> operates) {
        this.operates = operates;
    }

    public List<SysGroupForObjRespBo> getGroups() {
        return groups;
    }

    public void setGroups(List<SysGroupForObjRespBo> groups) {
        this.groups = groups;
    }
}
