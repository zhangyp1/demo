package com.newland.paas.ee.installer.clu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.newland.paas.ee.constant.ZoneConstants;
import com.newland.paas.ee.errorcode.EeErrorCode;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.CmssK8sCluInstanceDetailInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.RawClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.cluster.CluZoneVo;
import com.newland.paas.ee.vo.cluster.ZoneDetailVo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.ApplicationException;

/***
 * 集群安装卸载
 */
public class ClusterInstaller {
    private static final Log log = LogFactory.getLogger(ClusterInstaller.class);

    private static final String TAG = "ClusterInstaller======";

    public static OperateResult install(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                        Map<String, Object> paramMaps,
                                        AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) throws ApplicationException {
        if (clusterInstanceDetailInfo instanceof K8SClusterInstanceDetailInfo) {
            logK8sCluster("Install k8s cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            try (K8sClusterInstaller clusterInstaller = new K8sClusterInstaller(installConfig,
                    (K8SClusterInstanceDetailInfo) clusterInstanceDetailInfo, paramMaps, belongsTenantInfo)) {
                return clusterInstaller.install();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else if (clusterInstanceDetailInfo instanceof CmssK8sCluInstanceDetailInfo) {
            logK8sCluster("Install cmssk8s cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            try (CmssK8sClusterInstaller clusterInstaller = new CmssK8sClusterInstaller(installConfig,
                    (CmssK8sCluInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterInstaller.install();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        }else if (clusterInstanceDetailInfo instanceof RawClusterInstanceDetailInfo) {
            logK8sCluster("Install rawcluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            try (RawClusterInstaller clusterInstaller = new RawClusterInstaller(installConfig,
                    (RawClusterInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterInstaller.install();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logCluster("Install cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }

    private static void logK8sCluster(String methodName, InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                      Map<String, Object> paramMaps,
                                      AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) {
        log.debug(LogProperty.LOGTYPE_DETAIL, methodName);
        log.debug(LogProperty.LOGTYPE_DETAIL, "InstallConfig (gson)->" + new Gson().toJson(installConfig));
        String cluJson = "";
        if (clusterInstanceDetailInfo instanceof K8SClusterInstanceDetailInfo) {
            cluJson = JSONObject.toJSONString((K8SClusterInstanceDetailInfo) clusterInstanceDetailInfo);
        } else if (clusterInstanceDetailInfo instanceof CmssK8sCluInstanceDetailInfo) {
            cluJson = JSONObject.toJSONString((CmssK8sCluInstanceDetailInfo) clusterInstanceDetailInfo);
        }else if (clusterInstanceDetailInfo instanceof RawClusterInstanceDetailInfo) {
            cluJson = JSONObject.toJSONString((RawClusterInstanceDetailInfo) clusterInstanceDetailInfo);
        }
        log.debug(LogProperty.LOGTYPE_DETAIL, "ClusterInstanceDetailInfo->" + cluJson);
        log.debug(LogProperty.LOGTYPE_DETAIL, "ParamMaps->" + JSONObject.toJSONString(paramMaps));
        log.debug(LogProperty.LOGTYPE_DETAIL, "AssetDetailInfo->" + JSONObject.toJSONString(assetDetailInfo));
        log.debug(LogProperty.LOGTYPE_DETAIL, "BelongsTenantInfo->" + JSONObject.toJSONString(belongsTenantInfo));
    }

    private static void logCluster(String methodName, InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                   Map<String, Object> paramMaps,
                                   AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) {
        log.debug(LogProperty.LOGTYPE_DETAIL, methodName);
        log.debug(LogProperty.LOGTYPE_DETAIL, "InstallConfig (gson)->" + new Gson().toJson(installConfig));
        log.debug(LogProperty.LOGTYPE_DETAIL, "ClusterInstanceDetailInfo->" + JSONObject.toJSONString(clusterInstanceDetailInfo));
        log.debug(LogProperty.LOGTYPE_DETAIL, "ParamMaps->" + JSONObject.toJSONString(paramMaps));
        log.debug(LogProperty.LOGTYPE_DETAIL, "AssetDetailInfo->" + JSONObject.toJSONString(assetDetailInfo));
        log.debug(LogProperty.LOGTYPE_DETAIL, "BelongsTenantInfo->" + JSONObject.toJSONString(belongsTenantInfo));
    }

    /**
     * 卸载
     *
     * @param clusterInstanceDetailInfo 执行的对象，如集群
     * @param paramMaps                 界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
     * @param assetDetailInfo           依赖的资产详情信息
     * @param belongsTenantInfo         执行对象所属租户信息
     * @return OperateResult            操作返回结果，如：进度查询地址、Job名称、构建号等
     */
    public static OperateResult uninstall(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                          Map<String, Object> paramMaps,
                                          AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) throws ApplicationException {
        if (clusterInstanceDetailInfo instanceof K8SClusterInstanceDetailInfo) {
            logK8sCluster("Uninstall k8s cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            try (K8sClusterUninstaller clusterUninstaller = new K8sClusterUninstaller(installConfig,
                    (K8SClusterInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterUninstaller.uninstall();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        }else if(clusterInstanceDetailInfo instanceof CmssK8sCluInstanceDetailInfo) {
        	try (CmssK8sClusterUninstaller clusterUninstaller = new CmssK8sClusterUninstaller(installConfig,
        			(CmssK8sCluInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterUninstaller.uninstall();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        }else if(clusterInstanceDetailInfo instanceof RawClusterInstanceDetailInfo) {
        	try (RawClusterUninstaller clusterUninstaller = new RawClusterUninstaller(installConfig,
        			(RawClusterInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterUninstaller.uninstall();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logCluster("Uninstall cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }

    /**
     * 启动
     *
     * @param clusterInstanceDetailInfo 执行的对象，如集群
     * @param paramMaps                 界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
     * @param assetDetailInfo           依赖的资产详情信息
     * @param belongsTenantInfo         执行对象所属租户信息
     * @return OperateResult            操作返回结果，如：进度查询地址、Job名称、构建号等
     */
    public static OperateResult start(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                      Map<String, Object> paramMaps,
                                      AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) throws ApplicationException {
        if (clusterInstanceDetailInfo instanceof K8SClusterInstanceDetailInfo) {
            logK8sCluster("Start k8s cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            try (K8sClusterStartStop k8sClusterStartStop = new K8sClusterStartStop(installConfig,
                    (K8SClusterInstanceDetailInfo) clusterInstanceDetailInfo)) {
                return k8sClusterStartStop.start();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logCluster("Start cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }

    /**
     * 停止
     *
     * @param clusterInstanceDetailInfo 执行的对象，如集群
     * @param paramMaps                 界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
     * @param assetDetailInfo           依赖的资产详情信息
     * @param belongsTenantInfo         执行对象所属租户信息
     * @return OperateResult            操作返回结果，如：进度查询地址、Job名称、构建号等
     */
    public static OperateResult stop(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                     Map<String, Object> paramMaps,
                                     AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) throws ApplicationException {
        if (clusterInstanceDetailInfo instanceof K8SClusterInstanceDetailInfo) {
            logK8sCluster("Stop k8s cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            try (K8sClusterStartStop k8sClusterStartStop = new K8sClusterStartStop(installConfig,
                    (K8SClusterInstanceDetailInfo) clusterInstanceDetailInfo)) {
                return k8sClusterStartStop.stop();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logCluster("Stop cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }

    public static OperateResult scaleOut(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                         Map<String, Object> paramMaps,
                                         AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) throws ApplicationException {
        logK8sCluster("Scale out k8s cluster->", installConfig, clusterInstanceDetailInfo,
                paramMaps, assetDetailInfo, belongsTenantInfo);

        if (clusterInstanceDetailInfo instanceof K8SClusterInstanceDetailInfo) {
            try (K8sClusterScaleOut clusterInstaller = new K8sClusterScaleOut(installConfig,
                    (K8SClusterInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterInstaller.scaleOut();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        }else if (clusterInstanceDetailInfo instanceof CmssK8sCluInstanceDetailInfo) {
            try (CmssK8sClusterScaleOut clusterInstaller = new CmssK8sClusterScaleOut(installConfig,
                    (CmssK8sCluInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterInstaller.scaleOut();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logCluster("Scale out cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }

    public static OperateResult scaleIn(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                        Map<String, Object> paramMaps,
                                        AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) throws ApplicationException {
        if (clusterInstanceDetailInfo instanceof K8SClusterInstanceDetailInfo) {
            logK8sCluster("Scale in k8s cluster->", installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            try (K8sClusterScaleIn clusterInstaller = new K8sClusterScaleIn(installConfig,
                    (K8SClusterInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterInstaller.scaleIn();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else if (clusterInstanceDetailInfo instanceof CmssK8sCluInstanceDetailInfo) {
            logK8sCluster("Scale in cmssk8s cluster->", installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            try (CmssK8sClusterScaleIn clusterInstaller = new CmssK8sClusterScaleIn(installConfig,
                    (CmssK8sCluInstanceDetailInfo) clusterInstanceDetailInfo, belongsTenantInfo)) {
                return clusterInstaller.scaleIn();
            } catch (Exception e) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
            }
        } else {
            logCluster("Scale in cluster->", installConfig, clusterInstanceDetailInfo,
                    paramMaps, assetDetailInfo, belongsTenantInfo);
            throw new ApplicationException(EeErrorCode.EXEENGINE_INVALID_CLUSTER);
        }
    }


    public static OperateResult installZone(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                            Map<String, Object> paramMaps, CluZoneVo cluZoneVo) throws ApplicationException {
        log.debug(LogProperty.LOGTYPE_DETAIL, "zoneDetailInfo:" + cluZoneVo + "clusterInstanceDetailInfo:" + clusterInstanceDetailInfo);
        List<K8sClusterZoneInstaller> installList = new ArrayList<K8sClusterZoneInstaller>();
        OperateResult result = null;
        try {
            if (paramMaps == null) {
                paramMaps = new HashMap<String, Object>();
            }
            for (int i = 0; i < cluZoneVo.getClusterZones().size(); i++) {
                paramMaps.put(ZoneConstants.ZONE_PREFIX + cluZoneVo.getClusterId(), cluZoneVo.getClusterZones().get(i));
                K8sClusterZoneInstaller clusterInstaller = new K8sClusterZoneInstaller(installConfig, clusterInstanceDetailInfo, paramMaps, cluZoneVo);
                installList.add(clusterInstaller);
                result = clusterInstaller.install();
            }
        } catch (Throwable e) {
            log.error(LogProperty.LOGTYPE_DETAIL, null, e, TAG + "执行任务失败,开始执行集群分区创建的反向卸载操作," + e.getMessage());
            try {
                for (int i = installList.size() - 1; i >= 0; i--) {
                    K8sClusterZoneInstaller clusterInstaller = installList.get(i);
                    result = clusterInstaller.antiInstall();
                }
            } catch (Throwable throwable) {
                throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, throwable);
            }
        }
        return result;
    }

    @SuppressWarnings("resource")
    public static OperateResult unInstallZone(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                              Map<String, Object> paramMaps, CluZoneVo cluZoneVo) throws ApplicationException {
        log.debug(LogProperty.LOGTYPE_DETAIL, "zoneDetailInfo:" + cluZoneVo + "clusterInstanceDetailInfo:" + clusterInstanceDetailInfo);
        OperateResult result = null;
        try {
            if (paramMaps == null) {
                paramMaps = new HashMap<String, Object>();
            }
            for (int i = 0; i < cluZoneVo.getClusterZones().size(); i++) {
                paramMaps.put(ZoneConstants.ZONE_PREFIX + cluZoneVo.getClusterId(), cluZoneVo.getClusterZones().get(i));
                K8sClusterZoneInstaller clusterInstaller = new K8sClusterZoneInstaller(installConfig, clusterInstanceDetailInfo, paramMaps, cluZoneVo);
                result = clusterInstaller.antiInstall();
            }
        } catch (Throwable e) {
            log.error(LogProperty.LOGTYPE_DETAIL, null, e, TAG + "执行任务失败,开始执行集群分区创建的反向卸载操作," + e.getMessage());
            throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
        }
        return result;
    }

	public static OperateResult updateZoneQuota(InstallerConfig installConfig,
			ClusterInstanceDetailInfo clusterInstanceDetailInfo, Map<String, Object> paramMaps, CluZoneVo cluZoneVo) throws ApplicationException {
        log.debug(LogProperty.LOGTYPE_DETAIL, "zoneDetailInfo:" + cluZoneVo + "clusterInstanceDetailInfo:" + clusterInstanceDetailInfo);
        OperateResult result = null;
        try {
            for (int i = 0; i < cluZoneVo.getClusterZones().size(); i++) {
                ZoneDetailVo detailVo =  cluZoneVo.getClusterZones().get(i);
                K8sClusterZoneUpdater clusterUpdater = null;
                try{
                	clusterUpdater= new K8sClusterZoneUpdater(installConfig, clusterInstanceDetailInfo, paramMaps, detailVo);
                	result = clusterUpdater.install();
                }finally {
                	if(clusterUpdater != null) {
                		clusterUpdater.close();
                	}
                }
            }
        } catch (Throwable e) {
            log.error(LogProperty.LOGTYPE_DETAIL, null, e, TAG + "执行任务失败,开始执行集群分区创建的反向卸载操作," + e.getMessage());
            throw new ApplicationException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR, e);
        }
        return result;
    }

}
