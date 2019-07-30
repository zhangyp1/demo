package com.newland.paas.common.util;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * User: chenshen
 *
 * @Description: 压缩工具
 * date: 14:32 2017/10/18
 */
public class ZipUtil {

    private static Log log = LogFactory.getLogger(ZipUtil.class);

    public static void main(String[] args) throws Exception {
    	unzip("D:/sxm0119001.tar.gz","D:/",false);
    }

    /**
     * 解压到指定目录
     *
     * @param zipPath
     * @param descDir
     * @author isea533
     */
    public static void unzip(String zipPath, String descDir, boolean includeZipFileName) throws IOException {
        log.info(LogProperty.LOGTYPE_CALL, "unzip zipPath[" + zipPath + "] descDir[" + descDir + "] includeZipFileName[" + includeZipFileName + "]");
        unzip(new File(zipPath), descDir, includeZipFileName);
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipFile
     * @param descDir
     * @author isea533
     */
    @SuppressWarnings("rawtypes")
    private static void unzip(File zipFile, String descDir, boolean includeZipFileName) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        if (includeZipFileName) {
            String fileName = zipFile.getName();
            if (StringUtils.isNotEmpty(fileName)) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            descDir = descDir + "/" + fileName;
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            zipEntryName = zipEntryName.replaceAll("\\\\", "/");
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
     * 对文件或文件目录进行压缩
     *
     * @param srcPath     要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
     * @param zipPath     压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
     * @param zipFileName 压缩文件名
     * @throws Exception
     */
    public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception {
        zip(srcPath, zipPath, zipFileName, false);
    }

    /**
     * 对文件或文件目录进行压缩
     *
     * @param srcPath     要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
     * @param zipPath     压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
     * @param zipFileName 压缩文件名
     * @throws Exception
     */
    public static void zip(String srcPath, String zipPath, String zipFileName, boolean includeZipFileName) throws Exception {
        log.info(LogProperty.LOGTYPE_CALL, "zip srcPath[" + srcPath + "] zipPath[" + zipPath + "] zipFileName[" + zipFileName + "] includeZipFileName[" + includeZipFileName + "]");
        String fullzipname = zipPath + "/" + zipFileName;
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fullzipname));
        File sourceFile = new File(srcPath);

        if (sourceFile.isDirectory() && includeZipFileName) {
            String base = srcPath.substring(srcPath.lastIndexOf("/") + 1);
            //调用函数
            compress(out, sourceFile, base);
        } else {
            if(sourceFile.isFile()){
                String base = srcPath.substring(srcPath.lastIndexOf("/") + 1);
                //调用函数
                compress(out, sourceFile, base);
            }else{
                //调用函数
                compress(out, sourceFile, "");
            }
        }
        out.close();
    }

    private static void compress(ZipOutputStream out, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {

            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                log.info(LogProperty.LOGTYPE_CALL, "zipfilename:"+base + "/");
                out.putNextEntry(new ZipEntry(base + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (int i = 0; i < flist.length; i++) {
                    compress(out, flist[i], base + "/" + flist[i].getName());
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            log.info(LogProperty.LOGTYPE_CALL, "zipfilename:"+base);
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];

            int len;
            //将源文件写入到zip文件中
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            bis.close();
        }
    }
}
