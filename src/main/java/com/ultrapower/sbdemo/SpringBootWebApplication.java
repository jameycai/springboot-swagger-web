package com.ultrapower.sbdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @ClassName:     SpringBootWebApplication
 * @Description:   SpringBoot应用启动类
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月26日 上午9:45:07 
 *
 */
@SpringBootApplication
public class SpringBootWebApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootWebApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebApplication.class, args);
		logger.info("=============== SpringBoot WebServer Success !!! ==================");
	}
	
}
