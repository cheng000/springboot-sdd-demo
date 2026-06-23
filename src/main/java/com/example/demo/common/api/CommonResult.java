package com.example.demo.common.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果
 * <p>
 * 用于 service -> controller、controller -> 前端 的统一数据载体。
 * <p>
 * 设计要点：
 * 1. 静态工厂方法（ok / failed）使调用更简洁、语义更清晰；
 * 2. 实现序列化便于 RPC / 缓存场景；
 * 3. 泛型 {@code <T>} 支持任意业务数据载荷。
 *
 * @param <T> 业务数据类型
 */
@Data
@NoArgsConstructor
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务状态码 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 业务数据 */
    private T data;

    /** 时间戳 */
    private Long timestamp;

    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> CommonResult<T> ok() {
        return new CommonResult<>(ResultCode.SUCCESS.code(), ResultCode.SUCCESS.message(), null);
    }

    public static <T> CommonResult<T> ok(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.code(), ResultCode.SUCCESS.message(), data);
    }

    public static <T> CommonResult<T> ok(String message, T data) {
        return new CommonResult<>(ResultCode.SUCCESS.code(), message, data);
    }

    public static <T> CommonResult<T> failed(ResultCode resultCode) {
        return new CommonResult<>(resultCode.code(), resultCode.message(), null);
    }

    public static <T> CommonResult<T> failed(ResultCode resultCode, String message) {
        return new CommonResult<>(resultCode.code(), message, null);
    }

    public static <T> CommonResult<T> failed(Integer code, String message) {
        return new CommonResult<>(code, message, null);
    }

    /** 是否成功 */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.code().equals(this.code);
    }
}
