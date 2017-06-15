package com.db.hackaton.service;

import com.db.hackaton.service.dto.GroupsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Groups.
 */
public interface GroupsService {

    /**
     * Save a groups.
     *
     * @param groupsDTO the entity to save
     * @return the persisted entity
     */
    GroupsDTO save(GroupsDTO groupsDTO);

    /**
     *  Get all the groups.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GroupsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" groups.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GroupsDTO findOne(Long id);

    /**
     *  Delete the "id" groups.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the groups corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GroupsDTO> search(String query, Pageable pageable);
}
