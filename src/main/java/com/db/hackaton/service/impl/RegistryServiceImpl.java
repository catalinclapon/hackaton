package com.db.hackaton.service.impl;

import com.db.hackaton.repository.FieldRepository;
import com.db.hackaton.repository.RegistryFieldRepository;
import com.db.hackaton.service.RegistryService;
import com.db.hackaton.domain.Registry;
import com.db.hackaton.repository.RegistryRepository;
import com.db.hackaton.repository.search.RegistrySearchRepository;
import com.db.hackaton.service.dto.RegistryDTO;
import com.db.hackaton.service.dto.RegistryFieldDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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
    private final RegistryFieldRepository registryFieldRepository;
    private final FieldRepository fieldRepository;

    private final RegistrySearchRepository registrySearchRepository;

    public RegistryServiceImpl(RegistryRepository registryRepository, RegistryFieldRepository registryFieldRepository, FieldRepository fieldRepository, RegistrySearchRepository registrySearchRepository) {
        this.registryRepository = registryRepository;
        this.registryFieldRepository = registryFieldRepository;
        this.fieldRepository = fieldRepository;
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
        // set status superseded to current registry
        if(registryDTO.getId() != null) {
            registryRepository.save(Optional.of(registryDTO)
                .map(RegistryDTO::build)
                .map(registry -> {
                    registry.setStatus("SUPERSEDED");
                    // remove registry
                    registrySearchRepository.delete(registry);
                    return registry;
                })
                .get());
        }
        // save as new version:
        Registry registry = registryRepository.save(Optional.of(registryDTO)
            .map(RegistryDTO::build)
            .map(registry1 -> registry1.id(null)
                .status("LATEST")).get());

        // set new ID on DTO
        registryDTO.setId(registry.getId());

        // store fields as new fields
        registryDTO.getFields().stream()
            .map(RegistryFieldDTO::build)
            .map(registryField -> {
                return registryField.id(null)
                    .registry(registry)
                    .field(fieldRepository.saveAndFlush(registryField.getField()));
            })
            .forEach(registryFieldRepository::saveAndFlush);

        registrySearchRepository.save(registry);
        return registryDTO;
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
        return registryRepository.findByStatus(pageable, "LATEST")
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
