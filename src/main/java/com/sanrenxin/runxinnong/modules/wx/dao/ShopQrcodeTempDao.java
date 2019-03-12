package com.sanrenxin.runxinnong.modules.wx.dao;


import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.wx.entity.ShopQrcodeTemp;

/**
 * 推广二维码模板DAO接口
 * @author yls
 * @version 1531899750
 */
@MyBatisDao
public interface ShopQrcodeTempDao extends CrudDao<ShopQrcodeTemp> {
	
	int getMaxId();
}