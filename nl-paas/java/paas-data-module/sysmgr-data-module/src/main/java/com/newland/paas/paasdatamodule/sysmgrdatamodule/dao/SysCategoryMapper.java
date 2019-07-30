package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryCount;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysCategoryBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysCategoryMapper {
    int countBySelective(SysCategory record);

    int deleteBySelective(SysCategory record);

    int deleteByPrimaryKey(Long sysCategoryId);

    int insert(SysCategory record);

    int insertSelective(SysCategory record);

    List<SysCategory> selectBySelective(SysCategory record);

    SysCategory selectByPrimaryKey(Long sysCategoryId);

    List<SysCategory> selectByCategoryIds(@Param("sysCategoryIds") List<Long> sysCategoryIds);

    List<SysCategory> selectByGroupIds(@Param("likeCategoryName") String likeCategoryName, @Param("groupIds") List<Long> groupIds);

    int updateByPrimaryKeySelective(SysCategory record);

    int updateByPrimaryKey(SysCategory record);

    List<BaseTreeDo> getSysCategoryTree(@Param("tenantId") Long tenantId, @Param("groupIdList") List<Long> groupIdList);

    /**
     * 获取子条目
     * @param record
     * @return
     */
    List<SysCategory> listSubSysCategory(SysCategory record);

    /**
     * 删除当前以及子条目
     * @param record
     */
    void delSubSysCategory(SysCategory record);


    /**
     * 查询系统分组以及子系统分组
     * @param list
     * @return
     */
    List<SysCategory> listSubsSysCategory(@Param("list") List<SysCategory> list);

    /**
     * 查询工组以及子工组对应的系统分组
     * @param record
     * @return
     */
    List<SysCategory> listCurSysCategory(@Param("record") SysCategoryBo record);

    List<SysCategory> findBaseAll();


    /**
     * 获取子条目（包括根节点）
     * @param record
     * @return
     */
    List<SysCategory> listSubAndCurSysCategory(SysCategory record);

    /**
     * 获取父条目（包括根节点）
     * @param record
     * @return
     */
    List<SysCategory> listParentAndCurSysCategory(SysCategory record);

    /**
     * 获取当前层级
     * @param sysCategoryId
     * @return
     */
    int getCurLevel(Long sysCategoryId);

    /**
     * 获取工组对应的最顶级系统组
     * @param record
     * @return
     */
    SysCategory getTopSysCategory(SysCategory record);

    /**
     * 统计系统分组数量
     *
     * @param tenantId
     * @return
     */
    List<SysCategoryCount> countSysCategoryByTenantId(@Param("tenantId") Long tenantId);

}