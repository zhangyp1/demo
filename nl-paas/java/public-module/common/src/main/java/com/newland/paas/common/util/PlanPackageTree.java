package com.newland.paas.common.util;

import java.util.List;

/**
 * 上线包目录树
 * @author sunxm
 */
public class PlanPackageTree {
    private String label;
    private List<PlanPackageTree> children;

    public PlanPackageTree() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<PlanPackageTree> getChildren() {
        return children;
    }

    public void setChildren(List<PlanPackageTree> children) {
        this.children = children;
    }
}
