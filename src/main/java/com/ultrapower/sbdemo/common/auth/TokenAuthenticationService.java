package com.ultrapower.sbdemo.common.auth;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @ClassName:     TokenAuthenticationService
 * @Description:   JWT生成，和验签的类
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午4:18:41
 */
public class TokenAuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);
			
	static final long EXPIRATIONTIME = 86400_000;     // 24小时
	static final String SECRET = "Ultra@13";         // JWT密码  (8位)
	public static final String TOKEN_PREFIX = "Bearer ";        // Token前缀
	public static final String HEADER_STRING = "Authorization";// 存放Token的Header Key
	
	
	// JWT生成方法
	public static String addAuthentication(String username) throws Exception {
		try {
	        // 生成JWT
			String JWT = Jwts.builder()
	                // 保存权限（角色）
	                .claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
	                // 用户名写入标题
	                .setSubject(username)
	                // 有效期设置
	        		.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	                // 签名设置
	        		.signWith(SignatureAlgorithm.HS512, SECRET)
	        		.compact();

			  return JWT;
		}catch(Exception ex) {
			throw new BadCredentialsException("Rest接口安全验证时，生成jwt失败!!!");
		}
	}
	

	// JWT验证方法
	public static Authentication getAuthentication(HttpServletRequest request) throws Exception {
		try {
			// 从Header中拿到token
	        String token = request.getHeader(HEADER_STRING);

			if (null!=token && token.trim().length()>0) {
	            // 解析 Token
	            Claims claims = Jwts.parser()
	                    // 验签
						.setSigningKey(SECRET)
	                    // 去掉 Bearer
						.parseClaimsJws(token.trim().replace(TOKEN_PREFIX, ""))
						.getBody();

	            // 拿用户名
	            String user = claims.getSubject();

	            // 得到 权限（角色）
	            List<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

	            // 返回验证令牌
	            if (null!=user && user.trim().length()>0) {
	                return new UsernamePasswordAuthenticationToken(user, null, authorities);
	            }
			}
			return null; 
		}catch(Exception ex) {
			 throw new BadCredentialsException("Rest接口安全验证时，解析token失败!!!");
		}

	 }
}
