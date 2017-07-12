package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.config.ApplicationProperties;
import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.service.MedicalCaseService;
import com.db.hackaton.service.dto.MedicalCaseDTO;
import com.db.hackaton.web.rest.util.HeaderUtil;
import com.db.hackaton.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
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

    private ApplicationProperties applicationProperties;

    public MedicalCaseResource(MedicalCaseService medicalCaseService, ApplicationProperties applicationProperties) {
        this.medicalCaseService = medicalCaseService;
        this.applicationProperties = applicationProperties;
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
    public ResponseEntity<MedicalCaseDTO> createMedicalCase(@Valid @RequestBody MedicalCaseDTO medicalCase) throws URISyntaxException {
        log.debug("REST request to save MedicalCase : {}", medicalCase);
        if (medicalCase.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new medicalCase cannot already have an ID")).body(null);
        }
        MedicalCaseDTO result = medicalCaseService.save(medicalCase);
        return ResponseEntity.created(new URI("/api/medical-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medical-cases/:id : get the "id" medicalCase.
     *
     * @param id the id of the medicalCase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalCase, or with status 404 (Not Found)
     */
    @GetMapping("/medical-cases/{id}")
    @Timed
    public ResponseEntity<MedicalCaseDTO> getMedicalCase(@PathVariable Long id) {
        log.debug("REST request to get MedicalCase : {}", id);
        MedicalCaseDTO medicalCase = medicalCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicalCase));
    }

    @PostMapping("/upload")
    @Timed
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes;
        if (!file.isEmpty()) {
            bytes = file.getBytes();
            String filePath = applicationProperties.getLocalStoragePath() + "/" + file.getOriginalFilename();
            FileUtils.writeByteArrayToFile(new File(filePath), file.getBytes());
        }
        System.out.println(String.format("receive %s", file.getOriginalFilename()));
    }

}
