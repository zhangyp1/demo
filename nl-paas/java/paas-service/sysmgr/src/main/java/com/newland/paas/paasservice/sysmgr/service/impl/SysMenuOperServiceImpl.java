/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service.impl;

import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.OperBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuOperReqBo;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysMenuOperError;
import com.newland.paas.paasservice.sysmgr.service.SysMenuOperRoleService;
import com.newland.paas.paasservice.sysmgr.service.SysMenuOperService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年6月26日 下午3:34:42
 */
@Service("sysMenuOperService")
public class SysMenuOperServiceImpl implements SysMenuOperService {

    @Autowired
    private SysMenuOperMapper sysMenuOperMapper;
    @Autowired
    private SysMenuOperRoleService sysMenuOperRoleService;

    /**
     * 描述
     *
     * @param userId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年6月27日 下午4:36:52
     * @see com.newland.paas.paasservice.sysmgr.service.SysMenuOperService#getMenusByTenantUserId(java.lang.Long, Long)
     */
    @Override
    public List<MenuBO> getMenusByTenantUserId(Long userId, Long tenantId) {
        List<MenuBO> menus = sysMenuOperMapper.getMenusByUserId(userId, tenantId);
        // 目前不支持提供方功能，因此临时屏蔽 2019-06-20
//        if (tenantId >= SysMgrConstants.TENANT_ID_NORMAL_MIN) {
//            SysTenant sysTenant = sysTenantMapper.selectByPrimaryKey(tenantId);
//            String supplierStatus = sysTenant.getSupplierStatus();
//            if (Objects.equals(supplierStatus, GlbDictConstants.APPROVE_STATUS_APPROVEY.dictCode)) {
//                // 获取运维的菜单--我的服务
//                SysMenuOperReqBo query = new SysMenuOperReqBo();
//                query.setUrl(SysMgrConstants.MY_SERVICE_MENU_URL);
//                List<SysMenuOper> sysMenuOpers = sysMenuOperMapper.selectBySelective(query);
//                MenuBO menuBO = null;
//                if (sysMenuOpers != null && !sysMenuOpers.isEmpty()) {
//                    menuBO = new MenuBO();
//                    VoConvertUtil.copy(sysMenuOpers.get(0), menuBO);
//                    menus.add(menuBO);
//                }
//            }
//        }
        return menus;
    }

    /**
     * 描述
     *
     * @param userId
     * @return
     * @author linkun
     * @created 2018年6月27日 下午4:36:52
     * @see com.newland.paas.paasservice.sysmgr.service.SysMenuOperService#getOpersByTenantUserId(java.lang.Long, Long)
     */
    @Override
    public List<OperBO> getOpersByTenantUserId(Long userId, Long tenantId) {
        return sysMenuOperMapper.getOpersByUserId(userId, tenantId);
    }

    @Override
    public List<SysMenuOper> getMenus(SysMenuOperReqBo reqVo) {

        return sysMenuOperMapper.selectBySelective(reqVo);
    }

    @Override
    public int add(SysMenuOperReqBo params) throws ApplicationException {
        params.setCreateDate(new Date());
        params.setDelFlag(SysMgrConstants.IS_DELETE_FALSE);
        params.setCreatorId(RequestContext.getUserId());
        //判断排序是否重复
        if (isOrderNumberExist(null, params.getOrderNumber(), params.getParentId())) {
            throw new ApplicationException(SysMenuOperError.DOUBLE_ORDER_NUMBER_ERROR);
        }
        if (params.getParentId() != null) {
            SysMenuOper parent = sysMenuOperMapper.selectByPrimaryKey(params.getParentId());
            params.setOrderNumber(parent.getOrderNumber() + "/"
                    + String.format("%02d", Integer.parseInt(params.getOrderNumber())));
        } else {
            params.setOrderNumber(String.format("%02d", Integer.parseInt(params.getOrderNumber())));
        }
        return sysMenuOperMapper.insert(params);
    }

    /**
     * 判断排序号是否重复
     * 描述
     *
     * @param orderNumInt
     * @param parentId
     * @return
     * @throws ApplicationException
     * @author linkun
     * @created 2018年8月2日 下午1:45:31
     */
    protected boolean isOrderNumberExist(Long id, String orderNumInt, Long parentId) throws ApplicationException {
        boolean isExist = false;

        String orderNumber = String.format("%02d", Integer.parseInt(orderNumInt));
        if (parentId != null) {
            SysMenuOper parent = sysMenuOperMapper.selectByPrimaryKey(parentId);
            if (parent == null) {
                throw new ApplicationException(SysMenuOperError.PARENT_NOEXIST);
            }
            orderNumber = parent.getOrderNumber() + "/" + orderNumber;
        }

        SysMenuOperReqBo countOrderNumParam = new SysMenuOperReqBo();
        countOrderNumParam.setOrderNumber(orderNumber);
        countOrderNumParam.setNoeqId(id);

        if (sysMenuOperMapper.countBySelective(countOrderNumParam) > 0) {
            isExist = true;
        }

        return isExist;

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int update(SysMenuOperReqBo params) throws ApplicationException {
        if (StringUtils.isNotBlank(params.getOrderNumber())) {
            //判断排序是否重复
            if (isOrderNumberExist(params.getId(), params.getOrderNumber(), params.getParentId())) {
                throw new ApplicationException(SysMenuOperError.DOUBLE_ORDER_NUMBER_ERROR);
            }
            if (params.getParentId() != null) {
                SysMenuOper parent = sysMenuOperMapper.selectByPrimaryKey(params.getParentId());
                params.setOrderNumber(parent.getOrderNumber() + "/"
                        + String.format("%02d", Integer.parseInt(params.getOrderNumber())));
            } else {
                params.setOrderNumber(String.format("%02d", Integer.parseInt(params.getOrderNumber())));
            }

            updateChildrenOrderNum(params.getId(), params.getOrderNumber());
        }
        return sysMenuOperMapper.updateByPrimaryKeySelective(params);
    }

    /**
     * 更新子节点的排序号
     * 描述
     *
     * @param id
     * @param parentOrderNumber
     * @author linkun
     * @created 2018年8月2日 下午2:01:06
     */
    protected void updateChildrenOrderNum(Long id, String parentOrderNumber) {
        SysMenuOperReqBo selectChildrenParam = new SysMenuOperReqBo();
        selectChildrenParam.setParentId(id);
        List<SysMenuOper> list = sysMenuOperMapper.selectBySelective(selectChildrenParam);
        if (list.isEmpty()) {
            return;
        }
        for (SysMenuOper child : list) {
            String[] childOrderNumbers = child.getOrderNumber().split("/");
            child.setOrderNumber(parentOrderNumber + "/" + childOrderNumbers[childOrderNumbers.length - 1]);
            sysMenuOperMapper.updateByPrimaryKeySelective(child);
            updateChildrenOrderNum(child.getId(), child.getOrderNumber());
        }
    }

    @Override
    public int delete(Long id) throws ApplicationException {
        SysMenuOperReqBo childsCount = new SysMenuOperReqBo();
        childsCount.setParentId(id);
        int count = sysMenuOperMapper.countBySelective(childsCount);
        if (count > 0) {
            throw new ApplicationException(SysMenuOperError.HAS_CHILDS);
        }
        // 删除此菜单与角色的关联关系
        sysMenuOperRoleService.deleteByMenuId(id);
        return sysMenuOperMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SysMenuOper get(Long id) {
        return sysMenuOperMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResultPageData<SysMenuOper> pageQuery(SysMenuOperReqBo reqVo, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<SysMenuOper> rrs = sysMenuOperMapper.selectBySelective(reqVo);
        return new ResultPageData<>(rrs);
    }

    /**
     * 描述
     *
     * @return
     * @author caifeitong
     * @created 2018年7月31日 上午9:36:52
     * @see com.newland.paas.paasservice.sysmgr.service.SysMenuOperService#getMenusList()
     */
    @Override
    public List<MenuBO> getMenusList() {
        List<MenuBO> menus = sysMenuOperMapper.getMenusList();
        List<MenuBO> menusList = new ArrayList<>();
        for (MenuBO menu : menus) {
            if (menu.getParentId() == null) {
                menusList.add(menu);
                getMenusChildrenList(menus, menusList, menu.getMenuId());
            }

        }

        return menusList;
    }

    public void getMenusChildrenList(List<MenuBO> menus, List<MenuBO> menusList, Long menuId) {
        for (MenuBO menu : menus) {
            if (menu.getParentId() != null) {
                if (menu.getParentId().equals(menuId)) {
                    menusList.add(menu);
                    getMenusChildrenList(menus, menusList, menu.getMenuId());
                }
            }
        }

    }


    @Override
    public int countByMenuCode(SysMenuOperReqBo paramReqVo) {
        return sysMenuOperMapper.countByMenuCode(paramReqVo);
    }

}

