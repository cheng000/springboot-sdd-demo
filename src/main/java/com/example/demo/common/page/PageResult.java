package com.example.demo.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 统一分页返回结果
 * <p>
 * 配合 {@link PageQuery} 使用，封装当前页数据、总数、总页数等分页信息。
 *
 * @param <T> 单条记录类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页码 */
    private Long pageNum;

    /** 每页大小 */
    private Long pageSize;

    /** 总记录数 */
    private Long total;

    /** 总页数 */
    private Long pages;

    /** 当前页数据 */
    private List<T> list;

    public static <T> PageResult<T> of(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        PageResult<T> result = new PageResult<>();
        result.pageNum = pageNum == null ? 1L : pageNum.longValue();
        result.pageSize = pageSize == null ? 10L : pageSize.longValue();
        result.total = total == null ? 0L : total;
        result.list = list == null ? Collections.emptyList() : list;
        result.pages = result.pageSize == 0 ? 0L : (result.total + result.pageSize - 1) / result.pageSize;
        return result;
    }

    /**
     * 列表元素类型转换，常用于 entity -> VO 场景
     */
    public <R> PageResult<R> map(Function<T, R> mapper) {
        List<R> mapped = list.stream().map(mapper).collect(Collectors.toList());
        PageResult<R> r = new PageResult<>();
        r.pageNum = this.pageNum;
        r.pageSize = this.pageSize;
        r.total = this.total;
        r.pages = this.pages;
        r.list = mapped;
        return r;
    }
}
