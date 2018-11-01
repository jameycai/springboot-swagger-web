package com.ultrapower.sbdemo.common.apiver;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
import org.springframework.web.bind.annotation.Mapping;  
  
/**
 * 
 * @ClassName:     ApiVersion
 * @Description:   API版本号 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年3月21日 上午11:23:55
 */
@Target({ElementType.METHOD,ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Mapping  
public @interface ApiVersion {

	/** 
     * 标识版本号 
     * @return 
     */  
    double value() default 1.0;  
    
}
