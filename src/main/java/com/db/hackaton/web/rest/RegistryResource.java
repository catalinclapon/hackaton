package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.domain.Registry;
import com.db.hackaton.service.RegistryService;
import com.db.hackaton.service.dto.RegistryDTO;
import com.db.hackaton.web.rest.util.HeaderUtil;
import com.db.hackaton.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Registry.
 */
@RestController
@RequestMapping("/api")
public class RegistryResource {

    private final Logger log = LoggerFactory.getLogger(RegistryResource.class);

    private static final String ENTITY_NAME = "registry";

    private final RegistryService registryService;

    public RegistryResource(RegistryService registryService) {
        this.registryService = registryService;
    }

    /**
     * POST  /registries : Create a new registry.
     *
     * @param registry the registry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registry, or with status 400 (Bad Request) if the registry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/registries")
    @Timed
    public ResponseEntity<RegistryDTO> createRegistry(@Valid @RequestBody RegistryDTO registry) throws URISyntaxException {
        log.debug("REST request to save Registry : {}", registry);
        if (registry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new registry cannot already have an ID")).body(null);
        }
        RegistryDTO result = registryService.save(registry);
        return ResponseEntity.created(new URI("/api/registries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registries : Updates an existing registry.
     *
     * @param registry the registry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registry,
     * or with status 400 (Bad Request) if the registry is not valid,
     * or with status 500 (Internal Server Error) if the registry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/registries")
    @Timed
    public ResponseEntity<RegistryDTO> updateRegistry(@Valid @RequestBody RegistryDTO registry) throws URISyntaxException {
        log.debug("REST request to update Registry : {}", registry);
        if (registry.getId() == null) {
            return createRegistry(registry);
        }
        RegistryDTO result = registryService.save(registry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, registry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registries : get all the registries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of registries in body
     */
    @GetMapping("/registries")
    @Timed
    public ResponseEntity<List<RegistryDTO>> getAllRegistries(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Registries");
        Page<RegistryDTO> page = registryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/registries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /registries/:id : get the "id" registry.
     *
     * @param id the id of the registry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registry, or with status 404 (Not Found)
     */
    @GetMapping("/registries/{id}")
    @Timed
    public ResponseEntity<RegistryDTO> getRegistry(@PathVariable Long id) {
        log.debug("REST request to get Registry : {}", id);
        RegistryDTO registry = registryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(registry));
    }

    /**
     * DELETE  /registries/:id : delete the "id" registry.
     *
     * @param id the id of the registry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/registries/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegistry(@PathVariable Long id) {
        log.debug("REST request to delete Registry : {}", id);
        registryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/registries?query=:query : search for the registry corresponding
     * to the query.
     *
     * @param query the query of the registry search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/registries")
    @Timed
    public ResponseEntity<List<RegistryDTO>> searchRegistries(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Registries for query {}", query);
        Page<RegistryDTO> page = registryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/registries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping( produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE},value = "/registries/{id}/template")
    @Timed
    public void getTemplate(@PathVariable Long id,
                            HttpServletResponse response) throws IOException {

        try {
            // get your file as InputStream
            InputStream is = new FileInputStream("C:/Temp/uploads/Abonament-MEDLIFE-2015_2016..xls");
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream. Filename was '{}'", id, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }

    }


}
