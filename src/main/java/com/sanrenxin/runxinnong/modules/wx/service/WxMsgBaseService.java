package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgBaseDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信消息类主表Service
 * @author wjx
 * @version 1527470851
 */
@Service
@Transactional(readOnly = true)
public class WxMsgBaseService extends CrudService<WxMsgBaseDao, WxMsgBase> {

	public WxMsgBase get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgBase> findList(WxMsgBase wxMsgBase) {
		return super.findList(wxMsgBase);
	}
	
	public Page<WxMsgBase> findPage(Page<WxMsgBase> page, WxMsgBase wxMsgBase) {
		return super.findPage(page, wxMsgBase);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgBase wxMsgBase) {
		super.save(wxMsgBase);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgBase wxMsgBase) {
		super.delete(wxMsgBase);
	}
	
}