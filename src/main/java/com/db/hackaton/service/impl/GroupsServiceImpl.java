package com.db.hackaton.service.impl;

import com.db.hackaton.domain.Groups;
import com.db.hackaton.repository.GroupsRepository;
import com.db.hackaton.repository.search.GroupsSearchRepository;
import com.db.hackaton.service.GroupsService;
import com.db.hackaton.service.dto.GroupsDTO;
import com.db.hackaton.service.mapper.GroupsMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Groups.
 */
@Service
@Transactional
public class GroupsServiceImpl implements GroupsService{

    private final Logger log = LoggerFactory.getLogger(GroupsServiceImpl.class);

    private final GroupsRepository groupsRepository;

    private final GroupsMapper groupsMapper;

    private final GroupsSearchRepository groupsSearchRepository;

    public GroupsServiceImpl(GroupsRepository groupsRepository, GroupsMapper groupsMapper, GroupsSearchRepository groupsSearchRepository) {
        this.groupsRepository = groupsRepository;
        this.groupsMapper = groupsMapper;
        this.groupsSearchRepository = groupsSearchRepository;
    }

    /**
     * Save a groups.
     *
     * @param groupsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupsDTO save(GroupsDTO groupsDTO) {
        log.debug("Request to save Groups : {}", groupsDTO);
        Groups groups = groupsMapper.toEntity(groupsDTO);
        groups = groupsRepository.save(groups);
        GroupsDTO result = groupsMapper.toDto(groups);
        groupsSearchRepository.save(groups);
        return result;
    }

    /**
     *  Get all the groups.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupsRepository.findAll(pageable)
            .map(groupsMapper::toDto);
    }

    /**
     *  Get one groups by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GroupsDTO findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        Groups groups = groupsRepository.findOne(id);
        return groupsMapper.toDto(groups);
    }

    /**
     *  Delete the  groups by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
        groupsSearchRepository.delete(id);
    }

    /**
     * Search for the groups corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Groups for query {}", query);
        Page<Groups> result = groupsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(groupsMapper::toDto);
    }

    @Override
    public List<GroupsDTO> findAll() {
        log.debug("Request to search for all of GroupsDTO");
        return groupsMapper.toDto(Lists.newArrayList(groupsRepository.findAll()));
    }
}
