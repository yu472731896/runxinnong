/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.service;

import java.util.List;

import com.sanrenxin.runxinnong.modules.run.dao.RunDailyArticleDao;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.run.entity.RunNotice;
import com.sanrenxin.runxinnong.modules.run.dao.RunNoticeDao;

/**
 * 最新通知Service
 * @author mingh
 * @version 2018-11-11
 */
@Service
@Transactional(readOnly = true)
public class RunNoticeService extends CrudService<RunNoticeDao, RunNotice> {

	@Autowired
	private RunNoticeDao runNoticeDao;

	public RunNotice get(String id) {
		return super.get(id);
	}
	
	public List<RunNotice> findList(RunNotice runNotice) {
		return super.findList(runNotice);
	}
	
	public Page<RunNotice> findPage(Page<RunNotice> page, RunNotice runNotice) {
		return super.findPage(page, runNotice);
	}
	
	@Transactional(readOnly = false)
	public void save(RunNotice runNotice) {
		super.save(runNotice);
	}
	
	@Transactional(readOnly = false)
	public void delete(RunNotice runNotice) {
		super.delete(runNotice);
	}

	/**
	 * 获取最新通知列表
	 * @param title
	 * @param pageNo
	 * @param rows
	 * @param orderType
	 * @param orderField
	 * @return List
	 * @author YMH
	 * @date 2019-03-07
	 */
    public List<RunNotice> getRunNoticeList(String title, Integer pageNo, Integer rows,
												  String orderType, String orderField) {
		if(null == pageNo || null == rows ){
			pageNo = 1;
			rows = 10;
		}
		return runNoticeDao.getRunNoticeList(title,pageNo,rows,orderType,orderField);
    }
}