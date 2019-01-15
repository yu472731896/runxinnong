/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.entity;

import org.hibernate.validator.constraints.Length;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;

/**
 * 轮播图Entity
 * @author minghui
 * @version 2018-10-25
 */
public class RunSlideShow extends DataEntity<RunSlideShow> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String path;		// 图片地址
	private String articleId;		// 文章id
	private String content;		// 文本内容
	
	public RunSlideShow() {
		super();
	}

	public RunSlideShow(String id){
		super(id);
	}

	@Length(min=0, max=100, message="标题长度必须介于 0 和 100 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="图片地址长度必须介于 0 和 255 之间")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@Length(min=0, max=64, message="文章id长度必须介于 0 和 64 之间")
	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	@Length(min=0, max=10000, message="content长度必须介于 0 和 3000 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}