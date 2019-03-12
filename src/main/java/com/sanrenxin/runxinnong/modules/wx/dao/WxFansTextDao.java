package com.sanrenxin.runxinnong.modules.wx.dao;


import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxFansText;

/**
 * 粉丝发送消息管理DAO接口
 * @author wjx
 * @version 1533115174
 */
@MyBatisDao
public interface WxFansTextDao extends CrudDao<WxFansText> {
	
}