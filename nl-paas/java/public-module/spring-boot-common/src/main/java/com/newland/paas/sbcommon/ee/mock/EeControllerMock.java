package com.newland.paas.sbcommon.ee.mock;

import com.alibaba.fastjson.JSON;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobCallbackResultsQueryOut;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobCallbackVo;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobDetl;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobLogQueryOut;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobQueryOut;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobSubmitOut;
import com.newland.paas.paasservice.controllerapi.ee.vo.JobSubmitVo;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesItemProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.utils.SpringContextUtil;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mock com.newland.paas.paasservice.executeenginesvr.controller.EeController
 *
 * @author WRP
 * @since 2018/12/7
 */
public final class EeControllerMock {

    private static final String UNDERLINE = "_";

    private static final Map<String, Map<String, Object>> CMDS = new HashMap<>();

    private static final Map<String, JobQueryOut> JOB_QUERY_OUTS = new HashMap<>();

    private static final Map<String, List<String>> JOB_LOGS = new HashMap<>();

    private static final Map<String, JobCallbackResultsQueryOut> JOB_CALLBACK_RESULTS = new HashMap<>();

    static {
        // 免密
        Map<String, Object> resAvoidPassword = new HashMap<>();
        CMDS.put("res_avoidPassword", resAvoidPassword);

        // 资源初始化
        Map<String, Object> resResInit = new HashMap<>();
        CMDS.put("res_resInit", resResInit);

        // k8s集群部署
        Map<String, Object> k8sCluPfDeploy = new HashMap<>();
        CMDS.put("k8sClu_pf_deploy", k8sCluPfDeploy);

        // k8s集群卸载
        Map<String, Object> k8sCluPfUninstall = new HashMap<>();
        CMDS.put("k8sClu_pf_uninstall", k8sCluPfUninstall);

        // k8s集群扩容
        Map<String, Object> k8sCluPfScaleOut = new HashMap<>();
        CMDS.put("k8sClu_pf_scale_out", k8sCluPfScaleOut);

        // k8s集群缩容
        Map<String, Object> k8sCluPfScaleIn = new HashMap<>();
        CMDS.put("k8sClu_pf_scale_in", k8sCluPfScaleIn);

        // k8s集群修改节点标签
        Map<String, Object> k8sCluPfUpdtLbl = new HashMap<>();
        CMDS.put("k8sClu_pf_updt_lbl", k8sCluPfUpdtLbl);

        // k8s集群分区部署
        Map<String, Object> k8sZonePfDeploy = new HashMap<>();
        CMDS.put("k8sClu_pf_deploy", k8sZonePfDeploy);

        // k8s集群分区卸载
        Map<String, Object> k8sZonePfUninstall = new HashMap<>();
        CMDS.put("k8sClu_pf_uninstall", k8sZonePfUninstall);

        // k8s集群分区修改配额
        Map<String, Object> k8sZonePfModifyQuota = new HashMap<>();
        CMDS.put("k8sClu_pf_modify_quota", k8sZonePfModifyQuota);

        // ingress部署
        Map<String, Object> ingressPfDeploy = new HashMap<>();
        CMDS.put("ingress_pf_deploy", ingressPfDeploy);

        // ingress卸载
        Map<String, Object> ingressPfUninstall = new HashMap<>();
        CMDS.put("ingress_pf_uninstall", ingressPfUninstall);

        // ingress注册
        Map<String, Object> ingressPfRegister = new HashMap<>();
        CMDS.put("ingress_pf_register", ingressPfRegister);

        // 应用启动
        Map<String, Object> appPfStart = new HashMap<>();
        CMDS.put("app_pf_start", appPfStart);

        // 应用停止
        Map<String, Object> appPfStop = new HashMap<>();
        CMDS.put("app_pf_stop", appPfStop);

        // 应用缩容
        Map<String, Object> appPfScaleIn = new HashMap<>();
        CMDS.put("app_pf_scale_in", appPfScaleIn);

        // 应用扩容
        Map<String, Object> appPfScaleOut = new HashMap<>();
        CMDS.put("app_pf_scale_out", appPfScaleOut);

        // 应用自定义
        Map<String, Object> appCustom = new HashMap<>();
        CMDS.put("app_custom", appCustom);

        // 服务启动
        Map<String, Object> svrPfStart = new HashMap<>();
        CMDS.put("svr_pf_start", svrPfStart);

        // 服务停止
        Map<String, Object> svrPfStop = new HashMap<>();
        CMDS.put("svr_pf_stop", svrPfStop);

        // 服务缩容
        Map<String, Object> svrPfScaleIn = new HashMap<>();
        CMDS.put("svr_pf_scale_in", svrPfScaleIn);

        // 服务扩容
        Map<String, Object> svrPfScaleOut = new HashMap<>();
        CMDS.put("svr_pf_scale_out", svrPfScaleOut);

        // 服务自定义
        Map<String, Object> svrCustom = new HashMap<>();
        CMDS.put("svr_custom", svrCustom);

    }


    private EeControllerMock() {
    }

    /**
     * 请求对应的处理方法
     *
     * @param interfaceUrl
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T chooseInterface(String interfaceUrl, Object params) {
        if (interfaceUrl.indexOf("/") == 0) {
            interfaceUrl = interfaceUrl.substring(1);
        }
        if (interfaceUrl.lastIndexOf("/") == interfaceUrl.length() - 1) {
            interfaceUrl = interfaceUrl.substring(0, interfaceUrl.length() - 1);
        }
        String[] interfaceUrls = interfaceUrl.split("\\/");
        if (!"v1".equals(interfaceUrls[0]) || !"job".equals(interfaceUrls[1])) {
            throw new SystemException(new PaasError("404", "无此接口：" + interfaceUrl));
        }
        if ("submit".equals(interfaceUrls[2])) {
            return (T) submitJob((BasicRequestContentVo<JobSubmitVo>) params);
        } else if ("task2master".equals(interfaceUrls[2])) {
            String jobDetl = interfaceUrls[3];
            return (T) revMasterTask(jobDetl);
        } else if ("query".equals(interfaceUrls[2])) {
            if ("log".equals(interfaceUrls[3])) {
                String execJobDetlId = interfaceUrls[4];
                Long rownum = Long.parseLong(interfaceUrls[5]);
                return (T) queryLogResult(execJobDetlId, rownum);
            } else if ("callback".equals(interfaceUrls[3])) {
                String jobId = interfaceUrls[4];
                return (T) callBackJobResult(jobId);
            } else {
                String jobId = interfaceUrls[3];
                return (T) queryJobResult(jobId);
            }
        }
        throw new SystemException(new PaasError("404", "无此接口：" + interfaceUrl));
    }

    /**
     * 任务提交
     *
     * @param jobSubmitVo 作业ID
     * @return JobSubmitOut
     */
    private static BasicResponseContentVo<JobSubmitOut> submitJob(BasicRequestContentVo<JobSubmitVo> jobSubmitVo) {
        JobSubmitVo jobSubmit = jobSubmitVo.getParams();
        String jobId = jobSubmit.getCmdCatg() + UNDERLINE + jobSubmit.getCmdCode() + UNDERLINE + System.currentTimeMillis();
        jobSubmit.setJobId(jobId);
        JobSubmitOut jobSubmitOut = new JobSubmitOut();
        jobSubmitOut.setJobId(jobId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    // JobQueryOut mock
                    JobQueryOut jobQueryOut = new JobQueryOut();
                    jobQueryOut.setCmdCatg(jobSubmit.getCmdCatg());
                    jobQueryOut.setCmdCode(jobSubmit.getCmdCode());
                    jobQueryOut.setExecJobDesc(jobId);
                    jobQueryOut.setExecJobStatus(JobStatusType.RUN.value);
                    jobQueryOut.setExecuteStartTime(new Date());
                    List<JobDetl> execJobDetls = new ArrayList<>();
                    // JobDetl mock
                    JobDetl jobDetl = new JobDetl();
                    jobDetl.setExecJobDetlId(jobId + UNDERLINE + "1");
                    jobDetl.setExecOrder(1L);
                    jobDetl.setExecScriptName(jobSubmit.getCmdCode());
                    jobDetl.setRunStatus(JobDetlStatusType.SUCC.value);
                    jobDetl.setExecStartTime(new Date());
                    jobDetl.setExecEndTime(new Date());
                    // log mock
                    List<String> logs = new ArrayList<>();
                    logs.add(jobDetl.getExecJobDetlId() + UNDERLINE + "执行日志" + UNDERLINE + 1);
                    logs.add(jobDetl.getExecJobDetlId() + UNDERLINE + "执行日志" + UNDERLINE + 2);
                    logs.add(jobDetl.getExecJobDetlId() + UNDERLINE + "执行日志" + UNDERLINE + 3);
                    JOB_LOGS.put(jobDetl.getExecJobDetlId(), logs);

                    execJobDetls.add(jobDetl);
                    jobQueryOut.setExecJobDetls(execJobDetls);
                    JOB_QUERY_OUTS.put(jobId, jobQueryOut);
                    // 回调
                    MicroservicesItemProperties microservicesItemProperties = getMicroservicesItemProperties(jobSubmit.getServiceId());
                    JobCallbackVo jobCallbackVo = new JobCallbackVo();
                    jobCallbackVo.setJobId(jobId);
                    jobCallbackVo.setObjId(jobSubmit.getObjId());
                    jobCallbackVo.setResult(1);
                    Map<String, Object> params = CMDS.get(jobSubmit.getCmdCatg() + UNDERLINE + jobSubmit.getCmdCode());
                    jobCallbackVo.setCallBackSvrName(jobSubmit.getCallBackSvrName());
                    jobCallbackVo.setResultData(params);

                    RestTemplateUtils restTemplateUtils = SpringContextUtil.getContext().getBean(RestTemplateUtils.class);
                    BasicResponseContentVo<HashMap<String, Object>> response = restTemplateUtils.postForEntity(microservicesItemProperties, jobSubmit.getCallBackUrl(),
                            jobCallbackVo, new ParameterizedTypeReference<BasicResponseContentVo<HashMap<String, Object>>>() {
                            });

                    // JobCallbackResultsQueryOut mock
                    JobCallbackResultsQueryOut jobCallbackResultsQueryOut = new JobCallbackResultsQueryOut();
                    jobCallbackResultsQueryOut.setCallBackUrl(jobSubmit.getCallBackUrl());
                    jobCallbackResultsQueryOut.setCallBackMsg(JSON.toJSONString(response));
                    jobCallbackResultsQueryOut.setCallBackStatus(CallBackStatusType.SUCCESS.value.toString());
                    JOB_CALLBACK_RESULTS.put(jobId, jobCallbackResultsQueryOut);

                    Thread.sleep(10000);
                    jobQueryOut = JOB_QUERY_OUTS.get(jobId);
                    jobQueryOut.setExecJobStatus(JobStatusType.SUCC.value);
                    jobDetl = new JobDetl();
                    jobDetl.setExecJobDetlId(jobId + UNDERLINE + "2");
                    jobDetl.setExecOrder(2L);
                    jobDetl.setExecScriptName(jobSubmit.getCmdCode());
                    jobDetl.setRunStatus(JobDetlStatusType.SUCC.value);
                    jobDetl.setExecStartTime(new Date());
                    jobDetl.setExecEndTime(new Date());

                    logs = new ArrayList<>();
                    logs.add(jobDetl.getExecJobDetlId() + UNDERLINE + "执行日志" + UNDERLINE + 1);
                    logs.add(jobDetl.getExecJobDetlId() + UNDERLINE + "执行日志" + UNDERLINE + 2);
                    logs.add(jobDetl.getExecJobDetlId() + UNDERLINE + "执行日志" + UNDERLINE + 3);
                    JOB_LOGS.put(jobDetl.getExecJobDetlId(), logs);

                    execJobDetls.add(jobDetl);
                    jobQueryOut.setExecJobDetls(execJobDetls);
                    JOB_QUERY_OUTS.put(jobId, jobQueryOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return new BasicResponseContentVo<>(jobSubmitOut);
    }

    /**
     * 接收master 主机远程任务，例如multiRun 多机运行中-集群中正在运行的主机执行,multiAll 多机全部-集群中所有主机
     *
     * @param jobDetl
     * @return
     */
    private static BasicResponseContentVo<JobSubmitOut> revMasterTask(String jobDetl) {
        JobSubmitOut jobDetlRemoteOut = new JobSubmitOut();
        jobDetlRemoteOut.setJobId(System.currentTimeMillis() + "");
        return new BasicResponseContentVo<>(jobDetlRemoteOut);
    }

    /**
     * 任务执行结果查询
     *
     * @param jobId
     * @return
     */
    private static BasicResponseContentVo<JobQueryOut> queryJobResult(String jobId) {
        return new BasicResponseContentVo<>(JOB_QUERY_OUTS.get(jobId));
    }

    /**
     * 执行日志查询
     *
     * @param execJobDetlId
     * @param rownum
     * @return
     */
    private static BasicResponseContentVo<JobLogQueryOut> queryLogResult(String execJobDetlId, Long rownum) {
        List<String> logs = JOB_LOGS.get(execJobDetlId);
        StringBuilder logContent = new StringBuilder();
        int i = 0;
        if (rownum != null && rownum.longValue() > 0) {
            i = rownum.intValue();
        }
        for (; i < logs.size(); i++) {
            logContent.append(logs.get(i)).append(";");
        }
        JobLogQueryOut jobLogQueryOut = new JobLogQueryOut();
        if (logs.size() == 3) {
            jobLogQueryOut.setRownum(-1);
        } else {
            jobLogQueryOut.setRownum(i);
        }
        jobLogQueryOut.setLogContent(logContent.toString());
        return new BasicResponseContentVo<>(jobLogQueryOut);
    }

    /**
     * 任务执行回调结果查询
     *
     * @param jobId
     * @return
     */
    private static BasicResponseContentVo<JobCallbackResultsQueryOut> callBackJobResult(String jobId) {
        JobCallbackResultsQueryOut jobCallbackResultsQueryOut = new JobCallbackResultsQueryOut();
        return new BasicResponseContentVo<>(jobCallbackResultsQueryOut);

    }


    /**
     * MicroservicesItemProperties mock
     */
    private static MicroservicesItemProperties getMicroservicesItemProperties(String serviceId) {
        switch (serviceId) {
            case "appmgr":
                return buildMicroservicesItemProperties("appmgr", "/paas/appmgr");
            case "astmgr":
                return buildMicroservicesItemProperties("astmgr", "/paas/astmgr");
            case "clumgr":
                return buildMicroservicesItemProperties("clumgr", "/paas/clumgr");
            case "resmgr":
                return buildMicroservicesItemProperties("resmgr", "/paas/resmgr");
            case "svrmgr":
                return buildMicroservicesItemProperties("svrmgr", "/paas/svrmgr");
            case "sysmgr":
                return buildMicroservicesItemProperties("sysmgr", "/paas/sysmgr");
            case "auth":
                return buildMicroservicesItemProperties("auth", "/paas/auth");
            case "activiti-flow":
                return buildMicroservicesItemProperties("activiti-flow", "/paas/activiti-flow");
            case "common":
                return buildMicroservicesItemProperties("sysmgr", "/paas/sysmgr");
            case "sysmgrRoot":
                return buildMicroservicesItemProperties("sysmgr", "/paas");
            case "loggateway":
                return buildMicroservicesItemProperties("loggateway", "/paas/loggateway");
            case "execengn":
                return buildMicroservicesItemProperties("execengn", "/paas/execengn");
            default:
                return null;
        }
    }

    /**
     * 构造服务信息
     *
     * @param serviceId
     * @param contentPath
     * @return
     */
    private static MicroservicesItemProperties buildMicroservicesItemProperties(String serviceId, String contentPath) {
        MicroservicesItemProperties properties = new MicroservicesItemProperties();
        properties.setServiceId(serviceId);
        properties.setContentPath(contentPath);
        return properties;
    }

}
