package com.ultrapower.sbdemo.common.filter;

import com.alibaba.fastjson.JSON;
import com.ultrapower.sbdemo.common.auth.TokenAuthenticationService;
import com.ultrapower.sbdemo.common.utils.JsonResultBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName:     JWTLoginFilter
 * @Description:   JWT验证用户名密码正确后，生成一个token，并将token返回给客户端
 *                (1).attemptAuthentication - 登录时需要验证时候调用
 *                (2).successfulAuthentication - 验证成功后调用
 *                (3).unsuccessfulAuthentication - 验证失败后调用，这里直接灌入403错误返回
 *                
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午4:22:48
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter  {

	private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);
			
			
	public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
	}

	
	//  登录时需要验证时候调用
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {

		try {
//			ServletInputStream stream = req.getInputStream();
			String username = req.getParameter("username");
			String password = req.getParameter("password");
		   
			// JSON反序列化成 AccountCredentials
//			AccountCredentials creds = new ObjectMapper().readValue(stream, 
//					AccountCredentials.class);

			// 返回一个验证令牌
	        return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							username,
							password
					)
			);
		}catch(Exception ex) {
			 throw new RuntimeException(ex);
		}

	}

	// 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
	@Override
	protected void successfulAuthentication(
			HttpServletRequest req,
			HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {


		try {
			// 获取生成的JWT
			String token = TokenAuthenticationService.addAuthentication(auth.getName());
						
			// 设置HttpServletResponse的header
			res.addHeader(TokenAuthenticationService.HEADER_STRING, TokenAuthenticationService.TOKEN_PREFIX + token);
			
			
			// 设置输出body
            res.setContentType("application/json");
            res.setStatus(HttpServletResponse.SC_OK);
            
            JsonResultBean r = new JsonResultBean();
            if(null!=token && token.trim().length()>0) {
                r.setStatus(JsonResultBean.SUCCESS);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 200);
                map.put(TokenAuthenticationService.HEADER_STRING, TokenAuthenticationService.TOKEN_PREFIX + token);
                r.setResult(map);
            }else {
                r.setStatus(JsonResultBean.FAIL);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 500);
                map.put("message", "Failure to generate token!!!");
                r.setResult(map);
            }

            res.getOutputStream().println(JSON.toJSONString(r));
        } catch (Exception ex) {
        	throw new BadCredentialsException("Rest接口安全验证时，生成token时候失败!!!");
        }
	}

    //  验证失败后调用
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		try {
			// 设置输出body
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			
            JsonResultBean r = new JsonResultBean();
            r.setStatus(JsonResultBean.FAIL);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", "403.7");
            map.put("message", "Prohibition: user certificates are required!!!");
            r.setResult(map);

            response.getOutputStream().println(JSON.toJSONString(r));
        } catch (IOException ex) {
        	logger.error("error!", ex);
            ex.printStackTrace();
        }		
       
    }
    

}
