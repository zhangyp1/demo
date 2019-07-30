package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对象视图
 *
 * @author zhongqingjiang
 */
public interface SysObjViewMapper {

    // V_SYS_OBJ
    List<SysObj> selectObjBySelective(SysObj record);
    int countBySelectiveSysObj(SysObj record);

    // V_SYS_OBJ_NOT_GRANT
    List<SysObjNotGrant> selectObjNotGrantBySelective(SysObjNotGrant record);
    int countObjNotGrantBySelective(SysObjNotGrant record);

    // V_SYS_OBJ_GRANT
    List<SysObjGrant> selectObjGrantBySelective(SysObjGrant record);
    int countObjGrantBySelective(SysObjGrant record);

    // V_SYS_OBJ_EMPOWER
    List<SysObjEmpower> selectObjEmpowerBySelective(SysObjEmpower record);
    int countObjEmpowerBySelective(SysObjEmpower record);

    // V_SYS_USER_GROUP_OBJ
    List<SysUserObj> selectUserGroupObjBySelective(SysUserObj record);
    List<SysUserObj> selectUserGroupObjByCategoryAndUser(@Param("sysCategoryId") Long sysCategoryId,
                                                         @Param("userId") Long userId,
                                                         @Param("likeObjName") String likeObjName,
                                                         @Param("objType") String objType);
    List<SysUserObj> selectUserGroupObjByCategoryAndUser_Merged(@Param("sysCategoryId") Long sysCategoryId,
                                                                @Param("userId") Long userId,
                                                                @Param("likeObjName") String likeObjName,
                                                                @Param("objType") String objType,
                                                                @Param("inObjTypes") List<String> inObjTypes);

    // V_SYS_USER_GRANT_OBJ
    List<SysUserObj> selectUserGrantObjBySelective(SysUserObj record);
    List<Long> selectUserGrantObjCategoryId(@Param("userId") Long userId,
                                            @Param("tenantId") Long tenantId,
                                            @Param("notInSysCategoryIds") List<Long> notInSysCategoryIds,
                                            @Param("objType") String objType);
    List<SysUserObj> selectUserGrantObjByCategoryAndUser(@Param("sysCategoryId") Long sysCategoryId,
                                                         @Param("userId") Long userId,
                                                         @Param("likeObjName") String likeObjName,
                                                         @Param("objType") String objType);
    List<SysUserObj> selectUserGrantObjByCategoryAndUser_Merged(@Param("sysCategoryId") Long sysCategoryId,
                                                         @Param("userId") Long userId,
                                                         @Param("likeObjName") String likeObjName,
                                                         @Param("objType") String objType);

    // V_SYS_USER_OBJ
    List<SysUserObj> selectUserObjBySelective(SysUserObj record);
    List<SysUserObj> selectUserObjByCategoryAndUser_Merged(@Param("sysCategoryId") Long sysCategoryId,
                                                                @Param("userId") Long userId,
                                                                @Param("likeObjName") String likeObjName,
                                                                @Param("objType") String objType,
                                                                @Param("inObjTypes") List<String> inObjTypes);

}