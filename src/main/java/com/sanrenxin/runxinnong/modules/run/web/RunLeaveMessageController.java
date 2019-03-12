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
import com.sanrenxin.runxinnong.modules.run.entity.RunLeaveMessage;
import com.sanrenxin.runxinnong.modules.run.service.RunLeaveMessageService;

/**
 * 留言Controller
 * @author mingh
 * @version 2019-03-08
 */
@Controller
@RequestMapping(value = "${adminPath}/run/runLeaveMessage")
public class RunLeaveMessageController extends BaseController {

	@Autowired
	private RunLeaveMessageService runLeaveMessageService;
	
	@ModelAttribute
	public RunLeaveMessage get(@RequestParam(required=false) String id) {
		RunLeaveMessage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = runLeaveMessageService.get(id);
		}
		if (entity == null){
			entity = new RunLeaveMessage();
		}
		return entity;
	}
	
	@RequiresPermissions("run:runLeaveMessage:view")
	@RequestMapping(value = {"list", ""})
	public String list(RunLeaveMessage runLeaveMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RunLeaveMessage> page = runLeaveMessageService.findPage(new Page<RunLeaveMessage>(request, response), runLeaveMessage); 
		model.addAttribute("page", page);
		return "modules/run/runLeaveMessageList";
	}

	@RequiresPermissions("run:runLeaveMessage:view")
	@RequestMapping(value = "form")
	public String form(RunLeaveMessage runLeaveMessage, Model model) {
		model.addAttribute("runLeaveMessage", runLeaveMessage);
		return "modules/run/runLeaveMessageForm";
	}

	@RequiresPermissions("run:runLeaveMessage:edit")
	@RequestMapping(value = "save")
	public String save(RunLeaveMessage runLeaveMessage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, runLeaveMessage)){
			return form(runLeaveMessage, model);
		}
		runLeaveMessageService.save(runLeaveMessage);
		addMessage(redirectAttributes, "保存留言成功");
		return "redirect:"+Global.getAdminPath()+"/run/runLeaveMessage/?repage";
	}
	
	@RequiresPermissions("run:runLeaveMessage:edit")
	@RequestMapping(value = "delete")
	public String delete(RunLeaveMessage runLeaveMessage, RedirectAttributes redirectAttributes) {
		runLeaveMessageService.delete(runLeaveMessage);
		addMessage(redirectAttributes, "删除留言成功");
		return "redirect:"+Global.getAdminPath()+"/run/runLeaveMessage/?repage";
	}

}