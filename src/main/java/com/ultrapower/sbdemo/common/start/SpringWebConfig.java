package com.ultrapower.sbdemo.common.start;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.collect.Maps;
import com.ultrapower.sbdemo.common.filter.RestVisitProtectFilter;
import com.ultrapower.sbdemo.common.filter.UltraFilterDispatcher;
import com.ultrapower.sbdemo.common.listener.InitWebInfoServletListener;
import com.ultrapower.sbdemo.common.listener.LoadProgram;
import com.ultrapower.sbdemo.common.listener.SessionCycleListener;
  

/**
 * @ClassName:     SpringWebConfig
 * @Description:   Web初始化配置  
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月25日 下午3:15:29 
 *
 */
@Configuration
public class SpringWebConfig {

    @Autowired 
    private Environment environment;
	
	private static boolean casEnabled  = true;  
	  
	
    /** 
     * 该过滤器用于实现rest访问保护，当访问量超过一定数据量就不让访问，一定要放在其他filter之前 
     */  
    @Bean  
    public FilterRegistrationBean<RestVisitProtectFilter> restVisitProtectFilter() {  
        FilterRegistrationBean<RestVisitProtectFilter> filterRegistration = new FilterRegistrationBean<RestVisitProtectFilter>();  
        filterRegistration.setFilter(new RestVisitProtectFilter());  
        filterRegistration.setEnabled(casEnabled);  
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(2);   
        return filterRegistration;    
    }  
    
    
    
    /** 
     * 该过滤器用于实现字符过滤、转换、sql注入
     */  
    @Bean  
    public FilterRegistrationBean<UltraFilterDispatcher> ultraDispatcherFilter() {  
        String securityScan = environment.getProperty("springboot.webclient.securityScan");
        FilterRegistrationBean<UltraFilterDispatcher> filterRegistration = new FilterRegistrationBean<UltraFilterDispatcher>();  
        filterRegistration.setFilter(new UltraFilterDispatcher());  
        filterRegistration.setEnabled(casEnabled);  
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(10);  

        //开启安全扫描时 不排除以下路径
        if(!"true".equals(securityScan)) {
            Map<String, String> initParameters = Maps.newHashMap(); 
            initParameters.put("excludes", "/favicon.ico,/images/*,/js/*,/css/*,/fonts/*");  // 配置例外 
            initParameters.put("xframemode", "SAMEORIGIN");  
            filterRegistration.setInitParameters(initParameters);  
        }

        return filterRegistration;    
    }  
    
    
    /** 
     * 系统初始化加载 配置  LoadProgram
     */  
    @Bean  
    public ServletListenerRegistrationBean<LoadProgram> singleSignOutHttpSessionListener() {  
        ServletListenerRegistrationBean<LoadProgram> listener = new ServletListenerRegistrationBean<LoadProgram>();  
        listener.setEnabled(true);  
        listener.setListener(new LoadProgram());  
        listener.setOrder(11);  
        return listener;  
    }  
    
    
    
    /** 
     * session周期检测 
     */  
    @Bean  
    public ServletListenerRegistrationBean<SessionCycleListener> sessionCycleListener() {  
        ServletListenerRegistrationBean<SessionCycleListener> listener = new ServletListenerRegistrationBean<SessionCycleListener>();  
        listener.setEnabled(true);  
        listener.setListener(new SessionCycleListener());  
        listener.setOrder(12);  
        return listener;  
    }  
    
    
    
    
    
    
    
    /** 
     * 初始化web信息打印
     */  
    @Bean  
    public ServletListenerRegistrationBean<InitWebInfoServletListener> initWebInfoServletListener() {  
        ServletListenerRegistrationBean<InitWebInfoServletListener> listener = new ServletListenerRegistrationBean<InitWebInfoServletListener>();  
        listener.setEnabled(true);  
        listener.setListener(new InitWebInfoServletListener());  
        listener.setOrder(99);  
        return listener;  
    }  
    
    
}
