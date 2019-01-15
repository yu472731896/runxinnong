package com.sanrenxin.runxinnong.modules.sys.utils;

import lombok.Data;

import java.util.List;

/**
 * 列表分页查询结果
 *
 * @param <T>
 * @author mh
 * @create 2019-01-11 17:33
 */
@Data
public class PageDataGrid<T> {

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 分页查询的数据集
     */
    private List<T> rows;
}
