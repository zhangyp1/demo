package com.newland.paas.ee.vo;

import com.newland.paas.ee.vo.cluster.ClusterElasticVo;
import com.newland.paas.ee.vo.cluster.ClusterQuotaVo;
import com.newland.paas.ee.vo.resource.ResourceVo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集群实例详细信息
 * @author ai
 *
 */
public class K8SClusterInstanceDetailInfo extends ClusterInstanceDetailInfo{
    private Float cpuQuota;                   //共享CPU配额
    private Float memoryQuota;                //共享内存配额
    private List<ResourceVo> etcds;             //ETCD资源
    private List<ResourceVo> k8sMasters;        //k8s Masters资源
    private List<ResourceVo> k8sWorkers;        //k8s Workers资源
    private List<ClusterElasticVo> elastic;     //弹性策略列表
    private List<ClusterQuotaVo> quota;         //配额列表
    private String homePath;                    //K8S安装路径
    private String masterIp;                    //K8S_MASTER的IP地址
    private String floatIp;                     //K8S-MASTER的虚IP
    private String etcdHome;                    //ETCD安装路径

    public Float getCpuQuota() {
        return cpuQuota;
    }

    public void setCpuQuota(Float cpuQuota) {
        this.cpuQuota = cpuQuota;
    }

    public Float getMemoryQuota() {
        return memoryQuota;
    }

    public void setMemoryQuota(Float memoryQuota) {
        this.memoryQuota = memoryQuota;
    }

    public List<ResourceVo> getEtcds() {
        return etcds;
    }

    public void setEtcds(List<ResourceVo> etcds) {
        this.etcds = etcds;
    }

    public List<ResourceVo> getK8sMasters() {
        return k8sMasters;
    }

    public void setK8sMasters(List<ResourceVo> k8sMasters) {
        this.k8sMasters = k8sMasters;
    }

    public List<ResourceVo> getK8sWorkers() {
        return k8sWorkers;
    }

    public void setK8sWorkers(List<ResourceVo> k8sWorkers) {
        this.k8sWorkers = k8sWorkers;
    }

    public List<ClusterElasticVo> getElastic() {
        return elastic;
    }

    public void setElastic(List<ClusterElasticVo> elastic) {
        this.elastic = elastic;
    }

    public List<ClusterQuotaVo> getQuota() {
        return quota;
    }

    public void setQuota(List<ClusterQuotaVo> quota) {
        this.quota = quota;
    }

    public String getHomePath() {
        return homePath;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }

    public String getEtcdHome() {
        return etcdHome;
    }

    public void setEtcdHome(String etcdHome) {
        this.etcdHome = etcdHome;
    }

    public String getFloatIp() {
        return floatIp;
    }

    public void setFloatIp(String floatIp) {
        this.floatIp = floatIp;
    }

    public List<String> getK8sMasterIpList() {
	    if (this.k8sMasters != null) {
		    return this.k8sMasters.stream().map(f -> f.getIpAddress()).collect(Collectors.toList());
        }
	    return Collections.EMPTY_LIST;
    }

    public List<String> getK8sWorkIpList() {
	    if (this.k8sWorkers != null) {
		    return this.k8sWorkers.stream().map(f -> f.getIpAddress()).collect(Collectors.toList());
        }
	    return Collections.EMPTY_LIST;
    }

    public List<String> getEtcdIpList() {
	    if (this.etcds != null) {
		    return etcds.stream().map(f -> f.getIpAddress()).collect(Collectors.toList());
        }
	    return Collections.EMPTY_LIST;
    }
}
