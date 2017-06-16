package com.db.hackaton.service;

import com.db.hackaton.HackatonApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
@Transactional
public class UserBulkServiceIntTest {

    @Autowired
    private UploadBulkService uploadBulkService;

    @Test
    public void test() {
        Map<Pair<String,String>, String> categoryToFieldToValue = new HashMap<>();
        categoryToFieldToValue.put(Pair.of("CAT_1","CNP"),"1870220170020");
        categoryToFieldToValue.put(Pair.of("CAT_1","GENDER"),"MALE");
        categoryToFieldToValue.put(Pair.of("CAT_1","ADDRESS"),"BUCURESTI");

        uploadBulkService.save(categoryToFieldToValue, "1");
    }
}
