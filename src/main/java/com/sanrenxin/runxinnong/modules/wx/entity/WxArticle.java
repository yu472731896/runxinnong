package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 微信文章Entity
 * @author wjx
 * @version 1527669895
 */
public class WxArticle extends DataEntity<WxArticle> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String author;		// 作者
	private String content;		// 消息内容
	private String digest;		// digest
	private String showCoverPic;		// 是否显示封面图片
	private String url;		// URL地址
	private String thumbMediaId;		// thumb_media_id
	private String contentSourceUrl;		// 内容来源url地址
	private String mediaId;		// 媒体ID
	private String newsId;		// 图文消息ID
	private String newsIndex;		// 图文排序
	private String picUrl;		// 图片url地址
	private int needOpenComment;//是否开启留言0否，1是
	private int onlyFansCanComment;//是否只有粉丝可以留言0否、1是
	

	public WxArticle() {
		super();
	}

	public WxArticle(String id){
		super(id);
	}

	@Length(min=0, max=50, message="标题长度必须介于 0 和 50 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=50, message="作者长度必须介于 0 和 50 之间")
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=100, message="digest长度必须介于 0 和 100 之间")
	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	@Length(min=0, max=1, message="是否显示封面图片长度必须介于 0 和 1 之间")
	public String getShowCoverPic() {
		return showCoverPic;
	}

	public void setShowCoverPic(String showCoverPic) {
		this.showCoverPic = showCoverPic;
	}
	
	@Length(min=0, max=200, message="URL地址长度必须介于 0 和 200 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=0, max=150, message="thumb_media_id长度必须介于 0 和 150 之间")
	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	
	@Length(min=0, max=200, message="内容来源url地址长度必须介于 0 和 200 之间")
	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}
	
	@Length(min=0, max=150, message="媒体ID长度必须介于 0 和 150 之间")
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@Length(min=0, max=11, message="图文消息ID长度必须介于 0 和 11 之间")
	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	
	@Length(min=0, max=11, message="图文排序长度必须介于 0 和 11 之间")
	public String getNewsIndex() {
		return newsIndex;
	}

	public void setNewsIndex(String newsIndex) {
		this.newsIndex = newsIndex;
	}
	
	@Length(min=0, max=200, message="图片url地址长度必须介于 0 和 200 之间")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getNeedOpenComment() {
		return needOpenComment;
	}

	public void setNeedOpenComment(int needOpenComment) {
		this.needOpenComment = needOpenComment;
	}

	public int getOnlyFansCanComment() {
		return onlyFansCanComment;
	}

	public void setOnlyFansCanComment(int onlyFansCanComment) {
		this.onlyFansCanComment = onlyFansCanComment;
	}
	
}