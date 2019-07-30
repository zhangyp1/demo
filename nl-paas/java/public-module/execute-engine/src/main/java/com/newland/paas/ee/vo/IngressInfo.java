package com.newland.paas.ee.vo;

import java.io.Serializable;
import java.util.Date;

public class IngressInfo implements Serializable {
    private static final Long serialVersionUID = 89456134066724910L;

    private Long loadBalanceId;
    
    private Long clusterId;
    
    private Long clusterZoneId;
  
    private Long sysCategoryId;
    
    private Long tenantId;

    private String  lbAlgorithm;
      
    private String   ipAddrList;
    
    private Short replicasNum;
    
    private Short sslRedirect;
   
    private Float cpuLimit;
    
    private Float memoryLimit;
    
    private Float cpuRequest;
    
    private Float memoryRequest;
    
    private Long httpPort;
    
    private Long httpsPort;
    
    private Long statusPort;
    
    private Long metricPort;
    
    private Date createDate;
    
    private Date changeDate;

    private Short delFlag;
    
    private Float defaultBackendLimitCpu;
    
    private Float defaultBackendLimitMemory;
    
    private Float nginxIngressControllerLimitCpu;
    
    private Float nginxIngressControllerLimitMemory;
    
    private String lbname;
    
    private String hostName;
    
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }
    
    public String getLbname() {
        return lbname;
    }

    public void setLbname(String lbname) {
        this.lbname = lbname == null ? null : lbname.trim();
    }
    
    public Float getDefaultBackendLimitCpu() {
        return defaultBackendLimitCpu;
    }

    public void setDefaultBackendLimitCpu(Float defaultBackendLimitCpu) {
        this.defaultBackendLimitCpu = defaultBackendLimitCpu;
    }
    
    public Float getDefaultBackendLimitMemory() {
        return defaultBackendLimitMemory;
    }

    public void setDefaultBackendLimitMemory(Float defaultBackendLimitMemory) {
        this.defaultBackendLimitMemory = defaultBackendLimitMemory;
    }
 
    public Float getNginxIngressControllerLimitCpu() {
        return nginxIngressControllerLimitCpu;
    }

    public void setNginxIngressControllerLimitCpu(Float nginxIngressControllerLimitCpu) {
        this.nginxIngressControllerLimitCpu = nginxIngressControllerLimitCpu;
    }
    
    public Float getNginxIngressControllerLimitMemory() {
        return nginxIngressControllerLimitMemory;
    }

    public void setNginxIngressControllerLimitMemory(Float nginxIngressControllerLimitMemory) {
        this.nginxIngressControllerLimitMemory = nginxIngressControllerLimitMemory;
    }
    
    public Long getLoadBalanceId() {
        return loadBalanceId;
    }

    public void setLoadBalanceId(Long loadBalanceId) {
        this.loadBalanceId = loadBalanceId;
    }
    
    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }
    
    public Long getClusterZoneId() {
        return clusterZoneId;
    }

    public void setClusterZoneId(Long clusterZoneId) {
        this.clusterZoneId = clusterZoneId;
    }
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getLbAlgorithm() {
        return lbAlgorithm;
    }

    public void setLbAlgorithm(String lbAlgorithm) {
        this.lbAlgorithm = lbAlgorithm == null ? null : lbAlgorithm.trim();
    }
    
    public String getIpAddrList() {
        return ipAddrList;
    }

    public void setIpAddrList(String ipAddrList) {
        this.ipAddrList = ipAddrList == null ? null : ipAddrList.trim();
    }
   
    public Long getSysCategoryId() {
        return sysCategoryId;
    }

    public void setSysCategoryId(Long sysCategoryId) {
        this.sysCategoryId = sysCategoryId;
    }
    
    public Short getReplicasNum() {
        return replicasNum;
    }

    public void setReplicasNum(Short replicasNum) {
        this.replicasNum = replicasNum;
    }
    
    public Float getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(Float cpuLimit) {
        this.cpuLimit = cpuLimit;
    }
   
    public Float getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Float memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
    
    public Float getCpuRequest() {
        return cpuRequest;
    }

    public void setCpuRequest(Float cpuRequest) {
        this.cpuRequest = cpuRequest;
    }
    
    public Float getMemoryRequest() {
        return memoryRequest;
    }

    public void setMemoryRequest(Float memoryRequest) {
        this.memoryRequest = memoryRequest;
    }
    
    public Short getSslRedirect() {
        return sslRedirect;
    }

    public void setSslRedirect(Short sslRedirect) {
        this.sslRedirect = sslRedirect;
    }
    
    public Long getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Long httpPort) {
        this.httpPort = httpPort;
    }
    
    public Long getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(Long httpsPort) {
        this.httpsPort = httpsPort;
    }
    
    public Long getStatusPort() {
        return statusPort;
    }

    public void setStatusPort(Long statusPort) {
        this.statusPort = statusPort;
    }

    public Long getMetricPort() {
        return metricPort;
    }

    public void setMetricPort(Long metricPort) {
        this.metricPort = metricPort;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    
    public Short getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }

}