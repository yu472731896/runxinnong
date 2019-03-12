package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxArticleDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgNewsDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxArticle;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微信文章Service
 * @author wjx
 * @version 1527669895
 */
@Service
@Transactional(readOnly = true)
public class WxArticleService extends CrudService<WxArticleDao, WxArticle> {

	@Resource
	WxMsgNewsDao wxMsgNewsDao;
	public WxArticle get(String id) {
		return super.get(id);
	}
	
	public List<WxArticle> findList(WxArticle wxArticle) {
		return super.findList(wxArticle);
	}
	
	public Page<WxArticle> findPage(Page<WxArticle> page, WxArticle wxArticle) {
		return super.findPage(page, wxArticle);
	}
	
	@Transactional(readOnly = false)
	public void save(WxArticle wxArticle) {
		super.save(wxArticle);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxArticle wxArticle) {
		super.delete(wxArticle);
	}
	@Transactional(readOnly = false)
	public void update(WxArticle article){
		if(article.getNewsIndex().equals("0")){
			WxMsgNews news = wxMsgNewsDao.get(article.getNewsId());
			news.setTitle(article.getTitle());
			news.setAuthor(article.getAuthor());
			news.setBrief(article.getDigest());
			news.setDescription(article.getContent());
			news.setPicPath(article.getPicUrl());
			news.setThumbMediaId(article.getThumbMediaId());
			news.setFromUrl(article.getContentSourceUrl());
			news.setShowPic(article.getShowCoverPic());
			news.preUpdate();
			wxMsgNewsDao.update(news);
		}
		article.preUpdate();
		super.save(article);
		
	}
	
}