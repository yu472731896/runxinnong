/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.service;

import java.util.List;

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
	
}