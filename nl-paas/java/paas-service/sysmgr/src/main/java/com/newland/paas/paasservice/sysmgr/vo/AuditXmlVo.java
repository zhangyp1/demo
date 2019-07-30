package com.newland.paas.paasservice.sysmgr.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import java.util.List;

/**
 * Author:PanYang
 * Date:Created in 下午3:04 2018/8/2
 * Modified By:
 */
@XmlRootElement(name = "ROOT")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditXmlVo {
    List<AuditInfoVo> log4A;

    public List<AuditInfoVo> getLog4A() {
        return log4A;
    }

    public void setLog4A(List<AuditInfoVo> log4A) {
        this.log4A = log4A;
    }
}
