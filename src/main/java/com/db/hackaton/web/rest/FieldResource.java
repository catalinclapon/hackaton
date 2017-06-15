package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.domain.Field;
import com.db.hackaton.service.FieldService;
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
 * REST controller for managing Field.
 */
@RestController
@RequestMapping("/api")
public class FieldResource {

    private final Logger log = LoggerFactory.getLogger(FieldResource.class);

    private static final String ENTITY_NAME = "field";
        
    private final FieldService fieldService;

    public FieldResource(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    /**
     * POST  /fields : Create a new field.
     *
     * @param field the field to create
     * @return the ResponseEntity with status 201 (Created) and with body the new field, or with status 400 (Bad Request) if the field has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fields")
    @Timed
    public ResponseEntity<Field> createField(@Valid @RequestBody Field field) throws URISyntaxException {
        log.debug("REST request to save Field : {}", field);
        if (field.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new field cannot already have an ID")).body(null);
        }
        Field result = fieldService.save(field);
        return ResponseEntity.created(new URI("/api/fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fields : Updates an existing field.
     *
     * @param field the field to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated field,
     * or with status 400 (Bad Request) if the field is not valid,
     * or with status 500 (Internal Server Error) if the field couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fields")
    @Timed
    public ResponseEntity<Field> updateField(@Valid @RequestBody Field field) throws URISyntaxException {
        log.debug("REST request to update Field : {}", field);
        if (field.getId() == null) {
            return createField(field);
        }
        Field result = fieldService.save(field);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, field.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fields : get all the fields.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fields in body
     */
    @GetMapping("/fields")
    @Timed
    public ResponseEntity<List<Field>> getAllFields(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Fields");
        Page<Field> page = fieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fields/:id : get the "id" field.
     *
     * @param id the id of the field to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the field, or with status 404 (Not Found)
     */
    @GetMapping("/fields/{id}")
    @Timed
    public ResponseEntity<Field> getField(@PathVariable Long id) {
        log.debug("REST request to get Field : {}", id);
        Field field = fieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(field));
    }

    /**
     * DELETE  /fields/:id : delete the "id" field.
     *
     * @param id the id of the field to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fields/{id}")
    @Timed
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        log.debug("REST request to delete Field : {}", id);
        fieldService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fields?query=:query : search for the field corresponding
     * to the query.
     *
     * @param query the query of the field search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/fields")
    @Timed
    public ResponseEntity<List<Field>> searchFields(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Fields for query {}", query);
        Page<Field> page = fieldService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fields");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
