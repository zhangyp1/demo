package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasservice.sysmgr.vo.ObjectTypeVo;

import java.util.List;
import java.util.Map;

/**
 * 对象权限规则配置相关
 *
 * @author zhongqingjiang
 */
public interface ObjPermissionRulerService {

    List<ObjectTypeVo> listCanGrantObjType(Boolean withOperates);

    List<String> listCanNotGrantObjOperate(String objType);

    Map<String, ObjectTypeVo> listCanGrantObjTypeMap(Boolean withOperates);

    List<String> listCanGrantObjTypeCode();

}
