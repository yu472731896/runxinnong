/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 轮播图DAO接口
 * @author minghui
 * @version 2018-10-25
 */
@MyBatisDao
public interface RunSlideShowDao extends CrudDao<RunSlideShow> {

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
    List<RunSlideShow> getRunSlideShowList(@Param("title")String title, @Param("pageNo")Integer pageNo,
                                           @Param("rows")Integer rows, @Param("orderType")String orderType,
                                           @Param("orderField")String orderField);
}