package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgBaseDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgTextDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;
import com.sanrenxin.runxinnong.modules.wx.utils.MsgType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微信文本消息表Service
 * @author wjx
 * @version 1527470880
 */
@Service
@Transactional(readOnly = true)
public class WxMsgTextService extends CrudService<WxMsgTextDao, WxMsgText> {
	@Resource
	private WxMsgBaseDao baseDao;
	public WxMsgText get(String id) {
		return super.get(id);
	}
	
	public List<WxMsgText> findList(WxMsgText wxMsgText) {
		return super.findList(wxMsgText);
	}
	
	public Page<WxMsgText> findPage(Page<WxMsgText> page, WxMsgText wxMsgText) {
		return super.findPage(page, wxMsgText);
	}
	
	@Transactional(readOnly = false)
	public void save(WxMsgText wxMsgText) {
		WxMsgBase base = null;
		if(StringUtils.isEmpty(wxMsgText.getBaseId())){
			base = new WxMsgBase();
			base.setInputCode(wxMsgText.getInputCode());
			base.setMsgType(MsgType.Text.toString());
			base.preInsert();
			baseDao.insert(base);
		}else{
			base = baseDao.get(wxMsgText.getBaseId());
			base.setInputCode(wxMsgText.getInputCode());
			base.setMsgType(MsgType.Text.toString());
			base.preUpdate();
			baseDao.update(base);
		}
		wxMsgText.setBaseId(base.getId());
		super.save(wxMsgText);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxMsgText wxMsgText) {
		super.delete(wxMsgText);
	}
	
}