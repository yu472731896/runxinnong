package com.sanrenxin.runxinnong.common.web;

import com.sanrenxin.runxinnong.modules.sys.entity.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping(value="${adminPath}/ueditor")
@PropertySource("classpath:upload.properties")
public class UeditorController {

	 protected final Logger logger = LoggerFactory.getLogger(getClass());  
	 
	 @Resource
	 Environment env;
	  /**
	     * 注意RequestParam的value必须和ueditor.config.json中imageFieldName的值相同
	     *
	     * @param file
	     * @return
	     */
	    @ResponseBody
	    @RequestMapping(value = "/uploadFile")
	    public Map<String, String> uploadFile(@RequestParam(value = "upfile", required = false) MultipartFile file, HttpServletRequest request) {
	        Attachment attach = new Attachment();

	        //获取文件保存后面的动态路径
	        String backPath = this.getPath();

	        //服务器地址
	        StringBuilder serverPath = new StringBuilder();
	        serverPath.append("http://");
	        serverPath.append(request.getServerName()).append(":").append(request.getServerPort());
	        serverPath.append(request.getContextPath());
	        //文件保存的完整路径
	        String path = env.getProperty("fileBaseStoreDIR") + "/" + backPath;
	        //获取文件名
	        String fileName = file.getOriginalFilename();
	        //获取转换后的uuid文件名
	        String uuidFileName = this.getUUIDFileName(fileName);
	        //完善附件对象信息,如果需要设置
	        attach.setCreateTime(System.currentTimeMillis());
	        attach.setSize(file.getSize());
	        attach.setName(uuidFileName);
	        attach.setExt(this.getExtName(fileName, '.'));
	        attach.setRealPath(backPath + "/" + uuidFileName);


	        File targetFile = new File(path, uuidFileName);
	        //创建文件夹
	        if (!targetFile.exists()) {
	            targetFile.mkdirs();
	        }
	        Map<String, String> map = new HashMap<String, String>();
	        try {
	            //分装百度上传信息
	            file.transferTo(targetFile);
	            map.put("state", "SUCCESS");
	            map.put("url", serverPath+env.getProperty("downloadApi")+ uuidFileName);
	            map.put("title", "");
	            map.put("original", fileName);
	        } catch (Exception e) {
	        	logger.error("upload error:", e);
	            map.put("state", "上传失败");
	        }
	        return map;
	    }	
	    
	    /**
	     * download:附件下载. <br/>
	     *
	     * @param response
	     * @param name     唯一的文件名
	     * @throws IOException
	     */
	    @RequestMapping("/download")
	    public void download(HttpServletResponse response, String name) throws IOException {
	        if (StringUtils.hasText(name)) {
	            response.reset();
	            response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"), "iso-8859-1"));  //转码之后下载的文件名不会出现中文乱码
	            response.setContentType("application/octet-stream;charset=UTF-8");
	            String backPath = this.getPath();
	            //文件保存的完整路径
	            String path = env.getProperty("fileBaseStoreDIR") + "/" + backPath + "/" + name;
	            //读取文件
	            InputStream in = new FileInputStream(path);
	            OutputStream out = response.getOutputStream();
	            //写文件
	            int b;
	            while ((b = in.read()) != -1) {
	                out.write(b);
	            }
	            in.close();
	            out.close();
	        }
	    }

	    
	    /**
	     * 根据年月日生成文件
	     * @return
	     */
	    private String getPath() {
	        //获取年月日
	        Calendar a = Calendar.getInstance();
	        String year = String.valueOf(a.get(Calendar.YEAR));
	        String month = String.valueOf(a.get(Calendar.MONTH) + 1);
	        String day = String.valueOf(a.get(Calendar.DAY_OF_MONTH));
	        StringBuilder backPath = new StringBuilder(128);
	        backPath.append(year).append("/").append(month).append("/").append(day);
	        return "20170516";
	    }
	    
	    /**
	     * getUUIDFileName:把文件名转换成uuid表示，防止文件名上传重复. <br/>
	     *
	     * @param fileName 文件名
	     * @return
	     */
	    private String getUUIDFileName(String fileName) {
	        UUID uuid = UUID.randomUUID();
	        StringBuilder sb = new StringBuilder(100);
	        sb.append(uuid.toString()).append(".").append(this.getExtName(fileName, '.'));
	        return sb.toString();
	    }
	    
	    /**
	     * getExtName:获取文件后缀名. <br/>
	     *
	     * @param s     文件名包括后缀
	     * @param split 文件名和后缀之间的‘.’
	     * @return
	     */
	    private String getExtName(String s, char split) {
	        int i = s.lastIndexOf(split);
	        int leg = s.length();
	        return i > 0 ? (i + 1) == leg ? " " : s.substring(i + 1, s.length()) : " ";
	    }
}
