/**
 * #################################################
 * js加密和解密主方法
 * 
 *   引用 js/crypto/aes.js
 *      js/crypto/components/mode-ecb.js
 * ##################################################
 */

/**
 * 加密
 */
function AESEncrypt(word){  
     var key = CryptoJS.enc.Utf8.parse("ultrapower@itjk8");   
  
     var srcs = CryptoJS.enc.Utf8.parse(word);  
     var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
     return encrypted.toString();  
}  
/**
 * 解密
 */
function AESDecrypt(word){   
    var key = CryptoJS.enc.Utf8.parse("ultrapower@itjk8");   
  
    var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();  
}  
	    