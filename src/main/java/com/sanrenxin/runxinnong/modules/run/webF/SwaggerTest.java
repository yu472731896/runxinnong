package com.sanrenxin.runxinnong.modules.run.webF;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.modules.sys.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userController")
@Api(tags = "二：用户信息") //swagger分类标题注解
public class SwaggerTest {

    @RequestMapping(value = "/listCompound", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParam(value = "测试参数",name = "测试参数")
    @ApiOperation(httpMethod = "GET", value = "个人信息")//swagger 当前接口注解
    public String listCompound(
            @ApiParam(required = true, name = "start", value = "start") int start,
            int limit,
            @ApiParam(required = false, name = "userName", value = "名称模糊查询") String userName) {
        Result result = new Result();
        result.setCode(0);
        return JSONObject.toJSONString(result);
    }
}