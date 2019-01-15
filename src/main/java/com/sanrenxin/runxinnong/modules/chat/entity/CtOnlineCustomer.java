package com.sanrenxin.runxinnong.modules.chat.entity;

import java.util.Date;

/**
 *  客服（在线）
 * @author mh
 */
public class CtOnlineCustomer {
    /**
     * 客服id
     */
    private Long customerId;

    /**
     * 客服线程编号
     */
    private String inboundId;

    /**
     * 客服用户id
     */
    private Long userId;

    /**
     * 客服浏览器session
     */
    private String sessionId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createtime;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getInboundId() {
        return inboundId;
    }

    public void setInboundId(String inboundId) {
        this.inboundId = inboundId == null ? null : inboundId.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}