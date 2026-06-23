package com.example.demo.convert;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户对象转换器（MapStruct 编译期生成实现类）
 * <p>
 * 用途：在 entity (PO) 与 DTO 之间进行<b>类型安全、零反射</b>的转换，
 * 让 Service 层无感知地从 mapper 拿到的 PO 切换为对外的 DTO。
 * <p>
 * MapStruct 比 BeanUtils.copyProperties 性能更好、能在编译期发现字段不匹配问题。
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * PO -> DTO
     */
    UserDTO toDTO(User user);

    /**
     * PO 列表 -> DTO 列表
     */
    List<UserDTO> toDTOList(List<User> users);

    /**
     * DTO -> PO（新增场景：DTO 中没有 createTime/updateTime，交给 DB 处理）
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    User toEntity(UserDTO dto);

    /**
     * DTO -> PO（更新场景：把 DTO 中非空字段合并到已有 PO）
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    void updateEntity(UserDTO dto, @MappingTarget User user);
}
