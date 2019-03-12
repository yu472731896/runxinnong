/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.run.entity.RunLeaveMessage;

/**
 * 留言DAO接口
 * @author mingh
 * @version 2019-03-08
 */
@MyBatisDao
public interface RunLeaveMessageDao extends CrudDao<RunLeaveMessage> {
	
}