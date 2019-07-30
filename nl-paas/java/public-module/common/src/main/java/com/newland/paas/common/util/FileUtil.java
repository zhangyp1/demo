package com.newland.paas.common.util;

import com.newland.paas.common.exception.FileOperatorException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 文件处理
 *
 * @author Administrator
 */
public class FileUtil {

	private static final Log log = LogFactory.getLogger(FileUtil.class);

	static File file = null;

	/**
	 * 获得系统资源根目录，比如运行jar包的根目录
	 *
	 * @return
	 */
	public static String getRootPath() {
		URL url = FileUtil.class.getClassLoader().getResource("");
		if (url == null) {
			url = FileUtil.class.getClassLoader().getResource("/");
		}
		if (url != null) {
			return url.getPath();
		}
		String path = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		path = path.substring(0, path.lastIndexOf("/"));
		return path + "/";
	}

	/**
	 * 
	 * @param fileStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getFilePath(String fileStr) {
		Enumeration paths;
		URL url = null;
		try {
			paths = ClassLoader.getSystemResources(fileStr);
			if (paths.hasMoreElements()) {
				url = (URL) paths.nextElement();
				return url.getPath();
			}
			if (url == null) {
				url = FileUtil.class.getClassLoader().getResource(fileStr);
				if (url == null) {
					url = FileUtil.class.getClassLoader().getResource("/" + fileStr);
				}
			}
			if (url == null) {
				file = new File(fileStr);
				if (file != null) {
					return file.getPath();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url.getPath();
	}

	/**
	 * 写文件,不换行
	 *
	 * @param destFile
	 *            写文件的目标文件
	 * @param str
	 *            写入文件的字符串
	 * @throws Exception
	 */
	public static void write(String destFile, String str) throws IOException {
		FileOutputStream fos = null;
		FileChannel fc = null;
		try {
			fos = new FileOutputStream(destFile, true);
			fc = fos.getChannel();
			write(fc, str);
		} finally {
			closeOutStream(fos);
		}

	}

	/**
	 * 写文件,不换行
	 *
	 * @param destFile
	 *            写文件的目标文件
	 * @param str
	 *            写入文件的二进制数组
	 * @throws Exception
	 */
	public static void write(String destFile, byte[] str) throws IOException {
		FileOutputStream fos = null;
		FileChannel fc = null;
		try {
			fos = new FileOutputStream(destFile, true);
			fc = fos.getChannel();
			write(fc, str);
		} finally {
			closeOutStream(fos);
		}

	}

	/**
	 * 清空文件内容
	 *
	 * @param destFile
	 * @throws IOException
	 */
	public static void clearFile(String destFile) throws IOException {
		FileOutputStream fos = null;
		FileChannel fc = null;
		try {
			fos = new FileOutputStream(destFile, true);
			fc = fos.getChannel();
			// 将文件大小截为0
			fc.truncate(0);
		} finally {
			closeFileChannel(fc);
			closeOutStream(fos);
		}
	}

	/**
	 * @param fc
	 * @param standarCdr
	 * @throws IOException
	 * @Function:     write 
	 * @Description:  写文件（追加写入内容）
	 */
	public static void write(FileChannel fc, String standarCdr) throws IOException {

		ByteBuffer buffer = ByteBuffer.wrap(standarCdr.getBytes());

		while (buffer.hasRemaining()) {
			fc.write(buffer);
		}
	}

	/**
	 * @param fc
	 * @param bt
	 * @throws IOException
	 * @Function:     write 
	 * @Description:  写文件（追加写入内容）
	 */
	public static void write(FileChannel fc, byte[] bt) throws IOException {

		ByteBuffer buffer = ByteBuffer.wrap(bt);

		while (buffer.hasRemaining()) {
			fc.write(buffer);
		}
		ByteBuffer bf = ByteBuffer.wrap("\n".getBytes());

		while (bf.hasRemaining()) {
			fc.write(bf);
		}
	}

	/**
	 * 写文件，每行回车换行
	 *
	 * @param destFile
	 *            文件路径
	 * @param str
	 *            文件内容
	 * @throws IOException
	 */
	public static void writeSplitSeperate(String destFile, String str) throws IOException {

		FileOutputStream fos = null;
		FileChannel fc = null;
		try {
			fos = new FileOutputStream(destFile, true);
			fc = fos.getChannel();

			StringBuilder sb = new StringBuilder();
			sb.append(str).append(com.newland.paas.common.constant.Constant.LIST_ROW_SEPERATE);
			ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes());

			while (buffer.hasRemaining()) {
				fc.write(buffer);
			}
		} finally {
			if (fc != null) {
				fc.close();
			}
			if (fos != null) {
				fos.close();
			}

		}
	}

	/**
	 * 读取文件反回字符串
	 * 
	 * @throws IOException
	 */
	public static String readFileToString(String fileName) throws IOException {

		log.info(LogProperty.LOGTYPE_DETAIL, "fileName=" + fileName);

		StringBuffer sb = new StringBuffer();

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		try {
			File f = new File(fileName);
			if (f.exists()) {
				fileReader = new FileReader(f);
				bufferedReader = new BufferedReader(fileReader);
				while (true) {
					String s = bufferedReader.readLine();
					if (s == null)
						break;
					sb.append(s + "\n");
				}
			}
		} finally {
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (fileReader != null)
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return sb.toString();
	}

	public static String readClassPathFileToString(String fileName)  {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[1024];
		try {
			InputStreamReader isr = new InputStreamReader(is,"utf-8");
			int len = 0;
			while((len = isr.read(buffer))>0){
				sb.append(buffer,0,len);
			}
			return sb.toString();

		}catch (Exception ex){
			log.error(LogProperty.LOGTYPE_DETAIL,"",ex);
		}finally {
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * @throws IOException
	 * @Function:     delReadLine 
	 * @Description:  删除当前行
	 */
	public static void delReadLine(File file, String line) throws IOException {
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return;
		}

		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

		lines.remove(line);

		Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
	}

	/**
	 * 读取文件最后一行
	 *
	 * @param file
	 * @param charset
	 *            文件字符集
	 * @return
	 * @throws IOException
	 */
	public static String readLastLine(File file, String charset) throws IOException {
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return null;
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			long len = raf.length();
			if (len == 0L) {
				return "";
			} else {
				long pos = len - 1;
				while (pos > 0) {
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n') {
						break;
					}
				}
				if (pos == 0) {
					raf.seek(0);
				}
				byte[] bytes = new byte[(int) (len - pos)];
				raf.read(bytes);
				if (charset == null) {
					return new String(bytes);
				} else {
					return new String(bytes, charset);
				}
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}

	/**
	 * 复制单个文件(原名复制)
	 *
	 * @param oldPathFile
	 *            准备复制的文件源
	 * @param targetPath
	 *            目标目录
	 * @param flag
	 *            操作标识 0 move，1 copy （move 时删除源文件）
	 * @return
	 * @author zhuch
	 */
	public static void copyFileTo(String oldPathFile, String targetPath, int flag) throws IOException {
		try {
			// int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			String targetfile = targetPath + File.separator + oldfile.getName();

			// 目标目录不存在时创建
			File fileDir = new File(targetPath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(targetfile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
				if (0 == flag)
					oldfile.delete();
			} else {
				throw new IOException("[" + oldPathFile + "]文件不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定目录的文件名列表
	 *
	 * @param file
	 *            目标目录
	 * @param fileList
	 *            文件列表（返回参数）map<文件名，路径>
	 * @param recursiveFlag
	 *            递归标识 false时，只取当前目录的文件列表，true时，返回当前目录及所有子目录的文件列表
	 * @return
	 * @author zhuch
	 */
	public static void getFileNameList(File file, LinkedList<Map<String, String>> fileList, boolean recursiveFlag)
			throws IOException {
		if (!file.isDirectory()) {
			throw new IOException("不是目录！");
		} else {
			File[] t = file.listFiles();
			Map<String, String> map = null;
			for (int i = 0; i < t.length; i++) {
				if (t[i].isDirectory()) {
					// 如果是则执行tree递归，直到把此文件夹中所有文件输出为止
					if (recursiveFlag) {
						getFileNameList(t[i], fileList, recursiveFlag);
					}
				} else {
					map = new HashMap<String, String>();
					map.put(t[i].getName(), t[i].getPath());
					fileList.add(map);
				}
			}
		}
	}

	/**
	 * 目录下文件是否有文件
	 *
	 * @param dir
	 * @return true / false
	 * @throws IOException
	 */
	public static boolean ifFileExists(File dir) throws IOException {
		boolean flag = false;
		// 判断传入对象是否为一个文件夹对象
		if (!dir.isDirectory()) {
			throw new IOException("-_-!! is not dir!!");
		} else {
			File[] files = dir.listFiles();
			if (files.length > 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @param dir
	 * @throws FileOperatorException
	 * @Function:     createDir 
	 * @Description:  创建目录
	 */
	public static void createDir(String dir) throws FileOperatorException {
		Set<PosixFilePermission> perms = EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
				PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_READ);

		Path path = Paths.get(dir);

		// 工作目录不存在，创建目录及子目录
		try {
			Files.createDirectories(path, PosixFilePermissions.asFileAttribute(perms));
		} catch (Exception e) {
			// window系统下，不支持使用posix权限创建目录
			try {
				Files.createDirectories(path);
			} catch (Exception e1) {
				throw new FileOperatorException("", "创建目录:" + dir + "失败!", e1);
			}
		}
	}

	/**
	 * @param src
	 *            源文件路径
	 * @param target
	 *            目标文件路径
	 * @throws Exception
	 * @Function:     atomicMoveFile
	 * @Description:  原子移动文件
	 */
	public static void atomicMoveFile(Path src, Path target) throws Exception {

		try {

			Path targetDir = target.getParent();

			if (!Files.exists(targetDir)) {
				Files.createDirectories(targetDir);
			}

			Files.move(src, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (AtomicMoveNotSupportedException e) {
			// 当操作系统不支持原子移时，先将拷贝，最后删除源文件。
			copyFile(src, target, StandardCopyOption.REPLACE_EXISTING);
			File file = new File(src.toAbsolutePath().toString());
			file.deleteOnExit(); // 拷贝完成，删除
		}
	}

	/**
	 * @param srcPath
	 *            目标文件目录地址
	 * @throws Exception
	 * @Function:     createDirectory
	 * @Description:  创建具有读写权限的文件目录
	 */
	public static void createDirectories(Path srcPath) throws Exception {

		// 判断当前的操作系统类型
		// File.separator ==> On UNIX systems the value of this field is '/'; on
		// Microsoft Windows systems it is '\\'.
		if ("/".equalsIgnoreCase(File.separator)) {
			Set<PosixFilePermission> perms = EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
					PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_READ);

			Files.createDirectories(srcPath, PosixFilePermissions.asFileAttribute(perms));
		} else {
			Files.createDirectories(srcPath); // window系统下，不支持使用posix权限创建目录
		}
	}

	/**
	 * @param src
	 *            源文件路径
	 * @param target
	 *            目标文件路径
	 * @param options
	 *            选项
	 * @throws Exception
	 * @Function:     copyFile
	 * @Description:  拷贝文件
	 */
	public static void copyFile(Path src, Path target, StandardCopyOption... options) throws Exception {

		Path targetDir = target.getParent();

		if (!Files.exists(targetDir)) {
			Files.createDirectories(targetDir);
		}

		Files.copy(src, target, options);
	}

	/**
	 * @param file
	 *            文件路径
	 * @return
	 * @throws Exception
	 * @Function:     getBufferedReader
	 * @Description:  读取文件内容
	 */
	public static BufferedReader getBufferedReader(Path file, Charset cs) throws Exception {
		return Files.newBufferedReader(file, cs);
	}

	/**
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 * @Function:     bulidFileOutputStream
	 * @Description:  取得文件的FileOutputStream实例
	 */
	public static FileOutputStream getFileOutputStream(Path path) throws Exception {
		return new FileOutputStream(path.toFile(), true);
	}

	/**
	 * @param srcPath
	 *            待判断文件路径
	 * @param options
	 *            参数
	 * @return true：文件存在；false：文件不存在
	 * @throws SecurityException
	 * @Function:     isExistFileCheck
	 * @Description:  判断文件是否存在
	 */
	public static boolean isExistFileCheck(Path srcPath, LinkOption... options) throws SecurityException {
		return Files.exists(srcPath, options);
	}

	/**
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 * @Function:     getFileSize
	 * @Description:  获取文件大小，以字节为单位
	 */
	public static long getFileSize(Path path) throws Exception {
		return Files.size(path);
	}

	/**
	 * @param orgFileName
	 *            原文件名
	 * @param newFilename
	 *            新文件名
	 * @throws Exception
	 * @Function:     reFileName
	 * @Description:  文件重命名 当已经存在新的文件名时，将原文件的内容复制到新文件中，并且删除原文件。
	 */
	public static void reFileName(String orgFileName, String newFilename) throws Exception {

		File orgFile = new File(orgFileName);
		File newFile = new File(newFilename);

		if (!newFile.exists()) {
			orgFile.renameTo(newFile);
		} else {
			copyFile(Paths.get(orgFileName), Paths.get(newFilename), StandardCopyOption.REPLACE_EXISTING);
			orgFile.deleteOnExit(); // 删除原文件
		}
	}

	/**
	 * @param path
	 *            目标路径
	 * @throws Exception
	 * @Function:     createFile
	 * @Description:  创建文件
	 */
	public static boolean createFile(Path path) throws Exception {

		boolean result = false;

		try {

			Path dir = path.getParent();

			if (Files.notExists(dir)) {
				Files.createDirectories(dir);
			}

			Files.createFile(path);

			result = true;
		} catch (Exception e) {
			result = false;
			throw e;
		}

		return result;
	}

	/**
	 * @param pathStr
	 *            目标路径
	 * @throws Exception
	 * @Function:     createFile
	 * @Description:  创建文件
	 */
	public static boolean createFile(String pathStr) throws Exception {
		return createFile(Paths.get(pathStr));
	}

	/**
	 * @param out
	 * @throws Exception
	 * @Function:     closeOutStream
	 * @Description:  关闭FileOutputStream资源
	 */
	public static void closeOutStream(FileOutputStream out) throws IOException {
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	/**
	 * @param channel
	 * @throws Exception
	 * @Function:     closeFileChannel
	 * @Description:  关闭FileChannel资源
	 */
	public static void closeFileChannel(FileChannel channel) throws IOException {
		if (channel != null) {
			channel.close();
		}
	}

	/**
	 * @param path
	 * @param context
	 * @throws Exception
	 * @Function:     writeData
	 * @Description:  追加写文件
	 */
	public static void writeData(String path, String context) throws Exception {

		FileOutputStream out = null;

		try {

			Path srcPath = Paths.get(path);

			if (Files.notExists(srcPath)) {
				createFile(path);
			}

			out = getFileOutputStream(srcPath);

			out.write(context.getBytes());
		} catch (Exception e) {
			throw e;
		} finally {
			closeOutStream(out);
		}
	}

	/**
	 * @param filePath
	 *            删除文件路径
	 * @return true：删除成功；false：删除失败
	 * @throws NullPointerException
	 * @throws SecurityException
	 * @Function:     deleteFile
	 * @Description:  删除文件
	 */
	public static boolean deleteFile(String filePath) throws NullPointerException, SecurityException {

		boolean result = false;

		filePath = StringUtils.trimToNull(filePath);

		if (StringUtils.isBlank(filePath)) {
			return false;
		}

		File file = new File(filePath);

		result = deleteFile(file);

		return result;
	}

	/**
	 * @param file
	 *            待删除文件对象
	 * @return true：删除成功；false：删除失败
	 * @throws SecurityException
	 * @Function:     deleteFile
	 * @Description:  删除文件
	 */
	public static boolean deleteFile(File file) throws SecurityException {

		boolean result = false;

		// 判断文件是否存在，是否可执行
		if (file != null && file.exists()) {
			result = file.delete();
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * 向文件中的某个位置插入字符串 pos是插入的位置
	 *
	 * @param file
	 * @param pos
	 * @param insertContent
	 */
//	public static void insert(File file, int pos, String insertContent) {
//		RandomAccessFile raf = null;
//		FileOutputStream tmpOut = null;
//		FileInputStream tmpIn = null;
//		File tmp = null;
//		String uuid = Tools.getUUID();
//		try {
//			tmp = File.createTempFile("tmp_" + uuid, null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		tmp.deleteOnExit();
//		try {
//			raf = new RandomAccessFile(file, "rw");
//			tmpOut = new FileOutputStream(tmp);
//			tmpIn = new FileInputStream(tmp);
//			raf.seek(pos);// 首行的话是0
//			byte[] buf = new byte[1024];
//			int hasRead = 0;
//			while ((hasRead = raf.read(buf)) > 0) {
//				// 把原有内容读入临时文件
//				tmpOut.write(buf, 0, hasRead);
//			}
//			raf.seek(pos);
//			raf.write(insertContent.getBytes());
//			// 追加临时文件的内容
//			while ((hasRead = tmpIn.read(buf)) > 0) {
//				raf.write(buf, 0, hasRead);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (raf != null) {
//					raf.close();
//				}
//				if (tmpOut != null) {
//					tmpOut.close();
//				}
//				if (tmpIn != null) {
//					tmpIn.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

	/**
	 * 检查目录下是否有文件存在
	 *
	 * @param dir
	 *            目录路径
	 * @return
	 */
	public static boolean subFilesExists(String dir) {
		// 检测目录下是否有文件
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();
		if (files != null && files.length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除目录
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDir(String path) {
		File file = new File(path);
		return deleteDir(file);
	}

	/**
	 * 删除目录
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteDir(File file) {
		if (!file.exists())
			return false;
		if (file.isFile()) {
			file.delete();
			return true;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteDir(files[i]);
		}
		file.delete();
		return true;
	}

	/**
	 * 清空目录
	 * 
	 * @param path
	 * @return
	 * @throws FileOperatorException
	 */
	public static boolean clearDir(String path) throws FileOperatorException {
		if (deleteDir(path)) {
			createDir(path);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是二进制文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isBinary(File file) {
		boolean bBinary = false;
		try {
			FileInputStream fin = new FileInputStream(file);
			long len = file.length();
			for (int j = 0; j < (int) len; j++) {
				int t = fin.read();
				if (t < 32 && t != 9 && t != 10 && t != 13) {
					bBinary = true;
					break;
				}
			}
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bBinary;
	}

	/**
	 * 上传文件,会导致内存溢出
	 * 
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("uploadFile", "uploadFile上传开始时间>>>>>" + startTime);
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
		long endTime = System.currentTimeMillis();
		log.info("uploadFile", "uploadFile上传结束时间>>>>>" + endTime);
		log.info("uploadFile", "uploadFile上传总用时>>>>>" + String.valueOf(endTime - startTime) + "ms");
	}

	/**
	 * 上传文件，上传速度略慢
	 * 
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @throws IOException
	 */
	public static void uploadSingleFile(MultipartFile file, String filePath, String fileName) throws IOException {
		long startTime = System.currentTimeMillis();
		log.info("uploadSingleFile", "uploadSingleFile上传开始时间>>>>>" + startTime);
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		BufferedOutputStream stream = new BufferedOutputStream(out);
		int length = 0;
		byte[] buffer = new byte[1024];
		InputStream inputStream = file.getInputStream();
		while ((length = inputStream.read(buffer)) != -1) {
			stream.write(buffer, 0, length);
		}
		stream.flush();
		stream.close();
		out.flush();
		out.close();
		long endTime = System.currentTimeMillis();
		log.info("uploadSingleFile", "uploadSingleFile上传结束时间>>>>>" + endTime);
		log.info("uploadSingleFile", "uploadSingleFile上传总用时>>>>>" + String.valueOf(endTime - startTime) + "ms");
	}

	/**
	 * 效率高的文件上传，推荐的上传方法
	 * 
	 * @param file 上传的文件
	 * @param filePath 文件夹路径
	 * @param fileName 文件名称
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void uploadTransferToFile(MultipartFile file, String filePath, String fileName)
			throws IllegalStateException, IOException {
		long startTime = System.currentTimeMillis();
		log.info("uploadTransferToFile", "uploadTransferToFile上传开始时间>>>>>" + startTime);
		//创建文件夹
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		// 获取文件路径信息
		String fullPath = filePath + fileName;
		File newFile = new File(fullPath);
		// 上传文件主要方法
		file.transferTo(newFile);
		long endTime = System.currentTimeMillis();
		log.info("uploadTransferToFile", "uploadTransferToFile上传结束时间>>>>>" + endTime);
		log.info("uploadTransferToFile", "uploadTransferToFile上传总用时>>>>>" + String.valueOf(endTime - startTime) + "ms");
	}

	/**
	 * 重命名文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static String renameToUUID(String fileName) {
		// 获取UUID
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		// 获得文件后缀名称 String fileName = asynImg.getOriginalFilename();
		String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		// 生成最新的uuid文件名称
		String newFileName = uuid + "." + suffixName;
		return newFileName;
	}

	/**
	 * 验证是否是正确格式
	 * 
	 * @param contentType
	 *            输入的文件类型 如："f:\\a.jsp
	 * @param allowTypes
	 *            格式如：".txt", ".png", "gif", ".bmp"
	 * @return
	 */
	public static boolean isValid(String contentType, String... allowTypes) {
		if (null == contentType || "".equals(contentType)) {
			return false;
		}
		for (String type : allowTypes) {
			if (contentType.indexOf(type) > -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取上传文件的其他参数信息
	 * 
	 * @param request
	 * @return
	 */
	public static List<Map<String, Object>> getMapFilePath(HttpServletRequest request) {
		List<Map<String, Object>> listMapPath = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapPath = null;
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			// 解析request请求
			List<FileItem> list = upload.parseRequest(request);
			log.info(LogProperty.LOGTYPE_DETAIL, list.toString());
			if (null != list && list.size() > 0) {
				for (FileItem item : list) {
					// 是否是普通表单类型
					if (item.isFormField()) {
						mapPath = new HashMap<String, Object>();
						// 普通输入项数据的名
						String filedName = item.getFieldName();
						// 解决普通输入项的数据的中文乱码问题
						String filedValue = item.getString("UTF-8");// 普通输入项的值
						log.info(LogProperty.LOGTYPE_DETAIL, filedName + "/" + filedValue);
						mapPath.put(filedName, filedValue);
						listMapPath.add(mapPath);
					}
				}
			}
		} catch (Exception e) {
			log.info(LogProperty.LOGTYPE_DETAIL, e.getMessage());
			return null;
		}
		return listMapPath;
	}
	
	/**
	 * 保存字符串到文件
	 * 
	 * @param fileName 路径+文件名
	 * @param s 保存的值
	 * @param append 如果是true表示连续写文件，如果是false则表示新建文件，原来内空不保存
	 * @throws IOException 
	 */
	public static void write(String fileName, String s, boolean append) throws IOException {
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			buildPath(fileName);
			fw = new FileWriter(fileName, append);
			bw = new BufferedWriter(fw);
			bw.write(s);
		}finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 构建文件路径
	 * @param
	 */
	public static void buildPath(String fileName) {
		File file = new File(fileName);
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
	}

	/**
	 * 拷贝文件夹下的所有文件及文件夹
	 *
	 * @param oldPath  原路径
	 * @param newPath  目标路径
	 * @throws IOException
	 */
	public static void copyDir(String oldPath, String newPath) throws IOException {

		File oldFile = new File(oldPath );
		File newFile = new File(newPath );
		FileUtils.copyDirectory(oldFile,newFile);
	}

}
