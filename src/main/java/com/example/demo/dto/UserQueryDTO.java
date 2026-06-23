package com.example.demo.dto;

import com.example.demo.common.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页查询 DTO
 * <p>
 * 继承 {@link PageQuery} 自动拥有 {@code pageNum}/{@code pageSize} 字段。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends PageQuery {

    /** 用户名（模糊匹配） */
    private String username;

    /** 邮箱（精确匹配） */
    private String email;

    /** 状态：0-禁用，1-启用 */
    private Integer status;
}
