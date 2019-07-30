package com.newland.paas.ee.vo.asset;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 资产外部服务接口
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstExternalSvrIntfRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8191967309789133723L;
    // 接口标识
    private Long interfaceId;
    // 接口名称
    private String intfName;
    // 接口描述
    private String intfDesc;
    // 服务接口key
    private String intfKey;
    // 接口方式
    private String intfType;
    // 服务地址格式
    private String srvAddrFormat;
    // 负载均衡器类型
    private String lbType;
    // 提供服务单元ID
    private Long unitId;
    // 提供服务单元名称
    private String unitName;
    // 集群端口
    private String clusterPort;
    // 容器端口
    private String containerPort;
    // 接口适用范围
    private String scope;
    // 获取服务接口属性值
    private List<AstExternalSvrIntfAttrRsp> listAstExternalSvrIntfAttrRsp = Collections.emptyList();

    public Long getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Long interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getIntfName() {
        return intfName;
    }

    public void setIntfName(String intfName) {
        this.intfName = intfName;
    }

    public String getIntfDesc() {
        return intfDesc;
    }

    public void setIntfDesc(String intfDesc) {
        this.intfDesc = intfDesc;
    }

    public String getIntfKey() {
        return intfKey;
    }

    public void setIntfKey(String intfKey) {
        this.intfKey = intfKey;
    }

    public String getIntfType() {
        return intfType;
    }

    public void setIntfType(String intfType) {
        this.intfType = intfType;
    }

    public String getSrvAddrFormat() {
        return srvAddrFormat;
    }

    public void setSrvAddrFormat(String srvAddrFormat) {
        this.srvAddrFormat = srvAddrFormat;
    }

    public String getLbType() {
        return lbType;
    }

    public void setLbType(String lbType) {
        this.lbType = lbType;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getClusterPort() {
        return clusterPort;
    }

    public void setClusterPort(String clusterPort) {
        this.clusterPort = clusterPort;
    }

    public String getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(String containerPort) {
        this.containerPort = containerPort;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<AstExternalSvrIntfAttrRsp> getListAstExternalSvrIntfAttrRsp() {
        return listAstExternalSvrIntfAttrRsp;
    }

    public void setListAstExternalSvrIntfAttrRsp(List<AstExternalSvrIntfAttrRsp> listAstExternalSvrIntfAttrRsp) {
        this.listAstExternalSvrIntfAttrRsp = listAstExternalSvrIntfAttrRsp;
    }
}
