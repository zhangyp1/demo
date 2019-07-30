package com.newland.paas.common.invocation.handle.retry;

import com.newland.paas.common.invocation.IInvocation;
import com.newland.paas.common.invocation.filter.AbstractExceptionFilter;
import com.newland.paas.common.invocation.handle.retry.exception.RetryFailureException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2015, NEWLAND,LTD All Rights Reserved.
 *
 * @ClassName:TimeBasedRetryStrategy
 * @Description: 基于时限(时间及次数条件)重试策略实现
 * @Function List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年6月18日下午3:12:59
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public class TimeBasedRetryStrategy extends  AbstractRetryStrategy {
    
    private static final Log log = LogFactory.getLogger(TimeBasedRetryStrategy.class);
    /**
     * 超时时间(毫秒)
     */
    private long timeOutMillis = -1;

    /**
     * 重试次数限制
     */
    private int retryLimits    = Integer.MAX_VALUE;

    /**
     * 每次执行休眠(间隔)时间(毫秒)
     */
    private long sleepMillis   = -1;

    public TimeBasedRetryStrategy() {

    }
    /**
     * @Function: execute
     * @Description: 策略执行<br/>
     * @param invoSvc 调用对象引用
     * @param filter  过滤器
     * @param args    调用参数
     * @return 
     * @throws RetryFailureException 重试失败异常
     * @see com.newland.paas.common.IRetryStrategy.handle.retry.IRetryStategy#execute(com.newland.paas.rtc.rtccommon.invocation.IInvocation, Throwable)
     */
    public Object execute(IInvocation invoSvc,AbstractExceptionFilter filter, 
            Object ... args) 
            throws RetryFailureException {
        
        long timeStart  = System.currentTimeMillis();
        int  retryCount = 0;
        long costTimeMillis = 0L;
        //重试上下文
        RetryContext ctx= new RetryContext(timeStart,null);
        //返回结果
        Object result   = null;
        //调试日志
        StringBuffer buf = new StringBuffer();
        if(log.isDebugEnabled()){
            buf.append(getLogTag())
               .append("class:")
               .append(invoSvc.getClass())
               .append(args);
        }
        while(true){
            //调用重试
            retryCount ++ ;
            costTimeMillis = System.currentTimeMillis() - timeStart;
            
            ctx.setCostTimeMills(costTimeMillis);
            ctx.setRetryCount(retryCount);
            
            try{
                result = this.invoke(invoSvc,ctx, args);
                return result;
            }catch(Throwable e){
                
                ctx.setCurrThrowable(e);
                //判断是否有限定重试的异常
                if(!judgeException(filter,e)){
                    throw new RetryFailureException("非预期需重试的异常，不继续做重试处理!",e);
                }
                //耗时时限判断
                if(timeOutMillis > 0 && timeOutMillis <= costTimeMillis) 
                    throw new RetryFailureException("重试失败，重试时间超过:"+costTimeMillis+"(毫秒)",e);
                //重试次数判断
                if(retryLimits > 0 && retryCount >= retryLimits)
                    throw new RetryFailureException("重试失败，重试次数超过:"+retryLimits+"(次)",e);
            }
            //调试日志
            if(log.isDebugEnabled()){
                StringBuffer t = new StringBuffer(buf.toString());
                t.append("执行第：").append(retryCount).append("次重试失败,将在休眠后继续重试!")
                 .append("休眠时间为:").append(sleepMillis).append("毫秒")
                 .append("总重试耗时:").append(costTimeMillis).append("毫秒");
                log.debug(LogProperty.LOGTYPE_DETAIL, t.toString());
            }
            //休眠处理
            if(sleepMillis > 0){
                 try {
                    TimeUnit.MILLISECONDS.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    throw new RetryFailureException("重试失败,线程中断异常",e);
                }
            }
        }
    }
    /**
     * @Function:     invoke 
     * @Description:  重试方法执行  
     *
     * @param invoSvc
     * @param costTimeMillis
     * @param retryCount
     * @param args
     * @return
     */
    public Object invoke(IInvocation invoSvc,RetryContext ctx,Object ... args) throws Throwable{
        return invoSvc.handle(ctx,args);
    }
    
    public void setTimeOutMillis(long timeOutMillis) {
        this.timeOutMillis = timeOutMillis;
    }

    public void setRetryLimits(int retryLimits) {
        this.retryLimits = retryLimits;
    }

    public void setSleepMillis(long sleepMillis) {
        this.sleepMillis = sleepMillis;
    }
    
    public TimeBasedRetryStrategy appendTimeOutMillis(long timeOutMillis) {
        setTimeOutMillis(timeOutMillis);
        return this;
    }
    
    public TimeBasedRetryStrategy appendRetryLimits(int retryLimits) {
        setRetryLimits(retryLimits);
        return this;
    }
    
    public TimeBasedRetryStrategy appendSleepMillis(long sleepMillis) {
        setSleepMillis(sleepMillis);
        return this;
    }

}