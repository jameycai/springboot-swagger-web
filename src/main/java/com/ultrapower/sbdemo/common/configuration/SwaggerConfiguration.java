package com.ultrapower.sbdemo.common.configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.fasterxml.classmate.TypeResolver;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @ClassName:     SwaggerConfiguration
 * @Description:   Restful接口文档配置
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:54:59 
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    
    @Autowired
    private TypeResolver typeResolver;
    
    
	@Bean
	public Docket createRestApi() {
		
		// 添加header参数 
		String auth = "";
		ParameterBuilder tokenBuilder = new ParameterBuilder();
		tokenBuilder.name("Authorization").defaultValue(auth).description("Token值").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
	
		// 添加版本参数
		String ver = "v1.0";
		ParameterBuilder versionBuilder = new ParameterBuilder();
		versionBuilder.name("version").defaultValue(ver).description("API版本号(如：v1.0)").modelRef(new ModelRef("string")).parameterType("path").required(true).build();
	
		
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(tokenBuilder.build());
		parameters.add(versionBuilder.build());
		
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(false)
				.globalOperationParameters(parameters)
				.select()
				///.apis(RequestHandlerSelectors.basePackage("com.ultrapower.nmsc.webapp.home.controller.swagger"))  
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(
                		 AlternateTypeRules.newRule(
                	           typeResolver.resolve(Map.class, String.class,
                	           typeResolver.resolve(Map.class, String.class, typeResolver.resolve(List.class, String.class))),
                	           typeResolver.resolve(Map.class, String.class, WildcardType.class)))
                .useDefaultResponseMessages(false);
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Restful接口")
				.description("Restful接口")
				.termsOfServiceUrl("")
				.version("7.0.0")
				///.contact(new Contact("","", "jamey_cai@163.com"))
				.build();
	}
	
	
}
