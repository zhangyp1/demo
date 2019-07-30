package com.newland.paas.paasservice.sysmgr.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.ex.ITreeNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <V>
 */
public class DictTreeNode<V extends ITreeNode> implements Serializable, ITreeNode {
    /**
     *
     */
    private static final long serialVersionUID = 90717641001019044L;
    @JsonBackReference
    private DictTreeNode<V> parent;
    private String id;
    private String name;
    private Integer level;
    /**
     * 别名管理使用的，别名类型
     */
    private String type;
    @JsonIgnore
    private V data;
    private Boolean leaf;
    @JsonManagedReference
    private List<DictTreeNode<V>> children = null;

    public DictTreeNode() {
    }

    public DictTreeNode(V data) {
        super();
        this.id = data.getId();
        this.name = data.getName();
        this.level = data.getLevel();
        this.data = data;
    }

    /**
     *
     */
    private static final class TupleTwo {
        private String id;
        private String parentId;

        TupleTwo(String id, String parentId) {
            super();
            this.id = id;
            this.parentId = parentId;
        }

    }

    /**
     *
     * @param <V>
     */
    public static final class TreeBuilder<V extends ITreeNode> {
        DictTreeNode<V> rootNode = null;
        /**
         * 节点ID和节点自身MAP
         */
        Map<String, DictTreeNode<V>> idToNode = new LinkedHashMap<>();

        /**
         * 节点ID和父节点IDMAP
         */
        List<TupleTwo> idToParent = new ArrayList<>(idToNode.size());

        /**
         * 添加节点
         */
        public boolean addNode(String parentId, String id, V data) {
            if (parentId.equals(id)) {
                // 循环依赖
                return false;
            }
            DictTreeNode<V> node = new DictTreeNode<>(data);

            idToNode.put(id, node);
            idToParent.add(new TupleTwo(id, parentId));

            return true;
        }

        /**
         * 添加节点
         */
        public DictTreeNode<V> build() {
            for (TupleTwo tupleTwo : idToParent) {
                DictTreeNode<V> node = idToNode.get(tupleTwo.id);
                DictTreeNode<V> parent = idToNode.get(tupleTwo.parentId);

                if (parent == null) { // handle headless
                    if (rootNode == null) {
                        // 构造虚拟根节点
                        rootNode = new DictTreeNode<>();
                    }
                    rootNode.addChild(node);
                } else {
                    parent.addChild(node);
                }
            }
            return rootNode != null ? rootNode : new DictTreeNode<>();
        }

        /**
         * @param root
         */
        public void preOrder(DictTreeNode<V> root) {
            if (root != null) {
                if (root.children != null) {
                    for (int i = 0; i < root.children.size(); i++) {
                        preOrder(root.children.get(i));
                    }
                } else {
                    root.setLeaf(true);
                }
            }
        }

    }

    /**
     *
     * @param child
     * @return
     */
    public DictTreeNode<V> addChild(DictTreeNode<V> child) {
        if (child == this) {
            throw new IllegalArgumentException("circular dependency on " + this);
        }
        child.parent = this;
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DictTreeNode<V> getParent() {
        return parent;
    }

    public void setParent(DictTreeNode<V> parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public V getData() {
        return data;
    }

    public void setData(V data) {
        this.data = data;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public List<DictTreeNode<V>> getChildren() {
        return children;
    }

    public void setChildren(List<DictTreeNode<V>> children) {
        this.children = children;
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
}
