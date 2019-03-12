package com.sanrenxin.runxinnong.modules.wx.entity;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 微信图文消息Entity
 * @author wjx
 * @version 1527577385
 */
public class WxMsgNews extends DataEntity<WxMsgNews> {
	
	private static final long serialVersionUID = 1L;
	private String multType;		// 1单图文2多图文类
	private String title;		// 标题
	private String author;		// 作者
	private String brief;		// 简介
	private String description;		// 描述
	private String picPath;		// 封面图片
	private String showPic;		// 是否显示图片
	private String url;		// 图文消息原文链接
	private String fromUrl;		// 外部链接
	private String baseId;		// 消息主表id
	private String mediaId;		// 媒体id
	private String thumbMediaId;		// 封面图片id
	private String newsIndex;		// 多图文中的第几条
	private String account;		// account
	private int needOpenComment;		// 是否打开评论，0不打开，1打开
	private int onlyFansCanComment;		// 是否粉丝才可评论，0所有人可评论，1粉丝才可评论
	//一对多
	private List<WxArticle> articles;
	public List<WxArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<WxArticle> articles) {
		this.articles = articles;
	}

	public WxMsgNews() {
		super();
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

	public WxMsgNews(String id){
		super(id);
	}

	@Length(min=0, max=50, message="1单图文2多图文类长度必须介于 0 和 50 之间")
	public String getMultType() {
		return multType;
	}

	public void setMultType(String multType) {
		this.multType = multType;
	}
	
	@Length(min=0, max=255, message="标题长度必须介于 0 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="作者长度必须介于 0 和 255 之间")
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Length(min=0, max=255, message="简介长度必须介于 0 和 255 之间")
	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=255, message="封面图片长度必须介于 0 和 255 之间")
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	@Length(min=0, max=11, message="是否显示图片长度必须介于 0 和 11 之间")
	public String getShowPic() {
		return showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}
	
	@Length(min=0, max=255, message="图文消息原文链接长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=0, max=255, message="外部链接长度必须介于 0 和 255 之间")
	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}
	
	@Length(min=0, max=11, message="消息主表id长度必须介于 0 和 11 之间")
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	@Length(min=0, max=100, message="媒体id长度必须介于 0 和 100 之间")
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	@Length(min=0, max=150, message="封面图片id长度必须介于 0 和 150 之间")
	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	
	@Length(min=0, max=11, message="多图文中的第几条长度必须介于 0 和 11 之间")
	public String getNewsIndex() {
		return newsIndex;
	}

	public void setNewsIndex(String newsIndex) {
		this.newsIndex = newsIndex;
	}
	
	@Length(min=0, max=100, message="account长度必须介于 0 和 100 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}