package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 推广二维码模板Entity
 * @author yls
 * @version 1531899750
 */
public class ShopQrcodeTemp extends DataEntity<ShopQrcodeTemp> {
	
	private static final long serialVersionUID = 1L;
	private String bgimageUrl;		// 背景图片路径
	private String qrcodeX;		// 二维码的X坐标
	private String qrcodeY;		// 二维码的Y坐标
	private String qrcodeWidth;		// 二维码宽
	private String qrcodeHeight;		// 二维码高
	private String textX;		// 文字X坐标
	private String textY;		// 文字的Y坐标
	private String textWidth;		// 文字的宽度
	private String textHeight;		// 文字的高
	private String textColor;		// 字体颜色
	private String textSize;		// 字体大小
	private String qrimageUrl;		// qrimage_url
	private String textType;		//字体类型
	private String headUrl;			//头像路径
	private String headWidth;		//头像宽度
	private String headHeight;		//头像高度
	private String headX;			//头像X坐标
	private String headY;			//头像Y坐标
	private String nickName;		//微信昵称
	private String nickHeight;		//微信昵称高
	private String nickWidth;		//微信昵称宽
	private String nickX;			//微信昵称X坐标
	private String nickY;			//微信昵称Y坐标
	
	public ShopQrcodeTemp() {
		super();
	}

	public ShopQrcodeTemp(String id){
		super(id);
	}

	@Length(min=0, max=200, message="背景图片路径长度必须介于 0 和 200 之间")
	public String getBgimageUrl() {
		return bgimageUrl;
	}

	public void setBgimageUrl(String bgimageUrl) {
		this.bgimageUrl = bgimageUrl;
	}
	
	@Length(min=0, max=11, message="二维码的X坐标长度必须介于 0 和 11 之间")
	public String getQrcodeX() {
		return qrcodeX;
	}

	public void setQrcodeX(String qrcodeX) {
		this.qrcodeX = qrcodeX;
	}
	
	@Length(min=0, max=11, message="二维码的Y坐标长度必须介于 0 和 11 之间")
	public String getQrcodeY() {
		return qrcodeY;
	}

	public void setQrcodeY(String qrcodeY) {
		this.qrcodeY = qrcodeY;
	}
	
	@Length(min=0, max=11, message="二维码宽长度必须介于 0 和 11 之间")
	public String getQrcodeWidth() {
		return qrcodeWidth;
	}

	public void setQrcodeWidth(String qrcodeWidth) {
		this.qrcodeWidth = qrcodeWidth;
	}
	
	@Length(min=0, max=11, message="二维码高长度必须介于 0 和 11 之间")
	public String getQrcodeHeight() {
		return qrcodeHeight;
	}

	public void setQrcodeHeight(String qrcodeHeight) {
		this.qrcodeHeight = qrcodeHeight;
	}
	
	@Length(min=0, max=11, message="文字X坐标长度必须介于 0 和 11 之间")
	public String getTextX() {
		return textX;
	}

	public void setTextX(String textX) {
		this.textX = textX;
	}
	
	@Length(min=0, max=11, message="文字的Y坐标长度必须介于 0 和 11 之间")
	public String getTextY() {
		return textY;
	}

	public void setTextY(String textY) {
		this.textY = textY;
	}
	
	@Length(min=0, max=11, message="文字的宽度长度必须介于 0 和 11 之间")
	public String getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(String textWidth) {
		this.textWidth = textWidth;
	}
	
	@Length(min=0, max=11, message="文字的高长度必须介于 0 和 11 之间")
	public String getTextHeight() {
		return textHeight;
	}

	public void setTextHeight(String textHeight) {
		this.textHeight = textHeight;
	}
	
	@Length(min=0, max=20, message="字体颜色长度必须介于 0 和 20 之间")
	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	
	@Length(min=0, max=11, message="字体大小长度必须介于 0 和 11 之间")
	public String getTextSize() {
		return textSize;
	}

	public void setTextSize(String textSize) {
		this.textSize = textSize;
	}
	
	@Length(min=0, max=200, message="qrimage_url长度必须介于 0 和 200 之间")
	public String getQrimageUrl() {
		return qrimageUrl;
	}

	public void setQrimageUrl(String qrimageUrl) {
		this.qrimageUrl = qrimageUrl;
	}

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

	@Length(min=0, max=200, message="head_url长度必须介于 0 和 200 之间")
	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	@Length(min=0, max=11, message="头像的宽度长度必须介于 0 和 11 之间")
	public String getHeadWidth() {
		return headWidth;
	}

	public void setHeadWidth(String headWidth) {
		this.headWidth = headWidth;
	}
	@Length(min=0, max=11, message="头像的高度长度必须介于 0 和 11 之间")
	public String getHeadHeight() {
		return headHeight;
	}

	public void setHeadHeight(String headHeight) {
		this.headHeight = headHeight;
	}
	@Length(min=0, max=11, message="头像的X坐标必须介于 0 和 11 之间")
	public String getHeadX() {
		return headX;
	}

	public void setHeadX(String headX) {
		this.headX = headX;
	}

	@Length(min=0, max=11, message="头像的Y坐标必须介于 0 和 11 之间")
	public String getHeadY() {
		return headY;
	}

	public void setHeadY(String headY) {
		this.headY = headY;
	}
	@Length(min=0, max=200, message="微信昵称长度必须介于 0 和 200 之间")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@Length(min=0, max=11, message="微信昵称的高坐标必须介于 0 和 11 之间")
	public String getNickHeight() {
		return nickHeight;
	}

	public void setNickHeight(String nickHeight) {
		this.nickHeight = nickHeight;
	}
	@Length(min=0, max=11, message="微信昵称的宽坐标必须介于 0 和 11 之间")
	public String getNickWidth() {
		return nickWidth;
	}

	public void setNickWidth(String nickWidth) {
		this.nickWidth = nickWidth;
	}
	@Length(min=0, max=11, message="微信昵称的x坐标必须介于 0 和 11 之间")
	public String getNickX() {
		return nickX;
	}

	public void setNickX(String nickX) {
		this.nickX = nickX;
	}
	@Length(min=0, max=11, message="微信昵称的Y坐标必须介于 0 和 11 之间")
	public String getNickY() {
		return nickY;
	}

	public void setNickY(String nickY) {
		this.nickY = nickY;
	}
	
}