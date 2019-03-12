package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMenuDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信菜单表Service
 * @author wjx
 * @version 1527301918
 */
@Service
@Transactional(readOnly = true)
public class WxMenuService extends CrudService<WxMenuDao, WxMenu> {

	@Autowired
	private WxMenuDao wxMenuDao;
	public WxMenu get(String id) {
		return super.get(id);
	}
	
	public List<WxMenu> findList(WxMenu wxMenu) {
		return super.findList(wxMenu);
	}
	
	public Page<WxMenu> findPage(Page<WxMenu> page, WxMenu wxMenu) {
		return super.findPage(page, wxMenu);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMenu wxMenu) {
		super.save(wxMenu);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMenu wxMenu) {
		super.delete(wxMenu);
	}
	@Transactional(readOnly = false)
	public void deleteall(WxMenu wxMenu) {
		wxMenuDao.deleteall(wxMenu);
		
	}
	
}