package com.sanrenxin.runxinnong.modules.chat.socket;

import com.alibaba.fastjson.JSONObject;
import com.sanrenxin.runxinnong.common.constant.Constant;
import com.sanrenxin.runxinnong.common.utils.AddressUtils;
import com.sanrenxin.runxinnong.common.utils.DateUtils;
import com.sanrenxin.runxinnong.common.utils.JedisUtils;
import com.sanrenxin.runxinnong.common.utils.SpringContextHolder;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.common.utils.WebSocketUtil;
import com.sanrenxin.runxinnong.modules.chat.entity.ChatInitMsg;
import com.sanrenxin.runxinnong.modules.chat.entity.ChatMsg;
import com.sanrenxin.runxinnong.modules.run.dao.ChatInfoDao;
import com.sanrenxin.runxinnong.modules.run.entity.ChatInfo;
import com.sanrenxin.runxinnong.modules.sys.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mh
 * @create 2018-12-14 11:12
 */
@ServerEndpoint(value = "/websocket/{userType}",configurator = GetHttpSessionConfigurator.class)
@Slf4j
public class WebSocket {

    private static ChatInfoDao chatInfoDao = SpringContextHolder.getBean(ChatInfoDao.class);
    private final static AtomicInteger GUEST_IDS = new AtomicInteger(1);

    /**
     * 连接成功调用的方法
     * @param session
     * 参数可选
     * ip，时间
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userType") String userType, EndpointConfig config) {
        this.session = session;
        this.userType = userType;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        openHandle(session,userType,httpSession);
        WebSocketPool.addWebSocket(this);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        ChatMsg receiveMsg = JSONObject.parseObject(message,ChatMsg.class);
        //判断消息类型  顾客或者客服类型
        if(receiveMsg.getType().equals(Constant.Chat.TYPE_GUEST_SEND) ||
                receiveMsg.getType().equals(Constant.Chat.TYPE_CUESTOM_SEND)){
            WebSocketPool.sendMessageToSocket(receiveMsg.getSendToSessionID(),ChatMsg.successMsg(receiveMsg.getMsg()));
        }
    }


    /**
     *  连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

        System.out.println("close.sessionId--->"+session.getId());
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
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 创建聊天处理
     *
     * 1、若成功 添加 guestid 和 customid
     *
     * @param session 当前session
     * @param userType 用户类型
     */
    private void openHandle(Session session,String userType,HttpSession httpSession){

        try {
            //判断连接用户类型
            //客服人员
            if(Constant.Chat.USER_TYPE_CUSTOMER.equals(userType)){
                //添加在线客服
                addOnlineCustom(session);

                // 获取客服信息
                User user = (User) session.getUserProperties().get("user");
                Map<String,Object> resultMap = new HashMap<String, Object>(16);
                if(null != user && StringUtils.isNotBlank(user.getId())){
                    resultMap.put(Constant.Chat.SYS_USER,user);
                }
                resultMap.put(Constant.Chat.SESSION_ID,session.getId());
                resultMap.put(Constant.Chat.CREATE_TIME,DateUtils.getDate());
                //获取当前所有在线游客列表
                List<String> allOnlineGuest = WebSocketPool.getAllOnlineGuest();

            }else{//顾客类型
                //添加在线顾客
                addOnlineGuest(session);

                //添加对话信息
                String IpAddress = WebSocketUtil.getRemoteAddress(session).toString();
                IpAddress = IpAddress.substring(0,IpAddress.indexOf(":"));
                String address = AddressUtils.getAddressByIp(IpAddress);
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setIpAddress(IpAddress);
                chatInfo.setAddress(address);
                chatInfoDao.insert(chatInfo);

                //随机指定一名在线客服人员接通连线
                List<String> onlineCustomSessionIdList = WebSocketPool.getAllOnlineCustom();
                if(onlineCustomSessionIdList != null && !onlineCustomSessionIdList.isEmpty()){
                    Random random = new Random();
                    String customSessionId = onlineCustomSessionIdList.get(random.nextInt());
                    //给当前客户发送消息
                    // xxxx 客服正在建立连接请稍等.....  发送等待状态，直到客服进入
                    ChatInitMsg msgToGuest = new ChatInitMsg();
                    msgToGuest.setCode("0");
                    msgToGuest.setType("0");
                    msgToGuest.setMsg("客服正在建立连接请稍等.....");
                    msgToGuest.setGuestSessionId(session.getId());
                    msgToGuest.setCustomSessionId(customSessionId);
                    WebSocketPool.sendMessageToSocket(session.getId(),JSONObject.toJSONString(msgToGuest));

                    //向指定的客服推送连接信息
                    ChatInitMsg msgToCustom = new ChatInitMsg();
                    msgToCustom.setCode(Constant.Chat.CODE_SUCCESS);
                    msgToCustom.setType(Constant.Chat.TYPE_SYS);
                    msgToCustom.setMsg("顾客进行呼叫.....");
                    msgToCustom.setGuestSessionId(session.getId());
                    msgToCustom.setCustomSessionId(customSessionId);
                    WebSocketPool.sendMessageToSocket(session.getId(),JSONObject.toJSONString(msgToCustom));
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
     * 添加在线客服
     * @param session 会话
     * @author mh
     * @create 2018-12-14 11:12
     */
    private void addOnlineCustom(Session session){
        //在线表中添加客服信息（redis）
        List<String> onlineCustomSessionIdList = JedisUtils.getList(Constant.Chat.ONLINE_CUSTOM);
        if(onlineCustomSessionIdList == null){
           onlineCustomSessionIdList = new ArrayList<String>();
        }
        if(!onlineCustomSessionIdList.contains(session.getId())){
            onlineCustomSessionIdList.add(session.getId().toString());
        }
        JedisUtils.setList(Constant.Chat.ONLINE_CUSTOM,onlineCustomSessionIdList,0);
    }

    /**
     * 添加在线顾客
     * @param session 会话
     * @author mh
     * @create 2018-12-14 11:12
     */
    private void addOnlineGuest(Session session){
        //在线表中添加顾客信息（redis）
        List<String> onlinGuestSessionIdList = JedisUtils.getList(Constant.Chat.ONLINE_GUEST);
        if(onlinGuestSessionIdList == null){
            onlinGuestSessionIdList = new ArrayList<String>();
        }
        if( !onlinGuestSessionIdList.contains(session.getId())){
            onlinGuestSessionIdList.add(session.getId());
        }
        JedisUtils.setList(Constant.Chat.ONLINE_GUEST,onlinGuestSessionIdList,0);
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

}
