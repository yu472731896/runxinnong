/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.run.entity.RunDailyArticle;
import com.sanrenxin.runxinnong.modules.run.dao.RunDailyArticleDao;

/**
 * 每日一文Service
 * @author mingh
 * @version 2018-11-11
 */
@Service
@Transactional(readOnly = true)
public class RunDailyArticleService extends CrudService<RunDailyArticleDao, RunDailyArticle> {

	public RunDailyArticle get(String id) {
		return super.get(id);
	}
	
	public List<RunDailyArticle> findList(RunDailyArticle runDailyArticle) {
		return super.findList(runDailyArticle);
	}
	
	public Page<RunDailyArticle> findPage(Page<RunDailyArticle> page, RunDailyArticle runDailyArticle) {
		return super.findPage(page, runDailyArticle);
	}
	
	@Transactional(readOnly = false)
	public void save(RunDailyArticle runDailyArticle) {
		super.save(runDailyArticle);
	}
	
	@Transactional(readOnly = false)
	public void delete(RunDailyArticle runDailyArticle) {
		super.delete(runDailyArticle);
	}

	@Transactional(readOnly = false)
	public void test ()throws Exception{

		if(1==1){
			throw new Exception("cuo");
		}
	}
}