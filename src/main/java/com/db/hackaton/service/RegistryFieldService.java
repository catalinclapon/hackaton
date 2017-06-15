package com.db.hackaton.service;

import com.db.hackaton.domain.RegistryField;
import com.db.hackaton.repository.RegistryFieldRepository;
import com.db.hackaton.repository.search.RegistryFieldSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RegistryField.
 */
@Service
@Transactional
public class RegistryFieldService {

    private final Logger log = LoggerFactory.getLogger(RegistryFieldService.class);
    
    private final RegistryFieldRepository registryFieldRepository;

    private final RegistryFieldSearchRepository registryFieldSearchRepository;

    public RegistryFieldService(RegistryFieldRepository registryFieldRepository, RegistryFieldSearchRepository registryFieldSearchRepository) {
        this.registryFieldRepository = registryFieldRepository;
        this.registryFieldSearchRepository = registryFieldSearchRepository;
    }

    /**
     * Save a registryField.
     *
     * @param registryField the entity to save
     * @return the persisted entity
     */
    public RegistryField save(RegistryField registryField) {
        log.debug("Request to save RegistryField : {}", registryField);
        RegistryField result = registryFieldRepository.save(registryField);
        registryFieldSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the registryFields.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RegistryField> findAll(Pageable pageable) {
        log.debug("Request to get all RegistryFields");
        Page<RegistryField> result = registryFieldRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one registryField by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RegistryField findOne(Long id) {
        log.debug("Request to get RegistryField : {}", id);
        RegistryField registryField = registryFieldRepository.findOne(id);
        return registryField;
    }

    /**
     *  Delete the  registryField by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RegistryField : {}", id);
        registryFieldRepository.delete(id);
        registryFieldSearchRepository.delete(id);
    }

    /**
     * Search for the registryField corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RegistryField> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RegistryFields for query {}", query);
        Page<RegistryField> result = registryFieldSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
