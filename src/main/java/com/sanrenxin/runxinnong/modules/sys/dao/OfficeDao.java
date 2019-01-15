/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.sys.dao;

import com.sanrenxin.runxinnong.common.persistence.TreeDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
}
