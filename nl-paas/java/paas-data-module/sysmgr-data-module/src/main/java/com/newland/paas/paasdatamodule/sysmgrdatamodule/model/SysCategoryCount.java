package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;

/**
 * 系统分组按层级统计
 *
 * @author zhongqingjiang
 */
public class SysCategoryCount implements Serializable {

    private int levelNum;
    private int countNum;

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }
}
