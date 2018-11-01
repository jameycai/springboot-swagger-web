package com.ultrapower.sbdemo.common.auth.bean;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @ClassName:     GrantedAuthorityImpl
 * @Description:   JWT中 负责存储权限和角色
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午4:09:59
 */
public class GrantedAuthorityBean implements GrantedAuthority, Serializable {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String authority;

    public GrantedAuthorityBean(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

}
