package com.newland.paas.sbcommon.utils;

import com.newland.paas.sbcommon.vo.PageInfo;

/**
 * 分页工具
 * @author wrp
 */
public class PageParamsUtil {
	public static long getStartIndex(PageInfo pageParams) {
		return pageParams.getPageSize()*(pageParams.getCurrentPage()-1)+1;
	}
	
	public static int getEndIndex(PageInfo pageParams) {
		return pageParams.getPageSize()*pageParams.getCurrentPage();
	}
}
