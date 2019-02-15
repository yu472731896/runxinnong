package com.sanrenxin.runxinnong.modules.chat.entity;

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
public class ChatInitMsg{

    /**
     * 返回状态码 0：成功，-1失败
     */
    private String code;

    /**
     * 消息类型 0：系统消息，1：普通消息，-1：无客服状态
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
