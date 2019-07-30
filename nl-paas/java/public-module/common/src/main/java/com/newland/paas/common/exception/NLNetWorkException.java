package com.newland.paas.common.exception;

/**
 * 网络异常
 * @author czc
 */
public class NLNetWorkException extends NLCheckedException{
	private static final long serialVersionUID = 1L;
	//异常类型
	public static final String TYPE = "network"; 
	
	public NLNetWorkException(String code , String message,Throwable throwable){
		super(code, message,throwable);
	}
	
	public NLNetWorkException(String code,Throwable throwable){
		super(code,throwable);
	}
	
	public NLNetWorkException(String code,NLThrowable ex){
		super(code, ex);
	}
	
	public NLNetWorkException(String code,String message){
		super(code,message);
	}
	
	public String getType() {
		return TYPE;
	}
}
