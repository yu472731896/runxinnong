package com.sanrenxin.runxinnong.modules.wx.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxArticle;

import java.util.List;

/**
 * 微信文章DAO接口
 * @author wjx
 * @version 1527669895
 */
@MyBatisDao
public interface WxArticleDao extends CrudDao<WxArticle> {

	List<WxArticle> getByNewsId(int newsId);

	void insertByBatch(List<WxArticle> list);
	
}