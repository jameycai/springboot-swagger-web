package com.ultrapower.sbdemo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import com.ultrapower.sbdemo.common.configuration.DataSourceConfiguration;
import com.ultrapower.sbdemo.common.configuration.MapperScannerConfiguration;

/**
 * 
 * @ClassName:     ServletInitializer
 * @Description:   SpringBoot初始化
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月26日 上午9:44:58 
 *
 */
@Import({DataSourceConfiguration.class, MapperScannerConfiguration.class})
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootWebApplication.class);
	}

}
