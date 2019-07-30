package com.newland.paas.ee.vo;

import com.newland.paas.ee.vo.cluster.ZoneDetailVo;

import java.util.List;


/**
 * 集群实例详情抽象类
 * Created by zkq on 2018-07-02.
 */
public class ClusterInstanceDetailInfo {

    /**
     * 集群ID
     */
    private Long clusterId;

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群masterFloatIp
     */
    private String masterFloatIp;

    private ZoneDetailVo detailVo;

    //如下参数fluentd使用
    /**
     * Kafka服务地址列表
     */
    private String kafkaBrokerUrl;
    /**
     * es 服务ip列表
     */
    private List<String> es;
    /**
     * 集群日志ID
     */
    private Long logClusterId;
    /**
     * 集群ID，给苏研用的，下划线格式也是苏研那边规定这个参数的，按驼峰不应该这命名的
     */
    private String cluster_uid;
    /**
     * k8s日志采集存放，fluentId使用
     */
    private String k8sLogPath;
    
    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getMasterFloatIp() {
	
		return masterFloatIp;
	}

	public void setMasterFloatIp(String masterFloatIp) {
	
		this.masterFloatIp = masterFloatIp;
	}

	public ZoneDetailVo getDetailVo() {
	
		return detailVo;
	}

	public void setDetailVo(ZoneDetailVo detailVo) {
		this.detailVo = detailVo;
	}

    public String getKafkaBrokerUrl() {
        return kafkaBrokerUrl;
    }

    public void setKafkaBrokerUrl(String kafkaBrokerUrl) {
        this.kafkaBrokerUrl = kafkaBrokerUrl;
    }

    public Long getLogClusterId() {
        return logClusterId;
    }

    public void setLogClusterId(Long logClusterId) {
        this.logClusterId = logClusterId;
    }

    public List<String> getEs() {
        return es;
    }

    public void setEs(List<String> es) {
        this.es = es;
    }

    public String getCluster_uid() {
        return cluster_uid;
    }

    public void setCluster_uid(String cluster_uid) {
        this.cluster_uid = cluster_uid;
    }

    public String getK8sLogPath() {
        return k8sLogPath;
    }

    public void setK8sLogPath(String k8sLogPath) {
        this.k8sLogPath = k8sLogPath;
    }
}
