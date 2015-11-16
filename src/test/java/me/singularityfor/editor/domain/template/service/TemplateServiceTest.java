package me.singularityfor.editor.domain.template.service;

import lombok.val;
import me.singularityfor.SingularityApplication;
import me.singularityfor.editor.domain.template.domain.Template;
import me.singularityfor.group.domain.Group;
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
 * Created by hyva on 2015. 11. 13..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@Transactional
@WebAppConfiguration
public class TemplateServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceTest.class);

    @Resource private TemplateService templateService;
    @Resource private UserService userService;
    @Resource private GroupService groupService;

    private User user;
    private Template template;
    private Group group;

    @Before
    public void setUp() throws Exception {
        user = userService.create(new User("test@test.com", "testUserName", "test1234", new Date()));
        group = groupService.create(new Group("testGroup", this.user, new Date()));
        template = new Template(user, group, "serviceTestTemplate", "#공지사항");
    }

    @Test
    public void testCreate_같은_명칭의_템플릿이_그룹에_존재하지_않을_때 () throws Exception {
        val repositoryTemplate = templateService.create(template);
        assertEquals(template.getAuthor(), repositoryTemplate.getAuthor());
        assertEquals(template.getGroup(), repositoryTemplate.getGroup());
        assertEquals(template.getForm(), repositoryTemplate.getForm());
        assertEquals(template.getName(), repositoryTemplate.getName());
    }

    @Test
    public void testCreate_같은_명칭의_템플릿이_그룹에_존재할_때 () {
        val template1 = this.template.copyAddSuffix("_1");
        val template2 = this.template.copyAddSuffix("_2");
        val template3 = this.template.copyAddSuffix("_3");
        templateService.create(this.template);
        templateService.create(template1);
        templateService.create(template2);
        templateService.create(template3);
        val repositoryTemplate = templateService.create(this.template);
        assertEquals("serviceTestTemplate_4", repositoryTemplate.getName());
    }
}