package com.newland.paas.common.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TgzTarUtil {

    public final static int buffersize = 2048;
    public static final int BUFFER = 1024;

    /**
     * 解压tgz文件
     *
     * @param file tgz文件
     * @return
     */
    public static void deCompressTGZFile(String fileName) throws Exception {
        File file = deCompressGZFile(new File(fileName));
        deCompressTARFile(file);
    }

    /**
     * 转换成tar
     *
     * @param file
     * @return
     */
    public static File deCompressGZFile(File file) throws Exception {
        FileOutputStream out = null;
        GzipCompressorInputStream gzIn = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            File outFile = new File(file.getParent() + "/" + "tmp.tar");
            out = new FileOutputStream(outFile);
            gzIn = new GzipCompressorInputStream(in);
            final byte[] buffer = new byte[buffersize];
            int n = 0;
            while (-1 != (n = gzIn.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return outFile;
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                out.close();
                gzIn.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * 解压tar文件
     *
     * @param file tar文件
     * @return
     */
    public static void deCompressTARFile(String fileName) throws Exception {
        File file = new File(fileName);
        deCompressTARFile(file);
    }

    /**
     * tar文件解压
     *
     * @param file
     */
    public static void deCompressTARFile(File file) throws Exception {
        String basePath = file.getParent() + "/";
        TarArchiveInputStream is = null;
        try {
            is = new TarArchiveInputStream(new FileInputStream(file));
            while (true) {
                TarArchiveEntry entry = is.getNextTarEntry();
                if (entry == null) {
                    break;
                }
                if (entry.isDirectory()) {// 这里貌似不会运行到，跟ZipEntry有点不一样
                    new File(basePath + entry.getName()).mkdirs();
                } else {
                    FileOutputStream os = null;
                    try {
                        File f = new File(basePath + entry.getName());
                        if (!f.getParentFile().exists()) {
                            f.getParentFile().mkdirs();
                        }
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        os = new FileOutputStream(f);
                        byte[] bs = new byte[buffersize];
                        int len = -1;
                        while ((len = is.read(bs)) != -1) {
                            os.write(bs, 0, len);
                        }
                        os.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        os.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                is.close();
                // file.delete();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * tar.gz文件解压方法
     *
     * @param tarGzFile 要解压的压缩文件名称（绝对路径名称）
     * @param destDir   解压后文件放置的路径名（绝对路径名称）
     * @return 解压出的文件列表
     */
    public static List<String> deCompressGZipFile(String tarGzFile, String destDir) {

        List<String> fileList = new ArrayList<String>();

        OutputStream out = null; // 建立输出流，用于将从压缩文件中读出的文件流写入到磁盘
        FileInputStream fis = null; // 建立输入流，用于从压缩文件中读出文件
        GZIPInputStream gis = null;

        TarArchiveInputStream taris = null;
        TarArchiveEntry entry = null;
        TarArchiveEntry[] subEntries = null;

        File entryFile = null;
        File subEntryFile = null;
        String entryFileName = null;

        int entryNum = 0;
        try {
            fis = new FileInputStream(tarGzFile);
            gis = new GZIPInputStream(fis);
            taris = new TarArchiveInputStream(gis);

            while ((entry = taris.getNextTarEntry()) != null) {
                entryFileName = destDir + "/" + entry.getName();
                entryFile = new File(entryFileName);
                entryNum++;
                if (entry.isDirectory()) {
                    if (!entryFile.exists()) {
                        entryFile.mkdir();
                    }
                    subEntries = entry.getDirectoryEntries();
                    for (int i = 0; i < subEntries.length; i++) {
                        try {
                            subEntryFile = new File(entryFileName + "/" + subEntries[i].getName());
                            fileList.add(entryFileName + "/" + subEntries[i].getName());
                            out = new FileOutputStream(subEntryFile);
                            byte[] buf = new byte[1024];
                            int len = 0;
                            while ((len = taris.read(buf)) != -1) {
                                out.write(buf, 0, len);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            out.close();
                            out = null;
                        }
                    }
                } else {
                    fileList.add(entryFileName);
                    out = new FileOutputStream(entryFile);
                    try {
                        byte[] buf = new byte[1024];
                        int len = 0;
                        while ((len = taris.read(buf)) != -1) {
                            out.write(buf, 0, len);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        out.close();
                        out = null;
                    }
                }
            }

            if (entryNum == 0) {
                // log.warn("there is no entry in " + tarGzFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (taris != null) {
                try {
                    taris.close();
                } catch (Exception ce) {
                    taris = null;
                }
            }
            if (gis != null) {
                try {
                    gis.close();
                } catch (Exception ce) {
                    gis = null;
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ce) {
                    fis = null;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ce) {
                    out = null;
                }
            }
        }
        return fileList;

    }

    /**
     * 文件解压缩
     *
     * @param file
     * @param delete 是否删除原始文件
     * @throws Exception
     */
    public static void decompress(String inputFileName, String outputFileName)
            throws Exception {
        FileInputStream inputFile = new FileInputStream(inputFileName);
        FileOutputStream outputFile = new FileOutputStream(outputFileName);
        decompress(inputFile, outputFile);
        inputFile.close();
        outputFile.flush();
        outputFile.close();
    }

    /**
     * 数据解压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void decompress(InputStream is, OutputStream os)
            throws Exception {

        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    /**
     * 文件 解归档
     *
     * @param srcPath  源文件路径
     * @param destPath 目标文件路径
     * @throws Exception
     */
    public static void dearchive(String srcPath, String destPath)
            throws Exception {

        File srcFile = new File(srcPath);
        dearchive(srcFile, destPath);
    }

    /**
     * 解归档
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void dearchive(File srcFile, String destPath)
            throws Exception {
        dearchive(srcFile, new File(destPath));

    }

    /**
     * 解归档
     *
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void dearchive(File srcFile, File destFile) throws Exception {

        TarArchiveInputStream tais = new TarArchiveInputStream(
                new FileInputStream(srcFile));
        dearchive(destFile, tais);

        tais.close();

    }

    /**
     * 文件 解归档
     *
     * @param destFile 目标文件
     * @param tais     ZipInputStream
     * @throws Exception
     */
    private static void dearchive(File destFile, TarArchiveInputStream tais)
            throws Exception {

        TarArchiveEntry entry = null;
        while ((entry = tais.getNextTarEntry()) != null) {

            // 文件
            String dir = destFile.getPath() + File.separator + entry.getName();

            File dirFile = new File(dir);

            // 文件检查
            fileProber(dirFile);

            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                dearchiveFile(dirFile, tais);
            }

        }
    }

    /**
     * 文件解归档
     *
     * @param destFile 目标文件
     * @param tais     TarArchiveInputStream
     * @throws Exception
     */
    private static void dearchiveFile(File destFile, TarArchiveInputStream tais)
            throws Exception {

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile));

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = tais.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }

        bos.close();
    }

    /**
     * 文件探针
     *
     * <pre>
     * 当父目录不存在时，创建目录！
     * </pre>
     *
     * @param dirFile
     */
    private static void fileProber(File dirFile) {

        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {

            // 递归寻找上级目录
            fileProber(parentFile);

            parentFile.mkdir();
        }

    }

    
    /**
     * 文件解压缩
     *
     * @param file
     * @param delete 是否删除原始文件
     * @throws Exception
     */
    /**
     * tar.gz文件解压方法
     *
     * @param tarGzFile 要解压的压缩文件名称（绝对路径名称）
     * @param destDir   解压后文件放置的路径名（绝对路径名称）
     * @return 解压出的文件列表
     */
    public static void gzdecompress(String tarGzFile, String destDir) throws Exception {
    	 TgzTarUtil.decompress(tarGzFile, destDir+".tar");
    	 
    	 dearchive(destDir+".tar",destDir);
    	 
    	 FileUtil.deleteDir(destDir+".tar");
    }
    
    


    public static void main(String[] args) {


        try {
            String sougz= "/Users/zhang/newland/pass/资料/dev/mysql-5.6.35/2/mysql-5.6.36.tar.gz";
            String tar ="/Users/zhang/newland/pass/资料/dev/mysql-5.6.35/2";
            gzdecompress(sougz,tar);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
//        try {
//			String compressFile = "D:/sxm0119001.tar.gz";
//			TgzTarUtil.deCompressGZipFile(compressFile, "D:/");
//			//TgzTarUtil.deCompressTARFile(compressFile);
////			// 解压压缩文件校验,返回解压文件夹路径
//			//File filec = new File(compressFile);
//			//System.out.println(filec.getParent().replaceAll("\\\\", "/"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }
}
