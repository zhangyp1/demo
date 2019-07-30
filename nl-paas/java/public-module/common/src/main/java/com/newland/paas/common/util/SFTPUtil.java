package com.newland.paas.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * 
 * @ClassName: SFTPUtil
 * @Description: sftp连接工具类
 * @author cjw
 * @date 2018年07月19日
 */
public class SFTPUtil {

    private static final Log LOG = LogFactory.getLogger(SFTPUtil.class);

    private ChannelSftp sftp;

    private Session session;
    /** FTP 登录用户名 */
    private String username;
    /** FTP 登录密码 */
    private String password;
    /** 私钥 */
    private String privateKey;
    /** FTP 服务器地址IP地址 */
    private String host;
    /** FTP 端口 */
    private int port;

    public SFTPUtil() {}

    /**
     * 构造基于密码认证的sftp对象
     * 
     * @param userName
     * @param password
     * @param host
     * @param port
     */
    public SFTPUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     * 
     * @param userName
     * @param host
     * @param port
     * @param privateKey
     */
    public SFTPUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    /**
     * 连接sftp服务器
     * 
     * @throws Exception
     */
    public void login() throws JSchException {
        JSch jsch = new JSch();
        if (privateKey != null) {
            jsch.addIdentity(privateKey);// 设置私钥
            LOG.info(LogProperty.LOGTYPE_DETAIL, "sftp connect,path of private key file：{}" + privateKey);
        }

        LOG.info(LogProperty.LOGTYPE_DETAIL, "sftp connect by host:{} username:{}" + host + "<<<>>>" + username);
        session = jsch.getSession(username, host, port);

        LOG.info(LogProperty.LOGTYPE_DETAIL, "Session is build");

        if (password != null) {
            session.setPassword(password);
        }
        LOG.info(LogProperty.LOGTYPE_DETAIL, "password>>>>" + password);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        session.setConfig(config);
        session.connect();

        LOG.info(LogProperty.LOGTYPE_DETAIL, "Session is connected");

        Channel channel = session.openChannel("sftp");
        channel.connect();

        LOG.info(LogProperty.LOGTYPE_DETAIL, "channel is connected");

        sftp = (ChannelSftp) channel;

        LOG.info(LogProperty.LOGTYPE_DETAIL,
            String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                LOG.info(LogProperty.LOGTYPE_DETAIL, "sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                LOG.info(LogProperty.LOGTYPE_DETAIL, "sshSession is closed already");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     * 
     * @param directory 上传到该目录
     * @param sftpFileName sftp端文件名
     * @param in 输入流
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), "directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        // 上传目标文件(覆盖式)
        sftp.put(input, sftpFileName, ChannelSftp.OVERWRITE);
        LOG.info(LogProperty.LOGTYPE_DETAIL, "file:{} is upload successful>>>>>" + sftpFileName);
    }

    /**
     * 创建以当前用户默认路径
     * 
     * @param directory 路径 home/abc
     * @throws SftpException
     */
    public void createCurrentDir(String directory) throws SftpException {
        if (directory.indexOf("/") == 0) {
            directory = directory.replaceFirst("/", "");
        }
        String[] pathArry = directory.split("/");
        for (String path : pathArry) {
            if (path.equals("")) {
                continue;
            }
            try {
                sftp.cd(path);
            } catch (Exception e) {
                LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
                // 建立目录
                sftp.mkdir(path);
                // 进入并设置为当前目录
                sftp.cd(path);
            }
        }
    }

    /**
     * 创建一个以根目录的文件夹
     * 
     * @param directory 路径 /home/abc
     * @throws SftpException
     */
    public void createRootDir(String directory) throws SftpException {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "createRootDir>>>>>" + directory);
        if (isDirExist(directory)) {
            sftp.cd(directory);
            return;
        }
        String[] pathArry = directory.split("/");
        StringBuffer filePath = new StringBuffer("/");
        for (String path : pathArry) {
            if (path.equals("")) {
                continue;
            }
            filePath.append(path + "/");
            if (isDirExist(filePath.toString())) {
                sftp.cd(filePath.toString());
            } else {
                // 建立目录
                sftp.mkdir(filePath.toString());
                // 进入并设置为当前目录
                sftp.cd(filePath.toString());
            }
        }
        sftp.cd(filePath.toString());
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            sftp.cd(directory);
            isDirExistFlag = true;
        } catch (SftpException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), "directory is not exist");
            isDirExistFlag = false;
        }
        return isDirExistFlag;
    }

    /**
     * 上传单个文件
     * 
     * @param directory 上传到sftp目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     * 
     * @param directory 上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param byteArr 要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     * 
     * @param directory 上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param dataStr 待上传的数据
     * @param charsetName sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName)
        throws UnsupportedEncodingException, SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     * 
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveFile)
        throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        try {
            File file = new File(saveFile);
            FileOutputStream fos = new FileOutputStream(file);
            sftp.get(downloadFile, fos);
            LOG.info(LogProperty.LOGTYPE_DETAIL, "file:{} is download successful>>>>>" + downloadFile);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, e.getMessage(), e.getCause(), e.getMessage());
        }
    }

    /**
     * 下载文件
     * 
     * @param directory 下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        LOG.info(LogProperty.LOGTYPE_DETAIL,
            "directory:{},file:{} is download successful>>>>>directory:" + directory + " file:" + downloadFile);
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        byte[] fileData = IOUtils.toByteArray(is);
        return fileData;
    }

    /**
     * 删除文件
     * 
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 删除文件目录
     *
     * @param directory 要删除文件所在目录
     * @throws SftpException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void deleteDir(String directory) throws SftpException {
        if (isDirExist(directory)) {
            Vector<ChannelSftp.LsEntry> vector = sftp.ls(directory);
            if (vector.size() == 1) { // 文件，直接删除
                sftp.rm(directory);
            } else if (vector.size() == 2) { // 空文件夹，直接删除
                sftp.rmdir(directory);
            } else {
                String fileName = "";
                // 删除文件夹下所有文件
                for (ChannelSftp.LsEntry en : vector) {
                    fileName = en.getFilename();
                    if (".".equals(fileName) || "..".equals(fileName)) {
                        continue;
                    } else {
                        deleteDir(directory + (directory.endsWith("/") ? "" : "/") + fileName);
                    }
                }
                // 删除文件夹
                sftp.rmdir(directory);
            }
        } else {
            sftp.rm(directory);
        }
    }

    /**
     * 列出目录下的文件
     * 
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 列出目录下的文件
     * 
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Map<String, String> listFilesForMap(String directory) throws SftpException {
        Map<String, String> map = new HashMap<String, String>();
        Vector<?> vector = sftp.ls(directory);
        for (Object obj : vector) {
            if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry) obj).getFilename();
                map.put(fileName, fileName);
            }
        }
        return map;
    }

    /**
     * 递归列出所有目录和文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */

    public List<PlanPackageTree> listFilesTree(String directory) throws SftpException {
        List<PlanPackageTree> planPackageTreeList = new ArrayList<PlanPackageTree>();
        Vector<?> vector = sftp.ls(directory);
        for (Object obj : vector) {
            if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry) obj).getFilename();
                if (".".equals(fileName) || "..".equals(fileName)) {
                    continue;
                } else {
                    PlanPackageTree planPackageTree = new PlanPackageTree();
                    if (((ChannelSftp.LsEntry) obj).getAttrs().isDir()) {
                        // sftp.cd(fileName);
                        planPackageTree.setLabel(fileName);
                        planPackageTree.setChildren(listFilesTree(directory + fileName + "/"));
                        planPackageTreeList.add(planPackageTree);
                    } else {
                        planPackageTree.setLabel(fileName);
                        planPackageTree.setChildren(null);
                        planPackageTreeList.add(planPackageTree);
                    }
                }
            }
        }
        return planPackageTreeList;
    }

    public void downloadDirsAndFiles(String directory, String localPath) throws SftpException, FileNotFoundException {
        Vector<?> vector = sftp.ls(directory);
        for (Object obj : vector) {
            if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry) obj).getFilename();
                if (".".equals(fileName) || "..".equals(fileName)) {
                    continue;
                } else {
                    if (((ChannelSftp.LsEntry) obj).getAttrs().isDir()) {
                        // localPath = localPath+"/"+fileName;
                        // directory = directory+"/"+fileName;
                        File file = new File(localPath + "/" + fileName);
                        if (!file.exists()) {// 如果文件夹不存在
                            file.mkdirs();// 创建文件夹
                        }
                        downloadDirsAndFiles(directory + "/" + fileName, localPath + "/" + fileName);
                    } else {
                        download(directory, directory + "/" + fileName, localPath + "/" + fileName);
                    }
                }
            }
        }
    }

    /**
     * @描述: response页面下载
     * @param response
     * @param directory
     * @param downloadFile
     * @throws SftpException
     * @throws IOException
     * @创建人：zyp @创建时间：2019/04/09
     */
    public void responseDownload(HttpServletResponse response, String directory, String downloadFile)
        throws SftpException, IOException {
        LOG.info(LogProperty.LOGTYPE_DETAIL,
            "directory:{},file:{} is download successful>>>>>directory:" + directory + " file:" + downloadFile);
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = sftp.get(downloadFile);
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(downloadFile.getBytes("utf-8"), "ISO-8859-1"));
            os = response.getOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = is.read(data)) > 0) {
                os.write(data, 0, len);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }

    }

   /* public static void main(String[] args) throws SftpException, IOException {
        *//*Connection conn = new Connection("172.32.150.242", 22);
        boolean isAuthenticated = conn.authenticateWithPassword("root", "root123.");
        ch.ethz.ssh2.Session sess = conn.openSession();
        sess.execCommand("last");
        InputStream stdout = new StreamGobbler(sess.getStdout());
        
        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
        System.out.println("ExitCode: " + sess.getExitStatus());
        sess.close();
        conn.close();*//*

        SFTPUtil sftp = new SFTPUtil("root", "root123.", "172.32.150.242", 22);

        try {
            sftp.login();
            Map<String, String> map = sftp.listFilesForMap("/home/paas/ast_files/dmp_files/");
            System.out.println(map.toString());
            // sftp.deleteDir("/home/dragon/test/");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        // sftp.createRootDir("/aa/bb/");
        sftp.logout();
    }*/
}
