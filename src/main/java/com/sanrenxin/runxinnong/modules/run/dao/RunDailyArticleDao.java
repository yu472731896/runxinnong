/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.dao;

import com.sanrenxin.runxinnong.common.persistence.CrudDao;
import com.sanrenxin.runxinnong.common.persistence.annotation.MyBatisDao;
import com.sanrenxin.runxinnong.modules.run.entity.RunDailyArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 每日一文DAO接口
 * @author mingh
 * @version 2018-11-11
 */
@MyBatisDao
public interface RunDailyArticleDao extends CrudDao<RunDailyArticle> {

    /**
     * 获取每日一文数据
     * @param title
     * @param pageNo
     * @param rows
     * @param orderType
     * @param orderField
     * @return List
     * @author YMH
     * @date 2019-03-07
     */
    List<RunDailyArticle> getRunDailyArticleList(@Param("title")String title, @Param("pageNo")Integer pageNo,
                                                 @Param("rows")Integer rows, @Param("orderType")String orderType,
                                                 @Param("orderField")String orderField);
}