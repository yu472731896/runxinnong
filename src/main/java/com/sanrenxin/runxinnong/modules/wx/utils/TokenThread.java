package com.sanrenxin.runxinnong.modules.wx.utils;

import com.sanrenxin.runxinnong.modules.wx.config.WeixinConfig;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时获取微信access_token的线程
 * 
 * @author LiuDeHua 
 * @date 2016-09-19 
 */
public class TokenThread implements Runnable {

	private static Logger log = LoggerFactory.getLogger(TokenThread.class);

	public static AccessToken accessToken = null;
	public static String jsapi_ticket = null;
	@Override
	public void run() {
		while (true) {
			try {
				accessToken = WeixinUtil.getAccessToken(WeixinConfig.app_id, WeixinConfig.app_secret);
				if (null != accessToken) {
					log.info("获取access_token成功，有效时长{}秒 token:{}", accessToken.getExpiresIn(), accessToken.getToken());
					jsapi_ticket = WeixinUtil.getJsApiTicket(accessToken.getToken()); 
					if(null == jsapi_ticket){
						log.info("TokenThread获取jsapi_ticket为null");
						// 如果jsapi_ticket为null，60秒后再获取
						Thread.sleep(60 * 1000);
//						accessToken = WeixinUtil.getAccessToken(WeixinConfig.app_id, WeixinConfig.app_secret);
//						jsapi_ticket = WeixinUtil.getJsApiTicket(accessToken.getToken());  
					}else{
						log.info("TokenThread获取jsapi_ticket不为空 ，将睡眠7000秒");
						// 休眠7200-300秒
						Thread.sleep((accessToken.getExpiresIn() - 300) * 1000);
//						Thread.sleep(10 * 1000);
					}
				} else {
					log.info("TokenThread获取accessToken为null");
					// 如果access_token为null，60秒后再获取
					Thread.sleep(60 * 1000);
				}
			} catch (InterruptedException e) {
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e1) {
					log.error("{}", e1);
				}
				log.error("{}", e);
			}
		}
	}
	/**
	 * 当获取失败时，重新获取accessToken
	 */
	public static void regetAccessToken() {
		// TODO Auto-generated method stub
		log.info("将重新获取accessToken");
		accessToken = WeixinUtil.getAccessToken(WeixinConfig.app_id, WeixinConfig.app_secret);
	}

}
