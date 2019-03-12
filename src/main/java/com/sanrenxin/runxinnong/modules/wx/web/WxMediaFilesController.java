package com.sanrenxin.runxinnong.modules.wx.web;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.web.FrontController;
import com.sanrenxin.runxinnong.modules.wx.config.WeixinConfig;
import com.sanrenxin.runxinnong.modules.wx.entity.WxImgResource;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMediaFiles;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.service.WxMediaFilesService;
import com.sanrenxin.runxinnong.modules.wx.utils.AjaxResult;
import com.sanrenxin.runxinnong.modules.wx.utils.PropertiesUtil;
import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import com.sanrenxin.runxinnong.modules.wx.utils.WeixinUtil;
import com.sanrenxin.runxinnong.modules.wx.utils.WxApi;
import com.sanrenxin.runxinnong.modules.wx.utils.exception.WxErrorException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 微信视频文件Controller
 * @author wjx
 * @version 1527575470
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/wxMediaFiles")
public class WxMediaFilesController extends FrontController {

	@Autowired
	private WxMediaFilesService wxMediaFilesService;
	
	@ModelAttribute
	public WxMediaFiles get(@RequestParam(required=false) String id) {
		WxMediaFiles entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxMediaFilesService.get(id);
		}
		if (entity == null){
			entity = new WxMediaFiles();
		}
		return entity;
	}
	
	@RequiresPermissions("wx:wxMediaFiles:view")
	@RequestMapping(value = {"list", ""})
	public String list(WxMediaFiles wxMediaFiles, HttpServletRequest request, HttpServletResponse response, Model model) {
		String mediaType = request.getParameter("mediaType");
		String forward = "modules/wx/wxMediaFilesList";
		if(StringUtils.isNotBlank(mediaType) &&mediaType.equals("voice")){
			forward = "modules/wx/wxMediaFilesListForVoice";
		}
		Page<WxMediaFiles> page = wxMediaFilesService.findPage(new Page<WxMediaFiles>(request, response), wxMediaFiles);
		model.addAttribute("page", page);
		return forward;
	}

	@RequiresPermissions("wx:wxMediaFiles:view")
	@RequestMapping(value = "form")
	public String form(WxMediaFiles wxMediaFiles, Model model) {
		model.addAttribute("wxMediaFiles", wxMediaFiles);
		return "modules/wx/wxMediaFilesForm";
	}

	@RequiresPermissions("wx:wxMediaFiles:view")
	@RequestMapping(value = "formnew")
	public String formnew(WxMediaFiles wxMediaFiles, Model model, HttpServletRequest request) {
		String mediaType = request.getParameter("mediaType");
		String forward = "modules/wx/wxMediaFilesFormnew";
		if(StringUtils.isNotBlank(mediaType) &&mediaType.equals("voice")){
			forward = "modules/wx/wxMediaFilesFormVoice";
		}
		model.addAttribute("wxMediaFiles", wxMediaFiles);
		return forward;
	}
	@RequiresPermissions("wx:wxMediaFiles:edit")
	@RequestMapping(value = "save")
	public String save(WxMediaFiles wxMediaFiles, Model model, RedirectAttributes redirectAttributes) throws WxErrorException {
		if (!beanValidator(model, wxMediaFiles)){
			return form(wxMediaFiles, model);
		}
		String forward = "/wx/wxMediaFiles?mediaType=video&repage";
		if(!StringUtils.isBlank(wxMediaFiles.getMediaType())&&wxMediaFiles.getMediaType().equals("voice")){
			forward = "/wx/wxMediaFiles/list?mediaType=voice&repage";
			addMessage(redirectAttributes, "保存微信语音文件成功");
		}else if(StringUtils.isNotBlank(wxMediaFiles.getMediaType())&&wxMediaFiles.getMediaType().equals("video")){
			addMessage(redirectAttributes, "保存微信视频文件成功");
		}
		
		Map<String,String> params=new HashMap<>();
    	JSONObject json=new JSONObject();
    	json.put("title", wxMediaFiles.getTitle());
    	json.put("introduction", wxMediaFiles.getIntroduction());
    	params.put("description", json.toJSONString());
		JSONObject result = WeixinUtil.addMateria(TokenThread.accessToken.getToken(), "video", wxMediaFiles.getUrl(), params);
		wxMediaFiles.setMediaId(result.getString("media_id"));
		wxMediaFilesService.save(wxMediaFiles);
		
		return "redirect:"+ Global.getAdminPath()+forward;
	}
	
	@RequiresPermissions("wx:wxMediaFiles:edit")
	@RequestMapping(value = "delete")
	public String delete(WxMediaFiles wxMediaFiles, RedirectAttributes redirectAttributes) {
		wxMediaFilesService.delete(wxMediaFiles);
		addMessage(redirectAttributes, "删除微信视频文件成功");
		String forward = "/wx/wxMediaFiles?mediaType=video&repage";
		if(!StringUtils.isBlank(wxMediaFiles.getMediaType())&&wxMediaFiles.getMediaType().equals("voice")){
			forward = "/wx/wxMediaFiles/list?mediaType=voice&repage";
			addMessage(redirectAttributes, "保存微信语音文件成功");
		}else if(StringUtils.isNotBlank(wxMediaFiles.getMediaType())&&wxMediaFiles.getMediaType().equals("video")){
			addMessage(redirectAttributes, "保存微信视频文件成功");
		}
		return "redirect:"+Global.getAdminPath()+forward;
	}
	
	 /**
     *  上传素材文件到本地
     * @param file
     * @return
     * @throws Exception
     */
    @ResponseBody
	@RequestMapping("uploadFile")
	public AjaxResult uploadFile(MultipartFile file, HttpServletRequest request) throws Exception {
		//原文件名称
		String trueName = file.getOriginalFilename();
		//文件后缀名
		String ext = FilenameUtils.getExtension(trueName);

		//系统生成的文件名
		String fileName = file.getOriginalFilename();
		fileName = System.currentTimeMillis() + new Random().nextInt(10000) + "." + ext;
		//文件上传路径
		String resURL = WeixinConfig.weixin_img_url;
		String filePath = request.getSession().getServletContext().getRealPath(File.separator);

		//读取配置文上传件的路径
		if (StringUtils.isNotBlank(WeixinConfig.weixin_img_path)) {
		    filePath = WeixinConfig.weixin_img_path + fileName;
		} else {
			filePath = filePath + File.separator +"upload"+File.separator + fileName;
		}

		File saveFile = new File(filePath);

		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		file.transferTo(saveFile);
		//构造返回参数
		Map<String, Object> mapData = new HashMap();
		mapData.put("src", resURL + fileName);//文件url
		mapData.put("url", filePath);//文件绝对路径url
		mapData.put("title", fileName);//图片名称，这个会显示在输入框里
		
		return AjaxResult.success(mapData);
    }
    
    
    /**
     *  添加语音\图片\缩略图素材
     * @param file
     * @return
     * @throws Exception
     */
    @ResponseBody
	@RequestMapping("addMateria")
	public AjaxResult addMateria(MultipartFile file, String type, HttpServletRequest request) throws Exception {
    	JSONObject obj = new JSONObject();
    	if (null == file) {
			obj.put("message", "没有文件上传");
		}
    	//原文件名称
		String trueName = file.getOriginalFilename();
		//文件后缀名
		String ext = FilenameUtils.getExtension(trueName);

		//系统生成的文件名
		String fileName = file.getOriginalFilename();
		fileName = System.currentTimeMillis() + new Random().nextInt(10000) + "." + ext;
		//文件上传路径
		String resURL = PropertiesUtil.getString("res.upload.url").toString();
		String filePath = request.getSession().getServletContext().getRealPath("/");

		//读取配置文上传件的路径
		if (PropertiesUtil.getString("res.upload.path") != null) {
			filePath = PropertiesUtil.getString("res.upload.path").toString() + fileName;
		} else {
			filePath = filePath + "/upload/" + fileName;
		}

		File saveFile = new File(filePath);

		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		file.transferTo(saveFile);
		
		
		AccessToken mpAccount = TokenThread.accessToken;
    	String accessToken = mpAccount.getToken();
    	JSONObject result = WxApi.addMateria(accessToken, type, filePath,null );
    	String mediaId = result.getString("media_id");
    	//图片或者图文的缩略图
    	if(type.equals("image")||type.equals("thumb")){
    		//图片url
    		String imgUrl = result.getString("url");
    		WxImgResource img = new WxImgResource();
    		img.setName(fileName);
    		img.setSize( file.getSize()+"");
    		img.setTrueName(trueName);
    		img.setType(type);//这里用来区分image和thumb
    		img.setUrl(resURL + fileName);
    		img.setHttpUrl(imgUrl);
    		img.setMediaId(mediaId);
    		
//    		String imgRes = this.wxImgResourceService.addImg(img);
//    		obj.put("url", imgRes);
			obj.put("imgMediaId", mediaId);
			return AjaxResult.success(obj);
    	}else {//音频 voice
	    	WxMediaFiles mediaFile =new WxMediaFiles();
	    	mediaFile.setUploadUrl(resURL + fileName);
	    	mediaFile.setUrl(filePath);
	    	mediaFile.setTitle(fileName);//用title保存文件名
	    	mediaFile.setMediaId(mediaId);
	    	mediaFile.setMediaType(type);
	    	wxMediaFilesService.save(mediaFile);
			return AjaxResult.saveSuccess();
    	}
    }

}