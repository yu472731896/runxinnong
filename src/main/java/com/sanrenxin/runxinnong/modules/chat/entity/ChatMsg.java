package com.sanrenxin.runxinnong.modules.chat.entity;

import com.sanrenxin.runxinnong.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 消息类
 *
 * @author mh
 * @create 2019-01-26 9:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMsg {

    private static final String SUCCESS_CODE = "0";
    private static final String ERROR_CODE = "-1";

    /**
     * 返回状态码 0：成功，-1失败
     */
    private String code;

    /**
     * 消息类型 guest_send:顾客端发送消息，customer_send:客服端发送消息,heart_connect:心跳,by_self:自己返回消息
     */
    private String type;

    /**
     * 消息
     */
    private String msg;

    /**
     * 发送给sessionId
     * sendTo
     */
    private String toSessionId;

    /**
     * 消息来自sessionId
     * sendTo
     */
    private String fromSessionId;

    /**
     * 消息时间
     */
    private String dateTime;

    public static ChatMsg successMsg(String msg){
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setDateTime(DateUtils.getDateTime());
        chatMsg.setCode(SUCCESS_CODE);
        chatMsg.setMsg(msg);
        return chatMsg;
    }

    public static ChatMsg errorMsg(String msg){
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setDateTime(DateUtils.getDateTime());
        chatMsg.setCode(ERROR_CODE);
        chatMsg.setMsg(msg);
        return chatMsg;
    }
}
