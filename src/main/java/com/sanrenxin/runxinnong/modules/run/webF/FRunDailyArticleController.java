/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.webF;

import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.modules.run.entity.RunDailyArticle;
import com.sanrenxin.runxinnong.modules.run.service.RunDailyArticleService;
import com.sanrenxin.runxinnong.modules.sys.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 每日一文
 * @author mingh
 * @version 2018-11-11
 */
@RestController
@Api(tags = "每日一文")
@RequestMapping(value = "${frontPath}/run/runDailyArticle")
@Slf4j
public class FRunDailyArticleController {

	@Autowired
	private RunDailyArticleService runDailyArticleService;

	/**
	 * 默认查询前十条（最新）
	 * @return Result
	 * @author YMH
	 * @date 2019-03-08
	 */
	@ApiOperation(value = "获取每日一文列表",httpMethod = "POST")
    @RequestMapping(value="/getRunDailyArticleList", method = RequestMethod.POST)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "title", value = "名称模糊查询",required = false),
            @ApiImplicitParam(name = "pageNo", value = "当前页数",required = false,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "rows", value = "每页行数",required = false,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "orderType", value = "排序类型",required = false,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "orderField", value = "排序字段",required = false,dataType = "String",paramType = "query")
    })
	public Result getRunDailyArticleList(String title,Integer pageNo,Integer rows,String orderType,String orderField){
	    Result result = null;
	    try{
            List<RunDailyArticle> dailyArticleList =
                    runDailyArticleService.getRunDailyArticleList(title,pageNo,rows,orderType,orderField);
            result = Result.success("调用成功",dailyArticleList);
        }catch (Exception e){
	        log.error("获取每日一文列表"+"异常："+e);
            result = Result.error("获取每日一文列表异常"+e.getMessage());
        }
		return result;
	}

	/**
	 * 获取每日一文信息信息
	 * @return Result
	 * @author YMH
	 * @date 2019-03-08
	 */
	@ApiOperation(value = "获取每日一文信息",httpMethod = "POST")
	@RequestMapping(value="/getRunDailyArticle", method = RequestMethod.POST)
    @ApiImplicitParam(name = "id", value = "每日一文Id",required = true,dataType = "String",paramType = "query")
	public Result getRunDailyArticle(String id){
		Result result = null;
		try {
            if(StringUtils.isBlank(id)){
                return Result.error("每日一文Id不能为空");
            }
            result = Result.success("调用成功",runDailyArticleService.get(id));
		}catch (Exception e){
			log.error("获取每日一文信息"+"异常："+e);
			result = Result.error("获取每日一文信息异常"+e.getMessage());
		}
		return result;
	}

}