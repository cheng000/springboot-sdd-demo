package com.example.demo.common.exception;

import com.example.demo.common.api.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 * <p>
 * service 层在遇到可预知的业务错误时抛出，由 {@link GlobalExceptionHandler} 统一拦截并返回友好提示。
 * <p>
 * 设计要点：使用 RuntimeException，避免在方法签名上强制声明 throws，保持业务代码整洁。
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 错误码 */
    private final Integer code;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.message());
        this.code = resultCode.code();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.code();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
