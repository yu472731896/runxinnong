package com.sanrenxin.runxinnong.modules.chat.entity;

import java.util.Date;

public class CtUserMsg {
    /**
     * 信息id
     */
    private Long msgId;

    /**
     * 发送类型
     */
    private Integer sendType;

    /**
     * 后台客服用户id
     */
    private String sendUser;

    /**
     * 用户姓名
     */
    private String receiveUser;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 信息
     */
    private String chatMsg;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser == null ? null : sendUser.trim();
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser == null ? null : receiveUser.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg == null ? null : chatMsg.trim();
    }
}