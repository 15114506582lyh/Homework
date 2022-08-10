package com.example.homework.Domain.vo;

public interface PaginationQuerySupport {

	/**
	 * 每页记录数
	 */
	public void setPageSize(int pageSize);

	public int getPageSize();

	/**
	 * 第几页
	 */
	public void setPage(int page);

	public int getPage();

//	public String getOrderBy();

}
