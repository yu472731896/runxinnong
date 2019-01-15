package com.sanrenxin.runxinnong.modules.sys.utils;

import lombok.Data;

/**
 * 接口调用结果放回对象
 *
 * @author mh
 * @create 2019-01-11 17:17
 */
@Data
public class Result {

    /**
     * 是否成功标志
     */
    private boolean successFlag = true;

    /**
     * 消息体
     */
    private String message;

    /**
     * 返回数据内容
     */
    private int code;

    /**
     * 返回数据内容
     */
    private Object data;

    /**
     * 错误信息
     */
    public static Result error(String message){
        Result re = new Result();
        re.successFlag = false;
        re.message = message;
        re.code = -1;
        re.data = null;
        return re;
    }

    /**
     * 错误消息
     */
    public static Result error(int code,String message){
        Result re = new Result();
        re.successFlag = false;
        re.message = message;
        re.code = code;
        re.data = null;
        return re;
    }

    /**
     * 成功信息
     */
    public static Result success(String message){
        Result re = new Result();
        re.successFlag = true;
        re.message = message;
        re.code = 200;
        re.data = null;
        return re;
    }

    /**
     * 成功消息
     */
    public static Result success(String message,Object data){
        Result re = new Result();
        re.setSuccessFlag(true);
        re.setCode(200);
        re.setMessage(message);
        re.setData(data);
        return re;
    }

    /**
     * 成功消息
     */
    public static Result success(Object data){
        Result re = new Result();
        re.setSuccessFlag(true);
        re.setCode(200);
        re.setMessage("");
        re.setData(data);
        return re;
    }
}
