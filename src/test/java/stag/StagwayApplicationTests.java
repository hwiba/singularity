package stag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import singularity.SingularityApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@WebAppConfiguration
public class StagwayApplicationTests {

	@Test
	public void contextLoads() {
	}

}
