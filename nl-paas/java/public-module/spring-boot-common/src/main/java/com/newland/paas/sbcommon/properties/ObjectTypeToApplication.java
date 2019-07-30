package com.newland.paas.sbcommon.properties;

/**
 * @desc 对象类型和应用模块名称的枚举
 * 
 * @author zkq
 * @date 2019/01/25
 */
public enum ObjectTypeToApplication {
    /**
     * @desc 应用模块
     */
    APPLICATION("application", "Appmgr");
    /**
     * @desc 对象类型
     */
    private String objType;
    /**
     * @desc 应用模块名
     */
    private String appName;

    private ObjectTypeToApplication(String objType, String appName) {
        this.objType = objType;
        this.appName = appName;
    }

    /**
     * @desc 根据对象类型获取应用模块名称
     * 
     * @param objType
     * @return
     */
    public static String get(String objectType) {
        String appName = null;
        ObjectTypeToApplication[] enums = ObjectTypeToApplication.class.getEnumConstants();
        for (ObjectTypeToApplication objectTypeToApplication : enums) {
            if (objectTypeToApplication.objType.equals(objectType)) {
                appName = objectTypeToApplication.appName;
            }
        }

        return appName;
    }
}
