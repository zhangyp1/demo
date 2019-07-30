package com.newland.paas.ee;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import com.newland.paas.common.constant.Constant;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.common.util.Json;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.constant.ApplicationStatusEnum;
import com.newland.paas.ee.constant.ZoneConstants;
import com.newland.paas.ee.errorcode.EeErrorCode;
import com.newland.paas.ee.health.HealthCheck;
import com.newland.paas.ee.installer.ApplicationInstaller;
import com.newland.paas.ee.installer.ResourcePrepare;
import com.newland.paas.ee.installer.SecretFreeInstaller;
import com.newland.paas.ee.installer.clu.ClusterInstaller;
import com.newland.paas.ee.installer.clu.IngressInstaller;
import com.newland.paas.ee.installer.config.InstallerConfig;
import com.newland.paas.ee.service.IAppExecuteEngine;
import com.newland.paas.ee.service.IClusterExecuteEngine;
import com.newland.paas.ee.service.IHealthExecuteEngine;
import com.newland.paas.ee.service.IIngressExecuteEngine;
import com.newland.paas.ee.service.IResExecuteEngine;
import com.newland.paas.ee.service.IZoneExecuteEngine;
import com.newland.paas.ee.vo.AppInstanceDetailInfo;
import com.newland.paas.ee.vo.AssetDetailInfo;
import com.newland.paas.ee.vo.BuildResult;
import com.newland.paas.ee.vo.ClusterInstanceDetailInfo;
import com.newland.paas.ee.vo.DependInstanceInfo;
import com.newland.paas.ee.vo.IngressInfo;
import com.newland.paas.ee.vo.OperateResult;
import com.newland.paas.ee.vo.ProgressDetailResult;
import com.newland.paas.ee.vo.ResourceDetailInfo;
import com.newland.paas.ee.vo.StageResult;
import com.newland.paas.ee.vo.TenantInstanceDetailInfo;
import com.newland.paas.ee.vo.application.AppConfig;
import com.newland.paas.ee.vo.application.AppEndpoint;
import com.newland.paas.ee.vo.application.AppProp;
import com.newland.paas.ee.vo.asset.AstExternalSvrIntfRsp;
import com.newland.paas.ee.vo.asset.AstInstancePropRsp;
import com.newland.paas.ee.vo.cluster.CluZoneVo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:ExecuteEngineMgt
 * @Description: IClusterExecuteEngine, IAppExecuteEngine实现类
 * @Funtion List:
 * @Date 2018年7月2日        下午2:02:01
 */
public class ExecuteEngineMgt implements IClusterExecuteEngine, IAppExecuteEngine, IResExecuteEngine, IIngressExecuteEngine, IHealthExecuteEngine, IZoneExecuteEngine {

    private static final Log log = LogFactory.getLogger(ExecuteEngineMgt.class);

    private static final String TAG = "ExecuteEngineMgt======";

    private static Map<String, String> instanceStatusMap = new ConcurrentHashMap<String, String>();

    public static final String APP_STAGE_STATUS_SUCCESS = "SUCCESS";

    public static final String APP_STAGE_STATUS_FAIL = "FAILED";

    public static final String APP_STAGE_STATUS_EXECUTING = "IN_PROGRESS";

    public static final String INSTANCE_SUFFIX_APP = "APP_";

    public static final String INSTANCE_SUFFIX_CLUSTER = "CLUSTER_";

    public static final String INSTANCE_SUFFIX_RES = "RES_";

    /**
     * 安装
     *
     * @param clusterInstanceDetailInfo 执行的对象，如集群
     * @param paramMaps                 界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
     * @param assetDetailInfo           依赖的资产详情信息
     * @param belongsTenantInfo         执行对象所属租户信息
     * @return OperateResult            操作返回结果，如：进度查询地址、Job名称、构建号等
     */
    public OperateResult install(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                 Map<String, Object> paramMaps,
                                 AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ClusterInstaller.install(installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + clusterInstanceDetailInfo.getClusterId(), ApplicationStatusEnum.IN_INSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    /**
     * 卸载
     *
     * @param clusterInstanceDetailInfo 执行的对象，如集群
     * @param paramMaps                 界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
     * @param assetDetailInfo           依赖的资产详情信息
     * @param belongsTenantInfo         执行对象所属租户信息
     * @return OperateResult            操作返回结果，如：进度查询地址、Job名称、构建号等
     */
    public OperateResult uninstall(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                   Map<String, Object> paramMaps,
                                   AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ClusterInstaller.uninstall(installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + clusterInstanceDetailInfo.getClusterId(), ApplicationStatusEnum.IN_UNINSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    /**
     * 启动
     *
     * @param clusterInstanceDetailInfo 执行的对象，如集群
     * @param paramMaps                 界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
     * @param assetDetailInfo           依赖的资产详情信息
     * @param belongsTenantInfo         执行对象所属租户信息
     * @return OperateResult            操作返回结果，如：进度查询地址、Job名称、构建号等
     */
    public OperateResult start(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                               Map<String, Object> paramMaps,
                               AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ClusterInstaller.start(installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + clusterInstanceDetailInfo.getClusterId(), ApplicationStatusEnum.IN_STARTUP.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    /**
     * 停止
     *
     * @param clusterInstanceDetailInfo 执行的对象，如集群
     * @param paramMaps                 界面输入参数 Map<单元名称, Map<参数节, Map<参数key, 参数值>>>
     * @param assetDetailInfo           依赖的资产详情信息
     * @param belongsTenantInfo         执行对象所属租户信息
     * @return OperateResult            操作返回结果，如：进度查询地址、Job名称、构建号等
     */
    public OperateResult stop(InstallerConfig installConfig, ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                              Map<String, Object> paramMaps,
                              AssetDetailInfo assetDetailInfo, TenantInstanceDetailInfo belongsTenantInfo) {

        try {
            OperateResult operateResult = ClusterInstaller.stop(installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + clusterInstanceDetailInfo.getClusterId(), ApplicationStatusEnum.IN_STOP.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult scaleOut(InstallerConfig installConfig,
                                  ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                  Map<String, Object> paramMaps,
                                  AssetDetailInfo assetDetailInfo,
                                  TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ClusterInstaller.scaleOut(installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + clusterInstanceDetailInfo.getClusterId(), ApplicationStatusEnum.IN_INSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult scaleIn(InstallerConfig installConfig,
                                 ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                 Map<String, Object> paramMaps,
                                 AssetDetailInfo assetDetailInfo,
                                 TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ClusterInstaller.scaleIn(installConfig, clusterInstanceDetailInfo, paramMaps, assetDetailInfo, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + clusterInstanceDetailInfo.getClusterId(), ApplicationStatusEnum.IN_UNINSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }


    /**
     * 获取执行过程描述
     *
     * @param req 操作结果
     * @return ProgressDetailResult    进度明细信息
     */
    public ProgressDetailResult getClusterExecuteDescribe(OperateResult req) {
        return this.getExecuteDescribe(req, INSTANCE_SUFFIX_CLUSTER);
    }

    /**
     * 获取执行结果信息	调用前提: job执行完成
     *
     * @param
     * @return Map<String, String>		返回信息，包括：输出属性信息、endPoint等
     */
    public Map<String, String> getClusterExecuteResult(OperateResult req) {
        checkArguments(req);
        JenkinsServer server = null;
        try {
            server = new JenkinsServer(new URI(req.getProgressUrl()), req.getUserName(), req.getPasswd());
            JobWithDetails job = server.getJob(req.getJobName());
            if (job == null) {
                throw new IllegalArgumentException("任务" + req.getJobName() + "不存在,请确认任务名称");
            }
            Build lastBuild = job.getLastCompletedBuild();
            String text = lastBuild.details().getConsoleOutputText();
            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "ConsoleOutputText:" + text);
            Map<String, String> outParams = new HashMap<String, String>();
            Map<String, String> instanceProps = new HashMap<String, String>();
            instanceProps.put(ZoneConstants.CLUATTR_MASTER_FLOAT_IP, "");
            instanceProps.put(ZoneConstants.CLUATTR_API_SERVER, "");
            instanceProps.put(ZoneConstants.CLUATTR_K8S_PROMETHEUS_ADDRESS, "");
            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "instanceProps:" + instanceProps);
            if (instanceProps != null && instanceProps.size() > 0) {
                for (String propName : instanceProps.keySet()) {
                    String propKey = propName + " = ";
                    int indexLeft = text.indexOf(propKey);
                    if (indexLeft > 0) {
                        int indexRight = text.indexOf("\"\r\n", indexLeft);
                        String propVal = text.substring(indexLeft + propKey.length(), indexRight);
                        outParams.put(propName, propVal.trim());
                    }
                }
            }
            if (log.isInfoEnabled()) {
                log.info(LogProperty.LOGTYPE_DETAIL, TAG + "outParams:" + outParams);
            }
            return outParams;
        } catch (Throwable e) {
            throw new NLUnCheckedException(EeErrorCode.EXEENGINE_GDATA_FAIL.getCode(), EeErrorCode.EXEENGINE_GDATA_FAIL.getDescription(), e.getCause());
        } finally {
            server.close();
        }
    }

    @Override
    public OperateResult install(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo,
                                 AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList,
                                 Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ApplicationInstaller.install(installConfig, appInstanceDetailInfo,
                    appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_APP + appInstanceDetailInfo.getAppInfoId(), ApplicationStatusEnum.IN_INSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult uninstall(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo,
                                   AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList,
                                   Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ApplicationInstaller.uninstall(installConfig, appInstanceDetailInfo, appAssetDetailInfo,
                    dependInstanceInfoList, targetClusterList, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_APP + appInstanceDetailInfo.getAppInfoId(), ApplicationStatusEnum.IN_UNINSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult start(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo,
                               AssetDetailInfo appAssetDetailInfo, List<DependInstanceInfo> dependInstanceInfoList,
                               Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ApplicationInstaller.start(installConfig, appInstanceDetailInfo,
                    appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_APP + appInstanceDetailInfo.getAppInfoId(), ApplicationStatusEnum.IN_STARTUP.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult stop(InstallerConfig installConfig, AppInstanceDetailInfo appInstanceDetailInfo, AssetDetailInfo appAssetDetailInfo,
                              List<DependInstanceInfo> dependInstanceInfoList, Map<String, ClusterInstanceDetailInfo> targetClusterList, TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ApplicationInstaller.stop(installConfig, appInstanceDetailInfo, appAssetDetailInfo,
                    dependInstanceInfoList, targetClusterList, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_APP + appInstanceDetailInfo.getAppInfoId(), ApplicationStatusEnum.IN_STOP.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult scaleIn(InstallerConfig installConfig,
                                 AppInstanceDetailInfo appInstanceDetailInfo,
                                 AssetDetailInfo appAssetDetailInfo,
                                 List<DependInstanceInfo> dependInstanceInfoList,
                                 Map<String, ClusterInstanceDetailInfo> targetClusterList,
                                 TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ApplicationInstaller.scaleIn(installConfig, appInstanceDetailInfo,
                    appAssetDetailInfo, dependInstanceInfoList, targetClusterList, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_APP + appInstanceDetailInfo.getAppInfoId(),
                    ApplicationStatusEnum.IN_UNINSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }


    @Override
    public OperateResult scaleOut(InstallerConfig installConfig,
                                  AppInstanceDetailInfo appInstanceDetailInfo,
                                  AssetDetailInfo appAssetDetailInfo,
                                  List<DependInstanceInfo> dependInstanceInfoList,
                                  Map<String, ClusterInstanceDetailInfo> targetClusterList,
                                  TenantInstanceDetailInfo belongsTenantInfo) {
        try {
            OperateResult operateResult = ApplicationInstaller.scaleOut(installConfig, appInstanceDetailInfo, appAssetDetailInfo,
                    dependInstanceInfoList, targetClusterList, belongsTenantInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_APP + appInstanceDetailInfo.getAppInfoId(), ApplicationStatusEnum.IN_INSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    /**
     * @param stageMap
     * @return StageResult
     * @Function:     createStageResult 
     * @Description:  阶段结果集
     */
    private StageResult createStageResult(Map<?, ?> stageMap) {
        StageResult stageResult = new StageResult();
        stageResult.setStageName(String.valueOf(stageMap.get("name")));
        stageResult.setStatus(String.valueOf(stageMap.get("status")));
        stageResult.setStartTimeMillis(Long.parseLong(String.valueOf(stageMap.get("startTimeMillis"))));
        stageResult.setDurationMillis(Long.parseLong(String.valueOf(stageMap.get("durationMillis"))));
        stageResult.setPauseDurationMillis(Long.parseLong(String.valueOf(stageMap.get("pauseDurationMillis"))));
        return stageResult;
    }


    /**
     * @param req
     * @param map
     * @return BuildResult
     * @Function:     createBuildResult 
     * @Description:  构建结果
     */
    private BuildResult createBuildResult(OperateResult req, Map<?, ?> map) {
        BuildResult buildResult = new BuildResult();
        buildResult.setBuildNumber(req.getBuildNumber());
        buildResult.setDurationMillis(Long.parseLong(String.valueOf(map.get("durationMillis"))));
        buildResult.setEndTimeMillis(Long.parseLong(String.valueOf(map.get("endTimeMillis"))));
        buildResult.setPauseDurationMillis(Long.parseLong(String.valueOf(map.get("pauseDurationMillis"))));
        buildResult.setQueueDurationMillis(Long.parseLong(String.valueOf(map.get("queueDurationMillis"))));
        buildResult.setStartTimeMillis(Long.parseLong(String.valueOf(map.get("startTimeMillis"))));
        buildResult.setStatus(String.valueOf(map.get("status")));
        return buildResult;
    }

    /**
     * @param req
     * @return Object
     * @throws Exception
     * @Function:     getUrlData 
     * @Description:  获取uri数据
     */
    private Object getUriData(OperateResult req) throws Exception {
        //拼凑请求地址
        StringBuffer lineBuffer = new StringBuffer(req.getProgressUrl());
        lineBuffer.append(Constant.FILE_OBLIQUE_LINE).append("job");
        lineBuffer.append(Constant.FILE_OBLIQUE_LINE).append(URLEncoder.encode(req.getJobName(),"UTF-8").replaceAll("\\+", "%20"))
                .append(Constant.FILE_OBLIQUE_LINE).append(req.getBuildNumber()).append(ENGINE_SUFIX);
        if (log.isInfoEnabled()) {
            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "开始获取" + lineBuffer.toString() + "的数据");
        }
        JenkinsHttpClient url = null;
        InputStream inputStream = null;
        try {
            url = new JenkinsHttpClient(new URI(req.getProgressUrl() + Constant.FILE_OBLIQUE_LINE + "job",req.getUserName(), req.getPasswd()));
            System.out.println("lineBuffer.toString:"+lineBuffer.toString());
            inputStream = url.getFile(new URI(lineBuffer.toString()));
            byte[] bytes = new byte[1024];
            StringBuffer strBuffer = new StringBuffer();
            int len = 0;
            while ((len = inputStream.read(bytes)) > 0) {
                strBuffer.append(new String(bytes, 0, len, Charset.forName("UTF-8")));
            }
            System.out.println(strBuffer.toString());
            Object obj = Json.toObject(strBuffer.toString());
            if (log.isInfoEnabled()) {
                log.info(LogProperty.LOGTYPE_DETAIL, TAG + "获取数据结束,result:" + obj);
            }
            return obj;
        } catch(HttpResponseException e) {
        	if(e.getStatusCode() == HttpStatus.SC_NOT_FOUND){
        		log.info(LogProperty.LOGTYPE_DETAIL, TAG+"JobNotFound,statusCode:"+e.getStatusCode());
        		return null;
        	}
        	throw e;
        }catch (Throwable e) {
            log.error(LogProperty.LOGTYPE_DETAIL, null, e, TAG + e.getMessage());
            throw e;
        } finally {
            LogFactory.closeStartLog();
            if (url != null) {
                url.close();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error(LogProperty.LOGTYPE_DETAIL, null, e, TAG + e.getMessage());
            }
        }
    }
    
    /**
     * @param req
     * @return ProgressDetailResult
     * @Function: getAppExecuteDescribe
     * @Description: App执行描述
     */
    @Override
    public ProgressDetailResult getAppExecuteDescribe(OperateResult req) {
        return this.getExecuteDescribe(req, INSTANCE_SUFFIX_APP);
    }

    /**
     * @param req
     * @param suffix
     * @return ProgressDetailResult
     * @Function:     getAppExecuteDescribe 
     * @Description:  获取app执行描述
     */
    public ProgressDetailResult getExecuteDescribe(OperateResult req, String suffix) {
        try {
            checkArguments(req);
            Map<?, ?> map = (Map<?, ?>) getUriData(req);
            ProgressDetailResult result = new ProgressDetailResult();
            if(map != null) {
	            result.setOperateResult(req);
	            //总结果
	            BuildResult buildResult = createBuildResult(req, map);
	            result.setBuildResult(buildResult);
	            //阶段结果
	            List<StageResult> stageResultList = new ArrayList<StageResult>();
	            List<?> list = (List<?>) map.get("stages");
	            if (null != list && list.size() > 0) {
	                for (int i = 0; i < list.size(); i++) {
	                    Map<?, ?> stageMap = (Map<?, ?>) list.get(i);
	                    StageResult stageResult = createStageResult(stageMap);
	                    stageResultList.add(stageResult);
	                }
	            }
	            result.setStageResultList(stageResultList);
	            if (APP_STAGE_STATUS_SUCCESS.equals(result.getBuildResult().getStatus())) {
	                //状态流转  安装中转为运行  停止中转为停止
	                String appInfoId = req.getJobName();
	                String status = instanceStatusMap.get(req.getJobName());
	                if (ApplicationStatusEnum.IN_INSTALL.code.equals(status)) {
	                    instanceStatusMap.put(suffix + appInfoId, ApplicationStatusEnum.RUNNING.code);
	                } else if (ApplicationStatusEnum.IN_STOP.code.equals(status)) {
	                    instanceStatusMap.put(suffix + appInfoId, ApplicationStatusEnum.STOP.code);
	                }
	            }
            }
            if (log.isInfoEnabled()) {
                log.info(LogProperty.LOGTYPE_DETAIL, TAG + "获取数据集为:" + result);
            }
            return result;
        } catch (Exception e) {
            throw new NLUnCheckedException(EeErrorCode.EXEENGINE_GDATA_FAIL.getCode(), EeErrorCode.EXEENGINE_GDATA_FAIL.getDescription(), e.getCause());
        }
    }

    /**
     * @param req
     * @Function:     checkArguments 
     * @Description:  校验参数
     */
    private void checkArguments(OperateResult req) {
        if (StringUtils.isBlank(req.getProgressUrl())) {
            throw new IllegalArgumentException("url为空,请校验参数!");
        }
        if (StringUtils.isBlank(req.getJobName())) {
            throw new IllegalArgumentException("任务名为空,请校验参数!");
        }
        if (StringUtils.isBlank(req.getUserName())) {
            throw new IllegalArgumentException("用户名为空,请校验参数!");
        }
        if (StringUtils.isBlank(req.getPasswd())) {
            throw new IllegalArgumentException("密码为空,请校验参数!");
        }
    }

    /**
     * @param appInstanceDetailInfo
     * @param appAssetDetailInfo
     * @param req
     * @return AppInstanceDetailInfo
     * @Function: getAppExecuteResult
     * @Description: 获取执行结果
     */
    @Override
    public AppInstanceDetailInfo getAppExecuteResult(AppInstanceDetailInfo appInstanceDetailInfo, AssetDetailInfo appAssetDetailInfo, OperateResult req) {
        log.info(LogProperty.LOGTYPE_DETAIL, TAG + "Start getAppExecuteResult");
        checkArguments(req);
        JenkinsServer server = null;
        String appInfoId = INSTANCE_SUFFIX_APP + req.getJobName();
        String appStatus = instanceStatusMap.get(appInfoId);
        try {
            server = new JenkinsServer(new URI(req.getProgressUrl()), req.getUserName(), req.getPasswd());
            JobWithDetails job = server.getJob(req.getJobName());
            if (job == null) {
                throw new IllegalArgumentException("任务" + req.getJobName() + "不存在,请确认任务名称");
            }
            Build lastBuild = job.getLastCompletedBuild();
            String text = lastBuild.details().getConsoleOutputText();
            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "ConsoleOutputText:" + text);
            Map<String, String> astInsProps = new HashMap<String, String>();
            List<AstInstancePropRsp> instanceProps = appAssetDetailInfo.getInstanceProperties();
            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "InstanceProperties:" + appAssetDetailInfo.getInstanceProperties());
            //获取资产属性值
            if (instanceProps != null && instanceProps.size() > 0) {
                for (AstInstancePropRsp prop : appAssetDetailInfo.getInstanceProperties()) {
                    String propName = prop.getPropName().trim();
                    String propKey = propName + " = ";
                    int indexLeft = text.indexOf(propKey);
                    if (indexLeft > 0) {
                        int indexRight = text.indexOf("\"\r\n", indexLeft);
                        String propVal = text.substring(indexLeft + propKey.length(), indexRight);
                        astInsProps.put(prop.getPropName(), propVal.trim());
                    }
                }
            }
            //获取LB_VALUE值
            Map<String, List<Map<String, String>>> svrIntfMap = new HashMap<String, List<Map<String, String>>>();
            String intfSearchKey = ZoneConstants.LOAD_BALANCE_KEY + " = ";
            int indexLeft = 0;
            List<String> interfaceUrls = new ArrayList<String>();
            while ((indexLeft = text.indexOf(intfSearchKey, indexLeft)) >= 0) {
                int indexRight = text.indexOf("\"\r\n", indexLeft);
                String interfaceUrl = text.substring(indexLeft + intfSearchKey.length(), indexRight);
                boolean isFormat = interfaceUrl.startsWith(IAppExecuteEngine.ENGINE_PARAM_LEFT) && interfaceUrl.endsWith(IAppExecuteEngine.ENGINE_PARAM_RIGHT);
                if (isFormat) {
                    interfaceUrl = interfaceUrl.substring(IAppExecuteEngine.ENGINE_PARAM_LEFT.length());
                    interfaceUrl = interfaceUrl.substring(0, interfaceUrl.length() - 1);
                    if (interfaceUrls.contains(interfaceUrl)) {
                        log.info(LogProperty.LOGTYPE_DETAIL, TAG + ZoneConstants.LOAD_BALANCE_KEY + "访问点interfaceUrl已存在:" + interfaceUrl);
                        indexLeft = indexRight;
                        continue;
                    } else {
                        interfaceUrls.add(interfaceUrl);
                    }
                    String[] params = interfaceUrl.split("##");
                    if (params.length != 4) {
                        throw new IllegalArgumentException(ZoneConstants.LOAD_BALANCE_KEY + "值:" + interfaceUrl + "不符合${initKey##lbName##ip##port}规范");
                    }
                    Map<String, String> svrIntfParams = new HashMap<String, String>();
                    svrIntfParams.put(ZoneConstants.LOAD_BALANCE_KEY_INITKEY, params[0]);
                    svrIntfParams.put(ZoneConstants.LOAD_BALANCE_KEY_LBNAME, params[1]);
                    svrIntfParams.put(ZoneConstants.LOAD_BALANCE_KEY_IP, params[2]);
                    svrIntfParams.put(ZoneConstants.LOAD_BALANCE_KEY_PORT, params[3]);
                    if (!svrIntfMap.containsKey(params[0])) {
                        List<Map<String, String>> intfParamList = new ArrayList<Map<String, String>>();
                        intfParamList.add(svrIntfParams);
                        svrIntfMap.put(params[0], intfParamList);
                        log.info(LogProperty.LOGTYPE_DETAIL, TAG + ZoneConstants.LOAD_BALANCE_KEY + "访问点参数列表中加入initKey:" + params[0] + " svrIntfParams:" + svrIntfParams);
                    } else {
                        svrIntfMap.get(params[0]).add(svrIntfParams);
                        log.info(LogProperty.LOGTYPE_DETAIL, TAG + "访问点参数列表加入initKey:" + params[0] + " svrIntfParams:" + svrIntfParams);
                    }
                } else {
                    log.info(LogProperty.LOGTYPE_DETAIL, TAG + "获取到的" + ZoneConstants.LOAD_BALANCE_KEY + "非${}字符被舍弃,interfaceUrl:" + interfaceUrl);
                }
                indexLeft = indexRight;
            }
            log.info(LogProperty.LOGTYPE_DETAIL, TAG + ZoneConstants.LOAD_BALANCE_KEY + "======:" + svrIntfMap);
            if (log.isInfoEnabled()) {
                log.info(LogProperty.LOGTYPE_DETAIL, TAG + "astInsProps:" + astInsProps);
            }
            //设置应用属性信息
            //if(ApplicationStatusEnum.RUNNING.code.equals(appStatus)){
            List<AppProp> appProps = new ArrayList<AppProp>();
            if (astInsProps != null && astInsProps.size() > 0) {
                Iterator<String> iterator = astInsProps.keySet().iterator();
                while (iterator.hasNext()) {
                    AppProp appProp = new AppProp();
                    String propKey = iterator.next();
                    appProp.setPropKey(propKey);
                    appProp.setPropVal(astInsProps.get(propKey));
                    appProps.add(appProp);
                }
                appInstanceDetailInfo.setAppProps(appProps);
            }
            /* 访问点名称endPointName在appAssetDetailInfo.getInstanceProperties()的propName或appAssetDetailInfo.getSvrInterfaces()中intfKey，
			 * AstExternalSvrIntfRsp中srvAddrFormat含有jdbc:mysql://${mysql_ip}:${mysql_port}/root
			 * ?useUnicode=true&characterEncoding=gbk&allowMultiQueries=true
             * 动态参数,可能在appAssetDetailInfo.getConfigList()或appAssetDetailInfo.getInstanceProperties()中
			 */
            //访问点名称没有,到底怎么组成，是否资产实例属性和服务接口属性都拼接返回
            List<AppEndpoint> appEndpoints = new ArrayList<AppEndpoint>();
				 /*if(astInsProps!= null && astInsProps.size() > 0){
					 Iterator<String> iterator = astInsProps.keySet().iterator();
					while(iterator.hasNext()){
						 String propKey = iterator.next();
						 AppEndpoint endPoint = new AppEndpoint();
						 endPoint.setEndpointName(propKey);
						 endPoint.setEndpointValue(astInsProps.get(propKey));
						 appEndpoints.add(endPoint);
					 }
				 }*/
            //从资产服务列表中获取
            List<AstExternalSvrIntfRsp> svrInterfaces = appAssetDetailInfo.getSvrInterfaces();
            if (svrInterfaces != null && svrInterfaces.size() > 0) {
                for (AstExternalSvrIntfRsp svnInt : svrInterfaces) {
                    String intfKey = svnInt.getIntfKey();
                    if (svrIntfMap.containsKey(intfKey)) {
                        List<Map<String, String>> intfParamList = svrIntfMap.get(intfKey);
                        for (Map<String, String> intfParam : intfParamList) {
                            AppEndpoint endPoint = new AppEndpoint();
                            String srvAddrFormat = getSrvAddrFormat(appInstanceDetailInfo, astInsProps, svnInt, intfParam);
                            if (log.isInfoEnabled()) {
                                log.info(LogProperty.LOGTYPE_DETAIL, TAG + "使用配置值替换资产服务匹配参数值,srvAddrFormat:" + srvAddrFormat);
                            }
                            //设置访问点
                            String endPointName = svnInt.getIntfKey();
                            endPoint.setEndpointName(endPointName);
                            endPoint.setEndpointIdent(intfParam.get(ZoneConstants.LOAD_BALANCE_KEY_LBNAME));
                            endPoint.setEndpointValue(srvAddrFormat);
                            appEndpoints.add(endPoint);
                        }
                    } else {
                        AppEndpoint endPoint = new AppEndpoint();
                        String srvAddrFormat = getSrvAddrFormat(appInstanceDetailInfo, astInsProps, svnInt);
                        if (log.isInfoEnabled()) {
                            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "使用配置值替换资产服务匹配参数值,srvAddrFormat:" + srvAddrFormat);
                        }
                        //设置访问点
                        String endPointName = svnInt.getIntfKey();
                        endPoint.setEndpointName(endPointName);
                        endPoint.setEndpointValue(srvAddrFormat);
                        appEndpoints.add(endPoint);
                    }
                }
            }
            appInstanceDetailInfo.setAppEndpoints(appEndpoints);
            //}else{
			    /*
				 //置空属性列表和访问点列表
				 appInstanceDetailInfo.setAppProps(Collections.<AppProp> emptyList());
				 appInstanceDetailInfo.setAppEndpoints(Collections.<AppEndpoint> emptyList());
				 if(log.isInfoEnabled()){
					log.info(LogProperty.LOGTYPE_DETAIL, TAG+"置空属性列表和访问点列表");
				 }
			 }*/
            //设置应用状态
            //appInstanceDetailInfo.setAppStatus(appStatus);
            //暂时不删除任务
            //server.deleteJob(req.getJobName());
            return appInstanceDetailInfo;
        } catch (Throwable e) {
            throw new NLUnCheckedException(EeErrorCode.EXEENGINE_GDATA_FAIL.getCode(), EeErrorCode.EXEENGINE_GDATA_FAIL.getDescription(), e.getCause());
        } finally {
            server.close();
            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "end getAppExecuteResult");
        }

    }

    /**
     * @param appInstanceDetailInfo
     * @param astInsProps
     * @param svnInt
     * @return srvAddrFormat
     * @Function:     getSrvAddrFormat 
     * @Description:   服务地址格式参数替换
     */
    private String getSrvAddrFormat(AppInstanceDetailInfo appInstanceDetailInfo,
                                    Map<String, String> astInsProps, AstExternalSvrIntfRsp svnInt, Map<String, String> intfParam) {
        String srvAddrFormat = svnInt.getSrvAddrFormat();
        String backsrvAddrFormat = srvAddrFormat;
        int indexLeft = 0;
        while ((indexLeft = srvAddrFormat.indexOf(IAppExecuteEngine.ENGINE_PARAM_LEFT, indexLeft)) >= 0) {
            if (indexLeft >= 0) {
                int indexRight = srvAddrFormat.indexOf(IAppExecuteEngine.ENGINE_PARAM_RIGHT, indexLeft);
                String paramKey = srvAddrFormat.substring(indexLeft + 2, indexRight);
                paramKey = paramKey.trim();
                String paramRepKey = "\\$\\{" + paramKey + "\\}";
                indexLeft = indexRight;
                //替换参数值
                if (intfParam.containsKey(paramKey)) {
                    String val = intfParam.get(paramKey);
                    log.info(LogProperty.LOGTYPE_DETAIL, TAG + "从intfParam获取参数paramRepKey:" + val);
                    backsrvAddrFormat = backsrvAddrFormat.replaceAll(paramRepKey, val);
                } else if (astInsProps.containsKey(paramKey) && StringUtils.isNotBlank(astInsProps.get(paramKey))) {
                    String val = astInsProps.get(paramKey);
                    log.info(LogProperty.LOGTYPE_DETAIL, TAG + "从astInsProps获取参数paramRepKey:" + val);
                    backsrvAddrFormat = backsrvAddrFormat.replaceAll(paramRepKey, val);
                } else {
                    //使用配置值替换
                	List<AppConfig> configList = appInstanceDetailInfo.getAppConfigs();
                    for (AppConfig configRsp : configList) {
                        if (paramKey.equals(configRsp.getConfigName())) {
                            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "从configList获取参数paramRepKey:" + configRsp.getConfigValue());
                            if(StringUtils.isNotBlank(configRsp.getConfigValue())) {
                            	backsrvAddrFormat = backsrvAddrFormat.replaceAll(paramRepKey, configRsp.getConfigValue());
                            }else {
                              log.info(LogProperty.LOGTYPE_DETAIL, TAG + "从astInsProps获取参数srvAddrFormat:"+srvAddrFormat+","+paramKey+"对应的值为空,未进行值替换");	
                            }
                            break;
                        }
                    }
                }
                if (log.isInfoEnabled()) {
                    log.info(LogProperty.LOGTYPE_DETAIL, TAG + "使用配置值替换资产服务匹配参数值,paramKey:" + paramKey);
                }
            }
        }
        return backsrvAddrFormat;
    }

    /**
     * @param appInstanceDetailInfo
     * @param astInsProps
     * @param svnInt
     * @return srvAddrFormat
     * @Function:     getSrvAddrFormat 
     * @Description:   服务地址格式参数替换
     */
    private String getSrvAddrFormat(AppInstanceDetailInfo appInstanceDetailInfo,
                                    Map<String, String> astInsProps, AstExternalSvrIntfRsp svnInt) {
        String srvAddrFormat = svnInt.getSrvAddrFormat();
        String backsrvAddrFormat = srvAddrFormat;
        int indexLeft = 0;
        while ((indexLeft = srvAddrFormat.indexOf(IAppExecuteEngine.ENGINE_PARAM_LEFT, indexLeft)) >= 0) {
            if (indexLeft >= 0) {
                int indexRight = srvAddrFormat.indexOf(IAppExecuteEngine.ENGINE_PARAM_RIGHT, indexLeft);
                String paramKey = srvAddrFormat.substring(indexLeft + 2, indexRight);
                paramKey = paramKey.trim();
                String paramRepKey = "\\$\\{" + paramKey + "\\}";
                indexLeft = indexRight;
                //替换参数值
                if (astInsProps.containsKey(paramKey) && StringUtils.isNotBlank(astInsProps.get(paramKey))) {
                    String val = astInsProps.get(paramKey);
                    log.info(LogProperty.LOGTYPE_DETAIL, TAG + "从astInsProps获取参数paramRepKey:" + val);
                    backsrvAddrFormat = backsrvAddrFormat.replaceAll(paramRepKey, val);
                } else {
                    //使用配置值替换
                	List<AppConfig> configList = appInstanceDetailInfo.getAppConfigs();
                    for (AppConfig configRsp : configList) {
                        if (paramKey.equals(configRsp.getConfigName())) {
                            log.info(LogProperty.LOGTYPE_DETAIL, TAG + "从configList获取参数paramRepKey:" + configRsp.getConfigValue());
                            if(StringUtils.isNotBlank(configRsp.getConfigValue())) {
                            	backsrvAddrFormat = backsrvAddrFormat.replaceAll(paramRepKey, configRsp.getConfigValue());
                            }else {
                              log.info(LogProperty.LOGTYPE_DETAIL, TAG + "从astInsProps获取参数srvAddrFormat:"+backsrvAddrFormat+","+paramKey+"对应的值为空,未进行值替换");	
                            }
                            break;
                        }
                    }
                }
                if (log.isInfoEnabled()) {
                    log.info(LogProperty.LOGTYPE_DETAIL, TAG + "使用配置值替换资产服务匹配参数值,paramKey:" + paramKey);
                }
            }
        }
        return backsrvAddrFormat;
    }

    @Override
    public OperateResult remoteCipherPublish(InstallerConfig installConfig, ResourceDetailInfo resourceDetailInfo, String depotPath) {
        try (SecretFreeInstaller secretFreeInstaller = new SecretFreeInstaller(installConfig, resourceDetailInfo, depotPath)) {
            OperateResult operateResult = secretFreeInstaller.push();
            return operateResult;
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_DETAIL, EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR.getCode(),
                    e, EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR.getDescription());
            throw new NLUnCheckedException(EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR.getCode(),
                    EeErrorCode.EXECUTE_AUTOMATE_JOB_ERROR.getDescription(), e);
        }
    }

    @Override
    public ProgressDetailResult getResExecuteDescribe(OperateResult operateResult) {
        return this.getExecuteDescribe(operateResult, INSTANCE_SUFFIX_RES);
    }

    @Override
    public ResourceDetailInfo getResExecuteResult(ResourceDetailInfo resourceDetailInfo, OperateResult req) {
        checkArguments(req);
        JenkinsServer server = null;
        String appInfoId = INSTANCE_SUFFIX_RES + req.getJobName();
        try {
            server = new JenkinsServer(new URI(req.getProgressUrl()), req.getUserName(), req.getPasswd());
            JobWithDetails job = server.getJob(req.getJobName());
            if (job == null) {
                throw new IllegalArgumentException("任务" + req.getJobName() + "不存在,请确认任务名称");
            }
            Build lastBuild = job.getLastCompletedBuild();
            String text = lastBuild.details().getConsoleOutputText();
            System.out.println("text:" + text);
            server.deleteJob(req.getJobName());
            return resourceDetailInfo;
        } catch (Throwable e) {
            throw new NLUnCheckedException(EeErrorCode.EXEENGINE_GDATA_FAIL.getCode(), EeErrorCode.EXEENGINE_GDATA_FAIL.getDescription(), e.getCause());
        } finally {
            server.close();
        }
    }

    @Override
    public OperateResult installIngress(InstallerConfig installConfig, ClusterInstanceDetailInfo cidi,
                                        TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo) {
        try {
            OperateResult operateResult = IngressInstaller.installIngress(installConfig, cidi, belongsTenantInfo, ingressInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + cidi.getName(), ApplicationStatusEnum.IN_INSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult uninstallIngress(InstallerConfig installConfig, ClusterInstanceDetailInfo cidi,
                                          TenantInstanceDetailInfo belongsTenantInfo, IngressInfo ingressInfo) {
        try {
            OperateResult operateResult = IngressInstaller.uninstallIngress(installConfig, cidi, belongsTenantInfo, ingressInfo);
            instanceStatusMap.put(INSTANCE_SUFFIX_CLUSTER + cidi.getName(), ApplicationStatusEnum.IN_UNINSTALL.code);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    /**
     * 健康检查
     * @param targetIp 被检查的服务器IP
     * @return
     */
    /**
     * 健康检查
     *
     * @param targetIp 被检查的服务器IP
     * @return
     */
    @Override
    public boolean health2machine(InstallerConfig installerConfig, String targetIp) {
        try {
            String url = installerConfig.getSelectJenkinsConfig().getJenkinsUrl();
            String userName = installerConfig.getSelectJenkinsConfig().getJekinsUserName();
            String passwd = installerConfig.getSelectJenkinsConfig().getJeninsPassword();
            HealthCheck healthCheck = new HealthCheck(url,
                    userName, passwd);
            return healthCheck.health2machine(targetIp);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            log.error(LogProperty.LOGTYPE_DETAIL, "-1", e, e.getMessage());
        }
        return false;
    }

    /**
     * 更新
     *
     * @param targetIp
     * @param mapIpHostName
     * @return
     */
    @Override
    public boolean updateHost2machine(InstallerConfig installerConfig, String targetIp, Map<String, String> mapIpHostName,
                                      String dockerImageReposity, String ntpServer) {
        try {
            ResourcePrepare resourcePrepare = new ResourcePrepare(installerConfig, targetIp, mapIpHostName,
                    dockerImageReposity, ntpServer);
            resourcePrepare.install();
            return true;
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_DETAIL, "-1", e, e.getMessage());
        }
        return false;
    }

    /**
     * @param installConfig
     * @param clusterInstanceDetailInfo
     * @param paramMaps
     * @param
     * @param belongsTenantInfo
     * @return OperateResult
     * @Function: installZone
     * @Description: 安装集群分区
     */
    @Override
    public OperateResult installZone(InstallerConfig installConfig,
                                     ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                     Map<String, Object> paramMaps,
                                     CluZoneVo cluZoneVo) {
        try {
            OperateResult operateResult = ClusterInstaller.installZone(installConfig, clusterInstanceDetailInfo, paramMaps, cluZoneVo);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public OperateResult uninstallZone(InstallerConfig installConfig,
                                       ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                       Map<String, Object> paramMaps,
                                       CluZoneVo cluZoneVo) {
        try {
            OperateResult operateResult = ClusterInstaller.unInstallZone(installConfig, clusterInstanceDetailInfo, paramMaps, cluZoneVo);
            return operateResult;
        } catch (ApplicationException e) {
            log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
            return null;
        }
    }

    @Override
    public ProgressDetailResult getZoneExecuteDescribe(
            OperateResult operateResult) {

        // TODO Auto-generated method stub
        return null;

    }

    @Override
    public Map<String, String> getZoneExecuteResult(OperateResult operateResult) {

        // TODO Auto-generated method stub
        return null;

    }

    @Override
    public OperateResult updateZoneQuota(InstallerConfig installConfig,
                                         ClusterInstanceDetailInfo clusterInstanceDetailInfo,
                                         Map<String, Object> paramMaps,
                                         CluZoneVo cluZoneVo) {
    	  try {
              OperateResult operateResult = ClusterInstaller.updateZoneQuota(installConfig, clusterInstanceDetailInfo, paramMaps, cluZoneVo);
              return operateResult;
          } catch (ApplicationException e) {
              log.error(LogProperty.LOGTYPE_DETAIL, e.getError().getCode(), e.getThrowable(), e.getError().getDescription());
              return null;
          }

    }

}
