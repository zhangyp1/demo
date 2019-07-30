package com.newland.paas.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class CheckImagesFormatUtil {

	/**
	 * 图片后缀的格式检验
	 * 
	 * @param file
	 *            文件
	 * @param imageType
	 *            后缀格式，如"JPEG,png.."
	 * @return true:符合imageType格式; false:不符合
	 * @throws IOException
	 */
	public static boolean checkImageType(File file, String imageType) throws IOException {
		if (!file.exists()) {
			return false;
		}
		boolean result = false;
		ImageInputStream iis = ImageIO.createImageInputStream(file);
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		ImageReader reader = null;
		if (readers.hasNext()) {
			reader = readers.next();
		}
		if (reader.getFormatName().equals(imageType)) {
			result = true;
		}
		return result;
	}

	/**
	 * 图片的像素判断
	 * 
	 * @param file
	 *            文件
	 * @param imageWidth
	 *            图片宽度
	 * @param imageHeight
	 *            图片高度
	 * @return true:上传图片宽度和高度都小于等于规定最大值
	 * @throws IOException
	 */
	public static boolean checkImageElement(File file, int imageWidth, int imageHeight) throws IOException {
		boolean result = false;
		if (!file.exists()) {
			return false;
		}
		BufferedImage bufferedImage = ImageIO.read(file);
		if (bufferedImage != null && bufferedImage.getHeight() <= imageHeight
				&& bufferedImage.getWidth() <= imageWidth) {
			result = true;
		}
		return result;
	}

	/**
	 * 检测图片的大小
	 * 
	 * @param file
	 *            文件
	 * @param imageSize
	 *            图片最大值(KB)
	 * @return true:上传图片小于图片的最大值
	 */
	public static boolean checkImageSize(File file, int imageSize) {
		boolean result = false;
		if (!file.exists()) {
			return false;
		}
		if ((file.length() / 1024) <= imageSize) {
			result = true;
		}

		return result;
	}

//	public static void main(String[] args) throws IOException {
//		File logoFile = new File("D:/oracle-logo.jpg");
//		if(CheckImagesFormatUtil.checkImageElement(logoFile, 260, 80)){
//			System.out.println(2);
//		}else{
//			System.out.println(1);
//		}
//	}
}