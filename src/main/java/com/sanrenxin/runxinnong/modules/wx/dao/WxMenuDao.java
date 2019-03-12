package com.sanrenxin.runxinnong.modules.wx.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMenu;

/**
 * 微信菜单表DAO接口
 * @author wjx
 * @version 1527301918
 */
@MyBatisDao
public interface WxMenuDao extends CrudDao<WxMenu> {

	void deleteall(WxMenu wxMenu);
	
}