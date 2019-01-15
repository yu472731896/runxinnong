package com.sanrenxin.runxinnong.modules.run.webF;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mh
 * @create 2018-12-18 15:54
 */
@Controller
@RequestMapping("${frontPath}")
public class ChatCustomerController {

    @RequestMapping(value = "/chat")
    public String chat(){

        return "modules/chat/customerChat";
    }
}
