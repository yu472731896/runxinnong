package com.sanrenxin.runxinnong.modules.wx.entity;

import java.io.Serializable;

/**
 * 微信用户表Entity
 * 
 * @author LiuDeHua
 * @version 2016-09-16
 */
public class WxUser implements Serializable{
	
	private static final long serialVersionUID = -938673572753371514L;
	
	private String openid;
	private String nickname;
	private String sex;
	private String city;
	private String province;
	private String country;
	private String headimgurl;

	public WxUser() {
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

}
