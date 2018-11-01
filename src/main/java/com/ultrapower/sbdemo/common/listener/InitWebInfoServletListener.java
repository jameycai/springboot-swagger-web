package com.ultrapower.sbdemo.common.listener;

import java.net.InetAddress;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ultrapower.sbdemo.common.LogManage;
import com.ultrapower.sbdemo.common.SysProperties;
import com.ultrapower.sbdemo.common.utils.InetAddressUtil;

/**
 * 
 * @ClassName:     InitWebInfoServlet
 * @Description:   初始化web信息打印  
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月25日 上午11:30:44
 */
public class InitWebInfoServletListener implements ServletContextListener  {

	private static final Logger logger = LoggerFactory.getLogger(InitWebInfoServletListener.class);

	
	/** 换行(Windows、Mac、Unix/Linux) **/
	private String linefeed = "\r\n";
	
	
	/** 获取服务器的名称和版本 serverInfo **/
	private String ServerInfo = "";
	
	
	/** FreeMemory **/
	private long freeMemory = 0;
	/** FreeMemory **/
	private long maxMemory = 0;
	/** FreeMemory **/
	private long totalMemory = 0;
	/** 常量 kbUtil **/
	private static final long kbUtil = 1048576L;
	
	
//	// PASM配置的 服务器IP
//	private String PASM_Server_ip = "";
//	// PASM配置的 jms端口
//	private String PASM_Jms_port = "";
//	// PASM配置的 rmi端口
//	private String PASM_rmi_port = "";			
		
  
	@Override
	public void contextInitialized(ServletContextEvent sce) {  
        // TODO Auto-generated method stub  
		try {
			//==================获取服务器的名称和版本 --- by caijinpeng
			ServletContext application = sce.getServletContext();
			ServerInfo = application.getServerInfo();
		} catch (Exception ex) {
			//
		}
		
		
		try{
			//=================根据操作系统，设置“换行”符号 -- by caijinpeng
			String osName = System.getProperty("os.name");
			if(null!=osName && osName.trim().toLowerCase().startsWith("win") ){ // Windows
				linefeed = "\r\n";
			}else if(null!=osName && osName.trim().toLowerCase().startsWith("mac")){ // Mac
				linefeed = "\n";
			}else if(null!=osName && (osName.trim().toLowerCase().indexOf("linux")>=0
					|| osName.trim().toLowerCase().indexOf("solaris")>=0
					|| osName.trim().toLowerCase().indexOf("sunos")>=0
					|| osName.trim().toLowerCase().indexOf("hp-ux")>=0
					|| osName.trim().toLowerCase().indexOf("aix")>=0
					|| osName.trim().toLowerCase().indexOf("freebsd")>=0
					|| osName.trim().toLowerCase().indexOf("unix")>=0)){ // Unix/Linux
				linefeed = "\n";
			}else { //Other
				linefeed = "\n";
			}
		}catch(Exception ex){
			//
		}
		
		
		try{
			// ================= 打印 Springboot Web系统信息 -- by caijinpeng
			//logger.info(getWebInfo());
			LogManage.setLogInfor("InitWebInfoServlet", getWebInfo());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
    }  
	
	
	
	
	/**
	 * 获取 Web信息
	 * @return
	 * @author caijinpeng
	 */
	public String getWebInfo(){
	    // 获取JVM内存信息 
	    getJVMMemory();
	    
	    
	    // ========== 打印日志信息 - by caijinpeng
	    StringBuffer nmswInfo = new StringBuffer();
	    nmswInfo.append(linefeed);
	    nmswInfo.append("*******************************************[Spring boot WebServer] Information **************************************************** ");
		
	    //================= 系统信息
	    nmswInfo.append(linefeed+"\t");
	    nmswInfo.append("System Information:");
	    nmswInfo.append(linefeed+"\t\t");
	    nmswInfo.append("Operating System:---------"+getOSInfo());
	    nmswInfo.append(linefeed+"\t\t");
	    nmswInfo.append("Host Information:---------"+getHostAddress());			
	    nmswInfo.append(linefeed+"\t\t");
	    nmswInfo.append("JAVA Vendor:--------------"+getJavaVendor());			
	    nmswInfo.append(linefeed+"\t\t" );
	    nmswInfo.append("JAVA Version:-------------"+getJavaVersion());
	    nmswInfo.append(linefeed+"\t\t");
	    nmswInfo.append("JAVA HOME:----------------"+getJAVA_HOME());
	    nmswInfo.append(linefeed+"\t\t");
	    nmswInfo.append("Server Information:-------"+ServerInfo );	
				
	    //===================JVM内存信息
	    nmswInfo.append(linefeed+"\t" );
	    nmswInfo.append("JVM Memory Information:" );
	    nmswInfo.append(linefeed+"\t\t" );
	    nmswInfo.append("MaxMemory:----------------"+(maxMemory/kbUtil)+"M" );
	    nmswInfo.append(linefeed+"\t\t"  );
	    nmswInfo.append("TotalMemory:--------------"+(totalMemory/kbUtil)+"M"  );
	    nmswInfo.append(linefeed+"\t\t"  );
	    nmswInfo.append("FreeMemory:---------------"+(freeMemory/kbUtil)+"M"  );
				
	    //================= Web信息
	    nmswInfo.append(linefeed+"\t");
	    nmswInfo.append("Web Information:");
	    nmswInfo.append(linefeed+"\t\t" );
	    ///if(null!=ServerInfo && ServerInfo.indexOf("Tomcat")>0){
	   // nmswInfo.append("Web Version:-----------"+getSpringWebVersion() );
	   // nmswInfo.append(linefeed+"\t\t" );
	    ///}
	    nmswInfo.append("Web Path:-----------------"+getProjectPath());			
	    nmswInfo.append(linefeed+"\t\t" );
	    nmswInfo.append("Web URL:------------------"+getWebUrl());
	    nmswInfo.append(linefeed+"\t\t" );
	    nmswInfo.append("Restful API Doc:----------"+getSwaggerUrl());
	    
	    //================= NmsServer/Sigmam信息
	    nmswInfo.append(linefeed+"\t");
	    nmswInfo.append("Server/Sigmam Information:");
	    // 获取ZK标志
	    ///nmswInfo.append(linefeed+"\t\t" );
	    ////nmswInfo.append("zkConnect URL:------------"+getzkConnect());
	    
	    nmswInfo.append(linefeed+"\t\t" );
	    nmswInfo.append("JNDI URL:-----------------"+getJndiUrl());
	    
	    nmswInfo.append(linefeed+"\t\t" );
	    nmswInfo.append("DateBase Type:------------"+getDBTpye());

	    
	    
//	    //===================PASM配置信息
//	    nmswInfo.append(linefeed+"\t");
//	    nmswInfo.append("PASM Information:");
////		nmswInfo.append(linefeed+"\t\t");
////		nmswInfo.append("Whether integration:------"+ConfigProperties.PASM_RUN);
////		nmswInfo.append(linefeed+"\t\t");
////		nmswInfo.append("APPNAME:------------------"+"NMS");
//	    nmswInfo.append(linefeed+"\t\t" );
//	    nmswInfo.append("PASM ServerIP:------------"+PASM_Server_ip );
//	    nmswInfo.append(linefeed+"\t\t" );
//	    nmswInfo.append("PASM JMS Port:------------"+PASM_Jms_port );
//	    nmswInfo.append(linefeed+"\t\t");
//	    nmswInfo.append("PASM RMI Port:------------"+PASM_rmi_port );
	    nmswInfo.append(linefeed+"************************************************************************************************************************************ ");	
		
	    return nmswInfo.toString();
	}
	
	
	
	/**
	 * 获取操作系统信息
	 * @return
	 * @author caijinpeng
	 */
	private String getOSInfo() {
		// 系统信息
		String osInfo = "";
		try{
			String osName = System.getProperty("os.name");
			String osVersion = System.getProperty("os.version");
			String osArch = System.getProperty("os.arch");
			String osencoding = System.getProperty("file.encoding");
			osencoding = osencoding == null ? "" : osencoding;
			String osLang = System.getProperty("user.language");
			osLang = osLang == null ? "" : osLang;
			String osRegion = System.getProperty("user.region");
			osRegion = osRegion == null ? "" : osRegion;
			osInfo = "Name:" + osName + ", Version:" + osVersion + ", Arch:" + osArch + ", Lang:" + osLang + ", Region:" + osRegion + ", Encoding:" + osencoding;
		}catch(Exception ex){
		}
		return osInfo;
	}

	
	/**
	 * 获取主机的IP地址和主机名
	 * @return
	 * @author caijinpeng
	 */
	private String getHostAddress(){
		// 系统信息
		String hostIp = "";
		try{
			InetAddress addr = InetAddress.getLocalHost();
		    String ip = addr.getHostAddress();
			hostIp = "HostAddress:" + ip + ", HostName:" + addr.getHostName();
		}catch(Exception ex){
		}
		return hostIp;
	}
	
	
	/**
	 * 获取JAVA厂商
	 * @return
	 * @author caijinpeng
	 */
	private String getJavaVendor(){
		String javaVendor = "";
		try{
			//Java 运行时环境供应商  
			String jreVendor = System.getProperty("java.vendor");
			//Java 虚拟机实现供应商  
			String vmVendor = System.getProperty("java.vm.vendor");
			if(jreVendor.equals(vmVendor)){
				javaVendor  = "" + jreVendor;
			}else{
				javaVendor = "JRE:" + jreVendor+", JDK:"+vmVendor;
			}
		}catch(Exception ex){
		}
		return javaVendor;
	}
	
	/**
	 * 获取JAVA版本
	 * @return
	 * @author caijinpeng
	 */
	private String getJavaVersion(){
		String javaVersion= "";
		try{
			//Java 运行时环境版本  
			String jreVersion = System.getProperty("java.version");
			//Java 虚拟机实现版本 
			String vmSpecVersion = System.getProperty("java.vm.specification.version");
			String vmVersion = System.getProperty("java.vm.version");
			String classVer = System.getProperty("java.class.version");
			String javaArch = System.getProperty("sun.arch.data.model");
			
			javaVersion = "JDK:"+jreVersion+ ", VM Specification:"+vmSpecVersion+ ", VM:"+vmVersion+ ", Class:"+classVer+ ", Arch:"+javaArch;
		}catch(Exception ex){
		}
		return javaVersion;
	}

	
	/**
	 * 获取JAVA_HOME
	 * @return
	 * @author caijinpeng
	 */
	private String getJAVA_HOME(){
		String java_home = "";
		try{
			java_home = System.getProperty("java.home").replace("\\jre", "");
		}catch(Exception ex){
		}
		return java_home;
	}
	
	
	/***
	 * 获取JVM内存
	 * @author caijinpeng
	 */
	private void getJVMMemory(){
	    try{
			freeMemory = Runtime.getRuntime().freeMemory();
		    maxMemory = Runtime.getRuntime().maxMemory();
		    totalMemory = Runtime.getRuntime().totalMemory();
	    }catch(Exception ex){
	    }
	}
	
	
	
	/**
	 * 访问工程名
	 * @return
	 * @author caijinpeng
	 */
	private String getProjectPath(){
		String projectPath = "";
		try{
			projectPath = SysProperties.getProperty("server.servlet.context-path", "application.properties");
			if(null!=projectPath && projectPath.trim().length()>1
					&& projectPath.lastIndexOf("/")>=0){
				projectPath = projectPath.substring(1, projectPath.length());
			}
		}catch(Exception ex){
		}
		return projectPath;
	}
	
	
	/**
	 * 获取Web访问路径
	 * @return
	 * @author caijinpeng
	 */
	private String getWebUrl() {
		///String ip1 = InetAddress.getLocalHost().getHostAddress();
		///String ip2 = "192.168.19.129";
		///String port = "8080";
		String urlAddress = "";
		try{
			String url = InetAddressUtil.getLocalIp();
			String port = SysProperties.getProperty("server.port", "application.properties");
			String name = SysProperties.getProperty("server.servlet.context-path", "application.properties");
			
			urlAddress = "http://" + url + ":"+port+ name;
		}catch(Exception ex){
		}
		return urlAddress;
	}
	
	/**
	 * 获取Restful访问路径
	 * @return
	 * @author caijinpeng
	 */
	private String getSwaggerUrl() {
		///String ip1 = InetAddress.getLocalHost().getHostAddress();
		///String ip2 = "192.168.19.129";
		///String port = "8080";
		String urlAddress = "";
		try{
			String url = InetAddressUtil.getLocalIp();
			String port = SysProperties.getProperty("server.port", "application.properties");
			String name = SysProperties.getProperty("server.servlet.context-path", "application.properties");
			
			urlAddress = "http://" + url + ":"+port+ name+"/swagger-ui.html";
		}catch(Exception ex){
		}
		return urlAddress;
	}
    
	
   
    /**
     * 获取sigmam的zk
	 * @return
	 * @author caijinpeng
     */
    private String getzkConnect(){
    	String zkconnect = "";
    	try{
    		zkconnect = System.getProperty("zkconnect");
    	}catch(Exception ex){
    	}
    	return zkconnect;
    }
    
    /***
     * 获取jndi的url
	 * @return
	 * @author caijinpeng
     */
    private String getJndiUrl(){
    	String jndiurl = "";
    	try{
    		//jndiurl = SysProperties.getProperty("java.naming.provider.url", "jndi.properties");
    		jndiurl = System.getProperty("jdbc.url");
    		
    	}catch(Exception ex){
    	}
    	return jndiurl;
    }
    
    
    /***
     * 获取jndi中数据库类型
	 * @return
	 * @author caijinpeng
     */
    private String getDBTpye(){
    	String dbType = "";
    	try{
    		dbType = System.getProperty("jdbc.dbType");
    	}catch(Exception ex){
    	}
    	return dbType;
    }
    
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}
}
