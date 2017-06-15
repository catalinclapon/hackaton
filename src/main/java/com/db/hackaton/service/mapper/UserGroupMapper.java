package com.db.hackaton.service.mapper;

import com.db.hackaton.domain.*;
import com.db.hackaton.service.dto.UserGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserGroup and its DTO UserGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, GroupsMapper.class, })
public interface UserGroupMapper extends EntityMapper <UserGroupDTO, UserGroup> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    UserGroupDTO toDto(UserGroup userGroup); 

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "groupId", target = "group")
    UserGroup toEntity(UserGroupDTO userGroupDTO); 
    default UserGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserGroup userGroup = new UserGroup();
        userGroup.setId(id);
        return userGroup;
    }
}
