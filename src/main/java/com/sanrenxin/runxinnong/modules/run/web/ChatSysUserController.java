package com.sanrenxin.runxinnong.modules.run.web;

import com.sanrenxin.runxinnong.modules.sys.entity.User;
import com.sanrenxin.runxinnong.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 聊天系统 - 客服端
 * @author mh
 * @create 2018-12-18 15:52
 */
@Controller
@RequestMapping("${adminPath}")
public class ChatSysUserController {

    @RequestMapping(value = "/chat")
    public String sysUserChat(){

        User user = UserUtils.getUser();
        return "modules/chat/sysUserChat";
    }
}
