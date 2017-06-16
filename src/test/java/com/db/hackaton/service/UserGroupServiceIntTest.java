package com.db.hackaton.service;

import com.db.hackaton.HackatonApp;
import com.db.hackaton.repository.UserGroupRepository;
import com.db.hackaton.repository.search.UserGroupSearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackatonApp.class)
@Transactional
public class UserGroupServiceIntTest {

    @Autowired
    private UserGroupSearchRepository userGroupSearchRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserGroupService userGroupService;

    @Test
    public void test() {
        assertThat(userGroupRepository.findByUserIdAndGroupId(1L, 1L)).isNotNull();
        assertThat(userGroupRepository.findByUserIdAndGroupId(2L, 1L)).isNotNull();
        assertThat(userGroupRepository.findByUserIdAndGroupId(3L, 2L)).isNotNull();
        assertThat(userGroupRepository.findByUserIdAndGroupId(4L, 3L)).isNotNull();
    }

}
