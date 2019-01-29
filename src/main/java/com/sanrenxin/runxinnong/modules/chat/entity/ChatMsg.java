package com.sanrenxin.runxinnong.modules.chat.entity;

import com.sanrenxin.runxinnong.common.constant.Constant;
import com.sanrenxin.runxinnong.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

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

    private static final String successCode = "0";
    private static final String errorCode = "-1";

    /**
     * 返回状态码 0：成功，-1失败
     */
    private String code;

    /**
     * 消息类型 guest_send:顾客端发送消息，cuestom_send:客服端发送消息
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
    private String sendToSessionID;

    /**
     * 消息时间
     */
    private String dateTime;

    public static ChatMsg successMsg(String msg){
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setDateTime(DateUtils.getDateTime());
        chatMsg.setMsg(successCode);
        return chatMsg;
    }

    public static ChatMsg errorMsg(String msg){
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setDateTime(DateUtils.getDateTime());
        chatMsg.setMsg(errorCode);
        return chatMsg;
    }
}
