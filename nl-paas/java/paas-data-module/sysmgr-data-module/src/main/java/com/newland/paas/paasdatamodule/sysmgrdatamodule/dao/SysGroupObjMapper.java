package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupObjBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysObjFrightBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysObjSrightBO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysGroupObjMapper {
    int countBySelective(SysGroupObj record);

    List<SysGroupObj> selectBySelective(SysGroupObj record);

    List<SysGroupObjBO> selectDetailBySelective(@Param(value = "params") SysGroupObjBO params, @Param(value = "list") List<Long> groupIds);

    List<SysGroupObjBO> selectVSysObjDetailBySelective(@Param(value = "params") SysGroupObjBO params,
                                                       @Param(value = "groupIds") List<Long> groupIds,
                                                       @Param(value = "objTypes") List<String> objTypes);

    List<SysGroupObj> selectInGroupIds(@Param(value = "params") SysGroupObj params, @Param(value = "list") List<Long> groupIds);

    List<SysObjFrightBO> selectGroupRoleOperates(@Param(value = "userId") Long userId, @Param(value = "objId") Long objId);

    List<SysGroupObjBO> getGroupObjDetaillist(@Param(value = "objId") Long objId,@Param(value = "groupId") Long groupId);
}