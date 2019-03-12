package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.wx.dao.WxMsgBaseDao;
import com.sanrenxin.runxinnong.modules.wx.dao.WxTplMsgTextDao;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;
import com.sanrenxin.runxinnong.modules.wx.entity.WxTplMsgText;
import com.sanrenxin.runxinnong.modules.wx.utils.MsgType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微信模板消息Service
 * @author wjx
 * @version 1527596037
 */
@Service
@Transactional(readOnly = true)
public class WxTplMsgTextService extends CrudService<WxTplMsgTextDao, WxTplMsgText> {
	@Resource
	private WxMsgBaseDao baseDao;
	public WxTplMsgText get(String id) {
		return super.get(id);
	}
	
	public List<WxTplMsgText> findList(WxTplMsgText wxTplMsgText) {
		return super.findList(wxTplMsgText);
	}
	
	public Page<WxTplMsgText> findPage(Page<WxTplMsgText> page, WxTplMsgText wxTplMsgText) {
		return super.findPage(page, wxTplMsgText);
	}
	
	@Transactional(readOnly = false)
	public void save(WxTplMsgText wxTplMsgText) {
		WxMsgBase base = null;
		if(StringUtils.isEmpty(wxTplMsgText.getBaseId())){
			base = new WxMsgBase();
			base.setMsgType(MsgType.Text.toString());
			base.preInsert();
			baseDao.insert(base);
		}else{
			base = baseDao.get(wxTplMsgText.getBaseId());
			base.setMsgType(MsgType.Text.toString());
			base.preUpdate();
			baseDao.update(base);
		}
		wxTplMsgText.setBaseId(base.getId());
		super.save(wxTplMsgText);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxTplMsgText wxTplMsgText) {
		super.delete(wxTplMsgText);
	}
	
}