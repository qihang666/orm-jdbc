package com.commons.orm.model;

/**
 * @Description:分页对象
 * @author hang
 * @date 2016-9-23 下午3:59:21
 * @version V1.7
 */
public class Page {
	private int page;// 第几页
	private int rows;// 每页数量

	public Page(int page, int rows) {
		super();
		this.page = page;
		this.rows = rows;
	}

	public int getIndex() {
		return (this.page - 1) * this.rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
