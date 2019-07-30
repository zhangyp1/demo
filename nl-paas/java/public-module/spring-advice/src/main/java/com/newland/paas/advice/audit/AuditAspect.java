package com.newland.paas.advice.audit;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.ClientIP;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.utils.SpringContextUtil;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;

/**
 * 审计日志
 *
 * @author WRP
 * @since 2018/11/8
 */
@Component
@Aspect
public class AuditAspect {

    private static final Log LOG = LogFactory.getLogger(AuditAspect.class);

    /**
     * 版本
     */
    private static final String VERSION = "v1";
    /**
     * 操作类别
     */
    private static final String CATEGORY = "system_operate";

    private static final String LOGIN_MSG = "用户【{0}】{1}系统";
    private static final String LOGOUT_MSG = "用户【{0}】{1}系统";

    /**
     * 微服务之间调用工具类
     */
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    /**
     * 微服务配置（config/common.yml）
     */
    @Autowired
    private MicroservicesProperties microservicesProperties;
    /**
     * HttpServletRequest
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private HttpServletRequest request;

    /**
     * 接收注解类型
     */
    @Pointcut(value = "@annotation(com.newland.paas.advice.audit.AuditOperate)")
    public void access() {}

    /**
     * 环绕通知,环绕增强，相当于MethodInterceptor
     *
     * @param joinPoint 参数
     * @return 返回值
     * @throws Throwable 异常
     */
    @Around("access()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 对象类型
        Object obj = joinPoint.getTarget();
        AuditObject auditObject = AnnotationUtils.findAnnotation(obj.getClass(), AuditObject.class);
        String objectType = new String();
        if (auditObject != null) {
            objectType = auditObject.value();
        }

        // 操作码、描述
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AuditOperate auditOperate = AnnotationUtils.getAnnotation(method, AuditOperate.class);
        String operateCode = auditOperate.value();
        String operateName = auditOperate.name();
        String operateDesc = auditOperate.desc();

        AuditVo auditVo = new AuditVo();
        GlbAuditVo glbAuditVo = new GlbAuditVo();
        glbAuditVo.setModule(SpringContextUtil.getApplicationName());
        glbAuditVo.setVersion(VERSION);
        glbAuditVo.setCategory(CATEGORY);
        glbAuditVo.setCreateTime(new Date());
        glbAuditVo.setIp(ClientIP.get(request));
        if (StringUtils.isEmpty(operateDesc)) {
            operateDesc = operateName;
        }
        glbAuditVo.setDesc(operateDesc);
        auditVo.setGlbAudit(glbAuditVo);

        buildGlbAuditLogAttrVo("object_type", objectType, auditVo);
        buildGlbAuditLogAttrVo("operate_code", operateCode, auditVo);
        buildGlbAuditLogAttrVo("operate_name", operateName, auditVo);
        String userId = "";
        String account = "";
        try {
            userId = RequestContext.getUserId().toString();
            account = RequestContext.getSession().getAccount();
        } catch (SystemException e) {
            account = request.getParameter("account");
        }
        buildGlbAuditLogAttrVo("operate_user", userId, auditVo);
        buildGlbAuditLogAttrVo("operate_account", account, auditVo);

        // 账号为空暂时不存
        if (StringUtils.isNotEmpty(account)) {
            saveAuditLog(auditVo);
        }

        return joinPoint.proceed();

    }

    /**
     * 异步保存审计日志
     *
     * @param auditVo 审计日志参数
     */
    @Async("AuditAspectSaveAuditLog")
    public void saveAuditLog(AuditVo auditVo) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "auditVo:" + JSON.toJSONString(auditVo));
        BasicRequestContentVo<AuditVo> requestContentVo = new BasicRequestContentVo<>();
        requestContentVo.setParams(auditVo);
        try {
            restTemplateUtils.put(microservicesProperties.getCommon(), "/v1/glbAudit/save-audit", requestContentVo,
                new ParameterizedTypeReference<BasicResponseContentVo<Void>>() {});
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, "00010009", e, "审计日志保存失败");
        }

    }

    /**
     * 审计参数列表
     *
     * @param key 键
     * @param value 值
     * @param auditVo 审计日志vo
     */
    private static void buildGlbAuditLogAttrVo(String key, String value, final AuditVo auditVo) {
        GlbAuditLogAttrVo glbAuditLogAttrVo = new GlbAuditLogAttrVo();
        glbAuditLogAttrVo.setAttrKey(key);
        glbAuditLogAttrVo.setAttrValue(value);
        auditVo.getGlbAuditLogAttr().add(glbAuditLogAttrVo);
    }

}
