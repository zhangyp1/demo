
package com.newland.paas.common.invocation.handle.retry;
/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:RetryContext
 * @Description: 重试处理上下文文信息
 * @Funtion List:
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年10月19日		下午4:11:24
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class RetryContext {
    /*
     * 开始重试时间(UTC 1970年到当前时间毫秒数)
     */
    public long timeStartMills = 0;
    /*
     * 总重试耗时(毫秒)
     */
    public long costTimeMills  = 0;
    /*
     * 总重试次数
     */
    public long retryCount      = 0;
    /*
     * 最新异常对象
     */
    public Throwable  currThrowable     = null;
    
    public   RetryContext (){
        
    }
    public   RetryContext (long timeStartMills,Throwable t){
        this.timeStartMills  =  timeStartMills;
        this.currThrowable   =  t;
    }

    public long getRetryCount() {
    
        return retryCount;
    }

    public Throwable getCurrThrowable() {
    
        return currThrowable;
    }

    public void setRetryCount(long retryCount) {
    
        this.retryCount = retryCount;
    }

    public void setCurrThrowable(Throwable currThrowable) {
    
        this.currThrowable = currThrowable;
    }

    public long getTimeStartMills() {
    
        return timeStartMills;
    }

    public long getCostTimeMills() {
    
        return costTimeMills;
    }

    public void setTimeStartMills(long timeStartMills) {
    
        this.timeStartMills = timeStartMills;
    }

    public void setCostTimeMills(long costTimeMills) {
    
        this.costTimeMills = costTimeMills;
    }
    
}

