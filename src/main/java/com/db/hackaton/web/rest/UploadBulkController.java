package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.config.ApplicationProperties;
import com.db.hackaton.domain.MedicalCase;
import com.db.hackaton.domain.MedicalCaseField;
import com.db.hackaton.domain.Patient;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Irina-Mihaela on 6/15/2017.
 */
@RestController
@RequestMapping("/api")
public class UploadBulkController {
    private ApplicationProperties applicationProperties;


    private final Logger log = LoggerFactory.getLogger(UploadBulkController.class);

    public UploadBulkController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("/uploadBulk")
    @Timed
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes;
        String filePath = applicationProperties.getLocalStoragePath() + "/" + UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!file.isEmpty()) {
            FileUtils.writeByteArrayToFile(new File(filePath), file.getBytes());
        }
        System.out.println(String.format("receive %s", file.getOriginalFilename()));
        return new ResponseEntity<String>("{ \"path\" : \"" + filePath + "\" }", HttpStatus.OK);
    }


    @PostMapping("/registries/saveBulk")
    public void saveBulkData(@RequestBody String filePath) throws IOException {
        log.info("Trying to save bulk data from file {}", filePath);

        FileInputStream inputStream = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Patient patient = new Patient();
            MedicalCase medicalCase = new MedicalCase();
            Set<MedicalCaseField> fields = new HashSet<>();
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            if (isFirst) {
                isFirst = false;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String categoryName = cell.getStringCellValue().split("_")[0];
                    String fieldName = cell.getStringCellValue().split("_")[1];
                }
            } else {
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            log.info(cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            log.info("" + cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            log.info("" + cell.getNumericCellValue());
                            break;
                    }
                    log.info(" \t ");
                }
                log.info("\n");
            }
        }

        workbook.close();
        inputStream.close();

    }

}
