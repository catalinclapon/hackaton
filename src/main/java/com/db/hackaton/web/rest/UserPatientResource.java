package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.domain.Patient;
import com.db.hackaton.repository.PatientRepository;
import com.db.hackaton.service.UserService;
import com.db.hackaton.service.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Get patient info for the user</p>
 */
@RestController
@RequestMapping("/api")
public class UserPatientResource {

    private final Logger log = LoggerFactory.getLogger(UserPatientResource.class);

    private final UserService userService;

    private final PatientRepository patientRepository;

    public UserPatientResource(UserService userService, PatientRepository patientRepository) {
        this.userService = userService;
        this.patientRepository = patientRepository;
    }

    /**
     * GET  /userpatient/
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/userpatient")
    @Timed
    public ResponseEntity<PatientDTO> getPatientForCurrentUser() {
        PatientDTO patient = new PatientDTO();
        patient.setFlag(0);
        List<Patient> patientList = patientRepository.findByUserIsCurrentUser();
        if (patientList.size() > 0) {
            patient.setFlag(1);
            patient.setCnp(patientList.get(0).getCnp());
        }
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }
}
