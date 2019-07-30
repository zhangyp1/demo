package com.newland.paas.paasdatamodule.datacommonmodule;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

/**
 * Created by luolifeng on 2018/4/27. 拦截StatementHandler中参数类型为Connection的prepare方法
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class UpdateSqlInterceptor implements Interceptor {
    private String myName = "StatementHandler.prepare.Invc";
    private String logKey = "mybatis.Intercepts";
    private String appendColumn = "";
    private static final Log log = LogFactory.getLogger(UpdateSqlInterceptor.class);

    /**
     * 拦截逻辑实现
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // log.debug(logKey, String.format("starting: %s intercept", myName));

        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        // 通过MetaObject优雅访问对象的属性，这里是访问statementHandler的属性
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
            SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        // 先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        // 配置文件中SQL语句的ID

        String sqlId = mappedStatement.getId();
        if (sqlId.startsWith("com.xxl.job.admin.dao") || sqlId.contains("jobmgr")) {
            return invocation.proceed();
        }

        if (mappedStatement.getSqlCommandType() != SqlCommandType.UPDATE) {
            // log.debug(logKey, String.format("no match update sql"));
        } else {
            if (appendColumn == null || appendColumn == "" || appendColumn.indexOf("=") == -1) {
                return invocation.proceed();
            }
            String[] ac = appendColumn.split("=");
            if (ac[0] == null || ac[0] == "" || ac[0].trim().length() == 0) {
                return invocation.proceed();
            }

            BoundSql boundSql = statementHandler.getBoundSql();
            // 原始的SQL语句
            String sql = boundSql.getSql().trim();
            String tmpSQL = sql.toLowerCase();
            int where_s = tmpSQL.indexOf(" where ");
            int start = tmpSQL.indexOf(" set ");
            if (where_s == -1 || start == -1) {
                return invocation.proceed();
            }
            log.debug(logKey, "sql=" + sql + ",where_s=" + where_s + ",start=" + start);

            if (tmpSQL.indexOf(ac[0].toLowerCase(), start) == -1
                || tmpSQL.indexOf(ac[0].toLowerCase(), start) > where_s) {
                String my_sql =
                    String.format("%s set %s, %s", sql.substring(0, start), appendColumn, sql.substring(start + 5));
                metaObject.setValue("delegate.boundSql.sql", my_sql);
                log.debug(logKey, String.format("modify update sql:%s", my_sql));
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // log.debug(logKey, String.format("plugin target: %s;Invocation:%s", target, this));
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.myName = properties.getProperty("myName").trim();
        this.logKey = properties.getProperty("logKey").trim();
        this.appendColumn = properties.getProperty("appendColumn").trim();
        // log.debug(logKey, "loaded:properties");
        // TODO Auto-generated method stub
    }
}
