package com.sanrenxin.runxinnong.modules.chat.socket;

import com.sanrenxin.runxinnong.modules.sys.utils.UserUtils;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 *  获取HttpSession
 * @author YMH
 */
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request, HandshakeResponse response) {
        sec.getUserProperties().put("user", UserUtils.getUser());
        super.modifyHandshake(sec, request, response);

    }
}