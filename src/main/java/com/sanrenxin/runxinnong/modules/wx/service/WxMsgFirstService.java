package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgFirstDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgFirst;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信首次回复Service
 * @author wjx
 * @version 1527902348
 */
@Service
@Transactional(readOnly = true)
public class WxMsgFirstService extends CrudService<WxMsgFirstDao, WxMsgFirst> {

	public WxMsgFirst get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgFirst> findList(WxMsgFirst wxMsgFirst) {
		return super.findList(wxMsgFirst);
	}
	
	public Page<WxMsgFirst> findPage(Page<WxMsgFirst> page, WxMsgFirst wxMsgFirst) {
		return super.findPage(page, wxMsgFirst);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgFirst wxMsgFirst) {
		super.save(wxMsgFirst);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgFirst wxMsgFirst) {
		super.delete(wxMsgFirst);
	}
	
}