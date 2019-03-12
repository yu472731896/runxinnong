package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxFansTextDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxFansText;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 粉丝发送消息管理Service
 * @author wjx
 * @version 1533115174
 */
@Service
@Transactional(readOnly = true)
public class WxFansTextService extends CrudService<WxFansTextDao, WxFansText> {

	public WxFansText get(String id) {
		return super.get(id);
	}
	
	public List<WxFansText> findList(WxFansText wxFansText) {
		return super.findList(wxFansText);
	}
	
	public Page<WxFansText> findPage(Page<WxFansText> page, WxFansText wxFansText) {
		return super.findPage(page, wxFansText);
	}
	
	@Transactional(readOnly = false)
	public void save(WxFansText wxFansText) {
		super.save(wxFansText);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxFansText wxFansText) {
		super.delete(wxFansText);
	}
	
}