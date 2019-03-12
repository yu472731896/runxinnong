package com.sanrenxin.runxinnong.common.entity;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.servlet.QRCodeServlet;
import com.sanrenxin.runxinnong.modules.wx.entity.common.AccessToken;
import com.sanrenxin.runxinnong.modules.wx.service.WechatService;
import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 公众号二维码生成工具类
 * @author gzp
 *
 */
public class GenerateQRcodeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = LoggerFactory.getLogger(GenerateQRcodeVo.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	public static String generateQRcode(String mobile,String memberId)  {
		log.debug("开始加工二维码！mobile="+ mobile + ",memberId=" +memberId);
		String imgFileName = "";
		try {
			String filePath = Global.getConfig("qrCodePath");
			filePath += File.separator + "qrcode" + File.separator;
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
			AccessToken accessToken = getAccessToken();// 获取缓存中的唯一账号
			String url = WechatService.createErweima(accessToken.getToken(), memberId,null);
			log.debug("url>>>>" + url);
			// QRCode 二维码图片的文件
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
			OutputStream os = new FileOutputStream(filePath + File.separator + memberId + ".png");
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
			
			imageFile = new File(filePath + File.separator + memberId + ".png");
			
			log.debug("imageFile>>" + imageFile);
			
			bufImg = ImageIO.read(imageFile);
	
			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufImg)));
			
			result = formatReader.decode(binaryBitmap, hints);
	
			QRCodeServlet qr = new QRCodeServlet();
			log.debug("LOGO300是否存在：" + filePath + "LOGO300.PNG");
			if(new File(filePath + "LOGO300.PNG").exists()){
				log.debug("存在！");
			}
			qr.generateQRImage(result.getText(), filePath + "LOGO300.PNG", filePath, "hnd_" + memberId + "_logo.png","PNG", mobile);
			
			imgFileName = "hnd_" + memberId + "_logo.png";
			log.debug("imgFileName>>>" + imgFileName);
			bufImg.flush();

		} catch (Exception e) {
			log.error("加工二维码出错：" + e.getMessage());
			e.printStackTrace();
		}

		return imgFileName;
	}
	
	
	//有背景图的二维码
	
	@ResponseBody
	public static String backgroundQRcode(String tempVersion,String memberId,ImageBean imageBean,String bgImagePath)  {
		log.debug("开始合成有背景图的二维码！memberId=" +memberId);
		 String rePath = "";
		try {
			String filePath = Global.getConfig("qrCodePath");
			filePath += File.separator + "qrcode" + File.separator;
			
			AccessToken accessToken = getAccessToken();// 获取缓存中的唯一账号
			String url = WechatService.createErweima(accessToken.getToken(), memberId,null);
			log.debug("url>>>>" + url);
			// QRCode 二维码图片的文件
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
			String qrImageUrl=filePath + memberId + ".png";
			OutputStream os = new FileOutputStream(qrImageUrl);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
			//----创建二维码图片完成---
			//获取微信头像
			if(!String.valueOf(imageBean.getHeadWidth()).equals("0")){
				String defaultUrl = "/userfiles/default/Default_Avatar.jpg";
				String headUrlTemp = StringUtils.isEmpty(imageBean.getHeadUrl())? defaultUrl :imageBean.getHeadUrl();
				if(headUrlTemp.contains("thirdwx")){
					log.debug("下载微信头像开始");
					String headUrl=filePath + memberId + "_head.png";
					File file = new File(headUrl);
					if(!file.exists()){
						URL imageHeadUrl = new URL(imageBean.getHeadUrl());
						// 打开连接
						URLConnection conHead = imageHeadUrl.openConnection();
						// 输入流
						InputStream isHead = conHead.getInputStream();
						// 1K的数据缓冲
						byte[] bsHead = new byte[1024];
						// 读取到的数据长度
						int lenHead;
						// 输出的文件流
						OutputStream osHead = new FileOutputStream(headUrl);
						// 开始读取
						while ((lenHead = isHead.read(bsHead)) != -1) {
							osHead.write(bsHead, 0, lenHead);
						}
						// 完毕，关闭所有链接
						osHead.close();
						isHead.close();
					}
					
					imageBean.setHeadUrl(headUrl);
					log.debug("获取下载的微信头像路径==》"+headUrl);
				}else{
					String headUrl = Global.getConfig("userfiles.basedir") + headUrlTemp;
					File file = new File(headUrl);
					if(!file.exists()){
						headUrl = Global.getConfig("userfiles.basedir") + defaultUrl;
						log.debug("无微信头像且上传的头像路径不存在，使用默认头像！");
					}
					imageBean.setHeadUrl(headUrl);
					log.debug("获取本地设置的头像路径");
				}
			}
			
			//添加背景图
			//BufferedImage bgimg = ImageIO.read(new File(filePath+"bgimag1.jpg"));
			BufferedImage bgimg = ImageIO.read(new File(bgImagePath));
			if(new File(bgImagePath).exists()){
				log.debug("背景图存在！");
			}
			log.debug("bgImageFile>>" + bgImagePath);
			
	    	imageBean.setImageUrl(qrImageUrl);
	    	log.debug("ImageFile>>" + qrImageUrl);
	    	//生成的路径
	    	String reUrl = filePath;
	    	File FileReUrl = new File(reUrl);
	    	if(!FileReUrl.exists()){
	    		 FileReUrl.mkdirs();
            }
	    	String reFileName = memberId+"_"+tempVersion;
	    	//添加图片
	    	 QRCodeServlet servlet = new QRCodeServlet();
	    	 rePath = servlet.addBackground_QRCode(bgimg,imageBean,reUrl,reFileName);
	    	 log.debug("Return ImagePath>>" + rePath);
	    	 bgimg.flush();

		} catch (Exception e) {
			log.error("图片合成出错：" + e.getMessage());
			e.printStackTrace();
		}

		return rePath;
	}
	
	public static AccessToken getAccessToken() {
		AccessToken accessToken = null;
		TokenThread tokenThread = new TokenThread();
		accessToken = tokenThread.accessToken;
		return accessToken;
	}

}
