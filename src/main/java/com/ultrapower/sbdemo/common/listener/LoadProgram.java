package com.ultrapower.sbdemo.common.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ultrapower.sbdemo.common.ConvertUtil;
import com.ultrapower.sbdemo.common.LogManage;
import com.ultrapower.sbdemo.common.SysProperties;

/**
 * 
 * @ClassName:     LoadProgram
 * @Description:   初始化加载类 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月26日 上午9:41:41 
 *
 */
public class LoadProgram implements ServletContextListener  {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadProgram.class);
	

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try{
			
			// ==================== No.1 初始化jndi信息，通过解析application.properties
			try {
				initJndiPropertiesByApplication();
				LogManage.setLogInfor("LoadProgram", "No.1--initJndiPropertiesByApplication Init...over");
			}catch(Exception ex) {
				LogManage.setLogErrorInfor(LoadProgram.class, "No.1--initJndiPropertiesByApplication Init error!!! [initJndiPropertiesByApplication is fail]," +
						"Please check the path:= /resources/application.properties configuration is normal." +
						"[请检查/resources/application.properties 配置是否正常]" , ex);
				ex.printStackTrace();
			}
			
			
			
			
		}catch(Exception ex){
			logger.error("LoadProgram init error!!!", ex);
		}
	}

	
	
	
	/**
	 * @Title:initJndiPropertiesByApplication
	 * @Description: 初始化jndi信息，通过解析application.properties
	 * @author caijinpeng
	 */
	private void initJndiPropertiesByApplication() {
		Properties sysProp = System.getProperties();
		try {
			String drivername = SysProperties.getProperty("spring.datasource.driver-class-name", "application.properties");
			sysProp.put("jdbc.drivername", drivername);
			sysProp.put("jdbc.url", SysProperties.getProperty("spring.datasource.url", "application.properties"));
			sysProp.put("jdbc.username", SysProperties.getProperty("spring.datasource.username", "application.properties"));
			sysProp.put("jdbc.password", SysProperties.getProperty("spring.datasource.password", "application.properties"));
			sysProp.put("jdbc.isDbPwdSecurity", SysProperties.getProperty("spring.datasource.isDbPwdSecurity", "application.properties"));
			sysProp.put("jdbc.maxActive", ConvertUtil.Obj2int(SysProperties.getProperty("spring.datasource.maxActive", "application.properties"), 20));
			sysProp.put("jdbc.minIdle", ConvertUtil.Obj2int(SysProperties.getProperty("spring.datasource.minIdle", "application.properties"), 5));
			sysProp.put("jdbc.maxWait", ConvertUtil.Obj2int(SysProperties.getProperty("spring.datasource.maxWait", "application.properties"), 60000));
			sysProp.put("jdbc.poolsize", 10);
			
			String dbType = "mysql";
			if(null!=drivername && drivername.trim().length()>0) {
				if(drivername.toLowerCase().indexOf("oracle") != -1){ //oracle
					dbType = "oracle";
				}else if(drivername.toLowerCase().indexOf("sybase") != -1){ //sybase
					dbType = "sybase";
				}else if(drivername.toLowerCase().indexOf("sqlserver") != -1){ //sqlserver
					dbType = "sqlserver";
				}else if(drivername.toLowerCase().indexOf("mysql") != -1){ //sqlserver
					dbType = "mysql";
				}
			}
			sysProp.put("jdbc.dbType", dbType);
		} catch (Exception ex) {
			LogManage.setLogErrorInfor(LoadProgram.class, "init jndi信息，通过解析application.properties error! application.properties中jndi文件失败!  ", ex);
			ex.printStackTrace();
		}
		System.setProperties(sysProp);
		System.out.println(sysProp.getProperty("jdbc.url"));
		System.out.println(sysProp.getProperty("jdbc.dbType"));
	}
	
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}
}
