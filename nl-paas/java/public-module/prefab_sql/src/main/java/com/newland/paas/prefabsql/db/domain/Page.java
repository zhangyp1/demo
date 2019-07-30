package com.newland.paas.prefabsql.db.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 * @param <T>
 */
public class Page<T> {

	// 查询条件
	private Object conditon;
	
	// 结果集
	private List<T> list = new ArrayList<T>();

	// 查询记录数
	private int totalRecords;

	// 每页多少条数据
	private int pageSize=10;

	// 第几页
	private int pageNo = 1;
	/*
	 * 翻页开始Id+1：>startOrderById
	 */
	private long startOrderById;

	
	public long getStartOrderById() {
		return startOrderById;
	}

	public void setStartOrderById(long startOrderById) {
		this.startOrderById = startOrderById;
	}

	public int getStartIndex() {
		return pageSize*(pageNo-1)+1;
	}
	
	public int getEndIndex() {
		return pageSize*pageNo;
	}

	public Object getConditon() {
		return conditon;
	}

	public void setConditon(Object conditon) {
		this.conditon = conditon;
	}

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPages() {
		return (totalRecords + pageSize - 1) / pageSize;
	}

	/**
	 * 取得首页
	 * 
	 * @return
	 */
	public int getTopPageNo() {
		return 1;
	}

	/**
	 * 上一页
	 * 
	 * @return
	 */
	public int getPreviousPageNo() {
		if (pageNo <= 1) {
			return 1;
		}
		return pageNo - 1;
	}

	/**
	 * 下一页
	 * 
	 * @return
	 */
	public int getNextPageNo() {
		if (pageNo >= getBottomPageNo()) {
			return getBottomPageNo();
		}
		return pageNo + 1;
	}

	/**
	 * 取得尾页
	 * 
	 * @return
	 */
	public int getBottomPageNo() {
		return getTotalPages();
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public boolean hasNextPage(){
		pageNo++;
		return getStartIndex()<=totalRecords;
	}

}
