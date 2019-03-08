/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.service;

import java.util.List;

import com.sanrenxin.runxinnong.common.utils.Encodes;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;
import com.sanrenxin.runxinnong.modules.run.dao.RunSlideShowDao;

/**
 * 轮播图Service
 * @author minghui
 * @version 2018-10-25
 */
@Service
@Transactional(readOnly = true)
public class RunSlideShowService extends CrudService<RunSlideShowDao, RunSlideShow> {

	@Autowired
	private RunSlideShowDao runSlideShowDao;

	public RunSlideShow get(String id) {
		return super.get(id);
	}
	
	public List<RunSlideShow> findList(RunSlideShow runSlideShow) {
		return super.findList(runSlideShow);
	}
	
	public Page<RunSlideShow> findPage(Page<RunSlideShow> page, RunSlideShow runSlideShow) {
		return super.findPage(page, runSlideShow);
	}
	
	@Transactional(readOnly = false)
	public void save(RunSlideShow runSlideShow) {
		if(StringUtils.isNotBlank(runSlideShow.getContent())){
			runSlideShow.setContent(Encodes.unescapeHtml(runSlideShow.getContent()));
		}
		super.save(runSlideShow);
	}
	
	@Transactional(readOnly = false)
	public void delete(RunSlideShow runSlideShow) {
		super.delete(runSlideShow);
	}

	/**
	 * 获取轮播图列表
	 * @param title
	 * @param pageNo
	 * @param rows
	 * @param orderType
	 * @param orderField
	 * @return List
	 * @author YMH
	 * @date 2019-03-07
	 */
    public List<RunSlideShow> getRunSlideShowList(String title, Integer pageNo, Integer rows, String orderType, String orderField) {
    	if(null == pageNo || null == rows ){
			pageNo = 1;
			rows = 6;
		}
		return runSlideShowDao.getRunSlideShowList(title,pageNo,rows,orderType,orderField);
    }
}