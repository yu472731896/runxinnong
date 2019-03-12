package com.sanrenxin.runxinnong.modules.wx.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.modules.wx.config.WeixinConfig;
import com.sanrenxin.runxinnong.modules.wx.dao.WxAccountFansDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxArticleDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMenuDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgBaseDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgFirstDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgKeyDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgNewsDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgTextDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxTplMsgTextDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxAccountFans;
import com.sanrenxin.runxinnong.modules.wx.entity.WxArticle;
import com.sanrenxin.runxinnong.modules.wx.entity.WxFansText;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMenu;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgKey;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;
import com.sanrenxin.runxinnong.modules.wx.entity.WxTplMsgText;
import com.sanrenxin.runxinnong.modules.wx.entity.WxUser;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.entity.message.resp.Article;
import com.sanrenxin.runxinnong.modules.wx.entity.message.resp.NewsMessageResp;
import com.sanrenxin.runxinnong.modules.wx.entity.message.resp.TextMessageResp;
import com.sanrenxin.runxinnong.modules.wx.utils.HttpMethod;
import com.sanrenxin.runxinnong.modules.wx.utils.MessageUtil;
import com.sanrenxin.runxinnong.modules.wx.utils.MsgType;
import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import com.sanrenxin.runxinnong.modules.wx.utils.WeixinUtil;
import com.sanrenxin.runxinnong.modules.wx.utils.WxApi;
import com.sanrenxin.runxinnong.modules.wx.utils.WxMessageBuilder;
import com.sanrenxin.runxinnong.modules.wx.vo.Matchrule;
import com.sanrenxin.runxinnong.modules.wx.vo.TemplateMessage;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信表Service
 * 
 * @author LiuDeHua
 * @version 2016-01-16
 */
@Service
public class WechatService {
	
	private static Logger log = LoggerFactory.getLogger(WechatService.class);
	
	@Autowired
	private WxMenuDao wxMenuDao;
	@Autowired
	private WxAccountFansDao wxAccountFansDao;
	@Autowired
	private WxTplMsgTextDao wxTplMsgTextDao;
	@Autowired
	private WxMsgBaseDao baseDao;
	@Autowired
	private WxMsgTextDao wxMsgTextDao;
	@Autowired
	private WxMsgFirstDao wxMsgFirstDao;
	@Autowired
	private WxMsgNewsDao wxMsgNewsDao;
	@Autowired
	private WxArticleDao wxArticleDao;
	@Autowired
	private WxMsgKeyDao wxMsgKeyDao;
//	@Autowired
//	private ShopMemberidOpenidMappingDao shopMemberidOpenidMappingDao;
//	@Autowired
//	private ShopMemberDao shopMemberDao;
	@Autowired
	private WxFansTextService wxFansTextService;
//	@Autowired
//	private ShopParameterTabDao shopParameterTabDao;
	
	public String coreService(HttpServletRequest request, HttpServletResponse response) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "欢迎光临络！";
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息主键
//			String msgId = requestMap.get("MsgId");
			// 消息内容
			String content = requestMap.get("Content");
			// 带参二维码
			String EventKey = requestMap.get("EventKey");
			

			// 【微信触发类型】文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				if(StringUtils.isNotBlank(content)){//内容不为空的时候进行
					WxMsgKey key = new WxMsgKey();
					key.setInputCode(content);
					List<WxMsgKey> keylist = wxMsgKeyDao.findList(key);
					if(keylist.size()>0){
						key = keylist.get(0);
						//根据baseid获取要发送消息的类型
						if(key.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_TEXT)){//这边是返回文本消息
							respMessage = sendTextMessageForRespMessage(fromUserName, toUserName, key.getBaseId(), wxMsgTextDao);
						}else if(key.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_NEWS)){//这边是返回图文消息
							respMessage =sendNewsMessageForRespMessage(fromUserName, toUserName, key.getBaseId(), wxMsgNewsDao,wxArticleDao);
						}
					}
					saveFans(fromUserName);//判断粉丝表是否存在该粉丝，如果不存在则直接新增，如果存在不做处理
					//保存内容到粉丝发送消息表
					WxFansText fansText = new WxFansText();
					fansText.setContent(content);
					fansText.setType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
					fansText.setOpenId(fromUserName);
					wxFansTextService.save(fansText);
					log.debug("这个的内容是什么>>>>>"+content);
				}
				
				
			}
			// 【微信触发类型】图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 【微信触发类型】链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 【微信触发类型】音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 【微信触发类型】事件推送
		 /*{	else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT))
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					NewsMessageResp newsResp = new NewsMessageResp();
					newsResp.setCreateTime(System.currentTimeMillis());
					newsResp.setFromUserName(toUserName);
					newsResp.setToUserName(fromUserName);
					newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					//获取首次订阅回复的信息，
					List<WxMsgFirst> firstList = wxMsgFirstDao.findAllList(new WxMsgFirst());
					if(firstList.size()>0){
						WxMsgFirst first = firstList.get(0);
						//判断首次回复发送的类型是文本还是图文，并发送对应的首次回复内容到关注的微信公众号
						if(first.getMsgType().equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
							respMessage = sendTextMessageForRespMessage(fromUserName, toUserName, first.getBaseId(), wxMsgTextDao);
							ShopParameterTab shopParameterTab = shopParameterTabDao.getByCode("wx_fans_count");
							if(null != shopParameterTab && StringUtils.isNotEmpty(shopParameterTab.getId()) && StringUtils.isNotEmpty(shopParameterTab.getValue().trim())
									&& shopParameterTab.getValue().matches("^[0-9]*$")){
								shopParameterTab.setValue((Integer.parseInt(shopParameterTab.getValue())+1)+"");
								shopParameterTabDao.update(shopParameterTab);
								log.debug("关注：粉丝数量增加修改成功.");
								respMessage = respMessage.replace("fans_count", shopParameterTab.getValue());
							}else{
								log.debug("关注：粉丝数量增加修改失败，找不到粉丝数量参数或粉丝数量参数的值不是数字.");
							}

						}else if(first.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_NEWS)){
							respMessage = sendNewsMessageForRespMessage(fromUserName, toUserName, first.getBaseId(), wxMsgNewsDao, wxArticleDao);
						}
					}
					// TODO: 关注，订阅时将推荐人memberId 与 openId 存入映射表
					EventKey = requestMap.get("EventKey");
					String openId = requestMap.get("FromUserName");
					ShopMember sm = shopMemberDao.findByOpenId(openId);
					String memberId = "";
					if(null == sm && StringUtils.isNoneBlank(EventKey)){//参照 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1443433542
						 memberId = EventKey.split("_")[1];//scene_str 字符串类型，长度限制为1到64
						if (StringUtils.isNotEmpty(memberId)) {
							ShopMemberidOpenidMapping shopMemberidOpenidMapping = shopMemberidOpenidMappingDao.findByOpenId(openId);
							if(null == shopMemberidOpenidMapping){
								ShopMemberidOpenidMapping smom = new ShopMemberidOpenidMapping();
								smom.setOpenId(openId);
								smom.setMemberId(memberId);
								smom.preInsert();
								shopMemberidOpenidMappingDao.insert(smom);
							}
						}
					}
					updataShopMember(fromUserName,"1");
					log.info("被推荐人扫码 微信公众号发送信息通知推荐人开始。 ");
					if(StringUtils.isNotEmpty(memberId)){
						log.info("推荐人的memberId为： "+memberId);
						ShopMember recommended = shopMemberDao.get(memberId);
						//被推荐人
						WxAccountFans fans = new WxAccountFans();
						AccessToken accountToken = TokenThread.accessToken;
						fans = WeixinUtil.syncAccountFans(openId.toString(), accountToken);
						log.info("被推荐人昵称："+fans.getNickNameStr());
						if(null !=recommended && StringUtils.isNotEmpty(recommended.getOpenId()) && null != fans){
							WeiXinTemplateUtils.notifyRecommendforFrom(fans.getNickNameStr(),recommended.getName(), recommended.getOpenId());
							log.info("被推荐人扫码 微信公众号发送信息通知推荐人结束，openId："+openId);
						}
					}
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					ShopParameterTab shopParameterTab = shopParameterTabDao.getByCode("wx_fans_count");
					if(null != shopParameterTab && StringUtils.isNotEmpty(shopParameterTab.getId()) && StringUtils.isNotEmpty(shopParameterTab.getValue().trim())
							&& shopParameterTab.getValue().matches("^[0-9]*$")){
						shopParameterTab.setValue((Integer.parseInt(shopParameterTab.getValue())-1)+"");
						shopParameterTabDao.update(shopParameterTab);
						log.debug("取消关注：粉丝数量减少修改成功.");
					}else{
						log.debug("取消关注：粉丝数量减少修改失败，找不到粉丝数量参数或粉丝数量参数的值不是数字.");
					}
					updataShopMember(fromUserName,"0");
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					//创建自定义菜单的时候就以“_fix_”+wxmsgnews的ID进行组合，故直接先去除“_fix_”获取baseid
					String baseId = EventKey.replace("_fix_", "");
					if(StringUtils.isNoneBlank(baseId)){
						WxMsgBase base = baseDao.get(baseId);
						//根据baseid获取要发送消息的类型
						if(base.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_TEXT)){//这边是返回文本消息
							respMessage = sendTextMessageForRespMessage(fromUserName, toUserName, baseId, wxMsgTextDao);
						}else if(base.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_NEWS)){//这边是返回图文消息
							respMessage =sendNewsMessageForRespMessage(fromUserName, toUserName, baseId, wxMsgNewsDao,wxArticleDao);
						}
					}else{
						// 默认回复此文本消息
						TextMessageResp textMessage = new TextMessageResp();
						textMessage.setToUserName(fromUserName);
						textMessage.setFromUserName(toUserName);
						textMessage.setCreateTime(new Date().getTime());
						textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

						textMessage.setContent("<a href='"+Global.getBasePath()+"?openId="+fromUserName+"'>点击此处进行绑定</a>");
						respMessage =  MessageUtil.textMessageToXml(textMessage);
					}
//					respMessage = doMyMenuEvent(requestMap, textMessage, bundler, respMessage, toUserName, fromUserName, respContent, request);
				}


			}*/
			log.debug("respContent>>" + respContent);
		} catch (Exception e) {
			log.error("处理微信返回信息出错：" + e.getMessage());
			e.printStackTrace();
		}
		if(respMessage==null){
			respMessage = "success";
		}
		return respMessage;
	}

	public static String sendTextMessageForRespMessage(String fromUserName,String toUserName,String baseId ,WxMsgTextDao wxMsgTextDao){
		// 默认回复此文本消息
		TextMessageResp textMessage = new TextMessageResp();
					
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(System.currentTimeMillis());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		WxMsgText msgtext = new WxMsgText();
		msgtext.setBaseId(baseId);
		List<WxMsgText> msgtextList = wxMsgTextDao.findAllList(msgtext);
		if(msgtextList.size()>0){
			msgtext = msgtextList.get(0);
			textMessage.setContent(msgtext.getContent());
			return  MessageUtil.textMessageToXml(textMessage);
		}
		return null;		
	}
	
	public static String sendNewsMessageForRespMessage(String fromUserName,String toUserName,String baseId,WxMsgNewsDao wxMsgNewsDao,WxArticleDao wxArticleDao){
		NewsMessageResp newsResp = new NewsMessageResp();
		newsResp.setCreateTime(System.currentTimeMillis());
		newsResp.setFromUserName(toUserName);
		newsResp.setToUserName(fromUserName);
		newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		WxMsgNews msgNews = new WxMsgNews();
		msgNews.setBaseId(baseId);
		List<WxMsgNews> newsList = wxMsgNewsDao.findAllList(msgNews);
		if(newsList.size()>0){
			WxArticle art = new WxArticle();
			art.setNewsId(newsList.get(0).getId());
			List<WxArticle> artList = wxArticleDao.findAllList(art);
			List<Article> articleList = new ArrayList<Article>();
			for (WxArticle wxArticle : artList) {
				 Article article = new Article();
				 article.setTitle(wxArticle.getTitle());
				 article.setDescription(StringEscapeUtils.unescapeHtml4(wxArticle.getContent()));
				 article.setUrl(wxArticle.getUrl());
				 article.setPicUrl(wxArticle.getPicUrl());
				 articleList.add(article);  
			}
			  // 设置图文消息个数  
            newsResp.setArticleCount(articleList.size());  
            // 设置图文消息包含的图文集合  
            newsResp.setArticles(articleList);  
            // 将图文消息对象转换成xml字符串  
            return MessageUtil.newsMessageToXml(newsResp);  
		}
        return null;
	}
	
	/**
	 * Q译通使用指南
	 * 
	 * @return
	 */
	public static String getTranslateUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("微译使用指南").append("\n\n");
		buffer.append("微译为用户提供专业的多语言翻译服务，目前支持以下翻译方向：").append("\n");
		buffer.append("    中 -> 英").append("\n");
		buffer.append("    英 -> 中").append("\n");
		buffer.append("    日 -> 中").append("\n\n");
		buffer.append("使用示例：").append("\n");
		buffer.append("    翻译我是中国人").append("\n");
		buffer.append("    翻译dream").append("\n");
		buffer.append("    翻译さようなら").append("\n\n");
		buffer.append("回复“?”显示主菜单");
		return buffer.toString();
	}

	
	
	// 获取微信公众号带参的二维码
	public String getWxQrUrl(String memeberId){
		String wxqrContent = "";
		String tockenStr = TokenThread.accessToken.getToken();
		String ticketUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+tockenStr;
		
		JSONObject ticketId = WeixinUtil.httpRequest(ticketUrl, "POST", "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+memeberId+"\"}}}");
		
		if(null != ticketId){
			String ticket = ticketId.getString("ticket");
			wxqrContent = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
		}
		
		return wxqrContent;
	}
	
	/**
	 * 授权获取用户信息
	 * @param code
	 * @return
	 */
	public WxUser getOauthWxUser(String code){
		WxUser wxUser = new WxUser();
		
		String accessToken = "";
		String openid = "";
		String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ WeixinConfig.app_id+"&secret="+WeixinConfig.app_secret+"&code="+code+"&grant_type=authorization_code";
		
		JSONObject jsonAccessToken = WeixinUtil.httpRequest(accessTokenUrl, "GET", null);
		
		// 如果请求成功
		if (null != jsonAccessToken) {
			accessToken = jsonAccessToken.getString("access_token");
			openid = jsonAccessToken.getString("openid");
			
			String openidUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openid+"&lang=zh_CN";
			JSONObject jsonOpenId = WeixinUtil.httpRequest(openidUrl, "GET", null);
			if(null != jsonOpenId){
				openid = jsonOpenId.getString("openid");
				String nickname = jsonOpenId.getString("nickname");
				String sex = jsonOpenId.getString("sex");
				String province = jsonOpenId.getString("province");
				String city = jsonOpenId.getString("city");
				String country = jsonOpenId.getString("country");
				String headimgurl = jsonOpenId.getString("headimgurl");
				
				wxUser.setOpenid(openid);
				wxUser.setNickname(nickname);
				wxUser.setSex(sex);
				wxUser.setProvince(province);
				wxUser.setCity(city);
				wxUser.setCountry(country);
				wxUser.setHeadimgurl(headimgurl);
				
				return wxUser;
			}
		}
		
		return null;
		
		
	}
	//发布菜单
		public JSONObject publishMenu(AccessToken accessToken){
			//获取数据库菜单
			List<WxMenu> menus = wxMenuDao.findList(new WxMenu());
			Matchrule matchrule = new Matchrule();
			String menuJson =JSONObject.toJSONString(WeixinUtil.prepareMenus(menus,matchrule));
			log.info("创建菜单传参如下:"+menuJson);
			JSONObject rstObj = WeixinUtil.publishMenus(menuJson,accessToken);//创建普通菜单
			log.info("创建菜单返回消息如下:"+rstObj.toString());
			//以下为创建个性化菜单demo，只为男创建菜单；其他个性化需求 设置 Matchrule 属性即可
//			matchrule.setSex("1");//1-男 ；2-女
//			JSONObject rstObj = WxApiClient.publishAddconditionalMenus(menuJson,mpAccount);//创建个性化菜单
			
//			if(rstObj != null){//成功，更新菜单组
//				if(rstObj.containsKey("menu_id")){
//					menuGroupDao.updateMenuGroupDisable();
//					menuGroupDao.updateMenuGroupEnable(gid);
//				}else if(rstObj.containsKey("errcode") && rstObj.getInt("errcode") == 0){
//					menuGroupDao.updateMenuGroupDisable();
//					menuGroupDao.updateMenuGroupEnable(gid);
//				}
//			}
			return rstObj;
		}
		
		//删除菜单
		public JSONObject deleteMenu(AccessToken accessToken){
			JSONObject rstObj = WeixinUtil.deleteMenu(accessToken);
			return rstObj;
		}

		public boolean syncAccountFansList(AccessToken accountToken) {
			String nextOpenId = null;
			wxAccountFansDao.deleteAll(new WxAccountFans());
			return doSyncAccountFansList(nextOpenId,accountToken);
		}

		//同步粉丝列表(开发者在这里可以使用递归处理)
		private boolean doSyncAccountFansList(String nextOpenId,AccessToken accountToken){
	 		String url = WxApi.getFansListUrl(accountToken.getToken(), nextOpenId);
			log.info("同步粉丝入参消息如下:"+url);
			JSONObject jsonObject = WxApi.httpsRequest(url, HttpMethod.POST, null);
			log.info("同步粉丝返回消息如下:"+jsonObject.toString());
			if(jsonObject.containsKey("errcode")){
				return false;
			}
			List<WxAccountFans> fansList = new ArrayList<WxAccountFans>();
			if(jsonObject.containsKey("data")){
				if(jsonObject.getJSONObject("data").containsKey("openid")){
					
					JSONArray openidArr = jsonObject.getJSONObject("data").getJSONArray("openid");
					int length = openidArr.size();
					for(int i = 0; i < length ;i++){
						Object openId = openidArr.get(i);
						WxAccountFans fans = WeixinUtil.syncAccountFans(openId.toString(), accountToken);
						//设置公众号
						fans.setAccount(WeixinConfig.mch_id);
						if(fans != null && fans.getNickName() != null && fans.getNickName().length >0){
							try {
								fans.setNickNameStr( new String(fans.getNickName(),"UTF-8"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						fans.preInsert();
						fansList.add(fans);
//						wxAccountFansDao.insert(fans);
					}
//					//批处理
					wxAccountFansDao.saveList(fansList);
				}
			}
			return true;
		}
		
		/**
		 * 根据openid群发文本消息
		 * @param openids
		 * @param content
		 * @param mpAccount
		 * @return
		 */
		public static JSONObject massSendTextByOpenIds(List<String> openids,String content, AccessToken accessToken){
			if(openids != null && openids.size() > 0){
				if(openids.size() == 1){//根据openId群发，size至少为2
					openids.add("1");
				}
				String[] arr = (String[])openids.toArray(new String[openids.size()]);
				JSONObject postObj = new JSONObject();
				JSONObject text = new JSONObject();
				postObj.put("touser", arr);
				text.put("content", content);
				postObj.put("text", text);
				postObj.put("msgtype", MsgType.Text.toString());
				String token = accessToken.getToken();
				return WxApi.httpsRequest(WxApi.getMassSendUrl(token), HttpMethod.POST, postObj.toString());
			}
			return null;
		}
		
		/**
		 * 创建带参数的二维码，如果expireSeconds不为空，则生成的是临时二维码
		 *    参照官方说明：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1443433542
		 * @param token
		 * @param mobile
		 * @param expireSeconds
		 * @return
		 */
		@SuppressWarnings("deprecation")
		public static String createErweima(String token,String mobile,String expireSeconds){
			JSONObject postObj = new JSONObject();
			JSONObject text = new JSONObject();
			JSONObject text1 = new JSONObject();
			if(StringUtils.isNotEmpty(expireSeconds)){//不为空，则增加expire_seconds，表示生成的是临时二维码
				postObj.put("expire_seconds", 2592000);//30天
			}
			postObj.put("action_name", "QR_LIMIT_STR_SCENE");
//			text1.put("scene_id", 96588);
			text1.put("scene_str", mobile);
			text.put("scene", text1);
			postObj.put("action_info", text);
			log.debug("创建带参数的二维码 info: "+postObj.toString());
			JSONObject result = WxApi.httpsRequest(WxApi.getCreateQrcodeUrl(token), HttpMethod.POST, postObj.toString());
			log.debug("result>>>" + result.toString());
			String ticket = URLEncoder.encode(result.getString("ticket"));
			String url = WxApi.getShowQrcodeUrl(ticket);
			log.debug("创建带参数的二维码 URL：: "+ url);
			return url;
		}
		/**
		 * 发送模板消息
		 * @param tplMsg
		 * @param mpAccount 消息内容
		 * @return
		 */
		public static JSONObject sendTemplateMessage(TemplateMessage tplMsg, AccessToken accessToken){
			if(tplMsg != null){
				String token = accessToken.getToken();
				return WxApi.httpsRequest(WxApi.getSendTemplateMessageUrl(token), HttpMethod.POST, tplMsg.toString());
			}
			return null;
		}
		
		/**
		 *  往指定的各个openids发送通知消息
		 * @param openIds
		 * @param templateId
		 * @param url
		 * @param dataMap
		 * @return
		 */
		public static Map<String,Object> sendTemplateMessage(String[] openIds,String templateId,String url,Map<String,String> dataMap){
			TemplateMessage tplMsg = new TemplateMessage();
			Map<String,Object> map = new HashMap<String, Object>();
			for (String openId : openIds) {
				tplMsg.setOpenid(openId);
				//微信公众号号的template id，开发者自行处理参数
				tplMsg.setTemplateId(templateId);
				tplMsg.setUrl(url);
//				dataMap.put("first", "多公众号管理开源平台");
//				dataMap.put("keyword1", "时间：" + DateUtils.formatDateTime(new Date()));
//				dataMap.put("keyword2", "码云平台地址：https://gitee.com/qingfengtaizi/wxmp");
//				dataMap.put("keyword3", "github平台地址：https://github.com/qingfengtaizi/wxmp-web");
//				dataMap.put("remark", "我们期待您的加入");
				tplMsg.setDataMap(dataMap);
				JSONObject result = sendTemplateMessage(tplMsg, TokenThread.accessToken);
				log.debug("微信接口返回信息：code："+result.getString("errcode")+">>msg:"+result.getString("errmsg"));
				map.put(openId, result);
			}
			return map;
		}
		
		
		/**
		 * 同步微信模板消息
		 * @param accountToken
		 * @return
		 */
		public boolean syncWxTplMsgTextList(AccessToken accountToken) {
			String url = WxApi.getTemplateMessageListUrl(accountToken.getToken());
			log.info("同步消息模板入参消息如下:"+url);
			JSONObject jsonObject = WxApi.httpsRequest(url, HttpMethod.POST, null);
			log.info("同步消息返回消息如下:"+jsonObject.toString());
			if(jsonObject.containsKey("errcode")){
				return false;
			}
			List<WxTplMsgText> tplList = new ArrayList<WxTplMsgText>();
			if(jsonObject.containsKey("template_list")){
				wxTplMsgTextDao.deleteAll(new WxTplMsgText());
				baseDao.deleteForTplMsgText(new WxMsgBase());
					JSONArray jsonArr  = jsonObject.getJSONArray("template_list");
					System.out.println("集合的数量：" + 0);
					int length = jsonArr.size();
					for(int i = 0; i < length ;i++){
						WxMsgBase base = null;
						WxTplMsgText tpl = new WxTplMsgText();
						JSONObject json = (JSONObject) jsonArr.get(i);
						tpl.setContent(json.getString("content"));
						tpl.setTplId(json.getString("template_id"));
						tpl.setTitle(json.getString("title"));
						base = new WxMsgBase();
						base.setMsgType(MsgType.Text.toString());
						base.preInsert();
						baseDao.insert(base);
						tpl.setBaseId(base.getId());
						tpl.preInsert();
						tplList.add(tpl);
					}
					//批处理
					wxTplMsgTextDao.saveList(tplList);
			}
			return true;
		}

		/**
		 * 客服接口-发送图文消息
		 * @param openid
		 * @param msgNews
		 * @param mpAccount
		 * @return
		 */
		public JSONObject sendCustomNews(String openid, WxMsgNews msgNews,
				AccessToken mpAccount) {
			String content = "";
			if(!StringUtils.isBlank(openid)){
				String accessToken = mpAccount.getToken();
				content = WxMessageBuilder.prepareCustomNews(openid, msgNews);
				return WxApi.httpsRequest(WxApi.getSendCustomMessageUrl(accessToken), HttpMethod.POST, content);
			}
			return null;
		}
		
		
		/**
		 * 根据open_id判断粉丝表是否存在该用户，如果不存在则新增该用户的粉丝信息
		 * @param openId
		 */
		public void saveFans(String openId){
			WxAccountFans fans = new WxAccountFans();
			fans.setOpenId(openId);
			List<WxAccountFans> fansList = wxAccountFansDao.findList(fans);
			if(fansList.size()==0){
				AccessToken accountToken = TokenThread.accessToken;
				fans = WeixinUtil.syncAccountFans(openId.toString(), accountToken);
				//设置公众号
				fans.setAccount(WeixinConfig.mch_id);
				if(fans != null && fans.getNickName() != null && fans.getNickName().length >0){
					try {
						fans.setNickNameStr( new String(fans.getNickName(),"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				fans.preInsert();
				wxAccountFansDao.insert(fans);
			}
		}
		
		/*public void updataShopMember(String openId,String fansStatus){
			ShopMember member = shopMemberDao.findByOpenId(openId);
			if(member != null){//存在则进行判断是否更新
				if(!member.getWxSubscribeStatus().equals(fansStatus)){
					member.setWxSubscribeStatus(fansStatus);
					member.preUpdate();
					shopMemberDao.update(member);
				}
			}
		}*/
		
		/**
		 * 客服接口-发送消息
		 * @param openid
		 * @param msgNews
		 * @param mpAccount
		 * @return
		 */
		public JSONObject sendCustomText(String openid, String sendContent,
				AccessToken mpAccount) {
			String content = "";
			if(!StringUtils.isBlank(openid)){
				String accessToken = mpAccount.getToken();
				JSONObject jsObj = new JSONObject();
				jsObj.put("touser", openid);
				jsObj.put("msgtype", MsgType.Text.toString().toLowerCase());
				JSONObject text = new JSONObject();
				text.put("content", sendContent);
				jsObj.put("text", text);
				content = jsObj.toString();
				return WxApi.httpsRequest(WxApi.getSendCustomMessageUrl(accessToken), HttpMethod.POST, content);
			}
			return null;
		}
}