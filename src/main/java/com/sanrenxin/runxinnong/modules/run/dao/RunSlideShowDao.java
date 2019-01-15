/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;

/**
 * 轮播图DAO接口
 * @author minghui
 * @version 2018-10-25
 */
@MyBatisDao
public interface RunSlideShowDao extends CrudDao<RunSlideShow> {
	
}