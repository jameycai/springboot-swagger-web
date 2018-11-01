package com.ultrapower.sbdemo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName:     提供日志输出方法
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:54:59 
 *
 */
public class LogManage {
	
	//定义类的输出
	//private static final Logger logger = LoggerFactory.getLogger(LogManage.class);
	
	
	/**
	 * 错误信息类异常信息输出
	 * @param classname
	 * @param ex
	 */
	public static void setLogErrorInfor(Object classname,Throwable ex){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.error(classname.toString()+"Error:"+ex);
	}
	/**
	 * 错误信息类异常信息输出
	 * @param classname
	 * @param ex
	 */
	public static void setLogErrorInfor(Object classname,String message,Throwable ex){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.error(message, ex);
	}
	/**
	 * 信息类信息输出
	 * @param classname
	 * @param ex
	 */
	public static void setLogInfor(Object classname,String strmessage){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.info(classname.toString()+" out put:"+strmessage);
	}
	/**
	 * 信息类信息输出
	 * @param classname
	 * @param ex
	 */
	public static void setLogInfor(Object classname,String strmessage,Throwable ex){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.info(strmessage+" out put:"+ex);
	}
	/**
	 * 信息类警告信息输出
	 * @param classname
	 * @param ex
	 */
	public static void setLogWarnInfor(Object classname,String strmessage,Throwable ex){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.warn(strmessage+"out warn:"+ex);
	}
	
	/**
	 * 信息类警告信息输出
	 * @param classname
	 * @param ex
	 */
	public static void setLogWarnInfor(Object classname,String strmessage){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.warn(strmessage);
	}
	/**
	 * 调试性信息类警告信息输出
	 * @param classname
	 * @param ex
	 */
	public static void setLogDebugInfor(Object classname,String strmessage){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.debug(strmessage);
	}
	/**
	 * 调试性信息类
	 * @param classname
	 * @param ex
	 */
	public static void setLogDebugInfor(Object classname,String strmessage,Throwable ex){
		
		Logger logg = LoggerFactory.getLogger(classname.getClass());
		logg.warn(strmessage,ex);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
            
		
		
	}

}
