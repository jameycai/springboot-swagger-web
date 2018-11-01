package com.ultrapower.sbdemo.common;


import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;

/***
 * 
 * @ClassName:     NStringUtil
 * @Description:   String处理帮忙类
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月24日 上午10:37:41
 */
public class NStringUtil {

	private static Pattern blankPattern = Pattern.compile("\\t|\\r|\\n|\\r\\n");
	private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
	private static Pattern numericStringPattern = Pattern
			.compile("^[0-9\\-\\-]+$");
	private static Pattern floatNumericPattern = Pattern
			.compile("^[0-9\\-\\.]+$");
	private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
	public static final String splitStrPattern = ",|，|;|；|、|\\.|。|-|_|\\(|\\)|\\[|\\]|\\{|\\}|\\\\|/| |　|\"";
	
	private static Pattern htmlPattern = Pattern.compile("\\s*<.*?>\\s*", Pattern.DOTALL
			| Pattern.MULTILINE | Pattern.CASE_INSENSITIVE); // \\s?[s|Sc|Cr|Ri|Ip|Pt|T]
	
	private static Pattern numeric2Pattern = Pattern.compile("^[0-9]+$");
	
	/**
	 * 去除字符串中的回车、换行符、制表符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str){
		if(null==str || str.length()==0){
			return str;
		}
		
		try{
			String dest = str;
			Matcher m = blankPattern.matcher(str);
			while(m.find()){
				//将(\t|\r|\n|\r\n)替换成" "
		        dest = m.replaceAll(" ");
			}
	        return dest;
		}catch(Exception ex){
			//ex
		}
		return str;
	}
	
	/**
	 * 判断是否数字表示
	 * 
	 * @param src
	 *            源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isNumeric(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numericPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}
	/**
	 * 判断是否数字表示
	 * 
	 * @param src
	 *            源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isNumericString(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numericStringPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}

	/**
	 * 判断是否纯字母组合
	 * 
	 * @param src
	 *            源字符串
	 * @return 是否纯字母组合的标志
	 */
	public static boolean isABC(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = abcPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}

	/**
	 * 判断是否浮点数字表示
	 * 
	 * @param src
	 *            源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isFloatNumeric(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = floatNumericPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}

	/**
	 * 把string array or list用给定的符号symbol连接成一个字符串
	 * 
	 * @param array
	 * @param symbol
	 * @return
	 */
	public static String joinString(List array, String symbol) {
		String result = "";
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				String temp = array.get(i).toString();
				if (temp != null && temp.trim().length() > 0) {
					result += (temp + symbol);
				}
			}
			if (result.length() > 1) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}	
	
	/**
	 * 截取字符串　超出的字符用symbol代替 　　
	 * 
	 * @param len
	 *            　字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
	 * @param str
	 * @param symbol
	 * @return
	 */
	public static String getLimitLengthString(String str, int len, String symbol) {
		int iLen = len * 2;
		int counterOfDoubleByte = 0;
		String strRet = "";
		try {
			if (str != null) {
				byte[] b = str.getBytes("GBK");
				if (b.length <= iLen) {
					return str;
				}
				for (int i = 0; i < iLen; i++) {
					if (b[i] < 0) {
						counterOfDoubleByte++;
					}
				}
				if (counterOfDoubleByte % 2 == 0) {
					strRet = new String(b, 0, iLen, "GBK") + symbol;
					return strRet;
				} else {
					strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
					return strRet;
				}
			} else {
				return "";
			}
		} catch (Exception ex) {
			return str.substring(0, len);
		} finally {
			strRet = null;
		}
	}

	/**
	 * 检查数据串中是否包含非法字符集
	 * 
	 * @param str
	 * @return [true]|[false] 包含|不包含
	 */
	public static boolean check(String str) {
		String sIllegal = "'\"";
		int len = sIllegal.length();
		if (null == str) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (str.indexOf(sIllegal.charAt(i)) != -1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 随即生成指定位数的含验证码字符串
	 * 
	 * @author Peltason
	 * @date 2007-5-9
	 * @param bit
	 *            指定生成验证码位数
	 * @return String
	 */
	public static String random(int bit) {
		if (bit == 0) {
			bit = 6; // 默认6位
		}
		
		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		String str = "";
		str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";// 初始化种子
		return RandomStringUtils.random(bit, str);// 返回6位的字符串
	}

	/**
	 * 检查字符串是否属于手机号码
	 * 
	 * @author Peltason
	 * @date 2007-5-9
	 * @param str
	 * @return boolean
	 */
	public static boolean isMobile(String str) {
		if (str == null || str.equals("")) {
			return false;
		}
		if (str.length() != 11 || !isNumeric(str)) {
			return false;
		}
		if (!str.substring(0, 2).equals("13")
				&& !str.substring(0, 2).equals("15")
				&& !str.substring(0, 2).equals("18")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 取得字符串的实际长度（考虑了汉字的情况）
	 * 
	 * @param SrcStr
	 *            源字符串
	 * @return 字符串的实际长度
	 */
	public static int getStringLen(String SrcStr) {
		int return_value = 0;
		if (SrcStr != null) {
			char[] theChars = SrcStr.toCharArray();
			for (int i = 0; i < theChars.length; i++) {
				return_value += (theChars[i] <= 255) ? 1 : 2;
			}
		}
		return return_value;
	}
	

	/**
	 * Ascii转为Char
	 * 
	 * @author 甜瓜
	 * @param asc
	 * @return
	 */
	public static String AsciiToChar(int asc) {
		String TempStr = "A";
		char tempchar = (char) asc;
		TempStr = String.valueOf(tempchar);
		return TempStr;
	}

	/**
	 * 判断是否是空字符串 null和"" 都返回 true
	 * 
	 * @author Robin Chang
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (s != null && !s.equals("")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否是空字符串 null和"" null返回result,否则返回字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String isEmpty(String s, String result) {
		if (s != null && !s.equals("")) {
			return s;
		}
		return result;
	}
	/**
	 * 移除html标签
	 * 
	 * @param htmlstr
	 * @return
	 */
	public static String removeHtmlTag(String htmlstr) {
		// \\s?[s|Sc|Cr|Ri|Ip|Pt|T]
		Matcher m = htmlPattern.matcher(htmlstr);
		String rs = m.replaceAll("");
		rs = rs.replaceAll("&nbsp", " ");
		rs = rs.replaceAll("&lt;", "<");
		rs = rs.replaceAll("&gt;", ">");
		return rs;
	}

	/**
	 * 取从指定搜索项开始的字符串，返回的值不包含搜索项
	 * 
	 * @param captions
	 *            例如:"www.koubei.com"
	 * @param regex
	 *            分隔符，例如"."
	 * @return 结果字符串，如：koubei.com，如未找到返回空串
	 */
	public static String getStrAfterRegex(String captions, String regex) {
		if (!isEmpty(captions) && !isEmpty(regex)) {
			int pos = captions.indexOf(regex);
			if (pos != -1 && pos < captions.length() - 1) {
				return captions.substring(pos + 1);
			}
		}
		return "";
	}

	/**
	 * 把字节码转换成16进制
	 */
	public static String byte2hex(byte bytes[]) {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i) {
			retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
					.substring(1).toUpperCase());
		}
		return retString.toString();
	}

	/**
	 * 把16进制转换成字节码
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
		return bts;
	}

	/**
	 * 转换数字为固定长度的字符串
	 * 
	 * @param length
	 *            希望返回的字符串长度
	 * @param data
	 *            传入的数值
	 * @return
	 */
	public static String getStringByInt(int length, String data) {
		String s_data = "";
		int datalength = data.length();
		if (length > 0 && length >= datalength) {
			for (int i = 0; i < length - datalength; i++) {
				s_data += "0";
			}
			s_data += data;
		}

		return s_data;
	}

	/**
	 * 判断是否位数字,并可为空
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isNumericAndCanNull(String src) {
		
		if (src == null || src.equals("")) {
			return true;
		}
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numeric2Pattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}
	
	
	/**
	 * 将字符进行URLDecoder解码
	 * @param str
	 * @return
	 */
	public static String strURLDecoder(String str){
		if(null==str || str.trim().length()==0){
			return str;
		}
		
		try {
			str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
			// 字符串中加号(+)进行解码时候，导致内容丢失，所有需要将加号(+)替换成%2B，再进行解码就不会丢失
			str = str.replaceAll("\\+", "%2B");
			str = URLDecoder.decode(str, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * 将字符进行URLDecoder解码
	 * @param str
	 * @param encode
	 * @return
	 */
	public static String strURLDecoder(String str, String encode){
		if(null==str || str.trim().length()==0){
			return str;
		}
		
		try {
			str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
			// 字符串中加号(+)进行解码时候，导致内容丢失，所有需要将加号(+)替换成%2B，再进行解码就不会丢失
			str = str.replaceAll("\\+", "%2B");
			str = URLDecoder.decode(str, encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * 将字符串中加号(+)替换成%2B
	 * @param str
	 * @return
	 */
	public static String strReplacePlus(String str){
		if(null==str || str.trim().length()==0){
			return str;
		}
		
		try {
			// 字符串中加号(+)进行解码时候，导致内容丢失，所有需要将加号(+)替换成%2B
			str = str.replaceAll("\\+", "%2B");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * 获取子字符，在字符串中，第N次出现的位置
	 * @param string
	 * @param subStr
	 * @param n
	 * @return
	 */
	public static int getCharacterPosition(String string, String subStr, int n){
		int position = -1; 
		
		if(null==string || string.trim().length()==0 || null==subStr || subStr.trim().length()==0
				|| n<1){
			return position;
		}
		
		try{
		    //这里是获取 subStr字符 的位置
		    Matcher slashMatcher = Pattern.compile(subStr).matcher(string);
		    int mIdx = 0;
		    while(slashMatcher.find()) {
		       mIdx++;
		       //当 subStr字符 第N次出现的位置
		       if(mIdx == n){
		          break;
		       }
		    }
		    position = slashMatcher.start();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return position;
	}
	
	
	/**
	 * 在字符串中，每隔N个字符，添加一个字符。
	 * @param string
	 * @param cycle
	 * @param n
	 * @return
	 */
	public static String getCharacterCycleAddChar(String string, int n, String str){
		if(null==string || string.trim().length()==0 || null==str || n<0){
			return string;
		}
		
		try{
			char[] charArray = string.toCharArray();
			StringBuffer tempBuff = new StringBuffer();
			for(int i=0;i<charArray.length;i++){
				tempBuff.append(charArray[i]);
				if(i!=(charArray.length-1)&&(i+1)%n==0) {
					tempBuff.append(str);			
				}
			}
			return tempBuff.toString();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return string;
	}
	
	
	
	
	
}
