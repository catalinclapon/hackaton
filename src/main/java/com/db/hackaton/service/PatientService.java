package com.db.hackaton.service;

import com.db.hackaton.domain.Patient;
import com.db.hackaton.repository.PatientRepository;
import com.db.hackaton.repository.search.PatientSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Patient.
 */
@Service
@Transactional
public class PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientService.class);
    
    private final PatientRepository patientRepository;

    private final PatientSearchRepository patientSearchRepository;

    public PatientService(PatientRepository patientRepository, PatientSearchRepository patientSearchRepository) {
        this.patientRepository = patientRepository;
        this.patientSearchRepository = patientSearchRepository;
    }

    /**
     * Save a patient.
     *
     * @param patient the entity to save
     * @return the persisted entity
     */
    public Patient save(Patient patient) {
        log.debug("Request to save Patient : {}", patient);
        Patient result = patientRepository.save(patient);
        patientSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the patients.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        log.debug("Request to get all Patients");
        Page<Patient> result = patientRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one patient by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Patient findOne(Long id) {
        log.debug("Request to get Patient : {}", id);
        Patient patient = patientRepository.findOne(id);
        return patient;
    }

    /**
     *  Delete the  patient by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Patient : {}", id);
        patientRepository.delete(id);
        patientSearchRepository.delete(id);
    }

    /**
     * Search for the patient corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Patient> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Patients for query {}", query);
        Page<Patient> result = patientSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
