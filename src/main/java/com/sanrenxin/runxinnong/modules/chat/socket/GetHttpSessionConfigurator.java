package com.sanrenxin.runxinnong.modules.chat.socket;

import com.sanrenxin.runxinnong.modules.sys.utils.UserUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 *  获取HttpSession
 * @author YMH
 */
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession=(HttpSession) request.getHttpSession();
        sec.getUserProperties().put("user", UserUtils.getUser());
        super.modifyHandshake(sec, request, response);

    }
}