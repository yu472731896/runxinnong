package com.sanrenxin.runxinnong.modules.run.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mh
 * @create 2018-12-18 15:52
 */
@Controller
@RequestMapping("${adminPath}")
public class ChatSysUserController {

    @RequestMapping(value = "/chat")
    public String sysUserChat(){
        return "modules/chat/sysUserChat";
    }
}
