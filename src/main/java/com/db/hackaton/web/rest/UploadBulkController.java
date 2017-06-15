package com.db.hackaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackaton.config.ApplicationProperties;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Irina-Mihaela on 6/15/2017.
 */
@RestController
@RequestMapping("/api")
public class UploadBulkController {
    private ApplicationProperties applicationProperties;

    public UploadBulkController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("/uploadBulk")
    @Timed
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes;
        if (!file.isEmpty()) {
            bytes = file.getBytes();
            String filePath = applicationProperties.getLocalStoragePath() + "/" + file.getOriginalFilename();
            FileUtils.writeByteArrayToFile(new File(filePath), file.getBytes());
        }
        System.out.println(String.format("receive %s", file.getOriginalFilename()));
    }

}
