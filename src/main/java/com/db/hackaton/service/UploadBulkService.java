package com.db.hackaton.service;

import com.db.hackaton.config.ApplicationProperties;
import com.db.hackaton.domain.Field;
import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.domain.Patient;
import com.db.hackaton.repository.FieldRepository;
import com.db.hackaton.repository.UserGroupRepository;
import com.db.hackaton.repository.UserRepository;
import com.db.hackaton.security.SecurityUtils;
import com.db.hackaton.service.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

@Service
@Transactional
public class UploadBulkService {
    private final Logger log = LoggerFactory.getLogger(UploadBulkService.class);

    private PatientService patientService;
    private MedicalCaseService medicalCaseService;
    private ApplicationProperties applicationProperties;
    private FieldService fieldService;
    private MedicalCaseFieldService medicalCaseFieldService;
    private RegistryService registryService;
    private FieldRepository fieldRepository;
    private UserGroupRepository userGroupRepository;

    public UploadBulkService(ApplicationProperties applicationProperties, RegistryService registryService,
                             MedicalCaseService medicalCaseService, PatientService patientService,
                             FieldService fieldService, MedicalCaseFieldService medicalCaseFieldService,
                             FieldRepository fieldRepository, UserGroupRepository userGroupRepository) {
        this.applicationProperties = applicationProperties;
        this.medicalCaseService = medicalCaseService;
        this.patientService = patientService;
        this.fieldService = fieldService;
        this.medicalCaseFieldService = medicalCaseFieldService;
        this.registryService = registryService;
        this.fieldRepository = fieldRepository;
        this.userGroupRepository = userGroupRepository;
    }


    @Transactional
    public boolean sameFieldsCheck(String registryUuid, Map<Integer, Pair<String, String>> indexToCategoryToField) {

        RegistryDTO registry = registryService.findOneByStatusAndRegistryUuid("ACTIVATED", registryUuid);

        if (registry.getFields().size() != indexToCategoryToField.size() - 2) {
            return false;
        }
        for (RegistryFieldDTO registryFields : registry.getFields()) {
            Pair field = Pair.of(registryFields.getCategory(), registryFields.getField().getName());
            if (!indexToCategoryToField.containsValue(field)) {
                return false;
            }
        }

        return true;
    }

    public void save(Map<Pair<String, String>, String> categoryToFieldToValue, String registerUuid) {
        // Build Medical Case
        MedicalCaseDTO medicalCase = new MedicalCaseDTO();
        medicalCase.setUuid(UUID.randomUUID().toString());

        Map<String, Long> fieldMap = registryService.getFieldMapByUuid(registerUuid);

        String cnp = "";
        String medicalCaseName = "";
        // Find Patient
        for (Map.Entry<Pair<String, String>, String> entry : categoryToFieldToValue.entrySet()) {
            if (entry.getKey().getSecond().toUpperCase().equals("CNP")) {
                cnp = Optional.ofNullable(entry.getValue())
                    .map(val -> val.replaceAll("\"", ""))
                    .orElse(null);
                log.info("Got cnp {}", cnp);
            } else if (entry.getKey().getSecond().equals("Name")) {
                medicalCaseName = Optional.ofNullable(entry.getValue())
                    .map(val -> val.replaceAll("\"", ""))
                    .orElse(null);
                log.info("Got medicalCaseName {}", medicalCaseName);
            }
        }
        if(StringUtils.isBlank(cnp)){
            log.warn("Cannot save Medical case without CNP {}", categoryToFieldToValue.toString());
            return;
        }

        if(StringUtils.isBlank(medicalCaseName)){
            log.warn("Cannot save Medical case without a description {}", categoryToFieldToValue.toString());
            return;
        }

        Patient patient = Optional.ofNullable(patientService.findOneByCnp(cnp))
            .orElse(new Patient()
                .cnp(cnp));
        log.info("Got patient {}", patient);

        medicalCase.setPatientCnp(patient.getCnp());
        medicalCase.setRegistryUuid(registerUuid);
        medicalCase.setName(medicalCaseName);
        medicalCase.setApproval_by(SecurityUtils.getCurrentUserLogin());
        medicalCase.setApproval_date(Instant.now().toString());
        medicalCase.setFields(new ArrayList<>());

        categoryToFieldToValue.entrySet().forEach(addFieldValueToMedicalCase(medicalCase, fieldMap));

        if(medicalCase.getFields().size() != fieldMap.size()) {
            log.warn("Wrong fields!");
            return;
        }

        medicalCaseService.save(medicalCase);
    }

    private Consumer<? super Map.Entry<Pair<String, String>, String>> addFieldValueToMedicalCase(MedicalCaseDTO medicalCaseDTO, Map<String, Long> fieldMap) {
        return entry -> {
            String fieldKey = entry.getKey().getFirst() + "_" + entry.getKey().getSecond();
            Optional<Long> fieldId = Optional.ofNullable(fieldMap.get(fieldKey));
            if (!fieldId.isPresent()) {
                log.warn("No field for key {} in registry {}", fieldKey, medicalCaseDTO.getRegistryUuid());
                return;
            }

            Field field = fieldRepository.findById(fieldId.get());

            Pair<String, String> key = entry.getKey();
            String value = entry.getValue();
            log.info("Got getKey: {}", key);
            log.info("Got getValue: {}", value);

            medicalCaseDTO.getFields()
                .add(MedicalCaseFieldDTO.builder()
                    .id(fieldId.get())
                    .field(FieldDTO.build(field))
                    .value(value)
                    .build());
        };
    }
}
