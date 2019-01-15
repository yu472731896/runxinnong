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
import com.sanrenxin.runxinnong.modules.run.entity.RunNotice;
import com.sanrenxin.runxinnong.modules.run.service.RunNoticeService;

/**
 * 最新通知Controller
 * @author mingh
 * @version 2018-11-11
 */
@Controller
@RequestMapping(value = "${adminPath}/run/runNotice")
public class RunNoticeController extends BaseController {

	@Autowired
	private RunNoticeService runNoticeService;
	
	@ModelAttribute
	public RunNotice get(@RequestParam(required=false) String id) {
		RunNotice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = runNoticeService.get(id);
		}
		if (entity == null){
			entity = new RunNotice();
		}
		return entity;
	}
	
	@RequiresPermissions("run:runNotice:view")
	@RequestMapping(value = {"list", ""})
	public String list(RunNotice runNotice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RunNotice> page = runNoticeService.findPage(new Page<RunNotice>(request, response), runNotice); 
		model.addAttribute("page", page);
		return "modules/run/runNoticeList";
	}

	@RequiresPermissions("run:runNotice:view")
	@RequestMapping(value = "form")
	public String form(RunNotice runNotice, Model model) {
		model.addAttribute("runNotice", runNotice);
		return "modules/run/runNoticeForm";
	}

	@RequiresPermissions("run:runNotice:edit")
	@RequestMapping(value = "save")
	public String save(RunNotice runNotice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, runNotice)){
			return form(runNotice, model);
		}
		runNoticeService.save(runNotice);
		addMessage(redirectAttributes, "保存最新通知成功");
		return "redirect:"+Global.getAdminPath()+"/run/runNotice/?repage";
	}
	
	@RequiresPermissions("run:runNotice:edit")
	@RequestMapping(value = "delete")
	public String delete(RunNotice runNotice, RedirectAttributes redirectAttributes) {
		runNoticeService.delete(runNotice);
		addMessage(redirectAttributes, "删除最新通知成功");
		return "redirect:"+Global.getAdminPath()+"/run/runNotice/?repage";
	}

}