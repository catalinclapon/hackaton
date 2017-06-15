package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.service.MedicalCaseService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MedicalCase.
 */
@RestController
@RequestMapping("/api")
public class MedicalCaseResource {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseResource.class);

    private static final String ENTITY_NAME = "medicalCase";
        
    private final MedicalCaseService medicalCaseService;

    public MedicalCaseResource(MedicalCaseService medicalCaseService) {
        this.medicalCaseService = medicalCaseService;
    }

    /**
     * POST  /medical-cases : Create a new medicalCase.
     *
     * @param medicalCase the medicalCase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicalCase, or with status 400 (Bad Request) if the medicalCase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medical-cases")
    @Timed
    public ResponseEntity<MedicalCase> createMedicalCase(@Valid @RequestBody MedicalCase medicalCase) throws URISyntaxException {
        log.debug("REST request to save MedicalCase : {}", medicalCase);
        if (medicalCase.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new medicalCase cannot already have an ID")).body(null);
        }
        MedicalCase result = medicalCaseService.save(medicalCase);
        return ResponseEntity.created(new URI("/api/medical-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medical-cases : Updates an existing medicalCase.
     *
     * @param medicalCase the medicalCase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicalCase,
     * or with status 400 (Bad Request) if the medicalCase is not valid,
     * or with status 500 (Internal Server Error) if the medicalCase couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medical-cases")
    @Timed
    public ResponseEntity<MedicalCase> updateMedicalCase(@Valid @RequestBody MedicalCase medicalCase) throws URISyntaxException {
        log.debug("REST request to update MedicalCase : {}", medicalCase);
        if (medicalCase.getId() == null) {
            return createMedicalCase(medicalCase);
        }
        MedicalCase result = medicalCaseService.save(medicalCase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicalCase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medical-cases : get all the medicalCases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicalCases in body
     */
    @GetMapping("/medical-cases")
    @Timed
    public ResponseEntity<List<MedicalCase>> getAllMedicalCases(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MedicalCases");
        Page<MedicalCase> page = medicalCaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medical-cases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medical-cases/:id : get the "id" medicalCase.
     *
     * @param id the id of the medicalCase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalCase, or with status 404 (Not Found)
     */
    @GetMapping("/medical-cases/{id}")
    @Timed
    public ResponseEntity<MedicalCase> getMedicalCase(@PathVariable Long id) {
        log.debug("REST request to get MedicalCase : {}", id);
        MedicalCase medicalCase = medicalCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicalCase));
    }

    /**
     * DELETE  /medical-cases/:id : delete the "id" medicalCase.
     *
     * @param id the id of the medicalCase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medical-cases/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicalCase(@PathVariable Long id) {
        log.debug("REST request to delete MedicalCase : {}", id);
        medicalCaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medical-cases?query=:query : search for the medicalCase corresponding
     * to the query.
     *
     * @param query the query of the medicalCase search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medical-cases")
    @Timed
    public ResponseEntity<List<MedicalCase>> searchMedicalCases(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MedicalCases for query {}", query);
        Page<MedicalCase> page = medicalCaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medical-cases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
