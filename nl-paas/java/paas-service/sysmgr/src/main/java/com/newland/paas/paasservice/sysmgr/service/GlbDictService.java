package com.newland.paas.paasservice.sysmgr.service;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.ex.BaseTreeDo;
import com.newland.paas.paasservice.sysmgr.vo.DictNode;
import com.newland.paas.paasservice.sysmgr.vo.DictTreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;

import java.util.List;

/**
 * 字典管理
 *
 * @author wrp
 * @since 2018/6/26
 */
public interface GlbDictService {
	/**
	 *
	 * @return
	 */
    List<GlbDict> findAll();

	/**
	 *
	 * @param dictPcode
	 * @return
	 */
    List<GlbDict> getByDictPcode(String dictPcode);

	/**
	 *
	 * @return
	 */
	List<GlbDict> getAll();

	/**
	 *
	 * @param dictCode
	 * @return
	 */
	List<GlbDict> getByDictCode(String dictCode);

	/**
	 *
	 * @param glbDict
	 * @return
	 */
	int insertDict(GlbDict glbDict);

	/**
	 *
	 * @return
	 */
	DictTreeNode<BaseTreeDo> getAllTree();

	/**
	 *
	 * @return
	 */
	List<DictNode> getAllNode();

	/**
	 *
	 * @param glbDict
	 * @param oldCode
	 * @param oldPcode
	 * @return
	 * @throws ApplicationException
	 */
	int insertOrUpdate(GlbDict glbDict, String oldCode, String oldPcode) throws ApplicationException;

	/**
	 *
	 * @param pcode
	 * @return
	 */
	DictTreeNode<BaseTreeDo> getTree(String pcode);

	/**
	 *
	 * @param dictCode
	 * @param pcode
	 * @return
	 * @throws ApplicationException
	 */
	int delByDictCodeAndPcode(String dictCode, String pcode) throws ApplicationException;


	/**
	 *
	 * @param pCode
	 * @param dictCode
	 * @return
	 * @throws ApplicationException
	 */
	GlbDict getDict(String pCode, String dictCode);

}
