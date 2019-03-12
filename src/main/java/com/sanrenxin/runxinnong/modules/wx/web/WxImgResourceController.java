package com.sanrenxin.runxinnong.modules.wx.web;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.FrontController;
import com.sanrenxin.runxinnong.modules.wx.entity.WxImgResource;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.service.WxImgResourceService;
import com.sanrenxin.runxinnong.modules.wx.utils.AjaxResult;
import com.sanrenxin.runxinnong.modules.wx.utils.ImgTypeUtil;
import com.sanrenxin.runxinnong.modules.wx.utils.MediaType;
import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import com.sanrenxin.runxinnong.modules.wx.utils.WeixinUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Random;

/**
 * 微信图片信息Controller
 * @author wjx
 * @version 1527584856
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxImgResource")
public class WxImgResourceController extends FrontController {

	@Autowired
	private WxImgResourceService wxImgResourceService;
	
	@ModelAttribute
	public WxImgResource get(@RequestParam(required=false) String id) {
		WxImgResource entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxImgResourceService.get(id);
		}
		if (entity == null){
			entity = new WxImgResource();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxImgResource:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxImgResource wxImgResource, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxImgResource> page = wxImgResourceService.findPage(new Page<WxImgResource>(request, response), wxImgResource);
		model.addAttribute("page", page);
		return "modules/wx/wxImgResourceList";
	}

	@RequiresPermissions("wx:wxImgResource:view")
	@RequestMapping(value = "form")
	public String form(WxImgResource wxImgResource, Model model) {
		model.addAttribute("wxImgResource", wxImgResource);
		return "modules/wx/wxImgResourceForm";
	}

	@RequiresPermissions("wx:wxImgResource:edit")
	@RequestMapping(value = "save")
	public String save(WxImgResource wxImgResource, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, wxImgResource)){
			return form(wxImgResource, model);
		}
		wxImgResourceService.save(wxImgResource);
		addMessage(redirectAttributes, "保存微信图片信息成功");
		return "redirect:"+ Global.getAdminPath()+"/wx/wxImgResource/?repage";
	}
	
	@RequiresPermissions("wx:wxImgResource:edit")
	@RequestMapping(value = "delete")
	public String delete(WxImgResource wxImgResource, RedirectAttributes redirectAttributes) {
		WeixinUtil.deleteMaterial(wxImgResource.getMediaId(), TokenThread.accessToken);
		wxImgResourceService.delete(wxImgResource);
		addMessage(redirectAttributes, "删除微信图片信息成功");
		return "redirect:"+Global.getAdminPath()+"/wx/wxImgResource/?repage";
	}
	
	
	/**
	 * 上传图片(需要同步微信用)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("uploadImg")
	public AjaxResult saveFile(MultipartFile file, HttpServletRequest request) throws Exception {
		JSONObject obj = new JSONObject();
		if (null == file) {
			return AjaxResult.failure("没有图片上传");
		}
		//原文件名称
		String trueName = file.getOriginalFilename();
		//文件后缀名
		String ext = FilenameUtils.getExtension(trueName);
		if (!ImgTypeUtil.isImg(ext)) {
			return AjaxResult.failure("图片格式不正确");
		}
		if (file.getSize() > 10 * 1024 * 1024) {
			return AjaxResult.failure("上传图片不能大于10M");
		}

		//系统生成的文件名
		String fileName = System.currentTimeMillis() + new Random().nextInt(10000) + "." + ext;
		String filePath = Global.getUserfilesBaseDir()+Global.USERFILES_BASE_URL+File.separator + "wxupload";
		filePath = filePath +File.separator + fileName;

		File saveFile = new File(filePath);
		
		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		file.transferTo(saveFile);
		filePath = saveFile.getPath();
		AccessToken mpAccount = TokenThread.accessToken;//获取缓存中的唯一账号
		//添加永久图片
		String materialType = MediaType.Image.toString();
		//将图片同步到微信，返回mediaId
		JSONObject imgResultObj = WeixinUtil.addMaterial(filePath, materialType, mpAccount);
		
		//上传图片的id
		String imgMediaId = "";
		String imgUrl = "";
		if (imgResultObj != null && imgResultObj.containsKey("media_id")) {
			//微信返回来的媒体素材id
			imgMediaId = imgResultObj.getString("media_id");
			//图片url
			imgUrl = imgResultObj.getString("url");

			WxImgResource img = new WxImgResource();
			img.setName(fileName);
			img.setSize(file.getSize()+"");
			img.setTrueName(trueName);
			img.setType(ext);
			// userfiles/
			 String rul = Global.getBasePath() +  Global.USERFILES_BASE_URL+"wxupload"+ File.separator + fileName;
			img.setUrl(rul);
			img.setHttpUrl(imgUrl);
			img.setMediaId(imgMediaId);
			
			String result = this.wxImgResourceService.addImg(img);
			if (result != null) {
				obj.put("url", result);
				obj.put("imgMediaId", imgMediaId);
			} else {
				obj.put("url", null);
				obj.put("imgMediaId", null);
			}
			return AjaxResult.success(obj);
		} else {
			obj.put("url", null);
			obj.put("imgMediaId", null);
			return AjaxResult.failure();
		}
	}


}