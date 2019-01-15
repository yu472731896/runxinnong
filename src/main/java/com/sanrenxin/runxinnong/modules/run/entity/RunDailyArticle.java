/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.entity;

import org.hibernate.validator.constraints.Length;

import com.sanrenxin.runxinnong.common.persistence.DataEntity;

/**
 * 每日一文Entity
 * @author mingh
 * @version 2018-11-11
 */
public class RunDailyArticle extends DataEntity<RunDailyArticle> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String path;		// 图片地址
	private String content;		// 内容
	
	public RunDailyArticle() {
		super();
	}

	public RunDailyArticle(String id){
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
	
	@Length(min=0, max=5000, message="内容长度必须介于 0 和 5000 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}