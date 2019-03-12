package com.sanrenxin.runxinnong.modules.wx.web;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxFansText;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.service.WechatService;
import com.sanrenxin.runxinnong.modules.wx.service.WxFansTextService;
import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 粉丝发送消息管理Controller
 * @author wjx
 * @version 1533115174
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxFansText")
public class WxFansTextController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(WxFansTextController.class);
	@Autowired
	private WechatService wechatService;
	@Autowired
	private WxFansTextService wxFansTextService;
	
	@ModelAttribute
	public WxFansText get(@RequestParam(required=false) String id) {
		WxFansText entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxFansTextService.get(id);
		}
		if (entity == null){
			entity = new WxFansText();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxFansText:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxFansText wxFansText, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxFansText> page = wxFansTextService.findPage(new Page<WxFansText>(request, response), wxFansText);
		model.addAttribute("page", page);
		return "modules/wx/wxFansTextList";
	}

	@RequiresPermissions("wx:wxFansText:view")
	@RequestMapping(value = "form")
	public String form(WxFansText wxFansText, Model model) {
		if (wxFansText != null && StringUtils.isNotEmpty(wxFansText.getId())){
			wxFansText = wxFansTextService.get(wxFansText);
		}
		model.addAttribute("wxFansText", wxFansText);
		return "modules/wx/wxFansTextForm";
	}

	@RequiresPermissions("wx:wxFansText:edit")
	@RequestMapping(value = "save")
	public String save(WxFansText wxFansText, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxFansText)){
			return form(wxFansText, model);
		}
		if(StringUtils.isNotEmpty(wxFansText.getReply().trim())){
			String code = "";
			AccessToken accountToken = TokenThread.accessToken;
			JSONObject result =  wechatService.sendCustomText(wxFansText.getOpenId(), wxFansText.getReply(), accountToken);
			log.info(" 客服发送消息-文本消息："+result.toString());
			if(result.getInteger("errcode") != 0){
				code = result.toString();//发送失败
				addMessage(redirectAttributes, "发送回复消息失败");
			}else{
				code = "1";//发送成功
				wxFansText.setReTime(new Date());
				wxFansTextService.save(wxFansText);
				addMessage(redirectAttributes, "保存粉丝发送消息管理成功");
			}
		}
		return "redirect:"+ Global.getAdminPath()+"/wx/wxFansText/?repage";
	}
	
	@RequiresPermissions("wx:wxFansText:edit")
	@RequestMapping(value = "delete")
	public String delete(WxFansText wxFansText, RedirectAttributes redirectAttributes) {
		wxFansTextService.delete(wxFansText);
		addMessage(redirectAttributes, "删除粉丝发送消息管理成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxFansText/?repage";
	}

}