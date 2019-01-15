/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.webF;

import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.run.entity.RunDailyArticle;
import com.sanrenxin.runxinnong.modules.run.service.RunDailyArticleService;
import com.sanrenxin.runxinnong.modules.sys.entity.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 每日一文Controller
 * @author mingh
 * @version 2018-11-11
 */
@Controller
@RequestMapping(value = "${frontPath}/run/runDailyArticle")
public class FRunDailyArticleController extends BaseController {

	@Autowired
	private RunDailyArticleService runDailyArticleService;
	
	@ModelAttribute
	public RunDailyArticle get(@RequestParam(required=false) String id) {
		RunDailyArticle entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = runDailyArticleService.get(id);
		}
		if (entity == null){
			entity = new RunDailyArticle();
		}
		return entity;
	}

	/**
	 * 默认查询前十条（最新）
	 * @return
	 */
	@RequestMapping(value="getRunDailyArticleList")
	@ResponseBody
	public ResultBean getRunDailyArticleList(HttpServletRequest request, HttpServletResponse response, RunDailyArticle runDailyArticle){
//		ResultBean resultBean = new ResultBean();
		Page<RunDailyArticle> page = new Page<RunDailyArticle>(0,10);
		page.setOrderBy("updateDate");
		Page<RunDailyArticle> dailyArticlePage = runDailyArticleService.findPage(page, runDailyArticle);
		return ResultBean.getSuccess(dailyArticlePage);
	}

	/**
	 * 获取每日一文信息
	 */

}