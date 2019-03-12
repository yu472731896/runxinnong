package com.sanrenxin.runxinnong.modules.wx.dao;


import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxImgResource;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMediaFiles;

/**
 * 微信视频文件DAO接口
 * @author wjx
 * @version 1527575470
 */
@MyBatisDao
public interface WxMediaFilesDao extends CrudDao<WxMediaFiles> {

	void deleteByMediaId(WxImgResource wxImgResource);
	
}