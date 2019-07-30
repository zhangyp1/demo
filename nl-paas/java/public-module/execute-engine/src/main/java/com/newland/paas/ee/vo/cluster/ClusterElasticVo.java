package com.newland.paas.ee.vo.cluster;

/**
 * @author zkq
 * @Description com.newland.paas.ee.vo.cluster.ClusterElasticVo
 * @Date 2018/6/27
 */
public class ClusterElasticVo{
    private Long clusterId;             //弹性策略的集群ID
    private String name;                //弹性策略名称
    private String type;                //弹性策略弹性策略类型
    private String executeType;         //弹性策略执行动作
    private Integer executeNum;         //弹性策略执行数量

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public Integer getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(Integer executeNum) {
        this.executeNum = executeNum;
    }
}
