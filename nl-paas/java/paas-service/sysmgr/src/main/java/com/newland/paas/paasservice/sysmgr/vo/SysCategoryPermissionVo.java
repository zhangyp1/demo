package com.newland.paas.paasservice.sysmgr.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统分组权限
 *
 * @author zhongqingjiang
 */
public class SysCategoryPermissionVo extends SysCategoryVo {

    public SysCategoryPermissionVo () {
        objectTypeGrantList = new ArrayList<>();
    }

    List<ObjectTypeGrantVo> objectTypeGrantList;

    public List<ObjectTypeGrantVo> getObjectTypeGrantList() {
        return objectTypeGrantList;
    }

    public void setObjectTypeGrantList(List<ObjectTypeGrantVo> objectTypeGrantList) {
        this.objectTypeGrantList = objectTypeGrantList;
    }

    public ObjectTypeGrantVo getObjectTypeGrant(String objTypeCode) {
        for (ObjectTypeGrantVo objectTypeGrantVo : objectTypeGrantList) {
            if (objectTypeGrantVo.getObjTypeCode().equals(objTypeCode)) {
                return objectTypeGrantVo;
            }
        }
        return null;
    }
}
