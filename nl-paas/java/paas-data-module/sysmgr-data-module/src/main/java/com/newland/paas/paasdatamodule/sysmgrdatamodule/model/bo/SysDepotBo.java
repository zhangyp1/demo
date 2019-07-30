package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import java.util.Objects;

/**
 * @program: paas-all
 * @description: 仓库信息对象
 * @author: Frown
 * @create: 2018-08-07 13:58
 **/
public class SysDepotBo {
    // 主地址
    private String ipPort;
    // 子路径
    private String subPath;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 类型
    private String type;

    public SysDepotBo() {
    }

    public SysDepotBo(String ipPort, String subPath, String username, String password, String type) {
        this.ipPort = ipPort;
        this.subPath = subPath;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysDepotBo that = (SysDepotBo) o;
        return Objects.equals(ipPort, that.ipPort) &&
                Objects.equals(subPath, that.subPath) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ipPort, subPath, username, password);
    }
}
