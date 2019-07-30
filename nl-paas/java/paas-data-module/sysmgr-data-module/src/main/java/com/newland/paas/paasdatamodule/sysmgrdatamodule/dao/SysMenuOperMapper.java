package com.newland.paas.paasdatamodule.sysmgrdatamodule.dao;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.OperBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuOperReqBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuOperMapper {
    int countBySelective(SysMenuOperReqBo record);

    int deleteBySelective(SysMenuOperReqBo record);

    int deleteByPrimaryKey(Long id);

    int insert(SysMenuOper record);

    int insertSelective(SysMenuOper record);

    List<SysMenuOper> selectBySelective(SysMenuOperReqBo record);

    SysMenuOper selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysMenuOper record);

    int updateByPrimaryKey(SysMenuOper record);

    /**
     * 描述   根据用户id获取用户拥有的菜单
     * @author linkun
     * @created 2018年6月26日 下午3:36:16
     * @param userId
     * @param tenantId TODO
     * @return
     */
    List<MenuBO> getMenusByUserId(@Param("userId")Long userId,@Param("tenantId") Long tenantId);
    /**
     * 根据用户id获取用户拥有的操作
     * 描述
     * @author linkun
     * @created 2018年6月27日 下午4:35:24
     * @param userId
     * @param tenantId TODO
     * @return
     */
    List<OperBO> getOpersByUserId(@Param("userId")Long userId,@Param("tenantId") Long tenantId);

    /**
     * 描述   获取所有的菜单
     * @author 蔡飞统
     * @created 2018年7月31日 上午9:36:52
     * @return
     */
    List<MenuBO> getMenusList();

    /**
     * 根据给定的菜单id列表查询树对应的所有菜单id列表
     * @param menuIdList
     * @return
     */
	List<Long> selectAllMenuIdList4Tree(@Param("menuIdList")List<Long> menuIdList);

    /**
     * 查询url绑定的菜单
     * @param urlId
     * @return
     */
    List<SysMenuOper> selectMenuByUrl(@Param("urlId") String urlId);

    /**
     * 新增菜单操作时，检查操作代码是否已存在
     * @param paramReqVo
     * @return
     */
    int countByMenuCode(SysMenuOperReqBo paramReqVo);
}