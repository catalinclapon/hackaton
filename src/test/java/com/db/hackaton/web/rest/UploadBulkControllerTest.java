package com.db.hackaton.web.rest;

import com.db.hackaton.config.ApplicationProperties;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by Irina-Mihaela on 6/16/2017.
 */
public class UploadBulkControllerTest {

    @Autowired
    ApplicationProperties applicationProperties;

    @Test
    public void saveBulkData() throws Exception {
        UploadBulkController controller = new UploadBulkController(applicationProperties);
        controller.saveBulkData("C:/Users/Irina-Mihaela/Desktop/Alzheimer's Prevention Registry.xlsx");
    }

}
