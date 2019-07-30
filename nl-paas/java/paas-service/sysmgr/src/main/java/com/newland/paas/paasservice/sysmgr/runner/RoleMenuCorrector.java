package com.newland.paas.paasservice.sysmgr.runner;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleMapper;
import com.newland.paas.paasservice.sysmgr.service.SysMenuOperRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色菜单修正器
 *
 * @author zhongqingjiang
 */
@Component
public class RoleMenuCorrector {

    private static final Log LOG = LogFactory.getLogger(RoleMenuCorrector.class);

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuOperRoleService sysMenuOperRoleService;

    public void correct() throws ApplicationException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "开始修正角色的菜单操作数据.....");
        // 根据父角色的菜单操作，修正子角色的菜单操作
        List<Long> roleIdlist = sysRoleMapper.listRoleIdOfHasChild();
        for (Long roleId : roleIdlist) {
            sysMenuOperRoleService.correctMenuPrivileges(roleId, false);
        }
    }

}
