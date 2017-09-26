package com.db.hackaton.service;

import com.db.hackaton.domain.Authority;
import com.db.hackaton.domain.Patient;
import com.db.hackaton.repository.PatientRepository;
import com.db.hackaton.repository.UserRepository;
import com.db.hackaton.repository.search.UserSearchRepository;
import com.db.hackaton.security.AuthoritiesConstants;
import com.db.hackaton.security.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import com.db.hackaton.service.util.RandomUtil;


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

    private final MailService mailService;

    public UserPatientService(UserRepository userRepository, UserSearchRepository userSearchRepository,
                              PatientRepository patientRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.patientRepository = patientRepository;
        this.mailService = mailService;
    }

    public void updateUserAndPatient(String firstName, String lastName, String email, String langKey, boolean isPatient, String cnp) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            boolean isEmailChange = !user.getEmail().equals(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            userSearchRepository.save(user);

            if (!StringUtils.isBlank(cnp)) {
                Set<Authority> authorities = new HashSet<>();
                Authority authority = new Authority();
                authority.setName(AuthoritiesConstants.PATIENT);
                authorities.add(authority);
                user.setAuthorities(authorities);
            }
            userSearchRepository.save(user);
            List<Patient> listPatients = patientRepository.findByUserIsCurrentUser();
            if (listPatients.size() > 0) {
                Patient patient = listPatients.get(0);

                if(patient.getCnp() != null) {
                    log.debug("You can't change your cnp!");
                    return;
                }

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

            if (isEmailChange) {
                user.setActivationKey(RandomUtil.generateActivationKey());
                mailService.sendActivationEmail(user);

            }
            log.debug("Changed Information for User and Patient: {}", user);
        });
    }


}
