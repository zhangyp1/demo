package com.newland.paas.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * @类名称：GzipUtil 
 * @类描述： 压缩gzip
 * @创建人：zyp 
 * @创建时间：2019/02/20
 */
public class GzipUtil {
	public static final Integer HANDLEBYTE = 1024;
	public static final Integer HANDLEREAD = -1;
	private static final String PATH = "/";
	private static Log log = LogFactory.getLogger(GzipUtil.class);

	/**
	 * @param entry
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @描述: 归档
	 * @创建人：zyp 
	 * @创建时间：2019/01/26
	 */
	public static String archive(String entry) throws IOException {
		File file = new File(entry);
		TarArchiveOutputStream tos = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file.getAbsolutePath() + ".tar");
			tos = new TarArchiveOutputStream(out);
			String base = file.getName();
			if (file.isDirectory()) {
				archiveDir(file, tos, base);
			} else {
				archiveHandle(tos, file, base);
			}
		} finally {
			if (null != tos) {
				tos.close();
			}
		}
		return file.getAbsolutePath() + ".tar";
	}

	/**
	 * @param file
	 * @param tos
	 * @param basePath
	 * @throws IOException
	 * @描述: 递归处理，准备好路径
	 * @创建人：zyp 
	 * @创建时间：2019/01/26
	 */
	private static void archiveDir(File file, TarArchiveOutputStream tos, String basePath) throws IOException {
		File[] listFiles = file.listFiles();
		if (listFiles.length < 1) {
			TarArchiveEntry entry = new TarArchiveEntry(basePath  + PATH);
			tos.putArchiveEntry(entry);
			tos.closeArchiveEntry();
		}
		for (File fi : listFiles) {
			if (fi.isDirectory()) {
				archiveDir(fi, tos, basePath + File.separator + fi.getName());
			} else {
				archiveHandle(tos, fi, basePath);
			}
		}
	}

	/**
	 * @param tos
	 * @param fi
	 * @param basePath
	 * @throws IOException
	 * @描述: 具体归档处理（文件）
	 * @创建人：zyp 
	 * @创建时间：2019/01/26
	 */
	private static void archiveHandle(TarArchiveOutputStream tos, File fi, String basePath) throws IOException {
		TarArchiveEntry tEntry = new TarArchiveEntry(basePath + File.separator + fi.getName());
		tEntry.setSize(fi.length());
		BufferedInputStream bis = null;
		try {
			tos.putArchiveEntry(tEntry);
			bis = new BufferedInputStream(new FileInputStream(fi));
			byte[] buffer = new byte[HANDLEBYTE];
			int read = HANDLEREAD;
			while ((read = bis.read(buffer)) != HANDLEREAD) {
				tos.write(buffer, 0, read);
			}
		} finally {
			if (null != bis) {
				bis.close();
			}
			tos.closeArchiveEntry(); // 这里必须写，否则会失败
		}
	}

	/**
	 * @param path
	 * @return
	 * @throws IOException
	 * @描述: 把tar包压缩成gz
	 * @创建人：zyp @创建时间：2019/01/26
	 */
	public static String compressArchive(String path) throws IOException {
		BufferedInputStream bis = null;
		GzipCompressorOutputStream gcos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			gcos = new GzipCompressorOutputStream(new BufferedOutputStream(new FileOutputStream(path + ".gz")));
			byte[] buffer = new byte[HANDLEBYTE];
			int read = HANDLEREAD;
			while ((read = bis.read(buffer)) != HANDLEREAD) {
				gcos.write(buffer, 0, read);
			}
		} finally {
			if (null != gcos) {
				gcos.close();
			}
			if (null != bis) {
				bis.close();
			}
		}
		return path + ".gz";
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		/**
		 * 需要压缩的文件夹
		 */
		String entry = "/Users/zhang/newland/pass/资料/2.3/1";
		String archive = archive(entry); // 生成tar包
		log.info(LogProperty.LOGTYPE_DETAIL, "生成tar包：" + archive);
		compressArchive(archive); // 生成gz包
		FileUtil.deleteDir(archive);
	}

}
