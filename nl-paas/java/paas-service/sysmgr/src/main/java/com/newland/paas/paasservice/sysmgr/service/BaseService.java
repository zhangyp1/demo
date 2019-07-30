/*
 *
 */
package com.newland.paas.paasservice.sysmgr.service;

import java.util.List;

/**
 * 描述
 * @author linkun
 * @created 2018年7月9日 下午1:58:49
 */
public interface BaseService<T> {

    List<T> selectBySelective(T params);

    T selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int deleteBySelective(T params);

    int insert(T params);

    int insertSelective(T params);

    int countBySelective(T params);

    int updateByPrimaryKeySelective(T params);

    int updateByPrimaryKey(T params);
    
}

