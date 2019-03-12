package com.sanrenxin.runxinnong.modules.wx.web;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgKey;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgKeyService;
import com.sanrenxin.runxinnong.modules.wx.utils.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 微信关键词信息Controller
 * @author wjx
 * @version 1527928314
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMsgKey")
public class WxMsgKeyController extends BaseController {

	@Autowired
	private WxMsgKeyService wxMsgKeyService;
	
	@ModelAttribute
	public WxMsgKey get(@RequestParam(required=false) String id) {
		WxMsgKey entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMsgKeyService.get(id);
		}
		if (entity == null){
			entity = new WxMsgKey();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMsgKey:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxMsgKey wxMsgKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMsgKey> page = wxMsgKeyService.findPage(new Page<WxMsgKey>(request, response), wxMsgKey);
		model.addAttribute("page", page);
		return "modules/wx/wxMsgKeyList";
	}

	@RequiresPermissions("wx:wxMsgKey:view")
	@RequestMapping(value = "form")
	public String form(WxMsgKey wxMsgKey, Model model) {
		model.addAttribute("wxMsgKey", wxMsgKey);
		return "modules/wx/wxMsgKeyForm";
	}

	@RequiresPermissions("wx:wxMsgKey:edit")
	@RequestMapping(value = "save")
	public String save(WxMsgKey wxMsgKey, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxMsgKey)){
			return form(wxMsgKey, model);
		}
		wxMsgKeyService.save(wxMsgKey);
		addMessage(redirectAttributes, "保存微信关键词信息成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxMsgKey/list?repage";
	}
	
	@RequiresPermissions("wx:wxMsgKey:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMsgKey wxMsgKey, RedirectAttributes redirectAttributes) {
		wxMsgKeyService.delete(wxMsgKey);
		addMessage(redirectAttributes, "删除微信关键词信息成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMsgKey/list?repage";
	}
	
	//获取用户列表
		@RequestMapping(value = "/checkInputCode")
		@ResponseBody
		public AjaxResult checkInputCode(WxMsgKey wxMsgKey, Model model, RedirectAttributes redirectAttributes){
			try {
				List<WxMsgKey> list = wxMsgKeyService.findList(wxMsgKey);
				if(list.size()>0){
					return AjaxResult.success();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());;
			}
			return AjaxResult.failure();
		}

}