package com.newland.paas.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author: zhoufeilong
 * @Description:
 * @Date: 2017-11-06 19:58
 * @Modified By: zhoufeilong
 */
public class MD5Utils {

	/**
	 * 获取加密文件的MD5码
	 * 
	 * @param file
	 *            加密文件
	 * @return
	 */
	public static String getMd5ByFile(String fileName) {
		String value = null;
		FileInputStream in = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				in = new FileInputStream(file);
				value = DigestUtils.md5Hex(in);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	/**
	 * md5字符串
	 * 
	 * @param inputStr
	 * @return
	 */
	public static String EncoderByMd5(String inputStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = null;
		md5 = MessageDigest.getInstance("MD5");
		byte[] bytes = inputStr.getBytes("UTF-8");
		byte[] md5Bytes = md5.digest(bytes);
		return convertToHexString(md5Bytes);
	}

	/**
	 * 字节数组转十六进制
	 * 
	 * @param md5Bytes
	 * @return
	 */
	private static String convertToHexString(byte[] md5Bytes) {
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int value = ((int) md5Bytes[i]) & 0xff;
			if (value < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(value));
		}
		return hexValue.toString();
	}
}
