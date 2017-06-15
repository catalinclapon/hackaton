package com.db.hackaton.web.rest;

import com.db.hackaton.HackatonApp;

import com.db.hackaton.domain.RegistryField;
import com.db.hackaton.repository.RegistryFieldRepository;
import com.db.hackaton.service.RegistryFieldService;
import com.db.hackaton.repository.search.RegistryFieldSearchRepository;
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
 * Test class for the RegistryFieldResource REST controller.
 *
 * @see RegistryFieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
public class RegistryFieldResourceIntTest {

    private static final Integer DEFAULT_ORDER = 222;
    private static final Integer UPDATED_ORDER = 333;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private RegistryFieldRepository registryFieldRepository;

    @Autowired
    private RegistryFieldService registryFieldService;

    @Autowired
    private RegistryFieldSearchRepository registryFieldSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistryFieldMockMvc;

    private RegistryField registryField;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegistryFieldResource registryFieldResource = new RegistryFieldResource(registryFieldService);
        this.restRegistryFieldMockMvc = MockMvcBuilders.standaloneSetup(registryFieldResource)
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
    public static RegistryField createEntity(EntityManager em) {
        RegistryField registryField = new RegistryField()
            .order(DEFAULT_ORDER)
            .category(DEFAULT_CATEGORY);
        return registryField;
    }

    @Before
    public void initTest() {
        registryFieldSearchRepository.deleteAll();
        registryField = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistryField() throws Exception {
        int databaseSizeBeforeCreate = registryFieldRepository.findAll().size();

        // Create the RegistryField
        restRegistryFieldMockMvc.perform(post("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryField)))
            .andExpect(status().isCreated());

        // Validate the RegistryField in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeCreate + 1);
        RegistryField testRegistryField = registryFieldList.get(registryFieldList.size() - 1);
        assertThat(testRegistryField.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testRegistryField.getCategory()).isEqualTo(DEFAULT_CATEGORY);

        // Validate the RegistryField in Elasticsearch
        RegistryField registryFieldEs = registryFieldSearchRepository.findOne(testRegistryField.getId());
        assertThat(registryFieldEs).isEqualToComparingFieldByField(testRegistryField);
    }

    @Test
    @Transactional
    public void createRegistryFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registryFieldRepository.findAll().size();

        // Create the RegistryField with an existing ID
        registryField.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistryFieldMockMvc.perform(post("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryField)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegistryFields() throws Exception {
        // Initialize the database
        registryFieldRepository.saveAndFlush(registryField);

        // Get all the registryFieldList
        restRegistryFieldMockMvc.perform(get("/api/registry-fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registryField.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getRegistryField() throws Exception {
        // Initialize the database
        registryFieldRepository.saveAndFlush(registryField);

        // Get the registryField
        restRegistryFieldMockMvc.perform(get("/api/registry-fields/{id}", registryField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registryField.getId().intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistryField() throws Exception {
        // Get the registryField
        restRegistryFieldMockMvc.perform(get("/api/registry-fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistryField() throws Exception {
        // Initialize the database
        registryFieldService.save(registryField);

        int databaseSizeBeforeUpdate = registryFieldRepository.findAll().size();

        // Update the registryField
        RegistryField updatedRegistryField = registryFieldRepository.findOne(registryField.getId());
        updatedRegistryField
            .order(UPDATED_ORDER)
            .category(UPDATED_CATEGORY);

        restRegistryFieldMockMvc.perform(put("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistryField)))
            .andExpect(status().isOk());

        // Validate the RegistryField in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeUpdate);
        RegistryField testRegistryField = registryFieldList.get(registryFieldList.size() - 1);
        assertThat(testRegistryField.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testRegistryField.getCategory()).isEqualTo(UPDATED_CATEGORY);

        // Validate the RegistryField in Elasticsearch
        RegistryField registryFieldEs = registryFieldSearchRepository.findOne(testRegistryField.getId());
        assertThat(registryFieldEs).isEqualToComparingFieldByField(testRegistryField);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistryField() throws Exception {
        int databaseSizeBeforeUpdate = registryFieldRepository.findAll().size();

        // Create the RegistryField

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegistryFieldMockMvc.perform(put("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryField)))
            .andExpect(status().isCreated());

        // Validate the RegistryField in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegistryField() throws Exception {
        // Initialize the database
        registryFieldService.save(registryField);

        int databaseSizeBeforeDelete = registryFieldRepository.findAll().size();

        // Get the registryField
        restRegistryFieldMockMvc.perform(delete("/api/registry-fields/{id}", registryField.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean registryFieldExistsInEs = registryFieldSearchRepository.exists(registryField.getId());
        assertThat(registryFieldExistsInEs).isFalse();

        // Validate the database is empty
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRegistryField() throws Exception {
        // Initialize the database
        registryFieldService.save(registryField);

        // Search the registryField
        restRegistryFieldMockMvc.perform(get("/api/_search/registry-fields?query=id:" + registryField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registryField.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistryField.class);
        RegistryField registryField1 = new RegistryField();
        registryField1.setId(1L);
        RegistryField registryField2 = new RegistryField();
        registryField2.setId(registryField1.getId());
        assertThat(registryField1).isEqualTo(registryField2);
        registryField2.setId(2L);
        assertThat(registryField1).isNotEqualTo(registryField2);
        registryField1.setId(null);
        assertThat(registryField1).isNotEqualTo(registryField2);
    }
}
