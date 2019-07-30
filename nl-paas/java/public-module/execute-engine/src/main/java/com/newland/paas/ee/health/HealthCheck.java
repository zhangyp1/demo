package com.newland.paas.ee.health;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HealthCheck {

    JenkinsServer jenkins;
    private static final String jobName2date = "resmgr-job2health2date";
    private static final String jobName2modihost = "resmgr-job2modihost";
    private static final String ansibleGroup = "bymodigroup";
    private static final String JOB_SUCCESS = "SUCCESS";
    //尝试获取最后执行结果状态
    private static final int TRY_RESULT_TIMES = 10;

    public HealthCheck(String jenkinsUri, String jenkinsUser, String jenkinsPassword) throws URISyntaxException {
//        jenkinsUri = "http://10.1.8.13:9999";
//        jenkinsUser = "admin";
//        jenkinsPassword = "taasadmin";
        initjenkins(jenkinsUri, jenkinsUser, jenkinsPassword);
    }

    public HealthCheck(JenkinsServer jenkins) {
        this.jenkins = jenkins;
    }

    /**
     * 查询targetIP服务器是否能够连接通，健康检查
     * @param targetIp
     * @throws IOException
     * @throws URISyntaxException
     */
    public boolean health2machine(String targetIp) throws IOException, URISyntaxException {
        String jobxml = buildAnsibleInv2date(targetIp);
        execute(jobName2date, jobxml);
        return getJobStatus(jobName2date);
    }

    /**
     * 追加targetIp服务器/etc/hosts
     * @param targetIp
     * @param mapIpHostName
     */
    public boolean modihost2machine(String targetIp, Map<String, String> mapIpHostName) throws IOException, URISyntaxException {
        //生成模板字符串
        String jobxml = buildAnsibleInv2ModeHosts(targetIp, mapIpHostName);
        execute(jobName2modihost, jobxml);
        return getJobStatus(jobName2modihost);
    }
    /**
     * 执行和创建修改jenkins的job
     * @param jobName
     * @param jobxml
     * @throws IOException
     * @throws URISyntaxException
     */
    public void execute(String jobName, String jobxml) throws IOException, URISyntaxException {
        if (jenkins == null) {
            throw new IllegalArgumentException("create jenkins server is null");
        }
        JobWithDetails job = jenkins.getJob(jobName);
        if (job == null) {
            //job是空代表,jenkins不存在该job
            job = createJob(jobName, jobxml);
        } else {
            updateJob(jobName, jobxml);
        }
        buildJob(job);
    }

    /**
     * 初始化jenkins
     * @throws URISyntaxException
     */
    private void initjenkins(String jenkinsUri, String jenkinsUser, String jenkinsPassword) throws URISyntaxException {
        jenkins = new JenkinsServer(new URI(jenkinsUri), jenkinsUser, jenkinsPassword);
    }

    /**
     * 创建jenkins的job
     * @param jobName
     * @param xmlstr
     * @return
     * @throws IOException
     */
    private JobWithDetails createJob(String jobName, String xmlstr) throws IOException {
        jenkins.createJob(jobName, xmlstr, true);
        JobWithDetails job = jenkins.getJob(jobName);
        return job;
    }

    /**
     * 更新jenkins的job
     * @param jobName
     * @param xmlstr
     * @throws IOException
     */
    private void updateJob(String jobName, String xmlstr) throws IOException {
        jenkins.updateJob(jobName, xmlstr);
    }

    /**
     * 启动jenkins的job
     * @param job
     * @throws IOException
     */
    private void buildJob(JobWithDetails job) throws IOException {
        job.build(true);
    }

    private boolean getJobStatus(String jobName) throws IOException {
        int sleepTime = 20;
        //延时获取状态
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BuildResult buildResult = null;
        for (int i = 0;i < TRY_RESULT_TIMES && buildResult == null; i++) {
            buildResult = jenkins.getJob(jobName).getLastBuild().details().getResult();
            //如果没获取到结果状态，再尝试读取
            if (buildResult == null) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (i > 2) {
                    sleepTime = sleepTime*2;
                }
            }
        }
        if (buildResult == null)
            return false;
        String result = buildResult.name();
        return JOB_SUCCESS.equalsIgnoreCase(result);
    }

    /**
     * 健康度检查，获取日期
     * @param targetIp
     * @return
     * @throws IOException
     */
    private String buildAnsibleInv2date(String targetIp) throws IOException {
        Map<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("group", ansibleGroup);
        valuesMap.put("byIp", targetIp);
        valuesMap.put("command", COMMAND_DATE);
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String resolvedString = sub.replace(JOBTEMPXML);
        return resolvedString;
    }

    /**
     * 构建写/etc/hosts的xml
     * @param targetIp
     * @param mapIpHostName
     * @return
     */
    private String buildAnsibleInv2ModeHosts(String targetIp, Map<String, String> mapIpHostName) {
        //生成command的字符串
        StringBuilder sblCommand = new StringBuilder(100);
        mapIpHostName.forEach((appendIp, appendHostname)->{
            Map<String, Object> cmdValueMap = new HashMap<String, Object>();
            cmdValueMap.put("appendIp", appendIp);
            cmdValueMap.put("appendHostname", appendHostname);
            StringSubstitutor sub = new StringSubstitutor(cmdValueMap);
            String cmdString = sub.replace(COMMAND_MODIHOSTS);
            sblCommand.append(cmdString);
        });


        //生成jenkins模板字符串
        Map<String, Object> jobtempXML = new HashMap<String, Object>();
        jobtempXML.put("group", ansibleGroup);
        jobtempXML.put("byIp", targetIp);
        jobtempXML.put("command", sblCommand.toString());
        StringSubstitutor subJobTempXML = new StringSubstitutor(jobtempXML);
        String jobtempxmlstr = subJobTempXML.replace(JOBTEMPXML);
        return jobtempxmlstr;
    }

    private static final String COMMAND_DATE = "date";
    private static final String COMMAND_MODIHOSTS = "sed -i '/${appendIp} ${appendHostname}/d' /etc/hosts;echo '${appendIp} ${appendHostname}' >> /etc/hosts;";

    private static final String JOBTEMPXML =
            "<?xml version='1.0' encoding='UTF-8'?>                                                   "+
                    "<project>                                                                                "+
                    "  <actions/>                                                                             "+
                    "  <description></description>                                                            "+
                    "  <keepDependencies>false</keepDependencies>                                             "+
                    "  <properties/>                                                                          "+
                    "  <scm class=\"hudson.scm.NullSCM\"/>                                                      "+
                    "  <canRoam>true</canRoam>                                                                "+
                    "  <disabled>false</disabled>                                                             "+
                    "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>             "+
                    "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>                 "+
                    "  <triggers/>                                                                            "+
                    "  <concurrentBuild>false</concurrentBuild>                                               "+
                    "  <builders>                                                                             "+
                    "    <org.jenkinsci.plugins.ansible.AnsibleAdHocCommandBuilder plugin=\"ansible@1.0\">      "+
                    "      <ansibleName>ansible</ansibleName>                                                 "+
                    "      <credentialsId></credentialsId>                                                    "+
                    "      <vaultCredentialsId></vaultCredentialsId>                                          "+
                    "      <hostPattern>${byIp}</hostPattern>                                            "+
                    "      <inventory class=\"org.jenkinsci.plugins.ansible.InventoryContent\">                 "+
                    "        <content>[${group}]\n"+
                    "${byIp}</content>"+
                    "        <dynamic>false</dynamic>                                                         "+
                    "      </inventory>                                                                       "+
                    "      <module>shell</module>                                                             "+
                    "      <command>${command}</command>                                                        "+
                    "      <become>false</become>                                                             "+
                    "      <becomeUser></becomeUser>                                                          "+
                    "      <sudo>false</sudo>                                                                 "+
                    "      <sudoUser></sudoUser>                                                              "+
                    "      <forks>5</forks>                                                                	  "+
                    "      <unbufferedOutput>true</unbufferedOutput>                                          "+
                    "      <colorizedOutput>false</colorizedOutput>                                           "+
                    "      <disableHostKeyChecking>false</disableHostKeyChecking>                             "+
                    "      <additionalParameters></additionalParameters>                                      "+
                    "    </org.jenkinsci.plugins.ansible.AnsibleAdHocCommandBuilder>                          "+
                    "  </builders>                                                                            "+
                    "  <publishers/>                                                                          "+
                    "  <buildWrappers/>                                                                       "+
                    "</project>                                                                               ";



//    private String loadTempXml() throws IOException {
//        InputStream in=HealthCheck.class.getClassLoader().getResourceAsStream(jobTempXml);
//        String str = IOUtils.toString(in, "utf-8");
//        return str;
//    }
//    private static final String jenkinsUri = "http://192.168.59.3:8080";
//    private static final String jenkinsUser = "root";
//    private static final String password = "system";
    //    public static void main(String[] args) throws Exception {
//        HealthCheck jenkinsTest = new HealthCheck();
//        System.out.println("result:" + jenkinsTest.health2machine("192.168.59.2"));
//    }

//    public static void main(String[] args) throws Exception {
//        HealthCheck jenkinsTest = new HealthCheck();
//        System.out.println("result:" +
//                jenkinsTest.modihost2machine("192.168.59.2", "192.168.59.3", "ansible33"));
//    }

}
