package com.sanrenxin.runxinnong.modules.wx.service;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.entity.GenerateQRcodeVo;
import com.sanrenxin.runxinnong.common.entity.ImageBean;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.service.CrudService;
import com.sanrenxin.runxinnong.common.utils.FileUtils;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.modules.wx.dao.ShopQrcodeTempDao;
import com.sanrenxin.runxinnong.modules.wx.entity.ShopQrcodeTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WxQrcodeTempService extends CrudService<ShopQrcodeTempDao, ShopQrcodeTemp> {
	@Autowired
	private ShopQrcodeTempDao shopQrcodeTempDao;
	
	public ShopQrcodeTemp get(String id) {
		return super.get(id);
	}
	
	public List<ShopQrcodeTemp> findList(ShopQrcodeTemp shopQrcodeTemp) {
		return super.findList(shopQrcodeTemp);
	}
	
	public Page<ShopQrcodeTemp> findPage(Page<ShopQrcodeTemp> page, ShopQrcodeTemp shopQrcodeTemp) {
		return super.findPage(page, shopQrcodeTemp);
	}
	
	@Transactional(readOnly = false)
	public void save(ShopQrcodeTemp shopQrcodeTemp) {
		super.save(shopQrcodeTemp);
	}
	
	@Transactional(readOnly = false)
	public void delete(ShopQrcodeTemp shopQrcodeTemp) {
		List<String> fileNameList = FileUtils.getFileName(Global.getConfig("qrCodePath")+ File.separator +"qrcode");
		for(String fileName : fileNameList) {
			if(fileName.contains("_"+shopQrcodeTemp.getId())) {
				String isExistFileName = Global.getConfig("qrCodePath")+ File.separator +"qrcode"+File.separator+fileName;
				FileUtils.deleteFile(isExistFileName);
				logger.debug("根据版本删除图片："+isExistFileName);
			}
		}
		logger.debug("删除版本时将该版本对应的图片删除完成");
		super.delete(shopQrcodeTemp);
	}
	
	//保存创建模板
	@Transactional(readOnly = false)
	public void createQrcode(ShopQrcodeTemp shopQrcodeTemp,String menberId,String headUrl,String nickName,String tempVersion ) {

		ImageBean imageBean =new ImageBean();
    	imageBean.setImageHeight(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getQrcodeHeight())? "0" : shopQrcodeTemp.getQrcodeHeight()));
    	imageBean.setImageWidth(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getQrcodeWidth()) ? "0" :shopQrcodeTemp.getQrcodeWidth()));
    	imageBean.setImageX(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getQrcodeX())? "0" : shopQrcodeTemp.getQrcodeX()));
    	imageBean.setImageY(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getQrcodeY())? "0" : shopQrcodeTemp.getQrcodeY()));
    	if(StringUtils.isNotEmpty(shopQrcodeTemp.getHeadWidth())){
	    	imageBean.setHeadUrl(headUrl);
	    	imageBean.setHeadHeight(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadHeight())? "0" : shopQrcodeTemp.getHeadHeight()));
	    	imageBean.setHeadWidth(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadWidth()) ? "0" :shopQrcodeTemp.getHeadWidth()));
	    	imageBean.setHeadX(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadX())? "0" : shopQrcodeTemp.getHeadX()));
	    	imageBean.setHeadY(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadY())? "0" : shopQrcodeTemp.getHeadY()));
    	}
    	if(StringUtils.isNotEmpty(shopQrcodeTemp.getNickWidth())){
    		imageBean.setNickName(nickName);
        	imageBean.setNickHeight(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickHeight())? "0" : shopQrcodeTemp.getNickHeight()));
        	imageBean.setNickWidth(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickWidth()) ? "0" :shopQrcodeTemp.getNickWidth()));
        	imageBean.setNickX(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickX())? "0" : shopQrcodeTemp.getNickX()));
        	imageBean.setNickY(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickY())? "0" : shopQrcodeTemp.getNickY()));
    	}
    	 //防止空异常
    	shopQrcodeTemp.setTextX(StringUtils.isEmpty(shopQrcodeTemp.getTextX())? "0" : shopQrcodeTemp.getTextX());
    	shopQrcodeTemp.setTextY(StringUtils.isEmpty(shopQrcodeTemp.getTextY())? "0" : shopQrcodeTemp.getTextY());
    	shopQrcodeTemp.setTextHeight(StringUtils.isEmpty(shopQrcodeTemp.getTextHeight())? "0" : shopQrcodeTemp.getTextHeight());
    	shopQrcodeTemp.setTextWidth(StringUtils.isEmpty(shopQrcodeTemp.getTextWidth())? "0" : shopQrcodeTemp.getTextWidth());
    	shopQrcodeTemp.setTextSize(StringUtils.isEmpty(shopQrcodeTemp.getTextSize())? "0" : shopQrcodeTemp.getTextSize());
    	shopQrcodeTemp.setTextType(StringUtils.isEmpty(shopQrcodeTemp.getTextType())? "0" : shopQrcodeTemp.getTextType());
    	shopQrcodeTemp.setHeadX(StringUtils.isEmpty(shopQrcodeTemp.getHeadX())? "0" : shopQrcodeTemp.getHeadX());
    	shopQrcodeTemp.setHeadY(StringUtils.isEmpty(shopQrcodeTemp.getHeadY())? "0" : shopQrcodeTemp.getHeadY());
    	shopQrcodeTemp.setHeadHeight(StringUtils.isEmpty(shopQrcodeTemp.getHeadHeight())? "0" : shopQrcodeTemp.getHeadHeight());
    	shopQrcodeTemp.setHeadWidth(StringUtils.isEmpty(shopQrcodeTemp.getHeadWidth())? "0" : shopQrcodeTemp.getHeadWidth());
    	shopQrcodeTemp.setNickX(StringUtils.isEmpty(shopQrcodeTemp.getNickX())? "0" : shopQrcodeTemp.getNickX());
    	shopQrcodeTemp.setNickY(StringUtils.isEmpty(shopQrcodeTemp.getNickY())? "0" : shopQrcodeTemp.getNickY());
    	shopQrcodeTemp.setNickHeight(StringUtils.isEmpty(shopQrcodeTemp.getNickHeight())? "0" : shopQrcodeTemp.getNickHeight());
    	shopQrcodeTemp.setNickWidth(StringUtils.isEmpty(shopQrcodeTemp.getNickWidth())? "0" : shopQrcodeTemp.getNickWidth());
    	
    	String bgImageUrl=Global.getUserfilesBaseDir() +shopQrcodeTemp.getBgimageUrl();
		String qrimageUrl= GenerateQRcodeVo.backgroundQRcode(tempVersion, menberId,imageBean,bgImageUrl);
		qrimageUrl=qrimageUrl.replace(Global.getConfig("qrCodePath"), "") ; 
		shopQrcodeTemp.setQrimageUrl(qrimageUrl);
		super.save(shopQrcodeTemp);
	}
	
	//获取背景二维码的图片的路径
	@Transactional(readOnly = false)
	public String getBgImageQrcode(ShopQrcodeTemp shopQrcodeTemp,String menberId,String headUrl,String nickName,String tempVersion ) {

		ImageBean imageBean =new ImageBean();
		imageBean.setImageHeight(Integer.parseInt(shopQrcodeTemp.getQrcodeHeight() == null ? "0" : shopQrcodeTemp.getQrcodeHeight()));
    	imageBean.setImageWidth(Integer.parseInt(shopQrcodeTemp.getQrcodeWidth() == null ? "0" :shopQrcodeTemp.getQrcodeWidth()));
    	imageBean.setImageX(Integer.parseInt(shopQrcodeTemp.getQrcodeX()== null? "0" : shopQrcodeTemp.getQrcodeX()));
    	imageBean.setImageY(Integer.parseInt(shopQrcodeTemp.getQrcodeY() == null? "0" : shopQrcodeTemp.getQrcodeY()));
    	if(!shopQrcodeTemp.getHeadWidth().equals(0)){
    		imageBean.setHeadUrl(headUrl);
        	imageBean.setHeadHeight(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadHeight())? "0" : shopQrcodeTemp.getHeadHeight()));
        	imageBean.setHeadWidth(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadWidth()) ? "0" :shopQrcodeTemp.getHeadWidth()));
        	imageBean.setHeadX(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadX())? "0" : shopQrcodeTemp.getHeadX()));
        	imageBean.setHeadY(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getHeadY())? "0" : shopQrcodeTemp.getHeadY()));
    	}
    	if(!shopQrcodeTemp.getNickWidth().equals(0)){
    		imageBean.setNickName(nickName);
        	imageBean.setNickHeight(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickHeight())? "0" : shopQrcodeTemp.getNickHeight()));
        	imageBean.setNickWidth(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickWidth()) ? "0" :shopQrcodeTemp.getNickWidth()));
        	imageBean.setNickX(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickX())? "0" : shopQrcodeTemp.getNickX()));
        	imageBean.setNickY(Integer.parseInt(StringUtils.isEmpty(shopQrcodeTemp.getNickY())? "0" : shopQrcodeTemp.getNickY()));
    	}
    		
    	String bgImageUrl=Global.getUserfilesBaseDir() +shopQrcodeTemp.getBgimageUrl();
    	String qrimageUrl= GenerateQRcodeVo.backgroundQRcode(tempVersion, menberId,imageBean,bgImageUrl);
    	qrimageUrl=qrimageUrl.replace(Global.getConfig("qrCodePath"), "") ; 
    	return qrimageUrl;
	}

	//获取最大值
	public int getMaxId(){
		return shopQrcodeTempDao.getMaxId();
	}
}
