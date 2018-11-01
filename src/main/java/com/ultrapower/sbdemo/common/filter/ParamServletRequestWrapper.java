package com.ultrapower.sbdemo.common.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName:     ParamServletRequestWrapper
 * @Description:   重写请求参数处理函数
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年1月24日 上午10:49:10
 */
public class ParamServletRequestWrapper extends HttpServletRequestWrapper {

	HttpServletRequest orgRequest = null; 
	
    private Map<String , String[]> params = new HashMap<String, String[]>();

    public ParamServletRequestWrapper(HttpServletRequest request) {
        // 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
        super(request);
        orgRequest = request;  
        //将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
    }

    /**  
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>  
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>  
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖  
     */  
    @Override
    public String getParameter(String name) {//重写getParameter，代表参数从当前类中的map获取
    	name = JsoupXssUtil.clean(name); 
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        String value = values[0];
        if (StringUtils.isNotBlank(value)) {  
            value = JsoupXssUtil.clean(value);  
        }  
        return value;    
    }
    
    
    @Override  
    public String[] getParameterValues(String name) {//同上
    	String[] arr = params.get(name);
    	if(arr != null){  
            for (int i=0;i<arr.length;i++) {  
                arr[i] = JsoupXssUtil.clean(arr[i]);  
            }  
        }  
        return arr;  
    }
    
 
    /**  
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>  
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>  
     * getHeaderNames 也可能需要覆盖  
     */    
     @Override    
     public String getHeader(String name) {    
         name = JsoupXssUtil.clean(name);  
         String value = super.getHeader(name);    
         if (StringUtils.isNotBlank(value)) {    
             value = JsoupXssUtil.clean(value);   
         }    
         return value;    
     }  
     
  
     
     public void addAllParameters(Map<String , Object>otherParams) {//增加多个参数
         for(Map.Entry<String , Object>entry : otherParams.entrySet()) {
             addParameter(entry.getKey() , entry.getValue());
         }
     }

     public void addParameter(String name , Object value) {//增加参数
         if(value != null) {
             if(value instanceof String[]) {
                 params.put(name , (String[])value);
             }else if(value instanceof String) {
                 params.put(name , new String[] {(String)value});
             }else {
                 params.put(name , new String[] {String.valueOf(value)});
             }
         }
     }     
      
}
