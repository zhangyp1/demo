package com.newland.paas.ee.installer;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.installer.config.JenkinsConfig;
import com.newland.paas.ee.util.JenkinUtil;
import com.newland.paas.ee.vo.OperateResult;
import com.offbytwo.jenkins.JenkinsServer;

import java.util.Collections;
import java.util.Map;

public abstract class AbstractInstaller implements AutoCloseable{
	//private static final Log log = LogFactory.getLogger(AbstractInstaller.class);

	/**
	 * 构造函数
	 * @param installerConfig		安装配置
	 */
	protected AbstractInstaller(InstallerConfig installerConfig) {
		this.installerConfig = installerConfig;
	}

	/**
	 * 构造函数
	 * @param installerConfig		安装配置
	 * @param jobPrefix      job前缀，比如部署mysql就是 pf_deploy_mysql
	 */
	protected AbstractInstaller(InstallerConfig installerConfig , String jobPrefix ) {
		this.installerConfig = installerConfig;
		this.jobPrefix = jobPrefix;
	}

	/**
	 * 创建并启动jenkins job
	 * @param jobPrefix      job前缀，比如部署mysql就是 pf_deploy_mysql
	 */
	public void createAndBuildJob( String jobPrefix ) throws Exception{
		doCreateAndBuildJob(  getDeployStep(), jobPrefix);
	}

	/**
	 * 创建并启动jenkins job
	 * @param deployStep  安装步骤
	 * @param jobPrefix      job前缀，比如部署mysql就是 pf_deploy_mysql
	 */
	private void doCreateAndBuildJob( String deployStep,  String jobPrefix ) throws Exception{
		//首先选择jenkins server， 然后在选定的jenkins server上执行安装脚本
		jenkinsServer = installerConfig.selectJenkinsServer();
		JenkinsConfig jenkinsConfig = installerConfig.getSelectJenkinsConfig();
		if ( jenkinsConfig == null )
			throw new Exception("select jenkins config is null");

		if ( StringUtils.isEmpty(jenkinsConfig.getJenkinsAnsibleAlias()))
			throw new Exception("select jenkins config ansible alias is null, where jenkins url=" + jenkinsConfig.getJenkinsUrl());
		deployStep = deployStep.replace( "{{ansible_alias}}", jenkinsConfig.getJenkinsAnsibleAlias());

		jobName = JenkinUtil.createAndBuildJob(jenkinsServer, jobPrefix , deployStep );
	}

	public void createAndBuildJobParam() throws Exception{
		doCreateAndBuildJobParam();
	}

	private void doCreateAndBuildJobParam( ) throws Exception{
		//首先选择jenkins server， 然后在选定的jenkins server上执行安装脚本
		jenkinsServer = installerConfig.selectJenkinsServer();
		JenkinsConfig jenkinsConfig = installerConfig.getSelectJenkinsConfig();
		if ( jenkinsConfig == null )
			throw new Exception("select jenkins config is null");
		if ( StringUtils.isEmpty(jenkinsConfig.getJenkinsAnsibleAlias()))
			throw new Exception("select jenkins config ansible alias is null, where jenkins url=" + jenkinsConfig.getJenkinsUrl());
		//脚本需要的参数通过Map传入
		Map<String,String> deployParameter = getDeployParameter();
		deployParameter.put("ansible_alias", jenkinsConfig.getJenkinsAnsibleAlias());
		jobName = JenkinUtil.createAndBuildJobParam(jenkinsServer, jobPrefix , getDeployStep(), deployParameter );
	}

	/**
	 * 删除jenkins job
	 */
	public void deleteJob() throws Exception {
		jenkinsServer.deleteJob(jobName);
	}

	/**
	 * 释放资源, 关闭和jenkins的连接
	 */
	public void close() {
		if ( jenkinsServer != null ) {
			jenkinsServer.close();
		}
	}

	/**
	 * 获取当前安装job的名字
	 * @return  当前安装job的名字
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * @return 返回选定的Jenkins server的配置和所创建的job的名字
	 */
	public OperateResult createOperatorResult() {
		OperateResult operateResult = new OperateResult();
		JenkinsConfig jenkinsConfig = installerConfig.getSelectJenkinsConfig();
		operateResult.setProgressUrl(jenkinsConfig.getJenkinsUrl() );
		operateResult.setJobName(getJobName());
		operateResult.setBuildNumber( 1 );
		operateResult.setUserName(jenkinsConfig.getJekinsUserName());
		operateResult.setPasswd(jenkinsConfig.getJeninsPassword());
		return operateResult;
	}

	/**
	 * 返回选定的Jenkins server
	 */
	public JenkinsServer getJenkinsServer() {
		return jenkinsServer;
	}

	/**
	 * 返回安装配置
	 */
	public InstallerConfig getInstallConfig() {
		return installerConfig;
	}

	/**
	 * 返回jenkins的pipeline安装步骤
	 */
	public abstract String getDeployStep() throws Exception ;

	/**
	 * 返回jenkins的pipeline参数
	 */
	public Map<String,String> getDeployParameter() {
		return Collections.emptyMap();
	}
	
	private JenkinsServer jenkinsServer;
	private String jobName;
	private InstallerConfig installerConfig;
	private String jobPrefix;
}