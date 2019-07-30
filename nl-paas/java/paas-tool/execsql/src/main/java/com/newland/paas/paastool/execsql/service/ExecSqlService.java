package com.newland.paas.paastool.execsql.service;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import com.newland.paas.paastool.execsql.SpringContextUtil;
import com.newland.paas.paastool.execsql.util.PaasResourceDatabasePopulator;

@Component
public class ExecSqlService {
    private static final Log logger = LogFactory.getLog(ExecSqlService.class);
    @Autowired
    private DataSourceProperties properties;
    @Autowired
    private DataSource dataSource;
    private static final String PREFIX = "file:///";
    @Value("${inOneTransaction:false}")
    private Boolean inOneTransaction;

    public void runScriptsByPath(String resourcesPath) {
        if (resourcesPath.isEmpty()) {
            return;
        }
        ApplicationContext ctx = SpringContextUtil.getContext();
        Resource resource = ctx.getResource(PREFIX + resourcesPath);;
        List<Resource> resources = new ArrayList<>();
        resources.add(resource);

        if (inOneTransaction) {
            logger.debug("执行SQL脚本，一个脚本一个事务");
            runScriptsInOneTransaction(resources);
        } else {
            logger.debug("执行SQL脚本，自动提交，一个SQL语句一个事务");
            runScripts(resources);
        }
    }

    public void runScriptsByPath(List<String> resourcesPath) {
        if (resourcesPath.isEmpty()) {
            return;
        }
        ApplicationContext ctx = SpringContextUtil.getContext();
        Resource resource = null;
        List<Resource> resources = new ArrayList<>();
        for (String string : resourcesPath) {
            resource = ctx.getResource(PREFIX + string);
            resources.add(resource);
        }
        runScripts(resources);
    }

    /**
     * 执行一个SQL脚本，事务自动提交。一条SQL语句一个事务
     * 
     * @param resources
     */
    public void runScripts(List<Resource> resources) {
        if (resources.isEmpty()) {
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(this.properties.isContinueOnError());
        populator.setSeparator(this.properties.getSeparator());
        if (this.properties.getSqlScriptEncoding() != null) {
            populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
        }
        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        DataSource dataSource = this.dataSource;
        DatabasePopulatorUtils.execute(populator, dataSource);
    }

    /**
     * 执行一个SQL脚本，事务非自动提交。一个SQL文件一个事务
     * 
     * @param resources
     */
    public void runScriptsInOneTransaction(List<Resource> resources) {
        if (resources.isEmpty()) {
            return;
        }
        PaasResourceDatabasePopulator populator = new PaasResourceDatabasePopulator();
        populator.setContinueOnError(this.properties.isContinueOnError());
        populator.setSeparator(this.properties.getSeparator());
        if (this.properties.getSqlScriptEncoding() != null) {
            populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
        }
        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        DataSource dataSource = this.dataSource;
        DatabasePopulatorUtils.execute(populator, dataSource);
    }
}
