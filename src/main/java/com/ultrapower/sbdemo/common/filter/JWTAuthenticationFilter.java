package com.ultrapower.sbdemo.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.ultrapower.sbdemo.common.auth.TokenAuthenticationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/***
 * 
 * @ClassName:     JWTAuthenticationFilter
 * @Description:   token的校验 (从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。)
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午4:30:30
 */
public class JWTAuthenticationFilter extends GenericFilterBean  {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			
			// 
			String header = httpRequest.getHeader(TokenAuthenticationService.HEADER_STRING);
			if (null==header || !header.startsWith(TokenAuthenticationService.TOKEN_PREFIX) ) {
				chain.doFilter(request, response);
				return;
			}
			
			
			Authentication authentication = TokenAuthenticationService.getAuthentication(httpRequest);

			// 
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        chain.doFilter(request,response);
	        
		}catch(Exception ex) {
			throw new BadCredentialsException("Rest接口安全验证时，请求Token是非法!!!");
		}

	}
    
}
