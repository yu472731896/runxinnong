package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxImgResourceDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMediaFilesDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgBaseDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxImgResource;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMediaFiles;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;
import com.sanrenxin.runxinnong.modules.wx.utils.MediaType;
import com.sanrenxin.runxinnong.modules.wx.utils.MessageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微信图片信息Service
 * @author wjx
 * @version 1527584856
 */
@Service
@Transactional(readOnly = true)
public class WxImgResourceService extends CrudService<WxImgResourceDao, WxImgResource> {

	@Resource
    private WxImgResourceDao wxImgResourceDao;
	@Resource
    private WxMediaFilesDao wxMediaFilesDao;
	@Resource
    private WxMsgBaseDao wxMsgBaseDao;
	public WxImgResource get(String id) {
		return super.get(id);
	}
	
	public List<WxImgResource> findList(WxImgResource wxImgResource) {
		return super.findList(wxImgResource);
	}
	
	public Page<WxImgResource> findPage(Page<WxImgResource> page, WxImgResource wxImgResource) {
		return super.findPage(page, wxImgResource);
	}
	
	@Transactional(readOnly = false)
	public void save(WxImgResource wxImgResource) {
		super.save(wxImgResource);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxImgResource wxImgResource) {
		wxImgResource = wxImgResourceDao.get(wxImgResource.getId());
		wxMediaFilesDao.deleteByMediaId(wxImgResource);
		wxImgResourceDao.deleteByMediaId(wxImgResource);
//		super.delete(wxImgResource);
	}
	@Transactional(readOnly = false)
	public String addImg(WxImgResource img) {
		img.setFlag(MessageUtil.IMG_FLAG0);
		img.setIsNewRecord(true);
		img.preInsert();
		wxImgResourceDao.insert(img);
		//添加base表
		WxMsgBase base = new WxMsgBase();
		base.preInsert();
		base.setMsgType(MediaType.Image.name());
		wxMsgBaseDao.insert(base);
		//添加到素材表中
		WxMediaFiles entity = new WxMediaFiles();
		entity.setMediaId(img.getMediaId());
		entity.setMediaType("image");
		entity.preInsert();
		wxMediaFilesDao.insert(entity);
		return img.getUrl();
	}
	
}