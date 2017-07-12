package com.db.hackaton.service;

import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.repository.FieldRepository;
import com.db.hackaton.repository.MedicalCaseFieldRepository;
import com.db.hackaton.repository.MedicalCaseRepository;
import com.db.hackaton.repository.search.MedicalCaseSearchRepository;
import com.db.hackaton.service.dto.MedicalCaseDTO;
import com.db.hackaton.service.dto.MedicalCaseFieldDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing MedicalCase.
 */
@Service
@Transactional
public class MedicalCaseService {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseService.class);

    private final MedicalCaseRepository medicalCaseRepository;
    private final MedicalCaseFieldRepository medicalCaseFieldRepository;

    private final MedicalCaseSearchRepository medicalCaseSearchRepository;

    public MedicalCaseService(MedicalCaseRepository medicalCaseRepository,
                              MedicalCaseFieldRepository medicalCaseFieldRepository,
                              MedicalCaseSearchRepository medicalCaseSearchRepository) {
        this.medicalCaseFieldRepository = medicalCaseFieldRepository;
        this.medicalCaseRepository = medicalCaseRepository;
        this.medicalCaseSearchRepository = medicalCaseSearchRepository;
    }

    /**
     * Save a medicalCase.
     *
     * @param medicalCaseDTO the entity to save
     * @return the persisted entity
     */
    @SuppressWarnings("Duplicates")
    public MedicalCaseDTO save(MedicalCaseDTO medicalCaseDTO) {
        log.debug("Request to save MedicalCase : {}", medicalCaseDTO);
        if (medicalCaseDTO.getId() != null) {
            medicalCaseRepository.save(Optional.of(medicalCaseDTO)
                .map(MedicalCaseDTO::build)
                .map(mc -> {
                    mc.setStatus("SUPERSEDED");
                    medicalCaseSearchRepository.delete(mc);
                    return mc;
                }).get());
        } else //noinspection Duplicates
            if(medicalCaseDTO.getUuid() == null) {
            medicalCaseDTO.setUuid(UUID.randomUUID().toString());
        }

        MedicalCase medicalCase = medicalCaseRepository.save(Optional.of(medicalCaseDTO)
            .map(MedicalCaseDTO::build)
            .map(mc -> mc.id(null)
                .status("LATEST"))
            .get());

        medicalCaseDTO.setId(medicalCase.getId());

        medicalCaseDTO.getFields().stream()
            .map(MedicalCaseFieldDTO::build)
            .map(mc -> mc.id(null)
                .medicalCase(medicalCase))
            .forEach(medicalCaseFieldRepository::saveAndFlush);

        medicalCaseSearchRepository.save(medicalCase);
        //medicalCaseSearchRepository.save(result);
        return medicalCaseDTO;
    }

    /**
     * Get one medicalCase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MedicalCaseDTO findOne(Long id) {
        log.debug("Request to get MedicalCase : {}", id);
        MedicalCase medicalCase = medicalCaseRepository.findOne(id);
        return MedicalCaseDTO.build(medicalCase);
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
