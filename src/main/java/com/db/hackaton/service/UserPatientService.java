package com.db.hackaton.service;

import com.db.hackaton.config.Constants;
import com.db.hackaton.domain.Authority;
import com.db.hackaton.domain.Patient;
import com.db.hackaton.domain.User;
import com.db.hackaton.repository.AuthorityRepository;
import com.db.hackaton.repository.PatientRepository;
import com.db.hackaton.repository.PersistentTokenRepository;
import com.db.hackaton.repository.UserRepository;
import com.db.hackaton.repository.search.PatientSearchRepository;
import com.db.hackaton.repository.search.UserSearchRepository;
import com.db.hackaton.security.AuthoritiesConstants;
import com.db.hackaton.security.SecurityUtils;
import com.db.hackaton.service.dto.UserDTO;
import com.db.hackaton.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users and patients.
 */
@Service
@Transactional
public class UserPatientService {

    private final Logger log = LoggerFactory.getLogger(UserPatientService.class);

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final PatientRepository patientRepository;

    public UserPatientService(UserRepository userRepository, UserSearchRepository userSearchRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.patientRepository = patientRepository;
    }

    public void updateUserAndPatient(String firstName, String lastName, String email, String langKey, boolean isPatient, String cnp) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            userSearchRepository.save(user);

            List<Patient> listPatients = patientRepository.findByUserIsCurrentUser();
            if (listPatients.size() > 0) {
                Patient patient = listPatients.get(0);
                patient.setCnp(cnp);
                patientRepository.save(patient);
            } else {
                Patient patient = new Patient();
                patient.setUser(user);
                patient.setCnp(cnp);
                patient.setFirstName(firstName);
                patient.setLastName(lastName);
                patientRepository.save(Collections.singletonList(patient));
            }

            log.debug("Changed Information for User and Patient: {}", user);
        });
    }


}
