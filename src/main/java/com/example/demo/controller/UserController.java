package com.example.demo.controller;

import com.example.demo.common.api.CommonResult;
import com.example.demo.common.page.PageResult;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserQueryDTO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

/**
 * 用户 Controller
 * <p>
 * <b>分层职责（严格遵守）</b>：
 * <ul>
 *   <li>请求接收：定义 RESTful 接口</li>
 *   <li>参数校验：通过 {@code @Validated} + {@code @Valid} + 校验分组完成入参合法性校验</li>
 *   <li>权限校验：可在此处或拦截器 / AOP 中处理（本示例略）</li>
 *   <li><b>不含</b>任何业务逻辑、<b>不直接</b>调用 Mapper</li>
 * </ul>
 * Controller 不需要关心返回值包装，因为 Service 已经返回 {@link CommonResult}。
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * 新增用户
     */
    @PostMapping
    public CommonResult<Long> create(@RequestBody @Validated(UserDTO.Create.class) UserDTO dto) {
        return userService.create(dto);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public CommonResult<Void> update(@RequestBody @Validated(UserDTO.Update.class) UserDTO dto) {
        return userService.update(dto);
    }

    /**
     * 根据主键删除用户
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable("id") @Positive(message = "用户 ID 必须为正整数") Long id) {
        return userService.deleteById(id);
    }

    /**
     * 根据主键查询用户
     */
    @GetMapping("/{id}")
    public CommonResult<UserDTO> getById(@PathVariable("id") @Positive(message = "用户 ID 必须为正整数") Long id) {
        return userService.getById(id);
    }

    /**
     * 分页查询用户
     * <p>
     * 由于查询参数是简单类型（pageNum/pageSize/username/...），使用 {@code @Validated} 触发 PageQuery 上的校验。
     */
    @GetMapping
    public CommonResult<PageResult<UserDTO>> getPage(@Validated UserQueryDTO query) {
        return userService.getPage(query);
    }
}
