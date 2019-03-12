package com.sanrenxin.runxinnong.modules.sys.listener;

import javax.servlet.ServletContext;

import com.sanrenxin.runxinnong.modules.wx.utils.TokenThread;
import org.springframework.web.context.WebApplicationContext;

import com.sanrenxin.runxinnong.modules.sys.service.SystemService;

public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {
	
	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		if (!SystemService.printKeyLoadMessage()){
			return null;
		}
		// 启动定时获取access_token的线程
		new Thread(new TokenThread()).start();
		return super.initWebApplicationContext(servletContext);
	}
}
