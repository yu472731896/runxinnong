package com.sanrenxin.runxinnong.common.constant;

/**
 * @author mh
 * @create 2018-12-20 18:32
 */
public class Constant {

    public static class Chat{
        private Chat(){}

        /**
         * 0：成功，-1失败
         */
        public static final String CODE_SUCCESS = "0";
        public static final String CODE_ERROR = "-1";

        /**
         * 消息类型
         *
         * 0：系统消息，1：普通消息，-1：无客服状态,
         * guest_send:顾客端发送消息，cuestom_send:客服端发送消息
         */
        public static final String TYPE_SYS = "sys";
        public static final String TYPE_COMMON = "1";
        public static final String TYPE_NO_CUSTOMER = "-1";
        public static final String TYPE_GUEST_SEND = "guest_send";
        public static final String TYPE_CUSTOMER_SEND = "customer_send";
        public static final String TYPE_GUEST_JOIN = "guest_join";
        public static final String TYPE_CUESTOMER_JOIN = "customer_join";
        public static final String TYPE_guest_offline = "guest_offline";

        /**
         * 顾客类型
         */
        public static final String USER_TYPE_GUEST = "0";
        /**
         * 客服类型
         */
        public static final String  USER_TYPE_CUSTOMER = "1";
        /**
         * 在线客服缓存key
         */
        public static final String ONLINE_CUSTOM = "ONLINE_CUSTOM_SESSIONID";
        /**
         * 在线顾客缓存key
         */
        public static final String ONLINE_GUEST = "ONLINE_GUEST";
        /**
         * 登录的系统用户名称
         */
        public static final String SYS_USER= "sysUser";
        /**
         * 当前登录的sessionId
         */
        public static final String SESSION_ID= "sessionId";
        /**
         * 创建时间
         */
        public static final String CREATE_TIME= "createTime";
    }
}
