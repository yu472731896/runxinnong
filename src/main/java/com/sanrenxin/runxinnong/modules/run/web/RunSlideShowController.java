/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sanrenxin.runxinnong.modules.sys.entity.ResultBean;
import com.sanrenxin.runxinnong.modules.sys.entity.ReturnMsg;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;
import com.sanrenxin.runxinnong.modules.run.service.RunSlideShowService;

import java.util.List;

/**
 * 轮播图Controller
 * @author minghui
 * @version 2018-10-25
 */
@Controller
@RequestMapping(value = "${adminPath}/run/runSlideShow")
public class RunSlideShowController extends BaseController {

	@Autowired
	private RunSlideShowService runSlideShowService;

	@ModelAttribute
	public RunSlideShow get(@RequestParam(required=false) String id) {
		RunSlideShow entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = runSlideShowService.get(id);
		}
		if (entity == null){
			entity = new RunSlideShow();
		}
		return entity;
	}

	@RequiresPermissions("run:runSlideShow:view")
	@RequestMapping(value = {"list", ""})
	public String list(RunSlideShow runSlideShow, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RunSlideShow> page = runSlideShowService.findPage(new Page<RunSlideShow>(request, response), runSlideShow);
		model.addAttribute("page", page);
		return "modules/run/runSlideShowList";
	}

	@RequiresPermissions("run:runSlideShow:view")
	@RequestMapping(value = "form")
	public String form(RunSlideShow runSlideShow, Model model) {
		model.addAttribute("runSlideShow", runSlideShow);
		return "modules/run/runSlideShowForm";
	}

	@RequiresPermissions("run:runSlideShow:edit")
	@RequestMapping(value = "save")
	public String save(RunSlideShow runSlideShow, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, runSlideShow)){
			return form(runSlideShow, model);
		}
		runSlideShowService.save(runSlideShow);
		addMessage(redirectAttributes, "保存轮播图成功");
		return "redirect:"+Global.getAdminPath()+"/run/runSlideShow/?repage";
	}

	@RequiresPermissions("run:runSlideShow:edit")
	@RequestMapping(value = "delete")
	public String delete(RunSlideShow runSlideShow, RedirectAttributes redirectAttributes) {
		runSlideShowService.delete(runSlideShow);
		addMessage(redirectAttributes, "删除轮播图成功");
		return "redirect:"+Global.getAdminPath()+"/run/runSlideShow/?repage";
	}


}