package com.newland.paas.ee.util;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ApplicationConstants;
import com.newland.paas.ee.constant.AstIntfParamConstants;
import com.newland.paas.ee.installer.IngressDeployStepBuilder;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.application.AppConfig;
import com.newland.paas.ee.vo.application.AppProp;
import com.newland.paas.ee.vo.application.AppUnit;
import com.newland.paas.ee.vo.application.AppUnitLogConfig;
import com.newland.paas.ee.vo.asset.AstExternalCtlIntfRsp;
import com.newland.paas.ee.vo.asset.AstIntfParamRsp;
import com.newland.paas.ee.vo.cluster.ZoneDetailVo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ApplicationUtil {
    /**
     * 通过应用单元ID得到应用单元日志配置
     * 
     * @param appUnitLogConfigList 应用单元日志配置列表
     * @param appUnitId 应用单元ID
     * @return 应用单元日志配置列表
     */
    public static List<AppUnitLogConfig> getAppUnitLogByAppUnitId(List<AppUnitLogConfig> appUnitLogConfigList,
        long appUnitId) {
        List<AppUnitLogConfig> result = new LinkedList<>();
        for (AppUnitLogConfig appUnitLogConfig : appUnitLogConfigList) {
            if (appUnitLogConfig.getAppUnitId().equals(appUnitId))
                result.add(appUnitLogConfig);
        }

        return result;
    }

    /**
     * 附加属性到ansible host变量中
     * 
     * @param externalCtlIntf 外部控制接口
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param assetDetailInfo 依赖的资产详情信息
     * @param sb 属性写入到该sb
     * @param log 日志
     */
    public static void appendAppProperty(AstExternalCtlIntfRsp externalCtlIntf,
        AppInstanceDetailInfo appInstanceDetailInfo, AssetDetailInfo assetDetailInfo, StringBuilder sb, Log log) {
        for (AstIntfParamRsp param : externalCtlIntf.getParameters()) {
            // AstExternalCtlIntfRsp.parameters从外面AppInstanceDetailInfo.appConfigs取
            // AppInstanceDetailInfo appConfigs
            String propValue =
                getPropFromApp(appInstanceDetailInfo, assetDetailInfo.getAssetName(), param.getParamName(), log);
            if (!StringUtils.isEmpty(propValue)) {
                sb.append(param.getParamName());
                sb.append("=");
                sb.append(propValue);
                sb.append("\n");
            }
        }
    }

    /**
     * 附加属性到ansible host变量中，同时把在属性名称前面添加prefix组成新的属性, 添加到sbWithPrefix
     * 
     * @param externalCtlIntf 外部控制接口
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param assetDetailInfo 依赖的资产详情信息
     * @param sb 属性写入到该StringBuilder
     * @param sbWithPrefix 新的属性添加前缀后写到该StringBuilder
     * @param prefix 属性名称前面添加prefix组成新的属性
     * @param log 日志
     */
    public static void appendAppProperty(AstExternalCtlIntfRsp externalCtlIntf,
        AppInstanceDetailInfo appInstanceDetailInfo, AssetDetailInfo assetDetailInfo, StringBuilder sb,
        StringBuilder sbWithPrefix, String prefix, Log log) {
        for (AstIntfParamRsp param : externalCtlIntf.getParameters()) {
            // AstExternalCtlIntfRsp.parameters从外面AppInstanceDetailInfo.appConfigs取
            // AppInstanceDetailInfo appConfigs
            String propValue =
                getPropFromApp(appInstanceDetailInfo, assetDetailInfo.getAssetName(), param.getParamName(), log);
            if (!StringUtils.isEmpty(propValue)) {
                sb.append(param.getParamName());
                sb.append("=");
                sb.append(propValue);
                sb.append("\n");

                sbWithPrefix.append(prefix);
                sbWithPrefix.append(param.getParamName());
                sbWithPrefix.append("=");
                sbWithPrefix.append(propValue);
                sbWithPrefix.append("\n");
            }
        }
    }

    /**
     * 提取应用属性
     * 
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param assetName 资产名字
     * @param configName 配置名称
     * @param log 日志
     */
    public static String getPropFromApp(AppInstanceDetailInfo appInstanceDetailInfo, String assetName,
        String configName, Log log) {
        for (AppProp appProp : appInstanceDetailInfo.getAppProps()) {
            if (configName.equals(appProp.getPropKey())) {
                return appProp.getPropVal();
            }
        }

        log.warn(LogProperty.LOGTYPE_DETAIL, "asset_name=" + assetName + " , app_name="
            + appInstanceDetailInfo.getAppName() + " can not find app property " + configName);

        return "";
    }

    /**
     * 提取应用配置
     * 
     * @param externalCtlIntf 外部控制接口
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param assetDetailInfo 依赖的资产详情信息
     * @param sb 属性写入到该StringBuilder
     * @param log 日志
     */
    public static void appendAppConfig(AstExternalCtlIntfRsp externalCtlIntf,
        AppInstanceDetailInfo appInstanceDetailInfo, AssetDetailInfo assetDetailInfo, StringBuilder sb, Log log)
        throws Exception {
        for (AstIntfParamRsp param : externalCtlIntf.getParameters()) {
            // AstExternalCtlIntfRsp.parameters从外面AppInstanceDetailInfo.appConfigs取
            // AppInstanceDetailInfo appConfigs
            String configValue =
                getConfigFromApp(appInstanceDetailInfo, assetDetailInfo.getAssetName(), param.getParamName(), log);
            String defaultValue = param.getDefaultValue();

            if (StringUtils.isEmpty(configValue) && !StringUtils.isEmpty(defaultValue))
                configValue = defaultValue;

            if (StringUtils.isEmpty(configValue)) {
                if (param.getOptionalType().equals(AstIntfParamConstants.PARAM_REQUIRED))
                    throw new Exception("can not find parameter " + param.getParamName());
            } else {
                sb.append(param.getParamName());
                sb.append("=");
                sb.append(configValue);
                sb.append("\n");
            }
        }
    }

    /**
     * 提取应用配置, 同时把在属性名称前面添加prefix组成新的属性, 添加到sbWithPrefix
     * 
     * @param externalCtlIntf 外部控制接口
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param assetDetailInfo 依赖的资产详情信息
     * @param sb 属性写入到该StringBuilder
     * @param sbWithPrefix 新的属性添加前缀后写到该StringBuilder
     * @param prefix 属性名称前面添加prefix组成新的属性
     * @param log 日志
     */
    public static void appendAppConfig(AstExternalCtlIntfRsp externalCtlIntf,
        AppInstanceDetailInfo appInstanceDetailInfo, AssetDetailInfo assetDetailInfo, StringBuilder sb,
        StringBuilder sbWithPrefix, String prefix, Log log) throws Exception {
        for (AstIntfParamRsp param : externalCtlIntf.getParameters()) {
            // AstExternalCtlIntfRsp.parameters从外面AppInstanceDetailInfo.appConfigs取
            // AppInstanceDetailInfo appConfigs
            String configValue =
                getConfigFromApp(appInstanceDetailInfo, assetDetailInfo.getAssetName(), param.getParamName(), log);
            String defaultValue = param.getDefaultValue();

            if (StringUtils.isEmpty(configValue) && !StringUtils.isEmpty(defaultValue))
                configValue = defaultValue;

            if (StringUtils.isEmpty(configValue)) {
                if (param.getOptionalType().equals(AstIntfParamConstants.PARAM_REQUIRED))
                    throw new Exception("can not find parameter " + param.getParamName());
            } else {
                sb.append(param.getParamName());
                sb.append("=");
                sb.append(configValue);
                sb.append("\n");

                sbWithPrefix.append(prefix);
                sbWithPrefix.append(param.getParamName());
                sbWithPrefix.append("=");
                sbWithPrefix.append(configValue);
                sbWithPrefix.append("\n");
            }
        }
    }

    /**
     * 提取配置, 如果是${param}, 则从配置中读取param配置，否则返回原值
     * 
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param assetName 资产名字
     * @param configName 配置名字
     * @log 日志
     * @return 配置值或原值
     */
    public static String getIfConfig(AppInstanceDetailInfo appInstanceDetailInfo, String assetName, String configName,
        Log log) {
        if (configName.startsWith("${")) {
            String realConfigName = configName.substring(2, configName.length() - 1);
            return ApplicationUtil.getConfigFromApp(appInstanceDetailInfo, assetName, realConfigName, log);
        } else
            return configName;
    }

    /**
     * 提取配置
     * 
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param assetName 资产名字
     * @param configName 配置名字
     * @log 日志
     * @return 配置值
     */
    public static String getConfigFromApp(AppInstanceDetailInfo appInstanceDetailInfo, String assetName,
        String configName, Log log) {
        for (AppConfig appConfig : appInstanceDetailInfo.getAppConfigs()) {
            if (configName.equals(appConfig.getConfigName())) {
                return appConfig.getConfigValue();
            }
        }

        log.warn(LogProperty.LOGTYPE_DETAIL, "asset_name=" + assetName + " , app_name="
            + appInstanceDetailInfo.getAppName() + " can not find app config " + configName);

        return "";
    }

    /**
     * 附加application id到ansible host变量中
     * 
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param sb 属性写入到该StringBuilder
     */
    public static void appendAppId(AppInstanceDetailInfo appInstanceDetailInfo, StringBuilder sb) {
        sb.append("APP_ID="); // A的appid
        sb.append(appInstanceDetailInfo.getAppInfoId());
        sb.append('\n');
    }

    /**
     * 附加k8s home到ansible host变量中
     * 
     * @param k8sCidi 资产单元所属的集群信息
     * @param sb k8s home 属性写入到该StringBuilder
     */
    public static void appendK8sHome(K8SClusterInstanceDetailInfo k8sCidi, StringBuilder sb) throws Exception {
        if (StringUtils.isEmpty(k8sCidi.getHomePath())) // 即A对应的
            throw new Exception(
                "K8SClusterInstanceDetailInfo home path is empty where cluster id = " + k8sCidi.getClusterId());
        sb.append("K8S_HOME=");
        sb.append(k8sCidi.getHomePath());
        sb.append('\n');
    }

    /**
     * 附加k8s worker中任一个ip地址到ansible host变量中
     * 
     * @param k8sCidi 资产单元所属的集群信息
     * @param sb k8s worker中任一个ip地址写入到该StringBuilder
     */
    public static void appendRandomK8sWorker(K8SClusterInstanceDetailInfo k8sCidi, StringBuilder sb) throws Exception {
        if (k8sCidi.getK8sWorkIpList().isEmpty())
            throw new Exception(
                "K8SClusterInstanceDetailInfo worker ip list is empty where cluster id = " + k8sCidi.getClusterId());
        sb.append("K8S_WORKER=");
        int workerIpIndex = new Random().nextInt(k8sCidi.getK8sWorkIpList().size());
        sb.append(k8sCidi.getK8sWorkIpList().get(workerIpIndex));
        sb.append('\n');
    }

    /**
     * 附加名字空间到ansible host变量中
     * 
     * @param k8sCidi 资产单元所属的集群信息
     * @param sb namespace写入到该StringBuilder
     */
    public static void appendNamespace(K8SClusterInstanceDetailInfo k8sCidi, StringBuilder sb) throws Exception {
        ZoneDetailVo zoneDetailVo = k8sCidi.getDetailVo();
        if (zoneDetailVo == null)
            throw new Exception(
                "K8SClusterInstanceDetailInfo zone detail is null where cluster id=" + k8sCidi.getClusterId());
        if (StringUtils.isEmpty(zoneDetailVo.getZoneName()))
            throw new Exception("ZoneDetailVo zone name is empty where zone id=" + zoneDetailVo.getClusterZoneId());
        sb.append("NAME_SPACE=");
        sb.append(zoneDetailVo.getZoneName());
        sb.append("\n");
    }

    /**
     * 附加k8s master float ip地址到执行脚本
     * 
     * @param k8sCidi 资产单元所属的集群信息
     * @param sb k8s master float ip写入到该StringBuilder
     */
    public static void appendK8sMasterFloat(K8SClusterInstanceDetailInfo k8sCidi, StringBuilder sb) throws Exception {
        String k8sMasterFloatIp = k8sCidi.getFloatIp();
        if (StringUtils.isEmpty(k8sMasterFloatIp))
            throw new Exception("K8s master float ip is empty where cluster id = " + k8sCidi.getClusterId());
        sb.append("sh '''echo \\'");
        sb.append("[k8s_master_float]\n" + k8sMasterFloatIp + "\n");
    }

    /**
     * 附加docker镜像仓库到ansible host变量中
     * 
     * @param assetDetailInfo 依赖的资产详情信息
     * @param sb docker镜像中心写入到该StringBuilder
     */
    public static void appendDockerImageReposity(AssetDetailInfo assetDetailInfo, StringBuilder sb) throws Exception {
        if (StringUtils.isEmpty(assetDetailInfo.getImageRegistryAddr()))
            throw new Exception("AssetDetailInfo image path is empty where asset id = " + assetDetailInfo.getAssetId());
        sb.append("DOCKER_IMAGE_REPOSITY=");
        sb.append(assetDetailInfo.getImageRegistryAddr());
        sb.append('\n');
    }

    /**
     * 附加pods副本数到ansible host变量中
     * 
     * @param appUnit 应用单元
     * @param sb pods副本数写入到该StringBuilder
     */
    public static void appendReplicasCount(AppUnit appUnit, StringBuilder sb) {
        // if ( stagePrefix.equals("scale_out" ) || stagePrefix.equals("scale_in" ) ||
        // stagePrefix.equals("install") || stagePrefix.equals("start")) {
        sb.append("REPLICAS_COUNT=");
        sb.append(appUnit.getRunInsNum());
        sb.append("\n");
        // }
    }

    /**
     * 附加资源限制到ansible host变量中
     * 
     * @param assetDetailInfo 依赖的资产详情信息
     * @param appUnit 应用单元
     * @param sb 附加资源限制写入到该StringBuilder
     */
    public static void appendResourceLimit(AssetDetailInfo assetDetailInfo, AppUnit appUnit, StringBuilder sb)
        throws Exception {
        if (appUnit.getQuotaCpu() <= 0)
            throw new Exception("asset " + assetDetailInfo.getAssetName() + " unit " + appUnit.getUnitName()
                + " invalid cpu limit, cpu limit = " + Float.toString(appUnit.getQuotaCpu()));
        sb.append("LIMIT_CPU=");
        sb.append(Float.toString(appUnit.getQuotaCpu()));
        sb.append("\n");
        if (appUnit.getQuotaMemory() <= 0)
            throw new Exception("asset " + assetDetailInfo.getAssetName() + " unit " + appUnit.getUnitName()
                + " invalid memory limit, memory limit = " + Float.toString(appUnit.getQuotaMemory()));
        sb.append("LIMIT_MEMORY=");
        sb.append(Float.toString(appUnit.getQuotaMemory()));
        sb.append("Gi\n");
    }

    /**
     * 附加环境变量到执行脚本
     * 
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param ansibleHostName 执行ansible的host名字
     * @param sb 附加环境变量写入到该StringBuilder
     */
    public static void appendPfEnv(AppInstanceDetailInfo appInstanceDetailInfo,
        TenantInstanceDetailInfo belongsTenantInfo, AppUnit appUnit, String ansibleHostName, StringBuilder sb)
        throws Exception {
        sb.append("sh '''echo -n \\'PF_ENV=");
        sb.append("\\'>> " + ansibleHostName + "'''\n");

        sb.append("sh '''echo -n \"\\'\"");
        sb.append(">>" + ansibleHostName + "'''\n");

        // sh '''echo -n "\'">>install_tomcat-app_host_1'''

        sb.append("sh '''echo -n \\'");
        int appendCount = 0;
        for (AppConfig appConfig : appInstanceDetailInfo.getAppConfigs()) {
            if (!appConfig.getConfigName().startsWith("PF_"))
                continue;
            if (appendCount != 0)
                sb.append(",");
            sb.append("{\\\"name\\\":\\\"");
            sb.append(appConfig.getConfigName());
            sb.append("\\\",\\\"value\\\":\\\"");
            sb.append(appConfig.getConfigValue());
            sb.append("\\\"}");
            ++appendCount;
        }

        appendLogConfigEnv(appInstanceDetailInfo, belongsTenantInfo, appendCount, appUnit, sb);

        sb.append("\\'>> " + ansibleHostName + "'''\n");

        sb.append("sh '''echo -n \"\\'\\n\"");
        sb.append(">>" + ansibleHostName + "'''\n");
    }

    public static void appendLogConfigEnv(AppInstanceDetailInfo appInstanceDetailInfo,
        TenantInstanceDetailInfo belongsTenantInfo, int haveAppendEnvCount, AppUnit appUnit, StringBuilder sb)
        throws Exception {
        List<AppUnitLogConfig> appUnitLogConfigs = ApplicationUtil
            .getAppUnitLogByAppUnitId(appInstanceDetailInfo.getAppUnitLogConfigs(), appUnit.getAppUnitId());
        if (!appUnitLogConfigs.isEmpty()) {
            int goLogIndex = 1;
            for (AppUnitLogConfig appUnitLogConfig : appUnitLogConfigs) {
                if (StringUtils.isEmpty(appUnitLogConfig.getLogCategory()))
                    throw new Exception(
                        "AppUnitLogConfig log category is empty where unitLogCId=" + appUnitLogConfig.getUnitLogCId());

                if (haveAppendEnvCount > 0) {
                    sb.append(",");
                } else {
                    if (goLogIndex != 1) {
                        sb.append(",");
                    }
                }

                sb.append("{\\\"name\\\":\\\"");
                sb.append("PF_LOGPATH_" + appUnitLogConfig.getLogCategory());
                sb.append("\\\",\\\"value\\\":\\\"");
                sb.append("/nl/paas/logs/business/" + appInstanceDetailInfo.getTenantId() + "/"
                    + appInstanceDetailInfo.getAppInfoId() + "/" + appUnit.getAppUnitId() + "/"
                    + appUnitLogConfig.getLogCategory());
                sb.append("\\\"}");
                ++goLogIndex;
            }
        }
    }

    /**
     * 附加日志配置到ansible host变量中
     * 
     * @param appInstanceDetailInfo 执行的对象，如应用
     * @param belongsTenantInfo 执行对象所属租户信息
     * @param appUnit 应用单元
     * @param sb 附加日志配置写入到该StringBuilder
     */
    public static void appendLogConfig(AppInstanceDetailInfo appInstanceDetailInfo,
        TenantInstanceDetailInfo belongsTenantInfo, AppUnit appUnit, String ansibleHostName, StringBuilder sb)
        throws Exception {
        List<AppUnitLogConfig> appUnitLogConfigs = ApplicationUtil
            .getAppUnitLogByAppUnitId(appInstanceDetailInfo.getAppUnitLogConfigs(), appUnit.getAppUnitId());

        sb.append("sh '''echo -n \\'LOG_APP=");
        sb.append("\\'>> " + ansibleHostName + "'''\n");

        sb.append("sh '''echo -n \"\\'\"");
        sb.append(">>" + ansibleHostName + "'''\n");

        sb.append("sh '''echo -n \\'");
        if (!appUnitLogConfigs.isEmpty()) {
            int goLogIndex = 1;
            sb.append("[");
            for (AppUnitLogConfig appUnitLogConfig : appUnitLogConfigs) {
                if (StringUtils.isEmpty(appUnitLogConfig.getLogCategory()))
                    throw new Exception(
                        "AppUnitLogConfig log category is empty where unitLogCId=" + appUnitLogConfig.getUnitLogCId());
                if (goLogIndex != 1) {
                    sb.append(",");
                }
                sb.append("{\\\"name\\\":\\\"go-logs");
                if (goLogIndex != 1)
                    sb.append(Integer.toString(goLogIndex));
                sb.append("\\\",\\\"hostPath\\\":{\\\"path\\\":\\\"");
                sb.append("/nl/paas/logs/business/" + appInstanceDetailInfo.getTenantId() + "/"
                    + appInstanceDetailInfo.getAppInfoId() + "/" + appUnit.getAppUnitId() + "/"
                    + appUnitLogConfig.getLogCategory());
                sb.append("\"}}");
                ++goLogIndex;
            }
            sb.append("]");
        } else {
            sb.append("[");
            sb.append("{\\\"name\\\":\\\"go-logs");
            sb.append("\\\",emptyDir: {}");
            sb.append("}");
            sb.append("]");
        }

        sb.append("\\'>> " + ansibleHostName + "'''\n");

        sb.append("sh '''echo -n \"\\'\\n\"");
        sb.append(">>" + ansibleHostName + "'''\n");
    }

    /*
     * public static void appendInstallService( Long unitId, int unitIndex, String
     * ansibleHostName, StringBuilder sb ) throws Exception { String ansibleBookName
     * = "install_service_" + Long.toString(unitId) + "_" +
     * Long.toString(unitIndex); sb.append("                sh '''echo '");
     * sb.append(InstallerConfig.getInstallServiceSimpleYaml()); sb.append('\n');
     * sb.append( "'>"); sb.append(ansibleBookName); sb.append("'''\n");
     * 
     * String installServiceAnsible = InstallerConfig.getInstallServiceAnsible();
     * installServiceAnsible = installServiceAnsible.replace("{{service_yaml}}",
     * ansibleBookName);
     * 
     * sb.append("                sh '''echo '"); sb.append(installServiceAnsible);
     * sb.append('\n'); sb.append( "'>"); String installSeriviceBookName =
     * "install_service_ansible" + Long.toString(unitId) + "_" +
     * Integer.toString(unitIndex);
     * 
     * sb.append(installSeriviceBookName); sb.append("'''\n");
     * 
     * String playBookPipeline =
     * "ansiblePlaybook installation: '{{ansible_alias}}', inventory: '" +
     * ansibleHostName + "', playbook: '"+installSeriviceBookName+ "'\n";
     * sb.append(playBookPipeline); }
     */

    public static void appendUninstallService(Long unitId, int unitIndex, String ansibleHostName, StringBuilder sb)
        throws Exception {
        String uninstallServiceAnsible = InstallerConfig.getUninstallServiceAnsible();

        sb.append("                sh '''echo '");
        sb.append(uninstallServiceAnsible);
        sb.append('\n');
        sb.append("'>");
        String uninstallServiceBookName =
            "uninstall_service_ansible" + Long.toString(unitId) + "_" + Integer.toString(unitIndex);

        sb.append(uninstallServiceBookName);
        sb.append("'''\n");

        String playBookPipeline = "ansiblePlaybook installation: '{{ansible_alias}}', inventory: '" + ansibleHostName
            + "', playbook: '" + uninstallServiceBookName + "'\n";
        sb.append(playBookPipeline);
    }

    /**
     * 附加服务执行脚本到执行脚本
     * 
     * @param ansibleHostName 执行ansible的host名字
     */
    public static void appendService(IngressDeployStepBuilder ingressDeployStepBuilder, Long unitId, int unitIndex,
        String stagePrefix, String ansibleHostName, StringBuilder sb) throws Exception {
        if (stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY)
            || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_START)) {
            if (ingressDeployStepBuilder != null) {
                ingressDeployStepBuilder.appendInstallService(ansibleHostName, sb);
            }
            // todo 判断资产类型,非托管可以为空
            else {
                // throw new Exception("create service need load balance config");
                // ApplicationUtil.appendInstallService(unitId, unitIndex, ansibleHostName, sb);
            }
        } else if (stagePrefix.equals(ApplicationConstants.APPLICATION_PF_UNLOAD)
            || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_STOP)) {
            ApplicationUtil.appendUninstallService(unitId, unitIndex, ansibleHostName, sb);
        }
    }

}
