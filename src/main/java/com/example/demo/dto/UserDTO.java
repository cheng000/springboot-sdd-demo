package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户 DTO（Data Transfer Object）
 * <p>
 * 根据需求采用「PO + DTO/VO 两层」策略，本类<b>同时承担请求接收与响应返回</b>职责：
 * <ul>
 *   <li>请求场景：使用 {@code @NotNull}/{@code @NotBlank} 等校验注解（Controller 层 {@code @Valid} 触发）</li>
 *   <li>响应场景：使用 {@code createTime}/{@code updateTime} 等只读字段</li>
 * </ul>
 * <p>
 * 在新增 / 更新 / 单条查询接口中复用同一个 DTO，最大限度减少 Bean 数量；
 * 当字段差异较大时（如新增不需要 id），通过校验注解的「分组校验」处理，详见 Controller。
 */
@Data
@Schema(name = "UserDTO", description = "用户传输对象（同时用于请求体与响应体）")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * <p>
     * 新增时为空，更新时必填（通过 {@code Update.class} 分组校验）。
     */
    @Schema(description = "用户 ID（新增时不传，修改时必填）", example = "1")
    @NotNull(message = "用户 ID 不能为空", groups = Update.class)
    private Long id;

    /** 用户名 */
    @Schema(description = "用户名", example = "alice")
    @NotBlank(message = "用户名不能为空", groups = {Create.class, Update.class})
    @Size(min = 3, max = 32, message = "用户名长度需在 3~32 之间", groups = {Create.class, Update.class})
    private String username;

    /** 邮箱 */
    @Schema(description = "邮箱", example = "alice@example.com")
    @NotBlank(message = "邮箱不能为空", groups = {Create.class, Update.class})
    @Email(message = "邮箱格式不正确", groups = {Create.class, Update.class})
    private String email;

    /** 手机号 */
    @Schema(description = "手机号", example = "13800000001")
    @Size(max = 20, message = "手机号长度不能超过 20", groups = {Create.class, Update.class})
    private String phone;

    /** 年龄 */
    @Schema(description = "年龄", example = "25")
    @Min(value = 0, message = "年龄不能小于 0", groups = {Create.class, Update.class})
    @Max(value = 150, message = "年龄不能大于 150", groups = {Create.class, Update.class})
    private Integer age;

    /** 状态：0-禁用，1-启用 */
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    /** 创建时间（响应时返回） */
    @Schema(description = "创建时间（响应返回，请求时忽略）", example = "2025-01-01 12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createTime;

    /** 更新时间（响应时返回） */
    @Schema(description = "更新时间（响应返回，请求时忽略）", example = "2025-01-01 12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private Date updateTime;

    /** 校验分组：新增 */
    public interface Create {
    }

    /** 校验分组：更新 */
    public interface Update {
    }
}
