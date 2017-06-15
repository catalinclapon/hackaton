package com.db.hackaton.service.impl;

import com.db.hackaton.service.UserGroupService;
import com.db.hackaton.domain.UserGroup;
import com.db.hackaton.repository.UserGroupRepository;
import com.db.hackaton.repository.search.UserGroupSearchRepository;
import com.db.hackaton.service.dto.UserGroupDTO;
import com.db.hackaton.service.mapper.UserGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserGroup.
 */
@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService{

    private final Logger log = LoggerFactory.getLogger(UserGroupServiceImpl.class);

    private final UserGroupRepository userGroupRepository;

    private final UserGroupMapper userGroupMapper;

    private final UserGroupSearchRepository userGroupSearchRepository;

    public UserGroupServiceImpl(UserGroupRepository userGroupRepository, UserGroupMapper userGroupMapper, UserGroupSearchRepository userGroupSearchRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userGroupMapper = userGroupMapper;
        this.userGroupSearchRepository = userGroupSearchRepository;
    }

    /**
     * Save a userGroup.
     *
     * @param userGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserGroupDTO save(UserGroupDTO userGroupDTO) {
        log.debug("Request to save UserGroup : {}", userGroupDTO);
        UserGroup userGroup = userGroupMapper.toEntity(userGroupDTO);
        userGroup = userGroupRepository.save(userGroup);
        UserGroupDTO result = userGroupMapper.toDto(userGroup);
        userGroupSearchRepository.save(userGroup);
        return result;
    }

    /**
     *  Get all the userGroups.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserGroups");
        return userGroupRepository.findAll(pageable)
            .map(userGroupMapper::toDto);
    }

    /**
     *  Get one userGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserGroupDTO findOne(Long id) {
        log.debug("Request to get UserGroup : {}", id);
        UserGroup userGroup = userGroupRepository.findOne(id);
        return userGroupMapper.toDto(userGroup);
    }

    /**
     *  Delete the  userGroup by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserGroup : {}", id);
        userGroupRepository.delete(id);
        userGroupSearchRepository.delete(id);
    }

    /**
     * Search for the userGroup corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserGroups for query {}", query);
        Page<UserGroup> result = userGroupSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(userGroupMapper::toDto);
    }
}
