package com.newland.paas.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * TreeBuilder
 *
 * @param <V>
 */
public class TreeBuilder<T> {
    TreeNode<T> rootNode = null;
    // 节点ID和节点自身MAP
    Map<String, TreeNode<T>> idToNode = new LinkedHashMap<>();
    // 节点ID和父节点IDMAP
    List<TupleTwo> idToParent = new ArrayList<>(idToNode.size());

    /**
     * 添加节点
     *
     * @param parentId
     * @param id
     * @param data
     * @return
     */
    public boolean addNode(Function<T, Object> id, Function<T, Object> name, Function<T, Object> pId, T data) {
        if (pId.apply(data).equals(id.apply(data))) {
            // 循环依赖
            return false;
        }
        TreeNode<T> node = new TreeNode<>(data, id, name, pId);

        idToNode.put(id.apply(data).toString(), node);
        idToParent.add(new TupleTwo(id.apply(data).toString(), pId.apply(data).toString()));

        return true;
    }

    /**
     * 添加多个节点
     * 
     * @param id
     * @param name
     * @param pId
     * @param dataList
     */
    public void addNodes(Function<T, Object> id, Function<T, Object> name, Function<T, Object> pId, List<T> dataList) {
        for (T t : dataList) {
            addNode(id, name, pId, t);
        }
    }

    /**
     * 添加节点
     *
     * @return
     */
    public TreeNode<T> build() {
        for (TupleTwo tupleTwo : idToParent) {
            TreeNode<T> node = idToNode.get(tupleTwo.id);
            TreeNode<T> parent = idToNode.get(tupleTwo.parentId);

            // handle headless
            if (parent == null) {
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
     * TupleTwo
     */
    private static final class TupleTwo {
        private String id;
        private String parentId;

        /**
         * TupleTwo
         *
         * @param id
         * @param parentId
         */
        TupleTwo(String id, String parentId) {
            super();
            this.id = id;
            this.parentId = parentId;
        }

    }

    /**
     * TreeNode
     *
     * @param <V>
     */
    public class TreeNode<T> implements Serializable {
        private static final long serialVersionUID = 8900038756201739809L;
        @JsonBackReference
        private TreeNode<T> parent;
        private String id;
        private String name;
        @JsonIgnore
        private Object data;
        @JsonManagedReference
        private List<TreeNode<T>> children = null;

        public TreeNode(T data, Function<T, Object> id, Function<T, Object> name, Function<T, Object> pId) {
            super();
            this.id = id.apply(data).toString();
            this.name = name.apply(data).toString();
            this.data = data;
        }

        /**
         * TreeNode
         */
        public TreeNode() {}

        /**
         * addChild
         *
         * @param child
         * @return
         */
        public TreeNode<T> addChild(TreeNode<T> child) {
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

        public TreeNode<T> getParent() {
            return parent;
        }

        public void setParent(TreeNode<T> parent) {
            this.parent = parent;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public List<TreeNode<T>> getChildren() {
            return children;
        }

        public void setChildren(List<TreeNode<T>> children) {
            this.children = children;
        }

    }
}
