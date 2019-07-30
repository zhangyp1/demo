package com.newland.paas.ee.installer;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ApplicationConstants;
import com.newland.paas.ee.constant.AssetConstants;
import com.newland.paas.ee.errorcode.EeErrorCode;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.ApplicationUtil;
import com.newland.paas.ee.util.AssetUtil;
import com.newland.paas.ee.util.AstUnitRspLoadBalanceUtil;
import com.newland.paas.ee.vo.*;
import com.newland.paas.ee.vo.application.AppConfig;
import com.newland.paas.ee.vo.application.AppUnit;
import com.newland.paas.ee.vo.asset.*;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.*;

public class AssetAppDeployStepBuilder {
	private static final Log log = LogFactory.getLogger(AssetAppDeployStepBuilder.class);
	private StringBuilder sb ;
	private StringBuilder sbPrefixUnitPodNameConfigProp;
	private int unitIndex = 1;
	private String stagePrefix;
	private String stagePrefixHuman;
	private AssetDetailInfo assetDetailInfo;
	private Map<String, ClusterInstanceDetailInfo> targetClusterList;
	private TenantInstanceDetailInfo belongsTenantInfo;
	private AppInstanceDetailInfo appInstanceDetailInfo;
	private IngressDeployStepBuilder ingressDeployStepBuilder = null ;
	private InstallerConfig installerConfig;

	/**
	 * 构造函数
	 * @param  stagePrefix 执行前缀  比如部署就是pf_deploy
	 * @param  stagePrefixHuman 执行前缀，适合人观看， 比如用启动来代替start
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param targetClusterList	单元集群信息 Map<单元名称, 集群信息>
	 * @param belongsTenantInfo			执行对象所属租户信息
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param installerConfig		安装配置
	 */
	public AssetAppDeployStepBuilder(String stagePrefix, String stagePrefixHuman, AssetDetailInfo assetDetailInfo,
			Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo,
			AppInstanceDetailInfo appInstanceDetailInfo, InstallerConfig installerConfig ) {
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
	 * 构造应用的执行脚本
	 * @return 返回应用的执行脚本
	 */
	public String constructUnitDeployStep() throws Exception {
			constructAstUnitRspDeployStep(assetDetailInfo.getUnits());
			return sb.toString();
	}

	/**
	 * 构造部署脚本
	 * @param astUnitRspsUnsort 资产单元列表
	 */
	private void constructAstUnitRspDeployStep(List<AstUnitRsp> astUnitRspsUnsort) throws Exception {
		//根据unitDepends依赖进行排序
		List<AstUnitRsp> astUnitRsps = sortAstUnitRsps(astUnitRspsUnsort);

        //设置每个资产单元的负载均衡
		List<AstUnitRspLoadBalance> astUnitRspLoadBalances =AstUnitRspLoadBalanceUtil.astUnitRspToDefaultLoadBalanceConfig(astUnitRsps);
		if ( stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY) || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_UNLOAD) ||
				stagePrefix.equals(ApplicationConstants.APPLICATION_PF_START)) {
			AstUnitRspLoadBalanceUtil.setAstLoadBalanceConfig(astUnitRspLoadBalances, assetDetailInfo, appInstanceDetailInfo);
		}

		//构造每个资产单元的部署脚本
		for ( AstUnitRspLoadBalance astUnitRspLoadBalance : astUnitRspLoadBalances ) {
			 AstUnitRsp astUnitRsp = astUnitRspLoadBalance.getAstUnitRsp();
			 if(AssetConstants.ASSET_UNIT_TYPE_METALMACHINE.equals(astUnitRsp.getUnitType())){
				 appendOneAstUnitRspPrefix(astUnitRsp);
				 String unitPrefix = stagePrefix + "_" + astUnitRsp.getUnitName();
				 String prefix = astUnitRsp.getUnitName() + "__" +  unitIndex + "__";
				 //根据执行单元查找控制接口
				 AstExternalCtlIntfRsp externalCtlIntf = getAstExternalCtlIntfRspByStage(assetDetailInfo.getCtlInterfaces(), unitPrefix);
				 if (externalCtlIntf == null) {
					throw new Exception("Can not find method name = " + stagePrefix + " in AssetDetailInfo.getCtlInterfaces() where AstUnitRsp = " + astUnitRsp.getUnitName() );
				 }
				 ClusterInstanceDetailInfo cidi = targetClusterList.get(astUnitRsp.getUnitName());
				 K8SClusterInstanceDetailInfo k8sCidi = (K8SClusterInstanceDetailInfo) cidi;
				 if(k8sCidi.getK8sWorkers() == null || k8sCidi.getK8sWorkers().size() == 0) {
					 throw new Exception("unable to get the physicalcluster worker,clusterId: " + k8sCidi.getClusterId());
				 }
				 String k8sMasterFloatIp = k8sCidi.getK8sWorkers().get(0).getIpAddress();
				 if (StringUtils.isEmpty(k8sMasterFloatIp))
					throw new Exception("physicalcluster first worker is empty where cluster id = " + k8sCidi.getClusterId());
				 sb.append("sh '''echo \\'");
				 sb.append("[k8s_master_float]\n" + k8sMasterFloatIp + "\n");
				 sb.append("[all:vars]\n");	
				 AppUnit appUnit = AssetUtil.getAppUnitByUnitId(appInstanceDetailInfo, astUnitRsp.getUnitId());
				 
				 List<AppConfig> appConfigs = appInstanceDetailInfo.getAppConfigs();
				 for(AppConfig appConfig : appConfigs) {
					  sb.append(appConfig.getConfigName());
					  sb.append("=");
					  sb.append(appConfig.getConfigValue());
					  sb.append("\n");
				 }
				 //附加配置到ansible host变量中
				 ApplicationUtil.appendAppConfig(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb,sbPrefixUnitPodNameConfigProp, prefix, log);
				 //附加属性到ansible host变量中
				 ApplicationUtil.appendAppProperty(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb,sbPrefixUnitPodNameConfigProp, prefix, log);
				 ApplicationUtil.appendAppId(appInstanceDetailInfo, sb);
				 appendUnitId(astUnitRsp);
				 
				String ansibleHostName = unitPrefix + "_host_" + unitIndex;
				sb.append("\\'> " + ansibleHostName + "'''\n");
				if ( StringUtils.isEmpty(externalCtlIntf.getMethodFileName() ) )
					throw new Exception(stagePrefixHuman + " " + astUnitRsp.getUnitName() + " method file name is empty");
				if ( StringUtils.isEmpty(assetDetailInfo.getAssetName()))
					throw new Exception("AssetDetailInfo asset name is emtpy where asset id = " + assetDetailInfo.getAssetId());
				if ( StringUtils.isEmpty(assetDetailInfo.getSelectedVersion()))
					throw new Exception("AssetDetailInfo selected version is emtpy where asset id = " + assetDetailInfo.getAssetId());
				String  playBookName = assetDetailInfo.getAssetName() + "-" + assetDetailInfo.getSelectedVersion() + "/" + externalCtlIntf.getMethodFileName() ;
				String playBookPipeline = "ansiblePlaybook installation: '{{ansible_alias}}', inventory: '" + ansibleHostName + "', playbook: '"+playBookName+ "'\n";
				sb.append(playBookPipeline);
				appendOneAstUnitRspPostfix();
			 }else {
				 constructOneAstUnitRspDeployStep(astUnitRspLoadBalance);
			 }
			++unitIndex;
		}
	}

	/**
	 * 构造单个单元的部署脚本
	 * @param astUnitRspLoadBalance  资产单元和负载均衡列表
	 */
	private void constructOneAstUnitRspDeployStep(AstUnitRspLoadBalance astUnitRspLoadBalance) throws Exception 	{
		AstUnitRsp astUnitRsp = astUnitRspLoadBalance.getAstUnitRsp();
		//每个单元 一个stage   ,  stage name= "安装.unitName"
		if (StringUtils.isEmpty(astUnitRsp.getUnitName()))
			throw new Exception("AstUnitRsp unit name is empty where unit id = " + astUnitRsp.getUnitId());

		//查找应用单元的集群
		ClusterInstanceDetailInfo cidi = targetClusterList.get(astUnitRsp.getUnitName());
		if (cidi instanceof K8SClusterInstanceDetailInfo) {
			K8SClusterInstanceDetailInfo k8sCidi = (K8SClusterInstanceDetailInfo) cidi;
			//附加每个资产单元执行脚本的前缀
			appendOneAstUnitRspPrefix(astUnitRsp);
			String unitPrefix = stagePrefix + "_" + astUnitRsp.getUnitName();

			//附加k8s master float ip地址到执行脚本
			ApplicationUtil.appendK8sMasterFloat(k8sCidi, sb);

			//根据执行单元查找控制接口
			AstExternalCtlIntfRsp externalCtlIntf = getAstExternalCtlIntfRspByStage(assetDetailInfo.getCtlInterfaces(), unitPrefix);
			if (externalCtlIntf == null) {
				throw new Exception("Can not find method name = " + stagePrefix + " in AssetDetailInfo.getCtlInterfaces() where AstUnitRsp = " + astUnitRsp.getUnitName() );
			}

			//生成host
			String ansibleHostName = unitPrefix + "_host_" + unitIndex;
			resetAndCreateIngressDeployStepBuilderIfNeed(astUnitRspLoadBalance, k8sCidi, astUnitRsp, ansibleHostName);
			appendAllVars(k8sCidi, externalCtlIntf, astUnitRspLoadBalance);

			sb.append("\\'> " + ansibleHostName + "'''\n");

			//附加日志配置到ansible host变量中
			AppUnit appUnit = AssetUtil.getAppUnitByUnitId(appInstanceDetailInfo, astUnitRsp.getUnitId());
			if ( appUnit == null )
				throw new Exception("can not find AppUnit from AppInstanceDetailInfo by unitid where unitid=" + astUnitRsp.getUnitId() +
						", AppInstanceDetailInfo.appInfoId=" + appInstanceDetailInfo.getAppInfoId());

			//附加环境变量到执行脚本
			ApplicationUtil.appendPfEnv(appInstanceDetailInfo, belongsTenantInfo, appUnit, ansibleHostName, sb);
			ApplicationUtil.appendLogConfig(appInstanceDetailInfo, belongsTenantInfo, appUnit, ansibleHostName, sb);


			//附加ansible play book名字到执行脚本
			appendPlayBook(ansibleHostName, astUnitRsp, externalCtlIntf);
			//附加打印应用接入点到执行脚本
			appendAppEndPointExistPipeline(astUnitRspLoadBalance, ansibleHostName);
			//附加服务执行脚本到执行脚本
			ApplicationUtil.appendService(ingressDeployStepBuilder, astUnitRsp.getUnitId(), unitIndex, stagePrefix, ansibleHostName, sb);
			//附加ingress执行步骤到执行脚本
			appendIngressPipeLineIfNeed();
			//附加执行脚本结束符号
			appendOneAstUnitRspPostfix();
		}
		else {
			//扩容和缩容时找不到集群信息不需要报错，只是不执行， 因为前台只传入需要执行的资产单元的集群信息
			if ( cidi==null ){
				if ( stagePrefix.equals(ApplicationConstants.APPLICATION_PF_SCALE_IN) || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_SCALE_OUT))
					log.info(LogProperty.LOGTYPE_DETAIL, "asset_name=" + assetDetailInfo.getAssetName() +
							", unit name=" + astUnitRsp.getUnitName() + " can not find cluster in scale in or scale out, ignore");
				else
					if ( assetDetailInfo.getAssetType().equals(AssetConstants.ASSET_TYPE_NON_MANAGER) ) {
						doNonManagerInstall(astUnitRsp);
					}
					else {
						throw new NLUnCheckedException(EeErrorCode.EXEENGINE_INVALID_CLUSTER.getCode(),
								EeErrorCode.EXEENGINE_INVALID_CLUSTER.getDescription());
					}
			}
			else {
				throw new NLUnCheckedException(EeErrorCode.EXEENGINE_INVALID_CLUSTER.getCode(),
						EeErrorCode.EXEENGINE_INVALID_CLUSTER.getDescription());
			}
		}
	}

    private void doNonManagerInstall(AstUnitRsp astUnitRsp) throws Exception {
		//附加每个资产单元执行脚本的前缀
		appendOneAstUnitRspPrefix(astUnitRsp);
		String unitPrefix = stagePrefix + "_" + astUnitRsp.getUnitName();

		//根据执行单元查找控制接口
		AstExternalCtlIntfRsp externalCtlIntf = getAstExternalCtlIntfRspByStage(assetDetailInfo.getCtlInterfaces(), unitPrefix);
		if (externalCtlIntf == null) {
			throw new Exception("Can not find method name = " + stagePrefix + " in AssetDetailInfo.getCtlInterfaces() where AstUnitRsp = " + astUnitRsp.getUnitName() );
		}

		//生成host
		String ansibleHostName = unitPrefix + "_host_" + unitIndex;
		sb.append("sh '''echo \\'");
		sb.append("\\'> " + ansibleHostName + "'''\n");
		appendPlayBook(ansibleHostName, astUnitRsp, externalCtlIntf);
		appendOneAstUnitRspPostfix();
	}

	/**
	 * 附加打印应用接入点到执行脚本
	 * @param astUnitRspLoadBalance  资产单元与负载均衡的列表
	 * @param ansibleHostName 执行ansible的host名字
	 */
	private void appendAppEndPointExistPipeline(AstUnitRspLoadBalance astUnitRspLoadBalance, String ansibleHostName ) throws Exception {
		sb.append(IngressDeployStepBuilder.getAppEndPointExistPipeline(astUnitRspLoadBalance.getAppLoadBalanceConfig(),
				ansibleHostName, astUnitRspLoadBalance.getAstUnitRsp().getUnitId(), unitIndex, stagePrefix));
	}

	/**
	 * 如果需要则创建IngressDeployStepBuilder
	 * @param astUnitRspLoadBalance  资产单元与负载均衡的列表
	 * @param k8sCidi 资产单元所属的集群信息
	 * @param astUnitRsp 资产单元
	 * @param ansibleHostName 执行ansible的host名字
	 */
	private void resetAndCreateIngressDeployStepBuilderIfNeed(AstUnitRspLoadBalance astUnitRspLoadBalance, K8SClusterInstanceDetailInfo k8sCidi,
			AstUnitRsp astUnitRsp, String ansibleHostName) {
		ingressDeployStepBuilder = null;
		if ( astUnitRspLoadBalance.haveAppLoadBalanceConfig() ) {
			ingressDeployStepBuilder = new IngressDeployStepBuilder(
					astUnitRspLoadBalance.getAppLoadBalanceConfig(), k8sCidi, assetDetailInfo,
					appInstanceDetailInfo, astUnitRsp.getUnitId(), unitIndex,
					astUnitRsp.getUnitName(), ansibleHostName, stagePrefix, installerConfig.getJedisUrl(), installerConfig.getJedisLockTimeout());
		}
	}

	/**
	 * 附加ingress执行步骤到执行脚本
	 */
	public void appendIngressPipeLineIfNeed() throws Exception{
		if ( ingressDeployStepBuilder != null )
				sb.append(ingressDeployStepBuilder.getIngressPipeline());
	}

	/**
	 * 附加每个资产单元执行脚本的前缀
	 * @param astUnitRsp 资产单元
	 */
	public void appendOneAstUnitRspPrefix( AstUnitRsp astUnitRsp ) {
		sb.append("        stage('");
		sb.append(stagePrefixHuman);
		sb.append(".");
		sb.append(astUnitRsp.getUnitName());
		sb.append(".");
		sb.append(unitIndex);
		sb.append("') {\n            steps {\n");
	}

	/**
	 * 附加执行脚本结束符号
	 */
	public void appendOneAstUnitRspPostfix(  ) {
		sb.append("\n            }");
		sb.append("\n        }\n");
	}

	/**
	 * 附加所有变量
	 * @param k8sCidi 资产单元所属的集群信息
	 * @param externalCtlIntf 资产控制接口
	 * @param astUnitRspLoadBalance  资产单元和负载均衡列表
	 */
	public void appendAllVars(K8SClusterInstanceDetailInfo k8sCidi, AstExternalCtlIntfRsp externalCtlIntf,
			AstUnitRspLoadBalance astUnitRspLoadBalance) throws Exception{
		AstUnitRsp astUnitRsp = astUnitRspLoadBalance.getAstUnitRsp();

		//根据unitid得到App执行单元
		AppUnit appUnit = AssetUtil.getAppUnitByUnitId(appInstanceDetailInfo, astUnitRsp.getUnitId());
		if ( appUnit == null )
			throw new Exception("can not find AppUnit from AppInstanceDetailInfo by unit id where unit id=" + astUnitRsp.getUnitId() +
					", AppInstanceDetailInfo.appInfoId=" + appInstanceDetailInfo.getAppInfoId());
		sb.append("[all:vars]\n");
		//附加前面资产单元的配置变量到当前单元
		sb.append(sbPrefixUnitPodNameConfigProp);
		//附加k8s home到ansible host变量中
		ApplicationUtil.appendK8sHome(k8sCidi, sb);
		//附加docker镜像仓库到ansible host变量中
		ApplicationUtil.appendDockerImageReposity(assetDetailInfo, sb);
		//附加应用镜像到ansible host变量中
		appendApplicationImage(astUnitRsp);
		//附加模块名字到ansible host变量中
		appendApplicationModule(astUnitRsp);

		String prefix = astUnitRsp.getUnitName() + "__" +  unitIndex + "__";
		//附加k8s worker中任一个ip地址到ansible host变量中
		ApplicationUtil.appendRandomK8sWorker(k8sCidi, sb);
		//附加配置到ansible host变量中
		ApplicationUtil.appendAppConfig(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb,
				sbPrefixUnitPodNameConfigProp, prefix, log);
		//附加属性到ansible host变量中
		ApplicationUtil.appendAppProperty(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb,
				sbPrefixUnitPodNameConfigProp, prefix, log);
		//附加pods副本数到ansible host变量中
		ApplicationUtil.appendReplicasCount(appUnit, sb);
		//附加pod名字到ansible host变量中
		appendDeployName(astUnitRsp);
		//附加名字空间到ansible host变量中
		ApplicationUtil.appendNamespace(k8sCidi, sb);
		//附加资源限制到ansible host变量中
		ApplicationUtil.appendResourceLimit(assetDetailInfo, appUnit, sb);
		//附加application id到ansible host变量中
		ApplicationUtil.appendAppId(appInstanceDetailInfo, sb);
		//附加资产单元的id到ansible host变量中
		appendUnitId(astUnitRsp);
	}

	/**
	 * 附加资产单元的id到ansible host变量中
	 * @param astUnitRsp 资产单元
	 */
	private void appendUnitId(AstUnitRsp astUnitRsp) {
		sb.append("UNIT_ID=");
		sb.append(astUnitRsp.getUnitId());
		sb.append('\n');
	}

	/**
	 * 附加应用镜像到ansible host变量中
	 * @param astUnitRsp 资产单元
	 */
	public void appendApplicationImage(AstUnitRsp astUnitRsp) throws Exception {
		int imageIndex = 1;
		for ( AstUnitModuleRsp astUnitModuleRsp : astUnitRsp.getUnitModules()) {
			sb.append("APPLICATION_IMAGE_");
			sb.append(imageIndex);
			sb.append("=");
			if (StringUtils.isEmpty(astUnitModuleRsp.getModuleName()))
				throw new Exception( "AstUnitModuleRsp module name is empty where unit module id = " 	+ astUnitModuleRsp.getUnitModuleId());
			sb.append(astUnitModuleRsp.getModuleName());
			sb.append(":");

			if (StringUtils.isEmpty(astUnitModuleRsp.getModuleVersion()))
				throw new Exception( "AstUnitModuleRsp module version is empty where unit module id = " + astUnitModuleRsp.getUnitModuleId());
			sb.append(astUnitModuleRsp.getModuleVersion());
			sb.append('\n');
			++imageIndex;
		}
	}

	/**
	 * 附加模块名字到ansible host变量中
	 * @param astUnitRsp 资产单元
	 */
	public void appendApplicationModule(AstUnitRsp astUnitRsp) throws Exception {
		int imageIndex = 1;
		for ( AstUnitModuleRsp astUnitModuleRsp : astUnitRsp.getUnitModules()) {
			sb.append("APPLICATION_MODULE_");
			sb.append(imageIndex);
			sb.append("=");
			if (StringUtils.isEmpty(astUnitModuleRsp.getModuleName()))
				throw new Exception( "AstUnitModuleRsp module name is empty where unit module id = " 	+ astUnitModuleRsp.getUnitModuleId());
			sb.append(astUnitModuleRsp.getModuleName());
			sb.append('\n');
			++imageIndex;
		}
	}

	/**
	 * 附加ansible play book名字到执行脚本
	 * @param ansibleHostName 执行ansible的host名字	 *
	 * @param astUnitRsp 资产单元
	 * @param externalCtlIntf 外部控制接口
	 */
	void appendPlayBook(String ansibleHostName, AstUnitRsp astUnitRsp, AstExternalCtlIntfRsp externalCtlIntf) throws Exception {
		if ( StringUtils.isEmpty(externalCtlIntf.getMethodFileName() ) )
			throw new Exception(stagePrefixHuman + " " + astUnitRsp.getUnitName() + " method file name is empty");
		if ( StringUtils.isEmpty(assetDetailInfo.getAssetName()))
			throw new Exception("AssetDetailInfo asset name is emtpy where asset id = " + assetDetailInfo.getAssetId());
		if ( StringUtils.isEmpty(assetDetailInfo.getSelectedVersion()))
			throw new Exception("AssetDetailInfo selected version is emtpy where asset id = " + assetDetailInfo.getAssetId());
		String  playBookName = assetDetailInfo.getAssetName() + "-" + assetDetailInfo.getSelectedVersion() + "/" + externalCtlIntf.getMethodFileName() ;
		String playBookPipeline = "ansiblePlaybook installation: '{{ansible_alias}}', inventory: '" + ansibleHostName + "', playbook: '"+playBookName+ "'\n";
		sb.append(playBookPipeline);
	}

	/**
	 * 附加pod名字到ansible host变量中
	 * @param astUnitRsp 资产单元
	 */
	private void appendDeployName(AstUnitRsp astUnitRsp) throws Exception {
		String deployName = AssetUtil.getDeployName(assetDetailInfo, astUnitRsp.getUnitId(), astUnitRsp.getUnitName(), appInstanceDetailInfo);
		sb.append("POD_NAME=");
		sb.append( deployName ) ;
		sb.append("\n");

		sbPrefixUnitPodNameConfigProp.append(astUnitRsp.getUnitName());
		sbPrefixUnitPodNameConfigProp.append("__");
		sbPrefixUnitPodNameConfigProp.append(unitIndex);
		sbPrefixUnitPodNameConfigProp.append("__");
		sbPrefixUnitPodNameConfigProp.append("POD_NAME=");
		sbPrefixUnitPodNameConfigProp.append( deployName) ;
		sbPrefixUnitPodNameConfigProp.append("\n");
	}

	/**
	 * 根据执行单元查找控制接口
	 * @param ctlInterfaces 控制接口列表
	 * @param stagePrefix  控制单元名称,比如 pf_deploy_mysql
	 * @return 查找到的控制接口， 如果找不到就返回null
	 */
	AstExternalCtlIntfRsp getAstExternalCtlIntfRspByStage(List<AstExternalCtlIntfRsp> ctlInterfaces, String stagePrefix) {
		for ( AstExternalCtlIntfRsp externalCtlIntf : ctlInterfaces ) 
			if ( externalCtlIntf.getMethodName().equals(stagePrefix))
				return externalCtlIntf;

		return null;
	}

	/**
	 * 根据依赖关系排序资产的执行顺序
	 * 运用图论的拓扑排序
	 * @param astUnitRspsUnsortParam 资产单元列表
	 * @return 排好序的资产单元列表
	 */
	public static List<AstUnitRsp> sortAstUnitRsps(List<AstUnitRsp> astUnitRspsUnsortParam) throws  Exception{
		if ( astUnitRspsUnsortParam.size()==1)
			return astUnitRspsUnsortParam;

		List<AstUnitRsp> listAstUnitRsp= new LinkedList<>();
		listAstUnitRsp.addAll(astUnitRspsUnsortParam);

		TreeMap<String, AstUnitRsp> mapAstUnitRsp = new TreeMap<>();
		for ( AstUnitRsp astUnitRsp : listAstUnitRsp )
			mapAstUnitRsp.put(astUnitRsp.getUnitName(), astUnitRsp);

		List<AstUnitRsp> result = new LinkedList<>();
		int totalAstUnitRsp = listAstUnitRsp.size();
		for (int i=0; (i<totalAstUnitRsp) && (!listAstUnitRsp.isEmpty()); ++i) {
			Iterator<AstUnitRsp> iter = listAstUnitRsp.iterator();
			while ( iter.hasNext()) {
				AstUnitRsp astUnitRsp = iter.next();
				boolean haveDepend = false ;
//				for (AstUnitDependencyRsp depend : astUnitRsp.getUnitDepends()) {
//					if ( mapAstUnitRsp.get(depend.getTargetUnitName()) != null ) {
//						haveDepend = true;
//						break;
//					}
//				}
				if ( !haveDepend) {
					result.add(astUnitRsp);
					iter.remove();;
					mapAstUnitRsp.remove(astUnitRsp.getUnitName());
				}
			}
		}

		if ( !listAstUnitRsp.isEmpty())
			throw new Exception("AstUnitResp loop depend , AstUnitRsps -> "  +  JSONObject.toJSONString(astUnitRspsUnsortParam));
		return result;
	}
}
