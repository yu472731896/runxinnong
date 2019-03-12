package com.sanrenxin.runxinnong.modules.wx.dao;


import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxAccountFans;

import java.util.List;

/**
 * 微信粉丝表DAO接口
 * @author wjx
 * @version 1527472386
 */
@MyBatisDao
public interface WxAccountFansDao extends CrudDao<WxAccountFans> {

	WxAccountFans getLastOpenId();

	void saveList(List<WxAccountFans> fansList);
	
	void deleteAll(WxAccountFans wxAccountFans);
}