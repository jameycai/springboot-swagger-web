package com.ultrapower.sbdemo.common.abstractclass;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @ClassName:     BaseRestTemplate
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @company        北京神州泰岳软件股份有限公司
 * @author         caijinpeng
 * @email          caijinpeng@ultrapower.com.cn
 * @version        V1.0
 * @Date           2018年5月17日 下午5:47:48
 */
public abstract class BaseRestTemplate {

	@Resource
    public RestTemplate restTemplate;

	
	/**
	 * 调用RESTful接口的方法，基于RestTemplate来实现
	 * 
	 * @param url            RESTful的URL地址
	 * @param method         RESTful接口方法类型(HttpMethod.GET/HttpMethod.PUT/HttpMethod.POST/HttpMethod.DELETE)
	 * @param requestEntity  请求Body(HttpEntity<T>对象泛型，如： HttpEntity<Map>、  HttpEntity<List>、  HttpEntity<Bean>)
	 * @param responseType   返回值的类型
	 * @param uriVariables   请求URL上的参数，可以传递多个参数，这个参数的个数是不确定的
	 * @return
	 * @throws RestClientException
	 */
	protected <T> ResponseEntity<T> exchangeRestData(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
		
		return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
		
	}
	
	/**
	 * 调用RESTful接口的方法，基于RestTemplate来实现
	 * 
	 * @param url            RESTful的URL地址
	 * @param method         RESTful接口方法类型(HttpMethod.GET/HttpMethod.PUT/HttpMethod.POST/HttpMethod.DELETE)
	 * @param requestEntity  请求Body(HttpEntity<T>对象泛型，如： HttpEntity<Map>、  HttpEntity<List>、  HttpEntity<Bean>)
	 * @param responseType   返回值的类型
	 * @param uriVarMap      请求URL上的参数的Map对象
	 * @return
	 * @throws RestClientException
	 */
	protected <T> ResponseEntity<T> exchangeRestData(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Map<String, ?> uriVarMap) throws RestClientException {
		
		return restTemplate.exchange(url, method, requestEntity, responseType, uriVarMap);
	}
	
}
