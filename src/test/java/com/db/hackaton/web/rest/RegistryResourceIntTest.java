package com.db.hackaton.web.rest;

import com.db.hackaton.HackatonApp;

import com.db.hackaton.config.ApplicationProperties;
import com.db.hackaton.domain.Registry;
import com.db.hackaton.repository.RegistryRepository;
import com.db.hackaton.service.MedicalCaseService;
import com.db.hackaton.service.RegistryService;
import com.db.hackaton.repository.search.RegistrySearchRepository;
import com.db.hackaton.service.dto.RegistryDTO;
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
 * Test class for the RegistryResource REST controller.
 *
 * @see RegistryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
public class RegistryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    private RegistryService registryService;

    @Autowired
    private RegistrySearchRepository registrySearchRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MedicalCaseService medicalCaseService;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistryMockMvc;

    private Registry registry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegistryResource registryResource = new RegistryResource(registryService, medicalCaseService, applicationProperties);
        this.restRegistryMockMvc = MockMvcBuilders.standaloneSetup(registryResource)
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
    public static Registry createEntity(EntityManager em) {
        Registry registry = new Registry()
            .name(DEFAULT_NAME)
            .desc(DEFAULT_DESC)
            .uuid(DEFAULT_UUID)
            .status(DEFAULT_STATUS);
        return registry;
    }

    @Before
    public void initTest() {
        registrySearchRepository.deleteAll();
        registry = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistry() throws Exception {
        int databaseSizeBeforeCreate = registryRepository.findAll().size();

        // Create the Registry
        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isCreated());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeCreate + 1);
        Registry testRegistry = registryList.get(registryList.size() - 1);
        assertThat(testRegistry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRegistry.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testRegistry.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testRegistry.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Registry in Elasticsearch
        Registry registryEs = registrySearchRepository.findOne(testRegistry.getId());
        assertThat(registryEs).isEqualToComparingFieldByField(testRegistry);
    }

    @Test
    @Transactional
    public void createRegistryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registryRepository.findAll().size();

        // Create the Registry with an existing ID
        registry.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = registryRepository.findAll().size();
        // set the field null
        registry.setName(null);

        // Create the Registry, which fails.

        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isBadRequest());

        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = registryRepository.findAll().size();
        // set the field null
        registry.setUuid(null);

        // Create the Registry, which fails.

        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isBadRequest());

        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = registryRepository.findAll().size();
        // set the field null
        registry.setStatus(null);

        // Create the Registry, which fails.

        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isBadRequest());

        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegistries() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList
        restRegistryMockMvc.perform(get("/api/registries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getRegistry() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get the registry
        restRegistryMockMvc.perform(get("/api/registries/{id}", registry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistry() throws Exception {
        // Get the registry
        restRegistryMockMvc.perform(get("/api/registries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistry() throws Exception {
        // Initialize the database
        registryService.save(RegistryDTO.build(registry));

        int databaseSizeBeforeUpdate = registryRepository.findAll().size();

        // Update the registry
        Registry updatedRegistry = registryRepository.findOne(registry.getId());
        updatedRegistry
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC)
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS);

        restRegistryMockMvc.perform(put("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistry)))
            .andExpect(status().isOk());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeUpdate);
        Registry testRegistry = registryList.get(registryList.size() - 1);
        assertThat(testRegistry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegistry.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testRegistry.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testRegistry.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Registry in Elasticsearch
        Registry registryEs = registrySearchRepository.findOne(testRegistry.getId());
        assertThat(registryEs).isEqualToComparingFieldByField(testRegistry);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistry() throws Exception {
        int databaseSizeBeforeUpdate = registryRepository.findAll().size();

        // Create the Registry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegistryMockMvc.perform(put("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isCreated());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegistry() throws Exception {
        // Initialize the database
        registryService.save(RegistryDTO.build(registry));

        int databaseSizeBeforeDelete = registryRepository.findAll().size();

        // Get the registry
        restRegistryMockMvc.perform(delete("/api/registries/{id}", registry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean registryExistsInEs = registrySearchRepository.exists(registry.getId());
        assertThat(registryExistsInEs).isFalse();

        // Validate the database is empty
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRegistry() throws Exception {
        // Initialize the database
        registryService.save(RegistryDTO.build(registry));

        // Search the registry
        restRegistryMockMvc.perform(get("/api/_search/registries?query=id:" + registry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registry.class);
        Registry registry1 = new Registry();
        registry1.setId(1L);
        Registry registry2 = new Registry();
        registry2.setId(registry1.getId());
        assertThat(registry1).isEqualTo(registry2);
        registry2.setId(2L);
        assertThat(registry1).isNotEqualTo(registry2);
        registry1.setId(null);
        assertThat(registry1).isNotEqualTo(registry2);
    }
}
