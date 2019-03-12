/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.sys.dao;

import com.sanrenxin.runxinnong.common.persistence.TreeDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.sys.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {

    List<Area> findNameLike(@Param(value="name") String name, @Param(value="type") String type);
}
