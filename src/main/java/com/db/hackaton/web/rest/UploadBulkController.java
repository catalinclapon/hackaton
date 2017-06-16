package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.config.ApplicationProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public void saveBulkData(@RequestBody String filePath) {
        log.info("Trying to save bulk data from file {}", filePath);
    }

}
