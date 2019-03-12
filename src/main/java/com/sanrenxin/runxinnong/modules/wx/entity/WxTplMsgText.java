package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信模板消息Entity
 * @author wjx
 * @version 1527596037
 */
public class WxTplMsgText extends DataEntity<WxTplMsgText> {
	
	private static final long serialVersionUID = 1L;
	private String tplId;		// 信微模板ID
	private String title;		// 消息标题
	private String content;		// 消息内容
	private String wxTpl;		// 微信模板
	private String baseId;		// 消息主表id
	private String account;		// account
	
	public WxTplMsgText() {
		super();
	}

	public WxTplMsgText(String id){
		super(id);
	}

	@Length(min=0, max=50, message="信微模板ID长度必须介于 0 和 50 之间")
	public String getTplId() {
		return tplId;
	}

	public void setTplId(String tplId) {
		this.tplId = tplId;
	}
	
	@Length(min=0, max=200, message="消息标题长度必须介于 0 和 200 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=200, message="微信模板长度必须介于 0 和 200 之间")
	public String getWxTpl() {
		return wxTpl;
	}

	public void setWxTpl(String wxTpl) {
		this.wxTpl = wxTpl;
	}
	
	@Length(min=0, max=11, message="消息主表id长度必须介于 0 和 11 之间")
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	@Length(min=0, max=100, message="account长度必须介于 0 和 100 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}