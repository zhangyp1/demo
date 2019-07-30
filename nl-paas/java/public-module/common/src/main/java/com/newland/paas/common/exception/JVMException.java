package com.newland.paas.common.exception;
/**
 * JVM异常
 * @author czc
 */
public class JVMException extends NLUnCheckedException {
	
	private static final long serialVersionUID = 1L;
	//异常类型
	public static final String TYPE = "jvm"; 
	
	public JVMException(String code , String message,Throwable throwable){
		super(code, message, throwable);
	}
	
	public JVMException(String code,Throwable throwable){
		super(code,throwable);
	}
	
	public JVMException(String code,NLThrowable ex){
		super(code,ex);
	}
	
	public JVMException(String code,String message){
		super(code,message);
	}
	
	public String getType() {
		return TYPE;
	}

}
