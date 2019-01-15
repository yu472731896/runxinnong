/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.modules.run.entity.RunDailyArticle;
import com.sanrenxin.runxinnong.modules.run.service.RunDailyArticleService;

/**
 * 每日一文Controller
 * @author mingh
 * @version 2018-11-11
 */
@Controller
@RequestMapping(value = "${adminPath}/run/runDailyArticle")
public class RunDailyArticleController extends BaseController {

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
	
	@RequiresPermissions("run:runDailyArticle:view")
	@RequestMapping(value = {"list", ""})
	public String list(RunDailyArticle runDailyArticle, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RunDailyArticle> page = runDailyArticleService.findPage(new Page<RunDailyArticle>(request, response), runDailyArticle); 
		model.addAttribute("page", page);
		return "modules/run/runDailyArticleList";
	}

	@RequiresPermissions("run:runDailyArticle:view")
	@RequestMapping(value = "form")
	public String form(RunDailyArticle runDailyArticle, Model model) {
		model.addAttribute("runDailyArticle", runDailyArticle);
		return "modules/run/runDailyArticleForm";
	}

	@RequiresPermissions("run:runDailyArticle:edit")
	@RequestMapping(value = "save")
	public String save(RunDailyArticle runDailyArticle, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, runDailyArticle)){
			return form(runDailyArticle, model);
		}
		runDailyArticleService.save(runDailyArticle);
		addMessage(redirectAttributes, "保存每日一文成功");
		return "redirect:"+Global.getAdminPath()+"/run/runDailyArticle/?repage";
	}
	
	@RequiresPermissions("run:runDailyArticle:edit")
	@RequestMapping(value = "delete")
	public String delete(RunDailyArticle runDailyArticle, RedirectAttributes redirectAttributes) {
		runDailyArticleService.delete(runDailyArticle);
		addMessage(redirectAttributes, "删除每日一文成功");
		return "redirect:"+Global.getAdminPath()+"/run/runDailyArticle/?repage";
	}

}