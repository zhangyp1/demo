/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;
import java.util.List;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUrlRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleRespBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

/**
 * 描述
 * @author linkun
 * @created 2018-08-06 16:27:06
 */
public interface SysUrlRoleService {

	/**
	 *
	 * @param params
	 * @param pageInfo
	 * @return
	 * @throws ApplicationException
	 */
	ResultPageData<SysUrlRoleRespBo> page(SysUrlRoleReqBo params, PageInfo pageInfo) throws ApplicationException;

	/**
	 *
	 * @param params
	 * @return
	 * @throws ApplicationException
	 */
	List<SysUrlRoleRespBo> list(SysUrlRoleReqBo params) throws ApplicationException;

	/**
	 *
	 * @param params
	 * @return
	 * @throws ApplicationException
	 */
	int add(SysUrlRole params) throws ApplicationException;

	/**
	 *
	 * @param params
	 * @return
	 * @throws ApplicationException
	 */
    int update(SysUrlRole params) throws ApplicationException;

	/**
	 *
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
    int delete(Long id) throws ApplicationException;

	/**
	 *
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
    SysUrlRoleRespBo get(Long id) throws ApplicationException;

	/**
	 *
	 * @param params
	 * @return
	 * @throws ApplicationException
	 */
    int addUrls(SysUrlRoleReqBo params) throws ApplicationException;

	/**
	 * 描述
	 * @author linkun
	 * @created 2018年8月6日 下午4:33:25
	 * @param roleId
	 * @return
	 */
	List<SysUrlRoleRespBo> getUrlsByRole(Long roleId);
    
}

