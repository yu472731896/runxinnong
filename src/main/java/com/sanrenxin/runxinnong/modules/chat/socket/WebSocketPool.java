package com.sanrenxin.runxinnong.modules.chat.socket;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.constant.Constant;
import com.sanrenxin.runxinnong.common.utils.CacheUtils;
import com.sanrenxin.runxinnong.modules.chat.entity.ChatMsg;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author mh
 * @create 2018-12-18 14:33
 */
@Slf4j
public class WebSocketPool implements java.io.Serializable {

    //保持连接的Map容器
    public static final Map<String, WebSocket> connections = new HashMap<String, WebSocket>(16);

    //向连接池中添加连接
    public static void addWebSocket(WebSocket webSocket){
        //添加连接
        log.info("user : " + webSocket.getSession().getId() + " join..");
        connections.put(webSocket.getSession().getId(), webSocket);
    }

    //向连接池中修改连接
    public static void updateWebSocket(WebSocket webSocket){
        //修改连接
        log.info("user : " + webSocket.getSession().getId() + " update..");
        connections.put(webSocket.getSession().getId(), webSocket);
    }

    /**
     * 移除连接
     * @param sessionId 会话id
     */
    public static void removeWebSocket(String sessionId){
        WebSocket webSocket = connections.get(sessionId);
        //移除连接
        removeUserInfo(webSocket);
        log.info("user:" + webSocket.getSession().getId() + ",name:" + webSocket.getName() + " exit..");
        connections.remove(sessionId);
    }

    /**
     * 根据sessionId 获取websocket
     * @param sessionId
     * @return websocket
     */
    public static WebSocket getWebSocketBySessionId(String sessionId){
        return connections.get(sessionId);
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
//                webSocket.sendMessage(webSocket.getSession(),message);
                webSocket.getSession().getAsyncRemote().sendText(message);
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

    /**
     * 获取所有的在线用户
     */
    public static Set<String> getOnlineUser(){
        return connections.keySet();
    }

    /**
     * 获取所有在线客服
     */
    public static Map<String, WebSocket> getAllOnlineCustomer(){
        return (Map<String, WebSocket>) CacheUtils.get(Constant.Chat.ONLINE_CUSTOM);
    }

    /**
     * 获取所有在线顾客
     */
    public static Map<String, WebSocket> getAllOnlineGuest(){
        return (Map<String, WebSocket>) CacheUtils.get(Constant.Chat.ONLINE_GUEST);
    }

    /**
     * 添加在线客服
     * @param webSocket 信息
     * @author mh
     * @create 2018-12-14 11:12
     */
    public static void addOnlineCustom(WebSocket webSocket){
        //在线表中添加客服信息（redis）
        Map<String, WebSocket> onlineCustomWebSocketMap = (Map<String, WebSocket>) CacheUtils.get(Constant.Chat.ONLINE_CUSTOM);
        if(onlineCustomWebSocketMap == null){
            onlineCustomWebSocketMap = new HashMap<String, WebSocket>();
        }
        if(!onlineCustomWebSocketMap.containsKey(webSocket.getSession().getId())){
            onlineCustomWebSocketMap.put(webSocket.getSession().getId(),webSocket);
        }
        CacheUtils.put(Constant.Chat.ONLINE_CUSTOM,onlineCustomWebSocketMap);
    }

    /**
     * 添加在线顾客
     * @param webSocket 信息
     * @author mh
     * @create 2018-12-14 11:12
     */
    public static void addOnlineGuest(WebSocket webSocket){
        //在线表中添加顾客信息（redis）
        Map<String, WebSocket> onlinGuestWebSocketMap = (Map<String, WebSocket>) CacheUtils.get(Constant.Chat.ONLINE_GUEST);
        if(onlinGuestWebSocketMap == null){
            onlinGuestWebSocketMap = new HashMap<String, WebSocket>();
        }
        if( !onlinGuestWebSocketMap.containsKey(webSocket.getSession().getId())){
            onlinGuestWebSocketMap.put(webSocket.getSession().getId(),webSocket);
        }
        CacheUtils.put(Constant.Chat.ONLINE_GUEST,onlinGuestWebSocketMap);
    }

    /**
     * 缓存中移除用户信息
     */
    private static void removeUserInfo(WebSocket webSocket){
        //移除连接
        if(webSocket.getUserType().equals("0")){//顾客
            Map<String, WebSocket> onlinGuestWebSocketMap = getAllOnlineGuest();
            onlinGuestWebSocketMap.remove(webSocket.getSession().getId());
        }else{
            Map<String, WebSocket> onlineCustomWebSocketMap = getAllOnlineCustomer();
            onlineCustomWebSocketMap.remove(webSocket.getSession().getId());
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
