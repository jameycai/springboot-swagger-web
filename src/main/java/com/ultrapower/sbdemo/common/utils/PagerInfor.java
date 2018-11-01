package com.ultrapower.sbdemo.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName:     分页集合
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijingpeng
 * @email          caijingpeng@ultrapower.com.cn
 * @version        V1.0
 * @Date           2017年12月20日 下午12:57:05 
 *
 */
public class PagerInfor implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 总数
	 */
	private int totalNum;
	/*
	 * 当前页号
	 */
	private int pageNo;
	/**
	 * 页大小（50）
	 */
	private int pageSize;
	/**
	 * 起始号
	 */
	private int fromIndex;
	/**
	 * 结束号
	 */
	private int toIndex;
	/**
	 * 集合
	 */
	private List<?> list;
	

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

}

