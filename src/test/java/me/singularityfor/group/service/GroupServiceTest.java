package me.singularityfor.group.service;

import lombok.val;
import me.singularityfor.SingularityApplication;
import me.singularityfor.group.domain.Group;
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
public class GroupServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceTest.class);

    @Resource private UserService userService;
    @Resource private GroupService groupService;

    private User user;
    private Group group;

    @Before
    public void setUp() throws Exception {
        this.user = userService.create(new User("test@test.com", "testUserName", "test1234", new Date()));
        this.group = new Group("testGroup", this.user, new Date());
    }

    @Test
    public void createGroup() throws Exception {
        val repositoryGroup = groupService.createGroup(group);
        logger.warn("{}", repositoryGroup);
        assertEquals(group.getName(), repositoryGroup.getName());
        assertEquals(user, repositoryGroup.getCaptain());
    }

}