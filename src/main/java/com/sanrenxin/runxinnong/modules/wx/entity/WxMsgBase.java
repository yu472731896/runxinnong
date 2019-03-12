package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信消息类主表Entity
 * @author wjx
 * @version 1527470851
 */
public class WxMsgBase extends DataEntity<WxMsgBase> {
	
	private static final long serialVersionUID = 1L;
	private String msgType;		// 息消类型
	private String inputCode;		// 关注者发送的消息
	private String rule;		// 规则，目前是 &ldquo;相等&rdquo;
	private Integer enable;		// 是否可用
	private Long readCount;		// 消息阅读数
	private Long favourCount;		// 消息点赞数
	
	public WxMsgBase() {
		super();
	}

	public WxMsgBase(String id){
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
	
	@Length(min=0, max=50, message="规则，目前是 &ldquo;相等&rdquo;长度必须介于 0 和 50 之间")
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
	
	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	
	public Long getReadCount() {
		return readCount;
	}

	public void setReadCount(Long readCount) {
		this.readCount = readCount;
	}
	
	public Long getFavourCount() {
		return favourCount;
	}

	public void setFavourCount(Long favourCount) {
		this.favourCount = favourCount;
	}
	
}