package com.example.demo.common.exception;

import com.example.demo.common.api.CommonResult;
import com.example.demo.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器（业界推荐的统一异常处理方案）
 * <p>
 * 通过 {@code @RestControllerAdvice} + {@code @ExceptionHandler} 拦截所有 Controller 抛出的异常，
 * 并转换为统一的 {@link CommonResult} 响应体。
 * <p>
 * 处理顺序（从具体到通用）：
 * <ol>
 *   <li>业务异常 {@link BusinessException}：返回业务码 + 业务提示信息</li>
 *   <li>参数校验异常：聚合所有字段错误信息后返回</li>
 *   <li>请求方法/参数/请求体异常：返回对应的 4xx 错误</li>
 *   <li>未知异常：兜底返回 500，不向前端暴露内部堆栈</li>
 * </ol>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResult<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("[业务异常] uri={}, code={}, msg={}", request.getRequestURI(), e.getCode(), e.getMessage());
        return CommonResult.failed(e.getCode(), e.getMessage());
    }

    /**
     * {@code @RequestBody} + {@code @Valid} 校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("[参数校验失败] {}", msg);
        return CommonResult.failed(ResultCode.PARAM_VALIDATE_FAILED, msg);
    }

    /**
     * {@code @ModelAttribute} + {@code @Valid} 校验失败（form / 表单）
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<Void> handleBindException(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("[参数绑定失败] {}", msg);
        return CommonResult.failed(ResultCode.PARAM_VALIDATE_FAILED, msg);
    }

    /**
     * {@code @RequestParam} / {@code @PathVariable} 校验失败（{@code @Validated} 标注在 Controller 上时）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult<Void> handleConstraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("[约束校验失败] {}", msg);
        return CommonResult.failed(ResultCode.PARAM_VALIDATE_FAILED, msg);
    }

    /**
     * 缺少必填请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResult<Void> handleMissingParam(MissingServletRequestParameterException e) {
        String msg = "缺少必填参数：" + e.getParameterName();
        log.warn("[缺少参数] {}", msg);
        return CommonResult.failed(ResultCode.BAD_REQUEST, msg);
    }

    /**
     * 请求体不可读（JSON 格式错误等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult<Void> handleMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("[请求体解析失败] {}", e.getMessage());
        return CommonResult.failed(ResultCode.REQUEST_BODY_NOT_READABLE);
    }

    /**
     * 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("[方法不支持] {}", e.getMessage());
        return CommonResult.failed(ResultCode.METHOD_NOT_ALLOWED, e.getMessage());
    }

    /**
     * 兜底：未知系统异常
     */
    @ExceptionHandler(Throwable.class)
    public CommonResult<Void> handleThrowable(Throwable e, HttpServletRequest request) {
        log.error("[系统异常] uri={}", request.getRequestURI(), e);
        return CommonResult.failed(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
