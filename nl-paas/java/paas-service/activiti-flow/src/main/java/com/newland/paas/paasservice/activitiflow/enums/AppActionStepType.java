package com.newland.paas.paasservice.activitiflow.enums;

/**
 * AppActionStepType
 *
 * @author WRP
 * @since 2019/1/30
 */
public enum AppActionStepType {
    SHELL("shell", 0L), SQL("sql", 1L), CONFIG("config", 2L), OPERATION("app", 3L);

    private String name;
    private Long index;

    private static final Long DEFAULT_INDEX = -1L;

    AppActionStepType(String name, Long index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    /**
     * getIndex
     *
     * @param name name
     * @return return
     */
    public static Long getIndex(String name) {
        for (AppActionStepType type : AppActionStepType.values()) {
            if (type.name.equals(name)) {
                return type.index;
            }
        }
        return DEFAULT_INDEX;
    }

    /**
     * getName
     *
     * @param index index
     * @return return
     */
    public static String getName(Long index) {
        for (AppActionStepType type : AppActionStepType.values()) {
            if (type.index.equals(index)) {
                return type.name;
            }
        }
        return null;
    }

}
