package com.newland.paas.paasservice.activitiflow.controller;

import com.newland.paas.advice.audit.AuditObject;
import com.newland.paas.advice.audit.AuditOperate;
import com.newland.paas.common.util.FileUtil;
import com.newland.paas.common.util.SFTPUtil;
import com.newland.paas.paasservice.activitiflow.constant.ProcessEngineConstant;
import com.newland.paas.sbcommon.codeid.CodeIdUtil;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程部署管理
 *
 * @author WRP
 * @since 2018/7/15
 */
@RestController
@RequestMapping("v1/deploy")
@Validated
@AuditObject("流程部署")
public class DeployController {

    private static final String STRINGPROCESS = "process:";
    private static final String STRINGBPMN = ".bpmn";
    private static final String STRINGPROCESSES = "processes/";
    private static final String FILE_LINK = "/";

    /**
     * 部署流程 数组参数-内容为bpmn的名称，例如需要部署CreateTenant.bpmn，传{"params": ["CreateTenant"]}
     *
     * @param processesList 流程
     * @return id列表
     * @throws ApplicationException 异常
     */
    @AuditOperate(value = "deployBpmn", name = "bpmn部署")
    @PostMapping(value = "/bpmn", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<String> deployBpmn(
            @Validated @RequestBody BasicRequestContentVo<List<String>> processesList) throws ApplicationException {
        if (processesList.getParams() == null) {
            throw new ApplicationException(new PaasError("20008401", "流程信息不能为空！"));
        }
        List<String> results = new ArrayList<>();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        for (String process : processesList.getParams()) {
            Deployment deployment = processEngine.getRepositoryService().createDeployment()
                    .addClasspathResource(STRINGPROCESSES + process + STRINGBPMN)
                    .deploy();
            results.add(STRINGPROCESS + deployment.getId());
        }
        return results;
    }

    /**
     * 部署流程 单个文件部署
     *
     * @param fileName 文件名
     * @return 返回
     * @throws ApplicationException 异常
     */
    @AuditOperate(value = "deploySingleBpmn", name = "bpmn单个部署")
    @GetMapping(value = "/bpmn/{fileName}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo getDeployBpmn(@PathVariable(value = "fileName") String fileName) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .addClasspathResource(STRINGPROCESSES + fileName + STRINGBPMN)
                .deploy();
        return new BasicResponseContentVo(deployment.getId());
    }

    /**
     * 部署流程 所有流程
     *
     * @return 返回
     * @throws ApplicationException 异常
     */
    @AuditOperate(value = "deployAllBpmn", name = "bpmn所有部署")
    @GetMapping(value = "/bpmn-all", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<String> deployBpmnAll() {
        List<String> allBpmn = new ArrayList<>();
        allBpmn.add("ApplyResource");
        allBpmn.add("AstImportMgr");
        allBpmn.add("AstOfferMgr");
        allBpmn.add("CluInstall");
        allBpmn.add("CluScalein");
        allBpmn.add("CluScaleout");
        allBpmn.add("CluUninstall");
        allBpmn.add("SvrReleaseOrder");
        allBpmn.add("SvrSubscribeOrder");
        allBpmn.add("TenantCreate");
        allBpmn.add("TenantJoin");
        allBpmn.add("ProcessCreatePlan");
        List<String> results = new ArrayList<>();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        for (String process : allBpmn) {
            Deployment deployment = processEngine.getRepositoryService().createDeployment()
                    .addClasspathResource(STRINGPROCESSES + process + STRINGBPMN)
                    .deploy();
            results.add(STRINGPROCESS + deployment.getId());
        }
        return results;
    }

    /**
     * deployZip
     *
     * @param processesList 流程
     * @return 返回
     * @throws ApplicationException 异常
     */
    @AuditOperate(value = "deployZip", name = "zip包部署")
    @PostMapping(value = "/zip", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<String> deployZip(
            @Validated @RequestBody BasicRequestContentVo<List<String>> processesList) throws ApplicationException {
        if (processesList.getParams() == null) {
            throw new ApplicationException(new PaasError("20008401", "流程信息不能为空！"));
        }
        List<String> results = new ArrayList<>();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        for (String process : processesList.getParams()) {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(STRINGPROCESSES + process + ".zip");
            ZipInputStream zipInputStream = new ZipInputStream(in);
            Deployment deployment = processEngine.getRepositoryService().createDeployment()
                    .addZipInputStream(zipInputStream)
                    .deploy();
            results.add(STRINGPROCESS + deployment.getId());
        }
        return results;
    }

    /**
     * 加载动态流程图并部署(生产环境)
     *
     * @param params 参数
     * @return 返回
     * @throws Exception 异常
     */
    @AuditOperate(value = "uploadDynProcess", name = "加载动态流程")
    @PostMapping(value = "/uploadDynProcess", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo uploadDynProcess(@RequestBody Map<String, Object> params)
            throws ApplicationException {
        boolean result = true;
        try {
            Map<String, Object> resultMap = uploadDynProcessShare(params);
            //启动执行主流程将应用子流程加载到主流程
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            String processDefinitionKey = String.valueOf(params.get("processDefinitionKey"));
            String businessKey = String.valueOf(params.get("businessKey"));
            Map<String, Object> variables = new HashMap();
            variables.put(ProcessEngineConstant.PLANCODE, params.get(ProcessEngineConstant.PLANCODE));
            variables.put(ProcessEngineConstant.PLANID, params.get(ProcessEngineConstant.PLANID));
            variables.put(ProcessEngineConstant.GROUPID, ProcessEngineConstant.GROUP + ProcessEngineConstant.GROUPID);
            variables.put(ProcessEngineConstant.SUBPROCESSID, resultMap.get(ProcessEngineConstant.SUBPROCESSID));
            variables.put("skip", false);
            variables.put("exceptionskip", true);
            variables.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
            processEngine.getRuntimeService().
                    startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        } catch (ApplicationException e) {
            throw new ApplicationException(new PaasError("ActDepDyn04", "加载动态流程失败：" + e.getMessage()));
        }
        return new BasicResponseContentVo(result);

    }

    /**
     * 加载动态流程图并部署(开发测试环境)
     *
     * @param params 参数
     * @return 返回
     * @throws ApplicationException 异常
     */
    @AuditOperate(value = "uploadDynProcessLocal", name = "加载动态流程")
    @PostMapping(value = "/uploadDynProcessLocal", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo uploadDynProcessLocal(@RequestBody Map<String, Object> params)
            throws ApplicationException {
        Map<String, Object> resultMap = new HashMap();
        boolean result = true;
        try {
            Map<String, Object> resultShareMap = uploadDynProcessShare(params);
            resultMap.put(ProcessEngineConstant.SUBPROCESSID, resultShareMap.get(ProcessEngineConstant.SUBPROCESSID));
            result = (boolean) resultShareMap.get(ProcessEngineConstant.RESULT);
        } catch (ApplicationException e) {
            result = false;
            throw new ApplicationException(new PaasError("ActDepLocal04", "加载动态流程失败：" + e.getMessage()));
        } finally {
            //返回部署结果
            resultMap.put("result", result);
        }
        return new BasicResponseContentVo(resultMap);
    }

    /**
     * uploadDynProcessShare
     *
     * @param params 参数
     * @return 返回
     * @throws Exception 异常
     */
    protected Map<String, Object> uploadDynProcessShare(Map<String, Object> params) throws ApplicationException {
        Map<String, Object> returnMap = new HashMap();
        boolean result = false;
        String uploadUsername = String.valueOf(params.get("uploadUsername")); //获取ftp配置信息
        String uploadPassword = String.valueOf(params.get("uploadPassword"));
        String uploadHost = String.valueOf(params.get("uploadHost"));
        String uploadPort = String.valueOf(params.get("uploadPort"));
        String uploadHomePath = String.valueOf(params.get("uploadHomePath")); //根路径
        String fileRoute = String.valueOf(params.get("fileRoute")); //文件路径
        String planCode = String.valueOf(params.get("planCode"));
        try {
            SFTPUtil sftp = new SFTPUtil(uploadUsername, uploadPassword, uploadHost, Integer.parseInt(uploadPort));
            sftp.login();
            String localPath = FileUtil.getRootPath() + STRINGPROCESSES + fileRoute; //下载流程文件到本地process路径
            File file = new File(localPath); //从ftp获取bpmn文件
            if (!file.exists()) { //如果文件夹不存在
                file.mkdirs(); //创建文件夹
            }
            sftp.downloadDirsAndFiles(uploadHomePath + fileRoute, localPath);
            sftp.logout();
            File dir = new File(localPath);
            File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
            String mainProcessName = "";
            if (files != null) {
                //循环部署流程
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();
                    //主流程
                    if (fileName.endsWith("bpmn") && fileName.contains(planCode)
                            && fileName.contains("ProcessDevelopment")) {
                        mainProcessName = fileName;
                    } else if (fileName.endsWith("bpmn")) {
                        deployProcess(fileRoute, fileName); //获取子流程遍历每个节点替换groupCode为groupId
                    }
                }
                if (!"".equals(mainProcessName)) {
                    String subProcessId = deployProcess(fileRoute, mainProcessName);
                    returnMap.put("subProcessId", subProcessId);
                    result = true;
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(new PaasError("ActDep04", "部署失败：" + e.getMessage()));
        }
        returnMap.put("result", result);
        return returnMap;
    }


    /**
     * 部署流程
     * @param fileRoute 文件路径
     * @param processName 流程名称
     * @return 子流程ID
     * @throws ApplicationException 异常
     */
    private String deployProcess(String fileRoute, String processName) throws ApplicationException, XMLStreamException {
        String subProcessId = "";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        File processNameFile = new File(FileUtil.getRootPath()
                + STRINGPROCESSES + fileRoute + FILE_LINK + processName);
        BpmnXMLConverter converter = new BpmnXMLConverter();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try (InputStream inputStream = new FileInputStream(processNameFile)) {
            reader = factory.createXMLStreamReader(inputStream);
            //将xml文件转换成BpmnModel
            BpmnModel bpmnModel = converter.convertToBpmnModel(reader);
            List<Process> processList = bpmnModel.getProcesses();
            for (Process curprocess : processList) {
                subProcessId = curprocess.getId();
                List<UserTask> userTaskList = curprocess.findFlowElementsOfType(UserTask.class);
                for (UserTask userTask : userTaskList) {
                    List<String> groupCodeList = userTask.getCandidateGroups();
                    List<String> groupIdList = new ArrayList<>();
                    //调用code转id
                    for (String groupCode : groupCodeList) {
                        String groupId = CodeIdUtil.getByCode(CodeIdUtil.SYS_GROUP, groupCode);
                        groupIdList.add("group_" + groupId);
                    }
                    //重新设置groupId
                    userTask.setCandidateGroups(groupIdList);
                }
            }
            repositoryService.createDeployment().addBpmnModel(processName, bpmnModel).deploy();
        } catch (FileNotFoundException e) {
            throw new ApplicationException(new PaasError("ActDep01", e.getMessage()));
        } catch (IOException e) {
            throw new ApplicationException(new PaasError("ActDep02", e.getMessage()));
        } catch (XMLStreamException e) {
            throw new ApplicationException(new PaasError("ActDep03", e.getMessage()));
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return subProcessId;
    }

}
