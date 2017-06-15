package com.db.hackaton.web.rest;

import com.db.hackaton.HackatonApp;

import com.db.hackaton.domain.Field;
import com.db.hackaton.repository.FieldRepository;
import com.db.hackaton.service.FieldService;
import com.db.hackaton.repository.search.FieldSearchRepository;
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
 * Test class for the FieldResource REST controller.
 *
 * @see FieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
public class FieldResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MIN = 1;
    private static final Integer UPDATED_MIN = 2;

    private static final String DEFAULT_REQUIRED = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRED = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX = 1;
    private static final Integer UPDATED_MAX = 2;

    private static final String DEFAULT_EXT_VALIDATION = "AAAAAAAAAA";
    private static final String UPDATED_EXT_VALIDATION = "BBBBBBBBBB";

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private FieldSearchRepository fieldSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFieldMockMvc;

    private Field field;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FieldResource fieldResource = new FieldResource(fieldService);
        this.restFieldMockMvc = MockMvcBuilders.standaloneSetup(fieldResource)
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
    public static Field createEntity(EntityManager em) {
        Field field = new Field()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC)
            .type(DEFAULT_TYPE)
            .min(DEFAULT_MIN)
            .required(DEFAULT_REQUIRED)
            .max(DEFAULT_MAX)
            .extValidation(DEFAULT_EXT_VALIDATION);
        return field;
    }

    @Before
    public void initTest() {
        fieldSearchRepository.deleteAll();
        field = createEntity(em);
    }

    @Test
    @Transactional
    public void createField() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // Create the Field
        restFieldMockMvc.perform(post("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testField.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testField.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testField.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testField.getRequired()).isEqualTo(DEFAULT_REQUIRED);
        assertThat(testField.getMax()).isEqualTo(DEFAULT_MAX);
        assertThat(testField.getExtValidation()).isEqualTo(DEFAULT_EXT_VALIDATION);

        // Validate the Field in Elasticsearch
        Field fieldEs = fieldSearchRepository.findOne(testField.getId());
        assertThat(fieldEs).isEqualToComparingFieldByField(testField);
    }

    @Test
    @Transactional
    public void createFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // Create the Field with an existing ID
        field.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldMockMvc.perform(post("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fieldRepository.findAll().size();
        // set the field null
        field.setName(null);

        // Create the Field, which fails.

        restFieldMockMvc.perform(post("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isBadRequest());

        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fieldRepository.findAll().size();
        // set the field null
        field.setType(null);

        // Create the Field, which fails.

        restFieldMockMvc.perform(post("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isBadRequest());

        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFields() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList
        restFieldMockMvc.perform(get("/api/fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.toString())))
            .andExpect(jsonPath("$.[*].max").value(hasItem(DEFAULT_MAX)))
            .andExpect(jsonPath("$.[*].extValidation").value(hasItem(DEFAULT_EXT_VALIDATION.toString())));
    }

    @Test
    @Transactional
    public void getField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(field.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.min").value(DEFAULT_MIN))
            .andExpect(jsonPath("$.required").value(DEFAULT_REQUIRED.toString()))
            .andExpect(jsonPath("$.max").value(DEFAULT_MAX))
            .andExpect(jsonPath("$.extValidation").value(DEFAULT_EXT_VALIDATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingField() throws Exception {
        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateField() throws Exception {
        // Initialize the database
        fieldService.save(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field
        Field updatedField = fieldRepository.findOne(field.getId());
        updatedField
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC)
            .type(UPDATED_TYPE)
            .min(UPDATED_MIN)
            .required(UPDATED_REQUIRED)
            .max(UPDATED_MAX)
            .extValidation(UPDATED_EXT_VALIDATION);

        restFieldMockMvc.perform(put("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedField)))
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testField.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testField.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testField.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testField.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(testField.getMax()).isEqualTo(UPDATED_MAX);
        assertThat(testField.getExtValidation()).isEqualTo(UPDATED_EXT_VALIDATION);

        // Validate the Field in Elasticsearch
        Field fieldEs = fieldSearchRepository.findOne(testField.getId());
        assertThat(fieldEs).isEqualToComparingFieldByField(testField);
    }

    @Test
    @Transactional
    public void updateNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Create the Field

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFieldMockMvc.perform(put("/api/fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteField() throws Exception {
        // Initialize the database
        fieldService.save(field);

        int databaseSizeBeforeDelete = fieldRepository.findAll().size();

        // Get the field
        restFieldMockMvc.perform(delete("/api/fields/{id}", field.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean fieldExistsInEs = fieldSearchRepository.exists(field.getId());
        assertThat(fieldExistsInEs).isFalse();

        // Validate the database is empty
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchField() throws Exception {
        // Initialize the database
        fieldService.save(field);

        // Search the field
        restFieldMockMvc.perform(get("/api/_search/fields?query=id:" + field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].min").value(hasItem(DEFAULT_MIN)))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.toString())))
            .andExpect(jsonPath("$.[*].max").value(hasItem(DEFAULT_MAX)))
            .andExpect(jsonPath("$.[*].extValidation").value(hasItem(DEFAULT_EXT_VALIDATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Field.class);
        Field field1 = new Field();
        field1.setId(1L);
        Field field2 = new Field();
        field2.setId(field1.getId());
        assertThat(field1).isEqualTo(field2);
        field2.setId(2L);
        assertThat(field1).isNotEqualTo(field2);
        field1.setId(null);
        assertThat(field1).isNotEqualTo(field2);
    }
}
