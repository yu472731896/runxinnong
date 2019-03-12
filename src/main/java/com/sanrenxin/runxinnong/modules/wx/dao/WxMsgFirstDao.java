package com.sanrenxin.runxinnong.modules.wx.dao;


import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgFirst;

/**
 * 微信首次回复DAO接口
 * @author wjx
 * @version 1527902348
 */
@MyBatisDao
public interface WxMsgFirstDao extends CrudDao<WxMsgFirst> {
	
}