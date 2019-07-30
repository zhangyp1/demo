package com.newland.paas.ee.constant;

public enum ApplicationStatusEnum {

	NEW("new", "新增"),
	IN_INSTALL("deploy", "部署中"),
	IN_STARTUP("start", "启动中"),
	RUNNING("run", "运行中"),
	IN_STOP("stopping", "停止中"),
	STOP("stop", "停止"),
	IN_UNINSTALL("unloading", "卸载中"),
	AFTER_UNINSTALL("unloaded", "已卸载"),

    ;

    public final String code;
    public final String name;

    ApplicationStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
