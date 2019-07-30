package com.newland.paas.ee.installer;

import java.util.List;
import java.util.Map;

import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ApplicationConstants;
import com.newland.paas.ee.errorcode.EeErrorCode;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.ApplicationUtil;
import com.newland.paas.ee.util.AssetUtil;
import com.newland.paas.ee.util.AstAppletUnitRspLoadBalanceUtil;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.application.AppUnit;
import com.newland.paas.ee.vo.asset.AstAppletDeployRsp;
import com.newland.paas.ee.vo.asset.AstExternalCtlIntfRsp;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * 本类和AssetAppDeployStepBuilder基本一模一样， 除了执行单元是AstAppletUnitRsp，而不是 AstUnitRsp， 对应的注释见AssetAppDeployStepBuilder
 */

public class AssetSubAppDeployStepBuilder {
    private static final Log log = LogFactory.getLogger(AssetSubAppDeployStepBuilder.class);
    private StringBuilder sb;
    private StringBuilder sbPrefixUnitPodNameConfigProp;
    private int unitIndex = 1;
    private String stagePrefix;
    private String stagePrefixHuman;
    private AssetDetailInfo assetDetailInfo;
    private Map<String, ClusterInstanceDetailInfo> targetClusterList;
    private TenantInstanceDetailInfo belongsTenantInfo;
    private AppInstanceDetailInfo appInstanceDetailInfo;
    private IngressDeployStepBuilder ingressDeployStepBuilder = null;
    private InstallerConfig installerConfig;

    public AssetSubAppDeployStepBuilder(String stagePrefix, String stagePrefixHuman, AssetDetailInfo assetDetailInfo,
        Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo,
        AppInstanceDetailInfo appInstanceDetailInfo, InstallerConfig installerConfig) {
        sb = new StringBuilder();
        sbPrefixUnitPodNameConfigProp = new StringBuilder();
        this.stagePrefix = stagePrefix;
        this.stagePrefixHuman = stagePrefixHuman;
        this.assetDetailInfo = assetDetailInfo;
        this.targetClusterList = targetClusterList;
        this.belongsTenantInfo = belongsTenantInfo;
        this.appInstanceDetailInfo = appInstanceDetailInfo;
        this.installerConfig = installerConfig;
    }

    /**
     * @param
     * @return
     */
    public String constructUnitDeployStep() throws Exception {
        constructAstAppletUnitRspDeployStep(assetDetailInfo.getUnits().get(0).getUnitDeploys());
        return sb.toString();
    }

    /**
     * @param
     * @return
     */
    private void constructAstAppletUnitRspDeployStep(List<AstAppletDeployRsp> astAppletUnitRsps) throws Exception {
        List<AstAppletUnitRspLoadBalance> astAppletUnitRspLoadBalanceList =
            AstAppletUnitRspLoadBalanceUtil.astUnitRspToDefaultLoadBalanceConfig(astAppletUnitRsps);
        if (stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY)
            || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_UNLOAD)
            || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_START)) {
            AstAppletUnitRspLoadBalanceUtil.setAstLoadBalanceConfig(astAppletUnitRspLoadBalanceList, assetDetailInfo,
                appInstanceDetailInfo);
        }

        for (AstAppletUnitRspLoadBalance astAppletUnitRspLoadBalance : astAppletUnitRspLoadBalanceList) {
            constructOneAstAppletUnitRspDeployStep(astAppletUnitRspLoadBalance);
            ++unitIndex;
        }
    }

    /**
     * @param
     * @return
     */
    private void constructOneAstAppletUnitRspDeployStep(AstAppletUnitRspLoadBalance astAppletUnitRspLoadBalance)
        throws Exception {
        AstAppletDeployRsp astAppletDeployRsp = astAppletUnitRspLoadBalance.getAstAppletDeployRsp();
        if (StringUtils.isEmpty(astAppletDeployRsp.getUnitName()))
            throw new Exception(
                "AstAppletUnitRsp unit name is empty where unit id = " + astAppletDeployRsp.getUnitId());

        ClusterInstanceDetailInfo cidi = targetClusterList.get(astAppletDeployRsp.getUnitName());
        if (cidi instanceof K8SClusterInstanceDetailInfo) {
            K8SClusterInstanceDetailInfo k8sCidi = (K8SClusterInstanceDetailInfo) cidi;
            // 每个单元 一个stage , stage name= "安装.unitName"
            appendOneAstAppletUnitRspPrefix(astAppletDeployRsp);
            String unitPrefix = stagePrefix + "_" + astAppletDeployRsp.getUnitName();
            ApplicationUtil.appendK8sMasterFloat(k8sCidi, sb);
            AstExternalCtlIntfRsp externalCtlIntf =
                getAstExternalCtlIntfRspByStage(assetDetailInfo.getCtlInterfaces(), unitPrefix);
            if (externalCtlIntf == null) {
                throw new Exception("Can not find method name = " + stagePrefix
                    + " in AssetDetailInfo.getCtlInterfaces() where AstAppletUnitRsp = "
                    + astAppletDeployRsp.getUnitName());
            }

            // 生成host
            String ansibleHostName = unitPrefix + "_host_" + unitIndex;
            resetAndCreateIngressDeployStepBuilderIfNeed(astAppletUnitRspLoadBalance, k8sCidi, astAppletDeployRsp,
                ansibleHostName);
            appendAllVars(k8sCidi, externalCtlIntf, astAppletUnitRspLoadBalance);

            // host的固定参数ansible名字, k8s名字, 镜像仓库地址
            sb.append("\\'> " + ansibleHostName + "'''\n");
            // 执行
            // 调用脚本没找到就报错 ;
            // {{ansible_host_name}}

            // 脚本名称 fileName
            // 调用安装脚本 methodName=install_单元名称开头.yaml

            AppUnit appUnit = AssetUtil.getAppUnitByUnitId(appInstanceDetailInfo, astAppletDeployRsp.getUnitId());
            if (appUnit == null)
                throw new Exception("can not find AppUnit from AppInstanceDetailInfo by unitid where unitid="
                    + astAppletDeployRsp.getUnitId() + ", AppInstanceDetailInfo.appInfoId="
                    + appInstanceDetailInfo.getAppInfoId());

            // String ansiblePlayBookName = unitPrefix + ".yaml";
            ApplicationUtil.appendPfEnv(appInstanceDetailInfo, belongsTenantInfo, appUnit, ansibleHostName, sb);

            // 附加日志配置到ansible host变量中
            ApplicationUtil.appendLogConfig(appInstanceDetailInfo, belongsTenantInfo, appUnit, ansibleHostName, sb);

            appendPlayBook(ansibleHostName, astAppletDeployRsp, externalCtlIntf);
            appendAppEndPointExistPipeline(astAppletUnitRspLoadBalance, ansibleHostName);
            // 附加服务执行脚本到执行脚本
            ApplicationUtil.appendService(ingressDeployStepBuilder, astAppletDeployRsp.getUnitId(), unitIndex,
                stagePrefix, ansibleHostName, sb);
            appendIngressPipeLineIfNeed();
            appendOneAstAppletUnitRspPostfix();
        } else {
            if (cidi == null) {
                if (stagePrefix.equals(ApplicationConstants.APPLICATION_PF_SCALE_IN)
                    || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_SCALE_OUT))
                    log.info(LogProperty.LOGTYPE_DETAIL, "asset_name=" + assetDetailInfo.getAssetName() + ", unit name="
                        + astAppletDeployRsp.getUnitName() + " can not find cluster in scale in or scale out, ignore");
                else
                    throw new NLUnCheckedException(EeErrorCode.EXEENGINE_INVALID_CLUSTER.getCode(),
                        EeErrorCode.EXEENGINE_INVALID_CLUSTER.getDescription());
            } else {
                throw new NLUnCheckedException(EeErrorCode.EXEENGINE_INVALID_CLUSTER.getCode(),
                    EeErrorCode.EXEENGINE_INVALID_CLUSTER.getDescription());
            }
        }
    }

    /**
     * @param
     * @return
     */
    private void appendAppEndPointExistPipeline(AstAppletUnitRspLoadBalance astUnitRspLoadBalance,
        String ansibleHostName) throws Exception {
        sb.append(IngressDeployStepBuilder.getAppEndPointExistPipeline(astUnitRspLoadBalance.getAppLoadBalanceConfig(),
            ansibleHostName, astUnitRspLoadBalance.getAstAppletDeployRsp().getUnitId(), unitIndex, stagePrefix));
    }

    /**
     * @param
     * @return
     */
    private void resetAndCreateIngressDeployStepBuilderIfNeed(AstAppletUnitRspLoadBalance astAppletUnitRspLoadBalance,
        K8SClusterInstanceDetailInfo k8sCidi, AstAppletDeployRsp astAppletUnitRsp, String ansibleHostName) {
        ingressDeployStepBuilder = null;
        if (astAppletUnitRspLoadBalance.haveAppLoadBalanceConfig()) {
            ingressDeployStepBuilder = new IngressDeployStepBuilder(
                astAppletUnitRspLoadBalance.getAppLoadBalanceConfig(), k8sCidi, assetDetailInfo, appInstanceDetailInfo,
                astAppletUnitRsp.getUnitId(), unitIndex, astAppletUnitRsp.getUnitName(), ansibleHostName, stagePrefix,
                installerConfig.getJedisUrl(), installerConfig.getJedisLockTimeout());
        }
    }

    /**
     * @param
     * @return
     */
    public void appendIngressPipeLineIfNeed() throws Exception {
        if (ingressDeployStepBuilder != null)
            sb.append(ingressDeployStepBuilder.getIngressPipeline());
    }

    /**
     * @param
     * @return
     */
    public void appendOneAstAppletUnitRspPrefix(AstAppletDeployRsp astAppletUnitRsp) {
        sb.append("        stage('");
        sb.append(stagePrefixHuman);
        sb.append(".");
        sb.append(astAppletUnitRsp.getUnitName());
        sb.append(".");
        sb.append(unitIndex);
        sb.append("') {\n            steps {\n");
    }

    /**
     * @param
     * @return
     */
    public void appendOneAstAppletUnitRspPostfix() {
        sb.append("\n            }");
        sb.append("\n        }\n");
    }

    /**
     * @param
     * @return
     */
    public void appendAllVars(K8SClusterInstanceDetailInfo k8sCidi, AstExternalCtlIntfRsp externalCtlIntf,
        AstAppletUnitRspLoadBalance astAppletUnitRspLoadBalance) throws Exception {
        AstAppletDeployRsp astAppletUnitRsp = astAppletUnitRspLoadBalance.getAstAppletDeployRsp();
        AppUnit appUnit = AssetUtil.getAppUnitByUnitId(appInstanceDetailInfo, astAppletUnitRsp.getUnitId());
        if (appUnit == null)
            throw new Exception("can not find AppAppletUnit from AppInstanceDetailInfo by unitid where unitid="
                + astAppletUnitRsp.getUnitId() + ", AppInstanceDetailInfo.appInfoId="
                + appInstanceDetailInfo.getAppInfoId());
        sb.append("[all:vars]\n");
        sb.append(sbPrefixUnitPodNameConfigProp);
        ApplicationUtil.appendK8sHome(k8sCidi, sb);
        ApplicationUtil.appendDockerImageReposity(assetDetailInfo, sb);
        appendApplicationImage(astAppletUnitRsp);
        // 附加模块名字到ansible host变量中
        appendApplicationModule(astAppletUnitRsp);
        String prefix = astAppletUnitRsp.getUnitName() + "__" + unitIndex + "__";
        ApplicationUtil.appendRandomK8sWorker(k8sCidi, sb);
        ApplicationUtil.appendAppConfig(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb,
            sbPrefixUnitPodNameConfigProp, prefix, log);
        ApplicationUtil.appendAppProperty(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb,
            sbPrefixUnitPodNameConfigProp, prefix, log);
        ApplicationUtil.appendReplicasCount(appUnit, sb);
        appendDeployName(astAppletUnitRsp);
        ApplicationUtil.appendNamespace(k8sCidi, sb);
        ApplicationUtil.appendResourceLimit(assetDetailInfo, appUnit, sb);
        ApplicationUtil.appendAppId(appInstanceDetailInfo, sb);
        appendAppletUnitId(astAppletUnitRsp);
        // appendIngressVariantIfNeed();
    }

    /**
     * @param
     * @return
     */
    private void appendAppletUnitId(AstAppletDeployRsp astAppletUnitRsp) {
        sb.append("UNIT_ID=");
        sb.append(astAppletUnitRsp.getUnitId());
        sb.append('\n');
    }

    /**
     * @param
     * @return
     */
    public void appendApplicationImage(AstAppletDeployRsp astAppletUnitRsp) throws Exception {
        // int imageIndex = 1;
        // for (AstUnitModuleRsp astAppletUnitModuleRsp : astAppletUnitRsp.getUnitModules()) {
        // sb.append("APPLICATION_IMAGE_");
        // sb.append(imageIndex);
        // sb.append("=");
        // if (StringUtils.isEmpty(astAppletUnitModuleRsp.getModuleName()))
        // throw new Exception("AstAppletUnitModuleRsp module name is empty where unit module id = "
        // + astAppletUnitModuleRsp.getUnitModuleId());
        // sb.append(astAppletUnitModuleRsp.getModuleName());
        // sb.append(":");
        //
        // if (StringUtils.isEmpty(astAppletUnitModuleRsp.getModuleVersion()))
        // throw new Exception("AstAppletUnitModuleRsp module version is empty where unit module id = "
        // + astAppletUnitModuleRsp.getUnitModuleId());
        // sb.append(astAppletUnitModuleRsp.getModuleVersion());
        // sb.append('\n');
        // ++imageIndex;
        // }
    }

    /**
     * 附加模块名字到ansible host变量中
     * 
     * @param astUnitRsp 资产单元
     */
    public void appendApplicationModule(AstAppletDeployRsp astUnitRsp) throws Exception {
        // int imageIndex = 1;
        // for (AstUnitModuleRsp astUnitModuleRsp : astUnitRsp.getUnitModules()) {
        // sb.append("APPLICATION_MODULE_");
        // sb.append(imageIndex);
        // sb.append("=");
        // if (StringUtils.isEmpty(astUnitModuleRsp.getModuleName()))
        // throw new Exception("AstUnitModuleRsp module name is empty where unit module id = "
        // + astUnitModuleRsp.getUnitModuleId());
        // sb.append(astUnitModuleRsp.getModuleName());
        // sb.append('\n');
        // ++imageIndex;
        // }
    }

    /**
     * @param
     * @return
     */
    void appendPlayBook(String ansibleHostName, AstAppletDeployRsp astAppletUnitRsp,
        AstExternalCtlIntfRsp externalCtlIntf) throws Exception {
        if (StringUtils.isEmpty(externalCtlIntf.getMethodFileName()))
            throw new Exception(stagePrefixHuman + " " + astAppletUnitRsp.getUnitName() + " method file name is empty");
        if (StringUtils.isEmpty(assetDetailInfo.getAssetName()))
            throw new Exception("AssetDetailInfo asset name is emtpy where asset id = " + assetDetailInfo.getAssetId());
        if (StringUtils.isEmpty(assetDetailInfo.getSelectedVersion()))
            throw new Exception(
                "AssetDetailInfo selected version is emtpy where asset id = " + assetDetailInfo.getAssetId());
        String playBookName = assetDetailInfo.getAssetName() + "-" + assetDetailInfo.getSelectedVersion() + "/"
            + externalCtlIntf.getMethodFileName();
        String playBookPipeline = "ansiblePlaybook installation: '{{ansible_alias}}', inventory: '" + ansibleHostName
            + "', playbook: '" + playBookName + "'\n";
        sb.append(playBookPipeline);
    }

    /**
     * @param
     * @return
     */
    private void appendDeployName(AstAppletDeployRsp astAppletUnitRsp) throws Exception {
        String deployName = AssetUtil.getDeployName(assetDetailInfo, astAppletUnitRsp.getUnitId(),
            astAppletUnitRsp.getUnitName(), appInstanceDetailInfo);
        sb.append("POD_NAME=");
        sb.append(deployName);
        sb.append("\n");

        sbPrefixUnitPodNameConfigProp.append(astAppletUnitRsp.getUnitName());
        sbPrefixUnitPodNameConfigProp.append("__");
        sbPrefixUnitPodNameConfigProp.append(unitIndex);
        sbPrefixUnitPodNameConfigProp.append("__");
        sbPrefixUnitPodNameConfigProp.append("POD_NAME=");
        sbPrefixUnitPodNameConfigProp.append(deployName);
        sbPrefixUnitPodNameConfigProp.append("\n");
    }

    /**
     * @param
     * @return
     */
    AstExternalCtlIntfRsp getAstExternalCtlIntfRspByStage(List<AstExternalCtlIntfRsp> ctlInterfaces,
        String stagePrefix) {
        for (AstExternalCtlIntfRsp externalCtlIntf : ctlInterfaces)
            if (externalCtlIntf.getMethodName().equals(stagePrefix))
                return externalCtlIntf;

        return null;
    }
}
