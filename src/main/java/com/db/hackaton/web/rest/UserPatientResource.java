package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.config.Constants;
import com.db.hackaton.domain.Patient;
import com.db.hackaton.domain.User;
import com.db.hackaton.repository.PatientRepository;
import com.db.hackaton.repository.UserRepository;
import com.db.hackaton.repository.search.UserSearchRepository;
import com.db.hackaton.security.AuthoritiesConstants;
import com.db.hackaton.service.MailService;
import com.db.hackaton.service.PatientService;
import com.db.hackaton.service.UserService;
import com.db.hackaton.service.dto.PatientDTO;
import com.db.hackaton.service.dto.UserDTO;
import com.db.hackaton.web.rest.util.HeaderUtil;
import com.db.hackaton.web.rest.util.PaginationUtil;
import com.db.hackaton.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
        PatientDTO patient = null;
        List<Patient> patientList = patientRepository.findByUserIsCurrentUser();
        if (patientList.size() > 0) {
            patient = new PatientDTO();
            patient.setFlag(1);
            patient.setCnp(patientList.get(0).getCnp());

        }
        return ResponseUtil.wrapOrNotFound(Optional.of(patient));
    }

}
