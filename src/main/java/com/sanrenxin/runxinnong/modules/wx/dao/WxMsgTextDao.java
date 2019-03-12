package com.sanrenxin.runxinnong.modules.wx.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;

/**
 * 微信文本消息表DAO接口
 * @author wjx
 * @version 1527470880
 */
@MyBatisDao
public interface WxMsgTextDao extends CrudDao<WxMsgText> {
	
}