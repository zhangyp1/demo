package com.newland.paas.paasservice.sysmgr.vo;

/**
 *
 */
public class UserLoginTimesVO {
    private String time;
    private Integer userCount;

    public UserLoginTimesVO() {
    }

    public UserLoginTimesVO(String time, Integer userCount) {
        this.time = time;
        this.userCount = userCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
