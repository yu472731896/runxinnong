package com.sanrenxin.runxinnong.modules.wx.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMenu;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;
import com.sanrenxin.runxinnong.modules.wx.service.WxMenuService;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgNewsService;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgTextService;
import com.sanrenxin.runxinnong.modules.wx.utils.AjaxResult;
import com.sanrenxin.runxinnong.modules.wx.utils.MessageUtil;
import com.sanrenxin.runxinnong.modules.wx.utils.WeixinUtil;
import com.sanrenxin.runxinnong.modules.wx.vo.Matchrule;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.Date;
import java.util.List;

/**
 * 微信菜单表Controller
 * @author wjx
 * @version 1527301918
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMenu")
public class WxMenuController extends BaseController {

	@Autowired
	private WxMenuService wxMenuService;
	@Autowired
	private WxMsgTextService wxMsgTextService;
	@Autowired
	private WxMsgNewsService wxMsgNewsService;
	
	@ModelAttribute
	public WxMenu get(@RequestParam(required=false) String id) {
		WxMenu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMenuService.get(id);
		}
		if (entity == null){
			entity = new WxMenu();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMenu:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxMenu wxMenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxMenu> page = wxMenuService.findPage(new Page<WxMenu>(request, response), wxMenu);
		model.addAttribute("page", page);
		return "modules/wx/wxMenuList";
	}
	
	@RequiresPermissions("wx:wxMenu:view")
	@RequestMapping(value = {"tolist", ""})
	public String tolist(WxMenu wxMenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/wx/menu/menu";
	}
	@RequestMapping(value="/newlist")
	@ResponseBody
	public AjaxResult newlist(WxMenu wxMenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<WxMenu> wxMenus = wxMenuService.findList(wxMenu);
		Matchrule matchrule = new Matchrule();
		List<WxMsgText> msgTestList = wxMsgTextService.findList(new WxMsgText());
		List<WxMsgNews> msgNews = wxMsgNewsService.findList(new WxMsgNews());
		return AjaxResult.success(WeixinUtil.prepareMenus(wxMenus, matchrule,msgTestList,msgNews));
	}
	
	@RequestMapping(value = "/newsave")
	@ResponseBody
	public AjaxResult newsave(String menus) {
		JSONArray jsons = JSONArray.parseArray(menus.replaceAll("&quot;", "\""));
		//每次先行删除公众号所有菜单
		wxMenuService.deleteall(new WxMenu());
		if (CollectionUtils.isNotEmpty(jsons)) {
			for (int i = 0; i < jsons.size(); i++) {
				JSONObject json = jsons.getJSONObject(i);
				if (null != json) {
					WxMenu wxMenu = new WxMenu();
//					String pid = CommonUtil.getUID();
//					wxMenu.setId(pid);
					wxMenu.setName(json.getString("name"));
					wxMenu.setSort(i + 1);
					wxMenu.setParentId((long) 0);
					if (json.containsKey("type")) {
						wxMenu.setMtype(json.getString("type"));
						//判断是否设置key
						if (MessageUtil.MENU_NEED_KEY.contains(json.getString("type"))) {
							wxMenu.setEventType("fix");
                            wxMenu.setMsgType(json.getString("msgType"));
                            wxMenu.setMsgId(json.getString("msgId"));
						}
					}
					if (json.containsKey("url")) {
						wxMenu.setUrl(json.getString("url"));
					}
					if (json.containsKey("media_id")) {
						wxMenu.setMsgId(json.getString("media_id"));
					}
					wxMenu.setCreateDate(new Date());
					//保存
					wxMenuService.save(wxMenu);
					//判断是否有subbutton
					if (json.containsKey("sub_button")) {
						JSONArray buttons = json.getJSONArray("sub_button");
						if (CollectionUtils.isNotEmpty(buttons)) {
							String pid = wxMenu.getId();
							System.out.println("这个的ID是多少啊："+pid);
							for (int j = 0; j < buttons.size(); j++) {
								json = buttons.getJSONObject(j);
								wxMenu = new WxMenu();
								wxMenu.setParentId(Long.parseLong(pid));
								wxMenu.setName(json.getString("name"));
								wxMenu.setSort(j + 1);
								if (json.containsKey("type")) {
									wxMenu.setMtype(json.getString("type"));
									//判断是否设置key
									if (MessageUtil.MENU_NEED_KEY.contains(json.getString("type"))) {
										wxMenu.setEventType("fix");
                                        wxMenu.setMsgType(json.getString("msgType"));
                                        wxMenu.setMsgId(json.getString("msgId"));
									}
								}
								if (json.containsKey("url")) {
									wxMenu.setUrl(json.getString("url"));
								}
								if (json.containsKey("media_id")) {
									wxMenu.setMsgId(json.getString("media_id"));
								}
								wxMenu.setCreateDate(new Date());
								wxMenuService.save(wxMenu);
							}
						}
					}
				}
			}
		}
		return AjaxResult.success();
	}
	
	
	@RequiresPermissions("wx:wxMenu:view")
	@RequestMapping(value = "form")
	public String form(WxMenu wxMenu, Model model) {
		model.addAttribute("wxMenu", wxMenu);
		return "modules/wx/wxMenuForm";
	}

	@RequiresPermissions("wx:wxMenu:edit")
	@RequestMapping(value = "save")
	public String save(WxMenu wxMenu, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxMenu)){
			return form(wxMenu, model);
		}
		wxMenuService.save(wxMenu);
		addMessage(redirectAttributes, "保存微信菜单表成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxMenu/?repage";
	}
	
	@RequiresPermissions("wx:wxMenu:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMenu wxMenu, RedirectAttributes redirectAttributes) {
		wxMenuService.delete(wxMenu);
		addMessage(redirectAttributes, "删除微信菜单表成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxMenu/?repage";
	}

}