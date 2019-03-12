package com.sanrenxin.runxinnong.modules.wx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** 
 * 官方给的使用js的验证工具 
 * @author Administrator 
 * 
 */  
public class JsSignUtil {  
	protected static Logger logger = LoggerFactory.getLogger(JsSignUtil.class);
    public static Map<String, String> sign(String url, String appid, String appsecret) {
        
    	String jsapi_ticket = TokenThread.jsapi_ticket;  
    	Map<String, String> ret = new HashMap<String, String>();  
    	if(null == jsapi_ticket){
    		ret.put("code", "-1");
    	}
        
        
        String nonce_str = create_nonce_str();  
        String timestamp = create_timestamp();  
        String string1;  
        String signature = "";  
  
        //注意这里参数名必须全部小写，且必须有序  
        string1 = "jsapi_ticket=" + jsapi_ticket +  
                  "&noncestr=" + nonce_str +  
                  "&timestamp=" + timestamp +  
                  "&url=" + url;  
       logger.debug("string1="+string1);  
  
        try  
        {  
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");  
            crypt.reset();  
            crypt.update(string1.getBytes("UTF-8"));  
            signature = byteToHex(crypt.digest());  
        }  
        catch (NoSuchAlgorithmException e)  
        {  
        	logger.error("sign...NoSuchAlgorithmException..." + e.getMessage());
        }  
        catch (UnsupportedEncodingException e)  
        {  
        	logger.error("sign...UnsupportedEncodingException..." + e.getMessage());
        }  
        ret.put("code", "0");
        ret.put("url", url);  
        ret.put("jsapi_ticket", jsapi_ticket);  
        ret.put("nonceStr", nonce_str);  
        ret.put("timestamp", timestamp);  
        ret.put("signature", signature);  
        ret.put("appId", appid);  
  
       logger.debug("1.ticket(原始)="+jsapi_ticket);  
       logger.debug("2.url="+ret.get("url"));  
       logger.debug("3.jsapi_ticket（处理后）="+ret.get("jsapi_ticket"));  
       logger.debug("4.nonceStr="+ret.get("nonceStr"));  
       logger.debug("5.signature="+ret.get("signature"));  
       logger.debug("6.timestamp="+ret.get("timestamp"));  
          
        return ret;  
    }  
      
      
    /** 
     * 随机加密 
     * @param hash 
     * @return 
     */  
    private static String byteToHex(final byte[] hash) {  
        Formatter formatter = new Formatter();  
        for (byte b : hash)  
        {  
            formatter.format("%02x", b);  
        }  
        String result = formatter.toString();  
        formatter.close();  
        return result;  
    }  
  
    /** 
     * 产生随机串--由程序自己随机产生 
     * @return 
     */  
    private static String create_nonce_str() {  
        return UUID.randomUUID().toString();  
    }  
  
    /** 
     * 由程序自己获取当前时间 
     * @return 
     */  
    private static String create_timestamp() {  
        return Long.toString(System.currentTimeMillis() / 1000);  
    }  
}  