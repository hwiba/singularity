package singularity.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import singularity.SingularityApplication;
import singularity.user.domain.User;
import singularity.user.exception.ExistedUserException;
import singularity.user.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@WebAppConfiguration
@Transactional
public class UserServiceTest {

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserService userService;

    @Rule
    public ExpectedException expectedExcetption = ExpectedException.none();

    private final String testEmail = "email@email.com";
    private final User user = new User(testEmail, "1234qwer", new Date(), "testName", "nullImage");

    @Test
    public void testCreate_유저가_새로운_이메일주소일_때() throws Exception {
        userService.create(user);
        assertEquals(user, userRepository.findOneByEmail(testEmail));
    }

    @Test
    public void testCreate_이미_가입한_유저_이메일로_재차_가입요청이_들어올_때() throws Exception {
        expectedExcetption.expect(ExistedUserException.class);
        expectedExcetption.expectMessage("이미 가입된 E-mail 주소입니다.");
        userService.create(user);
        userService.create(user);
    }

    @Test
    public void testAccept_가입한_유저가_승인을_요청할_때_승인() {
        User dbUser = userService.create(user);
        userService.accept(dbUser);
        assertTrue(dbUser.isAccept());
    }

    @Test
    public void testAccept_가입하지_않은_유저가_가입승인을_요청할_때() {
        expectedExcetption.expect(IllegalArgumentException.class);
        expectedExcetption.expectMessage("가입하지 않은 유저입니다.");
        User cloneUser = this.user;
        userService.accept(cloneUser);
    }

    @Test
    public void testAccept_승인되었거나_탈퇴한_회원이_승인을_요청할_때() {
        User dbUser = userService.create(user);
        userService.accept(dbUser);
        expectedExcetption.expect(IllegalArgumentException.class);
        expectedExcetption.expectMessage("승인 대상이 아닌 유저입니다.");
        User cloneUser = this.user;
        userService.accept(cloneUser);
    }

}