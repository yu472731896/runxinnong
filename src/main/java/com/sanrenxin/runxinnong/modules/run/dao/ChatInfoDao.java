/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.run.entity.ChatInfo;

/**
 * 聊天信息DAO接口
 * @author mh
 * @version 2019-01-29
 */
@MyBatisDao
public interface ChatInfoDao extends CrudDao<ChatInfo> {
	
}