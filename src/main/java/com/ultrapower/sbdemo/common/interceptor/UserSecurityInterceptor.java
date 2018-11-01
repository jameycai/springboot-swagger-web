package com.ultrapower.sbdemo.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @ClassName:     UserSecurityInterceptor
 * @Description:   用户登录拦截器 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月25日 下午3:27:31
 */
public class UserSecurityInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(UserSecurityInterceptor.class);
			
	/**
     * 登录session user key
     */
    public final static String SESSION_KEY_USERACCOUNT = "user_account";

    
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      	// 访问前判断session是否有值,是否登入
        HttpSession session = request.getSession(false);
        if(null==session) {
        	session = request.getSession();
        }
        if (null!= session && session.getAttribute(SESSION_KEY_USERACCOUNT) != null) {
            return true;
        }
        
        
        System.out.println("preHandle sessionID:"+session.getId());
        logger.info("preHandle sessionID:"+session.getId());
        
        String projectName = "/sbdemo";
		if(null!=request && null!=request.getContextPath()){
			projectName = request.getContextPath();
		}
		
        // 跳转登录
        String url = projectName +"/login";
        response.sendRedirect(url); // 调整到登录url页面
        return false;
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
