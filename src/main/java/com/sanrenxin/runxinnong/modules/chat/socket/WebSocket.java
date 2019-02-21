package com.sanrenxin.runxinnong.modules.chat.socket;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.constant.Constant;
import com.sanrenxin.runxinnong.common.utils.AddressUtils;
import com.sanrenxin.runxinnong.common.utils.DateUtils;
import com.sanrenxin.runxinnong.common.utils.IdGen;
import com.sanrenxin.runxinnong.common.utils.RandomUtils;
import com.sanrenxin.runxinnong.common.utils.SpringContextHolder;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.modules.chat.entity.ChatInitMsg;
import com.sanrenxin.runxinnong.modules.chat.entity.ChatMsg;
import com.sanrenxin.runxinnong.modules.chat.entity.CtOnlineGuest;
import com.sanrenxin.runxinnong.modules.run.dao.ChatInfoDao;
import com.sanrenxin.runxinnong.modules.run.entity.ChatInfo;
import com.sanrenxin.runxinnong.modules.sys.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mh
 * @create 2018-12-14 11:12
 */
@ServerEndpoint(value = "/websocket/{userType}/{ipAddr}",configurator = GetHttpSessionConfigurator.class)
@Slf4j
public class WebSocket  implements Serializable {

    private static ChatInfoDao chatInfoDao = SpringContextHolder.getBean(ChatInfoDao.class);
    private final static AtomicInteger GUEST_IDS = new AtomicInteger(1);

    /**
     * 连接成功调用的方法
     * @param session 会话信息
     * @param userType  用户类型
     * @param ipAddr    ip地址
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userType") String userType,
                       @PathParam(value="ipAddr") String ipAddr,EndpointConfig config) {
        this.session = session;
        this.userType = userType;
        this.ipAddr = ipAddr;
        this.connectTime = new Date();

        //重复刷新页面的话，会重复添加sessionid， 隐藏地址栏，设置鼠标右键功能，关闭聊天窗口不断开会话直到关闭网页
        // TODO: 2019/2/14

        openHandle(session,userType,config,ipAddr);
        WebSocketPool.addWebSocket(this);
    }

    /**
     * 收到消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        ChatMsg receiveMsg = JSONObject.parseObject(message,ChatMsg.class);
        if(receiveMsg.getType().equals("heart_connect")){

        }else{
            //判断消息类型  顾客或者客服类型
            if(receiveMsg.getType().equals(Constant.Chat.TYPE_CUSTOMER_SEND)){
                //向游客发送消息
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setType("customer_send");
                chatMsg.setMsg(receiveMsg.getMsg());
                chatMsg.setDateTime(DateUtils.getDateTime());
                chatMsg.setCode("0");
                WebSocketPool.sendMessageToSocket(receiveMsg.getToSessionId(),JSONObject.toJSONString(chatMsg));

                //向自己发送放回消息
                ChatMsg chatMsgForSelf = new ChatMsg();
                chatMsgForSelf.setCode("0");
                chatMsgForSelf.setType("by_self");
                chatMsgForSelf.setDateTime(DateUtils.getDateTime());
                chatMsgForSelf.setToSessionId(receiveMsg.getToSessionId());
                chatMsgForSelf.setMsg(receiveMsg.getMsg());
                WebSocketPool.sendMessageToSocket(receiveMsg.getFromSessionId(),JSONObject.toJSONString(chatMsgForSelf));
            }else if(receiveMsg.getType().equals(Constant.Chat.TYPE_GUEST_SEND) ){
                //向客服发送消息
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setCode("0");
                chatMsg.setType("guest_send");
                chatMsg.setDateTime(DateUtils.getDateTime());
                chatMsg.setFromSessionId(receiveMsg.getFromSessionId());
                chatMsg.setMsg(receiveMsg.getMsg());
                WebSocketPool.sendMessageToSocket(receiveMsg.getToSessionId(),JSONObject.toJSONString(chatMsg));
                //向自己发送消息
                ChatMsg chatMsgForSelf = new ChatMsg();
                chatMsgForSelf.setCode(Constant.Chat.CODE_SUCCESS);
                chatMsgForSelf.setType("by_self");
                chatMsgForSelf.setMsg(receiveMsg.getMsg());
                chatMsgForSelf.setDateTime(DateUtils.getDateTime());
                WebSocketPool.sendMessageToSocket(receiveMsg.getFromSessionId(),JSONObject.toJSONString(chatMsgForSelf));
            }else if(receiveMsg.getType().equals(Constant.Chat.TYPE_guest_offline)){
                //向自己发送消息
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setType(Constant.Chat.TYPE_guest_offline);
                chatMsg.setDateTime(DateUtils.getDateTime());
                chatMsg.setFromSessionId(receiveMsg.getFromSessionId());
                WebSocketPool.sendMessageToSocket(receiveMsg.getToSessionId(),JSONObject.toJSONString(chatMsg));
            }
        }
    }

    /**
     *  连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //移除对应session信息
        WebSocketPool.removeWebSocket(session.getId());
    }

    /**
     *  发生错误调用
     */
    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("发生错误:"+error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(Session session,String message) throws IOException {
        session.getBasicRemote().sendText(message);
//        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 创建聊天处理
     *
     *  添加 guestid 和 customid
     *  @param session 当前session
     * @param userType 用户类型
     * @param ipAddr ip地址
     */
    private void openHandle(Session session, String userType, EndpointConfig config, String ipAddr){
        try {
            //判断连接用户类型
            if(Constant.Chat.USER_TYPE_CUSTOMER.equals(userType)){ //客服人员
                // 获取客服信息
                User user = (User) session.getUserProperties().get("user");
                this.id = user.getId();
                this.name = user.getName();
                //添加在线客服
                WebSocketPool.addOnlineCustom(this);
                WebSocketPool.updateWebSocket(this);

                ChatInitMsg initMsg = new ChatInitMsg();
                initMsg.setCustomerSessionId(session.getId());
                initMsg.setDate(new Date());
                //获取当前所有在线游客列表
                Map<String, WebSocket> allOnlineGuest = WebSocketPool.getAllOnlineGuest();
                if(allOnlineGuest != null && !allOnlineGuest.isEmpty()){
                    List<CtOnlineGuest> onlineGuestList = new ArrayList<>();
                    CtOnlineGuest onlineGuest = null;
                    for(String key : allOnlineGuest.keySet()){
                        onlineGuest = new CtOnlineGuest();
                        onlineGuest.setSessionId(allOnlineGuest.get(key).getSession().getId());
                        onlineGuest.setUserName(allOnlineGuest.get(key).getName());
                        onlineGuestList.add(onlineGuest);
                    }
                    initMsg.setData(onlineGuestList);
                }
                initMsg.setType("customer_init");
                initMsg.setCode("0");
                //将游客信息发送给客服
                WebSocketPool.sendMessageToSocket(session.getId(),JSONObject.toJSONString(initMsg));

            }else{//顾客类型
                //添加在线顾客
                WebSocketPool.addOnlineGuest(this);

                //添加对话信息,在客服进行对话时添加客服信息
                ChatInfo chatInfo = new ChatInfo();
                String address = "";
                if(StringUtils.isNotBlank(ipAddr)){
                    address = AddressUtils.getAddressByIp(ipAddr);
                    chatInfo.setIpAddress(ipAddr);
                    chatInfo.setAddress(address);
                }
                chatInfo.setGuestId(IdGen.uuid());
                chatInfo.setCreateDate(new Date());
                chatInfo.preInsert();
                chatInfoDao.insert(chatInfo);

                this.id = chatInfo.getGuestId();
                this.name = address + RandomStringUtils.randomNumeric(5);
                WebSocketPool.updateWebSocket(this);

                Map<String, WebSocket> onlineCustomMap = WebSocketPool.getAllOnlineCustomer();
                if(onlineCustomMap != null && !onlineCustomMap.isEmpty()){
                    //随机指定一名在线客服人员接通连线
                    WebSocket customerSocket = RandomUtils.getRandomValueFromMap(onlineCustomMap);
                    //给当前客户发送消息
                    // xxxx 客服正在建立连接请稍等.....  发送等待状态，直到客服进入
                    ChatInitMsg msgToGuest = new ChatInitMsg();
                    msgToGuest.setCode("0");
                    msgToGuest.setType(Constant.Chat.TYPE_SYS);
                    msgToGuest.setMsg("客服正在建立连接请稍等.....");
                    msgToGuest.setGuestSessionId(session.getId());
                    msgToGuest.setCustomerSessionId(customerSocket.getSession().getId());
                    msgToGuest.setDate(new Date());
                    WebSocketPool.sendMessageToSocket(session.getId(),JSONObject.toJSONString(msgToGuest));

                    //向指定的客服推送连接信息
                    ChatInitMsg msgToCustom = new ChatInitMsg();
                    msgToCustom.setCode(Constant.Chat.CODE_SUCCESS);
                    msgToCustom.setType(Constant.Chat.TYPE_GUEST_JOIN);
                    msgToCustom.setMsg("顾客进行呼叫.....");
                    msgToCustom.setGuestSessionId(session.getId());
                    msgToCustom.setCustomerSessionId(customerSocket.getSession().getId());

                    CtOnlineGuest onlineGuest = new CtOnlineGuest();
                    WebSocket webSocket = WebSocketPool.getWebSocketBySessionId(session.getId());
                    onlineGuest.setSessionId(session.getId());
                    onlineGuest.setUserName(webSocket.getName());

                    msgToCustom.setData(JSONObject.toJSON(onlineGuest));
                    WebSocketPool.sendMessageToSocket(customerSocket.getSession().getId(),JSONObject.toJSONString(msgToCustom));

                }else{//当前无客服在线
                    ChatInitMsg msgToGuest = new ChatInitMsg();
                    msgToGuest.setCode(Constant.Chat.CODE_SUCCESS);
                    msgToGuest.setType(Constant.Chat.TYPE_NO_CUSTOMER);
                    msgToGuest.setMsg("当前无客服在线");
                    msgToGuest.setGuestSessionId(session.getId());
                    WebSocketPool.sendMessageToSocket(session.getId(),JSONObject.toJSONString(msgToGuest));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("聊天对话创建异常："+e);
        }
    }


    /**
     * 用户连接的session
     */
    @Getter
    @Setter
    private Session session;

    /**
     * httpSessionId
     */
    @Getter
    @Setter
    private String httpSessionId;

    /**
     * 用户类型
     */
    @Getter
    @Setter
    private String userType;

    /**
     * 建立聊天id 双成功连接后设置
     */
    @Getter
    @Setter
    private String chatInfoId;

    /**
     * 用户名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 用户id
     */
    @Getter
    @Setter
    private String id;

    /**
     * 用户ip地址
     */
    @Getter
    @Setter
    private String ipAddr;

    /**
     * 连接时间
     */
    @Getter
    @Setter
    private Date connectTime;

}
