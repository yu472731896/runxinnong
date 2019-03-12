package com.sanrenxin.runxinnong.modules.wx.web;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgTextService;
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
 * 微信文本消息表Controller
 * @author wjx
 * @version 1527470880
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMsgText")
public class WxMsgTextController extends BaseController {

	@Autowired
	private WxMsgTextService wxMsgTextService;
	
	@ModelAttribute
	public WxMsgText get(@RequestParam(required=false) String id) {
		WxMsgText entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMsgTextService.get(id);
		}
		if (entity == null){
			entity = new WxMsgText();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMsgText:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxMsgText wxMsgText, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMsgText> page = wxMsgTextService.findPage(new Page<WxMsgText>(request, response), wxMsgText);
		model.addAttribute("page", page);
		return "modules/wx/wxMsgTextList";
	}

	@RequiresPermissions("wx:wxMsgText:view")
	@RequestMapping(value = "form")
	public String form(WxMsgText wxMsgText, Model model) {
		model.addAttribute("wxMsgText", wxMsgText);
		return "modules/wx/wxMsgTextForm";
	}

	@RequiresPermissions("wx:wxMsgText:edit")
	@RequestMapping(value = "save")
	public String save(WxMsgText wxMsgText, Model model, RedirectAttributes redirectAttributes, String inputCode) {
		if (!beanValidator(model, wxMsgText)){
			return form(wxMsgText, model);
		}
		wxMsgTextService.save(wxMsgText);
		addMessage(redirectAttributes, "保存微信文本消息表成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxMsgText/list?repage";
	}
	
	@RequiresPermissions("wx:wxMsgText:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMsgText wxMsgText, RedirectAttributes redirectAttributes) {
		wxMsgTextService.delete(wxMsgText);
		addMessage(redirectAttributes, "删除微信文本消息表成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMsgText/list?repage";
	}

	
	@RequiresPermissions("wx:wxMsgText:view")
	@RequestMapping(value = {"selectList", ""})
	public String selectList(WxMsgText wxMsgText, HttpServletRequest request, HttpServletResponse response, Model model) {
		String forward = "modules/wx/wxMsgTextListselect";
		if(StringUtils.isNotEmpty(request.getParameter("forward"))){
			forward = "modules/wx/wxMsgTextListselectForContent";
		}
		Page<WxMsgText> page = wxMsgTextService.findPage(new Page<WxMsgText>(request, response), wxMsgText); 
		model.addAttribute("page", page);
		return forward;
	}
}