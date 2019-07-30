package com.newland.paas.paastool.etcdtool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;

/**
 * Created with IntelliJ IDEA. Description: User: luolifeng Date: 2018-12-24 Time: 15:30
 */
public interface EtcdConnection extends AutoCloseable {
    public ByteSequence lock(String key) throws ExecutionException, InterruptedException;

    public void unLock(ByteSequence shareKey) throws ExecutionException, InterruptedException;

    /**
     * 获取Etcd客户端
     *
     * @return EtcdClient instance
     */
    public Client getEtcdClient();

    /**
     * get single etcdKey from etcd; 从Etcd获取单个key
     *
     * @param key etcdKey
     * @return etcdKey and value 's instance
     */
    public KeyValue getEtcdKey(String key) throws ExecutionException, InterruptedException;

    /**
     * @Created with IntelliJ IDEA.
     * @Description:
     * @return: 通过Key直接获取对应的value,
     * @params:
     * @author: luolifeng
     * @Date: 2018/12/21
     */
    public String get(String key) throws ExecutionException, InterruptedException;

    /**
     * @Created with IntelliJ IDEA.
     * @Description:
     * @return: 通过Key直接获取对应的value,
     * @params:
     * @author: luolifeng
     * @Date: 2018/12/21
     */
    public Map<String, String> getWithPrefix(String prefix) throws ExecutionException, InterruptedException;

    /**
     * get all etcdKey with this prefix 从Etcd获取满足前缀的所有key
     *
     * @param prefix etcdKey's prefix
     * @return all etcdKey with this prefix
     */
    public List<KeyValue> getEtcdKeyWithPrefix(String prefix) throws ExecutionException, InterruptedException;

    /**
     * put single etcdKey 将单个key放入Etcd中
     *
     * @param key single etcdKey
     * @param value etcdKey's value
     */
    public void putEtcdKey(String key, String value) throws ExecutionException, InterruptedException;

    /**
     * put single etcdKey with a expire time (by etcd lease) 将一个有过期时间的key放入Etcd，通过lease机制
     *
     * @param key single etcdKey
     * @param value etcdKey's value
     * @param expireTime expire time (s) 过期时间，单位秒
     * @return lease id 租约id
     */
    public long putEtcdKeyWithExpireTime(String key, String value, long expireTime)
        throws ExecutionException, InterruptedException;

    /**
     * put single etcdKey with a lease id 将一个key绑定指定的租约放入到Etcd。
     *
     * @param key single etcdKey
     * @param value etcdKey's value
     * @param leaseId lease id 租约id
     * @return revision id if exception return 0L
     */
    public long putEtcdKeyWithLeaseId(String key, String value, long leaseId)
        throws ExecutionException, InterruptedException;

    /**
     * keep alive for a single lease
     *
     * @param leaseId lease id 租约Id
     */
    public void keepAliveEtcdSingleLease(long leaseId);

    /**
     * delete single etcdKey 从Etcd中删除单个key
     *
     * @param key etcdKey
     */
    public void deleteEtcdKey(String key) throws ExecutionException, InterruptedException;

    /**
     * delete all key with prefix 从Etcd中删除所有满足前缀匹配的key
     *
     * @param prefix etcdKey's prefix
     */
    public void deleteEtcdKeyWithPrefix(String prefix) throws ExecutionException, InterruptedException;

    /**
     * get single etcdKey's custom watcher 得到一个单个key的自定义观察者
     *
     * @param key etcdKey
     * @return single etcdKey's custom watcher
     */
    public Watch.Watcher getCustomWatcherForSingleKey(String key);

    /**
     * get a watcher who watch all etcdKeys with prefix 得到一个满足所有前缀匹配的key集合的自定义观察者
     *
     * @param prefix etcdKey's prefix
     * @return a watcher who watch all etcdKeys with prefix
     */
    public Watch.Watcher getCustomWatcherForPrefix(String prefix);
}