package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.PageHelper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRoleUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserRespBo;
import com.newland.paas.paasservice.sysmgr.service.SysRoleUserService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018-08-09 08:53:46
 */
@Service("sysRoleUserService")
public class SysRoleUserServiceImpl implements SysRoleUserService {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public ResultPageData<SysRoleUserRespBo> page(SysRoleUserReqBo params, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysRoleUserRespBo> rrs = sysRoleUserMapper.selectBySelective(params);
        return new ResultPageData<>(rrs);
    }

    @Override
    public List<SysRoleUserRespBo> list(SysRoleUserReqBo params) throws ApplicationException {
        return sysRoleUserMapper.selectBySelective(params);
    }

    @Override
    public int add(SysRoleUser params) throws ApplicationException {
        return sysRoleUserMapper.insertSelective(params);
    }

    @Override
    public int update(SysRoleUser params) throws ApplicationException {
        return sysRoleUserMapper.updateByPrimaryKeySelective(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int delete(Long id) throws ApplicationException {
        return sysRoleUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByRoleId(Long roleId) throws ApplicationException {
        SysRoleUserReqBo req = new SysRoleUserReqBo();
        req.setRoleId(roleId);
        sysRoleUserMapper.deleteBySelective(req);
    }

    @Override
    public void deleteByRoleIdAndUserId(Long roleId, Long userId) throws ApplicationException {
        SysRoleUserReqBo req = new SysRoleUserReqBo();
        req.setRoleId(roleId);
        req.setUserId(userId);
        sysRoleUserMapper.deleteBySelective(req);
    }

    @Override
    public SysRoleUserRespBo get(Long id) throws ApplicationException {
        return sysRoleUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysRoleUserRespBo> getRoleUsersByTenant(Long tenantId) {
        return sysRoleUserMapper.getRoleUsersByTenant(tenantId);
    }

}
