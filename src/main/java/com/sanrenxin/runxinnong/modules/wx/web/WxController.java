package com.sanrenxin.runxinnong.modules.wx.web;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.servlet.QRCodeServlet;
import com.sanrenxin.runxinnong.common.web.FrontController;
import com.sanrenxin.runxinnong.modules.wx.config.WeixinConfig;
import com.sanrenxin.runxinnong.modules.wx.entity.SNSUserInfo;
import com.sanrenxin.runxinnong.modules.wx.entity.WeixinOauth2Token;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgNews;
import com.sanrenxin.runxinnong.modules.wx.entity.WxMsgText;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.service.WechatService;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgNewsService;
import com.sanrenxin.runxinnong.modules.wx.service.WxMsgTextService;
import com.sanrenxin.runxinnong.modules.wx.utils.AjaxResult;
import com.sanrenxin.runxinnong.modules.wx.utils.ErrCode;
import com.sanrenxin.runxinnong.modules.wx.utils.SignUtil;
import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import com.sanrenxin.runxinnong.modules.wx.utils.WeixinUtil;
import com.sanrenxin.runxinnong.modules.wx.vo.TemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号接口 Controller
 * 
 * @author LiuDeHua
 * @version 2015-10-22
 */
@Controller
@RequestMapping(value = "${frontPath}/wx")
public class WxController extends FrontController {
	private static Logger log = LoggerFactory.getLogger(WxController.class);
	@Autowired
	private WechatService wechatService;
	@Autowired
	private WxMsgTextService wxMsgTextService;
	@Autowired
	private WxMsgNewsService wxMsgNewsService;
//	@Autowired
//	private ShopMemberService shopMemberService;
//	@Autowired
//	private MemberAPIService memberAPIService;
//	@Autowired
//	private ShopRecommendService shopRecommendService;
//	@Autowired
//	private ShopDistributorService shopDistributorService;
//	@Autowired
//	private MemberLevelsAPIService memberLevelsAPIService;
//	@Autowired
//	private ShopMemberidOpenidMappingService shopMemberidOpenidMappingService;
//	@Autowired
//	private CouponAPIService counponAPIService;
	

	@RequestMapping(params = "wechat", method = RequestMethod.GET)
	public void wechatGet(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "signature") String signature, @RequestParam(value = "timestamp") String timestamp, @RequestParam(value = "nonce") String nonce, @RequestParam(value = "echostr") String echostr) {
		if (SignUtil.checkSignature(WeixinConfig.app_token, signature, timestamp, nonce)) {
			try {
				response.getWriter().print(echostr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(params = "wechat", method = RequestMethod.POST)
	public void wechatPost(HttpServletResponse response, HttpServletRequest request) throws IOException {
		System.out.println("==============进来公众号了======================");
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
        request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8");  
        
		String respMessage = wechatService.coreService(request,response);
		System.out.println(respMessage);
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();
	}
	
	//创建微信公众账号菜单
		@RequestMapping(value = "/doPublishMenu")
		@ResponseBody
		public AjaxResult doPublishMenu() {
			JSONObject rstObj = null;
				rstObj = wechatService.publishMenu(TokenThread.accessToken);
				if(rstObj != null){//成功，更新菜单组
					if(rstObj.containsKey("errcode") && rstObj.getInteger("errcode") == 0){
						return AjaxResult.success();
					}
				}
			return AjaxResult.failure();
		}
		
		
		//删除微信公众账号菜单
		@RequestMapping(value = "/deletePublicMenu")
		@ResponseBody
		public String deletePublicMenu(HttpServletRequest request) {
			String code = "";
			JSONObject rstObj = null;
				rstObj = wechatService.deleteMenu(TokenThread.accessToken);
				if(rstObj != null && rstObj.getInteger("errcode") == 0){
					code = "1";
					return code;
				}
			String failureMsg = "删除菜单失败";
			if(rstObj != null){
				failureMsg += ErrCode.errMsg(rstObj.getInteger("errcode"));
			}
			code = failureMsg;
			return code;
		}
		
		//获取用户列表
		@RequestMapping(value = "/syncAccountFansList")
		@ResponseBody
		public AjaxResult syncAccountFansList(){
			AccessToken accountToken = TokenThread.accessToken;
			if(accountToken != null){
				boolean flag = wechatService.syncAccountFansList(accountToken);
				if(flag){
					return AjaxResult.success();
				}
			}
			return AjaxResult.failure();
		}
		
		
		/**
	     * 群发-文本消息
	     * @param textId
	     * @param openIds
	     * @return
	     */
	@RequestMapping(value = "/massSendTextByOpenIds", method = RequestMethod.POST)
	@ResponseBody
	public String massSendTextByOpenIds(String textId,String openIds){
		String code = "0";
		WxMsgText msgText = wxMsgTextService.get(textId);
		//分隔字符串
		String[] openIdAarry = openIds.split(",");

		String content = msgText.getContent();
		
		AccessToken accessToken = TokenThread.accessToken;//获取缓存中的唯一账号
		//openids
		List<String> openidList = new ArrayList<String>();
		for(int i = 0; i<openIdAarry.length; i++){
			String openid = openIdAarry[i];
			openidList.add(openid);
		}
		JSONObject result = WechatService.massSendTextByOpenIds(openidList,content,accessToken);
		log.info(" 群发-文本消息："+result.toString());
		if(result.getInteger("errcode") != 0){
			code = result.toString();//发送失败
		}else{
			code = "1";//发送成功
		}
		return code;
	}
		
		
	/**
	 * 单个手机号生成推荐公众号二维码
	 * 1、调用微信接口创建新的公众号永久二维码
	 * 2、解码二维码
	 * 3、增加logo图片
	 * 4、增加文字
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/test")
	public String test(HttpServletRequest request, HttpServletResponse response,String mobile)  {
		try {
			
			String filePath = Global.getConfig("qrCodePath");
			filePath += File.separator + "static" + File.separator + "qrcode" + File.separator;
			MultiFormatReader formatReader = new MultiFormatReader();
			Map hints = new HashMap();
			// 定义二维码的参数
			//解码设置编码方式为：utf-8，
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			//优化精度
			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			//复杂模式，开启PURE_BARCODE模式
			hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
			Result result = null;
			BufferedImage bufImg = null;
			File imageFile = null;
			BinaryBitmap binaryBitmap = null;
			AccessToken accessToken = TokenThread.accessToken;// 获取缓存中的唯一账号
			String url = WechatService.createErweima(accessToken.getToken(), mobile,null);
			// QRCode 二维码图片的文件
			System.out.println("微信返回的url>>>>>" + url);
			URL imageUrl = new URL(url);
			// 打开连接
			URLConnection con = imageUrl.openConnection();
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(filePath + File.separator + mobile + ".png");
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
	
			imageFile = new File(filePath + File.separator + mobile + ".png");
			
			bufImg = ImageIO.read(imageFile);
	
			binaryBitmap = new BinaryBitmap(
					new HybridBinarizer(new BufferedImageLuminanceSource(bufImg)));
			
			
			result = formatReader.decode(binaryBitmap, hints);
			System.out.println("result>>" + result.getText());
	
			QRCodeServlet qr = new QRCodeServlet();
	
			System.out.println("filePath>>>>>" + filePath);
			// LogoConfig lc = new LogoConfig();
			qr.generateQRImage(result.getText(), filePath + "LOGO300.PNG", filePath, "hnd_" + mobile + "_logo.png",
					"PNG", mobile);
			bufImg.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
		
	/*@RequestMapping("/regAndLogin")
	public String regAndLogin(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session){
		//TODO: openId nickname sex country province city headImgUrl privilegeList
		try{
			request.setCharacterEncoding("UTF-8");
		    response.setCharacterEncoding("UTF-8");
			String openId = request.getParameter("openId");
			String nickname = URLDecoder.decode(request.getParameter("nickname"), "UTF-8");
			String headImgUrl = request.getParameter("headImgUrl");
			String province = request.getParameter("province");
			String city = request.getParameter("city");
			
			ShopMember shopMember = shopMemberService.findByOpenId(openId);
			//openId 存在会员表 直接登陆  跳转商城首页
			if(null != shopMember){
				session.removeAttribute(MemberUtils.MEMBER_SESSION_KEY);
				
				session.setAttribute(MemberUtils.MEMBER_SESSION_KEY, shopMember);
				// 清除临时注册会话
				session.removeAttribute("reg_member_temp");
				
				return "redirect:"+Global.getFrontPath()+"/mobile";
				
			}else{
				//openId 不存在会员表 先注册
				ShopMember sm = new ShopMember();
				sm.setOpenId(openId);
				sm.setName(nickname);
				sm.setPhoto(headImgUrl);
				sm.setProvince(province);
				sm.setCity(city);
				sm.setMemberType(Consts.BASE_CONSTS.MEMBER_TYPE_USER);
				sm.setIsOfflineAgent(Consts.BASE_CONSTS.NO);// 普通会员默认不是代理商
				sm.setIsSalesman(Consts.BASE_CONSTS.NO);// 普通会员默认不是业务员
				sm.setIsStore(Consts.BASE_CONSTS.NO);  // 普通会员默认不是门店
				sm.setIsDistribution(Consts.BASE_CONSTS.NO);// 普通会员默认不是分销员
				sm.setSignInState(Consts.SHOPMEMBER_CONSTS.NOT_SIGN_IN);
				memberAPIService.saveRegMember(sm);
				model.addAttribute("sm", sm);
				session.setAttribute(MemberUtils.MEMBER_SESSION_KEY, sm);
				// 清除临时注册会话
				session.removeAttribute("reg_member_temp");
				
				try {
					// 注册成功后，默认给优惠券
					counponAPIService.regMemberAddCoupon(sm.getId());
				} catch (Exception e) {
					log.error("微信首次关注，注册用户,方法（regAndLogin）-------------保存优惠券异常信息：");
					e.printStackTrace();
				}
				
				//保存推荐关系
				ShopMemberidOpenidMapping smom = shopMemberidOpenidMappingService.findByOpenId(openId);
				if(null != smom){
					String memberId = smom.getMemberId();
					ShopRecommend parent = new ShopRecommend();
					ShopRecommend sr = new ShopRecommend();
					// 新增后台管理推荐关系
					ShopMember reMember = shopMemberService.get(memberId);
					//判断推荐表内推荐人是否存在
					ShopRecommend shopRecommend = new ShopRecommend();
					List<ShopRecommend> sRList = shopRecommendService.findListByMemberId(reMember.getId());
					if(null != sRList && sRList.size() > 0){
						//推荐人存在，直接保存推荐关系
						shopRecommend = sRList.get(0);
						parent = shopRecommend;
						sr.setParent(parent);
						sr.setParentIds(parent.getParentIds()+parent.getId()+",");
						sr.setName(sm.getName());
						if(null != reMember.getMemberType() && reMember.getMemberType().equals("0")){
							sr.setReRole("普通会员");
						}
						if(null != reMember.getIsSalesclerk() && reMember.getIsSalesclerk().equals("1")){
							sr.setReRole("店员");
						}
						if(null != reMember.getIsDistribution() && reMember.getIsDistribution().equals("1")){
							sr.setReRole("分销商");
						}
						if(null != reMember.getIsStore() && reMember.getIsStore().equals("1")){
							sr.setReRole("店长");
						}
						if(null != reMember.getIsSalesman() && reMember.getIsSalesman().equals("1")){
							sr.setReRole("业务员");
						}
						if(null != reMember.getIsOfflineAgent() && reMember.getIsOfflineAgent().equals("1")){
							sr.setReRole("线下代理商");
						}
						sr.setMemberId(sm);
						sr.setRedRole("普通会员");
						shopRecommendService.save(sr);
					}else{
						//推荐人不存在，第一次保存推荐关系，先保存推荐人
						parent = shopRecommendService.get("1");
						shopRecommend.setParent(parent);
						shopRecommend.setParentIds(parent.getParentIds()+parent.getId()+",");
						shopRecommend.setName(reMember.getName());
						if(null != reMember.getMemberType() && reMember.getMemberType().equals("0")){
							shopRecommend.setRedRole("普通会员");
						}
						if(null != reMember.getIsSalesclerk() && reMember.getIsSalesclerk().equals("1")){
							shopRecommend.setRedRole("店员");
						}
						if(null != reMember.getIsDistribution() && reMember.getIsDistribution().equals("1")){
							shopRecommend.setRedRole("分销商");
						}
						if(null != reMember.getIsStore() && reMember.getIsStore().equals("1")){
							shopRecommend.setRedRole("店长");
						}
						if(null != reMember.getIsSalesman() && reMember.getIsSalesman().equals("1")){
							shopRecommend.setRedRole("业务员");
						}
						if(null != reMember.getIsOfflineAgent() && reMember.getIsOfflineAgent().equals("1")){
							shopRecommend.setRedRole("线下代理商");
						}
						shopRecommend.setMemberId(reMember);
						shopRecommend.setRedTelephone(reMember.getMobile());
						ShopDistributor sd = shopDistributorService.findByMemberId(memberId);
						if(null != sd){
							shopRecommend.setDistriGrade(sd.getDistriGrade());
							shopRecommend.setAuditor(sd.getAuditor());
							shopRecommend.setAuditStatus(sd.getStatus());
							shopRecommend.setAuditResult(sd.getAuditResult());
							shopRecommend.setAuditTime(sd.getAuditTime());
						}
						shopRecommendService.save(shopRecommend);
						//保存 被推荐人
						parent = shopRecommendService.get(shopRecommend);
						sr.setParent(parent);
						sr.setParentIds(parent.getParentIds()+parent.getId()+",");
						sr.setName(sm.getName());
						sr.setReRole(shopRecommend.getRedRole());
						sr.setMemberId(sm);
						sr.setRedRole("普通会员");
						shopRecommendService.save(sr);
					}
					//TODO: 保存或更新会员分销级别,设置三级分销展示（PC端)
					String idParent = reMember.getId();
					ShopMemberLevels memberLevels = new ShopMemberLevels(sm.getId(), idParent, "0");
					memberLevels.setMember(sm);
					
					memberLevelsAPIService.saveOrUpdate(memberLevels);
						
					//删除使用的推荐号
					CookieUtils.removeCookie(request, response, "memberId", "/mobile");
				}
			}
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return "redirect:"+Global.getFrontPath()+"/mobile";
	}*/
	
		
	
	/**
		 * 创建带参数的公众号二维码
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@ResponseBody
		@RequestMapping("/testBatch")
		public String testBatch(HttpServletRequest request, HttpServletResponse response)  {
			try {
				String[] mobiles = new String[] { 
						"13720885737","15959106279","13606094449", "18046202875", "13600976795",
						"13328301572", "18050116099","18205920188", "18030225658", "13806077", 
						"13599520175", "13959272151", "18050041145","18906023626","13859998843", 
						"15959868487", "15860751175", "13616056710", "13606069142", "18965186877", 
						"13850074806", "18046284688", "13400731338", "13666030689", "15396204663", 
						"13599915107", "13600973523", "13859992573", "18506995633", "15306926425",
						"15305026205", "13063088109",  "18259235097", "18965196588", "13779993583",
						"18106980613", "13799759667", "15711522521", "18020763989", "13720896076" , 
						"13860187779", "18150104810", "15060778708", "18965152359" ,"18250881669", 
						"13950160080", "15880274273", "13030891034" , "15160430598","13774656607"};
				System.out.println("总共" + mobiles.length + "个号码！");
				String filePath = Global.getConfig("qrCodePath");
				filePath += File.separator + "static" + File.separator + "qrcode" + File.separator;
				MultiFormatReader formatReader = new MultiFormatReader();
				Map hints = new HashMap();
				// 定义二维码的参数
				//解码设置编码方式为：utf-8，
				hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
				//优化精度
				hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
				//复杂模式，开启PURE_BARCODE模式
				hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
				Result result = null;
				BufferedImage bufImg = null;
				File imageFile = null;
				BinaryBitmap binaryBitmap = null;
				for (String mobile : mobiles) {
					Thread.sleep(2000l);//暂停2秒钟
					AccessToken accessToken = TokenThread.accessToken;// 获取缓存中的唯一账号
					String url = WechatService.createErweima(accessToken.getToken(), mobile,null);
					// QRCode 二维码图片的文件
					System.out.println("微信返回的url>>>>>" + url);
					URL imageUrl = new URL(url);
					// 打开连接
					URLConnection con = imageUrl.openConnection();
					// 输入流
					InputStream is = con.getInputStream();
					// 1K的数据缓冲
					byte[] bs = new byte[1024];
					// 读取到的数据长度
					int len;
					// 输出的文件流
					OutputStream os = new FileOutputStream(filePath + File.separator + mobile + ".png");
					// 开始读取
					while ((len = is.read(bs)) != -1) {
						os.write(bs, 0, len);
					}
					// 完毕，关闭所有链接
					os.close();
					is.close();
					
					imageFile = new File(filePath + File.separator + mobile + ".png");
					
					bufImg = ImageIO.read(imageFile);
					
					binaryBitmap = new BinaryBitmap(
							new HybridBinarizer(new BufferedImageLuminanceSource(bufImg)));
					
					// 定义二维码的参数
					
					result = formatReader.decode(binaryBitmap, hints);
					System.out.println("result>>" + result.getText());
					
					QRCodeServlet qr = new QRCodeServlet();
					
					System.out.println("filePath>>>>>" + filePath);
					// LogoConfig lc = new LogoConfig();
					qr.generateQRImage(result.getText(), filePath + "LOGO300.PNG", filePath, "hnd_" + mobile + "_logo.png",
							"PNG", mobile);
					bufImg.flush();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

	/**
	 * 发送模板消息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sendTemplateMessage", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendTemplateMessage(HttpServletRequest request, HttpServletResponse response, String openIds,
			String templateId, String url, Map<String, String> dataMap) {
		AccessToken accessToken = TokenThread.accessToken;// 获取缓存中的唯一账号
		TemplateMessage tplMsg = new TemplateMessage();

		String[] openIdArray = StringUtils.split(openIds, ",");
		for (String openId : openIdArray) {
			tplMsg.setOpenid(openId);
			// 微信公众号号的template id，开发者自行处理参数
			tplMsg.setTemplateId(templateId);
			tplMsg.setUrl(url);
			// dataMap.put("first", "多公众号管理开源平台");
			// dataMap.put("keyword1", "时间：" + DateUtils.formatDateTime(new
			// Date()));
			// dataMap.put("keyword2",
			// "码云平台地址：https://gitee.com/qingfengtaizi/wxmp");
			// dataMap.put("keyword3",
			// "github平台地址：https://github.com/qingfengtaizi/wxmp-web");
			// dataMap.put("remark", "我们期待您的加入");
			tplMsg.setDataMap(dataMap);
			
			JSONObject result = WechatService.sendTemplateMessage(tplMsg, accessToken);
			logger.debug("发送模板消息 结果：" + result);
		}

		return AjaxResult.success();
	}

	// 获取模板消息列表
	@RequestMapping(value = "/syncWxTplMsgTextList")
	@ResponseBody
	public AjaxResult syncWxTplMsgTextList() {
		AccessToken accountToken = TokenThread.accessToken;
		if (accountToken != null) {
			boolean flag = wechatService.syncWxTplMsgTextList(accountToken);
			if (flag) {
				return AjaxResult.success();
			}
		}
		return AjaxResult.failure();
	}

	/**
	 * 客服接口-发送图文消息
	 *
	 * @param msgId
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/sendNewsByOpenId", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendNewsByOpenId(String id, String openid) {

		WxMsgNews msgNews = this.wxMsgNewsService.get(id);

		AccessToken mpAccount = TokenThread.accessToken;// 获取缓存中的唯一账号
		JSONObject result = wechatService.sendCustomNews(openid, msgNews, mpAccount);
		log.info(" 客服接口-发送图文消息：" + result.toString());
		if (result.getInteger("errcode") != 0) {
			TokenThread.regetAccessToken();
			return AjaxResult.failure(result.toString());
		} else {
			return AjaxResult.success();
		}
	}
	
	/**
	 * 页面授权后进行跳转
	 *
	 * @param msgId
	 * @param openid
	 * @return 
	 * @return
	 */
	@RequestMapping(value = "/oAuthServlet")
	public String oAuthServlet(HttpServletRequest request, HttpServletResponse response) {
		SNSUserInfo snsUserInfo =null;
		String nickname = "";
		try {
			request.setCharacterEncoding("UTF-8");
			 
			response.setCharacterEncoding("UTF-8");
			// 用户同意授权后，能获取到code
			String code = request.getParameter("code");
			// 用户同意授权
			if (!"authdeny".equals(code)) {
				// 获取网页授权access_token
				WeixinOauth2Token weixinOauth2Token = WeixinUtil.getOauth2AccessToken(WeixinConfig.app_id,WeixinConfig.app_secret, code);
				// 网页授权接口访问凭证
				//String accessToken = weixinOauth2Token.getAccessToken();
				// 用户标识
				if(weixinOauth2Token == null ||(weixinOauth2Token != null && StringUtils.isBlank(weixinOauth2Token.getOpenId()))){
					weixinOauth2Token = WeixinUtil.refreshOauth2AccessToken(WeixinConfig.app_id,WeixinConfig.app_secret);
				}
				String openId = weixinOauth2Token.getOpenId();
				request.setAttribute("openId", openId);
				// 获取用户信息
				snsUserInfo = WeixinUtil.getSNSUserInfo(weixinOauth2Token.getAccessToken(), openId);
				System.out.println(snsUserInfo);
				String strGBK = snsUserInfo.getNickname();
				nickname = URLEncoder.encode(strGBK, "UTF-8");
				// 设置要传递的参数
				request.setAttribute("snsUserInfo", snsUserInfo);
			}
			//request.getRequestDispatcher(Global.getAdminPath()+"/wx/regAndLogin?snsUserInfo="+snsUserInfo).forward(request, response);
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("paht>>>"+Global.getFrontPath()+"/wx/regAndLogin");
		// 重定向到 regAndLogin
		return "redirect:"+Global.getFrontPath()+"/wx/regAndLogin?openId="+snsUserInfo.getOpenId()+"&nickname="+nickname+"&sex="+snsUserInfo.getSex()+"&country="+snsUserInfo.getCountry()+"&province="+snsUserInfo.getProvince()+"&city="+snsUserInfo.getCity()+"&headImgUrl="+snsUserInfo.getHeadImgUrl();
	}
	@RequestMapping(value = "/oAuthServletIsLogin")
	public String oAuthServletIsLogin(HttpServletRequest request, HttpServletResponse response) {
		 String openId = "";
		 String path = "";
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			// 用户同意授权后，能获取到code
			String code = request.getParameter("code");
			path = request.getParameter("path");
			String mobile = request.getParameter("state");
			log.info("path>>>>>>>"+path);
			log.info("mobile>>>>>>>"+mobile);
			// 用户同意授权
			if (!"authdeny".equals(code)) {
				// 获取网页授权access_token
				WeixinOauth2Token weixinOauth2Token = WeixinUtil.getOauth2AccessToken(WeixinConfig.app_id,WeixinConfig.app_secret, code);
				// 网页授权接口访问凭证
				//String accessToken = weixinOauth2Token.getAccessToken();
				// 用户标识
				if(weixinOauth2Token == null ||(weixinOauth2Token != null && StringUtils.isBlank(weixinOauth2Token.getOpenId()))){
					weixinOauth2Token = WeixinUtil.refreshOauth2AccessToken(WeixinConfig.app_id,WeixinConfig.app_secret);
				}
				openId = weixinOauth2Token.getOpenId();
				request.setAttribute("openId", openId);
				log.info("OPENID>>>>>" +openId);
//				ShopMember member = MemberUtils.getMember();
//				if(member != null && StringUtils.isNotEmpty(member.getMobile()) && StringUtils.isNotEmpty(member.getOpenId())){
//					member.setOpenId(openId);
//					shopMemberService.save(member);
//					//清除缓存
//					MemberUtils.clearCache();				
//					MemberUtils.putCache(MemberUtils.MEMBER_SESSION_KEY, member);
//				}
//				
//				AccessToken accountToken = TokenThread.accessToken;
//				WxAccountFans fans = WeixinUtil.syncAccountFans(openId.toString(), accountToken);
//				if(fans == null){
//					return "1";
//				}else{
//					if(fans.getSubscribeStatus().equals("0")){
//						return "1";
//					}
//				}
				
				
				
//				ShopMember member = shopMemberService.findByLoginName(mobile);
//				if(member != null && StringUtils.isNotEmpty(member.getMobile()) && StringUtils.isEmpty(member.getOpenId())){
//					log.info("会员存在>>>>>");
//					member.setOpenId(openId);
//					shopMemberService.save(member);
//					//清除缓存
//					MemberUtils.clearCache();				
//					MemberUtils.putCache(MemberUtils.MEMBER_SESSION_KEY, member);
//				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("重定向的页面>>>>>" + Global.getBasePath()+path);
		return "redirect:"+Global.getBasePath()+path;
	}
	
/*
	@RequestMapping(value = "/oAuthServletActivity")
	public String oAuthServletActivity(HttpServletRequest request, HttpServletResponse response, Model model) {
		 String openId = "";
		 String path = "";
		 String fansStatus = "0";//默认为0未关注
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			// 用户同意授权后，能获取到code
			String code = request.getParameter("code");
			path = request.getParameter("path");
			String mobile = request.getParameter("state");
			log.info("path>>>>>>>"+path);
			log.info("mobile>>>>>>>"+mobile);
			// 用户同意授权
			if (!"authdeny".equals(code)) {
				// 获取网页授权access_token
				WeixinOauth2Token weixinOauth2Token = WeixinUtil.getOauth2AccessToken(WeixinConfig.app_id,WeixinConfig.app_secret, code);
				// 网页授权接口访问凭证
				//String accessToken = weixinOauth2Token.getAccessToken();
				// 用户标识
				if(weixinOauth2Token == null ||(weixinOauth2Token != null && StringUtils.isBlank(weixinOauth2Token.getOpenId()))){
					weixinOauth2Token = WeixinUtil.refreshOauth2AccessToken(WeixinConfig.app_id,WeixinConfig.app_secret);
				}
				openId = weixinOauth2Token.getOpenId();
				request.setAttribute("openId", openId);
				log.info("OPENID>>>>>" +openId);
//				ShopMember member = MemberUtils.getMember();
//				if(member != null && StringUtils.isNotEmpty(member.getMobile()) && StringUtils.isNotEmpty(member.getOpenId())){
//					member.setOpenId(openId);
//					shopMemberService.save(member);
//					//清除缓存
//					MemberUtils.clearCache();				
//					MemberUtils.putCache(MemberUtils.MEMBER_SESSION_KEY, member);
//				}
				AccessToken accountToken = TokenThread.accessToken;
				WxAccountFans fans = WeixinUtil.syncAccountFans(openId.toString(), accountToken);
				if(fans == null){
					fansStatus = "0";//默认为0未关注
				}else{
					if(fans.getSubscribeStatus().equals("0")){//未关注
						fansStatus = "0";//默认为0未关注
					}else if(fans.getSubscribeStatus().equals("1")){//已关注,判断数据是否存在，不存在则新增粉丝，存在判断关注状态是否一致，不一致则进行更新
						fansStatus = "1";//1已关注
					}
				}
				ShopMember member = shopMemberService.findByOpenId(openId);
				if(member != null ){//如果会员存在，则判断关注状态是否相等，不相等则把当前的关注状态给会员，进行更新
					if(!member.getWxSubscribeStatus().equals(fansStatus)){
						member.setWxSubscribeStatus(fansStatus);
						shopMemberService.save(member);
					}
					ShopMember obj = MemberUtils.getMember();
					if(obj != null){
						MemberUtils.clearCache(obj);
					}
					member.setWxSubscribeStatus(fansStatus);
					MemberUtils.putCache(MemberUtils.MEMBER_SESSION_KEY, member);
				}else{//注册会员
					SNSUserInfo snsUserInfo =null;
					// 获取用户信息
					snsUserInfo = WeixinUtil.getSNSUserInfo(weixinOauth2Token.getAccessToken(), openId);
					//openId 不存在会员表 先注册
					ShopMember sm = new ShopMember();
					sm.setOpenId(openId);
					sm.setName(snsUserInfo.getNickname());
					sm.setPhoto(snsUserInfo.getHeadImgUrl());
					sm.setProvince(snsUserInfo.getProvince());
					sm.setCity(snsUserInfo.getCity());
					sm.setMemberType(Consts.BASE_CONSTS.MEMBER_TYPE_USER);
					sm.setIsOfflineAgent(Consts.BASE_CONSTS.NO);// 普通会员默认不是代理商
					sm.setIsSalesman(Consts.BASE_CONSTS.NO);// 普通会员默认不是业务员
					sm.setIsStore(Consts.BASE_CONSTS.NO);  // 普通会员默认不是门店
					sm.setIsDistribution(Consts.BASE_CONSTS.NO);// 普通会员默认不是分销员
					sm.setSignInState(Consts.SHOPMEMBER_CONSTS.NOT_SIGN_IN);
					sm.setWxSubscribeStatus(fansStatus);
					memberAPIService.saveRegMember(sm);
					model.addAttribute("sm", sm);
					MemberUtils.putCache(MemberUtils.MEMBER_SESSION_KEY, sm);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("重定向的页面>>>>>" + Global.getBasePath()+path);
		return "redirect:"+Global.getBasePath()+path;
	}
*/


}