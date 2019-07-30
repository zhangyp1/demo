package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.ex;

public class BaseTreeDo implements ITreeNode {
    private String id;
    private String parentId;
    private String name;
    private Integer level;
    /**
     * 别名管理使用的，别名类型
     */
    private String type;

    public BaseTreeDo() {}

    public BaseTreeDo(String id, String parentId, String name, String type) {
        super();
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BaseTreeDo [id=" + id + ", parentId=" + parentId + ", name=" + name + ", level=" + level + ", type=" + type + "]";
    }

}
