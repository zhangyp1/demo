package com.newland.paas.common.util;

import com.newland.paas.common.exception.ErrorCode;
import com.newland.paas.common.invocation.IInvocation;
import com.newland.paas.common.invocation.filter.ClassTypeIncludedFilter;
import com.newland.paas.common.invocation.filter.SimpleClassTypeFilter;
import com.newland.paas.common.invocation.handle.retry.IRetryStrategy;
import com.newland.paas.common.invocation.handle.retry.RetryContext;
import com.newland.paas.common.invocation.handle.retry.exception.RetryFailureException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.io.*;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.Charset;
import java.nio.file.*;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName FileWithExceptionProcessUtil
 * @Description 带有异常处理策略的文件操作工具类
 *
 * @author   Li Siqi
 * @version  
 * @Date	 2015年7月21日		下午7:35:09
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class FileWithExceptionProcessUtil {

	private static final Log log = LogFactory.getLogger(FileWithExceptionProcessUtil.class);

	/**
	 *
	 * @Function:     createDirectory
	 * @Description:  创建有一个可读写权限的目录
	 *
	 * @param srcPath 待创建目录路径
	 * @param strategy 异常处理策略
	 * @throws Exception
	 */
	public static void createDirectory(final Path srcPath
									  ,final IRetryStrategy strategy) 
									          throws RetryFailureException {

		try {
			FileUtil.createDirectories(srcPath);
		} catch (Exception e) {

			log.warn(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"创建目录:" + srcPath + "失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{UnsupportedOperationException.class
													,FileAlreadyExistsException.class
													,IOException.class
													,SecurityException.class});

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.createDirectories(srcPath);
						return null;
					}
				}
				,filer
				,srcPath);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 *
	 * @Function:     copyFile
	 * @Description:  拷贝文件
	 *
	 * @param src 源文件路径
	 * @param target 目标文件路径
	 * @param strategy 异常处理策略
	 * @param options 文件操作参数
	 * @throws Exception
	 */
	public static void copyFile(final Path src
							   ,final Path target
							   ,final IRetryStrategy strategy
							   ,final StandardCopyOption... options) 
							           throws RetryFailureException {

		try {
			FileUtil.copyFile(src, target, options);
		} catch (Exception e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"拷贝文件从源目录" + src + "到目标目录" + target + "失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{UnsupportedOperationException.class
													,FileAlreadyExistsException.class
													,DirectoryNotEmptyException.class
													,IOException.class
													,SecurityException.class});

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.copyFile(src, target, options);
						return null;
					}
				}
				,filer
				,src, target, options);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 *
	 * @Function:     getBufferedReader
	 * @Description:  读取文件内容到BufferedReader
	 *
	 * @param file 读取文件路径
	 * @param cs 字符编码
	 * @param strategy 异常处理策略
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader getBufferedReader(final Path file
												  ,final Charset cs
												  ,final IRetryStrategy strategy) 
												          throws RetryFailureException {

		BufferedReader reader = null;

		try {
			reader = FileUtil.getBufferedReader(file, cs);
		} catch (Exception e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"按照字符编码" + cs + "读取文件" + file + "失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{IOException.class
													,SecurityException.class});

				reader = (BufferedReader) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.getBufferedReader(file, cs);
					}
				}
				,filer
				,file, cs);
			} else {
				reader = null;
				throw new RetryFailureException(e);
			}
		}

		return reader;
	}

	/**
	 *
	 * @Function:     isExistFileCheck
	 * @Description:  判断文件是否存在
	 *
	 * @param srcPath 目标文件
	 * @param strategy 异常处理策略
	 * @param options 参数
	 * @return
	 */
	public static boolean isExistFileCheck(final Path srcPath
										  ,final IRetryStrategy strategy
										  ,final LinkOption... options) 
										          throws RetryFailureException {

		boolean result = false;

		try {
			result = FileUtil.isExistFileCheck(srcPath, options);
		} catch (SecurityException  e) {

			log.warn(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"判断是否存在文件" + srcPath + "操作失败!");

			if (strategy != null) {

				result = (Boolean) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.isExistFileCheck(srcPath, options);
					}
				}
				,new SimpleClassTypeFilter(SecurityException.class)
				,srcPath, options);
			} else {
				result = false;
				throw new RetryFailureException(e);
			}
		}

		return result;
	}

	/**
	 *
	 * @Function:     getFileSize
	 * @Description:  取得文件大小
	 *
	 * @param path 文件路径
	 * @param strategy 异常重试策略
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(final Path path
								  ,final IRetryStrategy strategy) 
								          throws RetryFailureException {

		long size = 0;

		try {
			size = FileUtil.getFileSize(path);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"获取文件" + path + "的大小操作失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{IOException.class
													,SecurityException.class});

				size = (Long) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.getFileSize(path);
					}
				}
				,filer
				,path);
			} else {
				size = 0;
				throw new RetryFailureException(e);
			}
		}

		return size;
	}

	/**
	 *
	 * @Function:     atomicMoveFile
	 * @Description:  原子移动文件
	 *
	 * @param src 源文件路径
	 * @param target 目标文件路径
	 * @param strategy 异常操作策略
	 * @throws Exception
	 */
	public static void atomicMoveFile(final Path src
									 ,final Path target
									 ,final IRetryStrategy strategy) 
									         throws RetryFailureException{

		try {
			FileUtil.atomicMoveFile(src, target);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"从" + src + "原子移动文件到" + target + "操作失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{FileNotFoundException.class
													,ClosedChannelException.class
													,OverlappingFileLockException.class
													,UnsupportedOperationException.class
													,DirectoryNotEmptyException.class
													,IOException.class
													,SecurityException.class});

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.atomicMoveFile(src, target);
						return null;
					}
				}
				,filer
				,src, target);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 *
	 * @Function:     reFileName
	 * @Description:  文件重命名
	 *
	 * @param orgFileName 原文件名
	 * @param newFilename 新文件名
	 * @param strategy 异常操作策略
	 * @throws Exception
	 */
	public static void reFileName(final String orgFileName
								 ,final String newFilename
								 ,final IRetryStrategy strategy) throws RetryFailureException {

		try {
			FileUtil.reFileName(orgFileName, newFilename);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"将文件" + orgFileName + "修改文件名为" + newFilename + "操作失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{UnsupportedOperationException.class
													,FileAlreadyExistsException.class
													,IOException.class
													,SecurityException.class
													,NullPointerException.class});

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.reFileName(orgFileName, newFilename);
						return null;
					}
				}
				,filer
				,orgFileName, newFilename);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 *
	 * @Function:     createFile
	 * @Description:  新建文件
	 *
	 * @param pathStr 文件路径
	 * @param strategy 异常操作策略
	 * @return true：文件创建成功；false：创建失败
	 * @throws Exception
	 */
	public static boolean createFile(final String pathStr
									,final IRetryStrategy strategy) throws RetryFailureException {

		boolean result = false;

		try {
			result = FileUtil.createFile(pathStr);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"创建文件" + pathStr + "操作失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{UnsupportedOperationException.class
													,FileAlreadyExistsException.class
													,IOException.class
													,SecurityException.class
													,InvalidPathException.class});

				result = (Boolean) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.createFile(pathStr);
					}
				}
				,filer
				,pathStr);
			} else {
				result = false;
				throw new RetryFailureException(e);
			}
		}

		return result;
	}

	/**
	 *
	 * @Function:     createFile
	 * @Description:  新建文件
	 *
	 * @param path 文件路径
	 * @param strategy 异常操作策略
	 * @return true：文件创建成功；false：创建失败
	 * @throws Exception
	 */
	public static boolean createFile(final Path path
									,final IRetryStrategy strategy) throws RetryFailureException {

		boolean result = false;

		try {
			result = FileUtil.createFile(path);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"创建文件" + path + "操作失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{UnsupportedOperationException.class
													,FileAlreadyExistsException.class
													,IOException.class
													,SecurityException.class});

				result = (Boolean) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.createFile(path);
					}
				}
				,filer
				,path);
			} else {
				result = false;
				throw new RetryFailureException(e);
			}
		}

		return result;
	}

	/**
	 *
	 * @Function:     closeOutStream
	 * @Description:  关闭FileOutputStream资源
	 *
	 * @param out
	 * @param strategy 异常处理策略
	 * @throws Exception
	 */
	public static void closeOutStream(final FileOutputStream out
									 ,final IRetryStrategy strategy) throws RetryFailureException {

		try {
			FileUtil.closeOutStream(out);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"关闭FileOutputStream资源操作失败!");

			if (strategy != null) {

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.closeOutStream(out);
						return null;
					}
				}
				,new SimpleClassTypeFilter(IOException.class)
				,out);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 *
	 * @Function:     closeFileChannel
	 * @Description:  关闭FileChannel资源
	 *
	 * @param channel
	 * @param strategy 异常处理策略
	 * @throws Exception
	 */
	public static void closeFileChannel(final FileChannel channel
									   ,final IRetryStrategy strategy) throws RetryFailureException {

		try {
			FileUtil.closeFileChannel(channel);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"关闭FileChannel资源操作失败!");

			if (strategy != null) {

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.closeFileChannel(channel);
						return null;
					}
				}
				,new SimpleClassTypeFilter(IOException.class)
				,channel);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 
	 * @Function:     getFileOutputStream
	 * @Description:  取得文件的FileOutputStream实例
	 *
	 * @param path 文件路径
	 * @param strategy 异常操作策略
	 * @return
	 * @throws Exception
	 */
	public static FileOutputStream getFileOutputStream(final Path path
													  ,final IRetryStrategy strategy) throws RetryFailureException {

		FileOutputStream outputStream = null;

		try {
			outputStream = FileUtil.getFileOutputStream(path);
		} catch (Exception e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"获取文件" + path + "的FileOutputStream实例失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{UnsupportedOperationException.class
													,FileNotFoundException.class
													,SecurityException.class});

				outputStream = (FileOutputStream) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.getFileOutputStream(path);
					}
				}
				,filer
				,path);
			} else {
				outputStream = null;
				throw new RetryFailureException(e);
			}
		}

		return outputStream;
	}

	/**
	 *
	 * @Function:     writeData
	 * @Description:  写文件（追加写入内容）
	 *
	 * @param path 文件路径
	 * @param context 写入内容
	 * @param strategy 异常处理策略
	 * @throws Exception
	 */
	public static void writeData(final String path
								,final String context
								,final IRetryStrategy strategy) throws RetryFailureException {

		try {
			FileUtil.writeData(path, context);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"向文件" + path + "写入文本内容（" + context + "）操作失败!");

			if (strategy != null) {

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.writeData(path, context);
						return null;
					}
				}
				,new SimpleClassTypeFilter(IOException.class)
				,path, context);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 *
	 * @Function:     write
	 * @Description:  写文件（追加写入内容）
	 *
	 * @param fileChannel 文件通道
	 * @param context 写入文件的字符串
	 * @param strategy 异常处理策略
	 * @throws Exception
	 */
	public static void write(final FileChannel fileChannel
							,final String context
							,final IRetryStrategy strategy) throws RetryFailureException {

		try {
			FileUtil.write(fileChannel, context);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"使用FileChannel向文件写入文本内容（" + context + "）操作失败!");

			if (strategy != null) {

				strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						FileUtil.write(fileChannel, context);
						return null;
					}
				}
				,new SimpleClassTypeFilter(IOException.class)
				,fileChannel, context);
			} else {
				throw new RetryFailureException(e);
			}
		}
	}

	/**
	 *
	 * @Function:     deleteFile 
	 * @Description:  删除文件
	 *
	 * @param filePath 待删除文件路径
	 * @param strategy 异常重试策略
	 * @return true：删除成功；false：删除失败
	 * @throws Exception
	 */
	public static boolean deleteFile(final String filePath
									,final IRetryStrategy strategy) throws RetryFailureException {

		boolean result = false;

		try {
			result = FileUtil.deleteFile(filePath);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"删除文件" + filePath + "操作失败!");

			if (strategy != null) {

				ClassTypeIncludedFilter filer = new ClassTypeIncludedFilter();

				filer.addRetryExceptions(new Class[]{NullPointerException.class
													,SecurityException.class});

				result = (Boolean) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.deleteFile(filePath);
					}
				}
				,filer
				,filePath);
			} else {
				result = false;
				throw new RetryFailureException(e);
			}
		}

		return result;
	}

	/**
	 *
	 * @Function:     deleteFile 
	 * @Description:  删除文件
	 *
	 * @param file 待删除文件对象
	 * @param strategy 异常重试策略
	 * @return true：删除成功；false：删除失败
	 * @throws Exception
	 */
	public static boolean deleteFile(final File file
									,final IRetryStrategy strategy) throws RetryFailureException {

		boolean result = false;

		try {
			result = FileUtil.deleteFile(file);
		} catch (Exception  e) {

			log.error(LogProperty.LOGCONFIG_DEALID
					 ,ErrorCode.SYSERROR_FILE
					 ,e
					 ,"删除文件" + file.getAbsolutePath() + "操作失败!");

			if (strategy != null) {

				result = (Boolean) strategy.execute(new IInvocation() {

					@Override
					public Object handle(RetryContext ctx,Object... args) throws Throwable {
						return FileUtil.deleteFile(file);
					}
				}
				,new SimpleClassTypeFilter(SecurityException.class)
				,file);
			} else {
				result = false;
				throw new RetryFailureException(e);
			}
		}

		return result;
	}
}
