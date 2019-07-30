package com.newland.paas.ee.installer;

import com.google.gson.Gson;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.util.K8sClusterUtil;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.Map;
import java.util.TreeMap;

/**
* 执行k8s集群预置功能， 比如关闭swap, 关闭Selinux, 关闭防火墙, 安装docker, 安装ntp客户端
 */
public class ResourcePrepare extends AbstractInstaller{
	private static final Log log = LogFactory.getLogger(ResourcePrepare.class);
	private TreeMap<String,String> parameterMap = new TreeMap<>();

	/**
	 * 生成属性，传给jenkins
	 * @param installerConfig		安装配置
	 * @param targetIp 需要执行预置功能的目标ip
	 * @param mapIpHostName ip和host的映射
	 * @param dockerImageReposity docker镜像中心
	 * @param ntpServer ntp服务器ip地址
	 */
	public ResourcePrepare(InstallerConfig installerConfig, String targetIp, Map<String, String> mapIpHostName,
			String dockerImageReposity, String ntpServer) throws Exception {
		super(installerConfig, "resource_prepare");

		if ( StringUtils.isEmpty(targetIp))
			throw new Exception("target ip is empty");
		parameterMap.put("target_ip", targetIp);

		if ( mapIpHostName == null || mapIpHostName.isEmpty())
			throw new Exception("ip hostname map is emtpy");
		parameterMap.put("ip_host_map", getIpHostMapStr(mapIpHostName));

		if (StringUtils.isEmpty(ntpServer))
			throw new Exception("ntp server is emtpy");
		parameterMap.put("ntp_server", ntpServer);

		K8sClusterUtil.checkAndAddClusterImagePath(dockerImageReposity, parameterMap);
		K8sClusterUtil.checkAndAddPubHarborAddressToMap(installerConfig.getPubFtpAddress(), parameterMap);
		K8sClusterUtil.checkAndAddAstUserName(installerConfig.getPubFtpUserName(), parameterMap);
		K8sClusterUtil.checkAndAddAstPassword(installerConfig.getPubFtpPassword(), parameterMap);
		log.info(LogProperty.LOGTYPE_DETAIL, "resource prepare parameter mapIpHostName (gson)->" + new Gson().toJson(mapIpHostName));
		log.info(LogProperty.LOGTYPE_DETAIL, "resource prepare parameter map (gson)->" + new Gson().toJson(parameterMap));
	}

	/**
	 * @return 资源预处理的执行步骤
	 */
	@Override public String getDeployStep() throws Exception {
		return InstallerConfig.getResourcePrepareDeployStep();
	}

	/**
	 * 提取ip和host映射的字符串，生成的结果如 ：192.168.56.8:host8, 192.168.56.9:host9, 用于更新目标机器上的/etc/hosts
	 * @param mapIpHostName ip和host的映射
	 * @return ip和host映射的字符串，生成的结果如 ：192.168.56.8:host8, 192.168.56.9:host9, 用于更新目标机器上的/etc/hosts
	 */
	private String getIpHostMapStr( Map<String, String> mapIpHostName) {
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		for ( Map.Entry<String, String> entry : mapIpHostName.entrySet() ) {
			if ( isFirst )
				isFirst = false;
			else
				sb.append(',');
			sb.append(entry.getKey());
			sb.append(": ");
			sb.append(entry.getValue());
		}
		return sb.toString();
	}

	@Override public TreeMap<String,String> getDeployParameter() {
		return parameterMap;
	}

	public OperateResult install() throws Exception {
		createAndBuildJobParam( );
		return createOperatorResult();
	}
}

