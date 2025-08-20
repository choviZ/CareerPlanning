package com.zcw.cpbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 *
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    /**
     * 成功响应
     *
     * @param data    数据
     * @param message 消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success(T data,String message) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 成功响应
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }
    
    /**
     * 失败响应
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    /**
     * 失败响应
     *
     * @param code    错误码
     * @param message 消息
     * @param <T>     数据类型
     * @return 响应结果
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

}