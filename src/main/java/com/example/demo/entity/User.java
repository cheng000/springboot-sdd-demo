package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体（PO，Persistent Object）
 * <p>
 * 与数据库 {@code t_user} 表一一对应，仅用于 mapper 层的数据库读写。
 * <b>禁止</b>把它直接返回给 Controller / 前端，避免表结构泄漏。
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 年龄 */
    private Integer age;

    /** 状态：0-禁用，1-启用 */
    private Integer status;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
