package com.newland.paas.sbcommon.vo;

import java.util.Date;

/**
 * 封装时间类vo
 * 
 * @author SongDi
 * @date 2019/01/17
 */
public class ChangeDateVo {

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date changeDate;

    /**
     * 获取创建时间
     * 
     * @return
     */
    public Date getCreateDate() {
        if (this.createDate != null) {
            return new Date(this.createDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 设置创建时间
     * 
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        if (createDate != null) {
            this.createDate = (Date)createDate.clone();
        } else {
            this.createDate = null;
        }

    }

    /**
     * 获取更新时间
     * 
     * @return
     */
    public Date getChangeDate() {
        if (this.changeDate != null) {
            return new Date(this.changeDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 设置更新时间
     * 
     * @param changeDate
     */
    public void setChangeDate(Date changeDate) {
        if (changeDate != null) {
            this.changeDate = (Date)changeDate.clone();
        } else {
            this.changeDate = null;
        }
    }

}