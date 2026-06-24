package com.example.demo.dto;

import com.example.demo.common.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页查询 DTO
 * <p>
 * 继承 {@link PageQuery} 自动拥有 {@code pageNum}/{@code pageSize} 字段。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "UserQueryDTO", description = "用户分页查询参数")
public class UserQueryDTO extends PageQuery {

    /** 用户名（模糊匹配） */
    @Schema(description = "用户名（模糊匹配）", example = "ali")
    private String username;

    /** 邮箱（精确匹配） */
    @Schema(description = "邮箱（精确匹配）", example = "alice@example.com")
    private String email;

    /** 状态：0-禁用，1-启用 */
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;
}
