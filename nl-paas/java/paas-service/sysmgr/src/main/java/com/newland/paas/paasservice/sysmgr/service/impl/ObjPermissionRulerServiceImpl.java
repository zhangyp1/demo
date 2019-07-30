package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.paasservice.sysmgr.service.GlbDictService;
import com.newland.paas.paasservice.sysmgr.service.ObjPermissionRulerService;
import com.newland.paas.paasservice.sysmgr.vo.ObjectTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对象权限规则配置相关
 *
 * @author zhongqingjiang
 */
@Service
public class ObjPermissionRulerServiceImpl implements ObjPermissionRulerService {

    @Autowired
    GlbDictService glbDictService;

    @Override
    public List<ObjectTypeVo> listCanGrantObjType(Boolean withOperates) {
        // TODO: 可授权的对象类型和对象操作，考虑配置化
        List<ObjectTypeVo> objectTypeVoList = new ArrayList<>();
        objectTypeVoList.add(new ObjectTypeVo(SysMgrConstants.OBJECT_TYPE_ASSETS, "资产"));
        objectTypeVoList.add(new ObjectTypeVo(SysMgrConstants.OBJECT_TYPE_ASSETS_CONTAINER, "原生资产镜像"));
        objectTypeVoList.add(new ObjectTypeVo(SysMgrConstants.OBJECT_TYPE_ASSETS_M, "资产编排"));
        if (!SysMgrConstants.TENANT_ID_YUNWEI.equals(RequestContext.getTenantId())) {
            objectTypeVoList.add(new ObjectTypeVo(SysMgrConstants.OBJECT_TYPE_APPLICATION, "应用"));
        }
        objectTypeVoList.add(new ObjectTypeVo(SysMgrConstants.OBJECT_TYPE_CLUSTER, "集群"));
        objectTypeVoList.add(new ObjectTypeVo(SysMgrConstants.OBJECT_TYPE_INGRESS, "ingress负载均衡器"));
        objectTypeVoList.add(new ObjectTypeVo(SysMgrConstants.OBJECT_TYPE_VOL, "持久卷"));
        if (withOperates) {
            for (ObjectTypeVo objectTypeVo : objectTypeVoList) {
                List<String> canNotGrantObjOperateList = this.listCanNotGrantObjOperate(objectTypeVo.getObjTypeCode());
                List<GlbDict> allOperates = glbDictService.getByDictPcode(objectTypeVo.getObjTypeCode() + "_op");
                List<GlbDict> filteredOperates = new ArrayList<>();
                for (GlbDict operate : allOperates) {
                    if (canNotGrantObjOperateList != null && canNotGrantObjOperateList.contains(operate.getDictCode())) {
                        continue;
                    }
                    filteredOperates.add(operate);
                }
                objectTypeVo.setOperates(filteredOperates);
            }
        }
        return objectTypeVoList;
    }

    @Override
    public List<String> listCanNotGrantObjOperate(String objType) {
        // TODO：不可授权的对象操作，考虑配置化
        List<String> canNotGrantObjOperateList = null;
        if (SysMgrConstants.OBJECT_TYPE_ASSETS.equals(objType)) {
            canNotGrantObjOperateList = new ArrayList<>();
            canNotGrantObjOperateList.add("pf_exportPag");
        } else if (SysMgrConstants.OBJECT_TYPE_ASSETS_M.equals(objType)) {
            canNotGrantObjOperateList = new ArrayList<>();
            canNotGrantObjOperateList.add("pf_quote");
        } else if (SysMgrConstants.OBJECT_TYPE_APPLICATION.equals(objType)) {
            canNotGrantObjOperateList = new ArrayList<>();
            canNotGrantObjOperateList.add("pf_modify");
            canNotGrantObjOperateList.add("pf_empower");
            canNotGrantObjOperateList.add("pf_policy");
            canNotGrantObjOperateList.add("pf_migrate");
            canNotGrantObjOperateList.add("pf_online");
            canNotGrantObjOperateList.add("pf_offline");
            canNotGrantObjOperateList.add("pf_execute");
        } else if (SysMgrConstants.OBJECT_TYPE_CLUSTER.equals(objType)) {
            canNotGrantObjOperateList = new ArrayList<>();
            canNotGrantObjOperateList.add("pf_modify");
            canNotGrantObjOperateList.add("pf_deploy");
            canNotGrantObjOperateList.add("pf_uninstall");
            canNotGrantObjOperateList.add("pf_scale_in");
            canNotGrantObjOperateList.add("pf_scale_out");
            canNotGrantObjOperateList.add("pf_deploy_approve");
            canNotGrantObjOperateList.add("pf_uninstall_approve");
            canNotGrantObjOperateList.add("pf_scaleIn_approve");
            canNotGrantObjOperateList.add("pf_scaleOut_approve");
            canNotGrantObjOperateList.add("pf_policy");
            canNotGrantObjOperateList.add("pf_updt_lbl");
            if (!SysMgrConstants.TENANT_ID_YUNWEI.equals(RequestContext.getTenantId())) {
                canNotGrantObjOperateList.add("pf_cluster_partition_create");
            }
        }
        return canNotGrantObjOperateList;
    }

    @Override
    public Map<String, ObjectTypeVo> listCanGrantObjTypeMap(Boolean withOperates) {
        HashMap<String, ObjectTypeVo> canGrantObjTypeMap = new HashMap<>();
        List<ObjectTypeVo> objectTypeVoList = this.listCanGrantObjType(withOperates);
        for (ObjectTypeVo objectTypeVO : objectTypeVoList) {
            canGrantObjTypeMap.put(objectTypeVO.getObjTypeCode(), objectTypeVO);
        }
        return  canGrantObjTypeMap;
    }

    @Override
    public List<String> listCanGrantObjTypeCode() {
        return listCanGrantObjType(false).stream().map(ObjectTypeVo::getObjTypeCode).collect(Collectors.toList());
    }

}
