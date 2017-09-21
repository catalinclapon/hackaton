package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.config.ApplicationProperties;
import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.service.MedicalCaseService;
import com.db.hackaton.service.RegistryService;
import com.db.hackaton.service.dto.MedicalCaseDTO;
import com.db.hackaton.service.dto.RegistryDTO;
import com.db.hackaton.web.rest.util.HeaderUtil;
import com.db.hackaton.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
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
    private final RegistryService registryService;

    private ApplicationProperties applicationProperties;

    public MedicalCaseResource(MedicalCaseService medicalCaseService, RegistryService registryService, ApplicationProperties applicationProperties) {
        this.medicalCaseService = medicalCaseService;
        this.applicationProperties = applicationProperties;
        this.registryService = registryService;
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
     * @param cnp the id of the medicalCase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalCase, or with status 404 (Not Found)
     */
    @GetMapping("/medical-cases/{registryId}/{cnp}")
    @Timed
    public ResponseEntity<MedicalCaseDTO> getMedicalCase(@PathVariable Long registryId, @PathVariable String cnp) {
        log.debug("REST request to get MedicalCase : {}", cnp);
        MedicalCaseDTO medicalCase = medicalCaseService.findByRegistryIdAndCNP(registryId, cnp);
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

    /**
     * PUT  /medical-cases : Update an existing medicalCase.
     *
     * @param medicalCase the medicalCase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicalCase, or with status 400 (Bad Request) if the medicalCase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medical-cases")
    @Timed
    public ResponseEntity<MedicalCaseDTO> updateMedicalCase(@Valid @RequestBody MedicalCaseDTO medicalCase) throws URISyntaxException {
        log.debug("REST request to update MedicalCase : {}", medicalCase);

        MedicalCaseDTO result = medicalCaseService.updateStatus(medicalCase);

       /* return ResponseEntity.created(new URI("/api/medical-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);*/

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);


    }

    /**
     * DELETE  /medical-cases/:id : delete the medical case with id = "id".
     *
     * @param id the id of the medicalCase to delete
     * @return the ResponseEntity with status 200 (OK) and with body the medicalCase, or with status 404 (Not Found)
     */
    @DeleteMapping("/medical-cases/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicalCase(@PathVariable Long id) {
        log.debug("REST request to delete MedicalCase : {}", id);
        medicalCaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(produces = {MediaType.APPLICATION_PDF_VALUE}, value = "/medical-cases/{id}/pdfExport")
    @Timed
    public void exportPdf(@PathVariable Long id,
                          HttpServletResponse response) throws IOException {

        log.debug("Export to PDF MedicalCase : {}", id);

        List<MedicalCase> cases = new ArrayList<>();
        cases.add(MedicalCaseDTO.build(medicalCaseService.findOne(id)));
        PDDocument document = medicalCaseService.exportDocuments(cases);

        String pathName = applicationProperties.getLocalStoragePath() + "/" + cases.get(0).getUuid() + ".pdf";
        medicalCaseService.saveDocument(document, pathName, response);
    }
}
