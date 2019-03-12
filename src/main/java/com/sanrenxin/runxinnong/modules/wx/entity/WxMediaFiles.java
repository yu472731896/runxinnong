package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信视频文件Entity
 * @author wjx
 * @version 1527575470
 */
public class WxMediaFiles extends DataEntity<WxMediaFiles> {
	
	private static final long serialVersionUID = 1L;
	private String mediaType;		// 媒体类型
	private String title;		// 标题
	private String introduction;		// 简介说明
	private String logicClass;		// 标签_逻辑分类
	private String mediaId;		// 返回的media_id
	private String uploadUrl;		// 返回的wx服务器url
	private String url;		//用于存放绝对路径
	private String baseId;//用于存放信息基类的ID
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public WxMediaFiles() {
		super();
	}

	public WxMediaFiles(String id){
		super(id);
	}

	@Length(min=0, max=50, message="媒体类型长度必须介于 0 和 50 之间")
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	@Length(min=0, max=50, message="标题长度必须介于 0 和 50 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=500, message="简介说明长度必须介于 0 和 500 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@Length(min=0, max=50, message="标签_逻辑分类长度必须介于 0 和 50 之间")
	public String getLogicClass() {
		return logicClass;
	}

	public void setLogicClass(String logicClass) {
		this.logicClass = logicClass;
	}
	
	@Length(min=0, max=100, message="返回的media_id长度必须介于 0 和 100 之间")
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@Length(min=0, max=255, message="返回的wx服务器url长度必须介于 0 和 255 之间")
	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
}