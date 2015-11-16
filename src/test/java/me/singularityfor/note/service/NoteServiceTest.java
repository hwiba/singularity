package me.singularityfor.note.service;

import lombok.val;
import me.singularityfor.SingularityApplication;
import me.singularityfor.note.domain.Note;
import me.singularityfor.group.domain.Group;
import me.singularityfor.group.service.GroupService;
import me.singularityfor.user.domain.User;
import me.singularityfor.user.service.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
public class NoteServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceTest.class);

    @Rule public ExpectedException expectedExcetption = ExpectedException.none();

    @Resource private UserService userService;
    @Resource private GroupService groupService;
    @Resource private NoteService noteService;

    private User user;
    private Group group;
    private Note note;

    @Before
    public void setUp() throws Exception {
        user = userService.create(new User("test@test.com", "testUserName", "test1234", new Date()));
        group = groupService.create(new Group("testGroup", this.user, new Date()));
        note = noteService.create(new Note(user, group, "#제목\n* 테스트 노트 내용\n* 12345 abcdef"));
    }

    @Test
    public void create() {
        assertEquals(note, noteService.findOne(note.getId()));
    }

    @Test
    public void create_권한이_없는_요청 () throws Exception {
        expectedExcetption.expect(IllegalAccessException.class);
        expectedExcetption.expectMessage("권한이 없는 요청");
        val user2 = userService.create(new User("2test@test.com", "2testUserName", "test1234", new Date()));
        noteService.create(new Note(user2, group, "테스트 노트 내용"));
    }

}