package com.newland.paas.paasservice.sysmgr.utils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author:PanYang
 * Date:Created in 上午10:20 2018/9/12
 * Modified By:
 */
public class FtpFileUtil {

    private static final Log LOGGER = LogFactory.getLogger(FtpFileUtil.class);
    //端口号
    private static final int FTP_PORT = 21;

    /**
     * 上传文件
     * @param originFileName
     * @param input
     * @param ftpName
     * @param ftpPassword
     * @param ftpPath
     * @param ftpAddress
     * @return
     */
    public static boolean uploadFile(String originFileName, InputStream input, String ftpName,
                                     String ftpPassword, String ftpPath, String ftpAddress) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
        try {
            int reply;
            //连接FTP服务器
            ftp.connect(ftpAddress, FTP_PORT);
            // 登录
            ftp.login(ftpName, ftpPassword);
            ftp.enterLocalPassiveMode();
            reply = ftp.getReplyCode();
            LOGGER.info(LogProperty.LOGTYPE_CALL, "reply结果is "+reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                LOGGER.info(LogProperty.LOGTYPE_CALL, "审计日志ftp上传用户名或密码错误!");
                return success;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
//            ftp.makeDirectory(ftpPath);
            ftp.changeWorkingDirectory(ftpPath);
            ftp.storeFile(originFileName, input);
            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            LOGGER.info(LogProperty.LOGTYPE_CALL, "文件上传错误:" + e.getMessage());
            LOGGER.error(LogProperty.LOGTYPE_CALL, "文件上传错误:", e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

}
