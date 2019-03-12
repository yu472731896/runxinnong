package com.sanrenxin.runxinnong.modules.wx.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgKey;

/**
 * 微信关键词信息DAO接口
 * @author wjx
 * @version 1527928314
 */
@MyBatisDao
public interface WxMsgKeyDao extends CrudDao<WxMsgKey> {
	
}