package com.newland.paas.ee.installer;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.util.ApplicationUtil;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.K8SClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.asset.AstExternalCtlIntfRsp;
import com.newland.paas.ee.vo.asset.AstExternalDependRsp;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.List;
import java.util.Map;

//应用依赖执行步骤生成器
public class DependAppDeployStepBuilder {
	private static final Log log = LogFactory.getLogger(DependAppDeployStepBuilder.class);
	private StringBuilder sb ;
	private AssetDetailInfo assetDetailInfo;
	private Map<String, ClusterInstanceDetailInfo> targetClusterList;
	private AppInstanceDetailInfo appInstanceDetailInfo;
	private String dependName;
	private int dependIndex = 1;

	/**
	 * 构造函数
	 * @param assetDetailInfo			依赖的资产详情信息
	 * @param targetClusterList	单元集群信息 Map<单元名称, 集群信息>
	 * @param appInstanceDetailInfo		执行的对象，如应用
	 */
	DependAppDeployStepBuilder (AssetDetailInfo assetDetailInfo, Map<String, ClusterInstanceDetailInfo> targetClusterList,
			AppInstanceDetailInfo appInstanceDetailInfo ) {
		this.sb = new StringBuilder();
		this.assetDetailInfo = assetDetailInfo;
		this.targetClusterList = targetClusterList;
		this.appInstanceDetailInfo = appInstanceDetailInfo;
	}

	/**
	 * @return 资产外部依赖的执行脚本
	 */
	public String constructDeployStep() throws Exception {
		if ( assetDetailInfo.getExtDepends() != null ) {
			for ( AstExternalDependRsp astExternalDependRsp : assetDetailInfo.getExtDepends() ) {
				//每个资产的外部依赖生成对应的脚本
				constructOneAstExternalDepend(astExternalDependRsp);
				++dependIndex;
			}
		}
		return sb.toString();
	}

	/**
	 * 每个资产的外部依赖生成对应的脚本
	 * @param astExternalDependRsp 资产的外部依赖
	 */
	private void constructOneAstExternalDepend(AstExternalDependRsp astExternalDependRsp) throws Exception
	{
		dependName = astExternalDependRsp.getDependName();
		ClusterInstanceDetailInfo cidi  = targetClusterList.get( astExternalDependRsp.getUnitName()) ;

		if (cidi instanceof K8SClusterInstanceDetailInfo) {
			K8SClusterInstanceDetailInfo k8sCidi = (K8SClusterInstanceDetailInfo) cidi;
			//附加外部依赖的前缀括号到执行脚本
			appendDependInstancePrefix(dependName);
			//生成host
			String ansibleHostName = dependName + "_host_" + dependIndex;

			//附加k8s master float ip到执行脚本
			ApplicationUtil.appendK8sMasterFloat(k8sCidi, sb);

			//通过外部依赖名称得到控制接口
			AstExternalCtlIntfRsp externalCtlIntf = getAstExternalCtlIntfRspByStage(assetDetailInfo.getCtlInterfaces(), dependName);
			if (externalCtlIntf == null)
				throw new Exception("Can not find method name = " + dependName + " in AssetDetailInfo.getCtlInterfaces()");

			//if ( astExternalDependRsp.getDependType().equals("1") ) { //应用
				//DependInstanceInfo dependInstanceInfo = getDependInstanceInfoByName(astExternalDependRsp.getDependName());
				//dependAppInstanceDetailInfo = dependInstanceInfo.getAppInstanceDetailInfo();
			    //附加所有变量到执行脚本
				appendAllVars(k8sCidi, externalCtlIntf);
			//}
			//else {
				//throw new Exception("depend only support application");
			//}
			sb.append("\\'> " + ansibleHostName + "'''\n");
			//如果需要执行ansible play book, 则附加执行该play book的脚本到执行脚本
			appendPlayBookIfNeed(ansibleHostName, dependName, externalCtlIntf ) ;
			//附加依赖的后缀括号到执行脚本
			appendDependInstancePostfix();
			//replaceFloatIp( k8sCidi );
		} 
		else {
			//外部依赖找不到集群就不执行
			log.warn(LogProperty.LOGTYPE_DETAIL, "can not find K8sClusterInstanceDetailInfo where Unit name = " + astExternalDependRsp.getUnitName());
		}
	}

	/**
	 * 附加外部依赖的前缀括号到执行脚本
	 * @param dependName 外部依赖名称
	 */
	public void appendDependInstancePrefix( String dependName ) {
		sb.append("        stage('");
		sb.append(dependName);
		//sb.append(".");
		//sb.append(astUnitRsp.getUnitName());
		sb.append("') {\n            steps {\n");
	}

	/**
	 * 附加依赖的后缀括号到执行脚本
	 */
	public void appendDependInstancePostfix(  ) {
		sb.append("\n            }");
		sb.append("\n        }\n");
	}

	/**
	 * 附加所有变量到执行脚本
	 * @param k8sCidi 资产单元所属的集群信息
	 * @param externalCtlIntf 外部控制接口
	 */
	public void appendAllVars(K8SClusterInstanceDetailInfo k8sCidi, AstExternalCtlIntfRsp externalCtlIntf ) throws Exception {
		sb.append("[all:vars]\n");
		ApplicationUtil.appendAppConfig(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb, log);
		ApplicationUtil.appendAppProperty(externalCtlIntf, appInstanceDetailInfo, assetDetailInfo, sb, log);
		ApplicationUtil.appendAppId(appInstanceDetailInfo, sb);
		ApplicationUtil.appendK8sHome(k8sCidi, sb);
		ApplicationUtil.appendNamespace(k8sCidi, sb);
	}

	/**
	 * 如果需要执行ansible play book, 则附加执行该play book的脚本到执行脚本
	 * @param ansibleHostName 执行ansible的host名字
	 * @param dependName 外部依赖名字
	 * @param externalCtlIntf 外部控制接口	 *
	 */
	void appendPlayBookIfNeed(String ansibleHostName, String dependName, AstExternalCtlIntfRsp externalCtlIntf) throws Exception {
		if ( StringUtils.isEmpty(externalCtlIntf.getMethodFileName()) )
			return;
			//throw new Exception( "external control interface " + externalCtlIntf.getMethodName() + " don't have method file name");
		if ( StringUtils.isEmpty(assetDetailInfo.getAssetName()))
			throw new Exception("AssetDetailInfo asset name is emtpy where asset id = " + assetDetailInfo.getAssetId());
		if ( StringUtils.isEmpty(assetDetailInfo.getSelectedVersion()))
			throw new Exception("AssetDetailInfo selected version is emtpy where asset id = " + assetDetailInfo.getAssetId());
		String  playBookName = assetDetailInfo.getAssetName() + "-" + assetDetailInfo.getSelectedVersion() + "/" + externalCtlIntf.getMethodFileName() ;
		String playBookPipeline = "ansiblePlaybook installation: '{{ansible_alias}}', inventory: '" + ansibleHostName + "', playbook: '"+playBookName+ "'\n";
		sb.append(playBookPipeline);
	}

	/**
	 * 通过接口名称得到控制接口
	 * @param ctlInterfaces 外部依赖控制接口列表
	 * @return stagePrefix 外部依赖控制接口名称
	 */
	AstExternalCtlIntfRsp getAstExternalCtlIntfRspByStage(List<AstExternalCtlIntfRsp> ctlInterfaces, String stagePrefix) {
		for ( AstExternalCtlIntfRsp externalCtlIntf : ctlInterfaces )
			if ( externalCtlIntf.getMethodName().equals(stagePrefix))
				return externalCtlIntf;
		return null;
	}
}
