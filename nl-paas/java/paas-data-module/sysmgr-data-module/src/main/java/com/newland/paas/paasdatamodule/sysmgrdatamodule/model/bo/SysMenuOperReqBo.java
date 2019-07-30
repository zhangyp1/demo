/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;

/**
 * 描述
 * @author linkun
 * @created 2018年8月22日 下午3:47:00
 */
public class SysMenuOperReqBo extends SysMenuOper{
	private String menuNameLike;
	private Long noeqId;

	public String getMenuNameLike() {
		return menuNameLike;
	}

	public void setMenuNameLike(String menuNameLike) {
		this.menuNameLike = menuNameLike;
	}

	public Long getNoeqId() {
		return noeqId;
	}

	public void setNoeqId(Long noeqId) {
		this.noeqId = noeqId;
	}
}
