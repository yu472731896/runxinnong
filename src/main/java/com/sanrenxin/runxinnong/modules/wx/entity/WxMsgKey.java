package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信关键词信息Entity
 * @author wjx
 * @version 1527928314
 */
public class WxMsgKey extends DataEntity<WxMsgKey> {
	
	private static final long serialVersionUID = 1L;
	private String msgType;		// 息消类型
	private String inputCode;		// 关注者发送的消息
	private String baseId;		// 消息基类的ID
	private String title;		// 消息标题
	
	public WxMsgKey() {
		super();
	}

	public WxMsgKey(String id){
		super(id);
	}

	@Length(min=0, max=50, message="息消类型长度必须介于 0 和 50 之间")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Length(min=0, max=50, message="关注者发送的消息长度必须介于 0 和 50 之间")
	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	
	@Length(min=0, max=11, message="消息基类的ID长度必须介于 0 和 11 之间")
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	@Length(min=0, max=255, message="消息标题长度必须介于 0 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}