package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxArticleDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMediaFilesDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgBaseDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgNewsDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxArticle;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMediaFiles;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import com.sanrenxin.runxinnong.modules.wx.utils.MsgType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信图文消息Service
 * @author wjx
 * @version 1527577385
 */
@Service
@Transactional(readOnly = true)
public class WxMsgNewsService extends CrudService<WxMsgNewsDao, WxMsgNews> {
	@Autowired
	private WxMsgBaseDao baseDao;
	@Autowired
	private WxMsgNewsDao wxMsgNewsDao;
	@Autowired
	private WxArticleDao articleDao;
	@Autowired
	private WxMediaFilesDao wxMediaFilesDao;
	
	
	@Override
	public WxMsgNews get(String id) {
		return super.get(id);
	}
	
	@Override
	public List<WxMsgNews> findList(WxMsgNews wxMsgNews) {
		return super.findList(wxMsgNews);
	}
	
	@Override
	public Page<WxMsgNews> findPage(Page<WxMsgNews> page, WxMsgNews wxMsgNews) {
		wxMsgNews.setPage(page);
		List<WxMsgNews> list = dao.findList(wxMsgNews);
		for (int i=0;i<list.size();i++) {
			WxArticle art = new WxArticle();
			art.setNewsId(list.get(i).getId());
			List<WxArticle> artList = articleDao.findList(art);
			WxMsgNews wxMsgNews2 = list.get(i);
			wxMsgNews2.setArticles(artList);
			list.set(i, wxMsgNews2);
		}
		page.setList(list);
		return page;
		
		
//		return super.findPage(page, wxMsgNews);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(WxMsgNews wxMsgNews) {
		super.save(wxMsgNews);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(WxMsgNews wxMsgNews) {
		super.delete(wxMsgNews);
	}
	@Transactional(readOnly = false)
	public int addSingleNews(WxMsgNews newsPo, WxMediaFiles entity) {
		int n=0;
	    int m = 0;
	    try {
	    	//保存基本消息
			WxMsgBase base = new WxMsgBase();
			base.setMsgType(MsgType.News.toString());
			base.preInsert();
			 baseDao.insert(base);
	    	//保存图文信息
			newsPo.setBaseId(base.getId());
			newsPo.preInsert();
			this.wxMsgNewsDao.insert(newsPo);
	    	WxArticle art = new WxArticle();
			art.setAuthor(newsPo.getAuthor());
			art.setContent(newsPo.getDescription());
			art.setContentSourceUrl(newsPo.getFromUrl());
			art.setDigest(newsPo.getBrief());
			art.setMediaId(newsPo.getMediaId());
			art.setNewsIndex("0");
			art.setPicUrl(newsPo.getPicPath());
			art.setShowCoverPic(newsPo.getShowPic());
			art.setThumbMediaId(newsPo.getThumbMediaId());
			art.setTitle(newsPo.getTitle());
			art.setUrl(newsPo.getUrl());
			art.setNeedOpenComment(newsPo.getNeedOpenComment());
			art.setOnlyFansCanComment(newsPo.getOnlyFansCanComment());
			art.setNewsId(newsPo.getId());
			art.preInsert();
			articleDao.insert(art);
	    	n = 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	    if(n > 0){
		    try {
		    	entity.preInsert();
		    	wxMediaFilesDao.insert(entity);
		    	m = 1;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	    }
	    if(n >0 && m >0){
	    	return 1;
	    }else{
	    	return 0;
	    }
	}
	@Transactional(readOnly = false)
	public void updateSingleNews(WxMsgNews news) {
		WxArticle art = new WxArticle();
		art.setAuthor(news.getAuthor());
		art.setContent(news.getDescription());
		art.setContentSourceUrl(news.getFromUrl());
		art.setDigest(news.getBrief());
		art.setMediaId(news.getMediaId());
		art.setNewsIndex("0");
		art.setPicUrl(news.getPicPath());
		art.setShowCoverPic(news.getShowPic());
		art.setThumbMediaId(news.getThumbMediaId());
		art.setTitle(news.getTitle());
		art.setUrl(news.getUrl());
		art.setNeedOpenComment(news.getNeedOpenComment());
		art.setOnlyFansCanComment(news.getOnlyFansCanComment());
		art.setNewsId(news.getId());
		String arId=articleDao.getByNewsId(Integer.parseInt(news.getId())).get(0).getId();
		art.setId(arId);
		art.preUpdate();
		articleDao.update(art);
		news.preUpdate();
		this.wxMsgNewsDao.update(news);
	}
	@Transactional(readOnly = false)
	public int addMoreNews(WxMsgNews news) {
		int n=0;
		   
	    try {
	    	List<WxArticle> articles = news.getArticles();
	    	List<WxArticle> list = new ArrayList<WxArticle>();
			//保存基本消息
			WxMsgBase base = new WxMsgBase();
			base.setMsgType(MsgType.News.toString());
			base.preInsert();
			baseDao.insert(base);
			news.setBaseId(base.getId());
	    	//保存图文信息
			news.preInsert();
	    	this.wxMsgNewsDao.insert(news);
	    	for (int i = 0; i < articles.size(); i++) {
	    		WxArticle article=articles.get(i);
	    		article.setNewsId(news.getId());
	    		article.preInsert();
	    		list.add(article);
			}
	    	articleDao.insertByBatch(list);
	    	n = 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	    if(n==1){
			WxMediaFiles entity = new WxMediaFiles();
			entity.setMediaId(news.getMediaId());
			entity.setMediaType(MsgType.News.toString());
			entity.preInsert();
			wxMediaFilesDao.insert(entity);
	    }
	    return n;
	}
	@Transactional(readOnly = false)
	public  int updateMediaId(WxMsgNews msgNews) {
		int n = 0 ;
		try {
			wxMsgNewsDao.updateMediaId(msgNews);
			n = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
		
	}
	
}