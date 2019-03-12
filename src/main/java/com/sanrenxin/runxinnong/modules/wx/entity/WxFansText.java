package com.sanrenxin.runxinnong.modules.wx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 粉丝发送消息管理Entity
 * @author wjx
 * @version 1533115174
 */
public class WxFansText extends DataEntity<WxFansText> {
	
	private static final long serialVersionUID = 1L;
	private String content;		// 消息内容
	private String type;		// 消息类型
	private String openId;		// 粉丝OPEN_ID
	private String reply;		// 回复内容
	private Date reTime;		// 回复时间
	
	private WxAccountFans fans;	//微信粉丝表对象
	
	public WxAccountFans getFans() {
		return fans;
	}

	public void setFans(WxAccountFans fans) {
		this.fans = fans;
	}

	public WxFansText() {
		super();
	}

	public WxFansText(String id){
		super(id);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=64, message="消息类型长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=100, message="粉丝OPEN_ID长度必须介于 0 和 100 之间")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReTime() {
		return reTime;
	}

	public void setReTime(Date reTime) {
		this.reTime = reTime;
	}
	
}