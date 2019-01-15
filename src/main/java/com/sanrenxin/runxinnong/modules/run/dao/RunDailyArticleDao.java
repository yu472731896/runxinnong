/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.run.entity.RunDailyArticle;

/**
 * 每日一文DAO接口
 * @author mingh
 * @version 2018-11-11
 */
@MyBatisDao
public interface RunDailyArticleDao extends CrudDao<RunDailyArticle> {
	
}