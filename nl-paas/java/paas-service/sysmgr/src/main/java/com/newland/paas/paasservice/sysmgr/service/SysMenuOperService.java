/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;

import java.util.List;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.OperBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuOperReqBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * 描述
 * @author linkun
 * @created 2018年6月26日 下午3:29:31
 */
public interface SysMenuOperService {

    /**
     * 获取用户的菜单列表
     * 描述
     * @author linkun
     * @created 2018年6月27日 下午4:36:26
     * @param userId
     * @param tenantId
     * @return
     * @throws ApplicationException
     */
    List<MenuBO> getMenusByTenantUserId(Long userId, Long tenantId) throws ApplicationException;
    /**
     * 获取用户的操作列表
     * 描述
     * @author linkun
     * @created 2018年6月27日 下午4:36:36
     * @param userId
     * @param tenantId
     * @return
     * @throws ApplicationException
     */
    List<OperBO> getOpersByTenantUserId(Long userId, Long tenantId) throws ApplicationException;

    /**
     *
     * @param reqBo
     * @return
     */
    List<SysMenuOper> getMenus(SysMenuOperReqBo reqBo);
    /**
     * 描述
     * @author linkun
     * @created 2018年7月19日 下午6:51:34
     * @param params
     * @return
     * @throws ApplicationException 
     */
    int add(SysMenuOperReqBo params) throws ApplicationException;
    /**
     * 描述
     * @author linkun
     * @created 2018年7月19日 下午6:58:07
     * @param params
     * @return
     * @throws ApplicationException 
     */
    int update(SysMenuOperReqBo params) throws ApplicationException;
    /**
     * 描述
     * @author linkun
     * @created 2018年7月19日 下午7:08:01
     * @param id
     * @return
     * @throws ApplicationException 
     */
    int delete(Long id) throws ApplicationException;
    /**
     * 描述
     * @author linkun
     * @created 2018年7月20日 上午11:23:53
     * @param id
     * @return
     */
    SysMenuOper get(Long id);
    /**
     * 描述
     * @author linkun
     * @created 2018年7月20日 下午1:55:58
     * @param reqBo
     * @param pageInfo
     * @return
     */
    ResultPageData<SysMenuOper> pageQuery(SysMenuOperReqBo reqBo, PageInfo pageInfo);

    /**
     *获取所有菜单的菜单列表
     * @return
     * @throws ApplicationException
     */
    List<MenuBO> getMenusList() throws ApplicationException;

    /**
     * 新增菜单操作时，检查操作代码是否已存在
     * @param paramReqVo
     * @return
     */
    int countByMenuCode(SysMenuOperReqBo paramReqVo);
}

