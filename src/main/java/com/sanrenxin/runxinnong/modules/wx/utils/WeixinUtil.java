package com.sanrenxin.runxinnong.modules.wx.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.utils.DateUtils;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.modules.sys.utils.AreaUtils;
import com.sanrenxin.runxinnong.modules.wx.entity.SNSUserInfo;
import com.sanrenxin.runxinnong.modules.wx.entity.WeixinOauth2Token;
import com.sanrenxin.runxinnong.modules.wx.entity.WxAccountFans;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMenu;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.utils.exception.WxError;
import com.sanrenxin.runxinnong.modules.wx.utils.exception.WxErrorException;
import com.sanrenxin.runxinnong.modules.wx.vo.Matchrule;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 公众平台通用接口工具类
 * 
 * @author liuyq
 * @date 2013-08-09
 */
@SuppressWarnings("serial")
public class WeixinUtil implements Serializable {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
	
	/**
	 * 以下注释了 
	 * 统一到 WxApi定义
	 */
	// 获取access_token的接口地址（GET） 限200（次/天）
//	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	// 菜单创建（POST） 限100（次/天）
//	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	// 客服接口地址
//	public static String send_message_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	// 用户同意授权，获取code地址
//	public static String authorize_code_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID";

	//
	// private static final ResourceBundle bundle = ResourceBundle.getBundle("weixin");
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)){
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString().trim());
		} catch (ConnectException ce) {
			log.info("Weixin server connection timed out.");
		} catch (Exception e) {
			log.info("https request error:{}" + e.getMessage());
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		
		String requestUrl = WxApi.getTokenUrl(appid, appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
			} catch (Exception e) {
				accessToken = null;
				// 获取token失败
				String wrongMessage = "获取token失败 errcode:{} errmsg:{}" + jsonObject.getInteger("errcode") + jsonObject.getString("errmsg");
				log.info(wrongMessage);
			}
		}
		
		return accessToken;
	}

	/**
	 * 编码
	 * 
	 * @param bstr
	 * @return String
	 */
	@SuppressWarnings("restriction")
	public static String encode(byte[] bstr) {
		return new sun.misc.BASE64Encoder().encode(bstr);
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return string
	 */
	@SuppressWarnings("restriction")
	public static byte[] decode(String str) {

		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bt;

	}
	
	/**
	 * 获取微信公众账号的菜单
	 * @param menus	菜单列表
	 * @param matchrule	个性化菜单配置
	 * @return
	 */
    public static JSONObject prepareMenus(List<WxMenu> menus, Matchrule matchrule, List<WxMsgText> msgTextList, List<WxMsgNews> msgNews) {
        JSONObject root = new JSONObject();
        if (!CollectionUtils.isEmpty(menus)) {
            List<WxMenu> parentAM = new ArrayList<WxMenu>();
            Map<Long, List<JSONObject>> subAm = new HashMap<Long, List<JSONObject>>();
            for (WxMenu m : menus) {
                if (m.getParentId().intValue() == 0) {// 一级菜单
                    parentAM.add(m);
                } else {// 二级菜单
                    if (subAm.get(m.getParentId()) == null) {
                        subAm.put(m.getParentId(), new ArrayList<JSONObject>());
                    }
                    List<JSONObject> tmpMenus = subAm.get(m.getParentId());
                    tmpMenus.add(getMenuJSONObj(m));
                    subAm.put(m.getParentId(), tmpMenus);
                }
            }
            JSONArray arr = new JSONArray();
            for (WxMenu m : parentAM) {
                if (subAm.get(Long.parseLong(m.getId())) != null) {// 有子菜单
                    arr.add(getParentMenuJSONObj(m, subAm.get(Long.parseLong(m.getId()))));
                } else {// 没有子菜单
                    arr.add(getMenuJSONObj(m));
                }
            }
            
            root.put("button", arr);
            root.put("matchrule", JSONObject.toJSONString(matchrule));
            
        }
        // 添加消息id
        root.put("msgs", getMsg(msgTextList,msgNews));
        return root;
    }
    public static JSONObject prepareMenus(List<WxMenu> menus, Matchrule matchrule) {
        JSONObject root = new JSONObject();
        if (!CollectionUtils.isEmpty(menus)) {
            List<WxMenu> parentAM = new ArrayList<WxMenu>();
            Map<Long, List<JSONObject>> subAm = new HashMap<Long, List<JSONObject>>();
            for (WxMenu m : menus) {
                if (m.getParentId().intValue() == 0) {// 一级菜单
                    parentAM.add(m);
                } else {// 二级菜单
                    if (subAm.get(m.getParentId()) == null) {
                        subAm.put(m.getParentId(), new ArrayList<JSONObject>());
                    }
                    List<JSONObject> tmpMenus = subAm.get(m.getParentId());
                    tmpMenus.add(getMenuJSONObj(m));
                    subAm.put(m.getParentId(), tmpMenus);
                }
            }
            JSONArray arr = new JSONArray();
            for (WxMenu m : parentAM) {
                if (subAm.get(Long.parseLong(m.getId())) != null) {// 有子菜单
                    arr.add(getParentMenuJSONObj(m, subAm.get(Long.parseLong(m.getId()))));
                } else {// 没有子菜单
                    arr.add(getMenuJSONObj(m));
                }
            }
            
            root.put("button", arr);
            root.put("matchrule", JSONObject.toJSONString(matchrule));
            
        }
        return root;
    }
    /**
     * 获取消息
     * @return
     */
	public static JSONArray getMsg(List<WxMsgText> msgTextList,List<WxMsgNews> msgNews){
		JSONArray arr = new JSONArray();
		if(CollectionUtils.isNotEmpty(msgTextList)){
			for (WxMsgText text:msgTextList) {
				JSONObject obj = new JSONObject();
				obj.put("id",text.getBaseId());
				obj.put("title",text.getTitle());
				obj.put("type", MsgType.Text.toString());
				arr.add(obj);
			}
		}
		if(CollectionUtils.isNotEmpty(msgNews)){
			for (WxMsgNews news:msgNews) {
				JSONObject obj = new JSONObject();
				obj.put("id",news.getBaseId());
				obj.put("title",news.getTitle());
				obj.put("type", MsgType.News.toString());
				arr.add(obj);
			}
		}
		return arr;
	}
	public static JSONObject getParentMenuJSONObj(WxMenu menu,List<JSONObject> subMenu){
		JSONObject obj = new JSONObject();
		obj.put("name", menu.getName());
		obj.put("sub_button", subMenu);
		return obj;
	}

	/**
	 * 此方法是构建菜单对象的；构建菜单时，对于  key 的值可以任意定义；
	 * 当用户点击菜单时，会把key传递回来；对已处理就可以了
	 * @param menu
	 * @return
	 */
	public static JSONObject getMenuJSONObj(WxMenu menu){
		JSONObject obj = new JSONObject();
		obj.put("name", menu.getName());
		obj.put("type", menu.getMtype());
		if(MessageUtil.MENU_NEED_KEY.contains(menu.getMtype())){//事件菜单
			if("fix".equals(menu.getEventType())){//fix 消息
				obj.put("key", "_fix_" + menu.getMsgId());//以 _fix_ 开头
			}else{
				if(StringUtils.isEmpty(menu.getInputCode())){//如果inputcode 为空，默认设置为 subscribe，以免创建菜单失败
					obj.put("key", "subscribe");
				}else{
					obj.put("key", menu.getInputCode());
				}
			}
			//存msgtype id
			obj.put("msgType", menu.getMsgType());
			obj.put("msgId", menu.getMsgId());//
			obj.put("MsgType", menu.getMsgType());
			obj.put("MsgId", menu.getMsgId());//
		}else{//链接菜单-view
			obj.put("url", menu.getUrl().replaceAll("&amp;", "&"));
		}
		return obj;
	}
	
	//发布菜单
		public static JSONObject publishMenus(String menus,AccessToken accessToken){
			String token = accessToken.getToken();
			String url = WxApi.getMenuCreateUrl(token);
			return WxApi.httpsRequest(url, HttpMethod.POST, menus);
		}
		
		//创建个性化菜单
		public static JSONObject publishAddconditionalMenus(String menus,AccessToken accessToken){
			String token = accessToken.getToken();
			String url = WxApi.getMenuAddconditionalUrl(token);
			return WxApi.httpsRequest(url, HttpMethod.POST, menus);
		}
		
		//删除菜单
		public static JSONObject deleteMenu(AccessToken accessToken){
			String token = accessToken.getToken();
			String url = WxApi.getMenuDeleteUrl(token);
			return WxApi.httpsRequest(url, HttpMethod.POST, null);
		}
		
		//根据openId获取粉丝信息
		public static WxAccountFans syncAccountFans(String openId, AccessToken accessToken){
			String token = accessToken.getToken();
			log.info("获取用户信息接口accessToken："+token);
			String url = WxApi.getFansInfoUrl(token, openId);
			log.info("获取用户信息接口url："+url);
			JSONObject jsonObj = WxApi.httpsRequest(url, "GET", null);
			if (null != jsonObj) {
				log.info("获取用户信息接口返回结果："+jsonObj.toString());
				if(jsonObj.containsKey("errcode")){
//					int errorCode = jsonObj.getInt("errcode");
					return null;
				}else{
					WxAccountFans fans = new WxAccountFans();
					fans.setOpenId(jsonObj.getString("openid"));// 用户的标识
					fans.setSubscribeStatus(jsonObj.getString("subscribe"));// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
					if(jsonObj.containsKey("subscribe_time")){
						System.out.println("订阅时间是：" + jsonObj.getString("subscribe_time"));
						fans.setSubscribeTime(DateUtils.timestampToDate(jsonObj.getString("subscribe_time")));// 用户关注时间
					}
					if(jsonObj.containsKey("nickname")){// 昵称
						try {
							String nickname = jsonObj.getString("nickname");
							fans.setNickName(nickname.getBytes("UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					if(jsonObj.containsKey("sex")){// 用户的性别（1是男性，2是女性，0是未知）
						fans.setGender(jsonObj.getString("sex"));
					}
					if(jsonObj.containsKey("language")){// 用户的语言，简体中文为zh_CN
						fans.setLanguage(jsonObj.getString("language"));
					}
					if(jsonObj.containsKey("country")){// 用户所在国家
						fans.setCountry(jsonObj.getString("country"));
					}
					if(jsonObj.containsKey("province")){// 用户所在省份
						fans.setProvince(jsonObj.getString("province"));
					}
					if(jsonObj.containsKey("city")){// 用户所在城市
						fans.setCity(jsonObj.getString("city"));
					}
					if(jsonObj.containsKey("headimgurl")){// 用户头像
						fans.setHeadImgUrl(jsonObj.getString("headimgurl"));
					}
					if(jsonObj.containsKey("remark")){
						fans.setRemark(jsonObj.getString("remark"));
					}
					fans.setStatus("1");
					return fans;
				}
			}
			return null;
		}
		
		//新增微信永久素材
		public static JSONObject addMaterial(String filePath,String materialType,AccessToken accessToken){
			JSONObject rstObj = new JSONObject();
			String token = accessToken.getToken();
			try{
				//JSONObject postObj = new JSONObject();
				//postObj.put("media", file);
				//String filePath = "D:/img/Tulips.jpg";
				//媒体类型
				//String materialType = MediaType.Image.toString();
				//上传永久图片素材
				rstObj = WxApi.addMaterial(WxApi.getMaterialUrl(token,materialType), filePath);
			}catch(Exception e){
				e.printStackTrace();
			}
			return rstObj;
		}
		//上传永久图片
		public static JSONObject uploadMaterialImg(String filePath,AccessToken mpAccount){
			JSONObject rstObj = new JSONObject();
			String accessToken = mpAccount.getToken();
			try{
//				JSONObject postObj = new JSONObject();
				//postObj.put("media", file);
//				String filePath = file.getAbsolutePath();
				//上传永久图片素材
				rstObj = WxApi.addMaterial(WxApi.getMaterialImgUrl(accessToken), filePath);
			}catch(Exception e){
				e.printStackTrace();
			}
			return rstObj;
		}

		/**
		 * 新增永久图文素材
		 * @param msgNewsList
		 * @param imgMediaId
		 * @param mpAccount
		 * @return
		 */
		public static JSONObject addNewsMaterial(List<WxMsgNews> msgNewsList,
				String imgMediaId, AccessToken mpAccount) {
			JSONObject rstObj = new JSONObject();
			String accessToken = mpAccount.getToken();
			try{
				JSONArray jsonArr = new JSONArray();
				for(WxMsgNews news : msgNewsList){
					JSONObject jsonObj = new JSONObject();
					//上传图片素材
					jsonObj.put("thumb_media_id", imgMediaId);
					if(news.getAuthor() != null){
						jsonObj.put("author", news.getAuthor());
					}else{
						jsonObj.put("author", "");
					}
					if(news.getTitle() != null){
						jsonObj.put("title", news.getTitle());
					}else{
						jsonObj.put("title", "");
					}
					if(news.getFromUrl() != null){
						jsonObj.put("content_source_url", news.getFromUrl());
					}else{
						jsonObj.put("content_source_url", "");
					}
					if(news.getBrief() != null){
						jsonObj.put("digest", news.getBrief());
					}else{
						jsonObj.put("digest", "");
					}
					if(news.getShowPic() != null){
						jsonObj.put("show_cover_pic", news.getShowPic());
					}else{
						jsonObj.put("show_cover_pic", 1);
					}
					jsonObj.put("need_open_comment", news.getNeedOpenComment());
					jsonObj.put("only_fans_can_comment", news.getOnlyFansCanComment());
					jsonObj.put("content", news.getDescription());
					jsonArr.add(jsonObj);
				}
				JSONObject postObj = new JSONObject();
				postObj.put("articles", jsonArr);
				rstObj = WxApi.httpsRequest(WxApi.getNewsMaterialUrl(accessToken), HttpMethod.POST, postObj.toString());
			}catch(Exception e){
				e.printStackTrace();
			}
			return rstObj;
		}
		
		/**
		 * 根据media_id获取永久图文素材
		 * @param media_id
		 * @param mpAccount
		 * @return
		 */
		public static JSONObject getMaterial(String media_id, AccessToken mpAccount){
				JSONObject postObj = new JSONObject();
				postObj.put("media_id", media_id);
				String accessToken = mpAccount.getToken();
				return WxApi.httpsRequest(WxApi.getMaterial(accessToken),
						HttpMethod.POST, postObj.toString());
		}

		/**
		 * 修改永久图文素材
		 * @param msgNewsList
		 * @param mpAccount
		 * @return
		 */
		public static JSONObject updateNewsMaterial(
				List<WxMsgNews> msgNewsList, int index, String mediaId,
				AccessToken mpAccount) {
				JSONObject rstObj = new JSONObject();
				String accessToken = mpAccount.getToken();
				try{
					WxMsgNews news =msgNewsList.get(0);
					JSONObject jsonObj = new JSONObject();
					
						//上传图片素材
						jsonObj.put("thumb_media_id", news.getThumbMediaId());
						if(news.getAuthor() != null){
							jsonObj.put("author", news.getAuthor());
						}else{
							jsonObj.put("author", "");
						}
						if(news.getTitle() != null){
							jsonObj.put("title", news.getTitle());
						}else{
							jsonObj.put("title", "");
						}
						if(news.getFromUrl() != null){
							jsonObj.put("content_source_url", news.getFromUrl());
						}else{
							jsonObj.put("content_source_url", "");
						}
						if(news.getBrief() != null){
							jsonObj.put("digest", news.getBrief());
						}else{
							jsonObj.put("digest", "");
						}
						if(news.getShowPic() != null){
							jsonObj.put("show_cover_pic", news.getShowPic());
						}else{
							jsonObj.put("show_cover_pic", 1);
						}
						jsonObj.put("content", news.getDescription());
						
					jsonObj.put("need_open_comment", news.getNeedOpenComment());
					jsonObj.put("only_fans_can_comment", news.getOnlyFansCanComment());
					JSONObject postObj = new JSONObject();
					postObj.put("media_id", mediaId);
					postObj.put("index", index);
					postObj.put("articles", jsonObj);
					rstObj = WxApi.httpsRequest(WxApi.getUpdateNewsMaterialUrl(accessToken),
							
							HttpMethod.POST, postObj.toString());
				}catch(Exception e){
					e.printStackTrace();
				}
				return rstObj;
		}

		public static JSONObject addMoreNewsMaterial2(JSONArray arryarticles,
				AccessToken mpAccount) {
			JSONObject rstObj = new JSONObject();
			String accessToken = mpAccount.getToken();
			try{
				JSONObject postObj = new JSONObject();
				postObj.put("articles", arryarticles);
				rstObj = WxApi.httpsRequest(WxApi.getNewsMaterialUrl(accessToken), HttpMethod.POST, postObj.toString());
			}catch(Exception e){
				e.printStackTrace();
			}
			return rstObj;
		}
		
		public static JSONObject updateNewsMaterial2(JSONObject jsonObj,String index,
				String mediaId,AccessToken mpAccount){
				JSONObject rstObj = new JSONObject();
				String accessToken = mpAccount.getToken();
				try{
					JSONObject postObj = new JSONObject();
					postObj.put("media_id", mediaId);
					postObj.put("index", index);
					postObj.put("articles", jsonObj);
					rstObj = WxApi.httpsRequest(WxApi.getUpdateNewsMaterialUrl(accessToken),
							
							HttpMethod.POST, postObj.toString());
				}catch(Exception e){
					e.printStackTrace();
				}
				return rstObj;
		}

		public static JSONObject deleteMaterial(String mediaId,
				AccessToken accessToken)  {
	        JSONObject postObj = new JSONObject();
	        postObj.put("media_id", mediaId);
	        String token = accessToken.getToken();
	        JSONObject rstObj = WxApi.httpsRequest(WxApi.getDelMaterialURL(token), HttpMethod.POST, postObj.toString());
	        return rstObj;
			
		}

		public static com.alibaba.fastjson.JSONObject addMateria(String token,
				String type, String uploadUrl, Map<String, String> params) throws WxErrorException {
			 File file =new File(uploadUrl);
		        if(!file.exists()){
		            return null;
		        }
		        String fileName=file.getName();
		        //获取后缀名
		        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		        long length=file.length();
		        //此处做判断是为了尽可能的减少对微信API的调用次数
		        if(WxApi.type_fix.get(type).indexOf(suffix)==-1){
		            throw new WxErrorException(WxError.newBuilder().setErrorCode(40005).setErrorMsg("不合法的文件类型").build());
		        }
		        if(length>WxApi.type_length.get(type)){
		            throw new WxErrorException(WxError.newBuilder().setErrorCode(40006).setErrorMsg("不合法的文件大小").build());
		        }
		        String url=WxApi.getMaterialUrl(token, type);
		        String result = HttpClientUtils.sendHttpPost(url, file, params);
		        WxError wxError = WxError.fromJson(result);
		        if(wxError.getErrorCode()!=0){
		            throw new WxErrorException(wxError);
		        }

		        return com.alibaba.fastjson.JSONObject.parseObject(result);
		}

		/**
		 * {"errcode":0,
		 * 	"errmsg":"ok",
		 * 	"ticket":"sM4AOVdWfPE4DxkXGEs8VDg5GTagXjSDhE3pU2_Xr6NNDjOIxlt_T0-A1FfukLQIf7Ux1ZVdKtC41Gx9gXLMMA",
		 * 	"expires_in":7200}
		 * @param accessToken
		 * @return
		 */
		public static String getJsApiTicket(String accessToken) {
			
			log.debug("获取 getJsApiTicket时的accessToken:" + accessToken);
			JSONObject result = WxApi.httpsRequest(WxApi.getJsApiTicketUrl(accessToken), HttpMethod.POST);
			if(result.getInteger("errcode")==0){
				return result.getString("ticket");
			}else{
				log.debug("获取失败 getJsApiTicket:"+result.toString());
				return null;
			}
		}
		
		
		
		/**
		 * 获取网页授权凭证
		 * 
		 * @param appId 公众账号的唯一标识
		 * @param appSecret 公众账号的密钥
		 * @param code
		 * @return WeixinAouth2Token
		 */
		public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
			WeixinOauth2Token wat = null;
			// 拼接请求地址
			String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			requestUrl = requestUrl.replace("APPID", appId);
			requestUrl = requestUrl.replace("SECRET", appSecret);
			requestUrl = requestUrl.replace("CODE", code);
			// 获取网页授权凭证
			JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
			System.out.println("网页授权凭证获得什么值========："+jsonObject.toString());
			if (null != jsonObject) {
				try {
					wat = new WeixinOauth2Token();
					wat.setAccessToken(jsonObject.getString("access_token"));
					wat.setExpiresIn(jsonObject.getInteger("expires_in"));
					wat.setRefreshToken(jsonObject.getString("refresh_token"));
					wat.setOpenId(jsonObject.getString("openid"));
					wat.setScope(jsonObject.getString("scope"));
				} catch (Exception e) {
					wat = null;
					int errorCode = jsonObject.getInteger("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}
			return wat;
		}

		/**
		 * 刷新网页授权凭证
		 * 
		 * @param appId 公众账号的唯一标识
		 * @param refreshToken
		 * @return WeixinAouth2Token
		 */
		public static WeixinOauth2Token refreshOauth2AccessToken(String appId, String refreshToken) {
			WeixinOauth2Token wat = null;
			// 拼接请求地址
			String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
			requestUrl = requestUrl.replace("APPID", appId);
			requestUrl = requestUrl.replace("REFRESH_TOKEN", refreshToken);
			// 刷新网页授权凭证
			JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
			if (null != jsonObject) {
				try {
					wat = new WeixinOauth2Token();
					wat.setAccessToken(jsonObject.getString("access_token"));
					wat.setExpiresIn(jsonObject.getInteger("expires_in"));
					wat.setRefreshToken(jsonObject.getString("refresh_token"));
					wat.setOpenId(jsonObject.getString("openid"));
					wat.setScope(jsonObject.getString("scope"));
				} catch (Exception e) {
					wat = null;
					int errorCode = jsonObject.getInteger("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					log.error("刷新网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}
			return wat;
		}

		/**
		 * 通过网页授权获取用户信息
		 * 
		 * @param accessToken 网页授权接口调用凭证
		 * @param openId 用户标识
		 * @return SNSUserInfo
		 */
		@SuppressWarnings( { "deprecation", "unchecked" })
		public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
			SNSUserInfo snsUserInfo = null;
			// 拼接请求地址
			String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
			requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
			// 通过网页授权获取用户信息
			JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);

			if (null != jsonObject) {
				try {
					snsUserInfo = new SNSUserInfo();
					// 用户的标识
					snsUserInfo.setOpenId(jsonObject.getString("openid"));
					// 昵称
					snsUserInfo.setNickname(jsonObject.getString("nickname"));
					// 性别（1是男性，2是女性，0是未知）
					snsUserInfo.setSex(jsonObject.getInteger("sex"));
					// 用户所在国家
					snsUserInfo.setCountry(jsonObject.getString("country"));
					// 用户所在省份
					String province = AreaUtils.findAreaIdByLikeNameAndType(jsonObject.getString("province"), "2");
					snsUserInfo.setProvince(province);
					// 用户所在城市
					String city = AreaUtils.findAreaIdByLikeNameAndType(jsonObject.getString("city"), "3");
					snsUserInfo.setCity(city);
					// 用户头像
					snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
					// 用户特权信息
					snsUserInfo.setPrivilegeList(JSONArray.parseArray(jsonObject.getJSONArray("privilege").toJSONString(),String.class));
				} catch (Exception e) {
					snsUserInfo = null;
					int errorCode = jsonObject.getInteger("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}
			return snsUserInfo;
		}
}