package com.sanrenxin.runxinnong.modules.wx.dao;


import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;

/**
 * 微信消息类主表DAO接口
 * @author wjx
 * @version 1527470851
 */
@MyBatisDao
public interface WxMsgBaseDao extends CrudDao<WxMsgBase> {

	void deleteForTplMsgText(WxMsgBase wxMsgBase);
	
}