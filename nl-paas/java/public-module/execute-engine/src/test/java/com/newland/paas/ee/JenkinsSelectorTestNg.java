package com.newland.paas.ee;

import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.installer.config.JenkinsConfig;
import com.newland.paas.ee.installer.config.JenkinsConfigSelector;
import com.offbytwo.jenkins.JenkinsServer;
import mockit.Mock;
import mockit.MockUp;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class JenkinsSelectorTestNg {
  	@Test
	public void testSelectJenkins() {
  		System.out.println("hello");
		for ( int i=0; i<10; ++i ) {
			testOneSelect();
		}
	}
  	
	public void testOneSelect()
	{ 
		List<JenkinsConfig> jenkinsList = new LinkedList<>();
		InstallTestUtil.addJenkinsConfig("localAnsible", "http://192.168.56.104:9999", "user", "password", jenkinsList);
		InstallTestUtil.addJenkinsConfig("ansible105", "http://192.168.56.105:9999", "user", "password", jenkinsList);
		InstallTestUtil.addJenkinsConfig("ansible106", "http://192.168.56.106:9999", "user", "password", jenkinsList);
		InstallTestUtil.addJenkinsConfig("ansible107", "http://192.168.56.107:9999", "user", "password", jenkinsList);
		InstallTestUtil.addJenkinsConfig("ansible201", "http://192.168.56.201:9999", "user", "password", jenkinsList);
		MockUp<InstallerConfig> mapperMock =new MockUp<InstallerConfig>() {
			JenkinsConfigSelector jenkinsConfigSelector = null;
			@Mock
			public void setJenkins( List<JenkinsConfig>  jenkinsConfigList ) {
				MockUp<JenkinsConfigSelector> m =new MockUp<JenkinsConfigSelector>() {
					@Mock
					private JenkinsServer createJenkinsServer( String url, String userName, String password) throws URISyntaxException
					{						
						MockUp<JenkinsServer> mt =new MockUp<JenkinsServer>() {
							@Mock
							public boolean isRunning(){
								return true;
							}
							@Mock
							public void close() {						
							}
						};
						MockUp<JenkinsServer> mf =new MockUp<JenkinsServer>() {
							@Mock
							public boolean isRunning(){
								return false;
							}
							@Mock
							public void close() {						
							}
						};						
						return url.equals( "http://192.168.56.104:9999")?mt.getMockInstance():mf.getMockInstance(); 
					}
				};
				jenkinsConfigSelector = m.getMockInstance();
				jenkinsConfigSelector.init(jenkinsConfigList);
			}
			@Mock
			public JenkinsServer selectJenkinsServer() {
				return jenkinsConfigSelector.selectJenkinsServer();
			}
			@Mock
			public JenkinsConfig getSelectJenkinsConfig() {
				return jenkinsConfigSelector.getSelectJenkinsConfig();
			}
		};
		InstallerConfig installerConfig =mapperMock.getMockInstance();
		installerConfig.setJenkins(jenkinsList);
        
		try (JenkinsServer jenkinsServer = installerConfig.selectJenkinsServer()) {
			JenkinsConfig jenkinsConfig = installerConfig.getSelectJenkinsConfig();
			Assert.assertEquals(jenkinsConfig.getJenkinsAnsibleAlias(), "localAnsible");
			Assert.assertEquals(jenkinsConfig.getJenkinsUrl(), "http://192.168.56.104:9999");
			Assert.assertEquals(jenkinsConfig.getJekinsUserName(), "user");
			Assert.assertEquals(jenkinsConfig.getJeninsPassword(), "password");
		}
	}
}
