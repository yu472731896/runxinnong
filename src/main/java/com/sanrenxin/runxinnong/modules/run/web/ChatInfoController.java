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
import com.sanrenxin.runxinnong.modules.run.entity.ChatInfo;
import com.sanrenxin.runxinnong.modules.run.service.ChatInfoService;

/**
 * 聊天信息Controller
 * @author mh
 * @version 2019-01-29
 */
@Controller
@RequestMapping(value = "${adminPath}/run/chatInfo")
public class ChatInfoController extends BaseController {

	@Autowired
	private ChatInfoService chatInfoService;
	
	@ModelAttribute
	public ChatInfo get(@RequestParam(required=false) String id) {
		ChatInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = chatInfoService.get(id);
		}
		if (entity == null){
			entity = new ChatInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("run:chatInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ChatInfo chatInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ChatInfo> page = chatInfoService.findPage(new Page<ChatInfo>(request, response), chatInfo); 
		model.addAttribute("page", page);
		return "modules/run/chatInfoList";
	}

	@RequiresPermissions("run:chatInfo:view")
	@RequestMapping(value = "form")
	public String form(ChatInfo chatInfo, Model model) {
		model.addAttribute("chatInfo", chatInfo);
		return "modules/run/chatInfoForm";
	}

	@RequiresPermissions("run:chatInfo:edit")
	@RequestMapping(value = "save")
	public String save(ChatInfo chatInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, chatInfo)){
			return form(chatInfo, model);
		}
		chatInfoService.save(chatInfo);
		addMessage(redirectAttributes, "保存聊天信息成功");
		return "redirect:"+Global.getAdminPath()+"/run/chatInfo/?repage";
	}
	
	@RequiresPermissions("run:chatInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(ChatInfo chatInfo, RedirectAttributes redirectAttributes) {
		chatInfoService.delete(chatInfo);
		addMessage(redirectAttributes, "删除聊天信息成功");
		return "redirect:"+Global.getAdminPath()+"/run/chatInfo/?repage";
	}

}