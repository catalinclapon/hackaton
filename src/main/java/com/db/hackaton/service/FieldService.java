package com.db.hackaton.service;

import com.db.hackaton.domain.Field;
import com.db.hackaton.repository.FieldRepository;
import com.db.hackaton.repository.search.FieldSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Field.
 */
@Service
@Transactional
public class FieldService {

    private final Logger log = LoggerFactory.getLogger(FieldService.class);

    private final FieldRepository fieldRepository;

    private final FieldSearchRepository fieldSearchRepository;

    public FieldService(FieldRepository fieldRepository, FieldSearchRepository fieldSearchRepository) {
        this.fieldRepository = fieldRepository;
        this.fieldSearchRepository = fieldSearchRepository;
    }

    /**
     * Save a field.
     *
     * @param field the entity to save
     * @return the persisted entity
     */
    public Field save(Field field) {
        log.debug("Request to save Field : {}", field);
        Field result = fieldRepository.save(field);
        fieldSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the fields.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Field> findAll(Pageable pageable) {
        log.debug("Request to get all Fields");
        Page<Field> result = fieldRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one field by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Field findOne(Long id) {
        log.debug("Request to get Field : {}", id);
        Field field = fieldRepository.findOne(id);
        return field;
    }

    /**
     *  Delete the  field by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Field : {}", id);
        fieldRepository.delete(id);
        fieldSearchRepository.delete(id);
    }

    /**
     * Search for the field corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Field> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Fields for query {}", query);
        Page<Field> result = fieldSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Field findFirstByName(String name) {
        return fieldRepository.findFirstByName(name);
    }
}
