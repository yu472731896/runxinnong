package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMediaFilesDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMediaFiles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信视频文件Service
 * @author wjx
 * @version 1527575470
 */
@Service
@Transactional(readOnly = true)
public class WxMediaFilesService extends CrudService<WxMediaFilesDao, WxMediaFiles> {

	public WxMediaFiles get(String id) {
		return super.get(id);
	}
	
	public List<WxMediaFiles> findList(WxMediaFiles wxMediaFiles) {
		return super.findList(wxMediaFiles);
	}
	
	public Page<WxMediaFiles> findPage(Page<WxMediaFiles> page, WxMediaFiles wxMediaFiles) {
		return super.findPage(page, wxMediaFiles);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMediaFiles wxMediaFiles) {
		super.save(wxMediaFiles);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMediaFiles wxMediaFiles) {
		super.delete(wxMediaFiles);
	}
	
}