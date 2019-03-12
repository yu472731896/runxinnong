package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信文本消息表Entity
 * @author wjx
 * @version 1527470880
 */
public class WxMsgText extends DataEntity<WxMsgText> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 消息标题
	private String content;		// 消息内容
	private String baseId;		// 消息主表id
	private String account;		// 微信公众号
	
	
	private String inputCode;//用于存放关键词
	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public WxMsgText() {
		super();
	}

	public WxMsgText(String id){
		super(id);
	}

	@Length(min=0, max=50, message="消息标题长度必须介于 0 和 50 之间")
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
	
	@Length(min=0, max=11, message="消息主表id长度必须介于 0 和 11 之间")
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	@Length(min=0, max=100, message="微信公众号长度必须介于 0 和 100 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}