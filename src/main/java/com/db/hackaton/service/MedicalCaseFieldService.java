package com.db.hackaton.service;

import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.repository.MedicalCaseFieldRepository;
import com.db.hackaton.repository.search.MedicalCaseFieldSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MedicalCase.
 */
@Service
@Transactional
public class MedicalCaseFieldService {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseFieldService.class);

    private final MedicalCaseFieldRepository medicalCaseFieldRepository;

    private final MedicalCaseFieldSearchRepository medicalCaseFieldSearchRepository;

    public MedicalCaseFieldService(MedicalCaseFieldRepository medicalCaseFieldRepository, MedicalCaseFieldSearchRepository medicalCaseFieldSearchRepository) {
        this.medicalCaseFieldRepository = medicalCaseFieldRepository;
        this.medicalCaseFieldSearchRepository = medicalCaseFieldSearchRepository;
    }

    public MedicalCaseField save(MedicalCaseField entity) {
        log.debug("Request to save MedicalCaseField : {}", entity);
        MedicalCaseField result = medicalCaseFieldRepository.save(entity);
        //medicalCaseFieldSearchRepository.save(result);
        return result;
    }
}
