/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.webF;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.run.entity.RunNotice;
import com.sanrenxin.runxinnong.modules.run.service.RunNoticeService;
import com.sanrenxin.runxinnong.modules.sys.entity.ResultBean;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.List;
import java.util.Map;

/**
 * 最新通知Controller
 * @author mingh
 * @version 2018-11-11
 */
@Controller
@RequestMapping(value = "${frontPath}/run/runNotice")
public class FRunNoticeController extends BaseController {

	@Autowired
	private RunNoticeService runNoticeService;
	
	@ModelAttribute
	public RunNotice get(@RequestParam(required=false) String id) {
		RunNotice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = runNoticeService.get(id);
		}
		if (entity == null){
			entity = new RunNotice();
		}
		return entity;
	}

	/**
	 * 默认查询前十条（最新）
	 * @return
	 */
	@RequestMapping(value="getRunSlideShowList")
	@ResponseBody
	public ResultBean getRunNoticeList(HttpServletRequest request,HttpServletResponse response,RunNotice runNotice){
//		ResultBean resultBean = new ResultBean();
        Page<RunNotice> page = new Page<RunNotice>(0,10);
        page.setOrderBy("updateDate");
		Page<RunNotice> noticePage = runNoticeService.findPage(page, runNotice);

        return ResultBean.getSuccess(noticePage);
	}

}