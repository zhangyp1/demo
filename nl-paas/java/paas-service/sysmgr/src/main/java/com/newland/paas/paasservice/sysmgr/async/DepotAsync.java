package com.newland.paas.paasservice.sysmgr.async;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.newland.paas.common.util.HarborUtil;
import com.newland.paas.common.util.SFTPUtil;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantOperateMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantOperate;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysDepotBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantDepotBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.SysConstant;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @program: paas-all
 * @description: 仓库操作
 * @author: Frown
 * @create: 2018-07-25 09:08
 **/
@Component
public class DepotAsync {
    private static final Log LOG = LogFactory.getLogger(DepotAsync.class);

    @Autowired
    private SysTenantOperateMapper sysTenantOperateMapper;

    @Value("${depot.harbor.username}")
    private String harborUsername;
    @Value("${depot.harbor.password}")
    private String harborPassword;
    @Value("${depot.ftp.username}")
    private String ftpUsername;
    @Value("${depot.ftp.password}")
    private String ftpPassword;
    @Value("${resource.ast.homePath}")
    private String astHomePath;

    private static final Long PARENTID = -1L;

    private static final Integer SYSTENANTOPERATECOUNT = 2;

    private static final Integer PORT = 22;

    private static final Integer DEPOTPORT = 5000;


    /**
     * 初始化仓库环境
     *
     * @param sysTenantDepotBo
     * @return
     * @throws Exception
     */
    @Async("mySimpleAsync")
    public Future<Boolean> initDepotEnv(SysTenantDepotBo sysTenantDepotBo) throws Exception {
        Long tenantOpId = sysTenantDepotBo.getTenantOpId();
        String depotType = sysTenantDepotBo.getDepotType();
        Set<SysDepotBo> depotSet = sysTenantDepotBo.getDepotSet();

        // 设置任务状态为运行
        this.refeshTenantStatus(tenantOpId, SysMgrConstants.TASK_RUN);

        if (Objects.equals(SysMgrConstants.DEPOT_AST, depotType)) {
            LOG.info(LogProperty.LOGCONFIG_THREAD_NAME,
                    MessageFormat.format("开始创建资产仓库，仓库参数：{0}",
                            JSONObject.toJSONString(sysTenantDepotBo)));
            this.initDepot(depotSet, depotType);
        } else {
            LOG.info(LogProperty.LOGCONFIG_THREAD_NAME,
                    MessageFormat.format("开始创建镜像仓库，仓库参数：{0}",
                            JSONObject.toJSONString(sysTenantDepotBo)));
            this.initDepot(depotSet, depotType);
        }
        return new AsyncResult<>(true);
    }

    /**
     * 初始化仓库
     * @param depotSet
     * @param depotType
     * @throws Exception
     */
    public void initDepot(Set<SysDepotBo> depotSet, String depotType) throws Exception {
        if (Objects.equals(SysMgrConstants.DEPOT_AST, depotType)) {
            // 创建资产仓库
            for (SysDepotBo sysDepotBo : depotSet) {
                createAstDepot(sysDepotBo);
            }
            LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "创建资产仓库完成");
        } else {
            // 创建镜像仓库
            for (SysDepotBo sysDepotBo : depotSet) {
                createHarborDepot(sysDepotBo);
            }
            LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "创建镜像仓库完成");
        }
    }

    /**
     * 创建harbor仓库
     * @param sysDepotBo
     * @throws Exception
     */
    private void createHarborDepot(SysDepotBo sysDepotBo) throws Exception {
        String host;
        int port;
        HarborUtil harborUtil;
        String hostIp = sysDepotBo.getIpPort();
        if (hostIp.contains(":")) {
            host = hostIp.split(":")[0];
            port = Integer.parseInt(hostIp.split(":")[1]);
        } else {
            host = hostIp;
            port = DEPOTPORT;
        }
        try {
            // 创建用户
            LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "创建镜像仓库用户：" + sysDepotBo.getUsername());
            harborUtil = new HarborUtil(this.harborUsername, this.harborPassword, host, port);
            harborUtil.createUser(sysDepotBo.getUsername(), sysDepotBo.getPassword());
            // 创建项目
            LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "创建镜像仓库项目：" + sysDepotBo.getSubPath());
            harborUtil = new HarborUtil(sysDepotBo.getUsername(), sysDepotBo.getPassword(), host, port);
            harborUtil.createProject(sysDepotBo.getSubPath(),
                    Objects.equals(sysDepotBo.getType(), SysMgrConstants.DEPOT_PUB));
            // 原生镜像项目
            String rawProject = sysDepotBo.getSubPath() + SysConstant.RAW_PROJECT_SUFFIX;
            LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "创建原生镜像仓库项目：" + rawProject);
            harborUtil.createProject(rawProject,
                    Objects.equals(sysDepotBo.getType(), SysMgrConstants.DEPOT_PUB));
        } catch (Exception e) {
            throw new Exception("创建镜像仓库异常:" + e.getMessage());
        }
    }

    /**
     * 创建资产仓库
     * @param sysDepotBo
     * @throws JSchException
     */
    private void createAstDepot(SysDepotBo sysDepotBo) throws JSchException {
        String host;
        int port;
        SFTPUtil sFTPUtil;
        String hostIp = sysDepotBo.getIpPort();
        if (hostIp.contains(":")) {
            host = hostIp.split(":")[0];
            port = Integer.parseInt(hostIp.split(":")[1]);
        } else {
            host = hostIp;
            port = PORT;
        }
        sFTPUtil = new SFTPUtil(this.ftpUsername, this.ftpPassword, host, port);
        try {
            sFTPUtil.login();
            sFTPUtil.createRootDir(astHomePath + sysDepotBo.getSubPath());
        } catch (JSchException | SftpException e) {
            throw new JSchException("创建资产仓库异常:" + e.getMessage());
        } finally {
            sFTPUtil.logout();
        }
    }

    /**
     * 刷新租户状态
     * @param operateId
     * @param status
     */
    public void refeshTenantStatus(Long operateId, String status) {
        // 当前任务
        SysTenantOperate operate = new SysTenantOperate();
        operate.setId(operateId);
        operate.setStatus(status);
        sysTenantOperateMapper.updateByPrimaryKeySelective(operate);

        // 上级任务
        SysTenantOperate curOperate = sysTenantOperateMapper.selectByPrimaryKey(operateId);
        if (!Objects.equals(PARENTID, curOperate.getParentId())) {
            if (Objects.equals(SysMgrConstants.TASK_SUCCESS, status)) {
                operate = new SysTenantOperate();
                operate.setParentId(curOperate.getParentId());
                operate.setStatus(SysMgrConstants.TASK_SUCCESS);
                int num = sysTenantOperateMapper.countBySelective(operate);
                if (num == SYSTENANTOPERATECOUNT) {
                    operate = new SysTenantOperate();
                    operate.setId(curOperate.getParentId());
                    operate.setStatus(SysMgrConstants.TASK_SUCCESS);
                    sysTenantOperateMapper.updateByPrimaryKeySelective(operate);
                }
            } else {
                operate = new SysTenantOperate();
                operate.setId(curOperate.getParentId());
                operate.setStatus(status);
                sysTenantOperateMapper.updateByPrimaryKeySelective(operate);
            }
        }
    }

    /**
     * 清除仓库环境
     * @param sysTenantDepotBo
     * @return
     * @throws Exception
     */
    @Async("mySimpleAsync")
    public Future<Boolean> clearDepotEnv(SysTenantDepotBo sysTenantDepotBo) throws Exception {
        LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, MessageFormat.format("删除仓库环境，仓库参数：{0}",
                JSONObject.toJSONString(sysTenantDepotBo)));
        String depotType = sysTenantDepotBo.getDepotType();
        Set<SysDepotBo> depotSet = sysTenantDepotBo.getDepotSet();

        if (Objects.equals(SysMgrConstants.DEPOT_AST, depotType)) {
            // 资产仓库
            SFTPUtil sFTPUtil = null;
            String host = null;
            int port = PORT;
            for (SysDepotBo sysDepotBo : depotSet) {
                LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "删除资产仓库目录：" + sysDepotBo.getSubPath());
                deleteAstDepot(sysDepotBo);
            }
        } else {
            // 镜像仓库
            HarborUtil harborUtil = null;
            String host = null;
            int port;
            for (SysDepotBo sysDepotBo : depotSet) {
                deleteHarborDepot(sysDepotBo);
            }
        }
        LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "删除仓库环境完成");
        return new AsyncResult<>(true);
    }

    /**
     * 删除harbor仓库
     * @param sysDepotBo
     * @throws Exception
     */
    private void deleteHarborDepot(SysDepotBo sysDepotBo) throws Exception {
        String host;
        int port;
        HarborUtil harborUtil;
        String hostIp = sysDepotBo.getIpPort();
        if (hostIp.contains(":")) {
            host = hostIp.split(":")[0];
            port = Integer.parseInt(hostIp.split(":")[1]);
        } else {
            host = hostIp;
            port = DEPOTPORT;
        }
        // 删除项目
        LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "删除镜像仓库项目：" + sysDepotBo.getSubPath());
        harborUtil = new HarborUtil(this.harborUsername, this.harborPassword, host, port);
        harborUtil.deleteProject(sysDepotBo.getSubPath());
        // 原生镜像项目
        String rawProject = sysDepotBo.getSubPath() + SysConstant.RAW_PROJECT_SUFFIX;
        LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "删除原生镜像仓库项目：" + rawProject);
        harborUtil.deleteProject(rawProject);
        // 删除用户
        LOG.info(LogProperty.LOGCONFIG_THREAD_NAME, "删除镜像仓库用户：" + sysDepotBo.getUsername());
        harborUtil.deleteUser(sysDepotBo.getUsername());
    }

    /**
     * 刪除资产仓库
     * @param sysDepotBo
     * @throws JSchException
     */
    private void deleteAstDepot(SysDepotBo sysDepotBo) throws JSchException {
        String host;
        int port;
        SFTPUtil sFTPUtil;
        String hostIp = sysDepotBo.getIpPort();
        if (hostIp.contains(":")) {
            host = hostIp.split(":")[0];
            port = Integer.parseInt(hostIp.split(":")[1]);
        } else {
            host = hostIp;
            port = PORT;
        }
        sFTPUtil = new SFTPUtil(this.ftpUsername, this.ftpPassword, host, port);
        try {
            sFTPUtil.login();
            String dir = astHomePath + sysDepotBo.getSubPath();
            if (sFTPUtil.isDirExist(dir)) {
                sFTPUtil.deleteDir(dir);
            }
        } catch (JSchException | SftpException e) {
            throw new JSchException("删除资产仓库目录异常：" + e.getMessage());
        } finally {
            sFTPUtil.logout();
        }
    }

}
