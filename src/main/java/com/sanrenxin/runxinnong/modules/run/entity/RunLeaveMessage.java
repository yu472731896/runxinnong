/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.entity;

import org.hibernate.validator.constraints.Length;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;

/**
 * 留言Entity
 * @author mingh
 * @version 2019-03-08
 */
public class RunLeaveMessage extends DataEntity<RunLeaveMessage> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String phoneNumber;		// 电话号码
	private String email;		// 邮箱
	private String address;		// 地址
	private String content;		// 内容
	private String state;		// 状态
	
	public RunLeaveMessage() {
		super();
	}

	public RunLeaveMessage(String id){
		super(id);
	}

	@Length(min=0, max=12, message="姓名长度必须介于 0 和 12 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=11, message="电话号码长度必须介于 1 和 11 之间")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Length(min=0, max=100, message="邮箱长度必须介于 0 和 100 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=50, message="地址长度必须介于 0 和 50 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=255, message="内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}