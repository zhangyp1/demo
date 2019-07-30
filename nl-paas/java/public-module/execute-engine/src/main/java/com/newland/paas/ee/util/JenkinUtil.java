package com.newland.paas.ee.util;

import com.google.gson.GsonBuilder;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class JenkinUtil {
	private static final Log log = LogFactory.getLogger(JenkinUtil.class);
	private static AtomicLong jobIndex = new AtomicLong() ;

	/**
	 * 创建jenkins job并启动
	 * @param jenkinsServer jenkins服务器
	 * @param jobPrefix 任务前缀
	 * @param deployStep 待执行的jenkins任务的pipeline脚本
	 * @return 创建的jenkins job名字
	 */
	public static String createAndBuildJob(JenkinsServer jenkinsServer, String jobPrefix, 
			String deployStep ) throws Exception {
		String jobName = null ;
		
		try {
			jobName = createJob(jenkinsServer, jobPrefix, deployStep);
			buildJob(jenkinsServer, jobName,jobPrefix);
		}
		catch(Exception e) {
			if ( jobName != null ) {
				try {
					jenkinsServer.deleteJob(jobName);
				}
				catch(Exception e1) {}
			}
			
			throw e;
		}
		
		return jobName;
	}

	/**
	 * 创建jenkins job
	 * @param jenkinsServer jenkins服务器
	 * @param jobPrefix 任务前缀
	 * @param deployStep 待执行的jenkins任务的pipeline脚本
	 * @return 创建的jenkins job名字
	 */
	public static String createJob(JenkinsServer jenkinsServer, String jobPrefix, String deployStep) throws Exception {
		String installTemplate = InstallerConfig.getInstallTemplate();
		log.debug(LogProperty.LOGTYPE_DETAIL, "Jenkins create job , " + "installTemplate : " +  installTemplate );		
		String configXml = installTemplate.replace("{{deploy_step}}", formatXml(deployStep) );
		String jobName = jobPrefix + "_tmp_" + System.currentTimeMillis() + "_" + jobIndex.incrementAndGet();
		log.debug(LogProperty.LOGTYPE_DETAIL, "Jenkins create job " + jobName + " , config xml = " + configXml );       
		jenkinsServer.createJob(jobName, configXml);		
		return jobName;
	}

	/**
	 * 创建jenkins job并启动
	 * @param jenkinsServer jenkins服务器
	 * @param jobPrefix 任务前缀
	 * @param deployStep 待执行的jenkins任务的pipeline脚本
	 * @param deployParameter jenkins脚本的配置属性
	 * @return 创建的jenkins job名字
	 */
	public static String createAndBuildJobParam(JenkinsServer jenkinsServer, String jobPrefix, 
			String deployStep, Map<String, String>  deployParameter) throws Exception {
		String jobName = null ;
		
		try {
			jobName = createJobParam(jenkinsServer, jobPrefix, deployStep, deployParameter);
			buildJobParam(jenkinsServer, jobName, deployParameter);
		}
		catch(Exception e) {
			if ( jobName != null ) {
				try {
					jenkinsServer.deleteJob(jobName);
				}
				catch(Exception e1) {}
			}
			
			throw e;
		}
		
		return jobName;
	}

	/**
	 * 创建jenkins job
	 * @param jenkinsServer jenkins服务器
	 * @param jobPrefix 任务前缀
	 * @param deployStep 待执行的jenkins任务的pipeline脚本
	 * @param deployParameter jenkins脚本的配置属性
	 * @return 创建的jenkins job名字
	 */
	public static String createJobParam(JenkinsServer jenkinsServer, String jobPrefix, 
			String deployStep, Map<String, String>  deployParameter) throws Exception {
		String jobName = jobPrefix + "_tmp_" + System.currentTimeMillis() + "_" + jobIndex.incrementAndGet();
		String installTemplate = InstallerConfig.getInstallTemplateParam();
		log.debug(LogProperty.LOGTYPE_DETAIL, "Jenkins create job " + jobName + " , installTemplate : " +  installTemplate );
		log.debug(LogProperty.LOGTYPE_DETAIL, "Jenkins create job " + jobName + " , deployStep : " + deployStep);
		String configXml = installTemplate.replace("{{deploy_step}}", formatXml(deployStep));		
		configXml = configXml.replace("{{jenkin_parameter}}", getJenkinsParameter(deployParameter));
		configXml = configXml.replace("{{pipeline_parameter}}", getPipelineParameter(deployParameter));
		log.debug(LogProperty.LOGTYPE_DETAIL, "jenkins create job " + jobName + " , config xml : " + 
		configXml + ", parameter = " + new GsonBuilder().serializeNulls().create().toJson(deployParameter) );
		jenkinsServer.createJob(jobName, configXml);		
		return jobName;
	}

	/**
	 * 启动jenkins job
	 * @param jenkinsServer jenkins服务器
	 * @param jobName 启动的jenkins job名字
	 * @throws InterruptedException 
	 */
	public static void buildJob(JenkinsServer jenkinsServer, String jobName,String jobPrefix) throws IOException, InterruptedException {
		JobWithDetails job = jenkinsServer.getJob(jobName);
		job.build();
		if("ssh-copy-id".equals(jobPrefix)) {
			/*while(job.getAllBuilds() == null) {
				log.debug(LogProperty.LOGTYPE_DETAIL, "JenkinUtil===FirstBuild is not run,wait 3000ms");		
				Thread.sleep(3000);
			}*/
		}
	}

	/**
	 * 启动jenkins job
	 * @param jenkinsServer jenkins服务器
	 * @param jobName 启动的jenkins job名字
	 * @param deployParameter jenkins脚本的配置属性
	 */
	public static void buildJobParam(JenkinsServer jenkinsServer, String jobName, 
			Map<String, String>  deployParameter) throws IOException {
		Job job = jenkinsServer.getJob(jobName);
		job.build(deployParameter);
	}

	/**
	 * 根据map属性获取Jenkins参数字符串
	 * @param deployParameter 脚本的配置属性
	 * @return jenkins格式的参数字符串
	 */
	public static String getJenkinsParameter( Map<String, String > deployParameter) {
		StringBuilder sb = new StringBuilder();
		String jenkinParameterTemplate = "<hudson.model.StringParameterDefinition>\n" + 
				"<name>parameter_name</name>\n" + 
				"<description></description>\n" +
				"<defaultValue></defaultValue>\n" + 
				"<trim>false</trim>\n" +
				"</hudson.model.StringParameterDefinition>\n";		
		for (String key : deployParameter.keySet()) {
			sb.append( jenkinParameterTemplate.replace("parameter_name", key));
		}
		return sb.toString();		
	}

	/**
	 * 根据map属性获取pipeline参数字符串
	 * @param deployParameter 脚本的配置属性
	 * @return pipeline格式的参数字符串
	 */
	public static String getPipelineParameter( Map<String, String > deployParameter) {
		StringBuilder sb = new StringBuilder();
		for (String key : deployParameter.keySet()) {
			sb.append( "string(name:'");
			sb.append(key);
			sb.append("',defaultValue: '', description: '')\n");
		}
		return sb.toString();		
	}

	/**
	 *
	 * @param configXml 格式化xml成适合html传输格式
	 * @return 适合html传输格式的xml字符串
	 */
	public static String formatXml( String configXml ) {
		configXml = configXml.replace("'", "&apos;");
		configXml = configXml.replace(">", "&gt;");
		return configXml;		
	}
	/*
	public static void buildJob(JenkinsServer jenkinsServer, String jobName, Map<String, String> deployParameter ) throws IOException {
		Job job = jenkinsServer.getJob(jobName);
		job.build(deployParameter);
	}*/
	
	/*
	public static void runTempJob(JenkinsServer jenkinsServer, String jobPrefix, String deployParam, String deployStep , 
			BuildConsoleStreamListener listener, int poolingInterval, int poolingTimeout)  throws IOException, InterruptedException {
		String jobName = createAndBuildAndGetJobResult(jenkinsServer, jobPrefix, deployParam, deployStep , 
				listener, poolingInterval, poolingTimeout);
		jenkinsServer.deleteJob(jobName);
	}
	*/
	/*
	public static void getJobResult(JenkinsServer jenkinsServer, String jobName, 
			BuildConsoleStreamListener listener, int poolingInterval, int poolingTimeout)  throws IOException, InterruptedException {
		JobWithDetails jobWithDetails = jenkinsServer.getJob(jobName);
		Build build = jobWithDetails.getLastBuild();
		BuildWithDetails buildWithDetails= build.details();
		buildWithDetails.streamConsoleOutput(	listener, poolingInterval, poolingTimeout);		
	}*/
	
	/*
	public static String createAndBuildJob(JenkinsServer jenkinsServer, String jobPrefix, 
			List<JenkinsParameterDesc> deployParamDesc, String deployStep, Map<String, String> deployParam) throws IOException {
		String jobName = createJob(jenkinsServer, jobPrefix, deployParamDesc, deployStep);
		buildJob(jenkinsServer, jobName, deployParam);		
		return jobName;
	}	*/
	
	/*
	public static String createJob(JenkinsServer jenkinsServer, String jobPrefix, List<JenkinsParameterDesc> deployParamDescs, 
			String deployStep) throws IOException {
		String installTemplate = InstallerConfig.getInstallTemplate();
		

		String configXml = installTemplate.replace("{{new_land_deploy_step}}", deployStep );
		configXml = configXml.replaceAll("{{new_land_deploy_parameter}}", createDeployParameterFromDesc(deployParamDescs));
		
		String jobName = jobPrefix + "_tmp_" + System.currentTimeMillis() + "_" + jobIndex.incrementAndGet();
		jenkinsServer.createJob(jobName, configXml);		
		return jobName;
	}*/
	
	/*
	public static String createAndBuildAndGetJobResult(JenkinsServer jenkinsServer, String jobPrefix, String deployParam, String deployStep , 
			BuildConsoleStreamListener listener, int poolingInterval, int poolingTimeout) throws IOException, InterruptedException {
		String jobName = createAndBuildJob(jenkinsServer, jobPrefix, deployStep);
		getJobResult(jenkinsServer, jobName, listener, poolingInterval, poolingTimeout) ;
		return jobName;
	}
	*/
	
	/*
	public static void runTempJob(JenkinsServer jenkinsServer, String jobPrefix, String deployParam, String deployStep , 
			BuildConsoleStreamListener listener, int poolingInterval, int poolingTimeout)  throws IOException, InterruptedException {
		String jobName = createAndBuildAndGetJobResult(jenkinsServer, jobPrefix, deployParam, deployStep , 
				listener, poolingInterval, poolingTimeout);
		jenkinsServer.deleteJob(jobName);
	}
	*/
	
	/*
	public static String createDeployParameterFromDesc( List<JenkinsParameterDesc> descs) {
		//assert ( (sb != null) && (!sb.equals("")));
		StringBuilder sb = new StringBuilder();
		sb.append("<hudson.model.ParametersDefinitionProperty>\n<parameterDefinitions>");
		
		for ( JenkinsParameterDesc desc : descs ) {
			if ( desc.type.equals("string")) {
				sb.append("<hudson.model.StringParameterDefinition>\n");
			    sb.append("<name>");
			    sb.append(desc.name);
			    sb.append("</name>\n");
			    sb.append("<description></description>\n");
			    sb.append("<defaultValue></defaultValue>\n");
			    sb.append("<trim>false</trim>\n");
			    sb.append("</hudson.model.StringParameterDefinition>\n");
			}
			else {					
			}
		}
	
		sb.append("</parameterDefinitions>\n</hudson.model.ParametersDefinitionProperty>\n");
		
		return sb.toString();
	}*/
}
