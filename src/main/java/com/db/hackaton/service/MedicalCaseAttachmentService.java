package com.db.hackaton.service;

import com.db.hackaton.domain.MedicalCaseAttachment;
import com.db.hackaton.repository.MedicalCaseAttachmentRepository;
import com.db.hackaton.repository.search.MedicalCaseAttachmentSearchRepository;
import com.db.hackaton.service.dto.MedicalCaseAttachmentDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public MedicalCaseAttachmentDTO save(MedicalCaseAttachmentDTO medicalCaseAttachmentDTO) {
        log.debug("Request to save MedicalCaseAttachment : {}", medicalCaseAttachmentDTO);

        MedicalCaseAttachment medicalCaseAttachment = medicalCaseAttachmentRepository.save(Optional.of(medicalCaseAttachmentDTO)
            .map(MedicalCaseAttachmentDTO::build)
            .get());

        medicalCaseAttachmentDTO.setId(medicalCaseAttachment.getId());

        return medicalCaseAttachmentDTO;
    }

    public void downloadAttachment(Long attachmentId, HttpServletResponse response) throws IOException  {

        MedicalCaseAttachment attachment = findOne(attachmentId);
        File file = new File(attachment.getLocation());
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(FileUtils.readFileToByteArray(file));

            ByteArrayInputStream inStream = new ByteArrayInputStream(bos.toByteArray());

            IOUtils.copy(inStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream.", ex);
            throw new RuntimeException("IOError writing file to output stream");

        }
    }
}
