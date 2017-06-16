package com.db.hackaton.service;

import com.db.hackaton.config.ApplicationProperties;
import com.db.hackaton.domain.Field;
import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.domain.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

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

    public UploadBulkService(ApplicationProperties applicationProperties, RegistryService registryService , MedicalCaseService medicalCaseService, PatientService patientService, FieldService fieldService, MedicalCaseFieldService medicalCaseFieldService) {
        this.applicationProperties = applicationProperties;
        this.medicalCaseService = medicalCaseService;
        this.patientService = patientService;
        this.fieldService = fieldService;
        this.medicalCaseFieldService = medicalCaseFieldService;
        this.registryService = registryService;
    }

    public void save(Map<Pair<String, String>, String> categoryToFieldToValue, String registerUuid) {
        // Build Medical Case
        MedicalCase medicalCase = new MedicalCase();
        medicalCase.setName(UUID.randomUUID().toString());
        medicalCase.setUuid(UUID.randomUUID().toString());

        String cnp = "";
        // Find Patient
        for(Map.Entry<Pair<String,String>, String> entry : categoryToFieldToValue.entrySet()) {
            if(entry.getKey().getSecond().toUpperCase().equals("CNP")) {
                cnp = entry.getValue();
                log.info("Got cnp {}", cnp);
            }
        }

        Patient patient = patientService.findOneByCnp(cnp.replaceAll("\"",""));
        log.info("Got patient {}", patient);
        if(patient == null) {
            // Admin
            log.info("Using ADMIN");
            patient = new Patient();
            patient.setId(1L);
        }

        // Populate Fields
        for(Map.Entry<Pair<String,String>,String> entry : categoryToFieldToValue.entrySet()) {
            Pair<String,String> key = entry.getKey();
            String value = entry.getValue();
            log.info("Got getKey: {}", key);
            log.info("Got getValue: {}", value);

            Field field = fieldService.findFirstByName(key.getSecond());
            System.out.println(field);

            medicalCase.setPatient(patient);
            medicalCase.setRegistryUuid(registerUuid);

            log.info("Got pre-save medicalCase {}", medicalCase);
            medicalCaseService.save(medicalCase);

            MedicalCaseField medicalCaseField = new MedicalCaseField();
            medicalCaseField.setField(field);
            medicalCaseField.setMedicalCase(medicalCase);
            medicalCaseField.setValue(value);
            medicalCaseFieldService.save(medicalCaseField);
        }
    }
}
