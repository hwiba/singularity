package me.singularityfor.user.service;

import lombok.val;
import me.singularityfor.SingularityApplication;
import me.singularityfor.user.domain.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Order on 2015. 10. 22..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@Transactional
@WebAppConfiguration
public class UserServiceTest {

    @Resource private UserService userService;

    @Rule public ExpectedException expectedExcetption = ExpectedException.none();

    private final User user = new User("test@test.com", "testName", "1234qwer", new Date());

    @Test
    public void testFindOne_회원_가입이_되어_있지_않을_때 () throws Exception {
        expectedExcetption.expect(NullPointerException.class);
        expectedExcetption.expectMessage("회원가입이 되어 있지 않습니다");
        userService.findOne(-1L);
    }

    @Test
    public void testCreate_password가_암호화_되는_지 () throws Exception {
        String pw = user.getPassword();
        val hashedPasswordUser = userService.create(user.copy());
        assertNotEquals(hashedPasswordUser.getPassword(), pw);
    }
}