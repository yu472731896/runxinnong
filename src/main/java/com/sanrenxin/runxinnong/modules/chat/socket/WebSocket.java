package com.sanrenxin.runxinnong.modules.chat.socket;

import com.alibaba.fastjson.JSON;
import com.sanrenxin.runxinnong.common.constant.Constant;
import com.sanrenxin.runxinnong.common.utils.DateUtils;
import com.sanrenxin.runxinnong.common.utils.IdGen;
import com.sanrenxin.runxinnong.common.utils.JedisUtils;
import com.sanrenxin.runxinnong.common.utils.StringUtils;
import com.sanrenxin.runxinnong.modules.sys.entity.User;
import com.sanrenxin.runxinnong.modules.sys.utils.UserUtils;
import groovy.util.logging.Slf4j;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mh
 * @create 2018-12-14 11:12
 */
@Slf4j
@ServerEndpoint(value = "/websocket/{userType}")
public class WebSocket {

    private final static AtomicInteger guestIds = new AtomicInteger(1);

    @Getter
    @Setter
    private Session session;

    @Getter
    @Setter
    private String userType;

    //登录的系统用户名称
    private final static String SYS_USER= "sysUser";
    //当前登录的sessionId
    private final static String SESSION_ID= "sessionId";
    //创建时间
    private final static String CREATE_TIME= "createTime";

    /**
     * 连接成功调用的方法
     * @param session
     * 参数可选
     * ip，时间
     */
    @OnOpen
    public void onOpen(Session session,@PathParam(value="userType") String userType) {
        this.session = session;
        this.userType = userType;
        WebSocketPool.addWebSocket(this);

        //判断连接用户类型
        //客服人员
        if(Constant.Chat.USER_TYPE_SYSUSER.equals(userType)){
            //在线表中添加客服信息（redis）
//            JedisUtils.set();
            //通过userId 获取客服信息
            User user = UserUtils.getUser();

            Map<String,Object> resultMap = new HashMap<String, Object>(16);
            if(null != user && StringUtils.isNotBlank(user.getId())){
                resultMap.put(SYS_USER,user);
            }
            resultMap.put(SESSION_ID,session.getId());
            resultMap.put(CREATE_TIME,DateUtils.getDate());
            //获取当前建立对话游客用户列表

        }else{
            //随机指定一名在线客服人员接通连线


        }
    }

    /**
     *  连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

    }

    /**
     *  发生错误调用
     */
    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("发生错误:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     * @param session
     *            可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        // 群发消息
    }

    public void sendMessage(Session session,String message) throws IOException {
        session.getBasicRemote().sendText(message);
        this.session.getAsyncRemote().sendText(message);
    }
}
