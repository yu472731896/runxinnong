package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxAccountFansDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxAccountFans;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信粉丝表Service
 * @author wjx
 * @version 1527472386
 */
@Service
@Transactional(readOnly = true)
public class WxAccountFansService extends CrudService<WxAccountFansDao, WxAccountFans> {

	public WxAccountFans get(String id) {
		return super.get(id);
	}
	
	public List<WxAccountFans> findList(WxAccountFans wxAccountFans) {
		return super.findList(wxAccountFans);
	}
	
	public Page<WxAccountFans> findPage(Page<WxAccountFans> page, WxAccountFans wxAccountFans) {
		return super.findPage(page, wxAccountFans);
	}
	
	@Transactional(readOnly = false)
	public void save(WxAccountFans wxAccountFans) {
		super.save(wxAccountFans);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxAccountFans wxAccountFans) {
		super.delete(wxAccountFans);
	}
	
}