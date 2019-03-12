package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgKeyDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信关键词信息Service
 * @author wjx
 * @version 1527928314
 */
@Service
@Transactional(readOnly = true)
public class WxMsgKeyService extends CrudService<WxMsgKeyDao, WxMsgKey> {

	public WxMsgKey get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgKey> findList(WxMsgKey wxMsgKey) {
		return super.findList(wxMsgKey);
	}
	
	public Page<WxMsgKey> findPage(Page<WxMsgKey> page, WxMsgKey wxMsgKey) {
		return super.findPage(page, wxMsgKey);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgKey wxMsgKey) {
		super.save(wxMsgKey);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgKey wxMsgKey) {
		super.delete(wxMsgKey);
	}
	
}