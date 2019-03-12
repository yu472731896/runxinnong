package com.sanrenxin.runxinnong.modules.wx.web;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxTplMsgText;
import com.sanrenxin.runxinnong.modules.wx.service.WxTplMsgTextService;
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
 * 微信模板消息Controller
 * @author wjx
 * @version 1527596037
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxTplMsgText")
public class WxTplMsgTextController extends BaseController {

	@Autowired
	private WxTplMsgTextService wxTplMsgTextService;
	
	@ModelAttribute
	public WxTplMsgText get(@RequestParam(required=false) String id) {
		WxTplMsgText entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxTplMsgTextService.get(id);
		}
		if (entity == null){
			entity = new WxTplMsgText();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxTplMsgText:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxTplMsgText wxTplMsgText, HttpServletRequest request, HttpServletResponse response, Model model) {
		String istongbu = request.getParameter("istongbu");
		Page<WxTplMsgText> page = wxTplMsgTextService.findPage(new Page<WxTplMsgText>(request, response), wxTplMsgText);
		model.addAttribute("page", page);
		if(StringUtils.isNotBlank(istongbu)){
			addMessage(model, "同步微信消息模板成功");
		}
		return "modules/wx/wxTplMsgTextList";
	}

	@RequiresPermissions("wx:wxTplMsgText:view")
	@RequestMapping(value = "form")
	public String form(WxTplMsgText wxTplMsgText, Model model) {
		model.addAttribute("wxTplMsgText", wxTplMsgText);
		return "modules/wx/wxTplMsgTextForm";
	}

	@RequiresPermissions("wx:wxTplMsgText:edit")
	@RequestMapping(value = "save")
	public String save(WxTplMsgText wxTplMsgText, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxTplMsgText)){
			return form(wxTplMsgText, model);
		}
		wxTplMsgTextService.save(wxTplMsgText);
		addMessage(redirectAttributes, "保存微信模板消息成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxTplMsgText/?repage";
	}
	
	@RequiresPermissions("wx:wxTplMsgText:edit")
	@RequestMapping(value = "delete")
	public String delete(WxTplMsgText wxTplMsgText, RedirectAttributes redirectAttributes) {
		wxTplMsgTextService.delete(wxTplMsgText);
		addMessage(redirectAttributes, "删除微信模板消息成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxTplMsgText/?repage";
	}

}