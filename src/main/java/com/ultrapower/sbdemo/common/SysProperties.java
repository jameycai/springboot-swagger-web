package com.ultrapower.sbdemo.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ultrapower.sbdemo.common.cache.EhCacheUtil;

/**
 * 
 * @ClassName:     SysProperties
 * @Description:   获取 系统(文件)的 配置信息
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月25日 下午3:17:30 
 *
 */
public class SysProperties {

	private static final String configPath = "server.conf";
	
	private static final String SERVER_PROPERTIES_PATH = "../conf";
	private static final String SERVER_PROPERTIES_TMP = "../conf/tmp";

	private static Properties configP = null;

	
	
	/**
	 * 取得指定文件中属性配置
	 * 
	 * @param propertyName
	 * @param filename
	 * @return
	 * 
	 * @author caijinpeng
	 * 
	 */
	public static String getProperty(String propertyName, String filename) {
		configP = new Properties();
		try {
			String value = (String)EhCacheUtil.get(EhCacheUtil.CACHENAME_COMMON, "SysProperties_"+filename+"_"+propertyName);
			if(null==value || value.trim().length()==0){
				
				
				try{
					// 如果为空,  则加载 Server配置文件, 获取key的值
					//configP.load(SysProperties.class.getClassLoader().getResourceAsStream(filename)); //修改文件后，不重启tomcat，修改的参数不生效
					File file = new File(getConfigPath(filename));
					InputStream in = new FileInputStream(file);
					configP.load(in);
				}catch(Exception e){

					try{
						//======== 解决SpringWeb的jar和配置文件，移到Tomcat外面层   by caijinpeng ===========
						File file = new File(getConfigPath(filename));
						InputStream in = new FileInputStream(file);
						configP.load(in);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
				}
				value = ConvertUtil.Obj2Str(configP.get(propertyName), "").trim();
//				// 判断是否为NULL
//				if(value!= null && value.trim().length()!=0) {
				EhCacheUtil.put(EhCacheUtil.CACHENAME_COMMON, "SysProperties_"+filename+"_"+propertyName, value);
//				}else {
//					//LogManage.setLogInfor("SysProperties","getProperty(propertyName, filename).  在" + filename + "中没有找到key=" + propertyName + "的属性. fail!");
//				}
			}
			return value;
		} catch (Exception ex) {
			LogManage.setLogErrorInfor(SysProperties.class, "getProperty(propertyName, filename).  在" + filename + "中找key=" + propertyName + "的属性失败！ error!", ex);
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得Server.conf配置文件属性配置
	 * 
	 * @return
	 */
	public static String getProperty(String propertyName) {
		try {
			// 获取key的值
			String value = getProperty(propertyName,  configPath);
			
			return value;
		} catch (Exception ex) {
			LogManage.setLogErrorInfor(SysProperties.class, "SysProperties getProperty(propertyName, filename).  在server.conf中找key=" + propertyName + "的属性失败！ error!", ex);
			ex.printStackTrace();
		}
		return null;
	}
	
    
	/**
	 * 获取文件的绝对路径
	 * 
	 * @author caijinpeng
	 * @param fileName
	 * @return
	 */
	public static String getConfigPath(String fileName) {
		try {
			if(null==fileName){
				return null;
			}
			
			String classpath = "";
			URL url = null;

			// ============= [1].获取classpath的绝对路径 -- by caijinpeng
			try{
				// 通过 Thread.currentThread().getContextClassLoader().getResource(fileName)
			    url = Thread.currentThread().getContextClassLoader().getResource(fileName);
			    if(null!=url){
	                // 从当前类的目录下开始查找，要找根路径下的内容，必须以/为开头：
	                classpath = url.getPath();
	                if (null != classpath && classpath.length() > 0) {
	                    classpath = classpath.replaceAll("%20", " "); // 将"%20"转换为" "
	                }
	            }
			}catch(Exception e){
				LogManage.setLogInfor("", "SysProperties getConfigPath[1]: "+e.getMessage());
			}
			
			
			// ============= [2].判断classpath是否为空
			try{
			    if(null==url || null==classpath || classpath.trim().length()==0){
			    	// 通过 Thread.currentThread().getContextClassLoader().getResource("")
			        url = Thread.currentThread().getContextClassLoader().getResource("");
	                if(null!=url){
	                    classpath = url.getPath();
	                    
	                    if (null != classpath && classpath.length() > 0) {
	                        classpath = classpath.replaceAll("%20", " "); // 将"%20"转换为" "
	                        
	                        //判断路径是否有“/”
	                        if((classpath.lastIndexOf("/")+1)==classpath.length()){
	                            classpath += fileName;
	                        }else{
	                            classpath += "/";
	                            classpath += fileName;
	                        }
	                    }
	               
	                }
			    }
			}catch(Exception e){
				LogManage.setLogInfor("", "SysProperties getConfigPath[2]: "+e.getMessage());
			}
			
			
			// ============= [3].判断classpath是否为空
			try{
			    if(null==url || null==classpath || classpath.trim().length()==0){
                    // 通过 SysProperties.class.getClassLoader().getResource("")
                    url = SysProperties.class.getClassLoader().getResource("");
                    if(null!=url){
                        classpath = url.getPath();
                            
                        if (null != classpath && classpath.length() > 0) {
                            classpath = classpath.replaceAll("%20", " "); // 将"%20"转换为" "
                            
                            //判断路径是否有“/”
                            if((classpath.lastIndexOf("/")+1)==classpath.length()){
                                classpath += fileName;
                            }else{
                                classpath += "/";
                                classpath += fileName;
                            }
                        }        
                    }

                }
			}catch(Exception e){
				LogManage.setLogInfor("", "SysProperties getConfigPath[3]: "+e.getMessage());
			}


			// ============= [4].判断classpath是否为空
			try{
			    if(null==url || null==classpath || classpath.trim().length()==0){
                    url = ClassLoader.getSystemResource("");
                    if(null!=url){
                        classpath = url.getPath();
                            
                        if (null != classpath && classpath.length() > 0) {
                            classpath = classpath.replaceAll("%20", " "); // 将"%20"转换为" "
                            
                            //判断路径是否有“/”
                            if((classpath.lastIndexOf("/")+1)==classpath.length()){
                                classpath += fileName;
                            }else{
                                classpath += "/";
                                classpath += fileName;
                            }
                        }        
                    }

                }
			}catch(Exception e){
				LogManage.setLogInfor("", "SysProperties getConfigPath[4]: "+e.getMessage());
			}
			
			
			// ======================== 对获取的文件路径进行效验 ， 根据路径去获取文件名
			File file = null;
			try{
				if(null!=classpath && classpath.trim().length()>0){
		            if(null!=classpath && classpath.indexOf("webapps/../")>0){
		            	classpath = classpath.replace("webapps/../", "");
		            }
					
					if(classpath.indexOf("/WEB-INF/lib/")!=-1){
						// 如果获取的文件路径包含/WEB-INF/lib/ ， 则进行强制转化成为  /WEB-INF/classes/  
						classpath = classpath.replace("/WEB-INF/lib/", "/WEB-INF/classes/");
					}
					
					// 获取文件
				    file = new File(classpath);
				}
			}catch(Exception e){
			}

		    
			if(null!=file && file.exists()){
				LogManage.setLogDebugInfor("SysProperties","getConfigPath() , 获取文件["+fileName+"]绝对路径: " + classpath);
				return classpath;
			}
			

			// 判断文件名称是否存在 --- by caijinpeng 为了解决tomcat7下无法获取正常路径问题
			// 再次获取文件路径 
			classpath = againGetFilePath(classpath, fileName);
			
			
			LogManage.setLogDebugInfor("SysProperties","getConfigPath() , 获取文件["+fileName+"]绝对路径: " + classpath);

			return classpath;
		} catch (Exception ex) {
			LogManage.setLogErrorInfor(SysProperties.class, "SysProperties getFileConfigPath() , 获取文件[" + fileName + "]绝对路径失败!  error=" + ex.getMessage(), ex);
			ex.printStackTrace();
		}
		return null;
	}
	
	// 再次获取文件的路径
	private static String againGetFilePath(String classpath, String fileName){
		URL url = null;
		try{
			
			if(null!=classpath && classpath.trim().length()>0 ){
				//====================== 已经获取到classpath 
				
				if(null!=fileName && fileName.trim().length()==0){
					// 不做任何处理
				}else if(null!=fileName && fileName.trim().startsWith("/")){
					// 从当前类的目录下开始查找，要找根路径下的内容，必须以/为开头：
	                url = Thread.currentThread().getContextClassLoader().getResource(fileName);
	                if(null!=url){
	                    classpath = url.getPath();
	                }
	                if (null != classpath && classpath.length() > 0) {
	                    classpath = classpath.replaceAll("%20", " "); // 将"%20"转换为" "
	                }
				
				}else if(null!=fileName && fileName.trim().startsWith("../")){
	                
	                // 获取文件名中"../"的个数
	                int count = subStrCounter(fileName, "../");
	                
	                url = Thread.currentThread().getContextClassLoader().getResource("");
	                if(null!=url){
	                    classpath = url.getPath();
	                }
	                if (null != classpath && classpath.length() > 0) {
	                    classpath = classpath.replaceAll("%20", " "); // 将"%20"转换为" "
	                    if((classpath.lastIndexOf("/")+1)!=classpath.length()){
	                        classpath += "/";
	                    }

	                    // 将获取绝对路径classpath, 根据"../"的个数, 进行截取
	                    StringBuilder classpathSb_ = new StringBuilder(classpath);
	                    classpathSb_.reverse();
	                    
	                    int end = getCharacterPosition(classpathSb_.toString(),"/",count+1);
	                    String classpath2 = classpathSb_.substring(end, classpath.length());
	                    
	                    StringBuilder classpathSb2_ = new StringBuilder(classpath2);
	                    classpathSb2_.reverse();
	                    classpath = classpathSb2_.toString();
	                    
	                    //将fileName, 截取"../"部分
	                    fileName = fileName.substring(3*count, fileName.length());
	                    
	                    classpath += fileName;
	                
	                }

	            }
				
				
			}else {
				//====================== 无法获取到classpath   
				
				try{
					url = SysProperties.class.getResource("/");
					if(null!=url){
	                    classpath = url.getPath();
	                        
	                    if (null != classpath && classpath.length() > 0) {
	                    	classpath = classpath.split("WEB-INF/classes")[0];
	                        classpath = classpath.replaceAll("%20", " "); // 将"%20"转换为" "
	                        
	                        //判断路径是否有“/”
	                        if((classpath.lastIndexOf("/")+1)==classpath.length()){
	                        	classpath += "WEB-INF/classes/";
	                        	classpath += fileName;
	                        }else{
	                            classpath += "/WEB-INF/classes/";
	                            classpath += fileName;
	                        }
	                    }        
	                }
				}catch(Exception ex){
				}
				
			}
			
			
            if(null!=classpath && classpath.indexOf("webapps/../")>0){
            	classpath = classpath.replace("webapps/../", "");
            }
            
		}catch(Exception ex){
			LogManage.setLogErrorInfor(SysProperties.class, "SysProperties againGetFilePath() , 获取文件[" + fileName + "]绝对路径失败!  error=" + ex.getMessage(), ex);
			ex.printStackTrace();
		}
		return classpath;
	}
	
	

	 /**
	  * 查找字符串中,子字符的个数
	  * @param str1
	  * @param substr
	  * @return
	  */
	 private static int subStrCounter(String str1, String substr) {
        int counter = 0;
        try {
        	if(null==str1 || str1.trim().length()==0 || null==substr || substr.trim().length()==0) {
        		return counter;
        	}
        	
            for (int i = 0; i <= str1.length() - substr.length(); i++) {
                if (str1.substring(i, i + substr.length()).equalsIgnoreCase(substr)) {
                    counter++;
                }
            }
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        return counter;
     } 
	 
	 /**
	  * 获取字符串中第N次出现的子字符位置 
	  * @param string
	  * @param subStr
	  * @param n
	  * @return
	  */
	 private static int getCharacterPosition(String string, String subStr, int n){
		 try{
			 if(null==string || string.trim().length()==0 || null==subStr || subStr.trim().length()==0){
				 return -1;
			 }
			 
		    //这里是获取"/"符号的位置
		    Matcher slashMatcher = Pattern.compile(subStr).matcher(string);
		    if(null!=slashMatcher){
			    int mIdx = 0;
			    while(slashMatcher.find()) {
			       mIdx++;
			       //当"/"符号第三次出现的位置
			       if(mIdx == n){
			          break;
			       }
			    }
			    return slashMatcher.start();
		    }
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
		 return -1;
	 }
	 
	 
	 
	/**
	 * 配置server.conf中的参数
	 * 
	 * @param curkey
	 * @param valueInfo
	 * @return flag
	 */
	 public static boolean saveInfo2ServerConf(String curkey, String valueInfo) {
		boolean flag = saveKey2File(getConfigPath(configPath), curkey, valueInfo);
		if (flag){
			EhCacheUtil.put(EhCacheUtil.CACHENAME_COMMON, "SysProperties_"+configPath+"_"+curkey, valueInfo);//更新缓存
			System.setProperty(curkey, valueInfo);
		}
		return flag;
	 }

	
	/***
	 * 配置server.conf
	 * @param filepathName
	 * @param curkey
	 * @param valueInfo
	 * @return
	 */
	public static boolean saveKey2File(String filepathName, String curkey, String valueInfo) {
		if (curkey == null || valueInfo == null || filepathName == null){
			return false;
		}
		String fileName = filepathName;
		if (filepathName.indexOf("/") > -1){
			fileName = filepathName.substring(filepathName.lastIndexOf("/") + 1);
		}
		LogManage.setLogDebugInfor("SysProperties", "saveKey2File() , Config flie " + fileName + " : " + curkey + "=" + valueInfo);
		
		try {
			// 备份原有文件
			copyFile(filepathName, fileName);
		} catch (Exception ex) {
			LogManage.setLogErrorInfor(SysProperties.class, "saveKey2File error !  error=" + ex.getMessage(), ex);
			return false;
		}
		
		
		StringBuffer content = new StringBuffer();
		File fileIn = new File(filepathName);
//		if (fileIn == null){
//			fileIn = new File(fileName);
//		}
		
		// 新建文件输入流并对它进行缓冲
		File path = new File(SERVER_PROPERTIES_PATH);
		if (!path.exists()){
			path.mkdirs();
		}
		String line_str = null;
		String key = null;
		String newPass = null;
		// log.info("fileIn========="+fileIn);
		try {
			// reader = new CSVReader(new FileReader(logFile));
			BufferedReader br = new BufferedReader(new FileReader(fileIn));
			boolean isHas = false;
			// log.info("patrolkey==========="+webInfo);
			while ((line_str = br.readLine()) != null) {
				// log.info("line_str========="+line_str);
				if (line_str.startsWith(curkey + "=")) {
					line_str = curkey + "=" + valueInfo;
					content = content.append(line_str + "\n");
					isHas = true;
				}else{
					content = content.append(line_str + "\n");
				}
			}

			if (!isHas) {
				line_str = curkey + "=" + valueInfo;
				content = content.append(line_str + "\n");
			}
			br.close();
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
			LogManage.setLogErrorInfor(SysProperties.class, "saveKey2File 读取文件出错 !  error=" + e1.getMessage(), e1);
			return false;
		} catch (Exception e1) {
			LogManage.setLogErrorInfor(SysProperties.class, "saveKey2File 读取文件出错 !  error=" + e1.getMessage(), e1);
			return false;
		}

		File targepath = new File(SERVER_PROPERTIES_PATH);
		if (!targepath.exists()){
			targepath.mkdirs();
		}
		
		try {
			if (content != null) {
				FileWriter fw = new FileWriter(filepathName);
				PrintWriter printout = new PrintWriter(fw);
				printout.print(content.toString());
				printout.close();
				fw.close();
			}
		} catch (Exception e) {

			LogManage.setLogErrorInfor(SysProperties.class, "saveKey2File " + filepathName + " Writer file Exception:" + e.getMessage(), e);
		}
		return true;
	}

	
	/***
	 * 复制原有配置文件
	 * @param filePathName
	 * @param backfileName
	 * @throws IOException
	 */
	private static void copyFile(String filePathName, String backfileName) throws IOException {
		InputStream input = SysProperties.class.getClassLoader().getResourceAsStream(backfileName);

		if (input == null) {
			input = new FileInputStream(filePathName);

		}
		// 新建文件输入流并对它进行缓冲
		File path = new File(SERVER_PROPERTIES_TMP);
		if (!path.exists()){
			path.mkdirs();
		}
		File targetFile = new File(SERVER_PROPERTIES_TMP + "/" + backfileName + ConvertUtil.getDateString("yyyyMMddHHmm", Locale.US, new Date(System.currentTimeMillis())));

		BufferedInputStream inBuff = new BufferedInputStream(input);
		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);
		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();
		// 关闭流
		outBuff.close();
		output.close();
		input.close();
		inBuff.close();
	}
	
	

}
