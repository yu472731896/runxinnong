/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.entity;

import org.hibernate.validator.constraints.Length;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;

/**
 * 聊天信息Entity
 * @author mh
 * @version 2019-01-29
 */
public class ChatInfo extends DataEntity<ChatInfo> {
	
	private static final long serialVersionUID = 1L;
	private String customId;		// 客服id
	private String guestId;		// 游客id
	private String ipAddress;		// ip地址
	private String address;		// address
	
	public ChatInfo() {
		super();
	}

	public ChatInfo(String id){
		super(id);
	}

    @Length(min=1, max=64, message="客服id长度必须介于 1 和 64 之间")
	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}
	
	@Length(min=1, max=64, message="游客id长度必须介于 1 和 64 之间")
	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}
	
	@Length(min=0, max=15, message="ip地址长度必须介于 0 和 15 之间")
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Length(min=0, max=32, message="address长度必须介于 0 和 32 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}