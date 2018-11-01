package com.ultrapower.sbdemo.common.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ultrapower.sbdemo.common.apiver.CustomRequestMappingHandlerMapping;
import com.ultrapower.sbdemo.common.interceptor.UserSecurityInterceptor;

/**
 * 
 * @ClassName:     MVCConfiguration 
 * @Description:   MVC的配置
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:54:59 
 *
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport  {

	private static final Logger logger = LoggerFactory.getLogger(WebMvcConfiguration.class);
			
	/**
	 * 设置web的启动默认页面 
	 */
	@Override
    public void addViewControllers(ViewControllerRegistry registry ) {
        registry.addViewController("/").setViewName("forward:/web/home/index"); // 默认页面(跳转到java路径)
        ///registry.addRedirectViewController("/", "/swagger-ui.html"); // 默认页面 (跳转到页面路径)
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers( registry );
    } 
	
	
    /**
     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。
     * 需要重新指定静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");  
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/swagger-ui/");
        super.addResourceHandlers(registry);
    }
 

    /**
     * 配置servlet处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    
    
    // 设置跨域访问
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
	        .allowedOrigins("*")
	        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
	        .allowedHeaders("*")
	        .allowCredentials(true)
	        .maxAge(3600);
    }
    
    
    

    /**
     * 配置拦截器 ，判断用户是否正常登录
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        InterceptorRegistration addInterceptor = 
        		registry.addInterceptor(new UserSecurityInterceptor());
        // 排除配置
        addInterceptor.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", 
        		"/**/*.eot","/**/*.svg","/**/*.ttf","/**/*.woff","/**/*.woff2","/**/*.otf,/**.ico");
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/404");
        addInterceptor.excludePathPatterns("/login**");
        addInterceptor.excludePathPatterns("/logout");
        addInterceptor.excludePathPatterns("/rest/**");
        
        // 拦截配置
        addInterceptor.addPathPatterns("/web/**");
        
        super.addInterceptors(registry);
    }


    

    /**
     * 添加restful的版本号
     */
    @Override  
    @Bean  
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {  
        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        return handlerMapping;
    }  

    
}
