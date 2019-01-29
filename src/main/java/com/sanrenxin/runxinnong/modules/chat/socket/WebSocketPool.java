package com.sanrenxin.runxinnong.modules.chat.socket;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.constant.Constant;
import com.sanrenxin.runxinnong.common.utils.JedisUtils;
import com.sanrenxin.runxinnong.modules.chat.entity.ChatMsg;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mh
 * @create 2018-12-18 14:33
 */
@Slf4j
public class WebSocketPool {

    //保持连接的Map容器
    public static final Map<String, WebSocket> connections = new HashMap<String, WebSocket>(16);

    //向连接池中添加连接
    public static void addWebSocket(WebSocket webSocket){
        //添加连接
        log.info("user : " + webSocket.getSession().getId() + " join..");
        connections.put(webSocket.getSession().getId(), webSocket);
    }

    /**
     * 获取所有的在线用户
     */
    public static Set<String> getOnlineUser(){
        return connections.keySet();
    }

    /**
     * 获取所有在线客服
     */
    public static List<String> getAllOnlineCustom(){
        return  JedisUtils.getList(Constant.Chat.ONLINE_CUSTOM);
    }

    /**
     * 获取所有在线顾客
     */
    public static List<String> getAllOnlineGuest(){
        return  JedisUtils.getList(Constant.Chat.ONLINE_GUEST);
    }

    /**
     * 移除连接
     * @param webSocket
     */
    public static void removeWebSocket(WebSocket webSocket){
        //移除连接
        log.info("user : " + webSocket.getSession().getId() + " exit..");
        connections.remove(webSocket.getSession().getId());
    }

    /**
     * 给指定用户发送消息
     * @param sessionId 指定用户的sessionId
     * @param message 消息
     */
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

    /**
     * 给指定用户发送消息
     * @param sessionId 指定用户的sessionId
     * @param chatMsg 消息类
     */
    public static void sendMessageToSocket(String sessionId, ChatMsg chatMsg){
        try {
            //向特定的用户发送数据
            log.info("send message to sessionId : " + sessionId + " ,message content : " + chatMsg);
            WebSocket webSocket = connections.get(sessionId);
            if(null != webSocket){
                webSocket.sendMessage(webSocket.getSession(), JSONObject.toJSONString(chatMsg));
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
