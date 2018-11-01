package com.ultrapower.sbdemo.common.filter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.ultrapower.sbdemo.common.ConvertUtil;
import com.ultrapower.sbdemo.common.DateTimeUtil;
import com.ultrapower.sbdemo.common.cache.EhCacheUtil;
import com.ultrapower.sbdemo.common.utils.JsonResultBean;

/**
 * 
 * @ClassName:     RestVisitProtectFilter
 * @Description:   rest接口访问保护
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月15日 上午10:46:34
 */
public class RestVisitProtectFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(RestVisitProtectFilter.class);
			

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
    	HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;
        
		String url = httpRequest.getRequestURI(); 
		
        ///logger.info("url: "+url);

        boolean isRedirect = false;
		try {
			if(null!=url && url.indexOf("/rest/")!=-1) {
				// 获取当前时间秒
				LocalDateTime localDateTime = LocalDateTime.now();
				Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
				long nowSecond = instant.getEpochSecond();
				
				String dateStr = DateTimeUtil.getNowData();
				
				// restful的key
				String key = "rest_"+dateStr+"_" + nowSecond;
				
				Object obj = EhCacheUtil.get(EhCacheUtil.CACHENAME_COMMON, key);
				if(null!=obj) {
					int count = ConvertUtil.Obj2int(obj);
					if(count > 100) {
						isRedirect = true;
						
						String basePath = url.substring(0, url.indexOf("/rest/"));
						logger.info("basePath: "+basePath);
						
						//==================================================
						httpResponse.setHeader("Cache-Control", "no-store");
						httpResponse.setDateHeader("Expires", 0);
						httpResponse.setHeader("Prama", "no-cache");
						
						httpResponse.setContentType("application/json; charset=utf-8");
						JsonResultBean jsonBean = new JsonResultBean();
						jsonBean.setStatus(JsonResultBean.FAIL);
						jsonBean.setResult("restful接口并发访问量过大");
						String responseJSONStr = JSON.toJSONString(jsonBean);
						//httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
						httpResponse.getWriter().write(responseJSONStr);
			            
						//此处设置了访问静态资源类
						///httpResponse.sendRedirect(basePath+"/templates/common/404.html");
					}else {
						//==================================================
						httpResponse.setHeader("Cache-Control", "no-store");
						httpResponse.setDateHeader("Expires", 0);
						httpResponse.setHeader("Prama", "no-cache");
						
						EhCacheUtil.put(EhCacheUtil.CACHENAME_COMMON, key, count+1);
					}
				}else {
					//==================================================
					httpResponse.setHeader("Cache-Control", "no-store");
					httpResponse.setDateHeader("Expires", 0);
					httpResponse.setHeader("Prama", "no-cache");
					
					EhCacheUtil.put(EhCacheUtil.CACHENAME_COMMON, key, 1);
				}
				
				
			}
		}catch(Exception ex) {
		}
		
		
		try {
			if(!isRedirect) {
				chain.doFilter(req, res); 
			}
		}catch(Exception ex) {
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
