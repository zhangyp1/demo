package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUrlRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUrlRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleRespBo;
import com.newland.paas.paasservice.sysmgr.error.SysUrlRoleError;
import com.newland.paas.paasservice.sysmgr.service.SysUrlRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018-08-06 16:27:06
 */
@Service("sysUrlRoleService")
public class SysUrlRoleServiceImpl implements SysUrlRoleService {

    @Autowired
    private SysUrlRoleMapper sysUrlRoleMapper;

    /**
     * 描述
     *
     * @param params
     * @param pageInfo
     * @return
     * @author linkun
     * @created 2018-08-06 16:27:06
     */
    @Override
    public ResultPageData<SysUrlRoleRespBo> page(SysUrlRoleReqBo params, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysUrlRoleRespBo> rrs = sysUrlRoleMapper.selectBySelective(params);
        return new ResultPageData<>(rrs);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018-08-06 16:27:06
     */
    @Override
    public List<SysUrlRoleRespBo> list(SysUrlRoleReqBo params) throws ApplicationException {
        return sysUrlRoleMapper.selectBySelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018-08-06 16:27:06
     */
    @Override
    public int add(SysUrlRole params) throws ApplicationException {
        return sysUrlRoleMapper.insertSelective(params);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @author linkun
     * @created 2018-08-06 16:27:06
     */
    @Override
    public int update(SysUrlRole params) throws ApplicationException {
        return sysUrlRoleMapper.updateByPrimaryKeySelective(params);
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @author linkun
     * @created 2018-08-06 16:27:06
     */
    @Override
    public int delete(Long id) throws ApplicationException {
        return sysUrlRoleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @author linkun
     * @created 2018-08-06 16:27:06
     */
    @Override
    public SysUrlRoleRespBo get(Long id) throws ApplicationException {
        return sysUrlRoleMapper.selectByPrimaryKey(id);
    }

    /**
     * 描述
     *
     * @param params
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月6日 下午4:33:55
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int addUrls(SysUrlRoleReqBo params) throws ApplicationException {
        if (params.getRoleId() == null) {
            throw new ApplicationException(SysUrlRoleError.ROLE_ID_IS_NULL);
        }
        if (params.getUrls() == null || params.getUrls().length == 0) {
            return 0;
        }
        // 删除旧的关系
        SysUrlRoleReqBo roleParam = new SysUrlRoleReqBo();
        roleParam.setRoleId(params.getRoleId());
        sysUrlRoleMapper.deleteBySelective(roleParam);

        // 添加新的关系
        int count = 0;
        Date now = new Date();
        for (String url : params.getUrls()) {
            SysUrlRole info = new SysUrlRole();
            info.setUrl(url);
            info.setRoleId(params.getRoleId());
            info.setCreateDate(now);
            info.setChangeDate(now);
            info.setCreatorId(RequestContext.getUserId());
            count += sysUrlRoleMapper.insertSelective(info);
        }
        return count;
    }

    /**
     * 描述
     *
     * @param roleId
     * @return
     * @author linkun
     * @created 2018年8月6日 下午4:33:55
     */
    @Override
    public List<SysUrlRoleRespBo> getUrlsByRole(Long roleId) {
        return sysUrlRoleMapper.getUrlsByRole(roleId);
    }

}
