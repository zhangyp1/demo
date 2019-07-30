package com.newland.paas.ee.installer.clu;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.newland.paas.ee.errorcode.EeErrorCode;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.*;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.ApplicationException;

/**
 * 安装、卸载ingress
 */
public class IngressInstaller {
    private static final Log log = LogFactory.getLogger(IngressInstaller.class);

    /**
     * 安装ingress
     * @param installConfig
     * @param cidi
     * @param belongsTenantInfo
     * @param ingressInfo
     * @return
     * @throws ApplicationException
     */
    public static OperateResult installIngress(InstallerConfig installConfig, ClusterInstanceDetailInfo cidi,
                                               TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo) throws ApplicationException {
        if (cidi instanceof K8SClusterInstanceDetailInfo) {
            logK8sIngress("Install k8s ingress->", installConfig, (K8SClusterInstanceDetailInfo) cidi, belongsTenantInfo, ingressInfo);
            try (K8sIngressInstaller k8sIngressInstaller = new K8sIngressInstaller("install_k8s_ingress", installConfig,
                    (K8SClusterInstanceDetailInfo) cidi, belongsTenantInfo, ingressInfo, "install_k8s_ingress.yaml")) {
                return k8sIngressInstaller.install();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logIngress("Install ingress->", installConfig, cidi, belongsTenantInfo, ingressInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }

    /**
     * 卸载ingress
     * @param installConfig
     * @param cidi
     * @param belongsTenantInfo
     * @param ingressInfo
     * @return
     * @throws ApplicationException
     */
    public static OperateResult uninstallIngress(InstallerConfig installConfig, ClusterInstanceDetailInfo cidi,
                                                 TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo) throws ApplicationException {
        if (cidi instanceof K8SClusterInstanceDetailInfo) {
            logK8sIngress("Uninstall k8s ingress->", installConfig, (K8SClusterInstanceDetailInfo) cidi, belongsTenantInfo, ingressInfo);
            try (K8sIngressInstaller k8sIngressInstaller = new K8sIngressInstaller("uninstall_k8s_ingress", installConfig,
                    (K8SClusterInstanceDetailInfo) cidi, belongsTenantInfo, ingressInfo, "uninstall_k8s_ingress.yaml")) {
                return k8sIngressInstaller.install();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logIngress("Uninstall cluster->", installConfig, cidi, belongsTenantInfo, ingressInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }

    private static void logIngress(String installMethod, InstallerConfig installConfig,
                                   ClusterInstanceDetailInfo cidi, TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo) {
        log.debug(LogProperty.LOGTYPE_DETAIL, installMethod);
        log.debug(LogProperty.LOGTYPE_DETAIL, "InstallConfig (gson)->" + new Gson().toJson(installConfig));
        log.debug(LogProperty.LOGTYPE_DETAIL, "ClusterInstanceDetailInfo->" + JSONObject.toJSONString(cidi));
        log.debug(LogProperty.LOGTYPE_DETAIL, "BelongsTenantInfo->" + JSONObject.toJSONString(belongsTenantInfo));
        log.debug(LogProperty.LOGTYPE_DETAIL, "IngressInfo->" + JSONObject.toJSONString(ingressInfo));
    }

    private static void logK8sIngress(String installMethod, InstallerConfig installConfig,
                                      K8SClusterInstanceDetailInfo cidi, TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo) {
        log.debug(LogProperty.LOGTYPE_DETAIL, installMethod);
        log.debug(LogProperty.LOGTYPE_DETAIL, "InstallConfig (gson)->" + new Gson().toJson(installConfig));
        log.debug(LogProperty.LOGTYPE_DETAIL, "ClusterInstanceDetailInfo->" + JSONObject.toJSONString(cidi));
        log.debug(LogProperty.LOGTYPE_DETAIL, "BelongsTenantInfo->" + JSONObject.toJSONString(belongsTenantInfo));
        log.debug(LogProperty.LOGTYPE_DETAIL, "IngressInfo->" + JSONObject.toJSONString(ingressInfo));
    }
}
