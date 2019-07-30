package com.newland.paas.paasdatamodule.datacommonmodule.vo;

import java.io.Serializable;

/**
 * 对象状态返回VO
 * 有多个状态信息自行添加
 *
 * @author WRP
 * @since 2019/2/26
 */
public class ObjStatusResponseVO implements Serializable {

    private static final long serialVersionUID = 1195204152526152704L;

    /**
     * 对象ID
     */
    private Long id;
    /**
     * 对象状态
     */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
