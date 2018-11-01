package com.ultrapower.sbdemo.webapp.home.controller.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ultrapower.sbdemo.common.AESEncryptUtils;
import com.ultrapower.sbdemo.common.ConvertUtil;
import com.ultrapower.sbdemo.common.SysProperties;
import com.ultrapower.sbdemo.common.interceptor.UserSecurityInterceptor;
import com.ultrapower.sbdemo.common.utils.JsonResultBean;
import com.ultrapower.sbdemo.webapp.home.model.UserBean;
import com.ultrapower.sbdemo.webapp.home.service.IUserService;
import com.ultrapower.sbdemo.webapp.home.utils.CasMD5;


/**
 * 
 * @ClassName:     LoginController
 * @Description:   用户登录Controller
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月9日 上午9:17:54
 */
///@RequestMapping("/web/login")  // 用户登录Controller，不需要指定class路径
@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Resource
	private IUserService userServiceImpl;
	
	
	/**
	 * @Title:login
	 * @Description: 跳转到登录页面 
	 * @param session  
	 * @return
	 * @author caijinpeng
	 */
	@RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(HttpSession session) {
        // 读取默认皮肤
        String defaultStyle = ConvertUtil.Obj2Str(SysProperties.getProperty("skinType.defaultStyle"), "excellenceblue");
		session.setAttribute("skinType", defaultStyle); // 默认皮肤
        return "/home/login"; //返回页面路径
    }
	
	
	/**
	 * 
	 * @Title:loginPost
	 * @Description: 用户登录验证
	 * @param account
	 * @param password
	 * @param session
	 * @return
	 * @author caijinpeng
	 */
	@ResponseBody
    @RequestMapping(value="/loginPost", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String loginPost(@RequestParam(value="account", required=true) String account,
    		@RequestParam(value="password", required=true) String password,
    		HttpSession session) {
       
    	JsonResultBean r = new JsonResultBean();
    	try{
    		if(null==account || account.trim().length()==0 || null==password || password.trim().length()==0) {
    			r.setStatus(JsonResultBean.FAIL);
    			r.setResult("用户账号或密码为空！");
    			return JSON.toJSONString(r);
    		}
    		
    		// 对web请求的密码，进行解密
	        String decPass = AESEncryptUtils.aesDecrypt(password.trim());
	        if(null==decPass || decPass.trim().length()==0) {
    			r.setStatus(JsonResultBean.FAIL);
    			r.setResult("用户账号或密码为空！");
    			return JSON.toJSONString(r);
	        }
	        // 将web请求密码，进行pasm编码
	        String codePass = CasMD5.getMD5Code(decPass);
	        
	        
    		UserBean user = userServiceImpl.getUserInfoByAccount(account.trim());
	        if(null==user || user.getUserId()==0){
	   			r.setStatus(JsonResultBean.FAIL);
    			r.setResult("用户账号不存在！");
    			return JSON.toJSONString(r);
	        }	   
	        
	        
    		// 进行密码比对
    		String userPass = user.getPassword();   
    		if(null!=codePass && codePass.equals(userPass)) {
    			// ============== 通过认证===============
    			session.setAttribute(UserSecurityInterceptor.SESSION_KEY_USERACCOUNT, account.trim()); //session 用户
    			session.setAttribute("skinType", "excellenceblue"); // session 皮肤
    			
    			System.out.println("loginPost sessionID:"+session.getId());
    			logger.info("loginPost sessionID:"+session.getId());
    			
	   			r.setStatus(JsonResultBean.SUCCESS);
    			r.setResult("/web/home/index"); //url路径 
    			return JSON.toJSONString(r);
    		}
    		
    	}catch(Exception ex){
    		logger.error("loginPost error!! request account: "+account, ex);
			ex.printStackTrace();
    	}
    	
		r.setStatus(JsonResultBean.FAIL);
		r.setResult("用户账号或密码不正确！");
		return JSON.toJSONString(r);
    } 
    
    
	/**
	 * 
	 * @Title:logout
	 * @Description: 注销登录
	 * @param session
	 * @return
	 * @author caijinpeng
	 */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        // 移除session
        session.removeAttribute(UserSecurityInterceptor.SESSION_KEY_USERACCOUNT);
        return "redirect:/login"; //url路径
    }
    

}
