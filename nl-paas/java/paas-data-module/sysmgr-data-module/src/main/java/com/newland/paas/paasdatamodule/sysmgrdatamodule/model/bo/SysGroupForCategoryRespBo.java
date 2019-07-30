package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;

/**
 * 描述
 *
 * @author linkun
 * @created 2018年7月30日 下午6:25:32
 */
public class SysGroupForCategoryRespBo extends SysGroup {

    private String categoryName;
    private Long categoryId;
    private String categoryPName;
    private Long categoryPId;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryPName() {
        return categoryPName;
    }

    public void setCategoryPName(String categoryPName) {
        this.categoryPName = categoryPName;
    }

    public Long getCategoryPId() {
        return categoryPId;
    }

    public void setCategoryPId(Long categoryPId) {
        this.categoryPId = categoryPId;
    }
}
