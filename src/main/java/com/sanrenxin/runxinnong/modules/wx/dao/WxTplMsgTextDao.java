package com.sanrenxin.runxinnong.modules.wx.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxTplMsgText;

import java.util.List;

/**
 * 微信模板消息DAO接口
 * @author wjx
 * @version 1527596037
 */
@MyBatisDao
public interface WxTplMsgTextDao extends CrudDao<WxTplMsgText> {

	void saveList(List<WxTplMsgText> tplList);
	
	void deleteAll(WxTplMsgText wxTplMsgText);
	
}