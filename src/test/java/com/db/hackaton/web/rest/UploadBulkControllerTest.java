package com.db.hackaton.web.rest;

import com.db.hackaton.HackatonApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Irina-Mihaela on 6/16/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
public class UploadBulkControllerTest {

    @Autowired
    private UploadBulkController uploadBulkController;

    @Test
    @Transactional
    public void saveBulkData() throws Exception {
        uploadBulkController.saveBulkData("C:\\Temp\\uploads\\TEST.xlsx", "x");
    }
}
