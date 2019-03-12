package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信菜单表Entity
 * @author wjx
 * @version 1527301918
 */
public class WxMenu extends DataEntity<WxMenu> {
	
	private static final long serialVersionUID = 1L;
	private String mtype;		// 事件消息类型
	private String eventType;		// 事件类型
	private String name;		// 菜单名称
	private String inputCode;		// 关键词
	private String url;		// 菜单url地址
	private Integer sort;		// sort
	private Long parentId;		// parent_id
	private String msgType;		// msg_type
	private String msgId;		// msg_id
	private Integer gid;		// gid
	private String account;		// account
	
	public WxMenu() {
		super();
	}

	public WxMenu(String id){
		super(id);
	}

	@Length(min=0, max=50, message="事件消息类型长度必须介于 0 和 50 之间")
	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	
	@Length(min=0, max=50, message="事件类型长度必须介于 0 和 50 之间")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	@Length(min=0, max=100, message="菜单名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="关键词长度必须介于 0 和 255 之间")
	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	
	@Length(min=0, max=255, message="菜单url地址长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Length(min=0, max=64, message="msg_type长度必须介于 0 和 64 之间")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Length(min=0, max=100, message="msg_id长度必须介于 0 和 100 之间")
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}
	
	@Length(min=0, max=100, message="account长度必须介于 0 和 100 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}