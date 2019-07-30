package com.newland.paas.paasservice.sysmgr.runner;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryQuotaMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategoryQuota;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysCategoryBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantReqVo;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryService;
import com.newland.paas.paasservice.sysmgr.service.SysGroupService;
import com.newland.paas.paasservice.sysmgr.service.SysTenantService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 版本升级需要执行的程序
 *
 * @author zhongqingjiang
 */
@Component
public class Upgrader {

    private static final Log LOG = LogFactory.getLogger(Upgrader.class);

    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private SysGroupService sysGroupService;
    @Autowired
    private SysCategoryService sysCategoryService;
    @Autowired
    private SysCategoryMapper sysCategoryMapper;
    @Autowired
    private SysCategoryQuotaMapper sysCategoryQuotaMapper;

    @Transactional(rollbackFor = {Exception.class})
    public void run() throws ApplicationException {
        // TODO: 版本升级临时代码，升级后择时删除 2019-07-18
        LOG.info(LogProperty.LOGTYPE_DETAIL, "开始执行升级");
        // 查询所有租户
        SysTenantReqVo sysTenantReqVo = new SysTenantReqVo();
        sysTenantReqVo.setIsAllTenants(true);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrentPage(1);
        pageInfo.setPageSize(999);
        ResultPageData<SysTenantBo> tenantBoResultPageData =
                sysTenantService.getSysTenantsByPage(sysTenantReqVo, pageInfo);
        List<SysTenantBo> sysTenantBoList = tenantBoResultPageData.getList();
        // 遍历所有租户
        for (SysTenantBo sysTenantBo : sysTenantBoList) {
            LOG.info(LogProperty.LOGTYPE_DETAIL, "准备升级，租户名称：" + sysTenantBo.getTenantName() + " 租户ID:" + sysTenantBo.getTenantId());
            // 获取根工组
            SysGroupReqBo sysGroupReqBo = new SysGroupReqBo();
            sysGroupReqBo.setTenantId(sysTenantBo.getTenantId());
            sysGroupReqBo.setParentGroupId(SysMgrConstants.ROOT_GROUP_PARENT_ID);
            SysGroup rootGroup = sysGroupService.list(sysGroupReqBo).get(0);
            // 查询所有顶级的系统分组
            SysCategory sysCategoryReq = new SysCategory();
            sysCategoryReq.setTenantId(sysTenantBo.getTenantId());
            sysCategoryReq.setSysCategoryPid(-1L);
            List<SysCategory> topSysCategoryList = sysCategoryService.listSysCategory(sysCategoryReq);
            // 检查是否存在系统分组，或顶级的系统分组是否挂在根工组下，以此判断是否已经升级过
            LOG.info(LogProperty.LOGTYPE_DETAIL, "检查该租户是否需要升级");
            boolean needMoveTopSysCategory = topSysCategoryList.size() > 0 && !topSysCategoryList.get(0).getGroupId().equals(rootGroup.getGroupId());
            boolean needAddRootSysCategory = needMoveTopSysCategory || topSysCategoryList.size() == 0;
            if (needAddRootSysCategory) {
                // 创建根系统分组，名称为“租户名称+系统”，挂在根工组下
                LOG.info(LogProperty.LOGTYPE_DETAIL, "创建根系统分组");
                SysCategoryBo rootSysCategoryBo = new SysCategoryBo();
                rootSysCategoryBo.setSysCategoryName(sysTenantBo.getTenantName() + "系统");
                rootSysCategoryBo.setSysCategoryPid(-1L);
                rootSysCategoryBo.setSysCategoryCode(UUID.randomUUID().toString().replace('-', '0').toLowerCase().substring(0, 12));
                rootSysCategoryBo.setGroupId(rootGroup.getGroupId());
                rootSysCategoryBo.setCreatorId(11L);
                rootSysCategoryBo.setTenantId(sysTenantBo.getTenantId());
                sysCategoryMapper.insertSelective(rootSysCategoryBo);
                // 根系统分组配额，默认为0，不限
                LOG.info(LogProperty.LOGTYPE_DETAIL, "创建根系统分组配额");
                SysCategoryQuota rootSysCategoryQuota = new SysCategoryQuota();
                rootSysCategoryQuota.setCpuQuota(0F);
                rootSysCategoryQuota.setMemoryQuota(0F);
                rootSysCategoryQuota.setStorageQuota(0F);
                rootSysCategoryQuota.setSysCategoryId(rootSysCategoryBo.getSysCategoryId());
                rootSysCategoryQuota.setTenantId(sysTenantBo.getTenantId());
                rootSysCategoryQuota.setCreatorId(11L);
                sysCategoryQuotaMapper.insertSelective(rootSysCategoryQuota);

                if (needMoveTopSysCategory) {
                    // 将之前顶级的系统分组挂到根系统分组下
                    LOG.info(LogProperty.LOGTYPE_DETAIL, "修改一级系统分组的父系统分组为根系统分组");
                    for (SysCategory topSysCategory : topSysCategoryList) {
                        topSysCategory.setSysCategoryPid(rootSysCategoryBo.getSysCategoryId());
                        sysCategoryMapper.updateByPrimaryKey(topSysCategory);
                    }
                }
            } else {
                LOG.info(LogProperty.LOGTYPE_DETAIL, "该租户已升级过，跳过升级");
            }
            LOG.info(LogProperty.LOGTYPE_DETAIL, "升级完成，租户名称：" + sysTenantBo.getTenantName() + " 租户ID:" + sysTenantBo.getTenantId());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "执行升级完成");
    }

}
