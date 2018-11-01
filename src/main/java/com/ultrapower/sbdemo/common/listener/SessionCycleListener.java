package com.ultrapower.sbdemo.common.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ultrapower.sbdemo.common.interceptor.UserSecurityInterceptor;

/**
 * 
 * @ClassName:     SessionCycleListener
 * @Description:   session周期检测 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月26日 下午2:51:39
 */
public class SessionCycleListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		//服务器一创建httpSession，就向list集合中添加HttpSession
        HttpSession httpSession = se.getSession( ); 
		ServletContext application = httpSession.getServletContext();
        List<HttpSession> list = (List<HttpSession>) application.getAttribute("list");
        if(null==list) {
        	list = new ArrayList<HttpSession>();
        }
        System.out.println("session创建添加了"+httpSession.getId());
        list.add(httpSession);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();  
		if (null!= session && session.getAttribute(UserSecurityInterceptor.SESSION_KEY_USERACCOUNT) != null) {
			session.removeAttribute(UserSecurityInterceptor.SESSION_KEY_USERACCOUNT);
		}
	}

	
}
