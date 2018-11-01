package com.ultrapower.sbdemo.common.apiver;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 
 * @ClassName:     CustomRequestMappingHandlerMapping
 * @Description:   自定义版本号
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月23日 下午4:19:39
 */
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	
    @Override
    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(apiVersion);
    }
 
    @Override
    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }
     
    private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
    }
    
}
