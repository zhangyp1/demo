package com.newland.paas.paasservice.sysmgr.async;

import com.alibaba.fastjson.JSONObject;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantOperateMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantOperate;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysDepotBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantDepotBo;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.common.SystemException;
import com.newland.paas.sbcommon.properties.MicroservicesProperties;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @program: paas-all
 * @description: 仓库辅助操作
 * @author: Frown
 * @create: 2018-09-11 16:59
 **/
@Component
public class DepotHelperAsync {
    private static final Log LOGGER = LogFactory.getLogger(DepotHelperAsync.class);

    @Autowired
    private SysTenantOperateMapper sysTenantOperateMapper;
    @Autowired
    private DepotAsync depotAsync;
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private MicroservicesProperties microservicesProperties;
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Value("${depot.harbor.host}")
    private String harborHost;
    @Value("${resource.ast.host}")
    private String astHost;
    @Value("${depot.timeout}")
    private Integer depotTimeout;
    private static final Long PARENTID = -1L;
    private static final String STATUS = "status";
    private static final int TWO = 2;
    private static final int THREE = 3;

    /**
     * 初始化租户仓库
     *
     * @param sysTenantInfo
     * @throws SystemException
     * @throws ApplicationException
     */
    @Async("mySimpleAsync")
    public void initSysTenantDepot(SysTenant sysTenantInfo, String token) {

        LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("准备创建租户仓库，租户信息：{0}", JSONObject.toJSONString(sysTenantInfo)));

        // 创建租户操作步骤
        this.createSysTenantOperate(sysTenantInfo);

        // 获取机房数据
        List<Map> computerList = this.getComputerList(token);
        LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("查询机房数据，返回：{0}", JSONObject.toJSON(computerList)));

        if (computerList == null || computerList.isEmpty()) {
            SysTenantOperate operateQuery = new SysTenantOperate();
            operateQuery.setTenantId(sysTenantInfo.getId());
            List<SysTenantOperate> operateList = sysTenantOperateMapper.selectBySelective(operateQuery);
            // 修改仓库操作状态为成功
            SysTenantOperate mainOperate = operateList.get(1);
            SysTenantOperate operate = new SysTenantOperate();
            operate.setId(mainOperate.getId());
            operate.setStatus(SysMgrConstants.TASK_SUCCESS);
            sysTenantOperateMapper.updateByPrimaryKeySelective(operate);
            return;
        }

        // 创建租户仓库路径
        this.createTenantDepositoryPath(token, sysTenantInfo, computerList);
    }

    /**
     * 创建租户操作步骤
     *
     * @param sysTenantInfo
     * @return
     */
    private void createSysTenantOperate(SysTenant sysTenantInfo) {
        // 创建租户操作数据初始化
        SysTenantOperate baseOperate =
                new SysTenantOperate(sysTenantInfo.getId(), "创建租户基本信息", SysMgrConstants.TASK_SUCCESS,
                        sysTenantInfo.getCreatorId(), PARENTID);
        sysTenantOperateMapper.insertSelective(baseOperate);
        SysTenantOperate depotOperate =
                new SysTenantOperate(sysTenantInfo.getId(), "创建租户仓库", SysMgrConstants.TASK_START,
                        sysTenantInfo.getCreatorId(), PARENTID);
        sysTenantOperateMapper.insertSelective(depotOperate);
        SysTenantOperate astOperate =
                new SysTenantOperate(sysTenantInfo.getId(), "创建租户资产仓库", SysMgrConstants.TASK_START,
                        sysTenantInfo.getCreatorId(), depotOperate.getId());
        sysTenantOperateMapper.insertSelective(astOperate);
        SysTenantOperate imgOperate =
                new SysTenantOperate(sysTenantInfo.getId(), "创建租户镜像仓库", SysMgrConstants.TASK_START,
                        sysTenantInfo.getCreatorId(), depotOperate.getId());
        sysTenantOperateMapper.insertSelective(imgOperate);
    }

    /**
     * 查询机房数据
     *
     * @param token
     * @return
     */
    private List<Map> getComputerList(String token) {
        // TODO：租户资产仓库、镜像仓库与各个机房中的仓库数据双向同步有问题，暂时使用单点仓库
        Map<String, String> map = new HashMap<>();
        map.put("assetSite", astHost);
        map.put("harborSite", harborHost);
        List<Map> computerList = new ArrayList<>();
        computerList.add(map);
        return computerList;

//        BasicResponseContentVo<List<Map>> response = null;
//        response = restTemplateUtils
//                .getForTokenEntity(microservicesProperties.getResmgr(),
//                        "/v1/computer-room/list", token, new HashMap<>(),
//                        new ParameterizedTypeReference<BasicResponseContentVo<List<Map>>>() {
//                        });
//
//        if (response != null && response.getContent() != null && !response.getContent().isEmpty()) {
//            return response.getContent();
//        }
//        return Collections.emptyList();
    }

    /**
     * 创建租户仓库路径
     *
     * @param token         token
     * @param sysTenantInfo 租户信息
     */
    private void createTenantDepositoryPath(String token, SysTenant sysTenantInfo, List<Map> computerList) {
        for (int i = 0; i < computerList.size(); i++) {
            // 单个仓库处理
            Map<String, Object> depotMap = this.getDepotSet(sysTenantInfo, computerList.subList(i, i + 1));
            LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                    MessageFormat.format("获取第{0}个机房的仓库数据，返回：{1}", i + 1, JSONObject.toJSON(depotMap)));
            SysTenantDepotBo astTenantDepot = (SysTenantDepotBo) depotMap.get("ast");
            SysTenantDepotBo imgTenantDepot = (SysTenantDepotBo) depotMap.get("img");

            try {
                // 创建租户资产产库
                this.createTenantDepot(astTenantDepot);
                // 创建租户镜像仓库
                this.createTenantDepot(imgTenantDepot);
            } catch (Exception e) {
                LOGGER.info(LogProperty.LOGTYPE_DETAIL, "创建租户仓库异常：" + e.getMessage());
                SysTenantOperate operateQuery = new SysTenantOperate();
                operateQuery.setTenantId(sysTenantInfo.getId());
                List<SysTenantOperate> operateList = sysTenantOperateMapper.selectBySelective(operateQuery);
                // 修改仓库操作状态为失败
                SysTenantOperate mainOperate = operateList.get(1);
                SysTenantOperate operate = new SysTenantOperate();
                operate.setId(mainOperate.getId());
                operate.setStatus(SysMgrConstants.TASK_FAILURE);
                sysTenantOperateMapper.updateByPrimaryKeySelective(operate);

                // 更新异常仓库对应的机房状态为：不可用
                //Map<String, Object> computer = computerList.get(i);
                //computer.put(STATUS, SysMgrConstants.RES_COMPUTER_ROOM_STATUS_INVALID);
                //updateComputerRoomStatus(computer, token);
            }
        }
    }

    /**
     * 获取仓库数据
     *
     * @param sysTenantInfo
     * @param computerList
     * @return
     */
    private Map<String, Object> getDepotSet(SysTenant sysTenantInfo, List<Map> computerList) {

        Long tenantId = sysTenantInfo.getId();

        SysTenantOperate operateQuery = new SysTenantOperate();
        operateQuery.setTenantId(tenantId);
        List<SysTenantOperate> operateList = sysTenantOperateMapper.selectBySelective(operateQuery);
        // 资产仓库操作
        SysTenantOperate astOperate = operateList.get(TWO);
        // 镜像仓库操作
        SysTenantOperate imgOperate = operateList.get(THREE);

        Map<String, Object> depotMap = new HashMap<>();
        Set<SysDepotBo> astDepot = new HashSet<>();
        Set<SysDepotBo> imgDepot = new HashSet<>();

        // 仓库地址去重
        computerList.forEach(map -> {
            String harborSite = map.get("harborSite").toString();
            String assetSite = map.get("assetSite").toString();
            astDepot.add(new SysDepotBo(assetSite, sysTenantInfo.getAstAddress(), sysTenantInfo.getAstUsername(),
                    sysTenantInfo.getAstPassword(), SysMgrConstants.DEPOT_PRI));
            imgDepot.add(new SysDepotBo(harborSite, sysTenantInfo.getImageProject(), sysTenantInfo.getImageUsername(),
                    sysTenantInfo.getImagePassword(), SysMgrConstants.DEPOT_PRI));
        });

        SysTenantDepotBo astTenantDepot =
                new SysTenantDepotBo(tenantId, astOperate.getId(), SysMgrConstants.DEPOT_AST, astDepot);
        SysTenantDepotBo imgTenantDepot =
                new SysTenantDepotBo(tenantId, imgOperate.getId(), SysMgrConstants.DEPOT_IMG, imgDepot);
        depotMap.put("ast", astTenantDepot);
        depotMap.put("img", imgTenantDepot);

        return depotMap;
    }

    /**
     * 创建租户仓库
     *
     * @param depot
     * @throws ApplicationException
     */
    private void createTenantDepot(SysTenantDepotBo depot) throws Exception {
        try {
            Future<Boolean> future = depotAsync.initDepotEnv(depot);
            future.get(this.depotTimeout, TimeUnit.SECONDS);
            // 设置任务状态为成功
            depotAsync.refeshTenantStatus(depot.getTenantOpId(), SysMgrConstants.TASK_SUCCESS);
        } catch (TimeoutException e) {
            createTenantDepotExceptionHandler(depot, new Exception("创建租户仓库超时！超时时间：" + this.depotTimeout + "秒"));
        } catch (ExecutionException e) {
            createTenantDepotExceptionHandler(depot, e);
        } catch (InterruptedException e) {
            createTenantDepotExceptionHandler(depot, e);
        } catch (Exception e) {
            createTenantDepotExceptionHandler(depot, e);
        }
    }

    private void createTenantDepotExceptionHandler (SysTenantDepotBo depot, Exception e) throws Exception {
        LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("创建租户仓库异常：", e.getMessage()));
        // 设置任务状态为失败
        depotAsync.refeshTenantStatus(depot.getTenantOpId(), SysMgrConstants.TASK_FAILURE);
        throw e;
    }

    /**
     * 清除租户仓库
     *
     * @param sysTenant
     * @param token
     */
    public void clearSysTenantDepot(SysTenant sysTenant, String token) {
        // 获取机房数据
        List<Map> computerList = this.getComputerList(token);
        LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("查询机房数据，返回：{0}", JSONObject.toJSON(computerList)));

        if (computerList != null) {
            Map<String, Object> depotMap = this.getDepotSet(sysTenant, computerList);
            SysTenantDepotBo astTenantDepot = (SysTenantDepotBo) depotMap.get("ast");
            SysTenantDepotBo imgTenantDepot = (SysTenantDepotBo) depotMap.get("img");

            // 删除租户资产产库
            this.removeTenantDepot(astTenantDepot);
            // 删除租户镜像仓库
            this.removeTenantDepot(imgTenantDepot);
        }
    }

    /**
     * 删除仓库
     *
     * @param depot
     */
    private void removeTenantDepot(SysTenantDepotBo depot) {
        LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("移除仓库，仓库参数{0}", JSONObject.toJSONString(depot)));
        try {
            Future<Boolean> astFuture = depotAsync.clearDepotEnv(depot);
            astFuture.get(this.depotTimeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                    MessageFormat.format("移除仓库失败，失败原因{0}", e.getMessage()));
        }
    }

    /**
     * @param map
     * @param token
     * @throws Exception
     */
    @Async("mySimpleAsync")
    public void refreshSysTenantDepot(Map<String, Object> map, String token) {
        LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                MessageFormat.format("更新租户的仓库信息:{0}", JSONObject.toJSONString(map)));
        Set<SysDepotBo> astDepot = new HashSet<>();
        Set<SysDepotBo> imgDepot = new HashSet<>();

        String harborSite = map.get("harborSite").toString();
        String assetSite = map.get("assetSite").toString();

        final List<SysTenant> userAddSysTenants1 = sysTenantMapper.getUserAddSysTenants();
        List<SysTenant> userAddSysTenants = userAddSysTenants1;
        for (SysTenant sysTenantInfo : userAddSysTenants) {
            astDepot.add(new SysDepotBo(assetSite, sysTenantInfo.getAstAddress(), sysTenantInfo.getAstUsername(),
                    sysTenantInfo.getAstPassword(), SysMgrConstants.DEPOT_PUB));
            imgDepot.add(new SysDepotBo(harborSite, sysTenantInfo.getImageProject(), sysTenantInfo.getImageUsername(),
                    sysTenantInfo.getImagePassword(), SysMgrConstants.DEPOT_PUB));

        }

        Map<String, Object> computer = new HashMap<>();
        computer.put("id", map.get("id"));
        try {
            depotAsync.initDepot(astDepot, SysMgrConstants.DEPOT_AST);
            depotAsync.initDepot(imgDepot, SysMgrConstants.DEPOT_IMG);
            computer.put(STATUS, SysMgrConstants.RES_COMPUTER_ROOM_STATUS_VALID);
        } catch (Exception e) {
            LOGGER.info(LogProperty.LOGTYPE_DETAIL, e.getMessage());
            computer.put(STATUS, SysMgrConstants.RES_COMPUTER_ROOM_STATUS_INVALID);
        } finally {
            updateComputerRoomStatus(computer, token);
        }

    }

    /**
     * 更新机房状态
     *
     * @param computer
     * @param token
     */
    private void updateComputerRoomStatus(Map<String, Object> computer, String token) {
        // 查询原信息
        BasicResponseContentVo<Map> response = null;
        response = restTemplateUtils
                .getForTokenEntity(microservicesProperties.getResmgr(),
                        "/v1/computer-room/" + computer.get("id"), token,
                        new HashMap<>(), new ParameterizedTypeReference<BasicResponseContentVo<Map>>() {
                        });
        Map<String, Object> newComputer = response.getContent();
        // 更新机房状
        newComputer.put(STATUS, computer.get(STATUS));
        BasicRequestContentVo<Map> reqInfo = new BasicRequestContentVo<>();
        reqInfo.setParams(newComputer);
        restTemplateUtils
                .postForTokenEntity(microservicesProperties.getResmgr(),
                        "/v1/computer-room/updateResComputerInfo", token,
                        reqInfo, new ParameterizedTypeReference<BasicResponseContentVo<?>>() {
                        });
        LOGGER.info(LogProperty.LOGTYPE_DETAIL, "机房状态更新完毕!");
    }
}
