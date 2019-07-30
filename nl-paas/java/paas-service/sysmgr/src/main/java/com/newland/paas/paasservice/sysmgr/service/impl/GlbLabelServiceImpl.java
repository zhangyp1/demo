package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbLabelMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbLabelValueMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabel;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabelValue;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.GlbLabelBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelLblItemRspVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelLblRspVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelQueryVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelRspVo;
import com.newland.paas.paasservice.sysmgr.common.LabelMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysLabelError;
import com.newland.paas.paasservice.sysmgr.service.GlbLabelService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: paas-all
 * @description: 标签管理
 * @author: Frown
 * @create: 2019-05-30 17:00
 **/
@Service
public class GlbLabelServiceImpl implements GlbLabelService {

    private static final Log LOG = LogFactory.getLogger(GlbLabelServiceImpl.class);

    @Autowired
    private GlbLabelMapper glbLabelMapper;
    @Autowired
    private GlbLabelValueMapper glbLabelValueMapper;

    private static final List<String> RESERVED_WORDS = Arrays.asList("app", "appcode", "unitid", "appid",
            "appname_b64", "appunitid", "sysCategoryId", "sysCategoryCode", "sysCategoryName_b64");

    private static final List<String> YY_OBJ_TYPES = Arrays.asList("res", "service");

    private static final List<String> ZH_OBJ_TYPES = Arrays.asList("assets", "application",
            "cluster_work", "lb_group");

    /**
     * 分页查询标签项
     *
     * @param queryVo
     * @param pageInfo
     * @return
     */
    @Override
    public ResultPageData<GlbLabel> pageGlbLabel(GlbLabelQueryVo queryVo, PageInfo pageInfo) {
        Long tenantId = RequestContext.getTenantId();
        GlbLabelBo glbLabelBo = new GlbLabelBo();
        glbLabelBo.setLabelType(queryVo.getLabelType());
        glbLabelBo.setObjType(queryVo.getObjType());
        glbLabelBo.setLabelName(queryVo.getLabelName());
        glbLabelBo.setTenantId(tenantId);
        if (tenantId.longValue() == LabelMgrConstants.LABEL_COMMON_TENANT) {
            glbLabelBo.setObjTypes(YY_OBJ_TYPES);
        } else {
            glbLabelBo.setObjTypes(ZH_OBJ_TYPES);
        }
        PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        List<GlbLabel> glbLabels = glbLabelMapper.lisGlbLabel(glbLabelBo);
        return new ResultPageData<>(glbLabels);
    }


    /**
     * 保存标签项
     *
     * @param glbLabel
     * @return
     */
    @Override
    public GlbLabel saveGlbLabel(GlbLabel glbLabel) throws ApplicationException {
        checkGlbLabel(glbLabel);
        Long glbLabelId = glbLabel.getGlbLabelId();
        if (glbLabelId == null) {
            GlbLabel glbLabelIn = new GlbLabel();
            glbLabelIn.setLabelType(glbLabel.getLabelType());
            glbLabelIn.setObjType(glbLabel.getObjType());
            glbLabelIn.setLabelCode(glbLabel.getLabelCode());
            glbLabelIn.setTenantId(RequestContext.getTenantId());
            glbLabelIn.setLabelName(glbLabel.getLabelName());
            glbLabelIn.setCreatorId(RequestContext.getUserId());
            glbLabelIn.setReservedWord(glbLabel.getReservedWord());
            glbLabelMapper.insert(glbLabelIn);

            glbLabel.setGlbLabelId(glbLabelIn.getGlbLabelId());

        } else {
            GlbLabel glbLabelIn = new GlbLabel();
            glbLabelIn.setGlbLabelId(glbLabelId);
            glbLabelIn.setObjType(glbLabel.getObjType());
            glbLabelIn.setLabelCode(glbLabel.getLabelCode());
            glbLabelIn.setLabelName(glbLabel.getLabelName());
            glbLabelIn.setReservedWord(glbLabel.getReservedWord());
            glbLabelMapper.updateByPrimaryKeySelective(glbLabelIn);
        }
        return glbLabel;
    }

    /**
     * 检查标签
     * @param glbLabel
     * @throws ApplicationException
     */
    private void checkGlbLabel(GlbLabel glbLabel) throws ApplicationException {
        Long glbLabelId = glbLabel.getGlbLabelId();
        if (glbLabelId == null) {
            GlbLabel glbLabelReq = new GlbLabel();
            glbLabelReq.setObjType(glbLabel.getObjType());
            glbLabelReq.setLabelCode(glbLabel.getLabelCode());
            glbLabelReq.setTenantId(RequestContext.getTenantId());
            List<GlbLabel> glbLabels = glbLabelMapper.selectBySelective(glbLabelReq);
            if (glbLabels != null && !glbLabels.isEmpty()) {
                throw new ApplicationException(SysLabelError.LABEL_EXIST_ERROR);
            }
        } else {
            GlbLabel glbLabelOld = glbLabelMapper.selectByPrimaryKey(glbLabelId);
            Long curTenantId = RequestContext.getTenantId();
            Long lblTenantId = glbLabelOld.getTenantId();
            if (curTenantId.longValue() != LabelMgrConstants.LABEL_COMMON_TENANT
                    && lblTenantId.longValue() == LabelMgrConstants.LABEL_COMMON_TENANT) {
                throw new ApplicationException(SysLabelError.LABEL_UPDATE_ERROR);
            }
            GlbLabel glbLabelReq = new GlbLabel();
            glbLabelReq.setObjType(glbLabel.getObjType());
            glbLabelReq.setLabelCode(glbLabel.getLabelCode());
            glbLabelReq.setTenantId(RequestContext.getTenantId());
            List<GlbLabel> glbLabels = glbLabelMapper.selectBySelective(glbLabelReq);
            for (GlbLabel label : glbLabels) {
                if (glbLabelId.longValue() != label.getGlbLabelId().longValue()) {
                    throw new ApplicationException(SysLabelError.LABEL_EXIST_ERROR);
                }
            }
        }
    }

    /**
     * @param glbLabel
     */
    @Override
    public void deleteGlbLabel(GlbLabel glbLabel) throws ApplicationException {
        Long glbLabelId = glbLabel.getGlbLabelId();
        if (glbLabelId == null) {
            throw new ApplicationException(SysLabelError.LABEL_ID_NULL_ERROR);
        }
        GlbLabel glbLabelReq = glbLabelMapper.selectByPrimaryKey(glbLabelId);

        Long curTenantId = RequestContext.getTenantId();
        Long lblTenantId = glbLabelReq.getTenantId();
        if (curTenantId.longValue() != LabelMgrConstants.LABEL_COMMON_TENANT
                && lblTenantId.longValue() == LabelMgrConstants.LABEL_COMMON_TENANT) {
            throw new ApplicationException(SysLabelError.LABEL_DEL_ERROR);
        }

        GlbLabelValue glbLabelValue = new GlbLabelValue();
        glbLabelValue.setGlbLabelId(glbLabelId);
        List<GlbLabelValue> glbLabelValues = glbLabelValueMapper.selectBySelective(glbLabelValue);

        if (glbLabelValues != null && !glbLabelValues.isEmpty()) {
            throw new ApplicationException(SysLabelError.LABEL_HAS_VALUES_ERROR);
        }


        GlbLabel glbLabelDel = new GlbLabel();
        glbLabelDel.setGlbLabelId(glbLabelId);
        glbLabelDel.setDelFlag(1L);
        glbLabelMapper.updateByPrimaryKeySelective(glbLabelDel);

    }

    /**
     * 获取标签项
     *
     * @param glbLabel
     * @return
     */
    @Override
    public GlbLabel getGlbLabel(GlbLabel glbLabel) throws ApplicationException {
        Long glbLabelId = glbLabel.getGlbLabelId();
        if (glbLabelId == null) {
            throw new ApplicationException(SysLabelError.LABEL_ID_NULL_ERROR);
        }
        return glbLabelMapper.selectByPrimaryKey(glbLabelId);
    }

    /**
     * 查询标签值分组
     * @param queryVo
     * @return
     */
    @Override
    public List<GlbLabelRspVo> listGroupGlbLabel(GlbLabelQueryVo queryVo) {
        Long tenantId = RequestContext.getTenantId();
        String objType = queryVo.getObjType();
        String labelType = queryVo.getLabelType();
        GlbLabelBo glbLabelBo = new GlbLabelBo();
        glbLabelBo.setObjType(objType);
        glbLabelBo.setLabelType(labelType);
        glbLabelBo.setTenantId(tenantId);
        List<GlbLabelBo> glbLabels = glbLabelMapper.listGlbLabels(glbLabelBo);
        LOG.info("##################查询标签值分组结果##################："
                + JSONObject.toJSONString(glbLabels));
        List<GlbLabelRspVo> rspVoList = new ArrayList<>();
        if (glbLabels != null && !glbLabels.isEmpty()) {
            handleExtLabels(queryVo, glbLabels);
            Map<String, List<GlbLabelBo>> glbLabelMap = glbLabels.stream()
                    .collect(Collectors.groupingBy(GlbLabelBo::getObjType));
            Set<Map.Entry<String, List<GlbLabelBo>>> ens = glbLabelMap.entrySet();
            List<GlbLabelLblRspVo> lblRspVoList = null;
            List<GlbLabelLblItemRspVo> lblItemRspVoList = null;
            // 标签对象汇总
            GlbLabelRspVo rspVo = null;
            // 标签汇总
            GlbLabelLblRspVo lblRspVo  = null;
            // 标签项汇总
            GlbLabelLblItemRspVo itemRspVo = null;
            for (Map.Entry<String, List<GlbLabelBo>> en : ens) {
                rspVo = new GlbLabelRspVo();
                rspVo.setObj(en.getKey());
                rspVoList.add(rspVo);
                List<GlbLabelBo> labels = en.getValue();
                Map<String, List<GlbLabelBo>> glbLabelLblMap = labels.stream()
                        .collect(Collectors.groupingBy(GlbLabelBo::getLabelCode));
                // 标签
                lblRspVoList = new ArrayList<>();
                rspVo.setLabels(lblRspVoList);
                Set<Map.Entry<String, List<GlbLabelBo>>> entries = glbLabelLblMap.entrySet();
                for (Map.Entry<String, List<GlbLabelBo>> entry : entries) {
                    lblRspVo = new GlbLabelLblRspVo();
                    lblRspVo.setCode(entry.getKey());
                    List<GlbLabelBo> lblItems = entry.getValue();
                    lblRspVo.setType(lblItems.get(0).getLabelType());
                    lblRspVo.setName(lblItems.get(0).getLabelName());
                    lblRspVo.setId(lblItems.get(0).getGlbLabelId());
                    lblRspVoList.add(lblRspVo);
                    // 标签项
                    lblItemRspVoList = new ArrayList<>();
                    lblRspVo.setValues(lblItemRspVoList);
                    for (GlbLabelBo lblItem : lblItems) {
                        if (lblItem != null
                                && StringUtils.isNotEmpty(lblItem.getLabelValue())) {
                            itemRspVo = new GlbLabelLblItemRspVo();
                            lblItemRspVoList.add(itemRspVo);
                            itemRspVo.setId(lblItem.getGlbLabelValueId());
                            itemRspVo.setValue(lblItem.getLabelValue());
                        }
                    }
                }
            }
        }

        return rspVoList;
    }

    /**
     * 处理外部传入标签
     * @param queryVo
     * @param glbLabels
     */
    private void handleExtLabels(GlbLabelQueryVo queryVo, List<GlbLabelBo> glbLabels) {
        List<GlbLabelQueryVo> extLabels = queryVo.getExtLabels();
        if (extLabels != null && !extLabels.isEmpty()) {
            List<GlbLabelBo> extList = extLabels.stream().map(e -> {
                GlbLabelBo label = new GlbLabelBo();
                label.setObjType(queryVo.getObjType());
                label.setLabelType(queryVo.getLabelType());
                label.setLabelName(e.getLabelName());
                label.setLabelCode(e.getLabelCode());
                label.setLabelValue(e.getLabelValue());
                return label;
            }).filter(e -> !glbLabels.contains(e)).collect(Collectors.toList());
            glbLabels.addAll(extList);
        }
    }

    /**
     * 检查标签编码
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map checkLabelCode(GlbLabelQueryVo queryVo) {
        Map<Object, Object> map = new HashMap<>();
        Long glbLabelId = queryVo.getGlbLabelId();
        String objType = queryVo.getObjType();
        String labelCode = queryVo.getLabelCode();
        Boolean result = true;
        String msg = null;

        // 排除未选择标签对象类型情况
        if (StringUtils.isEmpty(objType)) {
            map.put("result", result);
            map.put("msg", msg);
            return map;
        }

        // 排除保留字
        if (RESERVED_WORDS.contains(labelCode)) {
            result = false;
            msg = "不能使用系统保留字！";
        } else {
            GlbLabel glbLabel = null;
            glbLabel = new GlbLabel();
            glbLabel.setTenantId(LabelMgrConstants.LABEL_COMMON_TENANT);
            glbLabel.setLabelCode(labelCode);
            List<GlbLabel> commonLabels = glbLabelMapper.selectBySelective(glbLabel);
            if (commonLabels != null && !commonLabels.isEmpty()) {
                if (glbLabelId != null) {
                    for (GlbLabel commonLabel : commonLabels) {
                        if (commonLabel.getGlbLabelId().longValue() != glbLabelId.longValue()) {
                            result = false;
                            msg = "编码和公共标签编码重复！";
                            break;
                        }
                    }
                } else {
                    result = false;
                    msg = "编码和公共标签编码重复！";
                }
            } else {
                glbLabel = new GlbLabel();
                glbLabel.setObjType(objType);
                glbLabel.setLabelCode(labelCode);
                glbLabel.setTenantId(RequestContext.getTenantId());
                List<GlbLabel> tenantLabels = glbLabelMapper.selectBySelective(glbLabel);
                if (tenantLabels != null && !tenantLabels.isEmpty()) {
                    if (glbLabelId != null) {
                        for (GlbLabel tenantLabel : tenantLabels) {
                            if (tenantLabel.getGlbLabelId().longValue() != glbLabelId.longValue()) {
                                result = false;
                                msg = "编码和租户标签编码重复！";
                                break;
                            }
                        }
                    } else {
                        result = false;
                        msg = "编码和租户标签编码重复！";
                    }
                }
            }
        }

        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 检查标签名称
     *
     * @param queryVo
     * @return
     */
    @Override
    public Map checkLabelName(GlbLabelQueryVo queryVo) {
        Map<Object, Object> map = new HashMap<>();
        Long glbLabelId = queryVo.getGlbLabelId();
        String objType = queryVo.getObjType();
        String labelName = queryVo.getLabelName();
        Boolean result = true;
        String msg = null;

        // 排除未选择标签对象类型情况
        if (StringUtils.isEmpty(objType)) {
            map.put("result", result);
            map.put("msg", msg);
            return map;
        }

        if (RESERVED_WORDS.contains(labelName)) {
            result = false;
            msg = "不能使用系统保留字！";
        } else {
            GlbLabel glbLabel = null;
            glbLabel = new GlbLabel();
            glbLabel.setTenantId(LabelMgrConstants.LABEL_COMMON_TENANT);
            glbLabel.setLabelName(labelName);
            List<GlbLabel> commonLabels = glbLabelMapper.selectBySelective(glbLabel);
            if (commonLabels != null && !commonLabels.isEmpty()) {
                if (glbLabelId != null) {
                    for (GlbLabel commonLabel : commonLabels) {
                        if (commonLabel.getGlbLabelId().longValue() != glbLabelId.longValue()) {
                            result = false;
                            msg = "名称和公共标签名称重复！";
                            break;
                        }
                    }
                } else {
                    result = false;
                    msg = "名称和公共标签名称重复！";
                }
            } else {
                glbLabel = new GlbLabel();
                glbLabel.setObjType(objType);
                glbLabel.setLabelName(labelName);
                glbLabel.setTenantId(RequestContext.getTenantId());
                List<GlbLabel> tenantLabels = glbLabelMapper.selectBySelective(glbLabel);
                if (tenantLabels != null && !tenantLabels.isEmpty()) {
                    if (glbLabelId != null) {
                        for (GlbLabel tenantLabel : tenantLabels) {
                            if (tenantLabel.getGlbLabelId().longValue() != glbLabelId.longValue()) {
                                result = false;
                                msg = "名称和租户标签名称重复！";
                                break;
                            }
                        }
                    } else {
                        result = false;
                        msg = "名称和租户标签名称重复！";
                    }
                }
            }
        }

        map.put("result", result);
        map.put("msg", msg);
        return map;
    }
}
