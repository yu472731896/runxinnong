package com.sanrenxin.runxinnong.modules.run.webF;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 聊天系统 - 顾客端
 * @author mh
 * @create 2018-12-18 15:54
 */
@Controller
@RequestMapping("${frontPath}")
@Slf4j
public class ChatGuestController {

    /**
     *
     * @return
     */
    @RequestMapping(value = "/chat")
    public String chat(){
        return "modules/chat/guestChat";
    }
}
