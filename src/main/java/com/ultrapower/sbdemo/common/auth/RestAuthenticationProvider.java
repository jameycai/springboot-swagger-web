package com.ultrapower.sbdemo.common.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ultrapower.sbdemo.common.auth.bean.GrantedAuthorityBean;
import com.ultrapower.sbdemo.webapp.home.utils.CasMD5;

import java.util.ArrayList;

/**
 * 
 * @ClassName:     RestAuthenticationProvider
 * @Description:   自定义Rest接口身份认证验证组件
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午4:19:39
 */
public class RestAuthenticationProvider implements AuthenticationProvider  {

	private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationProvider.class);
		
	private UserDetailsService userDetailsService;

    public RestAuthenticationProvider(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
			
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	// 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 认证逻辑
        if(null==name || name.trim().length()==0 || null==password || password.trim().length()==0) {
        	throw new BadCredentialsException("Rest接口安全验证时，请求用户或密码不能为空!!!");
        }else {
        	
            // 认证逻辑
            UserDetails userDetails = userDetailsService.loadUserByUsername(name.trim());
        	if(null==userDetails || null==userDetails.getUsername()) {
        		throw new BadCredentialsException("Rest接口安全验证时，请求用户或密码不正确!!!");
        	}else {
        		// 对请求用户和密码，使用PASM端MD5进行加密
        		String codePass = CasMD5.getMD5Code(password.trim());
        		
        		String userPass = userDetails.getPassword();        		
        		if(null!=codePass && codePass.equals(userPass)) {
        			// ============== 通过认证=============== 
        			
        			// 这里设置权限和角色
                    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add( new GrantedAuthorityBean("ROLE_ADMIN") );
                    authorities.add( new GrantedAuthorityBean("AUTH_WRITE") );
                    // 生成令牌
                    Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);
                    return auth;
               
                }else {
                    throw new BadCredentialsException("Rest接口安全验证时，请求用户或密码不正确!!!");
                }
        	}
        }

    }

    
    // 是否可以提供输入类型的认证服务
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }



}
