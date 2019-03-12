package com.sanrenxin.runxinnong.modules.wx.web;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxAccountFans;
import com.sanrenxin.runxinnong.modules.wx.service.WxAccountFansService;
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
 * 微信粉丝表Controller
 * @author wjx
 * @version 1527472386
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxAccountFans")
public class WxAccountFansController extends BaseController {

	@Autowired
	private WxAccountFansService wxAccountFansService;
	
	@ModelAttribute
	public WxAccountFans get(@RequestParam(required=false) String id) {
		WxAccountFans entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxAccountFansService.get(id);
		}
		if (entity == null){
			entity = new WxAccountFans();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxAccountFans:view")
	@RequestMapping(value = "list")
	public String list(WxAccountFans wxAccountFans, HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		String istongbu = request.getParameter("istongbu");
		Page<WxAccountFans> page = wxAccountFansService.findPage(new Page<WxAccountFans>(request, response), wxAccountFans);
		model.addAttribute("page", page);
		if(StringUtils.isNotBlank(istongbu)){
			addMessage(model, "同步微信粉丝成功");
		}
		return "modules/wx/wxAccountFansList";
	}

	@RequiresPermissions("wx:wxAccountFans:view")
	@RequestMapping(value = "form")
	public String form(WxAccountFans wxAccountFans, Model model) {
		model.addAttribute("wxAccountFans", wxAccountFans);
		return "modules/wx/wxAccountFansForm";
	}

	@RequiresPermissions("wx:wxAccountFans:edit")
	@RequestMapping(value = "save")
	public String save(WxAccountFans wxAccountFans, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxAccountFans)){
			return form(wxAccountFans, model);
		}
		wxAccountFansService.save(wxAccountFans);
		addMessage(redirectAttributes, "保存微信粉成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxAccountFans/list?repage";
	}
	
	@RequiresPermissions("wx:wxAccountFans:edit")
	@RequestMapping(value = "delete")
	public String delete(WxAccountFans wxAccountFans, RedirectAttributes redirectAttributes) {
		wxAccountFansService.delete(wxAccountFans);
		addMessage(redirectAttributes, "删除微信粉丝成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxAccountFans/list?repage";
	}
	
	@RequiresPermissions("wx:wxAccountFans:view")
	@RequestMapping(value = "listselect")
	public String listselect(WxAccountFans wxAccountFans, HttpServletRequest request, HttpServletResponse response, Model model,
                             RedirectAttributes redirectAttributes, String istongbu) {
		Page<WxAccountFans> page = wxAccountFansService.findPage(new Page<WxAccountFans>(request, response), wxAccountFans); 
		model.addAttribute("page", page);
		if(StringUtils.isNotBlank(istongbu)){
			addMessage(redirectAttributes, "同步微信粉丝成功");
		}
		return "modules/wx/wxAccountFansListselect";
	}

}