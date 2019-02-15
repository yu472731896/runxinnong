package com.sanrenxin.runxinnong.modules.run.webF;

import com.sanrenxin.runxinnong.common.utils.AddressUtils;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public String chat(HttpServletRequest request, HttpServletResponse response, Model model){
        String ipAddr = StringUtils.getRemoteAddr(request);
        model.addAttribute("ipAddr",ipAddr);
        return "modules/chat/guestChat";
    }
}
