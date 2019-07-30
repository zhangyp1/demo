package com.newland.paas.paasservice.sysmgr.utils;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;

/**
 * @program: paas-all
 * @description: vo转换器
 * @author: Frown
 * @create: 2019-03-12 13:36
 **/
public final class VoConvertUtil {

    /**
     * VoConvertUtil
     */
    private VoConvertUtil() {

    }

    /**
     *
     * @param from
     * @param to
     */
    public static final void copy(SysMenuOper from, MenuBO to) {
        to.setMenuId(from.getId());
        to.setMenuName(from.getName());
        to.setIcon(from.getIcon());
        to.setUrl(from.getUrl());
        to.setOrderNumber(from.getOrderNumber());
        to.setParentId(from.getParentId());
        to.setHidden(from.getHidden());
    }
}
