package com.newland.paas.ee.installer.config;

import com.newland.paas.ee.errorcode.EeErrorCode;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.offbytwo.jenkins.JenkinsServer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class JenkinsConfigSelector {
    private static final Log log = LogFactory.getLogger(JenkinsConfigSelector.class);

    private int currentSelectjenkinsConfig;
    private int tryCount;
    private Object[] jenkinsConfigs;

    public JenkinsConfigSelector() {
    }

    public void init(List<JenkinsConfig> jenkinsConfigList) {
        jenkinsConfigs = jenkinsConfigList.toArray();
        tryCount = 0;
        currentSelectjenkinsConfig = new Random().nextInt(jenkinsConfigs.length);
    }

    public JenkinsServer selectJenkinsServer() {
        JenkinsServer jenkinsServer = null;
        while (tryCount < jenkinsConfigs.length) {
            try {
                JenkinsConfig jenkinsConfig = (JenkinsConfig) jenkinsConfigs[currentSelectjenkinsConfig];
                jenkinsServer = createJenkinsServer(jenkinsConfig.getJenkinsUrl(),
                        jenkinsConfig.getJekinsUserName(), jenkinsConfig.getJeninsPassword());
                if (jenkinsServer.isRunning()) {
                    log.info(LogProperty.LOGTYPE_DETAIL, "jenkins server is running, select it : " + jenkinsConfig.toString());
                    return jenkinsServer;
                } else {
                    log.info(LogProperty.LOGTYPE_DETAIL, "jenkins server is not running, try select another : " + jenkinsConfig.toString());
                    jenkinsServer.close();
                    jenkinsServer = null;
                    ++tryCount;
                    ++currentSelectjenkinsConfig;
                    if (currentSelectjenkinsConfig == jenkinsConfigs.length)
                        currentSelectjenkinsConfig = 0;
                }
            } catch (Exception e) {
                if (jenkinsServer != null) {
                    jenkinsServer.close();
                    jenkinsServer = null;
                }
                JenkinsConfig jenkinsConfig = (JenkinsConfig) jenkinsConfigs[currentSelectjenkinsConfig];
                log.error(LogProperty.LOGTYPE_DETAIL, EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR.getCode(),
                        e, EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR.getDescription() + " , select jenkins : " + jenkinsConfig.toString());
                ++tryCount;
                ++currentSelectjenkinsConfig;
                if (currentSelectjenkinsConfig == jenkinsConfigs.length)
                    currentSelectjenkinsConfig = 0;
            }
        }

        throw new RuntimeException("no suitable jenkins server");
    }

    private JenkinsServer createJenkinsServer(String url, String userName, String password) throws URISyntaxException {
        return new JenkinsServer(new URI(url), userName, password);
    }

    public JenkinsConfig getSelectJenkinsConfig() {
        return (JenkinsConfig) jenkinsConfigs[currentSelectjenkinsConfig];
    }

    public List<JenkinsConfig> getJenkinsConfigs() {
        List<JenkinsConfig> result = new LinkedList<>();
        for (Object object : jenkinsConfigs) {
            result.add((JenkinsConfig) object);
        }
        return result;
    }

}
