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
 * @ClassName:SleepGraduallyStrategy
 * @Description: 基于渐进式睡眠重试策略实现。<br/>
 *               处理策略:先重试一定次数(不休眠),如果超过该次数，则基于基准时间休眠T0，同时如果在N秒后(1个时间单元)，还是失败，则
 *               递增休眠时间为T0+T秒，如果2N秒后(2个时间单元)，还是失败，则递增休眠时间为T0+2T秒,...直到最大时限Lmax,
 *               则休眠时间为Tmax,一直休眠重试。
 * @Function List:
 *
 * @author chenzc
 * @version
 * @Date 2015年6月18日下午3:16:33
 *
 * @History:// 历史修改记录
 *                    <author>  // 修改人
 *                    <time>    // 修改时间
 *                    <version> // 版本
 *                    <desc>  // 描述修改内容
 */
public class SleepGraduallyStrategy  extends AbstractRetryStrategy{

    private static final Log log = LogFactory.getLogger(SleepGraduallyStrategy.class);
    /**
     * 初始重试次数(默认3次)
     */
    protected int  retryTimes = 3;

    /**
     * 休眠时间(T0，单位毫秒,默认休眠1000毫秒)
     */
    protected long sleepTimeMillcs = 1000L;

    /**
     * 失败超时单元（N单位秒,默认3秒）
     */
    protected int  timeUnitSecs = 3;

    /**
     * 休眠增量时间(T,单位毫秒，默认1000毫秒)
     */
    protected long sleepIncTimeMillcs = 1000L;

    /**
     * 重试失败超时时限(单位秒，默认30秒)
     */
    protected int  limitTimeUnitSecs = 30;

    /**
     * 重试线程休眠最大时限(单位毫秒，默认30000毫秒)
     */
    protected long limitSleepTime = 30000L;

    public SleepGraduallyStrategy() {

    }

    /**
     * @Function: execute
     * @Description: 策略执行
     *
     * @param invoSvc 调用对象引用
     * @param filter  过滤器
     * @return
     * @throws RetryFailureException 重试失败异常
     * @throws InterruptedException 线程中断异常
     *      Throwable)
     */
    public Object execute(IInvocation invoSvc,
                          AbstractExceptionFilter filter, Object... args)
            throws RetryFailureException {
        //重试总次数
        int retryCount = 0;
        //重试开始时间
        long timeStart = System.currentTimeMillis();
        //重试总耗时
        long costTimeMillics = 0L;
        //阶段一重试总耗时
        long firstStepTimeMillics = 0L;
        //阶段二重试总耗时
        long secStepTimeMillcs = 0L;
        //返回结果
        Object result = null;
        //时间单元数
        long timeUnit = 0;
        long sleepTime = 0;
        //重试上下文件
        RetryContext ctx= new RetryContext(timeStart,null);
        
        StringBuffer buf = new StringBuffer();
        if(log.isDebugEnabled()){
            buf.append(getLogTag())
               .append("class:")
               .append(invoSvc.getClass())
               .append(args);
        }
        
        while (true) {
            retryCount++;
            costTimeMillics = System.currentTimeMillis() - timeStart;
            
            ctx.setCostTimeMills(costTimeMillics);
            ctx.setRetryCount(retryCount);
            
            try {
                //调试日志
                if(log.isDebugEnabled()){
                    StringBuffer t = new StringBuffer(buf.toString());
                    t.append("执行第：").append(retryCount).append("次重试!");
                    log.debug(LogProperty.LOGTYPE_DETAIL, t.toString());
                }
                result = invoke(invoSvc,ctx, args);
                return result;
            } catch (Throwable e) {
                ctx.setCurrThrowable(e);
                
                if(!judgeException(filter,e)){
                    throw new RetryFailureException("非预期需重试的异常，不继续做重试处理!",e);
                }
                //阶段一、未超过重试次数
                if (retryTimes > 0 && retryCount < retryTimes) {
                    firstStepTimeMillics = costTimeMillics;
                }
                //阶段二、超过重试次数
                else {
                    secStepTimeMillcs = costTimeMillics - firstStepTimeMillics;
                    //计算休眠时间
                    //未超最大重试时限情况
                    if (secStepTimeMillcs < limitTimeUnitSecs * 1000) {
                        timeUnit = (secStepTimeMillcs / (timeUnitSecs * 1000));
                        sleepTime = sleepTimeMillcs + timeUnit
                                * sleepIncTimeMillcs;
                        sleepTime = sleepTime > limitSleepTime ? limitSleepTime
                                : sleepTime;
                    }
                    //超最大重试时限情况
                    else {
                        sleepTime = limitSleepTime;
                    }
                    //调试日志
                    if(log.isDebugEnabled()){
                        StringBuffer t = new StringBuffer(buf.toString());
                        t.append("执行第：").append(retryCount).append("次重试失败,将在休眠后继续重试!")
                         .append("休眠时间为:").append(sleepTime).append("毫秒")
                         .append("总重试耗时:").append(costTimeMillics).append("毫秒");
                        log.debug(LogProperty.LOGTYPE_DETAIL, t.toString());
                    }
                    //休眠
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                    } catch (InterruptedException e1) {
                        throw new RetryFailureException("重试失败,线程中断异常",e);
                    }
                }
            }
        }
    }

    /**
     * @Function:     invoke 
     * @Description:  重试方法执行  
     *
     * @param invoSvc 方法调用句柄
     * @param ctx     重试上下文件
     * @param args    方法句柄调用参数
     * @return
     */
    public Object invoke(IInvocation invoSvc, RetryContext ctx, Object... args) throws Throwable {
        return invoSvc.handle(ctx,args);
    }

    public int getRetryTimes() {

        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {

        this.retryTimes = retryTimes;
    }

    public long getSleepTimeMillcs() {

        return sleepTimeMillcs;
    }

    public void setSleepTimeMillcs(long sleepTimeMillcs) {
        this.sleepTimeMillcs = sleepTimeMillcs;
    }

    public int getTimeUnitSecs() {

        return timeUnitSecs;
    }

    public void setTimeUnitSecs(int timeUnitSecs) {

        this.timeUnitSecs = timeUnitSecs;
    }

    public long getSleepIncTimeMillcs() {

        return sleepIncTimeMillcs;
    }

    public void setSleepIncTimeMillcs(long sleepIncTimeMillcs) {

        this.sleepIncTimeMillcs = sleepIncTimeMillcs;
    }

    public int getLimitTimeUnitSecs() {

        return limitTimeUnitSecs;
    }

    public void setLimitTimeUnitSecs(int limitTimeUnitSecs) {

        this.limitTimeUnitSecs = limitTimeUnitSecs;
    }

    public long getLimitSleepTime() {

        return limitSleepTime;
    }

    public void setLimitSleepTime(long limitSleepTime) {

        this.limitSleepTime = limitSleepTime;
    }
    
    public SleepGraduallyStrategy appendRetryTimes(int retryTimes) {
        setRetryTimes(retryTimes);
        return this;
    }
    public SleepGraduallyStrategy appendSleepTimeMillcs(long sleepTimeMillcs) {
        setSleepTimeMillcs(sleepTimeMillcs);
        return this;
    }
    public SleepGraduallyStrategy appendTimeUnitSecs(int timeUnitSecs) {
        setTimeUnitSecs(timeUnitSecs);
        return this;
    }
    public SleepGraduallyStrategy appendSleepIncTimeMillcs(long sleepIncTimeMillcs) {
        setSleepIncTimeMillcs(sleepIncTimeMillcs);
        return this;
    }
    public SleepGraduallyStrategy appendLimitTimeUnitSecs(int limitTimeUnitSecs) {
        setLimitTimeUnitSecs(limitTimeUnitSecs);
        return this;
    }
    public SleepGraduallyStrategy appendLimitSleepTime(long limitSleepTime) {
        setLimitSleepTime(limitSleepTime);
        return this;
    }

}