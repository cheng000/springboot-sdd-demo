package com.example.demo.service.impl;

import com.example.demo.common.api.CommonResult;
import com.example.demo.common.api.ResultCode;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.page.PageResult;
import com.example.demo.convert.UserConvert;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserQueryDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户 Service 实现（只做业务逻辑）
 * <p>
 * 分层职责：
 * <ol>
 *   <li>调用 mapper 完成数据读写</li>
 *   <li>处理业务规则（如：用户名不能重复、用户是否存在等），违规时抛 {@link BusinessException}</li>
 *   <li>用 {@link UserConvert} 在 PO / DTO 间转换</li>
 *   <li>包装为 {@link CommonResult} 返回给 Controller</li>
 * </ol>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserConvert userConvert;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Long> create(UserDTO dto) {
        // 1. 业务规则：用户名不能重复
        if (userMapper.countByUsername(dto.getUsername()) > 0) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }
        // 2. DTO -> PO
        User user = userConvert.toEntity(dto);
        // 3. 写入数据库
        userMapper.insert(user);
        log.info("[新增用户] id={}, username={}", user.getId(), user.getUsername());
        return CommonResult.ok(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Void> update(UserDTO dto) {
        // 1. 业务规则：用户必须存在
        User existing = userMapper.selectById(dto.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 2. 如果改了用户名，校验是否冲突
        if (!existing.getUsername().equals(dto.getUsername())
                && userMapper.countByUsername(dto.getUsername()) > 0) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }
        // 3. 合并字段并更新
        userConvert.updateEntity(dto, existing);
        userMapper.updateById(existing);
        log.info("[更新用户] id={}", existing.getId());
        return CommonResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Void> deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户 ID 非法");
        }
        if (userMapper.selectById(id) == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        userMapper.deleteById(id);
        log.info("[删除用户] id={}", id);
        return CommonResult.ok();
    }

    @Override
    public CommonResult<UserDTO> getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return CommonResult.ok(userConvert.toDTO(user));
    }

    @Override
    public CommonResult<PageResult<UserDTO>> getPage(UserQueryDTO query) {
        long total = userMapper.selectPageCount(query.getUsername(), query.getEmail(), query.getStatus());
        if (total == 0L) {
            return CommonResult.ok(PageResult.of(query.getPageNum(), query.getPageSize(), 0L, null));
        }
        List<User> users = userMapper.selectPage(
                query.getOffset(), query.getPageSize(),
                query.getUsername(), query.getEmail(), query.getStatus());
        List<UserDTO> dtoList = userConvert.toDTOList(users);
        PageResult<UserDTO> page = PageResult.of(query.getPageNum(), query.getPageSize(), total, dtoList);
        return CommonResult.ok(page);
    }
}
