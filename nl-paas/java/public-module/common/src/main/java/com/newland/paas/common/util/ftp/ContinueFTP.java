package com.newland.paas.common.util.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.newland.paas.common.util.IOUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * 支持断点续传的FTP实用类
 * 
 * @description 实现基本断点上传下载
 * @description 实现上传下载进度汇报
 * @description 实现中文目录创建及中文文件创建，添加对于中文的支持
 * @author cjw
 */
public class ContinueFTP {

	private static final Log LOG = LogFactory.getLogger(ContinueFTP.class);

	public FTPClient ftpClient = null;

	public ContinueFTP() {
		// 设置将过程中使用到的命令输出到控制台
		// this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname 主机名
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return 是否连接成功
	 * @throws IOException 异常
	 */
	public boolean connect(String hostname, int port, String username, String password) throws Exception {
		ftpClient = new FTPClient();
		ftpClient.setRemoteVerificationEnabled(false); // 取消服务器获取自身Ip地址和提交的host进行匹配，否则当不一致时会报异常。
		ftpClient.setControlEncoding("utf-8"); // 在连接之前设置编码类型为utf-8
		try {
			LOG.info(LogProperty.LOGTYPE_DETAIL,
					"hostname>>" + hostname + "port>>" + port + "username>>" + username + "password>>" + password);
			ftpClient.connect(hostname, port);
		} catch (Exception e) {
			LOG.error(LogProperty.LOGTYPE_DETAIL, "99998", e, "登陆异常，请检查主机端口");
			throw new Exception("登陆异常，请检查主机端口");
		}
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				LOG.info(LogProperty.LOGTYPE_DETAIL, "登陆成功");
				LOG.info(LogProperty.LOGTYPE_DETAIL, "【initFtpClient】: 成功登录服务器,被动模式主机：" + ftpClient.getPassiveHost() + ":"
						+ ftpClient.getPassivePort());
				LOG.info(LogProperty.LOGTYPE_DETAIL, "【initFtpClient】: 成功登录服务器,主动模式主机：" + ftpClient.getRemoteAddress() + ":"
						+ ftpClient.getRemotePort());
				LOG.info(LogProperty.LOGTYPE_DETAIL, "【initFtpClient】: 成功登录服务器,本地主机：" + ftpClient.getLocalAddress() + ":"
						+ ftpClient.getLocalPort());
				LOG.info(LogProperty.LOGTYPE_DETAIL, "【initFtpClient】: 成功登录服务器,返回代码：" + ftpClient.getReplyCode() + ",显示状态" + ftpClient.getStatus());
				return true;
			} else {
				LOG.info(LogProperty.LOGTYPE_DETAIL, "登陆异常，请检查密码账号");
				throw new Exception("登陆异常，请检查密码账号");
			}
		} else {
			LOG.info(LogProperty.LOGTYPE_DETAIL, "登陆异常");
			throw new Exception("登陆异常");
		}
	}

	/**
	 * 获取文件列表
	 * 
	 * @param filedir 文件地址
	 * @return 返回
	 * @throws IOException io异常
	 */
	public String[] getFileList(String filedir) throws IOException {
		LOG.info(LogProperty.LOGTYPE_DETAIL, "设置被动enterLocalPassiveMode开始");
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		LOG.info(LogProperty.LOGTYPE_DETAIL, "设置被动enterLocalPassiveMode结束");
		//由于apache不支持中文语言环境，通过定制类解析中文日期类型  
		ftpClient.configure(new FTPClientConfig("com.newland.paas.common.util.ftp.UnixFTPEntryParser"));
		// 获取远程文件
		FTPFile[] files = ftpClient.listFiles(filedir);
		LOG.info(LogProperty.LOGTYPE_DETAIL, "获取成功");
		String[] sfiles = null;
		if (files != null) {
			sfiles = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				sfiles[i] = files[i].getName();
			}
		}
		return sfiles;
	}

	/**
	 * 从FTP服务器上下载文件,不支持断点续传，上传百分比汇报
	 * 
	 * @param remote 远程文件路径
	 * @param local 本地文件路径
	 * @return 上传的状态
	 * @throws IOException 异常
	 */
	public String downloadFile(String remote, String local, String nameUUID) throws IOException {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		//由于apache不支持中文语言环境，通过定制类解析中文日期类型  
		ftpClient.configure(new FTPClientConfig("com.newland.paas.common.util.ftp.UnixFTPEntryParser"));
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		
		String result = "";
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(remote);
		if (files.length != 1) {
			LOG.info(LogProperty.LOGTYPE_DETAIL, "远程文件不存在");
			return DownloadStatusEnum.REMOTE_FILE_NOEXIST.code;
		}
		long lRemoteSize = files[0].getSize();
		File file = new File(local);
		// 本地存在文件，进行断点下载
		if (file.exists()) {
			file.delete();
		}
		// 下载
		File f = new File(local);
		OutputStream out = new FileOutputStream(f);
		InputStream in = ftpClient.retrieveFileStream(remote);
		try {
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			long process = 0;
			long localSize = 0L;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0) {
						// 更新文件下载进度,值存放在process变量中
						FtpConstans.processStatus.put(nameUUID, String.valueOf(process));
						LOG.info(LogProperty.LOGTYPE_DETAIL, "下载进度：" + process);
					}
				}
			}
		} catch (Exception e) {
			throw new IOException("下载错误");
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
		boolean upNewStatus = ftpClient.completePendingCommand();
		if (upNewStatus) {
			result = DownloadStatusEnum.DOWNLOAD_NEW_SUCCESS.code;
		} else {
			result = DownloadStatusEnum.DOWNLOAD_NEW_FAILED.code;
		}
		return result;
	}

	/**
	 * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
	 * 
	 * @param remote 远程文件路径
	 * @param local 本地文件路径
	 * @return 上传的状态
	 * @throws IOException 异常
	 */
	public String download(String remote, String local) throws IOException {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		String result = "";
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("utf-8"), "iso-8859-1"));
		if (files.length != 1) {
			LOG.info(LogProperty.LOGTYPE_DETAIL, "远程文件不存在");
			return DownloadStatusEnum.REMOTE_FILE_NOEXIST.code;
		}
		long lRemoteSize = files[0].getSize();
		File f = new File(local);
		// 本地存在文件，进行断点下载
		if (f.exists()) {
			long localSize = f.length();
			// 判断本地文件大小是否大于远程文件大小
			if (localSize >= lRemoteSize) {
				LOG.info(LogProperty.LOGTYPE_DETAIL, "本地文件大于远程文件，下载中止");
				return DownloadStatusEnum.LOCAL_BIGGER_REMOTE.code;
			}
			// 进行断点续传，并记录状态
			FileOutputStream out = new FileOutputStream(f, true);
			// 找出本地已经接收了多少
			ftpClient.setRestartOffset(localSize);
			InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("utf-8"), "iso-8859-1"));
			try {
				byte[] bytes = new byte[1024];
				// 总的进度
				long step = lRemoteSize / 100;
				long process = localSize / step;
				int c;
				while ((c = in.read(bytes)) != -1) {
					out.write(bytes, 0, c);
					localSize += c;
					long nowProcess = localSize / step;
					if (nowProcess > process) {
						process = nowProcess;
						if (process % 10 == 0) {
							// 更新文件下载进度,值存放在process变量中
							LOG.info(LogProperty.LOGTYPE_DETAIL, "下载进度：" + process);
						}
					}
				}
			} catch (Exception e) {
				throw new IOException("下载错误");
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
			// 确认是否全部下载完毕
			boolean isDo = ftpClient.completePendingCommand();
			if (isDo) {
				result = DownloadStatusEnum.DOWNLOAD_FROM_BREAK_SUCCESS.code;
			} else {
				result = DownloadStatusEnum.DOWNLOAD_FROM_BREAK_FAILED.code;
			}
		} else {
			OutputStream out = new FileOutputStream(f);
			InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("utf-8"), "iso-8859-1"));
			try {
				byte[] bytes = new byte[1024];
				long step = lRemoteSize / 100;
				long process = 0;
				long localSize = 0L;
				int c;
				while ((c = in.read(bytes)) != -1) {
					out.write(bytes, 0, c);
					localSize += c;
					long nowProcess = localSize / step;
					if (nowProcess > process) {
						process = nowProcess;
						if (process % 10 == 0) {
							// 更新文件下载进度,值存放在process变量中
							LOG.info(LogProperty.LOGTYPE_DETAIL, "下载进度：" + process);
						}
					}
				}
			} catch (Exception e) {
				throw new IOException("下载错误");
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
			boolean upNewStatus = ftpClient.completePendingCommand();
			if (upNewStatus) {
				result = DownloadStatusEnum.DOWNLOAD_NEW_SUCCESS.code;
			} else {
				result = DownloadStatusEnum.DOWNLOAD_NEW_FAILED.code;
			}
		}
		return result;
	}

	/**
	 * 从FTP服务器上下载文件
	 * 
	 * @param remote 远程文件路径
	 * @param local 本地文件路径
	 * @return 上传的状态
	 * @throws Exception 
	 */
	public byte[] bytedownload(String remote) throws Exception {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("utf-8"), "iso-8859-1"));
		if (files.length != 1) {
			LOG.info(LogProperty.LOGTYPE_DETAIL, "远程文件不存在");
			throw new Exception("远程文件不存在");
		}
		InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("utf-8"), "iso-8859-1"));
	    byte[] fileData = IOUtils.toByteArray(in);
		return fileData;
	}
	
	/**
	 * 从FTP服务器上下载文件
	 * 
	 * @param remote 远程文件路径
	 * @param local 本地文件路径
	 * @return 上传的状态
	 * @throws Exception 
	 */
	public InputStream Inputbytedownload(String remote) throws Exception {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(remote);
		if (files.length != 1) {
			LOG.info(LogProperty.LOGTYPE_DETAIL, "远程文件不存在");
			throw new Exception("远程文件不存在");
		}
		return ftpClient.retrieveFileStream(remote);
	}
	/**
	 * 从FTP服务器上下载文件
	 * 
	 * @param remote 远程文件路径
	 * @param local 本地文件路径
	 * @return 上传的状态
	 * @throws Exception 
	 */
	public boolean downfile(String remote) throws Exception {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("utf-8"), "iso-8859-1"));
		if (files.length != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param local 本地文件名称，绝对路径
	 * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	public String upload(String local, String remote) throws IOException {
		// 设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("utf-8");
		String result;
		// 对远程目录的处理
		String remoteFileName = remote;
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			// 创建服务器远程目录结构，创建失败直接返回
			if (this.createDirecroty(remote, ftpClient) == UploadStatusEnum.CREATE_DIRECTORY_FAIL.name) {
				return UploadStatusEnum.CREATE_DIRECTORY_FAIL.code;
			}
		}
		// 检查远程是否存在文件
		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("utf-8"), "iso-8859-1"));
		if (files.length == 1) {
			long remoteSize = files[0].getSize();
			File f = new File(local);
			long localSize = f.length();
			if (remoteSize == localSize) {
				return UploadStatusEnum.FILE_EXITS.code;
			} else if (remoteSize > localSize) {
				return UploadStatusEnum.REMOTE_BIGGER_LOCAL.code;
			}

			// 尝试移动文件内读取指针,实现断点续传
			result = this.uploadFile(remoteFileName, f, ftpClient, remoteSize);
			// 如果断点续传没有成功，则删除服务器上文件，重新上传
			if (result == UploadStatusEnum.UPLOAD_FROM_BREAK_FAILED.code) {
				if (!ftpClient.deleteFile(remoteFileName)) {
					return UploadStatusEnum.DELETE_REMOTE_FAILD.code;
				}
				result = this.uploadFile(remoteFileName, f, ftpClient, 0);
			}
		} else {
			result = this.uploadFile(remoteFileName, new File(local), ftpClient, 0);
		}
		return result;
	}

	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException 异常
	 */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote 远程服务器文件绝对路径
	 * @param ftpClient FTPClient对象
	 * @return 目录创建是否成功
	 * @throws IOException 异常
	 */
	public String createDirecroty(String remote, FTPClient ftpClient) throws IOException {
		String status = UploadStatusEnum.CREATE_DIRECTORY_SUCCESS.code;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(new String(directory.getBytes("utf-8"), "iso-8859-1"))) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("utf-8"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						LOG.info(LogProperty.LOGTYPE_DETAIL, "创建目录失败");
						return UploadStatusEnum.CREATE_DIRECTORY_FAIL.code;
					}
				}
				start = end + 1;
				end = directory.indexOf("/", start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	/**
	 * 上传文件到服务器,新上传和断点续传
	 * 
	 * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变
	 * @param localFile 本地文件File句柄，绝对路径
	 * @param processStep 需要显示的处理进度步进值
	 * @param ftpClient FTPClient引用
	 * @return
	 * @throws IOException
	 */
	public String uploadFile(String remoteFile, File localFile, FTPClient ftpClient, long remoteSize) throws IOException {
		String status;
		// 显示进度的上传
		long step = localFile.length() / 100;
		long process = 0;
		long localreadbytes = 0L;
		RandomAccessFile raf = new RandomAccessFile(localFile, "r");
		OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("utf-8"), "iso-8859-1"));
		// 断点续传
		if (remoteSize > 0) {
			ftpClient.setRestartOffset(remoteSize);
			process = remoteSize / step;
			raf.seek(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] bytes = new byte[1024];
		int c;
		while ((c = raf.read(bytes)) != -1) {
			out.write(bytes, 0, c);
			localreadbytes += c;
			if (localreadbytes / step != process) {
				process = localreadbytes / step;
				// 汇报上传状态
				LOG.info(LogProperty.LOGTYPE_DETAIL, "上传进度:" + process);
			}
		}
		out.flush();
		raf.close();
		out.close();
		boolean result = ftpClient.completePendingCommand();
		if (remoteSize > 0) {
			status = result ? UploadStatusEnum.UPLOAD_FROM_BREAK_SUCCESS.code
					: UploadStatusEnum.UPLOAD_FROM_BREAK_FAILED.code;
		} else {
			status = result ? UploadStatusEnum.UPLOAD_NEW_FILE_SUCCESS.code
					: UploadStatusEnum.UPLOAD_NEW_FILE_FAILED.code;
		}
		return status;
	}
}
