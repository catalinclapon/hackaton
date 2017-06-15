package com.db.hackaton.web.rest;

import com.db.hackaton.HackatonApp;

import com.db.hackaton.domain.MedicalCaseAttachment;
import com.db.hackaton.repository.MedicalCaseAttachmentRepository;
import com.db.hackaton.service.MedicalCaseAttachmentService;
import com.db.hackaton.repository.search.MedicalCaseAttachmentSearchRepository;
import com.db.hackaton.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MedicalCaseAttachmentResource REST controller.
 *
 * @see MedicalCaseAttachmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
public class MedicalCaseAttachmentResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private MedicalCaseAttachmentRepository medicalCaseAttachmentRepository;

    @Autowired
    private MedicalCaseAttachmentService medicalCaseAttachmentService;

    @Autowired
    private MedicalCaseAttachmentSearchRepository medicalCaseAttachmentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicalCaseAttachmentMockMvc;

    private MedicalCaseAttachment medicalCaseAttachment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedicalCaseAttachmentResource medicalCaseAttachmentResource = new MedicalCaseAttachmentResource(medicalCaseAttachmentService);
        this.restMedicalCaseAttachmentMockMvc = MockMvcBuilders.standaloneSetup(medicalCaseAttachmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalCaseAttachment createEntity(EntityManager em) {
        MedicalCaseAttachment medicalCaseAttachment = new MedicalCaseAttachment()
            .title(DEFAULT_TITLE)
            .location(DEFAULT_LOCATION);
        return medicalCaseAttachment;
    }

    @Before
    public void initTest() {
        medicalCaseAttachmentSearchRepository.deleteAll();
        medicalCaseAttachment = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicalCaseAttachment() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseAttachmentRepository.findAll().size();

        // Create the MedicalCaseAttachment
        restMedicalCaseAttachmentMockMvc.perform(post("/api/medical-case-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCaseAttachment)))
            .andExpect(status().isCreated());

        // Validate the MedicalCaseAttachment in the database
        List<MedicalCaseAttachment> medicalCaseAttachmentList = medicalCaseAttachmentRepository.findAll();
        assertThat(medicalCaseAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        MedicalCaseAttachment testMedicalCaseAttachment = medicalCaseAttachmentList.get(medicalCaseAttachmentList.size() - 1);
        assertThat(testMedicalCaseAttachment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMedicalCaseAttachment.getLocation()).isEqualTo(DEFAULT_LOCATION);

        // Validate the MedicalCaseAttachment in Elasticsearch
        MedicalCaseAttachment medicalCaseAttachmentEs = medicalCaseAttachmentSearchRepository.findOne(testMedicalCaseAttachment.getId());
        assertThat(medicalCaseAttachmentEs).isEqualToComparingFieldByField(testMedicalCaseAttachment);
    }

    @Test
    @Transactional
    public void createMedicalCaseAttachmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseAttachmentRepository.findAll().size();

        // Create the MedicalCaseAttachment with an existing ID
        medicalCaseAttachment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalCaseAttachmentMockMvc.perform(post("/api/medical-case-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCaseAttachment)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MedicalCaseAttachment> medicalCaseAttachmentList = medicalCaseAttachmentRepository.findAll();
        assertThat(medicalCaseAttachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMedicalCaseAttachments() throws Exception {
        // Initialize the database
        medicalCaseAttachmentRepository.saveAndFlush(medicalCaseAttachment);

        // Get all the medicalCaseAttachmentList
        restMedicalCaseAttachmentMockMvc.perform(get("/api/medical-case-attachments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalCaseAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getMedicalCaseAttachment() throws Exception {
        // Initialize the database
        medicalCaseAttachmentRepository.saveAndFlush(medicalCaseAttachment);

        // Get the medicalCaseAttachment
        restMedicalCaseAttachmentMockMvc.perform(get("/api/medical-case-attachments/{id}", medicalCaseAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicalCaseAttachment.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicalCaseAttachment() throws Exception {
        // Get the medicalCaseAttachment
        restMedicalCaseAttachmentMockMvc.perform(get("/api/medical-case-attachments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicalCaseAttachment() throws Exception {
        // Initialize the database
        medicalCaseAttachmentService.save(medicalCaseAttachment);

        int databaseSizeBeforeUpdate = medicalCaseAttachmentRepository.findAll().size();

        // Update the medicalCaseAttachment
        MedicalCaseAttachment updatedMedicalCaseAttachment = medicalCaseAttachmentRepository.findOne(medicalCaseAttachment.getId());
        updatedMedicalCaseAttachment
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION);

        restMedicalCaseAttachmentMockMvc.perform(put("/api/medical-case-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicalCaseAttachment)))
            .andExpect(status().isOk());

        // Validate the MedicalCaseAttachment in the database
        List<MedicalCaseAttachment> medicalCaseAttachmentList = medicalCaseAttachmentRepository.findAll();
        assertThat(medicalCaseAttachmentList).hasSize(databaseSizeBeforeUpdate);
        MedicalCaseAttachment testMedicalCaseAttachment = medicalCaseAttachmentList.get(medicalCaseAttachmentList.size() - 1);
        assertThat(testMedicalCaseAttachment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMedicalCaseAttachment.getLocation()).isEqualTo(UPDATED_LOCATION);

        // Validate the MedicalCaseAttachment in Elasticsearch
        MedicalCaseAttachment medicalCaseAttachmentEs = medicalCaseAttachmentSearchRepository.findOne(testMedicalCaseAttachment.getId());
        assertThat(medicalCaseAttachmentEs).isEqualToComparingFieldByField(testMedicalCaseAttachment);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicalCaseAttachment() throws Exception {
        int databaseSizeBeforeUpdate = medicalCaseAttachmentRepository.findAll().size();

        // Create the MedicalCaseAttachment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicalCaseAttachmentMockMvc.perform(put("/api/medical-case-attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCaseAttachment)))
            .andExpect(status().isCreated());

        // Validate the MedicalCaseAttachment in the database
        List<MedicalCaseAttachment> medicalCaseAttachmentList = medicalCaseAttachmentRepository.findAll();
        assertThat(medicalCaseAttachmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedicalCaseAttachment() throws Exception {
        // Initialize the database
        medicalCaseAttachmentService.save(medicalCaseAttachment);

        int databaseSizeBeforeDelete = medicalCaseAttachmentRepository.findAll().size();

        // Get the medicalCaseAttachment
        restMedicalCaseAttachmentMockMvc.perform(delete("/api/medical-case-attachments/{id}", medicalCaseAttachment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean medicalCaseAttachmentExistsInEs = medicalCaseAttachmentSearchRepository.exists(medicalCaseAttachment.getId());
        assertThat(medicalCaseAttachmentExistsInEs).isFalse();

        // Validate the database is empty
        List<MedicalCaseAttachment> medicalCaseAttachmentList = medicalCaseAttachmentRepository.findAll();
        assertThat(medicalCaseAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMedicalCaseAttachment() throws Exception {
        // Initialize the database
        medicalCaseAttachmentService.save(medicalCaseAttachment);

        // Search the medicalCaseAttachment
        restMedicalCaseAttachmentMockMvc.perform(get("/api/_search/medical-case-attachments?query=id:" + medicalCaseAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalCaseAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalCaseAttachment.class);
        MedicalCaseAttachment medicalCaseAttachment1 = new MedicalCaseAttachment();
        medicalCaseAttachment1.setId(1L);
        MedicalCaseAttachment medicalCaseAttachment2 = new MedicalCaseAttachment();
        medicalCaseAttachment2.setId(medicalCaseAttachment1.getId());
        assertThat(medicalCaseAttachment1).isEqualTo(medicalCaseAttachment2);
        medicalCaseAttachment2.setId(2L);
        assertThat(medicalCaseAttachment1).isNotEqualTo(medicalCaseAttachment2);
        medicalCaseAttachment1.setId(null);
        assertThat(medicalCaseAttachment1).isNotEqualTo(medicalCaseAttachment2);
    }
}
