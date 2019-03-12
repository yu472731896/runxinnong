package com.sanrenxin.runxinnong.modules.wx.config;


import com.sanrenxin.runxinnong.common.utils.PropertiesLoader;

import java.io.Serializable;

/* *
 *类名：WeixinConfig
 *功能：weixin基础配置类
 *服务器异步通知 notify_url
微信对商户的请求数据处理完成后，会将处理的结果数据通过服务器主动通知的方式通知给商户网站。这些处理结果数据就是服务器异步通知参数。
 */

public class WeixinConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 统一下单
	 */
	public static String ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder" ;
//	public static String ORDER_URL = "https://api.mch.weixin.qq.com/sandbox/pay/micropay" ;//沙箱测试环境
	
	
	/**
	 * 查询订单
	 */
	public static String FIND_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery" ;
	
	/**
	 * 关闭订单
	 */
	public static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder" ;
	
	/**
	 * 申请退款
	 */
	public static String RERUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund" ;
	
	/**
	 * 查询退款
	 */
	public static String RERUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery" ;
	
	/**
	 * 下载对账单
	 */
	public static String DOWNLOADBILL = "https://api.mch.weixin.qq.com/pay/downloadbill" ;
	
	/**
	 * 用户同意授权，获取code
	 */
	public static String AUTHORIZE_RUL= "https://open.weixin.qq.com/connect/oauth2/authorize";
	
	/**
	 * 通过code换取网页授权access_token
	 */
	public static String ACCESS_TOKEN_RUL= "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	/**
	 * 公众账号ID
	 */
	public static final String app_id;

	/**
	 * 应用密钥
	 */
	public static final String app_secret;
	
	/**
	 * MP_TOKEN
	 */
	public static final String app_token;
	
	/**
	 * 商户号
	 */
	public static final String mch_id;
	
	/**
	 * API密钥
	 */
	public static final String api_key;
	
	/**
	 * 微信公众号上传url文件路径
	 */
	public static final String weixin_img_url;
	/**
	 * 微信公众号上传文件项目路径
	 */
	public static final String weixin_img_path;
	
	/**
	 * 字符编码格式 目前支持 gbk 或 utf-8
	 */
	public static final String input_charset = "utf-8";
	
	/**
	 * 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
	 * snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
	 */
	public static final String SCOPE = "snsapi_base";
	
	/**
	 * 返回类型，请填写code
	 */
	public static final String RESPONSE_TYPE = "code";;
	
	/**
	 * 服务器异步通知接口地址(可空)
	 */
	public static String notify_url = "";

	public WeixinConfig() {
		super();
	}

	static {
		PropertiesLoader pro = new PropertiesLoader("runxinnong.properties");
		app_id = pro.getProperty("app_id");
		app_secret = pro.getProperty("app_secret");
		app_token = pro.getProperty("app_token");
		mch_id = pro.getProperty("mch_id");
		api_key = pro.getProperty("api_key");
		weixin_img_url = pro.getProperty("weixin_img_url");
		weixin_img_path = pro.getProperty("weixin_img_path");
	}

}
