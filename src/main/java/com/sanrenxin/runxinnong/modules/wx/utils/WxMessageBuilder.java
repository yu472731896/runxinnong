/**
 * Copyright &copy; 2017-2018 <a href="http://www.webcsn.com">webcsn</a> All rights reserved.
 *
 * @author hermit
 * @date 2018-04-17 10:54:58
 */
package com.sanrenxin.runxinnong.modules.wx.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;
import com.sanrenxin.runxinnong.modules.wx.vo.Article;
import com.sanrenxin.runxinnong.modules.wx.vo.MsgRequest;
import com.sanrenxin.runxinnong.modules.wx.vo.MsgResponseNews;
import com.sanrenxin.runxinnong.modules.wx.vo.MsgResponseText;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 消息builder工具类
 */
public class WxMessageBuilder {
	
	//客服文本消息
	public static String prepareCustomText(String openid,String content){
		JSONObject jsObj = new JSONObject();
		jsObj.put("touser", openid);
		jsObj.put("msgtype", MsgType.Text.name());
		JSONObject textObj = new JSONObject();
		textObj.put("content", content);
		jsObj.put("text", textObj);
		return jsObj.toString();
	}
	
	
	//获取 MsgResponseText 对象
	public static MsgResponseText getMsgResponseText(MsgRequest msgRequest, WxMsgText msgText){
		if(msgText != null){
			MsgResponseText reponseText = new MsgResponseText();
			reponseText.setToUserName(msgRequest.getFromUserName());
			reponseText.setFromUserName(msgRequest.getToUserName());
			reponseText.setMsgType(MsgType.Text.toString());
			reponseText.setCreateTime(System.currentTimeMillis());
			reponseText.setContent(msgText.getContent());
			return reponseText;
		}else{
			return null;
		}
	}
	
	//获取 MsgResponseNews 对象
	public static MsgResponseNews getMsgResponseNews(MsgRequest msgRequest, List<WxMsgNews> msgNews){
		if(msgNews != null && msgNews.size() > 0){
			MsgResponseNews responseNews = new MsgResponseNews();
			responseNews.setToUserName(msgRequest.getFromUserName());
			responseNews.setFromUserName(msgRequest.getToUserName());
			responseNews.setMsgType(MsgType.News.toString());
			responseNews.setCreateTime(System.currentTimeMillis());
			responseNews.setArticleCount(msgNews.size());
			List<Article> articles = new ArrayList<Article>(msgNews.size());
			for(WxMsgNews n : msgNews){
				Article a = new Article();
				a.setTitle(n.getTitle());
				a.setPicUrl(n.getPicPath());
				if(StringUtils.isEmpty(n.getFromUrl())){
					a.setUrl(n.getUrl());
				}else{
					a.setUrl(n.getFromUrl());
				}
				a.setDescription(n.getBrief());
				articles.add(a);
			}
			responseNews.setArticles(articles);
			return responseNews;
		}else{
			return null;
		}
	}
	
	//客服图文消息
	public static String prepareCustomNews(String openid,WxMsgNews msgNews){
		JSONObject jsObj = new JSONObject();
//		List<MsgArticle> arts = msgNews.getArticles();
//		jsObj.put("touser", openid);
//		jsObj.put("msgtype", MsgType.News.toString().toLowerCase());
//		JSONObject articles = new JSONObject();
//		JSONArray articleArray = new JSONArray();
//		//支持多图文
//		arts.forEach((a)->{
//			JSONObject newsObj = new JSONObject();
//			newsObj.put("title", a.getTitle());
//			newsObj.put("description", a.getDigest());
//			newsObj.put("url", a.getUrl());
//			newsObj.put("picurl", a.getPicUrl());
//			articleArray.add(newsObj);
//		});
//		articles.put("articles", articleArray);
//		jsObj.put("news", articles);
//		第二种方式
		jsObj.put("touser", openid);
		jsObj.put("msgtype", MsgType.MPNEWS.toString().toLowerCase());
		JSONObject media = new JSONObject();
		media.put("media_id", msgNews.getMediaId());
		jsObj.put("mpnews", media);
		
		return jsObj.toString();
	}
	public static void main(String[] args) {
		JSONObject jsObj = new JSONObject();
		jsObj.put("touser", 1);
		jsObj.put("msgtype", 2);
		JSONObject articles = new JSONObject();
		JSONArray articleArray = new JSONArray();
		JSONObject newsObj = new JSONObject();
		newsObj.put("title", 3);
		newsObj.put("description", 4);
		articleArray.add(newsObj);
		articles.put("articles", articleArray);
		jsObj.put("news", articles);
		System.out.println(jsObj.toString());
	}
}
