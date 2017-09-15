package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.config.ApplicationProperties;
import com.db.hackaton.service.UploadBulkService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Irina-Mihaela on 6/15/2017.
 */
@RestController
@RequestMapping("/api")
public class UploadBulkController {
    private final Logger log = LoggerFactory.getLogger(UploadBulkController.class);

    private UploadBulkService uploadBulkService;
    private ApplicationProperties applicationProperties;

    public UploadBulkController(ApplicationProperties applicationProperties, UploadBulkService uploadBulkService) {
        this.uploadBulkService = uploadBulkService;
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

    @GetMapping("/registries/saveBulk")
    public void saveBulkData(@RequestParam String filePath, @RequestParam String registerUuid) throws IOException {
        System.out.println(registerUuid);
        // Store column index to category_field reference
        Map<Integer, Pair<String, String>> indexToCategoryToField = new HashMap<>();
        // Store values
        Map<Pair<String,String>, String> categoryToFieldToValue = new HashMap<>();

        log.info("Trying to save bulk data from file {}", filePath);

        FileInputStream inputStream = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            if (isFirst) {
                isFirst = false;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if(cell.toString().isEmpty()) {
                        continue;
                    }
                    log.info("Got cell: {}", cell);
                    String categoryName = cell.getStringCellValue().split("_")[0];
                    log.info("Got categoryName {}", categoryName);
                    String fieldName = cell.getStringCellValue().split("_")[1];
                    log.info("Got fieldName {}", fieldName);
                    log.info("Got cell.getColumnIndex() {} with value {}", cell.getColumnIndex(), Pair.of(categoryName, fieldName));
                    indexToCategoryToField.put(cell.getColumnIndex(), Pair.of(categoryName, fieldName));
                }
            } else {
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    String value = null;
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            log.info(cell.getStringCellValue());
                            value = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            log.info("" + cell.getBooleanCellValue());
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            log.info("" + cell.getNumericCellValue());
                            value = String.valueOf(cell.getNumericCellValue());
                            break;
                    }
                    categoryToFieldToValue.put(indexToCategoryToField.get(cell.getColumnIndex()), value);
                }
                log.info("\n");
            }
            uploadBulkService.save(categoryToFieldToValue, registerUuid);
        }

        workbook.close();
        inputStream.close();

    }

}
