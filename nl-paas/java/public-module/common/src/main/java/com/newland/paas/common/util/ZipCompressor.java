package com.newland.paas.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

/**
 * @类名称：ZipCompressor 
 * @类描述： 压缩zip
 * @创建人：zyp 
 * @创建时间：2019/02/20
 */
public class ZipCompressor {
	private static Log log = LogFactory.getLogger(ZipCompressor.class);
	static final int BUFFER = 8192;
	private File zipFile;

	public ZipCompressor(String pathName) {
		zipFile = new File(pathName);
	}

	/**
	 * @描述: 压缩文件目录
	 * @param pathName
	 * @throws IOException
	 * @创建人：zyp @创建时间：2019/03/07
	 */
	public void compress(String... pathName) throws IOException {
		ZipOutputStream out = null;
		FileOutputStream fileOutputStream = null;
		CheckedOutputStream cos = null;
		try {
			fileOutputStream = new FileOutputStream(zipFile);
			cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			out = new ZipOutputStream(cos);
			String basedir = "";
			for (int i = 0; i < pathName.length; i++) {
				compress(new File(pathName[i]), out, basedir);
			}
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * @描述: 压缩文件
	 * @param srcPathName
	 * @throws IOException
	 * @创建人：zyp @创建时间：2019/03/07
	 */
	public void compress(String srcPathName) throws IOException {
		File file = new File(srcPathName);
		FileOutputStream fileOutputStream = null;
		CheckedOutputStream cos = null;
		ZipOutputStream out = null;
		String basedir = "";
		if (!file.exists()) {
			throw new RuntimeException(srcPathName + "不存在！");
		}
		try {
			fileOutputStream = new FileOutputStream(zipFile);
			cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			out = new ZipOutputStream(cos);
			compress(file, out, basedir);

		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	private void compress(File file, ZipOutputStream out, String basedir) throws IOException {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			log.info(LogProperty.LOGTYPE_DETAIL, "压缩：" + basedir + file.getName());
			this.compressDirectory(file, out, basedir);
		} else {
			log.info(LogProperty.LOGTYPE_DETAIL, "压缩：" + basedir + file.getName());
			this.compressFile(file, out, basedir);
		}
	}

	/**
	 * 压缩一个目录
	 * 
	 * @throws IOException
	 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir) throws IOException {
		if (!dir.exists()) {
			return;
		}
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/**
	 * 压缩一个文件
	 * 
	 * @throws IOException
	 */
	private void compressFile(File file, ZipOutputStream out, String basedir) throws IOException {
		BufferedInputStream bis = null;

		if (!file.exists()) {
			return;
		}
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte[] data = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}

		} finally {
			if (null != bis) {
				bis.close();
			}
		}
	}

}