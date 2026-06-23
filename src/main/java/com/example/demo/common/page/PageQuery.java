package com.example.demo.common.page;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 分页查询参数基类
 * <p>
 * 所有需要分页的查询 DTO（如 UserQueryDTO）继承该类即可拥有分页能力，
 * 避免在每个查询 DTO 中重复定义 pageNum/pageSize。
 */
@Data
public class PageQuery {

    /** 页码，从 1 开始 */
    @Min(value = 1, message = "页码最小为 1")
    private Integer pageNum = 1;

    /** 每页大小 */
    @Min(value = 1, message = "每页大小最小为 1")
    @Max(value = 100, message = "每页大小最大为 100")
    private Integer pageSize = 10;

    /**
     * 计算数据库 OFFSET 值
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
