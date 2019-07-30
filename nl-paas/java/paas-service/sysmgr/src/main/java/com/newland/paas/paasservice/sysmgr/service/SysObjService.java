package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupObjBO;
import com.newland.paas.paasservice.sysmgr.vo.SrightFrightReqVo;
import com.newland.paas.paasservice.sysmgr.vo.SrightFrightVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 对象权限
 *
 * @author WRP
 * @since 2018/7/31
 */
public interface SysObjService {

    /**
     * 分页查询工组对象列表，从v_sys_obj
     *
     * @param pageInfo
     * @param params
     * @return
     */
    ResultPageData<SysGroupObjBO> pageViewByParams(PageInfo pageInfo, SysGroupObjBO params);

    /**
     * 查询对象详情
     *
     * @param objId
     * @return
     */
    SysGroupObjBO getGroupObjDetail(Long objId);

    /**
     * 根据objId和groupId列表获取对应的授权与赋权
     *
     * @param objId
     * @param groupIds
     * @return
     */
    SrightFrightVo getGroupAndGroupRole(Long objId, List<Long> groupIds);

    /**
     * 对象授权&赋权
     *
     * @param objId
     * @param srightFrights
     */
    void srightFright(Long objId, List<SrightFrightReqVo> srightFrights);

    /**
     * @描述:获取对象修改是已授权信息
     * @方法名: getGroupObjDetaillist
     * @param objId
     * @param groupId
     * @return
     * @创建人 zhang
     * @创建时间 2019-05-26
     */
    List<SysGroupObjBO> getGroupObjDetaillist(Long objId,Long groupId);

    void deleteSrightByGroupId(Long groupId);

    void deleteFrightByGroupRoleId(Long groupRoleId);

}
