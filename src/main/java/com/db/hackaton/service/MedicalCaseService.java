package com.db.hackaton.service;

import com.db.hackaton.domain.*;
import com.db.hackaton.repository.*;
import com.db.hackaton.repository.search.MedicalCaseSearchRepository;
import com.db.hackaton.security.AuthoritiesConstants;
import com.db.hackaton.security.SecurityUtils;
import com.db.hackaton.service.dto.MedicalCaseDTO;
import com.db.hackaton.service.dto.MedicalCaseFieldDTO;
import com.db.hackaton.service.util.RandomUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MedicalCase.
 */
@Service
@Transactional
public class MedicalCaseService {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseService.class);

    private final MedicalCaseRepository medicalCaseRepository;
    private final MedicalCaseFieldRepository medicalCaseFieldRepository;

    private final MedicalCaseSearchRepository medicalCaseSearchRepository;
    private final PatientRepository patientRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    private final MailService mailService;

    public MedicalCaseService(MedicalCaseRepository medicalCaseRepository, MedicalCaseFieldRepository medicalCaseFieldRepository,
                              MedicalCaseSearchRepository medicalCaseSearchRepository, PatientRepository patientRepository,
                              UserGroupRepository userGroupRepository, MailService mailService, UserRepository userRepository) {
        this.medicalCaseRepository = medicalCaseRepository;
        this.medicalCaseFieldRepository = medicalCaseFieldRepository;
        this.medicalCaseSearchRepository = medicalCaseSearchRepository;
        this.patientRepository = patientRepository;
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * Save a medicalCase.
     *
     * @param medicalCaseDTO the entity to save
     * @return the persisted entity
     */
    @SuppressWarnings("Duplicates")
    public MedicalCaseDTO save(MedicalCaseDTO medicalCaseDTO) {

        log.debug("Request to save MedicalCase : {}", medicalCaseDTO);
        if (medicalCaseDTO.getId() != null) {
            medicalCaseRepository.save(Optional.of(medicalCaseDTO)
                .map(MedicalCaseDTO::build)
                .map(mc -> {
//                    mc.setStatus("SUPERSEDED");
                    medicalCaseSearchRepository.delete(mc);
                    return mc;
                }).get());
        } else //noinspection Duplicates
            if(medicalCaseDTO.getUuid() == null) {
                medicalCaseDTO.setUuid(UUID.randomUUID().toString());
            }

        if(CollectionUtils.isNotEmpty(patientRepository.findByUserIsCurrentUser())) {
            medicalCaseDTO.setPatientCnp(patientRepository.findByUserIsCurrentUser().get(0).getCnp());
        } else if(StringUtils.isBlank(medicalCaseDTO.getPatientCnp())) {
            // throw new Exception("no cnp");  // already verified in frontend
        }

        String medicalCaseStatus = "PENDING_APPROVAL";

        if(CollectionUtils.isNotEmpty(userGroupRepository.findByUserIsCurrentUser())) {
            List<UserGroup> currentUserGroupList = userGroupRepository.findByUserIsCurrentUser();
            Set<Authority> authorities = currentUserGroupList.get(0).getUser().getAuthorities();
            Authority authority = (Authority) authorities.toArray()[0];
            if(!authority.getName().equals(AuthoritiesConstants.PATIENT)) {
                medicalCaseStatus = "APPROVED";
            }
        }

        String finalMedicalCaseStatus = medicalCaseStatus;
        MedicalCase medicalCase = medicalCaseRepository.save(Optional.of(medicalCaseDTO)
            .map(MedicalCaseDTO::build)
            .map(mc -> mc.id(null)
                .status(finalMedicalCaseStatus))
            .get());

        medicalCaseDTO.setId(medicalCase.getId());

        medicalCaseDTO.getFields().stream()
            .map(MedicalCaseFieldDTO::build)
            .map(mc -> mc.id(null)
                .medicalCase(medicalCase))
            .forEach(medicalCaseFieldRepository::saveAndFlush);

        medicalCaseSearchRepository.save(medicalCase);
        //medicalCaseSearchRepository.save(result);
        return medicalCaseDTO;
    }

    /**
     * Get one medicalCase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MedicalCaseDTO findOne(Long id) {
        log.debug("Request to get MedicalCase : {}", id);
        MedicalCase medicalCase = medicalCaseRepository.findOne(id);
        return MedicalCaseDTO.build(medicalCase);
    }

    /**
     * Delete the  medicalCase by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalCase : {}", id);
        MedicalCaseDTO medicalCaseDTO = MedicalCaseDTO.build(medicalCaseRepository.findOne(id));
        medicalCaseRepository.delete(id);
        medicalCaseSearchRepository.delete(id);

    }

    private Map<String, String> createFields(MedicalCase medicalCase, List<MedicalCase> cases, List<Long> fields) {
        Map<String, String> row = new HashMap<>();

        row.put("CNP", medicalCase.getPatientCnp() != null ? medicalCase.getPatientCnp() : "N/A");
        row.put("Name", medicalCase.getName());
        row.put("Status", medicalCase.getStatus());
        row.put("id", medicalCase.getId().toString());
        row.put("approval_by", medicalCase.getApprovalBy());
        row.put("approval_date", medicalCase.getApproval_date());

        for (MedicalCaseField field : medicalCase.getFields()) {
            if (field.getField() != null && fields.contains(field.getField().getId())) {
                row.put(field.getField().getName(), field.getValue());
            }
        }

        return row;
    }


    private List<Map<String, String>> findAllCases(List<MedicalCase> cases, List<Long> fields) {
        List<Map<String, String>> result = new ArrayList<>();

        for (MedicalCase medicalCase : cases) {
            result.add(createFields(medicalCase, cases, fields));
        }

        return result;
    }


    private List<Map<String, String>> findCasesByPatient(List<MedicalCase> cases, List<Long> fields, Patient patient) {
        List<Map<String, String>> result = new ArrayList<>();

        for (MedicalCase medicalCase : cases) {
            if (medicalCase.getPatientCnp().equals(patient.getCnp())) {
                result.add(createFields(medicalCase, cases, fields));
            }
        }

        return result;
    }


    private List<Map<String, String>> findCasesByGroup(List<MedicalCase> cases, List<Long> fields, List<UserGroup> currentUserGroupList) {
        List<Map<String, String>> result = new ArrayList<>();

        for(UserGroup userGroup : currentUserGroupList) {
            for (Patient patient : patientRepository.findAllByGroupId(userGroup.getGroup().getId())) {
                result.addAll(findCasesByPatient(cases, fields, patient));
            }
        }

        return result;
    }


    @Transactional(readOnly = true)
    public List<Map<String, String>> findCases(String registryUuid, List<Long> fields) throws Exception {
        List<MedicalCase> cases = medicalCaseRepository.findByLatestModifiedDateAndRegistryUuid(registryUuid);

        if (CollectionUtils.isEmpty(userGroupRepository.findByUserIsCurrentUser())) {
            if (CollectionUtils.isEmpty(patientRepository.findByUserIsCurrentUser())) {
                throw new Exception("No users logged in!");
            } else {
                return findCasesByPatient(cases, fields, patientRepository.findByUserIsCurrentUser().get(0));
            }
        } else {
            List<UserGroup> currentUserGroupList = userGroupRepository.findByUserIsCurrentUser();
            Set<Authority> authorities = currentUserGroupList.get(0).getUser().getAuthorities();
            Authority authority = (Authority) authorities.toArray()[0];
            if (authority.getName().equals(AuthoritiesConstants.ADMIN)) {
                return findAllCases(cases, fields);
            } else if (authority.getName().equals(AuthoritiesConstants.DOCTOR) || authority.getName().equals(AuthoritiesConstants.PROVIDER)) {
                return findCasesByGroup(cases, fields, currentUserGroupList);
            } else if (authority.getName().equals(AuthoritiesConstants.PATIENT)){
                Patient patient = patientRepository.findByUserIsCurrentUser().get(0);
                return findCasesByPatient(cases, fields, patient);
            }
            else {
                throw new Exception("Wrong authority");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<MedicalCase> findAllLatest(String registryUuid) {
        return medicalCaseRepository.findByStatusAndRegistryUuid("LATEST", registryUuid);
    }

    @Transactional(readOnly = true)
    public List<MedicalCase> findByRegistryUuid(String registryUuid) {
        return medicalCaseRepository.findByRegistryUuid(registryUuid);
    }

    @Transactional
    public MedicalCaseDTO update(MedicalCaseDTO medicalCaseDTO) {
        //MedicalCase medicalCase = medicalCaseRepository.findByCNP(medicalCaseDTO.getFields().get(2).getValue());
        String cnp = medicalCaseDTO.getFields().stream() // MedicalCaseFieldDTO stream
                                                .filter(mcf -> mcf.getField().getName().equals("CNP"))
                                                .map(mcf -> mcf.getValue())
                                                .collect(Collectors.toList())
                                                .get(0);
        MedicalCase medicalCase = medicalCaseRepository.findByCNP(cnp);
        Set<MedicalCaseField> managedFields = medicalCase.getFields();
        List<MedicalCaseFieldDTO> dtoFields = medicalCaseDTO.getFields();
        int i = 0;

        for (MedicalCaseField medicalCaseField : managedFields) {
            medicalCaseField.value(dtoFields.get(i).getValue())
                            .medicalCase(medicalCase);
            i++;
        }
        medicalCase.status("LATEST")
                   .setPatientCnp(patientRepository.findByUserIsCurrentUser().get(0).getCnp());
        //when transaction is completed, all the changes made to the managed medicalCase should be flushed
        return MedicalCaseDTO.build(medicalCase);
    }

    @Transactional
    public MedicalCaseDTO updateStatus(MedicalCaseDTO medicalCaseDTO) {
        log.debug("Reguest to update the status for a medical case: {}", medicalCaseDTO);

        // send email to patient if the doctor approve the medical case
        if (medicalCaseDTO.getStatus().equals("APPROVED")) {
            Patient patient = patientRepository.findFirstByCnp(medicalCaseDTO.getPatientCnp());
            User user = userRepository.findOne(patient.getUser().getId());
            user.setActivationKey(RandomUtil.generateActivationKey());
            mailService.sendApproveMedicalCaseEmail(user);
        }

        // change status, approval_by, approval_date
        String medicalCaseStatus = medicalCaseDTO.getStatus();
        medicalCaseRepository.setStatusForMedicalCase(medicalCaseStatus, medicalCaseDTO.getId());
        medicalCaseRepository.setAprovalBy(SecurityUtils.getCurrentUserLogin(), medicalCaseDTO.getId());
        String approval_date = Instant.now().toString();
        medicalCaseRepository.setApprovalDate(approval_date, medicalCaseDTO.getId());

        return medicalCaseDTO;
    }

    @Transactional
	public MedicalCaseDTO findByRegistryIdAndCNP(Long registryId, String cnp) {
		List<MedicalCase> medicalCaseList = medicalCaseRepository
				.findByLatestModifiedDateAndRegistryIdAndCnp(registryId, cnp);

        return MedicalCaseDTO.build(medicalCaseList.get(0));
    }

    public PDDocument exportDocuments(List<MedicalCase> cases) throws IOException  {

        // Create a document and add a page to it
        PDDocument document = new PDDocument();

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA_BOLD;

        for (MedicalCase mc : cases) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(100, 700);

            contentStream.showText("CNP: " + mc.getPatientCnp());
            contentStream.newLine();
            contentStream.showText("Medical case description: " + mc.getName());
            contentStream.newLine();
            contentStream.showText("Medical case status: " + mc.getStatus());
            contentStream.newLine();
            contentStream.showText("Last modified date:" + mc.getLastModifiedDate());
            contentStream.newLine();

            for (MedicalCaseField field : mc.getFields()) {
                contentStream.showText(field.getField().getName() + ": "+ field.getValue());
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
        }

        return document;
    }

    public void saveDocument(PDDocument document, String pathName,
                             HttpServletResponse response) throws IOException  {

        File file = new File(pathName);
        document.save(file);
        document.close();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(FileUtils.readFileToByteArray(file));

            //byte[] -> InputStream
            ByteArrayInputStream inStream = new ByteArrayInputStream(bos.toByteArray());

            org.apache.commons.io.IOUtils.copy(inStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream.", ex);
            throw new RuntimeException("IOError writing file to output stream");

        }
    }
}
