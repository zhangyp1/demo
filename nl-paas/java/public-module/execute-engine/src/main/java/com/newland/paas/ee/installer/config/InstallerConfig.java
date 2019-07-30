package com.newland.paas.ee.installer.config;

import com.offbytwo.jenkins.JenkinsServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class InstallerConfig {
    private JenkinsConfigSelector jenkinsConfigSelector;
    private String clusterImagePath;     //集群镜像仓库地址
    private String noCipherDepotPath; //免密文件存放路径
    private String clusterImageUserName;
    private String clusterImagePassword;
    private String pubFtpAddress;
    private String pubFtpUserName;
    private String pubFtpPassword;
    private String jedisUrl;
    private int jedisLockTimeout;

    public String getJedisUrl() {
        return jedisUrl;
    }

    public void setJedisUrl(String jedisUrl) {
        this.jedisUrl = jedisUrl;
    }

    public int getJedisLockTimeout() {
        return jedisLockTimeout;
    }

    public void setJedisLockTimeout(int jedisLockTimeout) {
        this.jedisLockTimeout = jedisLockTimeout;
    }

    public String getClusterImagePath() {
        return clusterImagePath;
    }

    public void setClusterImagePath(String clusterImagePath) {
        this.clusterImagePath = clusterImagePath;
    }

    public String getClusterImagePassword() {
        return clusterImagePassword;
    }

    public void setClusterImagePassword(String clusterImagePassword) {
        this.clusterImagePassword = clusterImagePassword;
    }

    public String getClusterImageUserName() {
        return clusterImageUserName;
    }

    public void setClusterImageUserName(String clusterImageUserName) {
        this.clusterImageUserName = clusterImageUserName;
    }

    static public String getInstallTemplate() throws Exception {
        return readResourceText("install_template");
    }

    static public String getInstallTemplateParam() throws Exception {
        return readResourceText("install_template_parameter");
    }

    public static String getPreprocessingDeployStep() throws Exception {
        return readResourceText("preprocessing_deploy_step");
    }

    public static String getInstallEtcdDeployStep() throws Exception {
        return readResourceText("install_etcd_deploy_step");
    }

    public static String getInstallK8sDeployStep() throws Exception {
        return readResourceText("install_k8s_deploy_step");
    }

    public static String getInstallEtcdK8sDeployStep() throws Exception {
        return readResourceText("install_etcd_k8s_deploy_step");
    }

    public static String getInstallEtcdCmssK8sDeployStep() throws Exception {
        return readResourceText("install_cmssk8s_deploy_stage");
    }


    public static String getInstallRawCluDeployStep() throws Exception {
        return readResourceText("install_rawclu_deploy_stage");
    }
    
	public static String getUnInstallRawCluDeployStep() throws Exception {
		return readResourceText("uninstall_rawclu_deploy_stage");
	}
	
    public static String getInstallK8sIngressDeployStep() throws Exception {
        return readResourceText("install_k8s_ingress_deploy_step");
    }

    public static String getScaleOutK8sDeployStep() throws Exception {
        return readResourceText("scale_out_k8s_deploy_step");
    }

    public static String getScaleOutCmssK8sDeployStep() throws Exception {
        return readResourceText("scale_out_cmssk8s_deploy_stage");
    }
    
    public static String getUninstallK8sDeployStep() throws Exception {
        return readResourceText("uninstall_k8s_deploy_step");
    }

    public static String getUninstallEtcdDeployStep() throws Exception {
        return readResourceText("uninstall_etcd_deploy_step");
    }

    public static String getUninstallK8sEtcdDeployStep() throws Exception {
        return readResourceText("uninstall_k8s_etcd_deploy_step");
    }

    public static String getUninstallCmssK8sEtcdDeployStep() throws Exception {
        return readResourceText("uninstall_cmssk8s_deploy_stage");
    }
    
    public static String getScaleInK8sDeployStep() throws Exception {
        return readResourceText("scale_in_k8s_deploy_step");
    }

    public static String getScaleInCmssK8sDeployStep() throws Exception {
        return readResourceText("scale_in_cmssk8s_deploy_stage");
    }
    
    public static String getEtcdK8sStartStopStep() throws Exception {
        return readResourceText("etcd_k8s_start_stop_deploy_step");
    }

    public static String getApplicationIngressAnsibleInstall() throws Exception {
        return readResourceText("application_ingress_ansible_install");
    }

    public static String getApplicationIngressAnsibleUninstall() throws Exception {
        return readResourceText("application_ingress_ansible_uninstall");
    }

    public static String getApplicationIngressAnsibleCommon() throws Exception {
        return readResourceText("application_ingress_ansible_common");
    }

    public static String getApplicationIngressAnsibleInstallPrintLbValue() throws Exception {
        return readResourceText("application_ingress_ansible_install_print_lb_value");
    }

    public static String getApplicationIngressAnsibleEndPointExist() throws Exception {
        return readResourceText("application_ingress_ansible_app_endpoint_exist");
    }

    public static String getSecretFreeDeployStep() throws Exception {
        return readResourceText("secret_free_deploy_step.txt");
    }

    public static String getInstallK8sZoneDeployStage() throws Exception {
        return readResourceText("install_k8s_zone_deploy_stage");
    }

    public static String getInstallK8sZoneTestDeployStage() throws Exception {
        return readResourceText("install_k8s_zone_test_deploy_stage");
    }

    public static String getResourcePrepareDeployStep() throws Exception {
        return readResourceText("resource_prepare_deploy_step");
    }

    public static String getInstallServiceYaml() throws Exception {
        return readResourceText("install_service_yaml");
    }

    public static String getInstallServiceAnsible() throws Exception {
        return readResourceText("install_service_ansible");
    }

    public static String getUninstallServiceAnsible() throws Exception {
        return readResourceText("uninstall_service_ansible");
    }

    public static String readResourceText(String fileName) throws Exception {
        StringBuilder sb = new StringBuilder();

        ClassLoader classloader = Thread.currentThread().getClass().getClassLoader();
        if (classloader == null) {
            classloader = InstallerConfig.class.getClassLoader();
        }
        try (InputStream is = classloader.getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            char[] buffer = new char[1024];
            int len = 0;
            while ((len = br.read(buffer)) > 0) {
                sb.append(buffer, 0, len);
            }
        }

        return sb.toString();
    }

    public void setJenkins(List<JenkinsConfig> jenkinsConfigList) {
        jenkinsConfigSelector = new JenkinsConfigSelector();
        jenkinsConfigSelector.init(jenkinsConfigList);
    }

    public JenkinsServer selectJenkinsServer() {
        return jenkinsConfigSelector.selectJenkinsServer();
    }

    public JenkinsConfig getSelectJenkinsConfig() {
        return jenkinsConfigSelector.getSelectJenkinsConfig();
    }

    public String getNoCipherDepotPath() {
        return noCipherDepotPath;
    }

    public void setNoCipherDepotPath(String noCipherDepotPath) {
        this.noCipherDepotPath = noCipherDepotPath;
    }

    public List<JenkinsConfig> getJenkinsConfigs() {
        return jenkinsConfigSelector.getJenkinsConfigs();
    }

    public String getPubFtpAddress() {

        return pubFtpAddress;
    }

    public void setPubFtpAddress(String pubFtpAddress) {

        this.pubFtpAddress = pubFtpAddress;
    }

    public String getPubFtpUserName() {

        return pubFtpUserName;
    }

    public void setPubFtpUserName(String pubFtpUserName) {

        this.pubFtpUserName = pubFtpUserName;
    }

    public String getPubFtpPassword() {

        return pubFtpPassword;
    }

    public void setPubFtpPassword(String pubFtpPassword) {

        this.pubFtpPassword = pubFtpPassword;
    }

}
