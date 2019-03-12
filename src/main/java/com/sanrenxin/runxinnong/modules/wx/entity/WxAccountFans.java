package com.sanrenxin.runxinnong.modules.wx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 微信粉丝表Entity
 * @author wjx
 * @version 1527472386
 */
public class WxAccountFans extends DataEntity<WxAccountFans> {
	
	private static final long serialVersionUID = 1L;
	private String openId;		// 每个用户都是唯一的
	private String subscribeStatus;		// 订阅状态
	private Date subscribeTime;		// 订阅时间
	private byte[] nickName;		// 昵称,二进制保存emoji表情
	private String nickNameStr;   // 昵称
	private String gender;		// 性别 0-女；1-男；2-未知
	private String language;		// 语言
	private String country;		// 国家
	private String province;		// 省
	private String city;		// 城市
	private String headImgUrl;		// 头像
	private String status;		// 用户状态 1-可用；0-不可用
	private String remark;		// 备注
	private String account;		// 公众号ID
	private String wxId;		// 微信号
	
	public WxAccountFans() {
		super();
	}

	public WxAccountFans(String id){
		super(id);
	}

	@Length(min=0, max=100, message="每个用户都是唯一的长度必须介于 0 和 100 之间")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@Length(min=0, max=1, message="订阅状态长度必须介于 0 和 1 之间")
	public String getSubscribeStatus() {
		return subscribeStatus;
	}

	public void setSubscribeStatus(String subscribeStatus) {
		this.subscribeStatus = subscribeStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	
	@Length(min=0, max=50, message="昵称,二进制保存emoji表情长度必须介于 0 和 50 之间")
	public byte[] getNickName() {
		return nickName;
	}
	
	public void setNickName(byte[] nickName) {
		this.nickName = nickName;
	}
	
	public String getNickNameStr() {
		if(this.getNickName() != null){
			try {
				this.nickNameStr = new String(this.getNickName(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return nickNameStr;
	}
	
	public void setNickNameStr(String nickNameStr) {
		this.nickNameStr = nickNameStr;
	}


	@Length(min=0, max=4, message="性别 0-女；1-男；2-未知长度必须介于 0 和 4 之间")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Length(min=0, max=50, message="语言长度必须介于 0 和 50 之间")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Length(min=0, max=30, message="国家长度必须介于 0 和 30 之间")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Length(min=0, max=30, message="省长度必须介于 0 和 30 之间")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Length(min=0, max=30, message="城市长度必须介于 0 和 30 之间")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Length(min=0, max=255, message="头像长度必须介于 0 和 255 之间")
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	@Length(min=0, max=4, message="用户状态 1-可用；0-不可用长度必须介于 0 和 4 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=50, message="备注长度必须介于 0 和 50 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Length(min=0, max=50, message="公众号ID长度必须介于 0 和 50 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Length(min=0, max=100, message="微信号长度必须介于 0 和 100 之间")
	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}
	
}