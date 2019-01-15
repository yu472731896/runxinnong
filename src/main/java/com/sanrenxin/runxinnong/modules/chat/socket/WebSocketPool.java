package com.sanrenxin.runxinnong.modules.chat.socket;

import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sun.tools.internal.ws.resources.WebserviceapMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.method.P;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author mh
 * @create 2018-12-18 14:33
 */
@Slf4j
public class WebSocketPool {

    //保持连接的Map容器
    private static final Map<String, WebSocket> connections = new HashMap<String, WebSocket>(16);

    //向连接池中添加连接
    public static void addWebSocket(WebSocket webSocket){
        //添加连接
        log.info("user : " + webSocket.getSession().getId() + " join..");
        connections.put(webSocket.getSession().getId(), webSocket);
    }

    //获取所有的在线用户
    public static Set<String> getOnlineUser(){
        return connections.keySet();
    }

    public static void removeWebSocket(WebSocket webSocket){
        //移除连接
        log.info("user : " + webSocket.getSession().getId() + " exit..");
        connections.remove(webSocket.getSession().getId());
    }

    //给指定用户发送消息
    public static void sendMessageToSocket(String sessionId,String message){
        try {
            //向特定的用户发送数据
            log.info("send message to sessionId : " + sessionId + " ,message content : " + message);
            WebSocket webSocket = connections.get(sessionId);
            if(null != webSocket){
                webSocket.sendMessage(webSocket.getSession(),message);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("webSocket sendMessageToSocket 异常:"+e.toString());
        }
    }

    //向所有用户发送消息
    public static void sendMessage(String message){
        try {
            Set<String> keySet = connections.keySet();
            for(String key : keySet){
                WebSocket webSocket = connections.get(key);
                if(null != webSocket){
                    log.info("send message to user : " + key + " ,message content : " + message);
                    webSocket.getSession().getBasicRemote().sendText(message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("向所有用户发送消息 method: sendMessage-->"+e.toString());
        }
    }

  /*  //客服人员
    public static boolean isCustomer(String socketId){
        WebSocket webSocket = connections.get(socketId);
        if(webSocket != null){
            if(webSocket.getCustomerId() != null && StringUtils.isNotBlank(webSocket.getCustomerId())){
                return true;
            }
        }
        return false;
    }

    //系统用户
    public static boolean isSysUser(String socketId){
        WebSocket webSocket = connections.get(socketId);
        if(webSocket != null){
            if(webSocket.getSysUserId() != null && StringUtils.isNotBlank(webSocket.getSysUserId())){
                return true;
            }
        }
        return false;
    }

    public static String getSysUserId(String socketId){
        WebSocket webSocket = connections.get(socketId);
        if(webSocket != null){
            return webSocket.getSysUserId();
        }
        return null;
    }

    public static String getSysUserName(String socketId){
        WebSocket webSocket = connections.get(socketId);
        if(webSocket != null){
            return webSocket.getSysUserId();
        }
        return null;
    }*/
}
