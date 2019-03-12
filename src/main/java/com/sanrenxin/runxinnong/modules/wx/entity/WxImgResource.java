package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信图片信息Entity
 * @author wjx
 * @version 1527584856
 */
public class WxImgResource extends DataEntity<WxImgResource> {
	
	private static final long serialVersionUID = 1L;
	private String mediaId;		// 微信返回的mediaId
	private String trueName;		// 图片原名称
	private String type;		// 图片尾缀名类型
	private String name;		// 图片存储名称
	private String url;		// 图片路径
	private String httpUrl;		// 图片http访问路径
	private String size;		// 图片大小byte
	private String flag;		// 图片状态字段：0.未引用 ，1.已被引用
	
	public WxImgResource() {
		super();
	}

	public WxImgResource(String id){
		super(id);
	}

	@Length(min=0, max=100, message="微信返回的mediaId长度必须介于 0 和 100 之间")
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@Length(min=0, max=100, message="图片原名称长度必须介于 0 和 100 之间")
	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	@Length(min=0, max=50, message="图片尾缀名类型长度必须介于 0 和 50 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=100, message="图片存储名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=200, message="图片路径长度必须介于 0 和 200 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=0, max=200, message="图片http访问路径长度必须介于 0 和 200 之间")
	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	
	@Length(min=0, max=9, message="图片大小byte长度必须介于 0 和 9 之间")
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	@Length(min=0, max=1, message="已被引用长度必须介于 0 和 1 之间")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}