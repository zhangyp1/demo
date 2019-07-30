package com.newland.paas.advice.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审计日志，对象类型【类级注解】
 *
 * @author WRP
 * @since 2018/11/8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditObject {

    /**
     * 对象类型
     *
     * @return
     */
    String value();

}
