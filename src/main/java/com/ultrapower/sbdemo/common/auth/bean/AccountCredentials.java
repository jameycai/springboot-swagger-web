package com.ultrapower.sbdemo.common.auth.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName:     AccountCredentials
 * @Description:   JWT中 负责存储用户名密码 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午4:08:58
 */
public class AccountCredentials implements Serializable {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String username = "";
    private String password = "";
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
