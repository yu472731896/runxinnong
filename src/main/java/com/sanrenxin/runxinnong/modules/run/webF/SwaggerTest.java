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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userController")
@Api(tags = "swagger测试") //swagger分类标题注解 projectName/swagger-ui.html
public class SwaggerTest {

    /**
     * @param start
     * @param userName
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "个人信息")//swagger 当前接口注解
    @RequestMapping(value = "/listCompound", method = RequestMethod.POST)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "start", value = "start",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "名称模糊查询",required = false)
    })
    public Result listCompound(int start,String userName) {
        Result result = new Result();
        result.setCode(0);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("start",start);
        jsonObject.put("userName",userName);
        result.setData(jsonObject);
        return result;
    }
}