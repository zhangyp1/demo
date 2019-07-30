package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 * 
 * @ClassName:AstLabelReq
 * @Description: TODO 一句话功能描述
 * @Funtion List:TODO 主要函数及其功能
 * 
 * @author kangweixiang
 * @version
 * @Date 2018年6月22日 下午5:18:29
 * 
 * @History: // 历史修改记录 
 *                      <author>  // 作者
 *                      <time>    // 修改时间
 *                      <version> // 版本
 *                      <desc>  // 描述
 */
public class AstLabelReq implements Serializable {
    private static final long serialVersionUID = -8794123775895061980L;

    private String labelCategory;

    private List<String> labelValue;

    public String getLabelCategory() {

        return labelCategory;
    }

    public void setLabelCategory(String labelCategory) {

        this.labelCategory = labelCategory;
    }

    public List<String> getLabelValue() {

        return labelValue;
    }

    public void setLabelValue(List<String> labelValue) {

        this.labelValue = labelValue;
    }

    @Override
    public String toString() {
        return "AstLabelReq [labelCategory=" + labelCategory + ", labelValue="
                + labelValue + "]";
    }

}
