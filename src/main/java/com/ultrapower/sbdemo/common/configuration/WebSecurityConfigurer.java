package com.ultrapower.sbdemo.common.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ultrapower.sbdemo.common.auth.RestAuthenticationProvider;
import com.ultrapower.sbdemo.common.filter.JWTAuthenticationFilter;
import com.ultrapower.sbdemo.common.filter.JWTLoginFilter;

import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * 
 * @ClassName:     WebSecurityConfigurer
 * @Description:   SpringSecurity的配置
 *                  (通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起，是restful的安全验证)
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月22日 下午5:22:36
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter  {

	private UserDetailsService userDetailsService;
	
	public WebSecurityConfigurer(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	   
    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
        	.and().csrf().disable() // 关闭csrf验证
        	.authorizeRequests()  // 对请求进行认证
        	///.antMatchers(HttpMethod.POST, "/api/users/sign").permitAll()  // 所有 /users/signup 的POST请求 都放行
            .antMatchers(HttpMethod.POST, "/api/**/users/sign").permitAll() // 所有 /users/signup 的POST请求 都放行
            .antMatchers("/api/**").authenticated()  // 所有rest请求需要身份认证
            ////.anyRequest().authenticated()  // 所有请求需要身份认证
           .and()
             .headers().frameOptions().disable() // 容许被嵌套 
           .and()
           // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
             ///.addFilterBefore(new JWTLoginFilter("/api/users/sign",authenticationManager()), UsernamePasswordAuthenticationFilter.class)
             .addFilterBefore(new JWTLoginFilter("/api/**/users/sign",authenticationManager()), UsernamePasswordAuthenticationFilter.class)
              // 添加一个过滤器验证其他请求的Token是否合法
		     .addFilterBefore(new JWTAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
         
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        // 使用自定义身份验证组件
        //////auth.authenticationProvider(new RestAuthenticationProvider(userDetailsService,bCryptPasswordEncoder));
    	// 使用自定义身份验证组件
        auth.authenticationProvider(new RestAuthenticationProvider(userDetailsService));
    }

    
    @Override
    public void configure(WebSecurity web) throws Exception {
    	web.ignoring().antMatchers("/static/**");
    }
    
    
}
