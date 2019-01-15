/**
 * @润新农
 */
package com.sanrenxin.runxinnong.modules.run.webF;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.persistence.Page;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;
import com.sanrenxin.runxinnong.modules.run.service.RunSlideShowService;
import com.sanrenxin.runxinnong.modules.sys.entity.ResultBean;
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

/**
 * 轮播图Controller
 * @author minghui
 * @version 2018-10-25
 */
@Controller
@RequestMapping(value = "${frontPath}/run/runSlideShow")
public class FRunSlideShowController extends BaseController {

	@Autowired
	private RunSlideShowService runSlideShowService;
	
	@ModelAttribute
	public RunSlideShow get(@RequestParam(required=false) String id) {
		RunSlideShow entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = runSlideShowService.get(id);
		}
		if (entity == null){
			entity = new RunSlideShow();
		}
		return entity;
	}
	

	/**
	 * 轮播图接口
	 */
	@RequestMapping(value="getRunSlideShowList")
	@ResponseBody
	public ResultBean getRunSlideShowList(){
//		ResultBean resultBean = new ResultBean();
		List<RunSlideShow> runSlideShowList = runSlideShowService.findList(new RunSlideShow());
//		resultBean.setData(runSlideShowList);
		return ResultBean.getSuccess(runSlideShowList);
	}

}