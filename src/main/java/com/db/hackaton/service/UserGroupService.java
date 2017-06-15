package com.db.hackaton.service;

import com.db.hackaton.service.dto.UserGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing UserGroup.
 */
public interface UserGroupService {

    /**
     * Save a userGroup.
     *
     * @param userGroupDTO the entity to save
     * @return the persisted entity
     */
    UserGroupDTO save(UserGroupDTO userGroupDTO);

    /**
     *  Get all the userGroups.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserGroupDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" userGroup.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserGroupDTO findOne(Long id);

    /**
     *  Delete the "id" userGroup.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userGroup corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserGroupDTO> search(String query, Pageable pageable);
}
