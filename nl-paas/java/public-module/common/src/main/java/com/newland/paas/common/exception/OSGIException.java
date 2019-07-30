package com.newland.paas.common.exception;

/**
 * OSGI/BluePrint/KARAF异常
 * @author czc
 */
public class OSGIException extends NLCheckedException{
	private static final long serialVersionUID = 1L;
	//异常类型
	public static final String TYPE = "osgi"; 
	
	public OSGIException(String code , String message,Throwable throwable){
		super(code, message,throwable);
	}
	
	public OSGIException(String code,Throwable throwable){
		super(code,throwable);
	}
	
	public OSGIException(String code,NLThrowable ex){
		super(code, ex);
	}
	
	public OSGIException(String code,String message){
		super(code,message);
	}
	
	public String getType() {
		return TYPE;
	}
}
