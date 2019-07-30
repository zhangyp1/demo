package com.newland.paas.ee.vo;

import java.util.List;

/**
 * 苏研集群实例详细信息
 */
public class CmssK8sCluInstanceDetailInfo extends ClusterInstanceDetailInfo{

    private List<NodeInfo> kubemaster;

    private List<NodeInfo> kubenode;

    private List<String> etcd;

    private List<String> user_management;

    private List<String> resource_management;

    private List<String> appstore;

    private List<String> ingress;

    private List<String> webssh;

    private List<String> alertmanager;

    private List<String> openresty;

    private String harbor;

    //private List<String> es;  放到父类

    private String controller_cluster = "true";

    private String domain_uid;

    private String cpuQuota;

    private String memoryQuota;

    public List<NodeInfo> getKubemaster() {

        return kubemaster;
    }

    public void setKubemaster(List<NodeInfo> kubemaster) {

        this.kubemaster = kubemaster;
    }

    public List<NodeInfo> getKubenode() {

        return kubenode;
    }

    public void setKubenode(List<NodeInfo> kubenode) {

        this.kubenode = kubenode;
    }

    public List<String> getEtcd() {

        return etcd;
    }

    public void setEtcd(List<String> etcd) {

        this.etcd = etcd;
    }

    public List<String> getUser_management() {

        return user_management;
    }

    public void setUser_management(List<String> user_management) {

        this.user_management = user_management;
    }

    public List<String> getResource_management() {

        return resource_management;
    }

    public void setResource_management(List<String> resource_management) {

        this.resource_management = resource_management;
    }

    public List<String> getAppstore() {

        return appstore;
    }

    public void setAppstore(List<String> appstore) {

        this.appstore = appstore;
    }

    public List<String> getIngress() {

        return ingress;
    }

    public void setIngress(List<String> ingress) {

        this.ingress = ingress;
    }

    public List<String> getWebssh() {

        return webssh;
    }

    public void setWebssh(List<String> webssh) {

        this.webssh = webssh;
    }

    public List<String> getAlertmanager() {

        return alertmanager;
    }

    public void setAlertmanager(List<String> alertmanager) {

        this.alertmanager = alertmanager;
    }

    public List<String> getOpenresty() {

        return openresty;
    }

    public void setOpenresty(List<String> openresty) {

        this.openresty = openresty;
    }

    public String getHarbor() {

        return harbor;
    }

    public void setHarbor(String harbor) {

        this.harbor = harbor;
    }

    public String getController_cluster() {

        return controller_cluster;
    }

    public void setController_cluster(String controller_cluster) {

        this.controller_cluster = controller_cluster;
    }

    public String getDomain_uid() {

        return domain_uid;
    }

    public void setDomain_uid(String domain_uid) {

        this.domain_uid = domain_uid;
    }

    public String getCpuQuota() {

        return cpuQuota;
    }

    public void setCpuQuota(String cpuQuota) {

        this.cpuQuota = cpuQuota;
    }

    public String getMemoryQuota() {

        return memoryQuota;
    }

    public void setMemoryQuota(String memoryQuota) {

        this.memoryQuota = memoryQuota;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SuK8sCluInstanceDetailInfo [kubemaster=");
        builder.append(kubemaster);
        builder.append(", kubenode=");
        builder.append(kubenode);
        builder.append(", etcd=");
        builder.append(etcd);
        builder.append(", user_management=");
        builder.append(user_management);
        builder.append(", resource_management=");
        builder.append(resource_management);
        builder.append(", appstore=");
        builder.append(appstore);
        builder.append(", ingress=");
        builder.append(ingress);
        builder.append(", webssh=");
        builder.append(webssh);
        builder.append(", alertmanager=");
        builder.append(alertmanager);
        builder.append(", openresty=");
        builder.append(openresty);
        builder.append(", harbor=");
        builder.append(harbor);
        builder.append(", cpuQuota=");
        builder.append(cpuQuota);
        builder.append(", memoryQuota=");
        builder.append(memoryQuota);
        builder.append(", controller_cluster=");
        builder.append(controller_cluster);
        builder.append(", domain_uid=");
        builder.append(domain_uid);
        builder.append("]");
        return builder.toString();
    }

}
