package com.newland.paas.common.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author cjw
 * @ClassName: DockerUtil
 * @Description: Docker命令工具类
 * @date 2018年07月26日
 */

public class DockerUtil {

    private static final Log LOG = LogFactory.getLogger(DockerUtil.class);

    private Session session;
    private ChannelExec openChannel;
    StringBuffer consoleLog = new StringBuffer();
    /**
     * 登录用户名
     */
    private String userName;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 服务器地址IP地址
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    // 保存输出内容的容器
    private ArrayList<String> stdout;

    /**
     * 构造基于密码认证的对象
     *
     * @param userName
     * @param password
     * @param host
     * @param port
     */
    public DockerUtil(String userName, String password, String host, int port) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
        stdout = new ArrayList<String>();
    }

    /**
     * 连接远程服务器
     *
     * @throws IOException
     * @throws Exception
     */
    public void login() throws IOException {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(userName, host, port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");// 跳过公钥的询问
            session.setConfig(config);
            session.setPassword(password);
            session.connect(5000);// 设置连接的超时时间
            openChannel = (ChannelExec) session.openChannel("exec");

            LOG.info(LogProperty.LOGTYPE_DETAIL, "userName>>>>>" + userName + "<<<<host>>>>>" + host + "<<<<port>>>>"
                + port + "<<<<password>>>>" + password);

        } catch (JSchException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), "Cannot connect to specified server");
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "sshSession is login");
    }

    /**
     * 从本地文件导入为docker镜像
     *
     * @param pathFile 本地文件绝对路径
     * @param imageName 镜像名称
     * @throws IOException
     * @throws JSchException
     * @throws Exception
     */
    public void dockerImportImage(String pathFile, String imageName) {
        try {
            String command = "docker import " + pathFile + " " + imageName;
            LOG.info(LogProperty.LOGTYPE_DETAIL, "docker_import_command>>>>>" + command);
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream()));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * 从本地文件导入为docker镜像
     *
     * @param pathFile 本地文件绝对路径
     * @throws IOException
     * @throws JSchException
     * @throws Exception
     */
    public void dockerLoadImage(String pathFile) {
        try {
            String command = "docker load < " + pathFile;
            LOG.info(LogProperty.LOGTYPE_DETAIL, "docker_load_command>>>>>" + command);
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream()));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * 从本地文件导入为docker镜像
     *
     * @param pathFile 本地文件绝对路径
     * @param imageName 镜像名称
     * @throws IOException
     * @throws JSchException
     * @throws Exception
     */
    public void dockerTagImage(String imageNameOrVersion, String imageName) {
        try {
            String command = "docker tag " + imageNameOrVersion + " " + imageName;
            LOG.info(LogProperty.LOGTYPE_DETAIL, "docker_tag_command>>>>>" + command);
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream()));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * push 镜像
     *
     * @param imageName 镜像名称 @throws
     */
    public void dockerPushImage(String imageName) {
        try {
            // push镜像
            String command = "docker push " + imageName;
            LOG.info(LogProperty.LOGTYPE_DETAIL, "docker_push_command>>>>>" + command);
            // 删除本地镜像
            command = command + " ; " + "docker rmi " + imageName;
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream()));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * 执行docker命令
     *
     * @param pathFile 镜像文件路径
     * @param imageNameOrVersion 模块镜像名称版本
     * @param pushImageName 镜像推送文件名称
     * @param imageUserName 镜像用户名
     * @param imageUserPassword 镜像密码
     * @param serverIp harbor地址端口
     */
    public void dockerAllImage(String pathFile, String imageNameOrVersion, String pushImageName, String imageUserName,
        String imageUserPassword, String serverIp) {
        try {
            String commandLogin = "docker login -u " + imageUserName + " -p " + imageUserPassword + " " + serverIp;
            String commandLoad = "docker load < " + pathFile;
            String commandTag = "docker tag " + imageNameOrVersion + " " + pushImageName;
            String commandPush = "docker push " + pushImageName;
            String commandDel = "docker rmi " + imageNameOrVersion;
            String commandPushDel = "docker rmi " + pushImageName;
            String command = commandLogin + " ; " + commandLoad + " ; " + commandTag + " ; " + commandPush + " ; "
                + commandDel + " ; " + commandPushDel;
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream(), "utf-8"));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * 执行docker拉取推送镜像
     * 
     * @param userName 镜像仓库用户名
     * @param userPassword 镜像仓库密码
     * @param serverIp 镜像仓库地址
     * @param oldImageAddress 镜像旧地址
     * @param newImageAddress 镜像新地址
     */
    public void dockerAllPullImage(String userName, String userPassword, String serverIp, String oldImageAddress,
        String newImageAddress) {
        try {
            String commandLogin = "docker login -u " + userName + " -p " + userPassword + " " + serverIp;
            String commandLoad = "docker pull " + oldImageAddress;
            String commandTag = "docker tag " + oldImageAddress + " " + newImageAddress;
            String commandPush = "docker push " + newImageAddress;
            String commandDel = "docker rmi " + newImageAddress;
            String commandPushDel = "docker rmi " + oldImageAddress;
            String command = commandLogin + " ; " + commandLoad + " ; " + commandTag + " ; " + commandPush + " ; "
                + commandDel + " ; " + commandPushDel;
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream(), "utf-8"));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * 执行docker拉取推送镜像
     * 
     * @param userName 镜像仓库用户名
     * @param userPassword 镜像仓库密码
     * @param serverIp 镜像仓库地址
     * @param oldImageAddress 镜像旧地址
     * @param newImageAddress 镜像新地址
     */
    public void dockerAllPullImagePush(String userNameHarbor, String userPasswordHarbor, String serverIpHarbor,
        String userName, String userPassword, String serverIp, String oldImageAddress, String newImageAddress) {
        try {
            String commandLogin =
                "docker login -u " + userNameHarbor + " -p " + userPasswordHarbor + " " + serverIpHarbor;
            String commandLoad = "docker pull " + oldImageAddress;
            String commandTag = "docker tag " + oldImageAddress + " " + newImageAddress;
            String commandLoginPush = "docker login -u " + userName + " -p " + userPassword + " " + serverIp;
            String commandPush = "docker push " + newImageAddress;
            String commandDel = "docker rmi " + newImageAddress;
            String commandPushDel = "docker rmi " + oldImageAddress;
            String command = commandLogin + " ; " + commandLoad + " ; " + commandTag + " ; " + commandLoginPush + " ; "
                + commandPush + " ; " + commandDel + " ; " + commandPushDel;
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream(), "utf-8"));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * 执行docker命令
     *
     * @param command
     */
    public String execDockerCommand(String command) throws IOException, JSchException {
        openChannel.setCommand(command);
        openChannel.setInputStream(null);
        BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream(), "utf-8"));
        openChannel.connect();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
        StringBuffer result = new StringBuffer();
        // 接收远程服务器执行命令的结果
        String line;
        while ((line = input.readLine()) != null) {
            result.append(line);
        }
        input.close();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "command[" + command + "]result:" + result.toString());
        return result.toString();
    }

    /**
     * 执行docker 删除命令
     *
     * @param pushImageName 推送镜像文件
     * @param imageUserName 推送镜像用户
     * @param imageUserPassword 推送镜像用户密码
     * @param serverIp harbor的地址端口
     */
    public void dockerRmiImage(String pushImageName, String imageUserName, String imageUserPassword, String serverIp) {
        try {
            String commandLogin = "docker login -u " + imageUserName + " -p " + imageUserPassword + " " + serverIp;
            String commandRmi = "docker rmi " + pushImageName;
            String command = commandLogin + " ; " + commandRmi + " ; ";
            openChannel.setCommand(command);
            openChannel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(openChannel.getInputStream(), "utf-8"));
            openChannel.connect();
            LOG.info(LogProperty.LOGTYPE_DETAIL, "The remote command is >>>>> :" + command);
            // 接收远程服务器执行命令的结果
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();
        } catch (JSchException | IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "stdout>>>>>" + stdout);
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (openChannel != null && !openChannel.isClosed()) {
            openChannel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "sshSession is closed already");
    }

    // public static void main(String args[]) {
    // DockerUtil dockerUtil = new DockerUtil("root", "root123.", "172.32.149.68", 22);
    // try {
    // dockerUtil.login();
    // // dockerUtil.dockerLoadImage("/home/dragon/mysql_5.6.35.tar");
    // // dockerUtil.dockerTagImage("mysql:5.6.35","10.1.8.15:5000/nl/mysql:5.6.35");
    // // dockerUtil.dockerPushImage("10.1.8.15:5000/nl/mysql:5.6.35");
    // // dockerUtil.dockerImportImage("/home/dragon/mysql_5.6.35.tar", "10.1.8.15:5000/nl/mysql_5.6.35:5.6.35");
    // dockerUtil.dockerAllImage("/home/dragon/mysql_5.6.35.tar", "mysql:5.6.35",
    // "172.32.148.111:80/yw/mysql:5.6.35", "yw", "aA12345678", "172.32.148.111:80");
    // dockerUtil.logout();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
