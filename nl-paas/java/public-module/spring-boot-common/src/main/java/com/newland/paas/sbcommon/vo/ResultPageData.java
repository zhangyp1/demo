package com.newland.paas.sbcommon.vo;

import java.util.Collection;
import java.util.List;

import com.github.pagehelper.Page;

/**
 * 分页查询，包含list和pageInfo信息
 *
 * @author wrp
 * @since 2018/6/20
 */
public class ResultPageData<T> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4472971178808408400L;

	public ResultPageData() {
    }

	public ResultPageData(List<T> list, PageInfo pageInfo) {
        this.list = list;
        this.pageInfo = pageInfo;
    }

	public ResultPageData(List<T> list) {
		this.pageInfo = new PageInfo();
		if (list instanceof Page) {
			Page<T> page = (Page<T>) list;
			this.pageInfo.setPageSize(page.getPageSize());
			this.pageInfo.setTotalRecord(page.getTotal());
			this.pageInfo.setCurrentPage(page.getPageNum());
			this.list = page;
		} else if (list instanceof Collection) {
			this.pageInfo.setTotalRecord((long) list.size());
			this.pageInfo.setPageSize(list.size());
			this.pageInfo.setCurrentPage(1);
			this.list = list;
		}
	}

    /**
     * 分页数据
     */
	private List<T> list;

    private PageInfo pageInfo;

	public List<T> getList() {
        return list;
    }

	public void setList(List<T> list) {
        this.list = list;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
