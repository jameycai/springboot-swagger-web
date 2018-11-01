package com.ultrapower.sbdemo.common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName:     ConvertUtil
 * @Description:   java对象转换 和 时间帮助 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:54:59 
 *
 */
public class ConvertUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ConvertUtil.class);
	 
	/*** =================================================================  
	 * 将object转换为相应字符型、整型
	 * 
	 * ================================================================== **/
	/**
	 * 将通用对象s转换为long类型，如果字符穿为空或null，返回r；
	 * @author caijinpeng 
	 * @param s
	 * @param r
	 * @return
	 */
	public static long Obj2long(Object s, long r) {
		long i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Long.parseLong(str);	
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}
	
	
	/**
	 * 将通用对象s转换为long类型，如果字符穿为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng 
	 */
	public static long Obj2long(Object s) {
		long i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Long.parseLong(str);
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	
	/**
	 * 将通用对象s转换为long类型，如果字符穿为空或null，返回r；
	 * @param s
	 * @return
	 * @author caijinpeng 
	 */
	public static double Obj2Double(Object s, double r) {
		double i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Double.parseDouble(str);
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}
	
	/**
	 * 将通用对象s转换为long类型，如果字符穿为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng 
	 */
	public static double Obj2Double(Object s) {
		double i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Double.parseDouble(str);
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	
	/**
	 * 将通用对象s转换为int类型，如果字符穿为空或null，返回r；
	 * @author caijinpeng 
	 * @param s
	 * @param r
	 * @return
	 */
	public static int Obj2int(Object s, int r) {
		int i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Integer.parseInt(str);
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}
	
	/**
	 * 将通用对象s转换为int类型，如果字符穿为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng 
	 */
	public static int Obj2int(Object s) {
		int i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Integer.parseInt(str);
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	
	/**
	 * 将通用对象s转换为float类型，如果字符穿为空或null，返回r；
	 * @author caijinpeng 
	 * @param s
	 * @param r
	 * @return
	 */
	public static float Obj2Float(Object s, float r) {
		float i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Float.parseFloat(str);
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}
	
	/**
	 * 将通用对象s转换为float类型，如果字符穿为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng 
	 */
	public static float Obj2Float(Object s) {
		float i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				i = Float.parseFloat(str);
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * 将通用对象s转换为String类型，如果字符穿为空或null，返回r；
	 * @author caijinpeng 
	 * @param s
	 * @param r
	 * @return
	 */
	public static String Obj2Str(Object s, String r) {
		String str = r;
		try {
			if(null!=s){
				str = s.toString().trim();
			}
			
			if(str.equalsIgnoreCase("null") || str.trim().length() == 0){
				str = r;
			}
		} catch (Exception e) {
			str = r;
		}
		return str;
	}
	
	
	/**
	 * 将字符串 转换成 boolean 类型
	 * @param r
	 * @return
	 */
	public static boolean Str2Boolean(String r){
	    boolean result = false;
        try {
            if(null!=r && r.trim().length()>0){
                Boolean rBoolean = Boolean.parseBoolean(r.trim());
                if(null!=rBoolean){
                    result = rBoolean.booleanValue();
                }
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
	}
	
	
	/**
	 * 去掉字符串 中前后的空格
	 * @param r
	 * @return
	 */
	public static String StringTrim(String r){
		String str = r;
		try{
			if(null!=r){
				str = r.trim();
			}
		}catch(Exception e){
			str = r;
		}
		return str;
	}
	
	
	/**
	 * 将long对象s转换为String类型，如果s为0，返回字符串r；
	 * @param s
	 * @return
	 */
	public static String Long2Str(long s, String r){
		String str = r;
		try {
			if(s!=0){
				str = String.valueOf(s);
			}
		} catch (Exception e){
			str = r;
		}
		return str;
	}
	
	
	/***
	 * 将double字符串对象d, 转换为int类型，如果d为NULL和空，返回0；
	 * @param d
	 * @return
	 */
	public static int DoubleStr2Int(String d){
		int i = 0;
		try{
			if(d!=null && d.trim().length()>0){
				Double D1=new Double(d);    
				i = D1.intValue();
			}
		}catch(Exception e){
			i = 0;
		}
		return i;
	}
	
	/***
	 * 将double字符串对象d, 转换为long类型，如果d为NULL和空，返回0；
	 * @param d
	 * @return
	 */
	public static long DoubleStr2Long(String d){
		long i = 0;
		try{
			if(d!=null && d.trim().length()>0){
				Double D1=new Double(d);    
				i = D1.longValue();
			}
		}catch(Exception e){
			i = 0;
		}
		return i;
	}
	
	
	/*** =================================================================  
	 * 时间不同格式进行转换
	 * 
	 * ================================================================== **/
	/**
	 * 将毫秒数转换为yyyy-MM-dd HH:mm:ss格式的时间串
	 * @param millis
	 * @return
	 */
	public static String Millis2StrLong(long millis){
		if (millis <= 0){
			return "";
		}
		
		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Instant instant = Instant.ofEpochMilli(millis);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			String s = df.format(localDeteTime);
			return s;
		}catch(Exception ex){
			logger.error("Millis2StrLong error! request millis:"+millis, ex);
		}
		return "";
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss类型的字符串转换为毫秒数
	 * @param dateStr
	 * @return
	 */
	public static long StrLong2Millis(String dateStr){
		if (dateStr == null || "".equals(dateStr.trim())) {
			return 0;
		}
		
		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df); 
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			long lTime = instant.toEpochMilli();
			return lTime;
		}catch(Exception ex){
			logger.error("StrLong2Millis error! request dateStr:"+dateStr, ex);
		}
		return 0;
	}
	
	
	
	/**
	 * 将毫秒数转换为yyyy-MM-dd格式的时间串
	 * @param millis
	 * @return
	 */
	public static String Millis2Str(long millis){
		if (millis <= 0){
			return "";
		}
		
		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Instant instant = Instant.ofEpochMilli(millis);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			String s = df.format(localDeteTime);
			return s;
		}catch(Exception ex){
			logger.error("Millis2Str error! request millis:"+millis, ex);
		}
		return "";
	}
	
	
	/**
	 * 将yyyy-MM-dd类型的字符串转换为毫秒数
	 * @param date
	 * @return
	 */
	public static long Str2Millis(String dateStr){
		if (dateStr == null || "".equals(dateStr.trim())) {
			return 0;
		}
		
		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df); 
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			long lTime = instant.toEpochMilli();
			return lTime;
		}catch(Exception ex){
			logger.error("Str2Millis error! request dateStr:"+dateStr, ex);
		}
		return 0;
	}

	/**
     * 将给定类型的时间字符串转换为毫秒数
     * @param dateStr
     * @param _dtFormat
     *     日期格式1  yyyy-MM-dd HH:mm:ss
     *     日期格式2  yyyy-MM-dd
     *     日期格式3  yyyyMMddHH
     * @return
     */ 
    public static long StrFormat2Millis(String dateStr, String _dtFormat){
    	if (dateStr == null || "".equals(dateStr.trim())) {
			return 0;
		}
		
		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df); 
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			long lTime = instant.toEpochMilli();
			return lTime;
		}catch(Exception ex){
			logger.error("StrFormat2Millis error! request dateStr:"+dateStr+", _dtFormat:"+_dtFormat, ex);
		}
		return 0;      
    }
    
    
    
    
    /**
     * 将给定类型的时间字符串转换为时间
     * @param dateStr
     * @param _dtFormat
     *     日期格式1  yyyy-MM-dd HH:mm:ss
     *     日期格式2  yyyy-MM-dd
     *     日期格式3  yyyyMMddHH
     * @return
     */ 
    public static Date StrFormat2Date(String dateStr, String _dtFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(_dtFormat);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(dateStr, pos);
        return strtodate;
    }
    
    
    /**
     * 将13位时间毫秒数转换为 指定格式的时间字符串串
     * @param millis
     * @param _dtFormat
     *     日期格式1  yyyy-MM-dd HH:mm:ss
     *     日期格式2  yyyyMMdd
     *     日期格式3  yyyyMMddHH
     * @return
     */
    public static String Millis2FormatStr(long millis, String _dtFormat){
        if (millis <= 0){
			return "";
		}
		
		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern(_dtFormat);
			Instant instant = Instant.ofEpochMilli(millis);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			String s = df.format(localDeteTime);
			return s;
		}catch(Exception ex){
			logger.error("Millis2FormatStr error! request millis:"+millis+", _dtFormat:"+_dtFormat, ex);
		}
		return "";
    }
    
    
    
    /** 
     * 按照日前模式和本地化转化日期为字符串
     * @param pattern
     * @param locale
     * @param date
     *
     */
    public static String getDateString(String pattern, Locale locale, Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
            
            return df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * 将时间秒转换为时分秒(HH时mm分ss秒)
     * @param time
     * @return
	 *    1. 30秒
	 *    2. 2分40秒   
	 *    3. 4小时50分5秒
     */
    public static String secondToHMSTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "0分0秒";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = minute + "分" + unitFormat(second)+"秒";  
            } else {  
                hour = minute / 60;  
                if (hour > 99999) {
                    return "99999时59分59秒"; 
                }
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = hour + "时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";  
            }  
        }  
        return timeStr;  
    } 
    
    
    
    private static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10) { 
            retStr = "0" + Integer.toString(i);  
        }else{  
            retStr = "" + i;  
        }
        return retStr;  
    }  
 
    

    /**
     * 将毫秒值转换为时分秒(HH时mm分ss秒)
     * @param mtime
     * @return
	 *    1. 30秒
	 *    2. 2分40秒   
	 *    3. 4小时50分5秒
     */
	public static String Millis2HMSTime(long mtime){
		long ltime = mtime / 1000;
		int time = Integer.parseInt(ltime+"");
		
		String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "0分0秒";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                if(minute==0){
                     timeStr = unitFormat(second)+"秒";  
                }else{
                	timeStr = minute + "分" + unitFormat(second)+"秒";  
                }
            } else {  
                hour = minute / 60;  
                if (hour > 99999)  {
                    return "99999时59分59秒"; 
                }
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = hour + "时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";  
            }  
        }  
        return timeStr;  
	}
	
	/* 
	 * 将毫秒值转换为时分秒(D天HH小时mm分钟)
     * 将 毫秒转化   天  小时  分钟 
     * @param ms 毫秒数  
     */  
    public static String Millis2DHMTime(Long ms) {    
        Integer ss = 1000;    
        Integer mi = ss * 60;    
        Integer hh = mi * 60;    
        Integer dd = hh * 24;    
        
        Long day = ms / dd;    
        Long hour = (ms - day * dd) / hh;    
        Long minute = (ms - day * dd - hour * hh) / mi;    
       // Long second = (ms - day * dd - hour * hh - minute * mi) / ss;    
            
        StringBuilder sb = new StringBuilder();    
        if(day > 0) {    
            sb.append(day+"天");    
        }    
        if(hour > 0) {    
            sb.append(hour+"小时");    
        }    
        if(minute > 0) {    
            sb.append(minute+"分钟");    
        }    
   /*    if(second > 0) {    
            sb.append(second+"秒");    
        }  */  
        return sb.toString();    
    }  
	
	
	/* 
	 * 将秒值转换为时分秒(D天HH小时mm分钟)
     * 将 毫秒转化   天  小时  分钟 
     * @param ms 秒数  
     */  
    public static String secondMillis2DHMTime(Long s) {    
        Integer ss = 1;    
        Integer mi = ss * 60;    
        Integer hh = mi * 60;    
        Integer dd = hh * 24;    
        
        Long day = s / dd;    
        Long hour = (s - day * dd) / hh;    
        Long minute = (s - day * dd - hour * hh) / mi;    
            
        StringBuilder sb = new StringBuilder();    
        if(day > 0) {    
            sb.append(day+"天");    
        }    
        if(hour > 0) {    
            sb.append(hour+"小时");    
        }    
        if(minute > 0) {    
            sb.append(minute+"分钟");    
        }    
   /*    if(second > 0) {    
            sb.append(second+"秒");    
        }  */  
        return sb.toString();    
    }  
	
	
}
