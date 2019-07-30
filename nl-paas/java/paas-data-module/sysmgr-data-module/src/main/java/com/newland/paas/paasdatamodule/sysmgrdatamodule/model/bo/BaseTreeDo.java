package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.ex.ITreeNode;

/**
 * 树形实体定义
 */

public class BaseTreeDo implements ITreeNode {
    private String id;
    private String parentId;
    private String name;
    /**
     * 别名管理使用的，别名类型
     */
    private String type;

    public BaseTreeDo() {}

    public BaseTreeDo(String id, String parentId, String name) {
        super();
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getLevel() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BaseTreeDo{"
                + "id='" + id + '\''
                + ", parentId='" + parentId + '\''
                + ", name='" + name + '\''
                + ", type='" + type + '\''
                + '}';
    }
}
