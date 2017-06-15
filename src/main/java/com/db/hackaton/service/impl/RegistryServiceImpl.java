package com.db.hackaton.service.impl;

import com.db.hackaton.service.RegistryService;
import com.db.hackaton.domain.Registry;
import com.db.hackaton.repository.RegistryRepository;
import com.db.hackaton.repository.search.RegistrySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Registry.
 */
@Service
@Transactional
public class RegistryServiceImpl implements RegistryService{

    private final Logger log = LoggerFactory.getLogger(RegistryServiceImpl.class);
    
    private final RegistryRepository registryRepository;

    private final RegistrySearchRepository registrySearchRepository;

    public RegistryServiceImpl(RegistryRepository registryRepository, RegistrySearchRepository registrySearchRepository) {
        this.registryRepository = registryRepository;
        this.registrySearchRepository = registrySearchRepository;
    }

    /**
     * Save a registry.
     *
     * @param registry the entity to save
     * @return the persisted entity
     */
    @Override
    public Registry save(Registry registry) {
        log.debug("Request to save Registry : {}", registry);
        Registry result = registryRepository.save(registry);
        registrySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the registries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Registry> findAll(Pageable pageable) {
        log.debug("Request to get all Registries");
        Page<Registry> result = registryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one registry by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Registry findOne(Long id) {
        log.debug("Request to get Registry : {}", id);
        Registry registry = registryRepository.findOne(id);
        return registry;
    }

    /**
     *  Delete the  registry by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Registry : {}", id);
        registryRepository.delete(id);
        registrySearchRepository.delete(id);
    }

    /**
     * Search for the registry corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Registry> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Registries for query {}", query);
        Page<Registry> result = registrySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
