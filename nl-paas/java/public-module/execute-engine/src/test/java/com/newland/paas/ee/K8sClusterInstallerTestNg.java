package com.newland.paas.ee;

//@Test
public class K8sClusterInstallerTestNg {
	/*
	@BeforeTest
	public void init() {
		MockUp<JenkinsServer> mapperMock =new MockUp<JenkinsServer>() {
			// 想Mock哪个方法，就给哪个方法加上@Mock， 没有@Mock的方法，不受影响
			@Mock public boolean isRunning() {
				return true;
			}
		}

		public void createJob(String jobName, String jobXml) throws IOException
		{

		}



	}

	{
		public static String createAndBuildJobParam (JenkinsServer jenkinsServer, String jobPrefix, String
		deployStep, Map < String, String > deployParameter) throws Exception {
	}

	public void testClusterInstall()  throws Exception{


		JenkinsConfig jenkinsConfig = new JenkinsConfig();
		jenkinsConfig.setJenkinsAnsibleAlias("localAnsible");
		jenkinsConfig.setJenkinsUrl("http://192.168.56.104:9999");
		jenkinsConfig.setJekinsUserName("user");
		jenkinsConfig.setJenkinsPassword("password");
		List<JenkinsConfig> jenkinsList = new LinkedList<>();
		jenkinsList.add(jenkinsConfig);
		InstallerConfig installerConfig = new InstallerConfig();
		installerConfig.setJenkins(jenkinsList);
		installerConfig.setClustermagePath("10.1.8.16:5000");

		K8SClusterInstanceDetailInfo cidi  = new K8SClusterInstanceDetailInfo();
		cidi.setK8sMasters(InstallTestUtil.ipToResourceVo(Arrays.asList("192.168.56.201")));
		cidi.setFloatIp("192.168.56.201");
		cidi.setK8sWorkers(InstallTestUtil.ipToResourceVo(Arrays.asList("192.168.56.202", "192.168.56.203")));
		cidi.setHomePath("/home/k8s");
		cidi.setEtcds( InstallTestUtil.ipToResourceVo(Arrays.asList("192.168.56.203")));
		cidi.setEtcdHome("/home/etcd");
		TenantInstanceDetailInfo belongsTenantInfo = new TenantInstanceDetailInfo();
		belongsTenantInfo.setTenantAssetPath("192.168.56.110:/home/dragon/harbor");
		ExecuteEngineMgt engineMgt = new ExecuteEngineMgt();
		engineMgt.install(installerConfig, cidi, null, null, belongsTenantInfo);
	}
	*/
}
