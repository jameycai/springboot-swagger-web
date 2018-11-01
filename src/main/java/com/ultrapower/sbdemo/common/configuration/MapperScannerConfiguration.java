package com.ultrapower.sbdemo.common.configuration;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName:     MapperScannerConfiguration
 * @Description:   扫描mybatis的接口
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:54:59 
 *
 */
@Configuration
// 因为这个对象的扫描，需要在MyBatisConfig的后面注入，所以加上下面的注解
@AutoConfigureAfter(DataSourceConfiguration.class)
public class MapperScannerConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(MapperScannerConfiguration.class);
	 
	@Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        //创建内置的config
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //获取之前注入的beanName为sqlSessionFactory的对象
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //指定mapper接口的路径 ,批量加上@Mapper ,也可以手动加上 ,省略此配置
        //会触发 No MyBatis mapper was found in '[com.ultrapower.sbdemo.webapp.controller, com.ultrapower.sbdemo.webapp.servcie]' package.  警告
        mapperScannerConfigurer.setBasePackage("com.ultrapower.sbdemo.webapp.*.mapper");
        return mapperScannerConfigurer;
    }
	
	
}
