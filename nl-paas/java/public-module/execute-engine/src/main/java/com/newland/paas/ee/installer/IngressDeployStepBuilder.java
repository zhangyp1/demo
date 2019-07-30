package com.newland.paas.ee.installer;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ApplicationConstants;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.*;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.application.AppLoadBalanceConfig;
import com.newland.paas.ee.vo.asset.AstExternalSvrIntfRsp;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Ingress执行步骤构建器
 */
public class IngressDeployStepBuilder {
	private List<AppLoadBalanceConfig> appLoadBalanceConfigList;
	K8SClusterInstanceDetailInfo k8sCidi;
	private AssetDetailInfo assetDetailInfo;
	private AppInstanceDetailInfo appInstanceDetailInfo;
	private long unitId;
	private long unitIndex;
	private String unitName;
	private String ansibleHostName;
	private String stagePrefix;
	private String jedisUrl;
	int lockTimeOut;
	private final long ADD_PORT=1;
	private final long REMOVE_PORT=2;
	private static final Log log = LogFactory.getLogger(IngressDeployStepBuilder.class);

	/**
     * @param appLoadBalanceConfigList  负载均衡配置列表
	 * @param k8sCidi 资产单元所属的集群信息
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 * @param unitId 资产单元Id
	 * @param unitIndex 资产单元序号, 第一个单元index为1,第二个为2
	 * @param unitName 资产单元名称
	 * @param ansibleHostName 执行ansible的host名字
	 * @param stagePrefix 执行前缀， 比如部署就是 pf_deploy
	 * @param jedisUrl  redis的url
	 * @param lockTimeOut redis锁定的时间
	 */
	public IngressDeployStepBuilder( List<AppLoadBalanceConfig> appLoadBalanceConfigList, K8SClusterInstanceDetailInfo k8sCidi,
			AssetDetailInfo assetDetailInfo, AppInstanceDetailInfo appInstanceDetailInfo,
			long unitId, long unitIndex, String unitName, String ansibleHostName, String stagePrefix,
			String jedisUrl, int lockTimeOut) {
		this.appLoadBalanceConfigList = appLoadBalanceConfigList;
		this.k8sCidi = k8sCidi;
		this.assetDetailInfo = assetDetailInfo;
		this.appInstanceDetailInfo = appInstanceDetailInfo;
		this.unitId = unitId ;
		this.unitIndex = unitIndex;
		this.unitName = unitName;
		this.ansibleHostName = ansibleHostName;
		this.stagePrefix = stagePrefix;
		this.jedisUrl = jedisUrl;
		this.lockTimeOut = lockTimeOut;
	}

	/**
	 * 检测应用负载均衡配置是否有效
	 * @param appLoadBalanceConfig 负载均衡配置
	 */
	private void checkIngress( AppLoadBalanceConfig appLoadBalanceConfig) throws Exception {
		if (StringUtils.isEmpty(appLoadBalanceConfig.getIngressInfo().getIpAddrList()))
			throw new Exception( "load balance config lb_node is empty where id = " + appLoadBalanceConfig.getLoadBalanceId());
		if ( appLoadBalanceConfig.getServicePort() <= 0 ) {
			throw new Exception( "load balance config service port is invalid where id = " + appLoadBalanceConfig.getLoadBalanceId() +
					" ,  service port = " + appLoadBalanceConfig.getServicePort() );
		}
	}

	/**
	 * 根据负载均衡配置的接入点名称找到对应的cluster port配置，如果是${开头的， 则需要到配置中找
	 * @param appLoadBalanceConfig 负载均衡配置
	 * @return cluster port的配置值
	 */
	String getAppEndPointClusterPort(AppLoadBalanceConfig appLoadBalanceConfig)
	{
		AstExternalSvrIntfRsp astExternalSvrIntfRsp = AstUnitRspLoadBalanceUtil.getSvrInterfacesByEndpointName(assetDetailInfo,
				appLoadBalanceConfig.getEndpointName());
		return ApplicationUtil.getIfConfig(appInstanceDetailInfo, assetDetailInfo.getAssetName(),
				astExternalSvrIntfRsp.getClusterPort(), log);
	}

	/**
	 * 提取执行ingress-manager的参数
	 * ingress-manager往tcp_services中设置端口映射，这样才能通过ingress访问k8s应用
	 * @param appLoadBalanceConfig 负载均衡配置
	 * @return 执行ingress-manager的参数
	 */
	private String getApplicationIngressParameter(AppLoadBalanceConfig appLoadBalanceConfig) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(Long.valueOf(k8sCidi.getClusterId()));
		sb.append(" ");
		sb.append(LoadBalanceUtil.getLoadBalanceIdStr(appLoadBalanceConfig));
		sb.append(" ");
		if(appLoadBalanceConfig.getIngressInfo() == null) {
			sb.append("");
		}else {
		    sb.append(appLoadBalanceConfig.getIngressInfo().getIpAddrList());
		}
		sb.append(" ");

		//sb.append(Long.valueOf(appLoadBalanceConfig.getHostPort()));
		sb.append(getAppEndPointClusterPort(appLoadBalanceConfig));
		sb.append(" ");
		sb.append(Long.valueOf(appLoadBalanceConfig.getServicePort()));
		sb.append(" ");
		sb.append(AssetUtil.getDeployName(assetDetailInfo, unitId, unitName, appInstanceDetailInfo));
		sb.append(" ");
		sb.append(appLoadBalanceConfig.getConnectionType());
		sb.append(" ");
		if ( stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY))
			sb.append(ADD_PORT);
		else
			sb.append(REMOVE_PORT);
		sb.append(" ");
		sb.append(k8sCidi.getDetailVo().getZoneName());
		sb.append(" ");
		sb.append(k8sCidi.getFloatIp());
		sb.append(" ");
		sb.append(jedisUrl);
		sb.append(" ");
		sb.append(Long.valueOf(lockTimeOut));
		return sb.toString();
	}

	/**
	 * 提取LB_VALUE配置值
	 * @param appLoadBalanceConfig 负载均衡配置
	 * @return LB_VALUE配置值
	 */
	public String getLbValue(AppLoadBalanceConfig appLoadBalanceConfig) {
		StringBuilder sb = new StringBuilder();
		AstExternalSvrIntfRsp astExternalSvrIntfRsp = AstAppletUnitRspLoadBalanceUtil.getSvrInterfacesByEndpointName( assetDetailInfo,
				appLoadBalanceConfig.getEndpointName());
		sb.append("${");
		sb.append( astExternalSvrIntfRsp.getIntfKey());
		sb.append("##");
		sb.append(appLoadBalanceConfig.getLoadBalanceId());
		sb.append("##");
		sb.append(appLoadBalanceConfig.getIngressInfo().getIpAddrList());
		sb.append("##");
		sb.append(appLoadBalanceConfig.getServicePort());
		sb.append('}');
		return sb.toString();
	}

	/**
	 * 提取执行Ingress的pipeline
	 * @return 执行Ingress的pipeline
	 */
	public String getIngressPipeline( ) throws Exception {
		//下面的内容都是拼凑起来的
		StringBuilder sb = new StringBuilder();
		String ansibleBookName = "ingress_" + unitId + "_" + unitIndex;
		sb.append("                sh '''echo '");
		sb.append(InstallerConfig.getApplicationIngressAnsibleCommon());
		sb.append('\n');
		String applicationIngressAnsible;

		//部署和启动需要打印LB_VALUE
		if (stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY) || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_START)) {
			for ( AppLoadBalanceConfig appLoadBalanceConfig : appLoadBalanceConfigList) {
				checkIngress(appLoadBalanceConfig);
				String applicationIngressPrintValue = InstallerConfig.getApplicationIngressAnsibleInstallPrintLbValue();
				applicationIngressPrintValue = applicationIngressPrintValue.replace("{{LB_VALUE}}", getLbValue(appLoadBalanceConfig));
				sb.append(applicationIngressPrintValue);
				sb.append('\n');
			}
		}

		//生成调用ingress-manager的pipeline
		if (stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY) || stagePrefix.equals(ApplicationConstants.APPLICATION_PF_UNLOAD)) {
			for (AppLoadBalanceConfig appLoadBalanceConfig : appLoadBalanceConfigList) {
				if (stagePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY))
					applicationIngressAnsible = InstallerConfig.getApplicationIngressAnsibleInstall();
				else
					applicationIngressAnsible = InstallerConfig.getApplicationIngressAnsibleUninstall();

				applicationIngressAnsible = applicationIngressAnsible.replace("{{APPLICATION_INGRESS_PARAMETER}}",
						getApplicationIngressParameter(appLoadBalanceConfig));
				applicationIngressAnsible = applicationIngressAnsible.replace("{{LOAD_BALANCE_ID}}",
						Long.toString(appLoadBalanceConfig.getLoadBalanceId()));
				sb.append(applicationIngressAnsible);
				sb.append('\n');
			}
		}

		sb.append( "'>");
		sb.append(ansibleBookName);
		sb.append("'''\n");

		//调用ansible playbook
		StringBuilder sbPlayBookScript = new StringBuilder();
		sbPlayBookScript.append("       ansiblePlaybook installation: '{{ansible_alias}}', inventory: '");
		sbPlayBookScript.append(ansibleHostName);
		sbPlayBookScript.append("', playbook: '");
		sbPlayBookScript.append(ansibleBookName);
		sbPlayBookScript.append("'");
		sb.append(sbPlayBookScript);
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 提取打印APP接入点是否存在的pipeline
	 * @param appLoadBalanceConfigList  负载均衡的列表
	 * @param ansibleHostName 执行ansible的host名字
	 * @param unitId 资产应用单元Id
	 * @param unitIndex 资产应用单元序号
	 * @param statePrefix 执行前缀， 比如部署就是 pf_deploy
	 * @return 打印APP接入点是否存在的pipeline
	 */
	public static String getAppEndPointExistPipeline(List<AppLoadBalanceConfig> appLoadBalanceConfigList,
			String ansibleHostName, long unitId, long unitIndex, String statePrefix ) throws Exception {
		boolean appEndPointExist ;
		//在部署和启动过程中，并且配置了负载均衡，则打印true，否则打印false
		if ( appLoadBalanceConfigList != null && (!appLoadBalanceConfigList.isEmpty())) {
			if ((statePrefix.equals(ApplicationConstants.APPLICATION_PF_DEPLOY) || statePrefix.equals(ApplicationConstants.APPLICATION_PF_START)))
				appEndPointExist = true;
			else
				appEndPointExist = false;
		}
		else
			appEndPointExist = false;


		String ansible = InstallerConfig.getApplicationIngressAnsibleEndPointExist();
		ansible = ansible.replace("{{APP_ENDPOINT_EXISTS}}", appEndPointExist?"true":"false");

		String ansibleBookName = "echo_ingress_app_endpoint_exist_" + unitId + "_" + unitIndex;
		StringBuilder sb = new StringBuilder();

		sb.append("                sh '''echo '");
		sb.append(ansible);
		sb.append('\n');
		sb.append( "'>");
		sb.append(ansibleBookName);
		sb.append("'''\n");

		//调用ansible playbook
		StringBuilder sbPlayBookScript = new StringBuilder();
		sbPlayBookScript.append("       ansiblePlaybook installation: '{{ansible_alias}}', inventory: '");
		sbPlayBookScript.append(ansibleHostName);
		sbPlayBookScript.append("', playbook: '");
		sbPlayBookScript.append(ansibleBookName);
		sbPlayBookScript.append("'");
		sb.append(sbPlayBookScript);
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 附加服务执行脚本到执行脚本，服务执行脚本就是用程序生成 执行生成k8s service的yaml文件，并生成调用该yaml文件的pipeline, 动态执行生成service的脚本
	 * @param ansibleHostName 执行ansible的host名字
	 * @param sb 属性写入到该StringBuilder
	 */
	public void appendInstallService( String ansibleHostName, StringBuilder sb ) throws Exception {
		String ansibleBookName = "install_service_" + Long.toString(unitId) + "_" + Long.toString(unitIndex);
		sb.append("                sh '''echo '");
		sb.append(InstallerConfig.getInstallServiceYaml());

		Map<Long, AstExternalSvrIntfRsp> mapAstExternalSvrIntfRsp = new HashMap<>();
		for ( AppLoadBalanceConfig appLoadBalanceConfig : appLoadBalanceConfigList ) {
			AstExternalSvrIntfRsp astExternalSvrIntfRsp = AstUnitRspLoadBalanceUtil
					.getSvrInterfacesByEndpointName(
					assetDetailInfo, appLoadBalanceConfig.getEndpointName());
			mapAstExternalSvrIntfRsp.put(astExternalSvrIntfRsp.getInterfaceId(), astExternalSvrIntfRsp);
		}

		for ( Map.Entry<Long, AstExternalSvrIntfRsp> entry : mapAstExternalSvrIntfRsp.entrySet() ) {
			AstExternalSvrIntfRsp astExternalSvrIntfRsp = entry.getValue();
			sb.append(" - port: ");
			sb.append(ApplicationUtil.getIfConfig(appInstanceDetailInfo, assetDetailInfo.getAssetName(),
					astExternalSvrIntfRsp.getClusterPort(), log));

			sb.append('\n');
			sb.append("   targetPort: ");
			sb.append(ApplicationUtil.getIfConfig(appInstanceDetailInfo, assetDetailInfo.getAssetName(),
					astExternalSvrIntfRsp.getContainerPort(), log));
			sb.append('\n');
		}
		sb.append('\n');
		sb.append( "'>");
		sb.append(ansibleBookName);
		sb.append("'''\n");

		String installServiceAnsible = InstallerConfig.getInstallServiceAnsible();
		installServiceAnsible = installServiceAnsible.replace("{{service_yaml}}", ansibleBookName);

		sb.append("                sh '''echo '");
		sb.append(installServiceAnsible);
		sb.append('\n');
		sb.append( "'>");
		String installSeriviceBookName = "install_service_ansible" + Long.toString(unitId) + "_" + Long.toString(unitIndex);;

		sb.append(installSeriviceBookName);
		sb.append("'''\n");



		String playBookPipeline = "ansiblePlaybook installation: '{{ansible_alias}}', inventory: '" + ansibleHostName + "', playbook: '"+installSeriviceBookName+ "'\n";
		sb.append(playBookPipeline);
	}
}

