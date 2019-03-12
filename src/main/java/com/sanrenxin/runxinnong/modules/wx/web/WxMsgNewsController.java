package com.sanrenxin.runxinnong.modules.wx.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.common.web.FrontController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxArticle;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMediaFiles;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.service.WxArticleService;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgNewsService;
import com.sanrenxin.runxinnong.modules.wx.utils.AjaxResult;
import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import com.sanrenxin.runxinnong.modules.wx.utils.WeixinUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信图文消息Controller
 * @author wjx
 * @version 1527577385
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMsgNews")
public class WxMsgNewsController extends FrontController {

	@Autowired
	private WxMsgNewsService wxMsgNewsService;
	@Autowired
	private WxArticleService wxArticleService;
	
	@ModelAttribute
	public WxMsgNews get(@RequestParam(required=false) String id) {
		WxMsgNews entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMsgNewsService.get(id);
		}
		if (entity == null){
			entity = new WxMsgNews();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMsgNews:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxMsgNews wxMsgNews, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMsgNews> page = wxMsgNewsService.findPage(new Page<WxMsgNews>(request, response), wxMsgNews);
		model.addAttribute("page", page);
		model.addAttribute("multType", wxMsgNews.getMultType());
		return "modules/wx/wxMsgNewsList";
	}

	@RequiresPermissions("wx:wxMsgNews:view")
	@RequestMapping(value = "form")
	public String form(WxMsgNews wxMsgNews, Model model) {
		model.addAttribute("wxMsgNews", wxMsgNews);
		return "modules/wx/wxMsgNewsForm";
	}
	
	@RequiresPermissions("wx:wxMsgNews:view")
	@RequestMapping(value = "formnew")
	public String formnew(WxMsgNews wxMsgNews, Model model) {
		String forward = "modules/wx/document/addsingle";
		if(StringUtils.isNotBlank(wxMsgNews.getMultType())&&wxMsgNews.getMultType().equals("2")){
			forward = "modules/wx/document/addmultiple";
		}
		if(StringUtils.isNotBlank(wxMsgNews.getId())){
			forward = "modules/wx/document/editsingle";
			if(StringUtils.isNotBlank(wxMsgNews.getMultType())&&wxMsgNews.getMultType().equals("2")){
				WxArticle art = new WxArticle();
				art.setNewsId(wxMsgNews.getId());
				List<WxArticle> artList = wxArticleService.findList(art);
				wxMsgNews.setArticles(artList);
				forward = "modules/wx/document/editmultiple";
			}
		}
		model.addAttribute("wxMsgNews", wxMsgNews);
		return forward;
	}
	
	@RequestMapping(value = "/toUpdateMultiple", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult toUpdateMultiple(WxMsgNews wxMsgNews, Model model) {
		if(StringUtils.isNotBlank(wxMsgNews.getId())){
				WxArticle art = new WxArticle();
				art.setNewsId(wxMsgNews.getId());
				List<WxArticle> artList = wxArticleService.findList(art);
				wxMsgNews.setArticles(artList);
		}
		return AjaxResult.success(wxMsgNews.getArticles());
	}
	
	@RequiresPermissions("wx:wxMsgNews:edit")
	@RequestMapping(value = "save")
	public String save(WxMsgNews wxMsgNews, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxMsgNews)){
			return form(wxMsgNews, model);
		}
		wxMsgNewsService.save(wxMsgNews);
		addMessage(redirectAttributes, "保存微信图文消息成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxMsgNews/list?repage";
	}
	
	@RequiresPermissions("wx:wxMsgNews:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMsgNews wxMsgNews, RedirectAttributes redirectAttributes) {
		wxMsgNewsService.delete(wxMsgNews);
		addMessage(redirectAttributes, "删除微信图文消息成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMsgNews/list?multType="+wxMsgNews.getMultType()+"&repage";
	}
	
	
	/**
	 * 添加单图文
	 * 
	 * @param msgNews
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addSingleNews", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addSingleNews(WxMsgNews msgNews, HttpServletRequest request) throws Exception{

		String filePath = request.getSession().getServletContext().getRealPath("/");

		String description = msgNews.getDescription();
		String description2 = msgNews.getDescription();
		
		description = description.replaceAll("'","\"");
		//去多个img的src值
		String subFilePath = "";
		String subOldFilePath = "";
		if (description.contains("img")) {
			Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
			Matcher m = p.matcher(description);

			while (m.find()) {
				String imgSrc = m.group(1);
				subOldFilePath +=  imgSrc + ",";
				String[] split = imgSrc.split("/");
				int k=imgSrc.indexOf(split[split.length-2]);
				String subImgSrc = imgSrc.substring(k, imgSrc.length());
				subFilePath += filePath + subImgSrc + ",";
			}
		}
		AccessToken mpAccount = TokenThread.accessToken;// 获取缓存中的唯一账号
		if(StringUtils.isNotBlank(subFilePath)){
			
			subFilePath = subFilePath.substring(0, subFilePath.length() -1);
			subOldFilePath = subOldFilePath.substring(0, subOldFilePath.length() -1);
			
			//本地图片地址
			String[] imgPathArry = subFilePath.split(",");
			String[] imgOldPathArry = subOldFilePath.split(",");
			
			String[] newPathArry = new String[imgPathArry.length];
			for(int i=0;i<imgPathArry.length;i++){
				String newFilePath = imgPathArry[i];
				// 添加永久图片
//				String materialType = MediaType.Image.toString();
				// 将图片上传到微信，返回url
				JSONObject imgResultObj = WeixinUtil.uploadMaterialImg(newFilePath, mpAccount);
				
				// 上传图片的id
//	   		String contentImgMediaId = "";
				String contentContentUrl = "";
				if (imgResultObj != null && imgResultObj.containsKey("url")) {
					// 微信返回来的媒体素材id
//	   			contentImgMediaId = imgResultObj.getString("media_id");
					// 图片url
					contentContentUrl = imgResultObj.getString("url");
				} 
				newPathArry[i] = contentContentUrl;
			}
			
			for(int i=0;i<imgPathArry.length;i++){
				description =  description.replace(imgOldPathArry[i], newPathArry[i]);
			}
		}
		
		//内容保存
		msgNews.setDescription(description);
		msgNews.setDescription(StringEscapeUtils.unescapeHtml4(msgNews.getDescription()));
		List<WxMsgNews> msgNewsList = new ArrayList<WxMsgNews>();
		msgNewsList.add(msgNews);
		// 封面图片媒体id
		String imgMediaId = msgNews.getThumbMediaId();

		JSONObject resultObj = WeixinUtil.addNewsMaterial(msgNewsList, imgMediaId, mpAccount);
		if (resultObj != null && resultObj.containsKey("media_id")) {
			String newsMediaId = resultObj.getString("media_id");
			JSONObject newsResult = WeixinUtil.getMaterial(newsMediaId, mpAccount);

			JSONArray articles = newsResult.getJSONArray("news_item");
			JSONObject article = (JSONObject) articles.get(0);
			WxMsgNews newsPo = new WxMsgNews();
			newsPo.setMultType("1");//指定为1，代表单图文
			newsPo.setTitle(article.getString("title"));
			newsPo.setAuthor(article.getString("author"));
			newsPo.setBrief(article.getString("digest"));
			newsPo.setDescription(description2);
			newsPo.setFromUrl(article.getString("content_source_url"));
			newsPo.setUrl(article.getString("url"));
			newsPo.setShowPic(article.getString("show_cover_pic"));
			newsPo.setPicPath(msgNews.getPicPath());
			newsPo.setMediaId(newsMediaId);
			newsPo.setNeedOpenComment(msgNews.getNeedOpenComment());
			newsPo.setOnlyFansCanComment(msgNews.getOnlyFansCanComment());
			newsPo.setThumbMediaId(imgMediaId);
			newsPo.setNewsIndex("0");
			WxMediaFiles entity = new WxMediaFiles();
			entity.setMediaId(newsMediaId);
			entity.setMediaType("news");
			int resultCount = this.wxMsgNewsService.addSingleNews(newsPo, entity);

			if (resultCount > 0) {
				return AjaxResult.success();
			} else {
				return AjaxResult.failure();
			}
		}
		return AjaxResult.failure();

	}

	/**
	 * 更新单图文素材
	 *
	 * @param msgNews
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateSingleNews", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult updateSingleNews(WxMsgNews msgNews, HttpServletRequest request) {
		String filePath = request.getSession().getServletContext().getRealPath("/");

		String description = msgNews.getDescription();
		String description2 = msgNews.getDescription();
		description = description.replaceAll("'","\"");
		//去多个img的src值
		String subFilePath = "";
		String subOldFilePath = "";
		if (description.contains("img")) {
			Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
			Matcher m = p.matcher(description);

			while (m.find()) {
				String imgSrc = m.group(1);
				subOldFilePath +=  imgSrc + ",";
				String[] split = imgSrc.split("/");
				int k=imgSrc.indexOf(split[split.length-2]);
				String subImgSrc = imgSrc.substring(k, imgSrc.length());
				subFilePath += filePath + subImgSrc + ",";
			}
		}
		if(StringUtils.isNotBlank(subFilePath)){
			subFilePath = subFilePath.substring(0, subFilePath.length() -1);
			subOldFilePath = subOldFilePath.substring(0, subOldFilePath.length() -1);
			//本地图片地址
			String[] imgPathArry = subFilePath.split(",");
			String[] imgOldPathArry = subOldFilePath.split(",");

			String[] newPathArry = new String[imgPathArry.length];
			for(int i=0;i<imgPathArry.length;i++){
				String newFilePath = imgPathArry[i];
				// 添加永久图片
//				String materialType = MediaType.Image.toString();
				// 将图片上传到微信，返回url
				JSONObject imgResultObj = WeixinUtil.uploadMaterialImg(newFilePath, TokenThread.accessToken);

				// 上传图片的id
//	   		String contentImgMediaId = "";
				String contentContentUrl = "";
				if (imgResultObj != null && imgResultObj.containsKey("url")) {
					// 微信返回来的媒体素材id
//	   			contentImgMediaId = imgResultObj.getString("media_id");
					// 图片url
					contentContentUrl = imgResultObj.getString("url");
				}
				newPathArry[i] = contentContentUrl;
			}

			for(int i=0;i<imgPathArry.length;i++){
				description =  description.replace(imgOldPathArry[i], newPathArry[i]);
			}
		}

		//内容保存
		msgNews.setDescription(description);
		msgNews.setDescription(StringEscapeUtils.unescapeHtml4(msgNews.getDescription()));
		List<WxMsgNews> msgNewsList = new ArrayList<WxMsgNews>();

		msgNewsList.add(msgNews);

		String mediaId = msgNews.getMediaId();

		AccessToken mpAccount = TokenThread.accessToken;// 获取缓存中的唯一账号

		JSONObject resultObj = WeixinUtil.updateNewsMaterial(msgNewsList, 0, mediaId, mpAccount);

		if (null != resultObj && resultObj.containsKey("errcode") && resultObj.getInteger("errcode") == 0) {
			msgNews.setDescription(description2);
			try {
				// 更新成功
				this.wxMsgNewsService.updateSingleNews(msgNews);
				return AjaxResult.updateSuccess();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return AjaxResult.failure();
	}
	
	
	/**
	 * 添加多图文
	 * 
	 * @param rows
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "/addMoreNews", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addMoreNews(String rows, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		
		String filePath = request.getSession().getServletContext().getRealPath("/");
		
		List<WxArticle> listArts= new ArrayList<WxArticle>();//数据库所有图文集合
		JSONArray arrays=JSONArray.parseArray(rows.replaceAll("&quot;", "\""));
		JSONArray arryarticles=new JSONArray();//微信所用json集合
		WxMsgNews msgNew=new WxMsgNews();
		for (int i = 0; i < arrays.size(); i++) {
			JSONObject obj = arrays.getJSONObject(i);
			JSONObject json=new JSONObject();
			json.put("title", obj.getString("title"));
			json.put("author", obj.getString("author"));
			json.put("thumb_media_id", obj.get("thumbMediaId"));
			json.put("digest", obj.get("brief"));
			json.put("show_cover_pic", obj.getInteger("showpic"));
			json.put("content_source_url", obj.get("fromurl"));
			json.put("need_open_comment", obj.get("need_open_comment"));
			json.put("only_fans_can_comment", obj.get("only_fans_can_comment"));
			WxArticle art=new WxArticle();
			art.setNewsIndex(i+"");
			art.setTitle(obj.getString("title"));
			art.setAuthor(obj.getString("author"));
			art.setContentSourceUrl(obj.getString("fromurl"));
			art.setDigest(obj.getString("brief"));
			art.setPicUrl(obj.getString("picpath"));
			art.setShowCoverPic(obj.getString("showpic"));
			art.setThumbMediaId(obj.getString("thumbMediaId"));
			art.setContent(obj.getString("description"));
			art.setNeedOpenComment(obj.getInteger("need_open_comment"));
			art.setOnlyFansCanComment(obj.getInteger("only_fans_can_comment"));
			if(i==0){
				msgNew.setAuthor(art.getAuthor());
				msgNew.setBrief(art.getDigest());
				msgNew.setDescription(art.getContent());
				msgNew.setFromUrl(art.getContentSourceUrl());
				msgNew.setMultType("2");
				msgNew.setPicPath(art.getPicUrl());
				msgNew.setNewsIndex("0");
				msgNew.setShowPic(art.getShowCoverPic());
				msgNew.setTitle(art.getTitle());
				msgNew.setThumbMediaId(art.getThumbMediaId());
				msgNew.setNeedOpenComment(art.getNeedOpenComment());
				msgNew.setOnlyFansCanComment(art.getOnlyFansCanComment());
			}
			//注意这是图文正文部分
			String description = obj.getString("description");
			description = description.replaceAll("'","\"");
			//去多个img的src值
			String subFilePath = "";
			String subOldFilePath = "";
			if (description.contains("img")) {
				Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
				Matcher m = p.matcher(description);

				while (m.find()) {
					String imgSrc = m.group(1);
					subOldFilePath +=  imgSrc + ",";
					String[] split = imgSrc.split("/");
					int k=imgSrc.indexOf(split[split.length-2]);
					String subImgSrc = imgSrc.substring(k, imgSrc.length());
					subFilePath += filePath + subImgSrc + ",";
				}
			}
			if(StringUtils.isNotBlank(subFilePath)){
				
				subFilePath = subFilePath.substring(0, subFilePath.length() -1);
				subOldFilePath = subOldFilePath.substring(0, subOldFilePath.length() -1);
				
				AccessToken mpAccount = TokenThread.accessToken;// 获取缓存中的唯一账号
				//本地图片地址
				String[] imgPathArry = subFilePath.split(",");
				String[] imgOldPathArry = subOldFilePath.split(",");
				
				String[] newPathArry = new String[imgPathArry.length];
				for(int j=0;j<imgPathArry.length;j++){
					String newFilePath = imgPathArry[j];
					// 添加永久图片
//					String materialType = MediaType.Image.toString();
					// 将图片上传到微信，返回url
					JSONObject imgResultObj = WeixinUtil.uploadMaterialImg(newFilePath, mpAccount);
					
					// 上传图片的id
//		   		String contentImgMediaId = "";
					String contentContentUrl = "";
					if (imgResultObj != null && imgResultObj.containsKey("url")) {
						// 微信返回来的媒体素材id
//		   			contentImgMediaId = imgResultObj.getString("media_id");
						// 图片url
						contentContentUrl = imgResultObj.getString("url");
					} 
					newPathArry[j] = contentContentUrl;
				}
				
				for(int j=0;j<imgPathArry.length;j++){
					description =  description.replace(imgOldPathArry[j], newPathArry[j]);
				}
			}
			description = StringEscapeUtils.unescapeHtml4(description);
		    json.put("content", description);
		    
		    arryarticles.add(json);
		    listArts.add(art);
		}
		
		
		AccessToken mpAccount = TokenThread.accessToken;// 获取缓存中的唯一账号
		// 添加多图文永久素材
		JSONObject resultObj = WeixinUtil.addMoreNewsMaterial2(arryarticles, mpAccount);
		//数据库存储使用
		List<WxArticle> listArticles= new ArrayList<WxArticle>();//数据库所有图文集合
		
		if (resultObj != null && resultObj.containsKey("media_id")) {
			String newsMediaId = resultObj.getString("media_id");
			msgNew.setMediaId(newsMediaId);
			JSONObject newsResult = WeixinUtil.getMaterial(newsMediaId, mpAccount);
			
			JSONArray articles = newsResult.getJSONArray("news_item");
			
			for (int i = 0; i < articles.size(); i++) {
				JSONObject article = (JSONObject) articles.get(i);
				if(i==0){
					msgNew.setUrl(article.getString("url"));
				}
				WxArticle msgart = listArts.get(i);
				msgart.setUrl(article.getString("url"));
				msgart.setMediaId(newsMediaId);
				listArticles.add(msgart);
			}
			msgNew.setArticles(listArticles);
			
			int bl = this.wxMsgNewsService.addMoreNews(msgNew);
			if(bl == 1){
				return AjaxResult.success();
			}
		}
		return AjaxResult.failure();
	}
	
	/**
	 * 更新多图文
	 * 
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateSubMoreNews", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult updateMoreNews(String  rows, HttpServletRequest request) {
		String filePath = request.getSession().getServletContext().getRealPath("/");
		JSONObject obj = JSONObject.parseObject(rows.replaceAll("&quot;", "\""));
		WxArticle article = JSONObject.toJavaObject(obj, WxArticle.class);
		String description = article.getContent();
		String description2 = article.getContent();
		description = description.replaceAll("'","\"");
		//去多个img的src值
		String subFilePath = "";
		String subOldFilePath = "";
		if (description.contains("img")) {
			Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
			Matcher m = p.matcher(description);

			while (m.find()) {
				String imgSrc = m.group(1);
				subOldFilePath +=  imgSrc + ",";
				String[] split = imgSrc.split("/");
				int k=imgSrc.indexOf(split[split.length-2]);
				String subImgSrc = imgSrc.substring(k, imgSrc.length());
				subFilePath += filePath + subImgSrc + ",";
			}
		}
		if(StringUtils.isNotBlank(subFilePath)){
			
			subFilePath = subFilePath.substring(0, subFilePath.length() -1);
			subOldFilePath = subOldFilePath.substring(0, subOldFilePath.length() -1);
			
			//本地图片地址
			String[] imgPathArry = subFilePath.split(",");
			String[] imgOldPathArry = subOldFilePath.split(",");
			
			String[] newPathArry = new String[imgPathArry.length];
			for(int i=0;i<imgPathArry.length;i++){
				String newFilePath = imgPathArry[i];
				// 添加永久图片
//				String materialType = MediaType.Image.toString();
				// 将图片上传到微信，返回url
				JSONObject imgResultObj = WeixinUtil.uploadMaterialImg(newFilePath, TokenThread.accessToken);
				
				// 上传图片的id
//	   		String contentImgMediaId = "";
				String contentContentUrl = "";
				if (imgResultObj != null && imgResultObj.containsKey("url")) {
					// 微信返回来的媒体素材id
//	   			contentImgMediaId = imgResultObj.getString("media_id");
					// 图片url
					contentContentUrl = imgResultObj.getString("url");
				} 
				newPathArry[i] = contentContentUrl;
			}
			
			for(int i=0;i<imgPathArry.length;i++){
				description =  description.replace(imgOldPathArry[i], newPathArry[i]);
			}
		}
		
		//内容保存
		description = StringEscapeUtils.unescapeHtml4(description);
		article.setContent(description);
		AccessToken mpAccount = TokenThread.accessToken;// 获取缓存中的唯一账号

		JSONObject jsonObj = new JSONObject();
		//上传图片素材
		jsonObj.put("thumb_media_id", article.getThumbMediaId());
		if(article.getAuthor() != null){
			jsonObj.put("author", article.getAuthor());
		}else{
			jsonObj.put("author", "");
		}
		if(article.getTitle() != null){
			jsonObj.put("title", article.getTitle());
		}else{
			jsonObj.put("title", "");
		}
		if(article.getContentSourceUrl() != null){
			jsonObj.put("content_source_url",article.getContentSourceUrl());
		}else{
			jsonObj.put("content_source_url", "");
		}
		if(article.getDigest() != null){
			jsonObj.put("digest", article.getDigest());
		}else{
			jsonObj.put("digest", "");
		}
		if(article.getShowCoverPic() != null){
			jsonObj.put("show_cover_pic", article.getShowCoverPic());
		}else{
			jsonObj.put("show_cover_pic", 1);
		}
		
		jsonObj.put("content", article.getContent());
		jsonObj.put("need_open_comment", article.getNeedOpenComment());
		jsonObj.put("only_fans_can_comment", article.getOnlyFansCanComment());
		JSONObject resultObj = WeixinUtil.updateNewsMaterial2(jsonObj, article.getNewsIndex(), article.getMediaId(), mpAccount);
	   
		if (null != resultObj && resultObj.containsKey("errcode") && resultObj.getInteger("errcode") == 0) {
			article.setContent(description2);
			// 更新成功
			this.wxArticleService.update(article);
			//修改图文news表数据
			WxMsgNews msgNews = this.wxMsgNewsService.get(article.getNewsId());
			WxArticle art = new WxArticle();
			art.setNewsId(article.getNewsId());
			List<WxArticle> newArticles = wxArticleService.findList(art);
			if(newArticles.get(0).getId()==article.getId()){
				//这里只修改title 为了模糊查询的时候可以查询到数据
				msgNews.setTitle(article.getTitle());
				this.wxMsgNewsService.updateMediaId(msgNews);
			}
			return AjaxResult.updateSuccess();
		} else {
			return AjaxResult.failure();
		}

	}
	
	
	@RequiresPermissions("wx:wxMsgNews:view")
	@RequestMapping(value = {"selectList", ""})
	public String selectList(WxMsgNews wxMsgNews, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMsgNews> page = wxMsgNewsService.findPage(new Page<WxMsgNews>(request, response), wxMsgNews); 
		model.addAttribute("page", page);
		model.addAttribute("multType", wxMsgNews.getMultType());
		return "modules/wx/wxMsgNewsListselect";
	}
}