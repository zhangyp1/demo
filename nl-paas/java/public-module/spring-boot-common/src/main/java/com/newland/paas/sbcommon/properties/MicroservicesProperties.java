package com.newland.paas.sbcommon.properties;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 微服务属性配置
 *
 * @author wrp
 */
@Component
@ConfigurationProperties(prefix = "microservices")
public class MicroservicesProperties {

    private static final Log LOG = LogFactory.getLogger(MicroservicesProperties.class);

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 应用管理
     */
    private MicroservicesItemProperties appmgr;
    /**
     * 资产管理
     */
    private MicroservicesItemProperties astmgr;
    /**
     * 集群管理
     */
    private MicroservicesItemProperties clumgr;
    /**
     * 资源管理
     */
    private MicroservicesItemProperties resmgr;
    /**
     * 服务管理
     */
    private MicroservicesItemProperties svrmgr;
    /**
     * 系统管理(content-path=/paas/sysmgr)
     */
    private MicroservicesItemProperties sysmgr;
    /**
     * 系统管理(content-path=/paas)
     */
    private MicroservicesItemProperties sysmgrRoot;
    /**
     * 系统管理(content-path=/paas/common)
     */
    private MicroservicesItemProperties common;
    /**
     * 认证服务
     */
    private MicroservicesItemProperties auth;
    /**
     * activiti流程
     */
    private MicroservicesItemProperties activitiFlow;
    /**
     * loggateway
     */
    private MicroservicesItemProperties loggateway;
    /**
     * 执行引擎
     */
    private MicroservicesItemProperties executeEngine;
    /**
     * 框架项目
     */
    private MicroservicesItemProperties frwmgr;
    /**
     * 上线方案
     */
    private MicroservicesItemProperties deploymentPlan;
    /**
     * 脚本管理
     */
    private MicroservicesItemProperties scpmgr;
    /**
     * jobmgr
     */
    private MicroservicesItemProperties jobmgr;
    /**
     * harbormgr
     */
    private MicroservicesItemProperties harbormgr;

    /**
     * harbormgrnj
     */
    private MicroservicesItemProperties harbormgrnj;

    /**
     * confapi
     */
    private MicroservicesItemProperties confapi;

    /**
     * 根据serviceId获取对应配置项
     *
     * @param serviceId
     * @return
     */
    public MicroservicesItemProperties getMicroservicesItemProperties(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new SystemException(new PaasError("20900006", "服务名不能为空！"));
        }
        final String s = "get" + serviceId.replaceAll("[-_\\s]", "");
        List<Method> mpMethods = Arrays.asList(MicroservicesProperties.class.getMethods());
        Method method = mpMethods.stream()
                .filter(m -> m.getName().toUpperCase().equals(s.toUpperCase()) && m.getParameters().length == 0).findFirst()
                .orElseThrow(() -> new SystemException(new PaasError("20900007", "服务不存在！：" + serviceId)));
        try {
            return (MicroservicesItemProperties) method.invoke(this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOG.error(LogProperty.LOGCONFIG_ERRORNO, "20900008", e);
            throw new SystemException(new PaasError("20900008", "获取服务配置出错！"));
        }
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public MicroservicesItemProperties getAppmgr() {
        return appmgr;
    }

    public void setAppmgr(MicroservicesItemProperties appmgr) {
        this.appmgr = appmgr;
    }

    public MicroservicesItemProperties getAstmgr() {
        return astmgr;
    }

    public void setAstmgr(MicroservicesItemProperties astmgr) {
        this.astmgr = astmgr;
    }

    public MicroservicesItemProperties getClumgr() {
        return clumgr;
    }

    public void setClumgr(MicroservicesItemProperties clumgr) {
        this.clumgr = clumgr;
    }

    public MicroservicesItemProperties getResmgr() {
        return resmgr;
    }

    public void setResmgr(MicroservicesItemProperties resmgr) {
        this.resmgr = resmgr;
    }

    public MicroservicesItemProperties getSvrmgr() {
        return svrmgr;
    }

    public void setSvrmgr(MicroservicesItemProperties svrmgr) {
        this.svrmgr = svrmgr;
    }

    public MicroservicesItemProperties getSysmgr() {
        return sysmgr;
    }

    public void setSysmgr(MicroservicesItemProperties sysmgr) {
        this.sysmgr = sysmgr;
    }

    public MicroservicesItemProperties getAuth() {
        return auth;
    }

    public void setAuth(MicroservicesItemProperties auth) {
        this.auth = auth;
    }

    public MicroservicesItemProperties getActivitiFlow() {
        return activitiFlow;
    }

    public void setActivitiFlow(MicroservicesItemProperties activitiFlow) {
        this.activitiFlow = activitiFlow;
    }

    public MicroservicesItemProperties getCommon() {
        return common;
    }

    public void setCommon(MicroservicesItemProperties common) {
        this.common = common;
    }

    public MicroservicesItemProperties getSysmgrRoot() {
        return sysmgrRoot;
    }

    public void setSysmgrRoot(MicroservicesItemProperties sysmgrRoot) {
        this.sysmgrRoot = sysmgrRoot;
    }

    public MicroservicesItemProperties getLoggateway() {
        return loggateway;
    }

    public void setLoggateway(MicroservicesItemProperties loggateway) {
        this.loggateway = loggateway;
    }

    public MicroservicesItemProperties getExecuteEngine() {
        return executeEngine;
    }

    public void setExecuteEngine(MicroservicesItemProperties executeEngine) {
        this.executeEngine = executeEngine;
    }

    public MicroservicesItemProperties getFrwmgr() {
        return frwmgr;
    }

    public void setFrwmgr(MicroservicesItemProperties frwmgr) {
        this.frwmgr = frwmgr;
    }

    public MicroservicesItemProperties getDeploymentPlan() {
        return deploymentPlan;
    }

    public void setDeploymentPlan(MicroservicesItemProperties deploymentPlan) {
        this.deploymentPlan = deploymentPlan;
    }

    public MicroservicesItemProperties getScpmgr() {
        return scpmgr;
    }

    public void setScpmgr(MicroservicesItemProperties scpmgr) {
        this.scpmgr = scpmgr;
    }

    public MicroservicesItemProperties getJobmgr() {
        return jobmgr;
    }

    public void setJobmgr(MicroservicesItemProperties jobmgr) {
        this.jobmgr = jobmgr;
    }

    public MicroservicesItemProperties getHarbormgrnj() {
        return harbormgrnj;
    }

    public MicroservicesItemProperties getHarbormgr() {
        return harbormgr;
    }

    public void setHarbormgr(MicroservicesItemProperties harbormgr) {
        this.harbormgr = harbormgr;
    }

    public void setHarbormgrnj(MicroservicesItemProperties harbormgrnj) {
        this.harbormgrnj = harbormgrnj;
    }

    public MicroservicesItemProperties getConfapi() {
        return confapi;
    }

    public void setConfapi(MicroservicesItemProperties confapi) {
        this.confapi = confapi;
    }
}
