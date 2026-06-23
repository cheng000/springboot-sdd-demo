package com.example.demo.service;

import com.example.demo.common.api.CommonResult;
import com.example.demo.common.page.PageResult;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserQueryDTO;

/**
 * 用户 Service
 * <p>
 * 返回值约定（统一标准返回类）：
 * <ul>
 *   <li>mapper 返回 PO 给 service</li>
 *   <li>service 通过 {@link CommonResult}{@code <UserDTO>} 或 {@link CommonResult}{@code <}{@link PageResult}{@code <UserDTO>>} 返回给 controller</li>
 * </ul>
 * 这样 Controller 层只需 {@code return userService.xxx(...);} 即可，无需再做包装。
 */
public interface UserService {

    /**
     * 新增用户
     */
    CommonResult<Long> create(UserDTO dto);

    /**
     * 修改用户
     */
    CommonResult<Void> update(UserDTO dto);

    /**
     * 根据主键删除用户
     */
    CommonResult<Void> deleteById(Long id);

    /**
     * 根据主键查询用户
     */
    CommonResult<UserDTO> getById(Long id);

    /**
     * 分页查询
     */
    CommonResult<PageResult<UserDTO>> getPage(UserQueryDTO query);
}
