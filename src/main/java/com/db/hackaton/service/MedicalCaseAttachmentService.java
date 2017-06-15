package com.db.hackaton.service;

import com.db.hackaton.domain.MedicalCaseAttachment;
import com.db.hackaton.repository.MedicalCaseAttachmentRepository;
import com.db.hackaton.repository.search.MedicalCaseAttachmentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MedicalCaseAttachment.
 */
@Service
@Transactional
public class MedicalCaseAttachmentService {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseAttachmentService.class);
    
    private final MedicalCaseAttachmentRepository medicalCaseAttachmentRepository;

    private final MedicalCaseAttachmentSearchRepository medicalCaseAttachmentSearchRepository;

    public MedicalCaseAttachmentService(MedicalCaseAttachmentRepository medicalCaseAttachmentRepository, MedicalCaseAttachmentSearchRepository medicalCaseAttachmentSearchRepository) {
        this.medicalCaseAttachmentRepository = medicalCaseAttachmentRepository;
        this.medicalCaseAttachmentSearchRepository = medicalCaseAttachmentSearchRepository;
    }

    /**
     * Save a medicalCaseAttachment.
     *
     * @param medicalCaseAttachment the entity to save
     * @return the persisted entity
     */
    public MedicalCaseAttachment save(MedicalCaseAttachment medicalCaseAttachment) {
        log.debug("Request to save MedicalCaseAttachment : {}", medicalCaseAttachment);
        MedicalCaseAttachment result = medicalCaseAttachmentRepository.save(medicalCaseAttachment);
        medicalCaseAttachmentSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the medicalCaseAttachments.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicalCaseAttachment> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalCaseAttachments");
        Page<MedicalCaseAttachment> result = medicalCaseAttachmentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one medicalCaseAttachment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MedicalCaseAttachment findOne(Long id) {
        log.debug("Request to get MedicalCaseAttachment : {}", id);
        MedicalCaseAttachment medicalCaseAttachment = medicalCaseAttachmentRepository.findOne(id);
        return medicalCaseAttachment;
    }

    /**
     *  Delete the  medicalCaseAttachment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalCaseAttachment : {}", id);
        medicalCaseAttachmentRepository.delete(id);
        medicalCaseAttachmentSearchRepository.delete(id);
    }

    /**
     * Search for the medicalCaseAttachment corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicalCaseAttachment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MedicalCaseAttachments for query {}", query);
        Page<MedicalCaseAttachment> result = medicalCaseAttachmentSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
