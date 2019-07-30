package com.newland.paas.ee.installer.config;

/**
 * Jenkins配置信息
 */
public class JenkinsConfig {

    private String jenkinsAnsibleAlias;
    private String jenkinsUrl;
    private String jenkinsUserName;
    private String jenkinsPassword;

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrlParam) {
        jenkinsUrl = jenkinsUrlParam;
    }

    public String getJekinsUserName() {
        return jenkinsUserName;
    }

    public void setJekinsUserName(String jekinsUserNameParam) {
        jenkinsUserName = jekinsUserNameParam;
    }

    public String getJeninsPassword() {
        return jenkinsPassword;
    }

    public void setJenkinsPassword(String jenkinsPasswordParam) {
        jenkinsPassword = jenkinsPasswordParam;
    }

    public String getJenkinsAnsibleAlias() {
        return jenkinsAnsibleAlias;
    }

    public void setJenkinsAnsibleAlias(String jenkinsAnsibleAliasParam) {
        jenkinsAnsibleAlias = jenkinsAnsibleAliasParam;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[jenkinsAnsibleAlias=");
        builder.append(jenkinsAnsibleAlias);
        builder.append(", jenkinsUrl=");
        builder.append(jenkinsUrl);
        builder.append(", jenkinsUserName=");
        builder.append(jenkinsUserName);
        builder.append(", jenkinsPassword=");
        builder.append(jenkinsPassword);
        builder.append("]");
        return builder.toString();
    }

}
