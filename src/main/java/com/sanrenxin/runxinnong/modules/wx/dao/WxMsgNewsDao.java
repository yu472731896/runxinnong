package com.sanrenxin.runxinnong.modules.wx.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;

/**
 * 微信图文消息DAO接口
 * @author wjx
 * @version 1527577385
 */
@MyBatisDao
public interface WxMsgNewsDao extends CrudDao<WxMsgNews> {

	void updateMediaId(WxMsgNews msgNews);
	
}