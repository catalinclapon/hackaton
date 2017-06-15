package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.domain.MedicalCaseField;

import com.db.hackaton.repository.MedicalCaseFieldRepository;
import com.db.hackaton.repository.search.MedicalCaseFieldSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MedicalCaseField.
 */
@RestController
@RequestMapping("/api")
public class MedicalCaseFieldResource {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseFieldResource.class);

    private static final String ENTITY_NAME = "medicalCaseField";
        
    private final MedicalCaseFieldRepository medicalCaseFieldRepository;

    private final MedicalCaseFieldSearchRepository medicalCaseFieldSearchRepository;

    public MedicalCaseFieldResource(MedicalCaseFieldRepository medicalCaseFieldRepository, MedicalCaseFieldSearchRepository medicalCaseFieldSearchRepository) {
        this.medicalCaseFieldRepository = medicalCaseFieldRepository;
        this.medicalCaseFieldSearchRepository = medicalCaseFieldSearchRepository;
    }

    /**
     * POST  /medical-case-fields : Create a new medicalCaseField.
     *
     * @param medicalCaseField the medicalCaseField to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicalCaseField, or with status 400 (Bad Request) if the medicalCaseField has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medical-case-fields")
    @Timed
    public ResponseEntity<MedicalCaseField> createMedicalCaseField(@RequestBody MedicalCaseField medicalCaseField) throws URISyntaxException {
        log.debug("REST request to save MedicalCaseField : {}", medicalCaseField);
        if (medicalCaseField.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new medicalCaseField cannot already have an ID")).body(null);
        }
        MedicalCaseField result = medicalCaseFieldRepository.save(medicalCaseField);
        medicalCaseFieldSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medical-case-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medical-case-fields : Updates an existing medicalCaseField.
     *
     * @param medicalCaseField the medicalCaseField to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicalCaseField,
     * or with status 400 (Bad Request) if the medicalCaseField is not valid,
     * or with status 500 (Internal Server Error) if the medicalCaseField couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medical-case-fields")
    @Timed
    public ResponseEntity<MedicalCaseField> updateMedicalCaseField(@RequestBody MedicalCaseField medicalCaseField) throws URISyntaxException {
        log.debug("REST request to update MedicalCaseField : {}", medicalCaseField);
        if (medicalCaseField.getId() == null) {
            return createMedicalCaseField(medicalCaseField);
        }
        MedicalCaseField result = medicalCaseFieldRepository.save(medicalCaseField);
        medicalCaseFieldSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicalCaseField.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medical-case-fields : get all the medicalCaseFields.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicalCaseFields in body
     */
    @GetMapping("/medical-case-fields")
    @Timed
    public ResponseEntity<List<MedicalCaseField>> getAllMedicalCaseFields(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MedicalCaseFields");
        Page<MedicalCaseField> page = medicalCaseFieldRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medical-case-fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medical-case-fields/:id : get the "id" medicalCaseField.
     *
     * @param id the id of the medicalCaseField to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalCaseField, or with status 404 (Not Found)
     */
    @GetMapping("/medical-case-fields/{id}")
    @Timed
    public ResponseEntity<MedicalCaseField> getMedicalCaseField(@PathVariable Long id) {
        log.debug("REST request to get MedicalCaseField : {}", id);
        MedicalCaseField medicalCaseField = medicalCaseFieldRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicalCaseField));
    }

    /**
     * DELETE  /medical-case-fields/:id : delete the "id" medicalCaseField.
     *
     * @param id the id of the medicalCaseField to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medical-case-fields/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicalCaseField(@PathVariable Long id) {
        log.debug("REST request to delete MedicalCaseField : {}", id);
        medicalCaseFieldRepository.delete(id);
        medicalCaseFieldSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medical-case-fields?query=:query : search for the medicalCaseField corresponding
     * to the query.
     *
     * @param query the query of the medicalCaseField search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medical-case-fields")
    @Timed
    public ResponseEntity<List<MedicalCaseField>> searchMedicalCaseFields(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MedicalCaseFields for query {}", query);
        Page<MedicalCaseField> page = medicalCaseFieldSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medical-case-fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
