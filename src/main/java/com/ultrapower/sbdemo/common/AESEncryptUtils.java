package com.ultrapower.sbdemo.common;

import javax.crypto.Cipher;  
import javax.crypto.KeyGenerator;  
import javax.crypto.spec.SecretKeySpec;  
  
import org.apache.commons.codec.binary.Base64;  
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @ClassName:     AESEncryptUtils
 * @Description:   AES加密，与前端aes.js一致
 * 
 * @company        Ultrapower
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2017年12月20日 下午12:54:59 
 *
 */
public class AESEncryptUtils {
	
	private static Logger log = LoggerFactory.getLogger(AESEncryptUtils.class);
	
	/** 
     * 密钥  16位
    */  
    private static final String KEY = "ultrapower@itjk8";  
      
    /** 
     * 算法 
     */  
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";  
  
    
    public static void main(String[] args) throws Exception {  
        String content = "北京神州泰岳软件 Ultra_2.x.x 版本，全网络产品。";  
        System.out.println("加密前：" + content);  
  
        ///System.out.println("加密密钥和解密密钥：" + KEY);  
  
        String encrypt = aesEncrypt(content, KEY);  
        System.out.println("加密后：" + encrypt);  
  
        String decrypt = aesDecrypt(encrypt, KEY);  
        System.out.println("解密后：" + decrypt);  
    }  
    
      
    /** 
     * aes解密 
     * @param encrypt内容 
     * @return 
     * @throws Exception 
     */  
    public static String aesDecrypt(String encrypt) {
    	try{
    		if(null==encrypt || encrypt.trim().length()==0){
    			return encrypt;
    		}
    		
    		return aesDecrypt(encrypt, KEY);  
    	}catch(Throwable t){
    		log.error("AESEncryptUtils", "aesDecrypt erorr!!! encrypt:"+encrypt, t);
    		t.printStackTrace();
    	}
    	return encrypt;
    }  
      
    /** 
     * aes加密 
     * @param content内容  
     * @return 
     * @throws Exception 
     */  
    public static String aesEncrypt(String content) {  
    	try{
    		if(null==content || content.trim().length()==0){
    			return content;
    		}
    		
    		return aesEncrypt(content, KEY);  
    	}catch(Throwable t){
    		log.error("AESEncryptUtils", "aesEncrypt erorr!!! content:"+content, t);
    		t.printStackTrace();
    	}
    	return content;
    }  
  
     
  
    /** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
    private static String base64Encode(byte[] bytes){  
        return new String(Base64.encodeBase64(bytes));  
    }  
  
    /** 
     * base 64 decode 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的byte[] 
     * @throws Exception 
     */  
    private static byte[] base64Decode(String base64Code) throws Exception{  
        return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);  
    }  
  
      
    /** 
     * AES加密 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的byte[] 
     * @throws Exception 
     */  
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));  
  
        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
  
  
    /** 
     * AES加密为base 64 code 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的base 64 code 
     * @throws Exception 
     */  
    private static String aesEncrypt(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }  
  
    /** 
     * AES解密 
     * @param encryptBytes 待解密的byte[] 
     * @param decryptKey 解密密钥 
     * @return 解密后的String 
     * @throws Exception 
     */  
     private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
  
        return new String(decryptBytes);  
    }  
  
  
    /** 
     * 将base 64 code AES解密 
     * @param encryptStr 待解密的base 64 code 
     * @param decryptKey 解密密钥 
     * @return 解密后的string 
     * @throws Exception 
     */  
    private static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {  
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);  
    }  
  
}
