package com.db.hackaton.web.rest;

import com.db.hackaton.HackatonApp;

import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.repository.MedicalCaseFieldRepository;
import com.db.hackaton.repository.search.MedicalCaseFieldSearchRepository;
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
 * Test class for the MedicalCaseFieldResource REST controller.
 *
 * @see MedicalCaseFieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
public class MedicalCaseFieldResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private MedicalCaseFieldRepository medicalCaseFieldRepository;

    @Autowired
    private MedicalCaseFieldSearchRepository medicalCaseFieldSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicalCaseFieldMockMvc;

    private MedicalCaseField medicalCaseField;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedicalCaseFieldResource medicalCaseFieldResource = new MedicalCaseFieldResource(medicalCaseFieldRepository, medicalCaseFieldSearchRepository);
        this.restMedicalCaseFieldMockMvc = MockMvcBuilders.standaloneSetup(medicalCaseFieldResource)
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
    public static MedicalCaseField createEntity(EntityManager em) {
        MedicalCaseField medicalCaseField = new MedicalCaseField()
            .value(DEFAULT_VALUE);
        return medicalCaseField;
    }

    @Before
    public void initTest() {
        medicalCaseFieldSearchRepository.deleteAll();
        medicalCaseField = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicalCaseField() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseFieldRepository.findAll().size();

        // Create the MedicalCaseField
        restMedicalCaseFieldMockMvc.perform(post("/api/medical-case-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCaseField)))
            .andExpect(status().isCreated());

        // Validate the MedicalCaseField in the database
        List<MedicalCaseField> medicalCaseFieldList = medicalCaseFieldRepository.findAll();
        assertThat(medicalCaseFieldList).hasSize(databaseSizeBeforeCreate + 1);
        MedicalCaseField testMedicalCaseField = medicalCaseFieldList.get(medicalCaseFieldList.size() - 1);
        assertThat(testMedicalCaseField.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the MedicalCaseField in Elasticsearch
        MedicalCaseField medicalCaseFieldEs = medicalCaseFieldSearchRepository.findOne(testMedicalCaseField.getId());
        assertThat(medicalCaseFieldEs).isEqualToComparingFieldByField(testMedicalCaseField);
    }

    @Test
    @Transactional
    public void createMedicalCaseFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseFieldRepository.findAll().size();

        // Create the MedicalCaseField with an existing ID
        medicalCaseField.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalCaseFieldMockMvc.perform(post("/api/medical-case-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCaseField)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MedicalCaseField> medicalCaseFieldList = medicalCaseFieldRepository.findAll();
        assertThat(medicalCaseFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMedicalCaseFields() throws Exception {
        // Initialize the database
        medicalCaseFieldRepository.saveAndFlush(medicalCaseField);

        // Get all the medicalCaseFieldList
        restMedicalCaseFieldMockMvc.perform(get("/api/medical-case-fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalCaseField.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getMedicalCaseField() throws Exception {
        // Initialize the database
        medicalCaseFieldRepository.saveAndFlush(medicalCaseField);

        // Get the medicalCaseField
        restMedicalCaseFieldMockMvc.perform(get("/api/medical-case-fields/{id}", medicalCaseField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicalCaseField.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicalCaseField() throws Exception {
        // Get the medicalCaseField
        restMedicalCaseFieldMockMvc.perform(get("/api/medical-case-fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicalCaseField() throws Exception {
        // Initialize the database
        medicalCaseFieldRepository.saveAndFlush(medicalCaseField);
        medicalCaseFieldSearchRepository.save(medicalCaseField);
        int databaseSizeBeforeUpdate = medicalCaseFieldRepository.findAll().size();

        // Update the medicalCaseField
        MedicalCaseField updatedMedicalCaseField = medicalCaseFieldRepository.findOne(medicalCaseField.getId());
        updatedMedicalCaseField
            .value(UPDATED_VALUE);

        restMedicalCaseFieldMockMvc.perform(put("/api/medical-case-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicalCaseField)))
            .andExpect(status().isOk());

        // Validate the MedicalCaseField in the database
        List<MedicalCaseField> medicalCaseFieldList = medicalCaseFieldRepository.findAll();
        assertThat(medicalCaseFieldList).hasSize(databaseSizeBeforeUpdate);
        MedicalCaseField testMedicalCaseField = medicalCaseFieldList.get(medicalCaseFieldList.size() - 1);
        assertThat(testMedicalCaseField.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the MedicalCaseField in Elasticsearch
        MedicalCaseField medicalCaseFieldEs = medicalCaseFieldSearchRepository.findOne(testMedicalCaseField.getId());
        assertThat(medicalCaseFieldEs).isEqualToComparingFieldByField(testMedicalCaseField);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicalCaseField() throws Exception {
        int databaseSizeBeforeUpdate = medicalCaseFieldRepository.findAll().size();

        // Create the MedicalCaseField

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicalCaseFieldMockMvc.perform(put("/api/medical-case-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCaseField)))
            .andExpect(status().isCreated());

        // Validate the MedicalCaseField in the database
        List<MedicalCaseField> medicalCaseFieldList = medicalCaseFieldRepository.findAll();
        assertThat(medicalCaseFieldList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedicalCaseField() throws Exception {
        // Initialize the database
        medicalCaseFieldRepository.saveAndFlush(medicalCaseField);
        medicalCaseFieldSearchRepository.save(medicalCaseField);
        int databaseSizeBeforeDelete = medicalCaseFieldRepository.findAll().size();

        // Get the medicalCaseField
        restMedicalCaseFieldMockMvc.perform(delete("/api/medical-case-fields/{id}", medicalCaseField.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean medicalCaseFieldExistsInEs = medicalCaseFieldSearchRepository.exists(medicalCaseField.getId());
        assertThat(medicalCaseFieldExistsInEs).isFalse();

        // Validate the database is empty
        List<MedicalCaseField> medicalCaseFieldList = medicalCaseFieldRepository.findAll();
        assertThat(medicalCaseFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMedicalCaseField() throws Exception {
        // Initialize the database
        medicalCaseFieldRepository.saveAndFlush(medicalCaseField);
        medicalCaseFieldSearchRepository.save(medicalCaseField);

        // Search the medicalCaseField
        restMedicalCaseFieldMockMvc.perform(get("/api/_search/medical-case-fields?query=id:" + medicalCaseField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalCaseField.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalCaseField.class);
        MedicalCaseField medicalCaseField1 = new MedicalCaseField();
        medicalCaseField1.setId(1L);
        MedicalCaseField medicalCaseField2 = new MedicalCaseField();
        medicalCaseField2.setId(medicalCaseField1.getId());
        assertThat(medicalCaseField1).isEqualTo(medicalCaseField2);
        medicalCaseField2.setId(2L);
        assertThat(medicalCaseField1).isNotEqualTo(medicalCaseField2);
        medicalCaseField1.setId(null);
        assertThat(medicalCaseField1).isNotEqualTo(medicalCaseField2);
    }
}
