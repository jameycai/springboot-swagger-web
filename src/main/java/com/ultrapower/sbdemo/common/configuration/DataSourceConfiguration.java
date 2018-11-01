package com.ultrapower.sbdemo.common.configuration;

import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.Arrays;

import com.alibaba.druid.pool.DruidDataSource;
import com.ultrapower.sbdemo.common.ConvertUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @ClassName:     DataSourceConfiguration
 * @Description:   对应xml中datasource的配置
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:54:59 
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
public class DataSourceConfiguration implements EnvironmentAware  {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);
	 
    private Environment env;

    
	@Override
	public void setEnvironment(Environment environment) {
        this.env = environment;
	}
    
    //注册dataSource
    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource()  throws SQLException {
      	
    	try {
    		if (StringUtils.isEmpty(env.getProperty("spring.datasource.url"))) {
            	logger.info("Your database connection pool configuration is incorrect!"
                        + " Please check your Spring profile, current profiles are:"
                        + Arrays.toString(env.getActiveProfiles()));
                throw new ApplicationContextException(
                        "Database connection pool is not configured correctly");
            }
            DruidDataSource druidDataSource = new DruidDataSource();
            String driverclassname = ConvertUtil.Obj2Str(env.getProperty("spring.datasource.driver-class-name"), "");
            druidDataSource.setDriverClassName(driverclassname);
            druidDataSource.setUrl(env.getProperty("spring.datasource.url"));
            druidDataSource.setUsername(env.getProperty("spring.datasource.username"));
            String password = env.getProperty("spring.datasource.password");
            druidDataSource.setPassword(password);
            
            druidDataSource.setInitialSize(ConvertUtil.Obj2int(env.getProperty("spring.datasource.initialSize"), 5));
            druidDataSource.setMinIdle(ConvertUtil.Obj2int(env.getProperty("spring.datasource.minIdle"), 2));
            druidDataSource.setMaxActive(ConvertUtil.Obj2int(env.getProperty("spring.datasource.maxActive"), 100));
            druidDataSource.setMaxIdle(50);
            druidDataSource.setMaxWait(ConvertUtil.Obj2long(env.getProperty("spring.datasource.maxWait"), 60000));
           
            // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            druidDataSource.setTimeBetweenEvictionRunsMillis(ConvertUtil.Obj2long(env.getProperty("spring.datasource.timeBetweenEvictionRunsMillis"), 60000));
            // 配置一个连接在池中最小生存的时间，单位是毫秒
            druidDataSource.setMinEvictableIdleTimeMillis(ConvertUtil.Obj2long(env.getProperty("spring.datasource.minEvictableIdleTimeMillis"), 60000));
            druidDataSource.setValidationQuery(ConvertUtil.Obj2Str(env.getProperty("spring.datasource.validationQuery"), ""));
        	druidDataSource.setTestWhileIdle(Boolean.parseBoolean(env.getProperty("spring.datasource.testWhileIdle")));
        	druidDataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("spring.datasource.testOnBorrow")));
        	druidDataSource.setTestOnReturn(Boolean.parseBoolean(env.getProperty("spring.datasource.testOnReturn")));
	    	// 打开PSCache，并且指定每个连接上PSCache的大小
	    	druidDataSource.setPoolPreparedStatements(true);
	    	druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
	    	try {
				druidDataSource.setFilters("config,stat,log4j");
			} catch (SQLException e) {
				logger.error("druid configuration initialization filter", e);
			}
	    	druidDataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");

            return druidDataSource;
    	}catch(Exception ex) {
    		logger.error("druidDataSource error!! ", ex);
            return null;
    	}
        
    }

 


}
