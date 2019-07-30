package com.newland.paas.common.util;

import java.util.*;

/*
 * 
 * Copyright (c) 2014, NEWLAND,LTD All Rights Reserved.
 * 
 * @ClassName:AriesBluePrintSvcListProxy
 * 
 * @Description: aries bluePrint 服务列表代理，
 * 当使用aries 1.0.* 版本,注入serviceList，在多线程高并发情况下，获取serviceList存在瓶颈
 * ，本代理主要通过外部缓存service，且提供异步功能，避开直接获取serviceList的瓶颈，
 * 但由于使用service是异步更新，可能存在serviceList更新不及时问题。
 * 
 * @author chenzc
 * 
 * @version
 * 
 * @Date 2014年8月20日下午3:28:49
 * 
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>  // 描述修改内容
 */
public class AriesBluePrintSvcListProxy<T> extends AbstractCollection<T> implements List<T>{

    /*
     * 被管理原BluePrint Service 的List
     */
    public List<T> srcList = null;

    /*
     * 数据比对刷新间隔时间,单位(毫秒)。
     */
    public long timeInterval = 5000;

    /*
     * 交易处理的使用的List
     */
    public ArrayList<T> tranList = null;

    private SynchSvcDataThread synThread = new SynchSvcDataThread();
    
    /**
     * Creates a new instance of AriesBluePrintSvcListManager.
     *
     * @param list 一般情况list传入的参数为：aries BluePrint的ManagedCollection(List的子类)
     *            ,在我们使用的时候bp注入进来类型为list。
     * @param timeInterval 数据比对刷新间隔时间,单位(毫秒)，默认5秒,推荐传入值为秒级别，以减少同步数据开销。
     */
    public AriesBluePrintSvcListProxy(List<T> list, long timeInterval) {
        this.srcList = list;
        if (timeInterval != 0)
            this.timeInterval = timeInterval;
        copyData();
    }

    /**
     * 
     * @Function:     copyData 
     * @Description:  复制从源List复制相关数据,复制方法采用新建对象，替换引用方式。
     */
    protected void copyData() {
        ArrayList<T> newList = new ArrayList<T>();
        if (srcList == null)
            return;

        for (T t : srcList) {
            newList.add(t);
        }
        tranList = newList;
    }

    /**
     * @Function:     hasChanged 
     * @Description:  判断列表元素是否发生变更
     *
     * @return
     */
    protected boolean hasChanged(){
        //两者如果存在空，则默认情况返回false,不执行copy
        if(this.srcList == null || this.tranList == null) 
            return false ;
        //如果两个列表个数不同则，直接返回已变更
        if(this.srcList.size() != this.tranList.size()) 
            return true;
        //个数相同情况下，如果未完全包含，则发生变更
        for(T t :this.srcList){
            if(!this.tranList.contains(t))
                return true;
        }
        
        return  false;
    }
    /**
     * @Function:     getList 
     * @Description:  获取服务列表引用
     *
     * @return
     */
    public List<T> getSvcList() {
        if (tranList == null)
            return new ArrayList<T>();
        return tranList;
    }
    /**
     * @Function:     destory 
     * @Description:  释放代理的资源，具体：停止后台异步数据同步线程,变量清理等。
     */
    public void destory(){
        if(synThread!= null && synThread.isAlive()){
            synThread.interrupt();
        }
        srcList = null;
        tranList = null;
        System.out.println("AriesBluePrintSvcListProxy释放代理资源!");
    }
    /**
     * 
     *
     * @ClassName:SynchSvcDataThread
     * @Description: 数据异步同步线程,主要功能为按一定间隔时间执行blueprint服务列表数据同步。
     * @author chenzc
     * @version AriesBluePrintSvcListManager
     * @Date 2014年8月20日下午4:19:22
     *
     * @History:// 历史修改记录 
     *                     <author>  // 修改人
     *                     <time>    // 修改时间
     *                     <version> // 版本
     *                     <desc>  // 描述修改内容
     */
    class SynchSvcDataThread extends Thread {

        public void run() {
            
            super.run();

            try {
                while (true) {
                    sleep(timeInterval);
                    //存在变更则执行数据复制操作
                    if(hasChanged())
                        copyData();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    
    public void setSrcList(List<T> srcList) {

        this.srcList = srcList;
    }

    public void setTimeInterval(long timeInterval) {
    
        this.timeInterval = timeInterval;
    }

    //==========================================implement methods ======================
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("该类型列表暂不支持此方法!");
    }
    
    public T set(int index, T element) {
        throw new UnsupportedOperationException("该类型列表暂不支持此方法!");
    }
    
    public void add(int index, T element) {
        throw new UnsupportedOperationException("该类型列表暂不支持此方法!");
    }
    
    public T remove(int index) {
        throw new UnsupportedOperationException("该类型列表暂不支持此方法!");
    }

    public T get(int index) {
        if(this.tranList == null) return null;
        return this.tranList.get(index);
    }
    
    public int indexOf(Object o) {
        if(this.tranList == null) return -1;
        return this.tranList.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        if(this.tranList == null) return -1;
        return this.tranList.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        if(this.tranList == null) return null;
        return this.tranList.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        if(this.tranList == null) return null;
        return this.tranList.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        if(this.tranList == null) return null;
        return this.tranList.subList(fromIndex, toIndex);
    }

    public Iterator<T> iterator() {
        if(this.tranList == null) return null;
        return this.tranList.iterator();
    }

    public int size() {
        if(this.tranList == null) return 0;
        return this.tranList.size();
    }
}
