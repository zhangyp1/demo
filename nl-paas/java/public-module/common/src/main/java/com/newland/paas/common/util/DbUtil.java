package com.newland.paas.common.util;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据处理公共方法
 * @author Administrator
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DbUtil {
    private static final String tag = "DbUtil===";

    private static final Log log = LogFactory.getLogger(DbUtil.class);

    /**
     * ResultSet对象转换为指定BEAN对象
     * @param rs    连接数据库结果对象
     * @param c     目标BEAN对象
     * @return
     * @throws Exception
     */
    public static <T> List<T> getRsForObj(ResultSet rs, Class<T> c) throws Exception {
        List values = new ArrayList();

        ResultSetMetaData data = rs.getMetaData();

        if (c == Map.class) {
            while (rs.next()) {
                Map m = new HashMap();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String colName = data.getColumnName(i);
                    m.put(colName.toLowerCase(), rs.getObject(colName));
                }
                values.add(m);
            }
        } else {
            while (rs.next()) {
                Object obj = c.newInstance();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String colName = data.getColumnName(i);
                    Field field = null;
                    try {
                        field = c.getDeclaredField(colName.toLowerCase());
                    } catch (Exception e) {
                    }
                    if (field != null) {
                        Object value = getValueObject(field, rs.getString(colName));
                        field.setAccessible(true);
                        field.set(obj, value);
                    }
                }
                values.add(obj);
            }
        }
        return values;
    }

    /**
     * 根据数据字段类型获取结果
     * @param field
     * @param value
     * @return
     */
    private static Object getValueObject(Field field, String value) {
        Object result;
        Class<?> fieldType = field.getType();

        if (fieldType.equals(Boolean.TYPE) || fieldType.equals(Boolean.class)) {
            result = Boolean.valueOf(value);
        } else if (fieldType.equals(Byte.TYPE) || fieldType.equals(Byte.class)) {
            result = Byte.valueOf(value);
        } else if (fieldType.equals(Short.TYPE) || fieldType.equals(Short.class)) {
            result = Short.valueOf(value);
        } else if (fieldType.equals(Integer.TYPE) || fieldType.equals(Integer.class)) {
            result = Integer.valueOf(value);
        } else if (fieldType.equals(Long.TYPE) || fieldType.equals(Long.class)) {
            result = Long.valueOf(value);
        } else if (fieldType.equals(Float.TYPE) || fieldType.equals(Float.class)) {
            result = Float.valueOf(value);
        } else if (fieldType.equals(Double.TYPE) || fieldType.equals(Double.class)) {
            result = Double.valueOf(value);
        } else if (fieldType.equals(String.class)) {
            result = value;
        } else {
            throw new IllegalArgumentException("unsupported field type: " + fieldType);
        }

        return result;
    }
}
