/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.webF;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.run.entity.RunLeaveMessage;
import com.sanrenxin.runxinnong.modules.run.service.RunLeaveMessageService;
import com.sanrenxin.runxinnong.modules.sys.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 留言Controller
 * @author mingh
 * @version 2019-03-08
 */
@RestController
@Api(tags = "留言信息")
@RequestMapping(value = "${frontPath}/run/runLeaveMessage")
@Slf4j
public class FRunLeaveMessageController extends BaseController {

	@Autowired
	private RunLeaveMessageService runLeaveMessageService;


	@ApiOperation(value = "保存留言信息",httpMethod = "POST")
	@RequestMapping(value = "saveLeaveMessage",method = RequestMethod.POST)
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "name", value = "名称",dataType = "String",required = false,paramType = "query"),
			@ApiImplicitParam(name = "phoneNumber", value = "电话号码",required = false,dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "email", value = "邮箱",required = false,dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "address", value = "地址",required = false,dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "content", value = "内容",required = false,dataType = "String",paramType = "query")
	})
	public Result save(RunLeaveMessage runLeaveMessage) {
		Result result = null;
		try {
			if(null == runLeaveMessage){
				return Result.error("留言参数有误");
			}
			if(StringUtils.isBlank(runLeaveMessage.getPhoneNumber())){
				return Result.error("电话号码不能为空");
			}
			runLeaveMessageService.save(runLeaveMessage);
			result = Result.success("调用成功");
		}catch (Exception e){
			log.error("保存留言信息"+"异常："+e);
			result = Result.error("保存留言信息异常"+e.getMessage());
		}
		return result;
	}
}