/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.webF;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.run.entity.RunDailyArticle;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;
import com.sanrenxin.runxinnong.modules.run.service.RunSlideShowService;
import com.sanrenxin.runxinnong.modules.sys.entity.ResultBean;
import com.sanrenxin.runxinnong.modules.sys.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 轮播图Controller
 * @author minghui
 * @version 2018-10-25
 */
@RestController
@Api(tags = "轮播图")
@RequestMapping(value = "${frontPath}/run/runSlideShow")
@Slf4j
public class FRunSlideShowController{

	@Autowired
	private RunSlideShowService runSlideShowService;

	/**
	 * 默认查询前8条（最新）
	 * @return Result
	 * @author YMH
	 * @date 2019-03-08
	 */
	@ApiOperation(value = "获取轮播图列表",httpMethod = "POST")
	@RequestMapping(value="/getRunSlideShowList", method = RequestMethod.POST)
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "title", value = "名称模糊查询",required = false),
			@ApiImplicitParam(name = "pageNo", value = "当前页数",required = false,dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "每页行数",required = false,dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "orderType", value = "排序类型",required = false,dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "orderField", value = "排序字段",required = false,dataType = "String",paramType = "query")
	})
	public Result getRunSlideShowList(String title, Integer pageNo, Integer rows, String orderType, String orderField){
		Result result = null;
		try{
			List<RunSlideShow> dailyArticleList =
					runSlideShowService.getRunSlideShowList(title,pageNo,rows,orderType,orderField);
			result = Result.success("调用成功",dailyArticleList);
		}catch (Exception e){
			log.error("获取轮播图列表"+"异常："+e);
			result = Result.error("获取轮播图列表异常"+e.getMessage());
		}
		return result;
	}

	/**
	 * 获取轮播图信息
	 * @return Result
	 * @author YMH
	 * @date 2019-03-08
	 */
	@ApiOperation(value = "获取轮播图信息",httpMethod = "POST")
	@RequestMapping(value="/getRunSlideShow", method = RequestMethod.POST)
	@ApiImplicitParam(name = "id", value = "轮播图Id",required = true,dataType = "String",paramType = "query")
	public Result getRunSlideShow(String id){
		Result result = null;
		try {
		    if(StringUtils.isBlank(id)){
		        return Result.error("轮播图Id不能为空");
            }
			result = Result.success("调用成功",runSlideShowService.get(id));
		}catch (Exception e){
			log.error("获取轮播图信息"+"异常："+e);
			result = Result.error("获取轮播图信息异常"+e.getMessage());
		}
		return result;
	}

}