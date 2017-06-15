package com.db.hackaton.service;

import com.db.hackaton.domain.Registry;
import com.db.hackaton.service.dto.RegistryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Registry.
 */
public interface RegistryService {

    /**
     * Save a registry.
     *
     * @param registry the entity to save
     * @return the persisted entity
     */
    RegistryDTO save(RegistryDTO registry);

    /**
     *  Get all the registries.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RegistryDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" registry.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RegistryDTO findOne(Long id);

    /**
     *  Delete the "id" registry.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the registry corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RegistryDTO> search(String query, Pageable pageable);
}
