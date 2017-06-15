package com.db.hackaton.web.rest;

import com.db.hackaton.HackatonApp;

import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.Patient;
import com.db.hackaton.repository.MedicalCaseRepository;
import com.db.hackaton.service.MedicalCaseService;
import com.db.hackaton.repository.search.MedicalCaseSearchRepository;
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
 * Test class for the MedicalCaseResource REST controller.
 *
 * @see MedicalCaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
public class MedicalCaseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private MedicalCaseRepository medicalCaseRepository;

    @Autowired
    private MedicalCaseService medicalCaseService;

    @Autowired
    private MedicalCaseSearchRepository medicalCaseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicalCaseMockMvc;

    private MedicalCase medicalCase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedicalCaseResource medicalCaseResource = new MedicalCaseResource(medicalCaseService);
        this.restMedicalCaseMockMvc = MockMvcBuilders.standaloneSetup(medicalCaseResource)
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
    public static MedicalCase createEntity(EntityManager em) {
        MedicalCase medicalCase = new MedicalCase()
            .name(DEFAULT_NAME)
            .uuid(DEFAULT_UUID)
            .status(DEFAULT_STATUS);
        // Add required entity
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        medicalCase.setPatient(patient);
        return medicalCase;
    }

    @Before
    public void initTest() {
        medicalCaseSearchRepository.deleteAll();
        medicalCase = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicalCase() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseRepository.findAll().size();

        // Create the MedicalCase
        restMedicalCaseMockMvc.perform(post("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isCreated());

        // Validate the MedicalCase in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeCreate + 1);
        MedicalCase testMedicalCase = medicalCaseList.get(medicalCaseList.size() - 1);
        assertThat(testMedicalCase.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedicalCase.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testMedicalCase.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the MedicalCase in Elasticsearch
        MedicalCase medicalCaseEs = medicalCaseSearchRepository.findOne(testMedicalCase.getId());
        assertThat(medicalCaseEs).isEqualToComparingFieldByField(testMedicalCase);
    }

    @Test
    @Transactional
    public void createMedicalCaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseRepository.findAll().size();

        // Create the MedicalCase with an existing ID
        medicalCase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalCaseMockMvc.perform(post("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicalCaseRepository.findAll().size();
        // set the field null
        medicalCase.setName(null);

        // Create the MedicalCase, which fails.

        restMedicalCaseMockMvc.perform(post("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isBadRequest());

        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicalCaseRepository.findAll().size();
        // set the field null
        medicalCase.setUuid(null);

        // Create the MedicalCase, which fails.

        restMedicalCaseMockMvc.perform(post("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isBadRequest());

        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicalCases() throws Exception {
        // Initialize the database
        medicalCaseRepository.saveAndFlush(medicalCase);

        // Get all the medicalCaseList
        restMedicalCaseMockMvc.perform(get("/api/medical-cases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getMedicalCase() throws Exception {
        // Initialize the database
        medicalCaseRepository.saveAndFlush(medicalCase);

        // Get the medicalCase
        restMedicalCaseMockMvc.perform(get("/api/medical-cases/{id}", medicalCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicalCase.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicalCase() throws Exception {
        // Get the medicalCase
        restMedicalCaseMockMvc.perform(get("/api/medical-cases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicalCase() throws Exception {
        // Initialize the database
        medicalCaseService.save(medicalCase);

        int databaseSizeBeforeUpdate = medicalCaseRepository.findAll().size();

        // Update the medicalCase
        MedicalCase updatedMedicalCase = medicalCaseRepository.findOne(medicalCase.getId());
        updatedMedicalCase
            .name(UPDATED_NAME)
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS);

        restMedicalCaseMockMvc.perform(put("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicalCase)))
            .andExpect(status().isOk());

        // Validate the MedicalCase in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeUpdate);
        MedicalCase testMedicalCase = medicalCaseList.get(medicalCaseList.size() - 1);
        assertThat(testMedicalCase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedicalCase.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMedicalCase.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the MedicalCase in Elasticsearch
        MedicalCase medicalCaseEs = medicalCaseSearchRepository.findOne(testMedicalCase.getId());
        assertThat(medicalCaseEs).isEqualToComparingFieldByField(testMedicalCase);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicalCase() throws Exception {
        int databaseSizeBeforeUpdate = medicalCaseRepository.findAll().size();

        // Create the MedicalCase

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicalCaseMockMvc.perform(put("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isCreated());

        // Validate the MedicalCase in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedicalCase() throws Exception {
        // Initialize the database
        medicalCaseService.save(medicalCase);

        int databaseSizeBeforeDelete = medicalCaseRepository.findAll().size();

        // Get the medicalCase
        restMedicalCaseMockMvc.perform(delete("/api/medical-cases/{id}", medicalCase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean medicalCaseExistsInEs = medicalCaseSearchRepository.exists(medicalCase.getId());
        assertThat(medicalCaseExistsInEs).isFalse();

        // Validate the database is empty
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMedicalCase() throws Exception {
        // Initialize the database
        medicalCaseService.save(medicalCase);

        // Search the medicalCase
        restMedicalCaseMockMvc.perform(get("/api/_search/medical-cases?query=id:" + medicalCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalCase.class);
        MedicalCase medicalCase1 = new MedicalCase();
        medicalCase1.setId(1L);
        MedicalCase medicalCase2 = new MedicalCase();
        medicalCase2.setId(medicalCase1.getId());
        assertThat(medicalCase1).isEqualTo(medicalCase2);
        medicalCase2.setId(2L);
        assertThat(medicalCase1).isNotEqualTo(medicalCase2);
        medicalCase1.setId(null);
        assertThat(medicalCase1).isNotEqualTo(medicalCase2);
    }
}
