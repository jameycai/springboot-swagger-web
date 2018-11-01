package com.ultrapower.sbdemo.common.auth.impl;


import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ultrapower.sbdemo.webapp.home.model.UserBean;
import com.ultrapower.sbdemo.webapp.home.service.IUserService;

import static java.util.Collections.emptyList;

/**
 * 
 * @ClassName:     UserDetailsServiceImpl
 * @Description:   Security的UserDetailsService的实现类
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午6:29:18
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private IUserService userServiceImpl;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserBean myUser = userServiceImpl.getUserInfoByAccount(username);
	    if(myUser == null){
	            throw new UsernameNotFoundException(username);
	    }
	    return new org.springframework.security.core.userdetails.User(myUser.getUserAccount(), myUser.getPassword(), emptyList());
	}

}
