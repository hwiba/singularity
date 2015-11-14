package me.singularityfor.group.domain;

import me.singularityfor.SingularityApplication;
import me.singularityfor.group.service.GroupService;
import me.singularityfor.user.domain.User;
import me.singularityfor.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by hyva on 2015. 11. 14..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@Transactional
@WebAppConfiguration
public class GroupTest {

    private static final Logger logger = LoggerFactory.getLogger(GroupTest.class);

    @Resource private UserService userService;
    @Resource private GroupService groupService;

    private User user;
    private Group group;

    @Before
    public void setUp() throws Exception {
        this.user = userService.create(new User("test@test.com", "testUserName", "test1234", new Date()));
        this.group = groupService.create(new Group("testGroup", this.user, new Date()));
    }

    @Test
    public void isCaptain_캡틴이_맞을_때() {
        assertTrue(group.isCaptain(user));
    }

    @Test
    public void hasMember() throws Exception {
        User[] users = new User[] {
                userService.create(new User("1test@test.com", "1testUserName", "test1234", new Date()))
                , userService.create(new User("2test@test.com", "2testUserName", "test1234", new Date()))
                , userService.create(new User("3test@test.com", "3testUserName", "test1234", new Date()))
        };
        group.addMember(users);
        assertTrue(group.hasMember(user));
        assertTrue(group.hasMember(users));
    }
}