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
public class TreeNode<V extends ITreeNode> implements Serializable, ITreeNode {
    /**
     * 
     */
    private static final long serialVersionUID = 90717641001019044L;
    @JsonBackReference
    private TreeNode<V> parent;
    private String id;
    private String name;
    /**
     * 别名管理使用的，别名类型
     */
    private String type;
    @JsonIgnore
    private V data;
    private Boolean leaf;
    @JsonManagedReference
    private List<TreeNode<V>> children = null;

    public TreeNode() {}

    public TreeNode(V data) {
        super();
        this.id = data.getId();
        this.name = data.getName();
        this.data = data;
        this.type = data.getType();
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
        TreeNode<V> rootNode = null;
        /**
         * 节点ID和节点自身MAP
         */
        Map<String, TreeNode<V>> idToNode = new LinkedHashMap<>();
        /**
         * 节点ID和父节点IDMAP
         */
        List<TupleTwo> idToParent = new ArrayList<>(idToNode.size());

        /** 添加节点 */
        public boolean addNode(String parentId, String id, V data) {
            if (parentId.equals(id)) {
                return false;
            }
            TreeNode<V> node = new TreeNode<>(data);
            idToNode.put(id, node);
            idToParent.add(new TupleTwo(id, parentId));
            return true;
        }

        /** 添加节点 */
        public TreeNode<V> build() {
            for (TupleTwo tupleTwo : idToParent) {
                TreeNode<V> node = idToNode.get(tupleTwo.id);
                TreeNode<V> parent = idToNode.get(tupleTwo.parentId);

                if (parent == null) { // handle headless
                    if (rootNode == null) {
                        // 构造虚拟根节点
                        rootNode = new TreeNode<>();
                    }
                    rootNode.addChild(node);
                } else {
                    parent.addChild(node);
                }
            }
            return rootNode != null ? rootNode : new TreeNode<>();
        }

        /**
         * 
         * @param root
         */
        public void preOrder(TreeNode<V> root) {
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
    public TreeNode<V> addChild(TreeNode<V> child) {
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

    public TreeNode<V> getParent() {
        return parent;
    }

    public void setParent(TreeNode<V> parent) {
        this.parent = parent;
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

    public List<TreeNode<V>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<V>> children) {
        this.children = children;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
