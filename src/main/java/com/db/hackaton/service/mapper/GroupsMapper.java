package com.db.hackaton.service.mapper;

import com.db.hackaton.domain.*;
import com.db.hackaton.service.dto.GroupsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Groups and its DTO GroupsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupsMapper extends EntityMapper <GroupsDTO, Groups> {
    
    
    default Groups fromId(Long id) {
        if (id == null) {
            return null;
        }
        Groups groups = new Groups();
        groups.setId(id);
        return groups;
    }
}
