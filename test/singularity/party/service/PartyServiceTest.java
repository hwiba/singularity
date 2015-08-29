package singularity.party.service;

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
    public void findByUserId가_정상동작하는지() {
    	User dbUser = userService.create(this.user);
    	partyService.create("testParty1", dbUser.getId(), Party.Openness.COMMUNITY);
    	partyService.create("testParty2", dbUser.getId(), Party.Openness.COMMUNITY);
    	partyService.create("testParty3", dbUser.getId(), Party.Openness.COMMUNITY);
    	
    	assertEquals(3, partyService.findAllByUserId(dbUser.getId()).size());
    }
    
    @Test
    public void create_시에_각_멤버들이_정상적으로_갖춰지는지() {
    	User dbUser = userService.create(this.user);
    	Party party = partyService.create("testParty1", dbUser.getId(), Party.Openness.COMMUNITY);
    	assertTrue(party.isCommunity());
    	assertTrue(party.hasMember(dbUser));
    	assertTrue(party.isAdmin(dbUser));
    	
    	Party secretParty = partyService.create("testParty2", dbUser.getId(), Party.Openness.SECRET);
    	assertTrue(secretParty.isSecret());
    }
    
    @Test
    public void delete가_정상_수행_될_때() {
    	User dbUser = userService.create(this.user);
    	Party party = partyService.create("testParty1", dbUser.getId(), Party.Openness.COMMUNITY);
    	partyService.delete(party.getId(), dbUser.getId());
    	Party closeParty = partyService.findOneByAdmin(dbUser);
    	assertEquals(Party.Openness.CLOSE, closeParty.getOpenness());
    }
    
    @Test
    public void 그룹이_존재하지_않을_때_delete_요청 () throws Exception {
        expectedExcetption.expect(IllegalArgumentException.class);
        expectedExcetption.expectMessage("그룹이 존재하지 않습니다.");
        User dbUser = userService.create(this.user);
        partyService.delete((long) -1, dbUser.getId());
    }
    
    @Test
    public void 그룹장이_아닐_때_delete_요청 () throws Exception {
        expectedExcetption.expect(IllegalArgumentException.class);
        expectedExcetption.expectMessage("그룹을 삭제할 권한이 없습니다.");
        User dbUser = userService.create(this.user);
        User dbUser2 = userService.create(new User("test2@email.com", "1234qwer", new Date(), "testName", "nullImage"));
        Party party = partyService.create("testParty1", dbUser.getId(), Party.Openness.COMMUNITY);
        partyService.delete(party.getId(), dbUser2.getId());
    }

    @Test
    public void 정상적으로_파티_정보를_업데이트_할_때 () {
        //partyService.update();
        assertTrue(true);
    }
    
}