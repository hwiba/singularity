package singularity.party.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import singularity.SingularityApplication;
import singularity.party.domain.Party;
import singularity.party.repository.PartyRepository;
import singularity.user.domain.User;
import singularity.user.service.UserService;

/**
 * Created by scala on 2015. 8. 24..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@WebAppConfiguration
@Transactional
public class PartyServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(PartyServiceTest.class);
	
	@Resource
	private PartyRepository partyRepository;
	@Resource
	private PartyService partyService;
	@Resource
	private UserService userService;

    @Rule
    public ExpectedException expectedExcetption = ExpectedException.none();

    private final String testEmail = "email@email.com";
    private final User user = new User(testEmail, "1234qwer", new Date(), "testName", "nullImage");

    @Test
    public void create와_findByUserId가_정상동작하는지() {
    	User dbUser = userService.create(this.user);
    	partyService.create("testParty1", dbUser.getId(), Party.Openness.COMMUNITY);
    	partyService.create("testParty2", dbUser.getId(), Party.Openness.COMMUNITY);
    	partyService.create("testParty3", dbUser.getId(), Party.Openness.COMMUNITY);
    	
    	assertEquals(3, partyService.findAllByUserId(dbUser.getId()).size());
    	
    }
}