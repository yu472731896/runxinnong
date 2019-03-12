package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信TOKEN表Entity
 * 
 * @author LiuDeHua
 * @version 2016-09-16
 */
public class WxAccesstoken extends DataEntity<WxAccesstoken> {

	private static final long serialVersionUID = 1L;
	private String accessToken; // TOKEN
	private Integer expiresIn; // 过期时间

	public WxAccesstoken() {
		super();
	}

	public WxAccesstoken(String id) {
		super(id);
	}

	@Length(min = 1, max = 255, message = "TOKEN长度必须介于 1 和 255 之间")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Length(min = 0, max = 11, message = "过期时间长度必须介于 0 和 11 之间")
	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

}