package com.newland.paas.sbcommon.codeid;

import java.util.Arrays;
import java.util.List;

/**
 * @desc 对象与CODE对应关系枚举
 * 
 * @author zkq
 * @date 2019年1月18日
 */
public enum ObjectToCode {
    /**
     * @desc 应用对应CODE枚举
     */
    // 需要增加应用和应用单元对应模块，及同时返回APP_INFO和APP_UNIT两张表
    APPLICATION("application", CodeIdUtil.APP_INFO + "," + CodeIdUtil.APP_UNIT);

    private String objType;

    private String codeType;
    
    public static final List<String> EXCEPT_MODULE_RIGHTS = Arrays.asList("APP_UNIT");

    private ObjectToCode(String objType, String codeType) {
        this.objType = objType;
        this.codeType = codeType;
    }

    public static String getTableName(String objType) {
        String tableName = null;
        ObjectToCode[] enums = ObjectToCode.class.getEnumConstants();
        for (ObjectToCode objectToCode : enums) {
            if (objType.equals(objectToCode.objType)) {
                tableName = objectToCode.codeType;
            }
        }

        return tableName;
    }
}
