package com.newland.paas.paasservice.sysmgr.ws;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasservice.sysmgr.service.SysUserService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:PanYang
 * Date:Created in 下午3:15 2018/7/25
 * Modified By:
 */
@Service
public class A4InteractServiceImpl {

    @Autowired
    private SysUserService sysUserService;

    /**
     * @param account
     * @return
     * @throws Exception
     */
    public SysUser getUserId(String account) throws ApplicationException {
        return sysUserService.getUserByAccount(account);
    }

    /**
     * @param userInfo
     * @throws Exception
     */
    public void delUser(SysUser userInfo) {
        sysUserService.deleteByPrimaryKey(userInfo.getUserId());
    }

    /**
     * @param operateId
     * @param modifymode
     * @param userInfo
     * @return
     * @throws Exception
     */
    public SysUser updateUser(String operateId, String modifymode, SysUser userInfo) throws ApplicationException {
        SysUser user = null;
        //4A 操作类型add、delete、change、chgstatus、resetpwd
        switch (modifymode) {
            case "add":
                sysUserService.saveSysUser(userInfo, 1);
                user = userInfo;
                break;
            case "delete":
                sysUserService.delSysUser(userInfo);
                user = userInfo;
                break;
            case "change":
                sysUserService.updateByPrimaryKeySelective(userInfo);
                user = sysUserService.getUserByAccount(userInfo.getAccount());
                break;
            case "chgstatus":
                SysUser chgstatus = new SysUser();
                chgstatus.setStatus(userInfo.getStatus());
                chgstatus.setUserId(userInfo.getUserId());
                sysUserService.updateByPrimaryKeySelective(chgstatus);
                user = sysUserService.getUserByAccount(userInfo.getAccount());
                break;
            case "resetpwd":
                SysUser resetpwd = new SysUser();
                resetpwd.setPassword(userInfo.getPassword());
                resetpwd.setUserId(userInfo.getUserId());
                sysUserService.updateByPrimaryKeySelective(resetpwd);
                user = sysUserService.getUserByAccount(userInfo.getAccount());
                break;
            default:
                throw new ApplicationException(
                        new PaasError("operateId:" + operateId, "the MODIFYMODE element  invalid"));
        }
        return user;
    }

}
