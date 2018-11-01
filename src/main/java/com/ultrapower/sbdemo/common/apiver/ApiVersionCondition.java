package com.ultrapower.sbdemo.common.apiver;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

/**
 * 
 * @ClassName:     ApiVersionCondition
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2018年3月21日 上午11:28:08
 */
public class ApiVersionCondition extends AbstractRequestCondition<ApiVersionCondition> {

	// 路径中版本的前缀， 这里用 /v[1-9]/的形式
    ///private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
	// 路径中版本的前缀， 这里用 /v[1-9][.][0-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v([1-9]\\d*\\.?\\d*)|(0\\.\\d*[0-9])");
     
    private double apiVersion;
     
    public ApiVersionCondition(double apiVersion){
        this.apiVersion = apiVersion;
    }
    
    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(other.getApiVersion());
    }
    
    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
    	String uri = request.getServletPath();
    	Matcher m = VERSION_PREFIX_PATTERN.matcher(uri);
        if(m.find()){
        	double version = Double.parseDouble(m.group(1));
        	
        	BigDecimal data1 = new BigDecimal(version); 
            BigDecimal data2 = new BigDecimal(this.apiVersion); 
            
            if (data1.compareTo(data2) >= 0) { // 如果请求的版本号大于配置版本号， 则满足
            	return this;
            } 
            
        }
        return null;
    }
    
    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
    	// 优先匹配最新的版本号
    	BigDecimal data1 = new BigDecimal(other.getApiVersion()); 
        BigDecimal data2 = new BigDecimal(this.apiVersion); 
        
        if (data1.compareTo(data2) > 0) {  
            return 1; //如果指定的数大于参数返回 1。
        } 
     
        if (data1.compareTo(data2) < 0) {  
            return -1;  //如果指定的数小于参数返回 -1。
        }  
        if (data1.compareTo(data2) == 0) {  
            return 0;   //如果指定的数与参数相等返回0。
        }  
        
        return 1;
    }
 
    public double getApiVersion() {
        return apiVersion;
    }

	@Override
	protected Collection<?> getContent() {
		Set<Double> set = new HashSet<Double>();
		set.add(this.apiVersion);
		return set;
	}

	@Override
	protected String getToStringInfix() {
		// TODO Auto-generated method stub
		return " || ";
	}

}
