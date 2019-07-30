package com.newland.paas.advice.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审计日志，操作码、描述【方法级注解】
 * @author WRP
 * @since 2018/11/8
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditOperate {

    /**
     * 操作码
     *
     * @return
     */
    String value();

    /**
     * 中文操作码
     *
     * @return
     */
    String name();

    /**
     * 描述
     *
     * @return
     */
    String desc() default "";

}
