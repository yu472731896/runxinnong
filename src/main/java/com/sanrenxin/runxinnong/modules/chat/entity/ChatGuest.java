package com.sanrenxin.runxinnong.modules.chat.entity;

import java.util.Date;

/**
 *  游客
 * @author mh
 */
public class ChatGuest {
    /**
     * id
     */
    private Long customerGuestId;

    /**
     * 客服线程编号
     */
    private String cInboundId;

    /**
     * 访客线程编号
     */
    private String gInboundId;

    /**
     * 客服用户id
     */
    private Long customerId;

    /**
     * 访客用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createtime;

    public Long getCustomerGuestId() {
        return customerGuestId;
    }

    public void setCustomerGuestId(Long customerGuestId) {
        this.customerGuestId = customerGuestId;
    }

    public String getcInboundId() {
        return cInboundId;
    }

    public void setcInboundId(String cInboundId) {
        this.cInboundId = cInboundId == null ? null : cInboundId.trim();
    }

    public String getgInboundId() {
        return gInboundId;
    }

    public void setgInboundId(String gInboundId) {
        this.gInboundId = gInboundId == null ? null : gInboundId.trim();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}