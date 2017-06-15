package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.domain.RegistryField;
import com.db.hackaton.service.RegistryFieldService;
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
 * REST controller for managing RegistryField.
 */
@RestController
@RequestMapping("/api")
public class RegistryFieldResource {

    private final Logger log = LoggerFactory.getLogger(RegistryFieldResource.class);

    private static final String ENTITY_NAME = "registryField";
        
    private final RegistryFieldService registryFieldService;

    public RegistryFieldResource(RegistryFieldService registryFieldService) {
        this.registryFieldService = registryFieldService;
    }

    /**
     * POST  /registry-fields : Create a new registryField.
     *
     * @param registryField the registryField to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registryField, or with status 400 (Bad Request) if the registryField has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/registry-fields")
    @Timed
    public ResponseEntity<RegistryField> createRegistryField(@RequestBody RegistryField registryField) throws URISyntaxException {
        log.debug("REST request to save RegistryField : {}", registryField);
        if (registryField.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new registryField cannot already have an ID")).body(null);
        }
        RegistryField result = registryFieldService.save(registryField);
        return ResponseEntity.created(new URI("/api/registry-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registry-fields : Updates an existing registryField.
     *
     * @param registryField the registryField to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registryField,
     * or with status 400 (Bad Request) if the registryField is not valid,
     * or with status 500 (Internal Server Error) if the registryField couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/registry-fields")
    @Timed
    public ResponseEntity<RegistryField> updateRegistryField(@RequestBody RegistryField registryField) throws URISyntaxException {
        log.debug("REST request to update RegistryField : {}", registryField);
        if (registryField.getId() == null) {
            return createRegistryField(registryField);
        }
        RegistryField result = registryFieldService.save(registryField);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, registryField.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registry-fields : get all the registryFields.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of registryFields in body
     */
    @GetMapping("/registry-fields")
    @Timed
    public ResponseEntity<List<RegistryField>> getAllRegistryFields(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RegistryFields");
        Page<RegistryField> page = registryFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/registry-fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /registry-fields/:id : get the "id" registryField.
     *
     * @param id the id of the registryField to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registryField, or with status 404 (Not Found)
     */
    @GetMapping("/registry-fields/{id}")
    @Timed
    public ResponseEntity<RegistryField> getRegistryField(@PathVariable Long id) {
        log.debug("REST request to get RegistryField : {}", id);
        RegistryField registryField = registryFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(registryField));
    }

    /**
     * DELETE  /registry-fields/:id : delete the "id" registryField.
     *
     * @param id the id of the registryField to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/registry-fields/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegistryField(@PathVariable Long id) {
        log.debug("REST request to delete RegistryField : {}", id);
        registryFieldService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/registry-fields?query=:query : search for the registryField corresponding
     * to the query.
     *
     * @param query the query of the registryField search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/registry-fields")
    @Timed
    public ResponseEntity<List<RegistryField>> searchRegistryFields(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of RegistryFields for query {}", query);
        Page<RegistryField> page = registryFieldService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/registry-fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
