package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysInterfaceUrl;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuUrlBO;
import com.newland.paas.paasservice.sysmgr.vo.SyncInterfaceUrlVO;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

import java.util.List;

/**
 * 接口api权限
 *
 * @author WRP
 * @since 2019/1/19
 */
public interface SysApiAuthService {

    /**
     * 同步全量接口地址信息
     *
     * @return 同步结果
     */
    List<SyncInterfaceUrlVO> syncInterfaceUrl();

    /**
     * 接口列表
     *
     * @param menuId
     * @param sysInterfaceUrl
     * @param pageInfo
     * @return
     */
    ResultPageData<SysInterfaceUrl> interfaceUrlPageByParams(Long menuId,
                                                             SysInterfaceUrl sysInterfaceUrl, PageInfo pageInfo);

    /**
     * 菜单URL_接口列表
     *
     * @return
     */
    List<SysMenuUrlBO> menuInterfaceUrlByParams(SysMenuUrlBO sysMenuUrlBO);

    /**
     * 修改 菜单URL关联关系
     *
     * @param menuId
     * @param urlIds
     * @return
     */
    boolean updateMenuUrlRel(Long menuId, List<String> urlIds);

    /**
     * 创建接口
     *
     * @param sysInterfaceUrl
     */
    void createInterfaceUrl(SysInterfaceUrl sysInterfaceUrl);

    /**
     * 修改接口
     *
     * @param sysInterfaceUrl
     */
    void updateInterfaceUrl(SysInterfaceUrl sysInterfaceUrl);

    /**
     * 删除接口
     *
     * @param urlId
     */
    void deleteInterfaceUrl(String urlId);

    /**
     * 接口列表
     *
     * @param sysInterfaceUrl
     * @param pageInfo
     * @return
     */
    ResultPageData<SysInterfaceUrl> interfaceUrlList(SysInterfaceUrl sysInterfaceUrl, PageInfo pageInfo);

    /**
     * 菜单列表
     *
     * @param urlId
     * @return
     */
    List<SysMenuOper> selectMenuByUrl(String urlId);
}
