package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper（仅做数据处理层）
 * <p>
 * 设计要点：
 * <ul>
 *   <li>仅与数据库交互，返回 / 接收的都是 PO 实体，<b>禁止出现 DTO/VO</b></li>
 *   <li>不写业务逻辑，不做参数业务校验</li>
 *   <li>SQL 统一写在 {@code resources/mapper/UserMapper.xml}</li>
 * </ul>
 */
public interface UserMapper {

    /**
     * 根据主键查询
     */
    User selectById(@Param("id") Long id);

    /**
     * 条件分页查询
     *
     * @param offset   偏移量（由 PageQuery.getOffset() 计算）
     * @param pageSize 每页大小
     * @param username 用户名（模糊匹配，可空）
     * @param email    邮箱（精确匹配，可空）
     * @param status   状态，可空
     */
    List<User> selectPage(@Param("offset") Integer offset,
                          @Param("pageSize") Integer pageSize,
                          @Param("username") String username,
                          @Param("email") String email,
                          @Param("status") Integer status);

    /**
     * 条件总数（与 {@link #selectPage} 的 where 条件保持一致）
     */
    long selectPageCount(@Param("username") String username,
                         @Param("email") String email,
                         @Param("status") Integer status);

    /**
     * 新增用户（返回数据库生成的主键）
     */
    int insert(User user);

    /**
     * 根据主键更新（动态字段）
     */
    int updateById(User user);

    /**
     * 根据主键删除
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户名统计（用于判断用户名是否已存在）
     */
    long countByUsername(@Param("username") String username);
}
