package com.ultrapower.sbdemo.common.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ultrapower.sbdemo.common.NStringUtil;
import com.ultrapower.sbdemo.common.filter.ParamServletRequestWrapper;


/**
 * 
 * @ClassName:     UltraFilterDispatcher
 * @Description:   自定义通用过滤器，实现字符过滤、转换、sql注入 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月24日 上午10:49:41
 */
public class UltraFilterDispatcher implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(UltraFilterDispatcher.class);

    private static String encoding = "UTF-8";
    
    /**
     * DENY 浏览器拒绝当前页面加载任何Frame页面
     * SAMEORIGIN frame页面的地址只能为同源域名下的页面
     * ALLOW-FROM 允许frame加载的页面地址
     */
    private static String xframemode = "DENY";
    
    /**
     * 例外url
     */
    public List<String> excludes = new ArrayList<>();  
    
    
    /**
     * 是否开启安全扫描
     */
    private String securityScan;
    
    
    private String scriptTest = "alert,eval,script,alert,window,prompt,function,href,src,confirm,onclick,onmouseover,onmousemove,onmousedown,ondblclick";
    private static String [] scriptTests;
    
    
    public UltraFilterDispatcher(String securityScan) {
        super();
        this.securityScan = securityScan;
        scriptTests=scriptTest.split(",");
    }
    
    public UltraFilterDispatcher() {
        super();
    }

    
    /**
     * 初始化
     */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	    String encodingParam = filterConfig.getInitParameter("encoding");
	    if (encodingParam != null && encodingParam.trim().length() != 0) {
	        encoding = encodingParam;
	    }
	    

	    String xframemodeParam = filterConfig.getInitParameter("xframemode");
	    if (xframemodeParam != null && xframemodeParam.trim().length() != 0) {
	    	xframemode = xframemodeParam;
	    }else{
	    	//如果web.xml中没有配置，默认关闭
	    	xframemode = "DENY";
	    }

	    
	    String excludesStr = filterConfig.getInitParameter("excludes");  
        if (null!=excludesStr && excludesStr.trim().length()!=0) {  
            String[] urlArry = excludesStr.split(",");  
            for (int i = 0;  null!=urlArry && i < urlArry.length; i++) { 
            	String urlStr = urlArry[i];
            	if(null==urlStr || urlStr.trim().length()==0) {
            		continue;
            	}
            	
                excludes.add(urlStr.trim());  
            }  
        } 
        
        
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {


    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        
    	// [1]. 设置字符编码UTF-8
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);



        // [2]. Cookie设置HttpOnly属性   --- by caijinpeng 
        Cookie[] cookies = request.getCookies();  
        if (cookies != null) {  
            Cookie cookie = cookies[0];  
            if (cookie != null) {  
                /*cookie.setMaxAge(3600); 
                cookie.setSecure(true); 
                resp.addCookie(cookie);*/  
                  
                //在Cookie上直接设置HttpOnly属性  
                String value = cookie.getValue();  
                StringBuilder builder = new StringBuilder();  
                builder.append("JSESSIONID=" + value + "; ");  
                builder.append("Secure; ");  
                builder.append("HttpOnly; ");  
                Calendar cal = Calendar.getInstance();  
                cal.add(Calendar.HOUR, 2);  
                Date date = cal.getTime();  
                Locale locale = Locale.CHINA;  
                SimpleDateFormat sdf =   
                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",locale);  
                builder.append("Expires=" + sdf.format(date));  
                response.setHeader("Set-Cookie", builder.toString());  
            }  
        }  
		
		
        // [3]. 点击劫持漏洞:使用X-Frame-Options 防止网页被Frame
        if("true".equals(securityScan)) {
        	//logger.info("UltraFilterDispatcher", "~~~~~~~~~ Ultra-GoAgent X-FRAME-OPTIONS mode=" + xframemode);
	        response.addHeader("X-FRAME-OPTIONS", xframemode);
        }else {
        	response.addHeader("X-FRAME-OPTIONS", "ALLOW-FROM");
        }
        
        
        
        //=========== 判断是否是例外 URL =========
        if(handleExcludeURL(request, response)) {
    		try{
    			chain.doFilter(request, response); 
    		}catch(Exception ex){
    			//ex
    		}
    		return;
        }
        
        
        
		// [4]. 过滤  请求参数和值中脚本威胁字符及sql注入   --- by caijinpeng 
    	ParamServletRequestWrapper requestWrapper = new ParamServletRequestWrapper((HttpServletRequest)req);
        Enumeration<String> paramkey = req.getParameterNames();
		if(null!=paramkey && null!=requestWrapper){
			// 过滤  请求参数和值中脚本威胁字符及sql注入
			boolean r = requestParamKeypFilter(paramkey, requestWrapper);
			if("true".equals(securityScan)) {
			    if (!r) {
			        //跳转到404页面
			        response.sendRedirect("/common/404.html");
			    }
			}
		}


		
		
		// [5]. 验证HTTP Referer字段, 解决跨站点请求伪造 --- by caijinpeng 
		
		
		

        
		try{
			if(null!=requestWrapper) {
		        ServletRequest newreq = requestWrapper.getRequest();
		        if(null!=newreq){
		        	chain.doFilter(requestWrapper, response); 
		        }else{
		        	chain.doFilter(request, response); 
		        }
			}else {
				chain.doFilter(request, response); 
			}
		}catch(Exception ex){
			//ex
		}

	}
	
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	
	/**
	 * @Title:handleExcludeURL
	 * @Description: 是否要处理例外URL
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {  
        if (excludes == null || excludes.isEmpty()) {  
            return false;  
        }  
  
        try {
            String url = request.getServletPath();  
            for (String pattern : excludes) {  
                Pattern p = Pattern.compile(pattern);  
                Matcher m = p.matcher(url);  
                if (m.find()) {  
                	///System.out.println("-------f1[YES]----:"+url);
                    return true;  
                }  
            }  
            ////System.out.println("-------f2[No]----:"+url);
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        return false;  
    }  
	
	
	/**
	 * 过滤  请求参数和值中脚本威胁字符及sql注入
	 * @param paramkey
	 * @param requestWrapper
	 * @return
	 * @author caijinpeng
	 */
    protected boolean requestParamKeypFilter(Enumeration<String> paramkey, ParamServletRequestWrapper requestWrapper){
    	if(null==paramkey || null==requestWrapper){
			return true;
		}
		
		
		while (paramkey.hasMoreElements()) {
		    String okey = paramkey.nextElement();
		    String[] ovalues = requestWrapper.getParameterValues(okey);

		    // 1.将字符进行URLDecoder解码
		    okey = strDecode(okey);
		    // 2.正则表达式 过滤字符串中非法脚本威胁 
		    okey = replaceStrIllegalScript(okey);
		    
		    // 因为在进行Xss替换时候，已经将参数中%2B变成加号(+)，现在需要还原，否则在内容模块中直接解码参数有可能会丢失加号(+)
		    // 将字符串中加号(+)替换成%2B
		    okey = NStringUtil.strReplacePlus(okey);
		    
		    if(null!=ovalues){
		    	for(int i=0; i<ovalues.length; i++){
		    		String ovalue = ovalues[i];
		    		
		    		// 1.将字符进行URLDecoder解码
		    		ovalue = strDecode(ovalue);
		    		// 2.正则表达式 过滤字符串中非法脚本威胁 
		    		ovalue = replaceStrIllegalScript(ovalue);
		    		if("true".equals(securityScan)) {
		    		    if(!ovalue.equals(ovalues[i])) {
		    		        return false;
		    		    }
		    		}
		    		// 3.正则表达式 过滤sql注入中 非法表达式
		    		ovalue = replaceSqlIllegalExpr(ovalue,securityScan);
		    		if("true".equals(securityScan)) {
                        if(!ovalue.equals(ovalues[i])) {
                            return false;
                        }
                    }
		    		// 4.替换sql注入中 关键字 (exec|insert|select|delete|drop|truncate...)
		    		ovalue = replaceSqlIllegalKeyWord(ovalue,securityScan);
		    		if("true".equals(securityScan)) {
                        if(!ovalue.equals(ovalues[i])) {
                            return false;
                        }
                    }

		    		// 因为在进行Xss替换时候，已经将参数中%2B变成加号(+)，现在需要还原，否则在内容模块中直接解码参数有可能会丢失加号(+)
				    // 将字符串中加号(+)替换成%2B
		    		ovalue = NStringUtil.strReplacePlus(ovalue);
		    		ovalues[i] = ovalue;
		    	}
		    }
            // 新增新的
    		requestWrapper.addParameter(okey, ovalues);
		}
		return true;
	}
    
	
	/**
	 * 将字符进行URLDecoder解码
	 * @param str
	 * @return
	 */
	protected static String strDecode(String str){
		try{
			if(null!=str && str.trim().length()>0){
				// 将字符进行URLDecoder解码
				str = NStringUtil.strURLDecoder(str);
    		}
		}catch(Exception ex){
			logger.error("strDecode str: [" +str+"] error !!", ex);
			ex.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * 正则表达式 过滤字符串中 非法脚本威胁 
	 * @param str
	 * @return
	 * @author caijinpeng
	 */
	protected static String replaceStrIllegalScript(String str){
		if(null==str || str.trim().length()==0){
			return str;
		}
		
		try{
			///已经统一转换过decode
			////str = NStringUtil.strURLDecoder(str,"UTF-8");
			
			// 正则表达式 过滤字符串中非法脚本威胁 
			Pattern  replse01 = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>",Pattern.CASE_INSENSITIVE);    
	        Pattern  replse02 = Pattern.compile("<[\\s]*?link[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?link[\\s]*?>",Pattern.CASE_INSENSITIVE);    
	        Pattern  replse03 = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>",Pattern.CASE_INSENSITIVE);    
	        Pattern  replse04 = Pattern.compile("<[\\s]*?iframe[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?iframe[\\s]*?>",Pattern.CASE_INSENSITIVE);    
	        Pattern  replse05 = Pattern.compile("<(script|link|style|iframe)(.|\\n)*<\\/\\>\\s*",Pattern.CASE_INSENSITIVE);    
	        Pattern  replse06 = Pattern.compile("javascript",  Pattern.CASE_INSENSITIVE);    
	        Pattern  replse07 = Pattern.compile("expression\\([^)]+\\)", Pattern.CASE_INSENSITIVE);    
	        Pattern  replse08 = Pattern.compile("[\\s]*href[\\s]*=",Pattern.CASE_INSENSITIVE);  // href=  
	        Pattern  replse09 = Pattern.compile("[\\s]*src[\\s]*=",Pattern.CASE_INSENSITIVE);   // src=

	        Pattern  replse21 = Pattern.compile("prompt[\\s]*\\(", Pattern.CASE_INSENSITIVE);  // url name=prompt(1);
			Pattern  replse22 = Pattern.compile("alert[\\s]*\\(", Pattern.CASE_INSENSITIVE);  // url name=alert(1);   
			Pattern  replse23 = Pattern.compile("confirm[\\s]*\\(", Pattern.CASE_INSENSITIVE);  // url name=eval(1); 
			Pattern  replse24 = Pattern.compile("eval[\\s]*\\(", Pattern.CASE_INSENSITIVE);  // url name=eval(1); 
			
			////Pattern  replse41 = Pattern.compile("<[\\s]*?img[^>]*?>[\\s\\S]*?", Pattern.CASE_INSENSITIVE);  // url name='<IMG SRC="/WF_XSRF8498.html">';  
			////Pattern  replse42 = Pattern.compile("<[\\s]*?a[^>]*?>[\\s\\S]*?",Pattern.CASE_INSENSITIVE);  // url name='<a href="/WF_XSRF8498.html">';  
			Pattern  replse43 = Pattern.compile("[\\s]*window[\\s]*\\[\\'location\\'\\]", Pattern.CASE_INSENSITIVE);  // url name=window['location']=222;
			Pattern  replse44 = Pattern.compile("[\\s]*window[\\s]*\\[\\u0022location\\u0022\\]", Pattern.CASE_INSENSITIVE);  // url name=window["location"]=222;
			Pattern  replse45 = Pattern.compile("[\\s]*window[\\s]*.[\\s]*location[\\s]*.[\\s]*href", Pattern.CASE_INSENSITIVE);  // url name=window.location.href=222;
			Pattern  replse46 = Pattern.compile("[\\s]*window[\\s]*\\[\\'open\\'\\]", Pattern.CASE_INSENSITIVE);  // url name=window['open']=222;
			Pattern  replse47 = Pattern.compile("[\\s]*window[\\s]*\\[\\u0022open\\u0022\\]", Pattern.CASE_INSENSITIVE);  // url name=window["open"]=222;
			Pattern  replse48 = Pattern.compile("[\\s]*window[\\s]*.[\\s]*open[\\s]*\\(", Pattern.CASE_INSENSITIVE);  // url name=window.open(;
			Pattern  replse49 = Pattern.compile("[\\s]*new[\\s]*Function[\\s]*\\(", Pattern.CASE_INSENSITIVE);  // url name=new Function(){
	        
			
			////Pattern  replse61 = Pattern.compile("(?i)on[A-Za-z]*[\\s]*?=");  //匹配不区分大小写的on  + w(有无空格) + =  
			Pattern  replse62 = Pattern.compile("(?i)onclick*[\\s]*?=");  //匹配不区分大小写的onclick  + w(有无空格) + =  
			Pattern  replse63 = Pattern.compile("(?i)onchange*[\\s]*?=");  //匹配不区分大小写的onchange  + w(有无空格) + =  
			Pattern  replse64 = Pattern.compile("(?i)onmouseover*[\\s]*?=");  //匹配不区分大小写的onmouseover  + w(有无空格) + =  
			Pattern  replse65 = Pattern.compile("(?i)onmousemove*[\\s]*?=");  //匹配不区分大小写的onmousemove  + w(有无空格) + =  
			Pattern  replse66 = Pattern.compile("(?i)onmousedown*[\\s]*?=");  //匹配不区分大小写的onmousedown  + w(有无空格) + =  
			Pattern  replse67 = Pattern.compile("(?i)ondblclick*[\\s]*?=");  //匹配不区分大小写的ondblclick  + w(有无空格) + =  
			 
			 
	        Matcher matcher01 = replse01.matcher(str);    
	        str = matcher01.replaceAll("");     
	 
	        Matcher matcher02 = replse02.matcher(str);    
	        str = matcher02.replaceAll("");     
	 
	        Matcher matcher03 = replse03.matcher(str);    
	        str = matcher03.replaceAll("");     
	 
	        Matcher matcher04 = replse04.matcher(str);    
	        str = matcher04.replaceAll("");     
	 
	        Matcher matcher05 = replse05.matcher(str);    
	        str = matcher05.replaceAll("");    
	 
	        Matcher matcher06 = replse06.matcher(str);    
	        str = matcher06.replaceAll("");    
	 
	        Matcher matcher07 = replse07.matcher(str);    
	        str = matcher07.replaceAll("");    
	 
	        Matcher matcher08 = replse08.matcher(str);    
	        str = matcher08.replaceAll("");    
	 
	        Matcher matcher09 = replse09.matcher(str);    
	        str = matcher09.replaceAll("");    
	        
	
	        
	        Matcher matcher21 = replse21.matcher(str);    
	        str = matcher21.replaceAll("");   
	        
	        Matcher matcher22 = replse22.matcher(str);    
	        str = matcher22.replaceAll("");   
	      
	        Matcher matcher23 = replse23.matcher(str);    
	        str = matcher23.replaceAll("");  
	        
	        Matcher matcher24 = replse24.matcher(str);    
	        str = matcher24.replaceAll("");  
	        
	        
	        
	        ////Matcher matcher41 = replse41.matcher(str);    
	        ////str = matcher41.replaceAll(""); 
	        
	        ////Matcher matcher42 = replse42.matcher(str);    
	        ////str = matcher42.replaceAll(""); 
	        
	        Matcher matcher43 = replse43.matcher(str);    
	        str = matcher43.replaceAll(""); 
	        
	        Matcher matcher44 = replse44.matcher(str);    
	        str = matcher44.replaceAll(""); 
	        
	        Matcher matcher45 = replse45.matcher(str);    
	        str = matcher45.replaceAll(""); 
	        
	        Matcher matcher46 = replse46.matcher(str);    
	        str = matcher46.replaceAll(""); 
	        
	        Matcher matcher47 = replse47.matcher(str);    
	        str = matcher47.replaceAll("");
	        
	        Matcher matcher48 = replse48.matcher(str);    
	        str = matcher48.replaceAll("");
	        
	        Matcher matcher49 = replse49.matcher(str);    
	        str = matcher49.replaceAll("");
	        
	        
	        
	        ////Matcher matcher61 = replse61.matcher(str);    
            ////str = matcher61.replaceAll(""); 
	        
	        Matcher matcher62 = replse62.matcher(str);    
            str = matcher62.replaceAll(""); 
            
            Matcher matcher63 = replse63.matcher(str);    
            str = matcher63.replaceAll(""); 
	        
            Matcher matcher64 = replse64.matcher(str);    
            str = matcher64.replaceAll(""); 
            
            Matcher matcher65 = replse65.matcher(str);    
            str = matcher65.replaceAll(""); 
            
            Matcher matcher66 = replse66.matcher(str);    
            str = matcher66.replaceAll(""); 
            
            Matcher matcher67 = replse67.matcher(str);    
            str = matcher67.replaceAll("");
            
		}catch(Exception ex){
			logger.error("replaceStrIllegalScript str: [" +str+"] error !!", ex);
			ex.printStackTrace();
		}
    	return str;
	}
	
	
	/**
	 * 正则表达式 过滤sql注入中 非法表达式
	 * @param str
	 * @param securityScan
	 * @return
	 */
	protected static String replaceSqlIllegalExpr(String str, String securityScan){
		if(null==str || str.trim().length()==0){
			return str;
		}
		
		try{
			///已经统一转换过decode
			////str = NStringUtil.strURLDecoder(str,"UTF-8");
			
            // 安全扫描
            if ("true".equals(securityScan)) {
            	
    			// 正则表达式 过滤sql注入中 非法表达式
    			// 不能单独判断这个几个字段，如：update, exec存在 使用情况
    			////Pattern.compile("(:')|(--)|(/\\*(:.|[\\n\\r])* \\*/)|" + "(\\b(select|and|or|delete|create|insert|update|trancate|into|substr|ascii|declare|master|into|drop|execute)\\b)",Pattern.CASE_INSENSITIVE);    
    			//Pattern replse1 = Pattern.compile("(--)|(/\\*(:.|[\\n\\r])* \\*/)|" + "(\\b(select|and|or|delete|create|insert|update|trancate|into|substr|ascii|declare|into|drop|execute)\\b)",Pattern.CASE_INSENSITIVE);    
    			//Pattern replse1 = Pattern.compile("(/\\*(:.|[\\n\\r])* \\*/)|" + "(\\b(select|and|or|sleep|delete|create|insert|update|trancate|into|substr|ascii|declare|into|drop|execute)\\b)",Pattern.CASE_INSENSITIVE);    
    			Pattern replse1 = Pattern.compile("(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)",Pattern.CASE_INSENSITIVE); 
    	        Matcher matcher1 = replse1.matcher(str);    
    	        str = matcher1.replaceAll(""); 
    	        
    	        
                for (String string : scriptTests) {
                    if (str.toLowerCase().contains(string)) {
                        str = "-1";
                        return str;
                    }  
                }
                
                str = str.replaceAll("\\.\\.", "");
                str = StringEscapeUtils.escapeHtml(str);
                str = StringEscapeUtils.escapeJavaScript(str);
                str = StringEscapeUtils.escapeSql(str);
            }

		}catch(Exception ex){
			logger.error("replaceSqlIllegalExpr str: [" +str+"] error !!", ex);
			ex.printStackTrace();
		}
    	return str;
	}
	
	
	/**
	 * 替换sql注入中 关键字 (exec|insert|select|delete|drop|truncate...)
	 * 
	 * @param str
	 * @param securityScan
	 * @return
	 * @author caijinpeng
	 */
	protected static String replaceSqlIllegalKeyWord(String str, String securityScan){
		if(null==str || str.trim().length()==0){
			return str;
		}
		

		try{
			///已经统一转换过decode
			/////str = NStringUtil.strURLDecoder(str,"UTF-8");
			 // 安全扫描
            if ("true".equals(securityScan)) {

				String newstr_lower = str.trim().toLowerCase();//统一转为为小写
				String badStrs = "exec|execute|insert|select|delete|update|drop|truncate|limit|sleep|" +
						"declare|create|like'|grant|group_concat|column_name|master|or|and|" +
						"table|from|information_schema.columns|table_schema|union|where|union";
				
				String[] badStrArry = badStrs.split("\\|");
				for(int i=0; i<badStrArry.length; i++){
					String badStr = badStrArry[i];
					if(null==badStr || badStr.length()==0){
						continue;
					}
					
					if(newstr_lower.startsWith(badStr+" ")){ 
						// 以delete table xxx开头
						str = replaceAll_IgnoreCase(str, badStr+" ", " ");
					}else if(newstr_lower.endsWith(" "+badStr)){
						// 以delete table xxx结尾
						str = replaceAll_IgnoreCase(str, " "+badStr, " ");
					}else if(newstr_lower.indexOf("("+badStr)>=0){ 
						// 字符串(delete table xx)
						str = replaceAll_IgnoreCase(str, "("+badStr, "");
					}else if(newstr_lower.indexOf(badStr+"(")>=0){ 
						// 字符串select * from(table xx)
						str = replaceAll_IgnoreCase(str, badStr+"(", "");
					}else if(newstr_lower.indexOf(badStr+")")>=0){ 
						// 字符串(delete table xx)
						str = replaceAll_IgnoreCase(str, badStr+")", "");
					}else if(newstr_lower.indexOf(")"+badStr)>=0){ 
						// 字符串  select*from(select(sleep(20)))a
						str = replaceAll_IgnoreCase(str, ")"+badStr, "");
					}else if(newstr_lower.indexOf(" "+badStr+" ")>=0){ 
						// 字符串( delete table xxx )中包括   delete 
						str = replaceAll_IgnoreCase(str, " "+badStr+" ", "  "); 
					}else if(newstr_lower.indexOf("+"+badStr+"+")>=0){ 
						//字符串  +delete+
						str = replaceAll_IgnoreCase(str, "+"+badStr+"+", "");
					}else if(newstr_lower.indexOf("("+badStr+"+")>=0){ 
						//字符串   (delete+
						str = replaceAll_IgnoreCase(str, "("+badStr+"+", "");
					}else if(newstr_lower.indexOf(badStr+"*")>=0){ 
						//字符串  select*from(select(sleep(20)))a
						str = replaceAll_IgnoreCase(str, badStr+"*", "");
					}else if(newstr_lower.indexOf("*"+badStr+"(")>=0){ 
						//字符串  select*from(select(sleep(20)))a
						str = replaceAll_IgnoreCase(str, "*"+badStr+"(", "");
					}
				
				}
			}
		}catch(Exception ex){
			logger.error("replaceSqlIllegalKeyWord str: [" +str+"] error !!", ex);
			
		}
		return str;
	}
  
	
	private static String replaceAll_IgnoreCase(String input, String regex, String replacement) {
	    try{
	        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	        Matcher m = p.matcher(input);
	        String result = m.replaceAll(replacement);
	        return result;
	    }catch(Exception ex){
	    	logger.error("replaceAll_IgnoreCase input: [" +input+"], regex: ["+ regex+"], replacement: ["+replacement+"] error !!", ex);
	        ex.printStackTrace();
	    }
		return input;
	}
	
	
}
