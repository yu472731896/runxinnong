/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.run.entity.RunNotice;

/**
 * 最新通知DAO接口
 * @author mingh
 * @version 2018-11-11
 */
@MyBatisDao
public interface RunNoticeDao extends CrudDao<RunNotice> {
	
}