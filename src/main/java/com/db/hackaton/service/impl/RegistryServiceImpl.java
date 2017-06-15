package com.db.hackaton.service.impl;

import com.db.hackaton.service.RegistryService;
import com.db.hackaton.domain.Registry;
import com.db.hackaton.repository.RegistryRepository;
import com.db.hackaton.repository.search.RegistrySearchRepository;
import com.db.hackaton.service.dto.RegistryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * @param registryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RegistryDTO save(RegistryDTO registryDTO) {
        log.debug("Request to save Registry : {}", registryDTO);
        Registry registry = registryRepository.save(Optional.of(registryDTO)
            .map(RegistryDTO::build).get());
        registrySearchRepository.save(registry);
        return RegistryDTO.build(registry);
    }

    /**
     *  Get all the registries.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RegistryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Registries");
        return registryRepository.findAll(pageable)
            .map(RegistryDTO::build);
    }

    /**
     *  Get one registry by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RegistryDTO findOne(Long id) {
        log.debug("Request to get Registry : {}", id);
        return RegistryDTO.build(registryRepository.findOne(id));
    }

    /**
     *  Delete the  registry by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Registry : {}", id);
        // TODO: Logical delete
        //registryRepository.delete(id);
        //registrySearchRepository.delete(id);
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
    public Page<RegistryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Registries for query {}", query);
        return registrySearchRepository.search(queryStringQuery(query), pageable)
            .map(RegistryDTO::build);
    }
}
