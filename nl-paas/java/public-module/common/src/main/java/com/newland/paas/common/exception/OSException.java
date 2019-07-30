package com.newland.paas.common.exception;

/**
 * 操作系统异常
 * @author czc
 */
public class OSException extends NLCheckedException{
	private static final long serialVersionUID = 1L;
	//异常类型
	public static final String TYPE = "os"; 
	
	public OSException(String code , String message,Throwable throwable){
		super(code, message,throwable);
	}
	
	public OSException(String code,Throwable throwable){
		super(code,throwable);
	}
	
	public OSException(String code,NLThrowable ex){
		super(code, ex);
	}
	
	public OSException(String code,String message){
		super(code,message);
	}
	
	public String getType() {
		return TYPE;
	}
}
