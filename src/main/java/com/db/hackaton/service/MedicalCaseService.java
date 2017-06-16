package com.db.hackaton.service;

import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.repository.MedicalCaseRepository;
import com.db.hackaton.repository.search.MedicalCaseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing MedicalCase.
 */
@Service
@Transactional
public class MedicalCaseService {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseService.class);

    private final MedicalCaseRepository medicalCaseRepository;

    private final MedicalCaseSearchRepository medicalCaseSearchRepository;

    public MedicalCaseService(MedicalCaseRepository medicalCaseRepository, MedicalCaseSearchRepository medicalCaseSearchRepository) {
        this.medicalCaseRepository = medicalCaseRepository;
        this.medicalCaseSearchRepository = medicalCaseSearchRepository;
    }

    /**
     * Save a medicalCase.
     *
     * @param medicalCase the entity to save
     * @return the persisted entity
     */
    public MedicalCase save(MedicalCase medicalCase) {
        log.debug("Request to save MedicalCase : {}", medicalCase);
        MedicalCase result = medicalCaseRepository.save(medicalCase);
        medicalCaseSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the medicalCases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicalCase> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalCases");
        Page<MedicalCase> result = medicalCaseRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one medicalCase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MedicalCase findOne(Long id) {
        log.debug("Request to get MedicalCase : {}", id);
        MedicalCase medicalCase = medicalCaseRepository.findOne(id);
        return medicalCase;
    }

    /**
     * Delete the  medicalCase by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalCase : {}", id);
        medicalCaseRepository.delete(id);
        medicalCaseSearchRepository.delete(id);
    }

    /**
     * Search for the medicalCase corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicalCase> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MedicalCases for query {}", query);
        Page<MedicalCase> result = medicalCaseSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Map<String, String>> findAll(String registryUuid, List<Long> fields) {
        List<Map<String, String>> result = new ArrayList<>();
        List<MedicalCase> cases = medicalCaseRepository.findByStatusAndRegistryUuid("LATEST", registryUuid);
        for (MedicalCase medicalCase : cases) {
            Map<String, String> row = new HashMap<>();
            row.put("CNP", medicalCase.getPatient() != null ? medicalCase.getPatient().getCnp() : "N/A");
            row.put("Name", medicalCase.getName() != null ? medicalCase.getName() : "N/A");
            Map<Long, MedicalCaseField> tempFields = new HashMap<>();
            for (MedicalCaseField field : medicalCase.getFields()) {
                if (field.getField() != null && fields.contains(field.getField().getId())) {
                    row.put(field.getField().getName(), field.getValue());
                }
            }
            result.add(row);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<MedicalCase> findAllLatest(String registryUuid) {
        return medicalCaseRepository.findByStatusAndRegistryUuid("LATEST", registryUuid);
    }
}
