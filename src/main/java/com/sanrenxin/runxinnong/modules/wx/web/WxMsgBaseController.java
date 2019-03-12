package com.sanrenxin.runxinnong.modules.wx.web;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgBase;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgBaseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信消息类主表Controller
 * @author wjx
 * @version 1527470851
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMsgBase")
public class WxMsgBaseController extends BaseController {

	@Autowired
	private WxMsgBaseService wxMsgBaseService;
	
	@ModelAttribute
	public WxMsgBase get(@RequestParam(required=false) String id) {
		WxMsgBase entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMsgBaseService.get(id);
		}
		if (entity == null){
			entity = new WxMsgBase();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMsgBase:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxMsgBase wxMsgBase, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMsgBase> page = wxMsgBaseService.findPage(new Page<WxMsgBase>(request, response), wxMsgBase);
		model.addAttribute("page", page);
		return "modules/wx/wxMsgBaseList";
	}

	@RequiresPermissions("wx:wxMsgBase:view")
	@RequestMapping(value = "form")
	public String form(WxMsgBase wxMsgBase, Model model) {
		model.addAttribute("wxMsgBase", wxMsgBase);
		return "modules/wx/wxMsgBaseForm";
	}

	@RequiresPermissions("wx:wxMsgBase:edit")
	@RequestMapping(value = "save")
	public String save(WxMsgBase wxMsgBase, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxMsgBase)){
			return form(wxMsgBase, model);
		}
		wxMsgBaseService.save(wxMsgBase);
		addMessage(redirectAttributes, "保存微信消息类主表成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxMsgBase/?repage";
	}
	
	@RequiresPermissions("wx:wxMsgBase:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMsgBase wxMsgBase, RedirectAttributes redirectAttributes) {
		wxMsgBaseService.delete(wxMsgBase);
		addMessage(redirectAttributes, "删除微信消息类主表成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMsgBase/?repage";
	}

}