package com.ultrapower.sbdemo.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName:     InetAddressUtil
 * @Description:   网络地址公用类
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月25日 上午11:28:07
 */
public class InetAddressUtil {
	
	//日志
	private static final Logger logger = LoggerFactory.getLogger(InetAddressUtil.class);
	
	private static String localHostHame = null;
	
	private static String localIp = null;
			
	/**
	 * 获取本地主机名称
	 * @return
	 */
	public static String getLocalHostName() {
		try {
			InetAddress inetAddr=InetAddress.getLocalHost();
			localHostHame =inetAddr.getHostName();
		} catch (Exception e2) {
			logger.error("localHostHame error!!", e2);
			e2.printStackTrace();
		}
		return localHostHame;		
	}
	
	/**
	 * 获取本地Ip地址
	 * 
	 * @return
	 */
	public static String getLocalIp() {
	    if(StringUtils.isEmpty(localIp)){ //如果localIp为空
	    	localIp = getHostIp();
	        
	        if(StringUtils.isEmpty(localIp)){
	        	try {
					localIp = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					logger.error("getLocalIp error!!", e);
					e.printStackTrace();
				}
	        }
	        
	    }
		
	    return localIp;
	}
	

	
	/**
	 * 获取Host的Ip地址
	 * @return
	 */
	private static String getHostIp(){
		try{
		    // 获取 Linux的Ip地址
			//logger.info("current os is linux");
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ipAddress = null;
			String ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				if(ni.getInetAddresses() == null){
					continue;
				}

				Enumeration<InetAddress> ipEm = ni.getInetAddresses();
				while(ipEm.hasMoreElements()){
					ipAddress = ipEm.nextElement(); 
					//logger.info("current ipaddress "+ipAddress.toString());
			        
					if (!ipAddress.isAnyLocalAddress()  // 当IP地址是通配符地址时
							&& !ipAddress.isLoopbackAddress() //  当IP地址是loopback地址
							&& !ipAddress.isLinkLocalAddress() // 当IP地址是本地连接地址（LinkLocalAddress）时
							&& !ipAddress.isMulticastAddress() // 当IP地址是广播地址（MulticastAddress）时
							&& !ipAddress.isMCGlobal()  // 当IP地址是全球范围的广播地址时
							&& !ipAddress.isMCLinkLocal() // 当IP地址是子网广播地址时
							&& !ipAddress.isMCNodeLocal() // 当IP地址是本地接口广播地址时
							&& !ipAddress.isMCOrgLocal() // 当IP地址是组织范围的广播地址时
							&& !ipAddress.isMCSiteLocal() // 当IP地址是站点范围的广播地址时
							&& ipAddress.getHostAddress().indexOf(":") == -1) {
						
						ip = ipAddress.getHostAddress();
						//logger.info("ipAddress getHostAddress:"+ip + ", networkName: "+ipAddress);
						
						if(null==ip || ip.trim().length()==0 || ip.equals("127.0.0.1") || ip.equals("192.168.56.1") ){
							continue;
						}
						//防止有的项目中，所有虚拟机存在一个共同地址，例如北京Bomc的192.168.122.1地址
						//可以统一在cmc的system.properties中进行地址过滤needFilterIp=192.168.122.1
						if(ip.equalsIgnoreCase(System.getProperty("needFilterIp")))
						{
							continue;
						}
						
						// 判断是否为合法ip 
						if(isIPv4Address(ip) || isIPv6Address(ip)){
							logger.info("InetAddressUtil host get ip="+ip);
							return ip;
						}

					} 
				}
			}   
		}catch(Exception ex){
			logger.info("getHostIp catch an exception",ex);
		}
		
		return null;
		
		
	}

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	/**
     * 是否为一个IPV4地址
     * 
     * @param ipAddress
     * @return
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

	/**
     * 是否为一个IPV6地址
     * 
     * @param ipAddress
     * @return
     */
    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input); 
    }
    
    private static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    private static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }
    

    
    
    //十进制ip地址转化为长整型（59.225.0.0-->1004601344L）
    public static long stringIpToLong(String ip) {  
        String[] ips = ip.split("\\.");  
        long num =  16777216L*Long.parseLong(ips[0]) + 65536L*Long.parseLong(ips[1]) + 256*Long.parseLong(ips[2]) + Long.parseLong(ips[3]);  
        return num;  
    }  

    //长整型转化为十进制ip地址（1004601344L-->59.225.0.0）
    public static String longIpToString(long ipLong) {     
        long mask[] = {0x000000FF,0x0000FF00,0x00FF0000,0xFF000000};  
        long num = 0;  
        StringBuffer ipInfo = new StringBuffer();  
        for(int i=0;i<4;i++){  
            num = (ipLong & mask[i])>>(i*8);  
            if(i>0) ipInfo.insert(0,".");  
                ipInfo.insert(0,Long.toString(num,10));  
        }  
        return ipInfo.toString();  
    }
    
    //根据IP和子网掩码获取IP网段
    public static String getIPStartAndEnd(String ip,String mask){
        long s1 = stringIpToLong(ip);
        long s2 = stringIpToLong(mask);
        String erj = Long.toBinaryString(s2);
        long s3 = s1 & s2;
        long start = stringIpToLong(longIpToString(s3));
        String wl = Long.toBinaryString(s3);
        if(wl.length()<32) {
            int le = 32-wl.length();
            for(int i=0; i<le; i++) {
                wl = "0"+wl;
            }
        }
        String gbl = wl.substring(0,erj.indexOf("0"))+wl.substring(erj.indexOf("0"),wl.length()).replace("0", "1");
        long s4 = Long.parseLong(gbl, 2);
        long end = stringIpToLong(longIpToString(s4));
        return start+"|"+end;
    }
    
    public static void main(String[]arg) {
//        System.out.println(getIPStartAndEnd("59.224.192.0", "255.255.192.0"));
//        System.out.println(stringIpToLong("1.12.0.0"));
//        System.out.println(longIpToString(1000L));
//        System.out.println(longIpToString(1000000L));
    	System.out.println(getLocalHostName());
    	System.out.println(getLocalIp());
    	
    }
    


}
