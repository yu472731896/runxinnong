package com.sanrenxin.runxinnong.modules.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

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
public class ChatInitMsg{

    /**
     * 返回状态码 0：成功，-1失败
     */
    private String code;

    /**
     * 消息类型 sys：系统消息，no_customer：无客服状态
     * 消息类型 guest_send:顾客端发送消息，customer_send:客服端发送消息,heart_connect:心跳,by_self:自己返回消息
     */
    private String type;

    /**
     * 消息
     */
    private String msg;

    /**
     * 游客sessionId
     * sendTo
     */
    private String guestSessionId;

    /**
     * 客服sessionId
     * from
     */
    private String customerSessionId;

    /**
     * 时间
     */
    private Date date;

    /**
     *  数据
     */
    private Object data;
}
