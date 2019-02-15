package com.sanrenxin.runxinnong.modules.run.web;

import com.sanrenxin.runxinnong.common.constant.Constant;
import com.sanrenxin.runxinnong.common.utils.CacheUtils;
import com.sanrenxin.runxinnong.modules.chat.socket.WebSocket;
import com.sanrenxin.runxinnong.modules.sys.entity.User;
import com.sanrenxin.runxinnong.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 聊天系统 - 客服端
 * @author mh
 * @create 2018-12-18 15:52
 */
@Controller
@RequestMapping("${adminPath}/chat")
public class ChatSysUserController {

    /**
     * 客服人员页面跳转
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "")
    public String sysUserChat(HttpServletRequest request, HttpServletResponse response, Model model){
        String ipAddr = request.getRemoteAddr();
        model.addAttribute("ipAddr",ipAddr);
        return "modules/chat/customerChat";
    }

    /**
     * 查看当前在线客服和在线游客
     */
    @RequestMapping(value = "/getOnlineUserList")
    public String getOnlineUserList(Model model){
        //在线客服
        Map<String, WebSocket> onlineCustomWebSocketMap = (Map<String, WebSocket>) CacheUtils.get(Constant.Chat.ONLINE_CUSTOM);
        //在线游客
        Map<String, WebSocket> onlinGuestWebSocketMap = (Map<String, WebSocket>) CacheUtils.get(Constant.Chat.ONLINE_GUEST);
        model.addAttribute("onlineCustomWebSocketMap",onlineCustomWebSocketMap);
        model.addAttribute("onlinGuestWebSocketMap",onlinGuestWebSocketMap);
        return "modules/chat/onlineUserList";
    }
}
