package com.example.demo.common.api;

/**
 * 统一状态码枚举
 * <p>
 * 业界推荐编码规范：
 * - 成功：20000 段
 * - 通用错误：40000 / 50000 段
 * - 业务错误：按模块自定义（如 1xxxx 用户模块、2xxxx 订单模块）
 */
public enum ResultCode {

    SUCCESS(20000, "成功"),

    // ---------- 通用错误 4xxxx ----------
    BAD_REQUEST(40000, "请求参数错误"),
    PARAM_VALIDATE_FAILED(40001, "参数校验失败"),
    UNAUTHORIZED(40100, "未登录或登录已过期"),
    FORBIDDEN(40300, "无权限访问"),
    NOT_FOUND(40400, "资源不存在"),
    METHOD_NOT_ALLOWED(40500, "请求方法不支持"),
    REQUEST_BODY_NOT_READABLE(40002, "请求体格式错误"),

    // ---------- 服务器错误 5xxxx ----------
    INTERNAL_SERVER_ERROR(50000, "服务器内部错误"),

    // ---------- 用户模块 1xxxx ----------
    USER_NOT_FOUND(10001, "用户不存在"),
    USER_ALREADY_EXISTS(10002, "用户名已被占用"),
    USER_NAME_OR_PASSWORD_ERROR(10003, "用户名或密码错误");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return code;
    }

    public String message() {
        return message;
    }
}
