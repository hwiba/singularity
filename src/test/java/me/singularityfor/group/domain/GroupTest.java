package me.singularityfor.group.domain;

import me.singularityfor.SingularityApplication;
import me.singularityfor.user.domain.User;
import me.singularityfor.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Order on 2015. 11. 14..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@Transactional
@WebAppConfiguration
public class GroupTest {

    @Resource
    private UserService userService;

    private User user;
    private Group group;

    @Before
    public void setUp() throws Exception {
        this.user = userService.create(new User("test@test.com", "testUserName", "test1234", new Date()));
        this.group = new Group("testGroup", this.user, new Date());
    }

    @Test
    public void isCaptain_캡틴이_맞을_때() {
        assertTrue(group.isCaptain(user));
    }
}