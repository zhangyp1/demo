package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;

/**
 * 系统分组树列表请求Vo
 *
 * @author zhongqingjiang
 */
public class SysCategoryTreeListReqVo implements Serializable {

    private String menuOperCode;

    public String getMenuOperCode() {
        return menuOperCode;
    }

    public void setMenuOperCode(String menuOperCode) {
        this.menuOperCode = menuOperCode;
    }
}
