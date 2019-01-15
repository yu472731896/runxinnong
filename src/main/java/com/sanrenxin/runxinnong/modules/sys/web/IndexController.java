package com.sanrenxin.runxinnong.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sanrenxin.runxinnong.common.config.Global;
import com.sanrenxin.runxinnong.common.web.BaseController;
import com.sanrenxin.runxinnong.modules.run.entity.RunSlideShow;
import com.sanrenxin.runxinnong.modules.run.service.RunSlideShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 访问首页
 */
@Controller
@RequestMapping(value="${frontPath}")
public class IndexController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private RunSlideShowService slideShowService;

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(  Model model, HttpServletRequest request, HttpServletResponse response) {

        //目前未用到 前端访问地址：若为空进行登录判断
       // return "redirect:"+Global.getAdminPath()+"/login?repage";
        return list(request, response,model);
    }

    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String,Object> indexDate = new HashMap<String, Object>(16);
        try {

            //加载首页数据
            //轮播图
            List<RunSlideShow> slideShowList = slideShowService.findList(new RunSlideShow());



            indexDate.put("slideShowList",slideShowList);
            model.addAttribute("indexDate",indexDate);
            if (com.sanrenxin.runxinnong.common.utils.UserAgentUtils.isMobile(request) ) {
                return "redirect:" + Global.getFrontPath() + "/mobile";
            }
            model.addAttribute("cure", "custom");
            return "modules/runFront/index";
        } catch (Exception e) {
            log.error("");
            e.printStackTrace();
        }
        return "modules/run/frontError";
    }
}