package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.domain.MedicalCaseAttachment;
import com.db.hackaton.service.MedicalCaseAttachmentService;
import com.db.hackaton.web.rest.util.HeaderUtil;
import com.db.hackaton.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MedicalCaseAttachment.
 */
@RestController
@RequestMapping("/api")
public class MedicalCaseAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseAttachmentResource.class);

    private static final String ENTITY_NAME = "medicalCaseAttachment";
        
    private final MedicalCaseAttachmentService medicalCaseAttachmentService;

    public MedicalCaseAttachmentResource(MedicalCaseAttachmentService medicalCaseAttachmentService) {
        this.medicalCaseAttachmentService = medicalCaseAttachmentService;
    }

    /**
     * POST  /medical-case-attachments : Create a new medicalCaseAttachment.
     *
     * @param medicalCaseAttachment the medicalCaseAttachment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicalCaseAttachment, or with status 400 (Bad Request) if the medicalCaseAttachment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medical-case-attachments")
    @Timed
    public ResponseEntity<MedicalCaseAttachment> createMedicalCaseAttachment(@RequestBody MedicalCaseAttachment medicalCaseAttachment) throws URISyntaxException {
        log.debug("REST request to save MedicalCaseAttachment : {}", medicalCaseAttachment);
        if (medicalCaseAttachment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new medicalCaseAttachment cannot already have an ID")).body(null);
        }
        MedicalCaseAttachment result = medicalCaseAttachmentService.save(medicalCaseAttachment);
        return ResponseEntity.created(new URI("/api/medical-case-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medical-case-attachments : Updates an existing medicalCaseAttachment.
     *
     * @param medicalCaseAttachment the medicalCaseAttachment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicalCaseAttachment,
     * or with status 400 (Bad Request) if the medicalCaseAttachment is not valid,
     * or with status 500 (Internal Server Error) if the medicalCaseAttachment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medical-case-attachments")
    @Timed
    public ResponseEntity<MedicalCaseAttachment> updateMedicalCaseAttachment(@RequestBody MedicalCaseAttachment medicalCaseAttachment) throws URISyntaxException {
        log.debug("REST request to update MedicalCaseAttachment : {}", medicalCaseAttachment);
        if (medicalCaseAttachment.getId() == null) {
            return createMedicalCaseAttachment(medicalCaseAttachment);
        }
        MedicalCaseAttachment result = medicalCaseAttachmentService.save(medicalCaseAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicalCaseAttachment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medical-case-attachments : get all the medicalCaseAttachments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicalCaseAttachments in body
     */
    @GetMapping("/medical-case-attachments")
    @Timed
    public ResponseEntity<List<MedicalCaseAttachment>> getAllMedicalCaseAttachments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MedicalCaseAttachments");
        Page<MedicalCaseAttachment> page = medicalCaseAttachmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medical-case-attachments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medical-case-attachments/:id : get the "id" medicalCaseAttachment.
     *
     * @param id the id of the medicalCaseAttachment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalCaseAttachment, or with status 404 (Not Found)
     */
    @GetMapping("/medical-case-attachments/{id}")
    @Timed
    public ResponseEntity<MedicalCaseAttachment> getMedicalCaseAttachment(@PathVariable Long id) {
        log.debug("REST request to get MedicalCaseAttachment : {}", id);
        MedicalCaseAttachment medicalCaseAttachment = medicalCaseAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicalCaseAttachment));
    }

    /**
     * DELETE  /medical-case-attachments/:id : delete the "id" medicalCaseAttachment.
     *
     * @param id the id of the medicalCaseAttachment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medical-case-attachments/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicalCaseAttachment(@PathVariable Long id) {
        log.debug("REST request to delete MedicalCaseAttachment : {}", id);
        medicalCaseAttachmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medical-case-attachments?query=:query : search for the medicalCaseAttachment corresponding
     * to the query.
     *
     * @param query the query of the medicalCaseAttachment search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medical-case-attachments")
    @Timed
    public ResponseEntity<List<MedicalCaseAttachment>> searchMedicalCaseAttachments(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MedicalCaseAttachments for query {}", query);
        Page<MedicalCaseAttachment> page = medicalCaseAttachmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medical-case-attachments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
