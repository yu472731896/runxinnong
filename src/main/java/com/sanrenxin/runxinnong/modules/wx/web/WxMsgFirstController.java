package com.sanrenxin.runxinnong.modules.wx.web;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgFirst;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgFirstService;
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
 * 微信首次回复Controller
 * @author wjx
 * @version 1527902348
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMsgFirst")
public class WxMsgFirstController extends BaseController {

	@Autowired
	private WxMsgFirstService wxMsgFirstService;
	
	@ModelAttribute
	public WxMsgFirst get(@RequestParam(required=false) String id) {
		WxMsgFirst entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMsgFirstService.get(id);
		}
		if (entity == null){
			entity = new WxMsgFirst();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMsgFirst:view")
	@RequestMapping(value = "list")
	public String list(WxMsgFirst wxMsgFirst, HttpServletRequest request, HttpServletResponse response, Model model) {
		String isselect =  request.getParameter("isselect");
		if(StringUtils.isNoneBlank(isselect)){
			addMessage(model, "选择微信首次回复成功");
		}
		Page<WxMsgFirst> page = wxMsgFirstService.findPage(new Page<WxMsgFirst>(request, response), wxMsgFirst);
		model.addAttribute("page", page);
		return "modules/wx/wxMsgFirstList";
	}

	@RequiresPermissions("wx:wxMsgFirst:view")
	@RequestMapping(value = "form")
	public String form(WxMsgFirst wxMsgFirst, Model model) {
		model.addAttribute("wxMsgFirst", wxMsgFirst);
		return "modules/wx/wxMsgFirstForm";
	}

	@RequiresPermissions("wx:wxMsgFirst:edit")
	@RequestMapping(value = "save")
	public String save(WxMsgFirst wxMsgFirst, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxMsgFirst)){
			return form(wxMsgFirst, model);
		}
		wxMsgFirstService.save(wxMsgFirst);
		addMessage(redirectAttributes, "保存微信首次回复成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxMsgFirst/?repage";
	}
	//保存或修改首次回复信息
	@RequestMapping(value = "/newsave")
	@ResponseBody
	public AjaxResult newsave(WxMsgFirst wxMsgFirst, Model model, RedirectAttributes redirectAttributes){
		try {
			List<WxMsgFirst> firstList = wxMsgFirstService.findList(new WxMsgFirst());
			if(firstList.size()>0){
				WxMsgFirst obj = firstList.get(0);
				obj.setTitle(wxMsgFirst.getTitle());
				obj.setBaseId(wxMsgFirst.getBaseId());
				obj.setMsgType(wxMsgFirst.getMsgType());
				wxMsgFirstService.save(obj);
			}else{
				if(wxMsgFirst != null){
					wxMsgFirstService.save(wxMsgFirst);
				}
			}
			return AjaxResult.success();
		} catch (Exception e) {
			logger.error(e.getMessage());;
		}
		return AjaxResult.failure();
	}
	@RequiresPermissions("wx:wxMsgFirst:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMsgFirst wxMsgFirst, RedirectAttributes redirectAttributes) {
		wxMsgFirstService.delete(wxMsgFirst);
		addMessage(redirectAttributes, "删除微信首次回复成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMsgFirst/list?repage";
	}

}