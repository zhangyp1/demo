package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbLabelMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbLabelValueMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabel;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabelValue;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.GlbLabelQueryVo;
import com.newland.paas.paasservice.sysmgr.common.LabelMgrConstants;
import com.newland.paas.paasservice.sysmgr.error.SysLabelError;
import com.newland.paas.paasservice.sysmgr.service.GlbLabelValueService;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: paas-all
 * @description: 标签值实现
 * @author: Frown
 * @create: 2019-05-30 17:24
 **/
@Service("glbLabelValueService")
public class GlbLabelValueServiceImpl implements GlbLabelValueService {

    @Autowired
    private GlbLabelValueMapper glbLabelValueMapper;
    @Autowired
    private GlbLabelMapper glbLabelMapper;

    /**
     * 查询标签值
     *
     * @param queryVo
     * @return
     */
    @Override
    public List<GlbLabelValue> listGlbLabelValue(GlbLabelQueryVo queryVo) {
        GlbLabelValue glbLabelValue = new GlbLabelValue();
        glbLabelValue.setGlbLabelId(queryVo.getGlbLabelId());
        glbLabelValue.setTenantId(RequestContext.getTenantId());
        return glbLabelValueMapper.selectBySelective(glbLabelValue);
    }

    /**
     * 保存标签值
     *
     * @param glbLabelValue
     * @return
     */
    @Override
    public GlbLabelValue saveGlbLabelValue(GlbLabelValue glbLabelValue) throws ApplicationException {
        checkGlbLabelValue(glbLabelValue);
        Long glbLabelValueId = glbLabelValue.getGlbLabelValueId();
        if (glbLabelValueId == null) {
            GlbLabelValue glbLabelValueIn = new GlbLabelValue();
            glbLabelValueIn.setGlbLabelId(glbLabelValue.getGlbLabelId());
            glbLabelValueIn.setLabelValue(glbLabelValue.getLabelValue());
            glbLabelValueIn.setTenantId(RequestContext.getTenantId());
            glbLabelValueIn.setCreatorId(RequestContext.getUserId());
            glbLabelValueMapper.insert(glbLabelValueIn);

            glbLabelValue.setGlbLabelValueId(glbLabelValueIn.getGlbLabelValueId());
        } else {
            GlbLabelValue glbLabelValueIn = new GlbLabelValue();
            glbLabelValueIn.setGlbLabelValueId(glbLabelValueId);
            glbLabelValueIn.setGlbLabelId(glbLabelValue.getGlbLabelId());
            glbLabelValueIn.setLabelValue(glbLabelValue.getLabelValue());
            glbLabelValueMapper.updateByPrimaryKeySelective(glbLabelValueIn);
        }
        return glbLabelValue;
    }

    /**
     * 检查标签值
     * @param glbLabelValue
     * @throws ApplicationException
     */
    private void checkGlbLabelValue(GlbLabelValue glbLabelValue) throws ApplicationException {
        Long glbLabelValueId = glbLabelValue.getGlbLabelValueId();
        if (glbLabelValueId == null) {
            Long glbLabelId = glbLabelValue.getGlbLabelId();
            GlbLabel glbLabel = glbLabelMapper.selectByPrimaryKey(glbLabelId);
            Long curTenantId = RequestContext.getTenantId();
            Long lblTenantId = glbLabel.getTenantId();
            if (curTenantId.longValue() != LabelMgrConstants.LABEL_COMMON_TENANT
                    && lblTenantId.longValue() == LabelMgrConstants.LABEL_COMMON_TENANT) {
                throw new ApplicationException(SysLabelError.LABEL_VALUE_ADD_ERROR);
            }

            GlbLabelValue glbLabelValueReq = new GlbLabelValue();
            glbLabelValueReq.setGlbLabelId(glbLabelId);
            glbLabelValueReq.setLabelValue(glbLabelValue.getLabelValue());
            glbLabelValueReq.setTenantId(RequestContext.getTenantId());
            List<GlbLabelValue> glbLabels = glbLabelValueMapper.selectBySelective(glbLabelValueReq);
            if (glbLabels != null && !glbLabels.isEmpty()) {
                throw new ApplicationException(SysLabelError.LABEL_VALUE_EXIST_ERROR);
            }
        } else {
            GlbLabelValue glbLabelValueOld = glbLabelValueMapper.selectByPrimaryKey(glbLabelValueId);
            Long curTenantId = RequestContext.getTenantId();
            Long lblTenantId = glbLabelValueOld.getTenantId();
            if (curTenantId.longValue() != LabelMgrConstants.LABEL_COMMON_TENANT
                    && lblTenantId.longValue() == LabelMgrConstants.LABEL_COMMON_TENANT) {
                throw new ApplicationException(SysLabelError.LABEL_VALUE_UPDATE_ERROR);
            }

            GlbLabelValue glbLabelValueReq = new GlbLabelValue();
            glbLabelValueReq.setGlbLabelId(glbLabelValue.getGlbLabelId());
            glbLabelValueReq.setLabelValue(glbLabelValue.getLabelValue());
            glbLabelValueReq.setTenantId(RequestContext.getTenantId());
            List<GlbLabelValue> glbLabels = glbLabelValueMapper.selectBySelective(glbLabelValueReq);
            for (GlbLabelValue label : glbLabels) {
                if (glbLabelValueId.longValue() != label.getGlbLabelId().longValue()) {
                    throw new ApplicationException(SysLabelError.LABEL_EXIST_ERROR);
                }
            }
        }
    }

    /**
     * 获取标签值
     *
     * @param glbLabelValue
     * @return
     */
    @Override
    public GlbLabelValue getGlbLabelValue(GlbLabelValue glbLabelValue) throws ApplicationException {
        Long glbLabelValueId = glbLabelValue.getGlbLabelValueId();
        if (glbLabelValueId == null) {
            throw new ApplicationException(SysLabelError.LABEL_ID_NULL_ERROR);
        }
        return glbLabelValueMapper.selectByPrimaryKey(glbLabelValueId);
    }

    /**
     * 删除标签值
     *
     * @param glbLabelValue
     */
    @Override
    public void deleteGlbLabelValue(GlbLabelValue glbLabelValue) throws ApplicationException {
        Long glbLabelValueId = glbLabelValue.getGlbLabelValueId();
        if (glbLabelValueId == null) {
            throw new ApplicationException(SysLabelError.LABEL_ID_NULL_ERROR);
        }
        GlbLabelValue glbLabelValueReq = glbLabelValueMapper.selectByPrimaryKey(glbLabelValueId);

        Long curTenantId = RequestContext.getTenantId();
        Long lblTenantId = glbLabelValueReq.getTenantId();
        if (curTenantId.longValue() != LabelMgrConstants.LABEL_COMMON_TENANT
                && lblTenantId.longValue() == LabelMgrConstants.LABEL_COMMON_TENANT) {
            throw new ApplicationException(SysLabelError.LABEL_VALUE_DEL_ERROR);
        }

        GlbLabelValue glbLabelValueDel = new GlbLabelValue();
        glbLabelValueDel.setGlbLabelValueId(glbLabelValueId);
        glbLabelValueDel.setDelFlag(1L);
        glbLabelValueMapper.updateByPrimaryKeySelective(glbLabelValueDel);
    }
}
