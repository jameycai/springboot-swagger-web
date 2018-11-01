package com.ultrapower.sbdemo.common.utils;

import java.io.Serializable;

/**
 * 
 * @ClassName:     JsonResultBean
 * @Description:   返回json对象bean
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:57:05 
 *
 */
public class JsonResultBean implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 常用状态：成功
	 */
	public final static String SUCCESS = "success";
	/**
	 * 常用状态：失败
	 */
	public final static String FAIL = "fail";
	
	
	/**
	 * 返回状态(成功:success; 失败:fail)
	 */
	private String status = null;


	/**
	 * 返回状态为失败时候，错误信息提示
	 */
	private String errorMsg = "";
	/**
	 * 返回状态为成功时候，成功信息提示
	 */
	private String successMsg = "";
	
	
	
	/**
	 * 返回结果(返回结果类型：JSON对象、JSONArray对象的字符串)
	 *  [如果是返回是查询列表, 则JSON对象包含查询列表 和查询总数，如：rows:列表, total:总数]
	 */
	private Object result = null;

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	
	
}
