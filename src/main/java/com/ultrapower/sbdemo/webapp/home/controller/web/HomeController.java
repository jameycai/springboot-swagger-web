package com.ultrapower.sbdemo.webapp.home.controller.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ultrapower.sbdemo.common.DateTimeUtil;
import com.ultrapower.sbdemo.common.ConvertUtil;
import com.ultrapower.sbdemo.common.interceptor.UserSecurityInterceptor;
import com.ultrapower.sbdemo.webapp.home.service.IUserService;


/**
 * 
 * @ClassName:     HomeController
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月9日 上午9:17:54
 */
@RequestMapping("/web/home")
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Resource
	private IUserService userServiceImpl;
	
	/**
	 * @Title:login
	 * @Description: 跳转到系统首页
	 * @return
	 * @author caijinpeng
	 */
    @RequestMapping(value="/index", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String mainIndex(HttpServletRequest request,  ModelMap model) {
       
    	String dataStr = DateTimeUtil.getNowDateTime();
    	HttpSession session = request.getSession();
    	String userAccount = ConvertUtil.Obj2Str(session.getAttribute(UserSecurityInterceptor.SESSION_KEY_USERACCOUNT),"");
    	
    	model.put("user", userAccount);
    	model.put("time", dataStr);
        logger.info("Spring boot mainIndex ~~~~~~ "+ dataStr);
        return "/home/index"; //页面路径
    } 
    
    
}
