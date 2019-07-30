package com.newland.paas.common.util;

import java.util.*;

/**
 * @Author: zhoufeilong
 * @Description: 保证顺序的属性文件写入
 * @Date: 2017-10-23 16:06
 * @Modified By:
 */
public class OrderedProperties extends Properties {

    private static final long serialVersionUID = 7447704414289995224L;

    private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

    public Enumeration<Object> keys() {
        return Collections.<Object> enumeration(keys);
    }

    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }


    public Set<Object> keySet() {
        return keys;
    }


    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();
        for (Object key : this.keys) {
            set.add((String) key);
        }
        return set;
    }
}
