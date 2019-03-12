package com.sanrenxin.runxinnong.modules.wx.dao;


import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxImgResource;

/**
 * 微信图片信息DAO接口
 * @author wjx
 * @version 1527584856
 */
@MyBatisDao
public interface WxImgResourceDao extends CrudDao<WxImgResource> {

	void deleteByMediaId(WxImgResource wxImgResource);
	
}